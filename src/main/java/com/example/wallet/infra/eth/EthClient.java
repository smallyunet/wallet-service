package com.example.wallet.infra.eth;

import com.example.wallet.config.AppProperties;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EthClient {
    private static final Logger logger = LoggerFactory.getLogger(EthClient.class);
    private final AppProperties appProperties;

    public EthClient(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String getBalance(String network, String address) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return "0x" + ethGetBalance.getBalance().toString(16);  // Convert to hex string manually
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch ETH balance", e);
        }
    }
    
    /**
     * Get gas fee suggestions for transactions
     * This method combines on-chain data with estimated confirmation times
     */
    public GasFeeSuggestion getGasFeeSuggestion(String network) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        try {
            // Get current gas price from the network
            EthGasPrice gasPrice = web3j.ethGasPrice().send();
            BigInteger currentGasPrice = gasPrice.getGasPrice();
            
            // Convert wei to gwei for better readability
            BigDecimal baseGasPriceGwei = Convert.fromWei(new BigDecimal(currentGasPrice), Convert.Unit.GWEI)
                                              .setScale(2, RoundingMode.HALF_UP);
            
            // Create gas fee suggestion object with different priority levels
            GasFeeSuggestion suggestion = new GasFeeSuggestion();
            
            // Set base fee
            suggestion.setBaseFee(baseGasPriceGwei.toString());
            
            // Set slow fee (80% of base)
            GasFeeSuggestion.GasFeeDetail slow = new GasFeeSuggestion.GasFeeDetail();
            slow.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(1.0)).toString());
            slow.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.1)).toString());
            slow.setEstimatedSeconds(120); // ~2 minutes
            suggestion.setSlow(slow);
            
            // Set average fee (100% of base)
            GasFeeSuggestion.GasFeeDetail average = new GasFeeSuggestion.GasFeeDetail();
            average.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(1.2)).toString());
            average.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.2)).toString());
            average.setEstimatedSeconds(60); // ~1 minute
            suggestion.setAverage(average);
            
            // Set fast fee (120% of base)
            GasFeeSuggestion.GasFeeDetail fast = new GasFeeSuggestion.GasFeeDetail();
            fast.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(1.5)).toString());
            fast.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.3)).toString());
            fast.setEstimatedSeconds(30); // ~30 seconds
            suggestion.setFast(fast);
            
            // Set fastest fee (150% of base)
            GasFeeSuggestion.GasFeeDetail fastest = new GasFeeSuggestion.GasFeeDetail();
            fastest.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(2.0)).toString());
            fastest.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.5)).toString());
            fastest.setEstimatedSeconds(15); // ~15 seconds
            suggestion.setFastest(fastest);
            
            return suggestion;
            
        } catch (IOException e) {
            logger.error("Failed to fetch gas price", e);
            throw new RuntimeException("Failed to fetch gas price: " + e.getMessage(), e);
        }
    }
}

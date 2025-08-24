package com.example.wallet;

import java.util.Map;
import java.util.HashMap;
import com.example.wallet.config.IAppProperties;
import com.example.wallet.infra.btc.IBtcClient;
import com.example.wallet.infra.eth.IEthClient;
import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.NonceResponse;
import com.example.wallet.domain.eth.TokenBalanceResponse;
import com.example.wallet.domain.eth.TokenTransferRequest;
import com.example.wallet.domain.eth.TokenTransferResponse;
import com.example.wallet.domain.eth.TransactionStatusResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 测试配置类，提供测试所需的Bean模拟实现
 */
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public IEthClient ethClient() {
        return new IEthClient() {
            @Override
            public String getBalance(String network, String address) {
                return "0.0";
            }
            
            @Override
            public TokenBalanceResponse getTokenBalance(String network, String contractAddress, String ownerAddress) {
                return new TokenBalanceResponse();
            }
            
            @Override
            public NonceResponse getNonce(String network, String address) {
                return new NonceResponse();
            }
            
            @Override
            public GasFeeSuggestion getGasFees(String network) {
                return new GasFeeSuggestion();
            }
            
            @Override
            public GasFeeSuggestion getGasFeeSuggestion(String network) {
                return new GasFeeSuggestion();
            }
            
            @Override
            public EthTransferResponse sendTransaction(String network, EthTransferRequest request) {
                return new EthTransferResponse();
            }
            
            @Override
            public EthTransferResponse sendTransaction(String network, String privateKey, String toAddress, String amount, String gasPrice, String gasLimit) {
                return new EthTransferResponse();
            }
            
            @Override
            public TokenTransferResponse sendTokenTransaction(String network, TokenTransferRequest request) {
                return new TokenTransferResponse();
            }
            
            @Override
            public TokenTransferResponse sendTokenTransaction(String network, String privateKey, String contractAddress, String toAddress, String amount, String gasPrice, String gasLimit) {
                return new TokenTransferResponse();
            }
            
            @Override
            public TransactionStatusResponse getTransactionStatus(String network, String txHash) {
                return new TransactionStatusResponse();
            }
        };
    }
    
    @Bean
    @Primary
    public IBtcClient btcClient() {
        // 返回一个简单的模拟实现
        return new IBtcClient() {
            @Override
            public String getBalance(String network, String address) {
                return "0.0";
            }
            
            @Override
            public String getTransactionStatus(String network, String txHash) {
                return "confirmed";
            }
            
            @Override
            public String getUtxos(String network, String address) {
                return "[]"; // 返回一个空的UTXO列表
            }
        };
    }
    
    @Bean
    @Primary
    public IAppProperties appProperties() {
        return new IAppProperties() {
            @Override
            public IAppProperties.Rpc getRpc() {
                return new IAppProperties.Rpc() {
                    @Override
                    public Map<String, String> getEth() {
                        return Map.of(
                            "mainnet", "https://eth-mainnet.test",
                            "sepolia", "https://eth-sepolia.test"
                        );
                    }
                    
                    @Override
                    public Map<String, String> getBtc() {
                        return Map.of(
                            "mainnet", "https://blockstream.test/api",
                            "testnet", "https://blockstream.test/testnet/api"
                        );
                    }
                };
            }
        };
    }
}

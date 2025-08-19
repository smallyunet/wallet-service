package com.example.wallet.infra.eth;


import com.example.wallet.config.AppProperties;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import java.io.IOException;

@Component
public class EthClient {
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
            return Numeric.toHexStringWithPrefixSafe(ethGetBalance.getBalance());
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch ETH balance", e);
        }
    }
}

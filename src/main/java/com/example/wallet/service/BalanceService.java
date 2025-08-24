package com.example.wallet.service;

import com.example.wallet.config.AppProperties;
import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.infra.eth.EthClient;
import com.example.wallet.infra.btc.BtcClient;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private final EthClient ethClient;
    private final BtcClient btcClient;
    private final AppProperties props;

    public BalanceService(EthClient ethClient, BtcClient btcClient, AppProperties props) {
        this.ethClient = ethClient;
        this.btcClient = btcClient;
        this.props = props;
    }

    public BalanceResponse ethBalance(String network, String address) {
        String wei = ethClient.getBalance(network, address);
        return new BalanceResponse("ETH", network, address, wei);
    }

    public BalanceResponse btcBalance(String network, String address) {
        String sats = btcClient.getBalance(network, address);
        return new BalanceResponse("BTC", network, address, sats);
    }

    public String effectiveEthRpc(String network) {
        String url = props.getRpc().getEth().get(network);
        if (url == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        return url;
    }

    public Object effectiveBtcRpc() {
        return props.getRpc().getBtc();
    }
    
    /**
     * Get ERC-20 token balance for a specific address
     *
     * @param network the Ethereum network
     * @param tokenAddress the token contract address
     * @param walletAddress the wallet address to check balance for
     * @return the token balance response with token details
     */
    public com.example.wallet.domain.eth.TokenBalanceResponse getTokenBalance(String network, String tokenAddress, String walletAddress) {
        return ethClient.getTokenBalance(network, tokenAddress, walletAddress);
    }
}

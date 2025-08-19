package com.example.wallet.service;

import com.example.wallet.config.AppProperties;
import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.TransactionStatusResponse;
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
    
    /**
     * Get gas fee suggestions for Ethereum transactions
     */
    public GasFeeSuggestion getGasFeeSuggestion(String network) {
        return ethClient.getGasFeeSuggestion(network);
    }
    
    /**
     * Send an Ethereum transaction
     * Takes a signed transaction from client and broadcasts it to the network
     */
    public EthTransferResponse sendEthTransaction(String network, EthTransferRequest request) {
        return ethClient.sendTransaction(network, request);
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
     * Get transaction status and receipt by transaction hash
     *
     * @param network the Ethereum network name
     * @param txHash the transaction hash to query
     * @return detailed transaction status and receipt information
     */
    public TransactionStatusResponse getTransactionStatus(String network, String txHash) {
        return ethClient.getTransactionStatus(network, txHash);
    }
}

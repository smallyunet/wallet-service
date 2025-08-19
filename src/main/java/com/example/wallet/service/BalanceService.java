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
        // TODO: Implement real RPC call in EthClient
        String wei = ethClient.getBalance(network, address);
        return new BalanceResponse("ETH", network, address, wei);
    }

    public BalanceResponse btcBalance(String network, String address) {
        // TODO: Implement real HTTP call in BtcClient
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
}

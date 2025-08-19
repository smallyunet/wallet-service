package com.example.wallet.infra.btc;

import org.springframework.stereotype.Component;

@Component
public class BtcClient {
    public String getBalance(String network, String address) {
        // TODO: Implement actual Bitcoin balance query logic
        return "0";
    }
}

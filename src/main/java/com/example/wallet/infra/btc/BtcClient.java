package com.example.wallet.infra.btc;

import org.springframework.stereotype.Component;

@Component
public class BtcClient {
    public String getBalance(String network, String address) {
        // TODO: 实现真实的比特币余额查询逻辑
        return "0";
    }
}

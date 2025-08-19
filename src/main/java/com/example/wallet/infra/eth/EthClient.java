package com.example.wallet.infra.eth;

import org.springframework.stereotype.Component;

@Component
public class EthClient {
    public String getBalance(String network, String address) {
        // TODO: 实现真实的以太坊余额查询逻辑
        return "0";
    }
}

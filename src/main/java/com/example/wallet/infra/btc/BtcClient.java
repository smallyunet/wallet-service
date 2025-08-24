package com.example.wallet.infra.btc;

import org.springframework.stereotype.Component;

/**
 * Implementation of the Bitcoin client interface
 */
@Component
public class BtcClient implements IBtcClient {
    
    @Override
    public String getBalance(String network, String address) {
        // TODO: Implement actual Bitcoin balance query logic
        return "0";
    }
    
    @Override
    public String getTransactionStatus(String network, String txHash) {
        // TODO: Implement actual transaction status logic
        return "{}";
    }
    
    @Override
    public String getUtxos(String network, String address) {
        // TODO: Implement actual UTXO retrieval logic
        return "[]";
    }
}

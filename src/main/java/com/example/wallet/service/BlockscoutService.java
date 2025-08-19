package com.example.wallet.service;

import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.infra.eth.BlockscoutProvider;
import org.springframework.stereotype.Service;

@Service
public class BlockscoutService {
    private final BlockscoutProvider blockscoutProvider;

    public BlockscoutService(BlockscoutProvider blockscoutProvider) {
        this.blockscoutProvider = blockscoutProvider;
    }

    /**
     * Get transactions for an address in raw JSON format
     */
    public String getTransactions(String network, String address, String filter) {
        return blockscoutProvider.getTransactions(network, address, filter);
    }
    
    /**
     * Get transactions for an address as a parsed object
     */
    public BlockscoutTransactionResponse getTransactionsAsObject(String network, String address, String filter) {
        return blockscoutProvider.getTransactionsAsObject(network, address, filter);
    }
}

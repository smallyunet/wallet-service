package com.example.wallet.service;

import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.BlockscoutTokenListResponse;
import com.example.wallet.infra.eth.BlockscoutProvider;
import org.springframework.stereotype.Service;

@Service
public class BlockscoutService {
    /**
     * Query information about tokens like ERC-20
     * @param network Network name (e.g. "sepolia")
     * @param tokenSymbol Token symbol to search for (e.g. "USDT")
     * @param type Token type(s) to filter by (e.g. "ERC-20,ERC-721,ERC-1155")
     */
    public BlockscoutTokenListResponse getTokens(String network, String tokenSymbol, String type) {
        return blockscoutProvider.getTokens(network, tokenSymbol, type);
    }
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

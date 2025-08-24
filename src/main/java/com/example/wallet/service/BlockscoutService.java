package com.example.wallet.service;

import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.BlockscoutTokenInfo;
import com.example.wallet.domain.eth.BlockscoutTokenListResponse;
import com.example.wallet.domain.eth.TokenTransferListResponse;
import com.example.wallet.infra.eth.IBlockscoutProvider;
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
    
    /**
     * Get detailed information about a specific token by its address
     * @param network Network name (e.g. "sepolia")
     * @param tokenAddress Token contract address (e.g. "0x84637EaB3d14d481E7242D124e5567B72213D7F2")
     * @return Token details
     */
    public BlockscoutTokenInfo getTokenByAddress(String network, String tokenAddress) {
        return blockscoutProvider.getTokenByAddress(network, tokenAddress);
    }
    private final IBlockscoutProvider blockscoutProvider;

    public BlockscoutService(IBlockscoutProvider blockscoutProvider) {
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
    
    /**
     * Get token transfers for a specific address
     * @param network Network name (e.g. "sepolia")
     * @param address Wallet address to get token transfers for
     * @param tokenAddress Optional token address to filter by specific token (null for all tokens)
     * @param type Optional filter by type (e.g. "from", "to", null for all)
     * @return List of token transfers
     */
    public TokenTransferListResponse getTokenTransfers(String network, String address, String tokenAddress, String type) {
        return blockscoutProvider.getTokenTransfers(network, address, tokenAddress, type);
    }
}

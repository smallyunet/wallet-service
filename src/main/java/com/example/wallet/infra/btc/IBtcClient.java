package com.example.wallet.infra.btc;

/**
 * Interface for Bitcoin client operations
 * Using an interface makes the code more testable
 */
public interface IBtcClient {
    
    /**
     * Get BTC balance for an address
     * 
     * @param network The Bitcoin network (mainnet, testnet)
     * @param address The Bitcoin address
     * @return String representation of the balance
     */
    String getBalance(String network, String address);
    
    /**
     * Get transaction status by transaction hash
     * 
     * @param network The Bitcoin network (mainnet, testnet)
     * @param txHash The transaction hash
     * @return Transaction status information
     */
    String getTransactionStatus(String network, String txHash);
    
    /**
     * Get UTXO information for a Bitcoin address
     * 
     * @param network The Bitcoin network (mainnet, testnet)
     * @param address The Bitcoin address
     * @return UTXO information
     */
    String getUtxos(String network, String address);
}

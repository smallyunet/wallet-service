package com.example.wallet.config;

import java.util.Map;

/**
 * Interface for application configuration properties
 * Using an interface makes the code more testable
 */
public interface IAppProperties {
    
    /**
     * Get RPC configuration
     * 
     * @return RPC configuration object
     */
    Rpc getRpc();
    
    /**
     * RPC configuration interface
     */
    interface Rpc {
        /**
         * Get Ethereum RPC endpoints map
         * 
         * @return Map of network name to RPC URL
         */
        Map<String, String> getEth();
        
        /**
         * Get Bitcoin RPC endpoints map
         * 
         * @return Map of network name to RPC URL
         */
        Map<String, String> getBtc();
    }
}

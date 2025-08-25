package com.example.wallet.domain.btc;

/**
 * Detailed information about a Bitcoin address
 */
public record BitcoinAddressInfo(
    String address, 
    String network, 
    String balance, 
    Integer txCount, 
    Integer unspentOutputs, 
    String totalReceived,
    String totalSent
) {}

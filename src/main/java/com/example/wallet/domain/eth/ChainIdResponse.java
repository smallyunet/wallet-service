package com.example.wallet.domain.eth;

/**
 * Response object containing the chain ID for an Ethereum network
 */
public class ChainIdResponse {
    
    private long chainid;
    
    public ChainIdResponse() {
    }
    
    public ChainIdResponse(long chainid) {
        this.chainid = chainid;
    }
    
    public long getChainid() {
        return chainid;
    }
    
    public void setChainid(long chainid) {
        this.chainid = chainid;
    }
}

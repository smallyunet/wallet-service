package com.example.wallet.domain.eth;

/**
 * Nonce response for Ethereum address
 */
public class NonceResponse {
    private String network;
    private String address;
    private String nonce;

    public NonceResponse(String network, String address, String nonce) {
        this.network = network;
        this.address = address;
        this.nonce = nonce;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}

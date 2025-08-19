package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for nonce query
 */
public class NonceResponse {
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("network")
    private String network;
    
    @JsonProperty("nonce")
    private String nonce;
    
    @JsonProperty("nonce_decimal")
    private Long nonceDecimal;
    
    public NonceResponse() {
    }
    
    public NonceResponse(String network, String address, String nonce) {
        this.network = network;
        this.address = address;
        this.nonce = nonce;
        // Parse hex nonce to decimal if it's a valid hex string
        if (nonce != null && nonce.startsWith("0x")) {
            try {
                this.nonceDecimal = Long.parseLong(nonce.substring(2), 16);
            } catch (NumberFormatException e) {
                this.nonceDecimal = null;
            }
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
        // Parse hex nonce to decimal if it's a valid hex string
        if (nonce != null && nonce.startsWith("0x")) {
            try {
                this.nonceDecimal = Long.parseLong(nonce.substring(2), 16);
            } catch (NumberFormatException e) {
                this.nonceDecimal = null;
            }
        }
    }

    public Long getNonceDecimal() {
        return nonceDecimal;
    }

    public void setNonceDecimal(Long nonceDecimal) {
        this.nonceDecimal = nonceDecimal;
    }
}

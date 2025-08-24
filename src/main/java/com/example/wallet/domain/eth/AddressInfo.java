package com.example.wallet.domain.eth;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents detailed information about a blockchain address
 */
public class AddressInfo {
    @JsonProperty("ens_domain_name")
    private String ensDomainName;
    
    private String hash;
    
    private List<String> implementations;
    
    @JsonProperty("is_contract")
    private boolean isContract;
    
    @JsonProperty("is_scam")
    private boolean isScam;
    
    @JsonProperty("is_verified")
    private boolean isVerified;
    
    private Object metadata;
    
    private String name;
    
    @JsonProperty("private_tags")
    private List<String> privateTags;
    
    @JsonProperty("proxy_type")
    private String proxyType;
    
    @JsonProperty("public_tags")
    private List<String> publicTags;
    
    @JsonProperty("watchlist_names")
    private List<String> watchlistNames;

    // Getters and Setters
    public String getEnsDomainName() {
        return ensDomainName;
    }

    public void setEnsDomainName(String ensDomainName) {
        this.ensDomainName = ensDomainName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<String> getImplementations() {
        return implementations;
    }

    public void setImplementations(List<String> implementations) {
        this.implementations = implementations;
    }

    public boolean isContract() {
        return isContract;
    }

    public void setContract(boolean isContract) {
        this.isContract = isContract;
    }

    public boolean isScam() {
        return isScam;
    }

    public void setScam(boolean isScam) {
        this.isScam = isScam;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPrivateTags() {
        return privateTags;
    }

    public void setPrivateTags(List<String> privateTags) {
        this.privateTags = privateTags;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public List<String> getPublicTags() {
        return publicTags;
    }

    public void setPublicTags(List<String> publicTags) {
        this.publicTags = publicTags;
    }

    public List<String> getWatchlistNames() {
        return watchlistNames;
    }

    public void setWatchlistNames(List<String> watchlistNames) {
        this.watchlistNames = watchlistNames;
    }
}

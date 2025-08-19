package com.example.wallet.domain.blockscout;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents address information in Blockscout API response
 */
public class AddressInfo {
    @JsonProperty("ens_domain_name")
    private String ensDomainName;
    
    private String hash;
    
    private List<Object> implementations;
    
    @JsonProperty("is_contract")
    private Boolean isContract;
    
    @JsonProperty("is_scam")
    private Boolean isScam;
    
    @JsonProperty("is_verified")
    private Boolean isVerified;
    
    private Object metadata;
    
    private String name;
    
    @JsonProperty("private_tags")
    private List<Object> privateTags;
    
    @JsonProperty("proxy_type")
    private String proxyType;
    
    @JsonProperty("public_tags")
    private List<Object> publicTags;
    
    @JsonProperty("watchlist_names")
    private List<Object> watchlistNames;

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

    public List<Object> getImplementations() {
        return implementations;
    }

    public void setImplementations(List<Object> implementations) {
        this.implementations = implementations;
    }

    public Boolean getIsContract() {
        return isContract;
    }

    public void setIsContract(Boolean isContract) {
        this.isContract = isContract;
    }

    public Boolean getIsScam() {
        return isScam;
    }

    public void setIsScam(Boolean isScam) {
        this.isScam = isScam;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
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

    public List<Object> getPrivateTags() {
        return privateTags;
    }

    public void setPrivateTags(List<Object> privateTags) {
        this.privateTags = privateTags;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public List<Object> getPublicTags() {
        return publicTags;
    }

    public void setPublicTags(List<Object> publicTags) {
        this.publicTags = publicTags;
    }

    public List<Object> getWatchlistNames() {
        return watchlistNames;
    }

    public void setWatchlistNames(List<Object> watchlistNames) {
        this.watchlistNames = watchlistNames;
    }
}

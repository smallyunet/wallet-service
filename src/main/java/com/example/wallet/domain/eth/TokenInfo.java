package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the detailed information of an ERC-20 token
 */
public class TokenInfo {
    private String address;
    
    @JsonProperty("circulating_market_cap")
    private String circulatingMarketCap;
    
    private Integer decimals;
    
    private String exchange_rate;
    
    private String holders;
    
    private String icon_url;
    
    private String name;
    
    private String symbol;
    
    private String type;
    
    @JsonProperty("total_supply")
    private String totalSupply;

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCirculatingMarketCap() {
        return circulatingMarketCap;
    }

    public void setCirculatingMarketCap(String circulatingMarketCap) {
        this.circulatingMarketCap = circulatingMarketCap;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getHolders() {
        return holders;
    }

    public void setHolders(String holders) {
        this.holders = holders;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }
}

package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockscoutTokenInfo {
    @JsonProperty("address_hash")
    private String addressHash;
    @JsonProperty("circulating_market_cap")
    private String circulatingMarketCap;
    private String decimals;
    @JsonProperty("exchange_rate")
    private String exchangeRate;
    @JsonProperty("holders_count")
    private String holdersCount;
    @JsonProperty("icon_url")
    private String iconUrl;
    private String name;
    private String symbol;
    @JsonProperty("total_supply")
    private String totalSupply;
    private String type;
    @JsonProperty("volume_24h")
    private String volume24h;

    // getters and setters
    public String getAddressHash() { return addressHash; }
    public void setAddressHash(String addressHash) { this.addressHash = addressHash; }
    public String getCirculatingMarketCap() { return circulatingMarketCap; }
    public void setCirculatingMarketCap(String circulatingMarketCap) { this.circulatingMarketCap = circulatingMarketCap; }
    public String getDecimals() { return decimals; }
    public void setDecimals(String decimals) { this.decimals = decimals; }
    public String getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(String exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getHoldersCount() { return holdersCount; }
    public void setHoldersCount(String holdersCount) { this.holdersCount = holdersCount; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getTotalSupply() { return totalSupply; }
    public void setTotalSupply(String totalSupply) { this.totalSupply = totalSupply; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getVolume24h() { return volume24h; }
    public void setVolume24h(String volume24h) { this.volume24h = volume24h; }
}

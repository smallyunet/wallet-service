package com.example.wallet.domain.eth;

/**
 * Response object for token balance queries
 */
public class TokenBalanceResponse {
    private String tokenAddress;
    private String network;
    private String walletAddress;
    private String balance;
    private String symbol;
    private String name;
    private String decimals;

    public TokenBalanceResponse() {
    }

    public TokenBalanceResponse(String tokenAddress, String network, String walletAddress,
                              String balance, String symbol, String name, String decimals) {
        this.tokenAddress = tokenAddress;
        this.network = network;
        this.walletAddress = walletAddress;
        this.balance = balance;
        this.symbol = symbol;
        this.name = name;
        this.decimals = decimals;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }
}

package com.example.wallet.domain;

public record BalanceResponse(String chain, String network, String address, String balance) {}

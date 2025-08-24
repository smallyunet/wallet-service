package com.example.wallet.domain.eth;

import java.math.BigInteger;

/**
 * Basic method definitions for ERC-20 contract
 */
public interface ERC20 {
    String name();
    String symbol();
    String decimals();
    BigInteger balanceOf(String owner);
}

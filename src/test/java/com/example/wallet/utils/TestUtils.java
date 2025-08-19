package com.example.wallet.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Test utility class
 */
public class TestUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter objectWriter = objectMapper.writer();

    /**
     * Convert object to JSON string
     */
    public static String toJson(Object obj) {
        try {
            return objectWriter.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate an Ethereum address for testing
     */
    public static String generateEthAddress() {
        return "0x" + generateHexString(40);
    }

    /**
     * Generate a Bitcoin address for testing
     */
    public static String generateBtcAddress() {
        return "bc1" + generateHexString(40);
    }

    /**
     * Generate a transaction hash for testing
     */
    public static String generateTxHash() {
        return "0x" + generateHexString(64);
    }

    /**
     * Generate a hexadecimal string of specified length
     */
    private static String generateHexString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 16);
            sb.append(Integer.toHexString(digit));
        }
        return sb.toString();
    }
}

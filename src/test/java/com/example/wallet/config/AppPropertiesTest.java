package com.example.wallet.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;
    
    @Test
    public void testAppPropertiesBinding() {
        // Verify environment properties
        assertEquals("test", appProperties.getEnv());
        
        // Verify RPC URL
        assertNotNull(appProperties.getRpc());
        assertNotNull(appProperties.getRpc().getEth());
        assertNotNull(appProperties.getRpc().getBtc());
        
        // Verify ETH RPC URLs
        assertEquals("https://eth-mainnet.mock", appProperties.getRpc().getEth().get("mainnet"));
        assertEquals("https://ethereum-sepolia.mock", appProperties.getRpc().getEth().get("sepolia"));
        
        // Verify BTC RPC URLs
        assertEquals("https://blockstream.mock/api", appProperties.getRpc().getBtc().get("mainnet"));
        assertEquals("https://blockstream.mock/testnet/api", appProperties.getRpc().getBtc().get("testnet"));
        
        // Verify security settings
        assertEquals("*", appProperties.getSecurity().getAllowOrigins());
    }
}

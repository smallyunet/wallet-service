package com.example.wallet.infra.btc;

import com.example.wallet.utils.TestUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BtcClientTest {
    
    @InjectMocks
    private BtcClient btcClient;
    
    private final String BTC_ADDRESS = TestUtils.generateBtcAddress();

    @ParameterizedTest
    @ValueSource(strings = {"mainnet", "testnet"})
    public void testGetBalanceForDifferentNetworks(String network) {
        // Currently BtcClient.getBalance always returns "0", so we're just testing this behavior
        String result = btcClient.getBalance(network, BTC_ADDRESS);
        assertEquals("0", result);
    }
    
    @Test
    public void testGetBalanceWithInvalidAddress() {
        // Test with invalid address format
        String invalidAddress = "invalid-address";
        String result = btcClient.getBalance("mainnet", invalidAddress);
        
        // Even with invalid address, method should return "0" based on implementation
        assertEquals("0", result);
    }
}

package com.example.wallet.infra.btc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BtcClientTest {

    @InjectMocks
    private BtcClient btcClient;

    @Test
    public void testGetBalance() {
        // Currently BtcClient.getBalance always returns "0", so we're just testing this behavior
        String result = btcClient.getBalance("mainnet", "bc1abc123");
        assertEquals("0", result);
    }
}

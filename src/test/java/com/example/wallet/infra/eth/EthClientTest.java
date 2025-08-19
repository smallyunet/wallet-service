package com.example.wallet.infra.eth;

import com.example.wallet.config.AppProperties;
import com.example.wallet.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EthClientTest {

    @Mock
    private AppProperties appProperties;

    @Mock
    private AppProperties.Rpc rpc;

    private EthClient ethClient;

    private final String NETWORK = "mainnet";
    private final String ETH_ADDRESS = TestUtils.generateEthAddress();
    private final BigInteger BALANCE = BigInteger.valueOf(1234567890);
    private final String ETH_RPC = "https://eth-mainnet.mock";

    @BeforeEach
    public void setup() {
        // Configure App properties
        Map<String, String> ethMap = new HashMap<>();
        ethMap.put(NETWORK, ETH_RPC);
        
        when(appProperties.getRpc()).thenReturn(rpc);
        when(rpc.getEth()).thenReturn(ethMap);
        
        ethClient = new EthClient(appProperties);
    }

    @Test
    public void testGetBalanceThrowsForInvalidNetwork() {
        // Verify exception is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ethClient.getBalance("invalid-network", ETH_ADDRESS);
        });
        
        assertEquals("Unsupported ETH network: invalid-network", exception.getMessage());
    }

    // Note: Due to Web3j using static methods and final classes, fully testing EthClient becomes complex
    // These tests would require more advanced mocking capabilities like MockedStatic or PowerMock
}

package com.example.wallet.service;

import com.example.wallet.config.AppProperties;
import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.NonceResponse;
import com.example.wallet.infra.btc.BtcClient;
import com.example.wallet.infra.eth.EthClient;
import com.example.wallet.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private EthClient ethClient;

    @Mock
    private BtcClient btcClient;

    @Mock
    private AppProperties appProperties;

    @Mock
    private AppProperties.Rpc rpc;

    private BalanceService balanceService;

    private final String ETH_NETWORK = "mainnet";
    private final String BTC_NETWORK = "mainnet";
    private final String ETH_ADDRESS = TestUtils.generateEthAddress();
    private final String BTC_ADDRESS = TestUtils.generateBtcAddress();
    private final String ETH_BALANCE = "0x" + System.currentTimeMillis();
    private final String BTC_BALANCE = String.valueOf(System.currentTimeMillis());
    private final String ETH_RPC = "https://eth-mainnet.mock";
    private final String ETH_NONCE = "0x1";

    @BeforeEach
    public void setup() {
        // Configure App properties
        Map<String, String> ethMap = new HashMap<>();
        ethMap.put(ETH_NETWORK, ETH_RPC);
        
        Map<String, String> btcMap = new HashMap<>();
        btcMap.put(BTC_NETWORK, "https://blockstream.mock/api");

        when(appProperties.getRpc()).thenReturn(rpc);
        when(rpc.getEth()).thenReturn(ethMap);
        when(rpc.getBtc()).thenReturn(btcMap);
        
        balanceService = new BalanceService(ethClient, btcClient, appProperties);
    }

    @Test
    public void testEthBalance() {
        // Configure mock behavior
        when(ethClient.getBalance(eq(ETH_NETWORK), eq(ETH_ADDRESS))).thenReturn(ETH_BALANCE);

        // Call service method
        BalanceResponse response = balanceService.ethBalance(ETH_NETWORK, ETH_ADDRESS);

        // Verify results
        assertNotNull(response);
        assertEquals("ETH", response.chain());
        assertEquals(ETH_NETWORK, response.network());
        assertEquals(ETH_ADDRESS, response.address());
        assertEquals(ETH_BALANCE, response.balance());
        
        // Verify calls
        verify(ethClient).getBalance(ETH_NETWORK, ETH_ADDRESS);
    }

    @Test
    public void testBtcBalance() {
        // Configure mock behavior
        when(btcClient.getBalance(eq(BTC_NETWORK), eq(BTC_ADDRESS))).thenReturn(BTC_BALANCE);

        // Call service method
        BalanceResponse response = balanceService.btcBalance(BTC_NETWORK, BTC_ADDRESS);

        // Verify results
        assertNotNull(response);
        assertEquals("BTC", response.chain());
        assertEquals(BTC_NETWORK, response.network());
        assertEquals(BTC_ADDRESS, response.address());
        assertEquals(BTC_BALANCE, response.balance());
        
        // Verify calls
        verify(btcClient).getBalance(BTC_NETWORK, BTC_ADDRESS);
    }

    @Test
    public void testEffectiveEthRpc() {
        // Call service method
        String result = balanceService.effectiveEthRpc(ETH_NETWORK);
        
        // Verify results
        assertEquals(ETH_RPC, result);
    }

    @Test
    public void testEffectiveEthRpcThrowsForInvalidNetwork() {
        // Verify exception is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            balanceService.effectiveEthRpc("invalid-network");
        });
        
        assertTrue(exception.getMessage().contains("Unsupported ETH network"));
    }

    @Test
    public void testEffectiveBtcRpc() {
        // Call service method
        Object result = balanceService.effectiveBtcRpc();
        
        // Verify results
        assertNotNull(result);
    }
}

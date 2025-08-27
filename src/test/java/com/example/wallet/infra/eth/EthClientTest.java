package com.example.wallet.infra.eth;

import com.example.wallet.config.IAppProperties;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGasPrice;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EthClientTest {

    private EthClient ethClient;

    @Mock
    private IAppProperties appProperties;

    @Mock
    private Web3j web3j;

    @Mock
    private IAppProperties.Rpc rpc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup app properties mock
        Map<String, String> ethNetworks = new HashMap<>();
        ethNetworks.put("mainnet", "https://eth-mainnet.example.com");
        ethNetworks.put("sepolia", "https://eth-sepolia.example.com");
        
        when(appProperties.getRpc()).thenReturn(rpc);
        when(rpc.getEth()).thenReturn(ethNetworks);
        
        // Create a partial mock of EthClient
        ethClient = new EthClient(appProperties);
        
        // We'll use PowerMockito to mock the static Web3j.build method in each test
    }

    /**
     * Test gas fee calculation with a normal gas price
     */
    @Test
    public void testGetGasFeeSuggestion_NormalGasPrice() throws IOException {
        // Setup mock response
        BigInteger normalGasPrice = BigInteger.valueOf(20_000_000_000L); // 20 Gwei
        setupMockGasPrice(normalGasPrice);
        
        // Call the method
        GasFeeSuggestion suggestion = ethClient.getGasFeeSuggestion("mainnet");
        
        // Verify the results
        assertNotNull(suggestion);
        assertEquals("20.000", suggestion.getBaseFee());
        assertEquals("20.000", suggestion.getSlow().getMaxFee());
        assertEquals("2.000", suggestion.getSlow().getMaxPriorityFee());
        assertEquals("24.000", suggestion.getAverage().getMaxFee());
        assertEquals("4.000", suggestion.getAverage().getMaxPriorityFee());
        assertEquals("30.000", suggestion.getFast().getMaxFee());
        assertEquals("6.000", suggestion.getFast().getMaxPriorityFee());
        assertEquals("40.000", suggestion.getFastest().getMaxFee());
        assertEquals("10.000", suggestion.getFastest().getMaxPriorityFee());
        assertEquals("gwei", suggestion.getUnit());
    }
    
    /**
     * Test gas fee calculation with zero gas price
     */
    @Test
    public void testGetGasFeeSuggestion_ZeroGasPrice() throws IOException {
        // Setup mock to return zero gas price
        setupMockGasPrice(BigInteger.ZERO);
        
        // Call the method
        GasFeeSuggestion suggestion = ethClient.getGasFeeSuggestion("sepolia");
        
        // Verify the results - should use minimum displayable value (0.001 Gwei)
        assertNotNull(suggestion);
        assertEquals("0.001", suggestion.getBaseFee());
        assertEquals("0.001", suggestion.getSlow().getMaxFee());
        assertEquals("0.000", suggestion.getSlow().getMaxPriorityFee()); // 0.0001 rounds to 0.000
        assertEquals("0.001", suggestion.getAverage().getMaxFee());
        assertEquals("0.000", suggestion.getAverage().getMaxPriorityFee());
        assertEquals("0.002", suggestion.getFast().getMaxFee());
        assertEquals("0.000", suggestion.getFast().getMaxPriorityFee());
        assertEquals("0.002", suggestion.getFastest().getMaxFee());
        assertEquals("0.001", suggestion.getFastest().getMaxPriorityFee());
    }
    
    /**
     * Test gas fee calculation with very low gas price (below display precision)
     */
    @Test
    public void testGetGasFeeSuggestion_VeryLowGasPrice() throws IOException {
        // Setup mock with very low gas price (100 wei, well below 0.001 Gwei)
        setupMockGasPrice(BigInteger.valueOf(100L));
        
        // Call the method
        GasFeeSuggestion suggestion = ethClient.getGasFeeSuggestion("sepolia");
        
        // Verify the results - should use minimum displayable value (0.001 Gwei)
        assertNotNull(suggestion);
        assertEquals("0.001", suggestion.getBaseFee());
        assertEquals("0.001", suggestion.getSlow().getMaxFee());
        assertEquals("0.000", suggestion.getSlow().getMaxPriorityFee());
    }
    
    /**
     * Test gas fee calculation with gas price just at the threshold
     */
    @Test
    public void testGetGasFeeSuggestion_ThresholdGasPrice() throws IOException {
        // Setup mock with gas price at exactly 1,000,000 wei (0.001 Gwei)
        setupMockGasPrice(BigInteger.valueOf(1_000_000L));
        
        // Call the method
        GasFeeSuggestion suggestion = ethClient.getGasFeeSuggestion("sepolia");
        
        // Verify the results - should use actual value
        assertNotNull(suggestion);
        assertEquals("0.001", suggestion.getBaseFee());
        assertEquals("0.001", suggestion.getSlow().getMaxFee());
        assertEquals("0.000", suggestion.getSlow().getMaxPriorityFee());
    }
    
    /**
     * Test gas fee calculation with gas price just above the threshold
     */
    @Test
    public void testGetGasFeeSuggestion_AboveThresholdGasPrice() throws IOException {
        // Setup mock with gas price just above threshold (1,500,000 wei)
        setupMockGasPrice(BigInteger.valueOf(1_500_000L));
        
        // Call the method
        GasFeeSuggestion suggestion = ethClient.getGasFeeSuggestion("sepolia");
        
        // Verify the results - should use actual value
        assertNotNull(suggestion);
        assertEquals("0.002", suggestion.getBaseFee());
        assertEquals("0.002", suggestion.getSlow().getMaxFee());
        assertEquals("0.000", suggestion.getSlow().getMaxPriorityFee());
    }
    
    /**
     * Test exception handling when RPC call fails
     */
    @Test
    @SuppressWarnings("rawtypes") // Suppress raw type warnings
    public void testGetGasFeeSuggestion_RpcException() throws IOException {
        // Setup mock to throw exception
        Request mockRequest = mock(Request.class);
        when(mockRequest.send()).thenThrow(new IOException("RPC connection failed"));
        
        doReturn(mockRequest).when(web3j).ethGasPrice();
        
        // Verify that the exception is properly wrapped
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ethClient.getGasFeeSuggestion("mainnet");
        });
        
        assertTrue(exception.getMessage().contains("Failed to fetch gas price"));
        assertTrue(exception.getCause() instanceof IOException);
    }
    
    /**
     * Test exception handling when network is not supported
     */
    @Test
    public void testGetGasFeeSuggestion_UnsupportedNetwork() {
        // Call with unsupported network
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ethClient.getGasFeeSuggestion("unknown-network");
        });
        
        assertEquals("Unsupported ETH network: unknown-network", exception.getMessage());
    }
    
    // Helper method to setup mock gas price response
    @SuppressWarnings("rawtypes") // Suppress raw type warnings
    private void setupMockGasPrice(BigInteger gasPrice) throws IOException {
        EthGasPrice mockGasPrice = mock(EthGasPrice.class);
        when(mockGasPrice.getGasPrice()).thenReturn(gasPrice);
        
        // Use raw type with @SuppressWarnings
        Request mockRequest = mock(Request.class);
        when(mockRequest.send()).thenReturn(mockGasPrice);
        
        // Use doReturn instead of when().thenReturn() to avoid type issues
        doReturn(mockRequest).when(web3j).ethGasPrice();
    }
}

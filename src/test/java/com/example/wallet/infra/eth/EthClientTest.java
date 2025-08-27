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
import org.web3j.protocol.core.methods.response.EthChainId;
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
        
        // Create EthClient with constructor injection of mocked web3j
        ethClient = spy(new EthClient(appProperties) {
            @Override
            protected Web3j createWeb3j(String rpcUrl) {
                return web3j; // Return our mocked web3j instance
            }
        });
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
        assertEquals("20.0000", suggestion.getBaseFee());
        assertEquals("20.0000", suggestion.getSlow().getMaxFee());
        assertEquals("2.0000", suggestion.getSlow().getMaxPriorityFee());
        assertEquals("24.0000", suggestion.getAverage().getMaxFee());
        assertEquals("4.0000", suggestion.getAverage().getMaxPriorityFee());
        assertEquals("30.0000", suggestion.getFast().getMaxFee());
        assertEquals("6.0000", suggestion.getFast().getMaxPriorityFee());
        assertEquals("40.0000", suggestion.getFastest().getMaxFee());
        assertEquals("10.0000", suggestion.getFastest().getMaxPriorityFee());
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
        assertEquals("0.0010", suggestion.getBaseFee());
        assertEquals("0.0010", suggestion.getSlow().getMaxFee());
        assertEquals("0.0001", suggestion.getSlow().getMaxPriorityFee()); // 0.0001 no longer rounded to 0.0000 with String.format
        assertEquals("0.0012", suggestion.getAverage().getMaxFee()); // 0.0010 * 1.2 = 0.0012
        assertEquals("0.0002", suggestion.getAverage().getMaxPriorityFee()); // 0.001 * 0.2 = 0.0002
        assertEquals("0.0015", suggestion.getFast().getMaxFee()); // 0.0010 * 1.5 = 0.0015
        assertEquals("0.0003", suggestion.getFast().getMaxPriorityFee()); // 0.001 * 0.3 = 0.0003
        assertEquals("0.0020", suggestion.getFastest().getMaxFee()); // 0.0010 * 2.0 = 0.0020
        assertEquals("0.0005", suggestion.getFastest().getMaxPriorityFee()); // 0.001 * 0.5 = 0.0005
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
        assertEquals("0.0010", suggestion.getBaseFee());
        assertEquals("0.0010", suggestion.getSlow().getMaxFee());
        assertEquals("0.0001", suggestion.getSlow().getMaxPriorityFee());
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
        assertEquals("0.0010", suggestion.getBaseFee());
        assertEquals("0.0010", suggestion.getSlow().getMaxFee());
        assertEquals("0.0001", suggestion.getSlow().getMaxPriorityFee());
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
        assertEquals("0.0015", suggestion.getBaseFee());
        assertEquals("0.0015", suggestion.getSlow().getMaxFee());
        assertEquals("0.0002", suggestion.getSlow().getMaxPriorityFee());
    }
    
    /**
     * Test exception handling when RPC call fails
     */
    @Test
    @SuppressWarnings({"rawtypes"}) // Suppress raw type warnings
    public void testGetGasFeeSuggestion_RpcException() throws IOException {
        // Setup mock to throw exception
        Request mockRequest = mock(Request.class);
        when(mockRequest.send()).thenThrow(new IOException("RPC connection failed"));
        
        // Setup the mock web3j client to return the request mock
        when(web3j.ethGasPrice()).thenReturn(mockRequest);
        
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
    
    /**
     * Test getting chain ID for Ethereum mainnet
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Suppress raw type warnings
    @Test
    public void testGetChainId_Mainnet() throws IOException {
        // Setup mock response for chain ID (Ethereum mainnet = 1)
        EthChainId mockChainId = mock(EthChainId.class);
        when(mockChainId.getChainId()).thenReturn(BigInteger.valueOf(1L));
        when(mockChainId.hasError()).thenReturn(false);
        
        Request mockRequest = mock(Request.class);
        when(mockRequest.send()).thenReturn(mockChainId);
        doReturn(mockRequest).when(web3j).ethChainId();
        
        // Call the method
        long chainId = ethClient.getChainId("mainnet");
        
        // Verify the result
        assertEquals(1L, chainId);
    }
    
    /**
     * Test getting chain ID for Ethereum Sepolia testnet
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Suppress raw type warnings
    @Test
    public void testGetChainId_Sepolia() throws IOException {
        // Setup mock response for chain ID (Sepolia = 11155111)
        EthChainId mockChainId = mock(EthChainId.class);
        when(mockChainId.getChainId()).thenReturn(BigInteger.valueOf(11155111L));
        when(mockChainId.hasError()).thenReturn(false);
        
        Request mockRequest = mock(Request.class);
        when(mockRequest.send()).thenReturn(mockChainId);
        doReturn(mockRequest).when(web3j).ethChainId();
        
        // Call the method
        long chainId = ethClient.getChainId("sepolia");
        
        // Verify the result
        assertEquals(11155111L, chainId);
    }
    
    /**
     * Test error handling when network is not found
     */
    @Test
    public void testGetChainId_UnsupportedNetwork() {
        // Setup app properties to throw exception for unknown network
        when(rpc.getEth().get("unknown-network")).thenReturn(null);
        
        // Call with unsupported network
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ethClient.getChainId("unknown-network");
        });
        
        // Verify the exception message
        assertTrue(exception.getMessage().contains("Network unknown-network is not configured"));
    }
    
    /**
     * Test error handling when RPC call fails
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Suppress raw type warnings
    @Test
    public void testGetChainId_RpcError() throws IOException {
        // Setup mock response for chain ID with error
        EthChainId mockChainId = mock(EthChainId.class);
        when(mockChainId.hasError()).thenReturn(true);
        org.web3j.protocol.core.Response.Error mockError = new org.web3j.protocol.core.Response.Error();
        mockError.setMessage("RPC connection failed");
        when(mockChainId.getError()).thenReturn(mockError);
        
        Request mockRequest = mock(Request.class);
        when(mockRequest.send()).thenReturn(mockChainId);
        doReturn(mockRequest).when(web3j).ethChainId();
        
        // Call with network that will cause RPC error
        Exception exception = assertThrows(IOException.class, () -> {
            ethClient.getChainId("mainnet");
        });
        
        // Verify the exception message
        assertTrue(exception.getMessage().contains("Error fetching chain ID"));
    }
    
    // Helper method to setup mock gas price response
    @SuppressWarnings({"rawtypes", "unchecked"}) // Suppress raw type warnings
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

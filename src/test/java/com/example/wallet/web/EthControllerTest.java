package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.BlockscoutTokenInfo;
import com.example.wallet.domain.eth.BlockscoutTokenListResponse;
import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.NonceResponse;
import com.example.wallet.domain.eth.TokenBalanceResponse;
import com.example.wallet.domain.eth.TokenTransferListResponse;
import com.example.wallet.domain.eth.TokenTransferRequest;
import com.example.wallet.domain.eth.TokenTransferResponse;
import com.example.wallet.domain.eth.TransactionStatusResponse;
import com.example.wallet.service.BalanceService;
import com.example.wallet.service.BlockscoutService;
import com.example.wallet.service.TransactionService;
import com.example.wallet.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EthController.class)
public class EthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BalanceService balanceService;

    @MockBean
    private BlockscoutService blockscoutService;
    
    @MockBean
    private TransactionService transactionService;

    @Test
    public void testGetBalance() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = TestUtils.generateEthAddress();
        BalanceResponse mockResponse = new BalanceResponse("ETH", network, address, "0x1234567890");

        // Configure mock service behavior
        when(balanceService.ethBalance(eq(network), eq(address))).thenReturn(mockResponse);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/balance", network, address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chain").value("ETH"))
                .andExpect(jsonPath("$.network").value(network))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.balance").value("0x1234567890"));
    }

    @Test
    public void testGetNonce() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = TestUtils.generateEthAddress();
        String nonce = "0x1";
        NonceResponse mockResponse = new NonceResponse(network, address, nonce);

        // Configure mock service behavior
        when(transactionService.getNonce(eq(network), eq(address))).thenReturn(mockResponse);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/nonce", network, address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.network").value(network))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.nonce").value(nonce));
    }

    @Test
    public void testGetGasFees() throws Exception {
        // Prepare test data
        String network = "mainnet";
        GasFeeSuggestion mockResponse = new GasFeeSuggestion();
        mockResponse.setBaseFee("20.0");
        
        // Set up details for slow
        GasFeeSuggestion.GasFeeDetail slow = new GasFeeSuggestion.GasFeeDetail();
        slow.setMaxFee("20.0");
        slow.setMaxPriorityFee("2.0");
        slow.setEstimatedSeconds(120);
        mockResponse.setSlow(slow);
        
        // Configure mock service behavior
        when(transactionService.getGasFeeSuggestion(eq(network))).thenReturn(mockResponse);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/gas-fees", network)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base_fee").value("20.0"))
                .andExpect(jsonPath("$.slow.max_fee").value("20.0"));
    }

    @Test
    public void testConfigRpc() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String rpcUrl = "https://eth-mainnet.mock";

        // Configure mock service behavior
        when(balanceService.effectiveEthRpc(eq(network))).thenReturn(rpcUrl);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/config/rpc", network)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(rpcUrl));
    }
    
    @Test
    public void testSendTransaction() throws Exception {
        // Prepare test data
        String network = "mainnet";
        EthTransferRequest request = new EthTransferRequest();
        request.setSignedTransaction("0x1234567890abcdef");
        request.setFrom("0xSenderAddress");
        request.setTo("0xRecipientAddress");
        request.setValue("0.1");
        
        EthTransferResponse mockResponse = new EthTransferResponse();
        mockResponse.setTransactionHash("0xTransactionHash");
        mockResponse.setFrom(request.getFrom());
        mockResponse.setTo(request.getTo());
        mockResponse.setValue(request.getValue());
        
        // Configure mock service behavior
        when(transactionService.sendEthTransaction(eq(network), any(EthTransferRequest.class)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(post("/v1/eth/{network}/transfer", network)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_hash").value("0xTransactionHash"))
                .andExpect(jsonPath("$.from").value(request.getFrom()))
                .andExpect(jsonPath("$.to").value(request.getTo()))
                .andExpect(jsonPath("$.value").value(request.getValue()));
    }
    
    @Test
    public void testSendTokenTransaction() throws Exception {
        // Prepare test data
        String network = "mainnet";
        TokenTransferRequest request = new TokenTransferRequest();
        request.setSignedTransaction("0x1234567890abcdef");
        request.setFrom("0xSenderAddress");
        request.setTo("0xRecipientAddress");
        request.setTokenAddress("0xTokenContractAddress");
        request.setValue("100");
        
        TokenTransferResponse mockResponse = new TokenTransferResponse();
        mockResponse.setTransactionHash("0xTokenTransactionHash");
        mockResponse.setFrom(request.getFrom());
        mockResponse.setTo(request.getTo());
        mockResponse.setTokenAddress(request.getTokenAddress());
        mockResponse.setValue(request.getValue());
        
        // Configure mock service behavior
        when(transactionService.sendTokenTransaction(eq(network), any(TokenTransferRequest.class)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(post("/v1/eth/{network}/token-transfer", network)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_hash").value("0xTokenTransactionHash"))
                .andExpect(jsonPath("$.from").value(request.getFrom()))
                .andExpect(jsonPath("$.to").value(request.getTo()))
                .andExpect(jsonPath("$.token_address").value(request.getTokenAddress()))
                .andExpect(jsonPath("$.value").value(request.getValue()));
    }
    
    @Test
    public void testGetTransactionStatus() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String txHash = "0x1234567890abcdef";
        
        TransactionStatusResponse mockResponse = new TransactionStatusResponse();
        mockResponse.setTransactionHash(txHash);
        mockResponse.setStatus("confirmed");
        mockResponse.setBlockNumber("123456");
        mockResponse.setFrom("0xSenderAddress");
        mockResponse.setTo("0xRecipientAddress");
        
        // Configure mock service behavior
        when(transactionService.getTransactionStatus(eq(network), eq(txHash)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/tx/{txHash}", network, txHash)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_hash").value(txHash))
                .andExpect(jsonPath("$.status").value("confirmed"))
                .andExpect(jsonPath("$.block_number").value("123456"))
                .andExpect(jsonPath("$.from").value("0xSenderAddress"))
                .andExpect(jsonPath("$.to").value("0xRecipientAddress"));
    }
    
    @Test
    public void testGetTokenBalance() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String tokenAddress = "0xTokenContractAddress";
        String walletAddress = "0xWalletAddress";
        
        TokenBalanceResponse mockResponse = new TokenBalanceResponse();
        mockResponse.setBalance("1000000000000000000");
        mockResponse.setDecimals("18");
        mockResponse.setSymbol("USDT");
        mockResponse.setName("Tether USD");
        mockResponse.setTokenAddress(tokenAddress);
        mockResponse.setNetwork(network);
        mockResponse.setWalletAddress(walletAddress);
        
        // Configure mock service behavior
        when(balanceService.getTokenBalance(eq(network), eq(tokenAddress), eq(walletAddress)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/tokens/{tokenAddress}/balances/{walletAddress}", 
                network, tokenAddress, walletAddress)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("1000000000000000000"))
                .andExpect(jsonPath("$.decimals").value("18"))
                .andExpect(jsonPath("$.symbol").value("USDT"))
                .andExpect(jsonPath("$.name").value("Tether USD"))
                .andExpect(jsonPath("$.tokenAddress").value(tokenAddress))
                .andExpect(jsonPath("$.network").value(network))
                .andExpect(jsonPath("$.walletAddress").value(walletAddress));
    }
    
    @Test
    public void testGetTokenTransfers() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = "0xWalletAddress";
        String tokenAddress = "0xTokenAddress";
        String type = "from";
        
        TokenTransferListResponse mockResponse = new TokenTransferListResponse();
        // Set up mock response properties
        
        // Configure mock service behavior
        when(blockscoutService.getTokenTransfers(eq(network), eq(address), eq(tokenAddress), eq(type)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/token-transfers", network, address)
                .param("tokenAddress", tokenAddress)
                .param("type", type)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetTokens() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String tokenSymbol = "USDT";
        String type = "ERC-20";
        
        BlockscoutTokenListResponse mockResponse = new BlockscoutTokenListResponse();
        // Set up mock response properties
        
        // Configure mock service behavior
        when(blockscoutService.getTokens(eq(network), eq(tokenSymbol), eq(type)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/tokens", network)
                .param("tokenSymbol", tokenSymbol)
                .param("type", type)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetTokenByAddress() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String tokenAddress = "0xTokenContractAddress";
        
        BlockscoutTokenInfo mockResponse = new BlockscoutTokenInfo();
        // Set up mock response properties
        
        // Configure mock service behavior
        when(blockscoutService.getTokenByAddress(eq(network), eq(tokenAddress)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/tokens/{tokenAddress}", network, tokenAddress)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetTransactions() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = "0xWalletAddress";
        String filter = "to";
        
        BlockscoutTransactionResponse mockResponse = new BlockscoutTransactionResponse();
        // Set up mock response properties
        
        // Configure mock service behavior
        when(blockscoutService.getTransactionsAsObject(eq(network), eq(address), eq(filter)))
            .thenReturn(mockResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/transactions", network, address)
                .param("filter", filter)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testGetTransactionsRaw() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = "0xWalletAddress";
        String filter = "to";
        String mockRawResponse = "{\"items\":[]}";
        
        // Configure mock service behavior
        when(blockscoutService.getTransactions(eq(network), eq(address), eq(filter)))
            .thenReturn(mockRawResponse);
        
        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/transactions/raw", network, address)
                .param("filter", filter)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mockRawResponse));
    }
}

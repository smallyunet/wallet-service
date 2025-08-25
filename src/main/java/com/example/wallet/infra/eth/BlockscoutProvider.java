package com.example.wallet.infra.eth;

import com.example.wallet.config.AppProperties;
import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.BlockscoutTokenInfo;
import com.example.wallet.domain.eth.BlockscoutTokenListResponse;
import com.example.wallet.domain.eth.TokenTransferListResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BlockscoutProvider implements IBlockscoutProvider {
    /**
     * Query ERC-20 and other token information
     * @param network Network name (e.g. "sepolia")
     * @param tokenSymbol Token symbol to search for (e.g. "USDT")
     * @param type Token type(s) to filter by (e.g. "ERC-20,ERC-721,ERC-1155")
     */
    public BlockscoutTokenListResponse getTokens(String network, String tokenSymbol, String type) {
        try {
            String baseUrl = appProperties.getBlockscout().get("eth").get(network);
            if (baseUrl == null) {
                throw new IllegalArgumentException("Unsupported blockscout network: " + network);
            }
            String rootUrl = baseUrl.replace("/api/v2/addresses", "");
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rootUrl)
                    .path("/api/v2/tokens")
                    .queryParam("q", tokenSymbol);
                    
            if (type != null && !type.trim().isEmpty()) {
                builder.queryParam("type", type);
            }
            String finalUrl = builder.toUriString();
            logger.info("Requesting tokens from: {}", finalUrl);
            HttpEntity<?> entity = buildJsonHeaders();
            ResponseEntity<BlockscoutTokenListResponse> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    entity,
                    BlockscoutTokenListResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error from Blockscout API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Blockscout API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error fetching tokens", e);
            throw new RuntimeException("Failed to fetch tokens: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get detailed information about a specific token by its address
     * @param network Network name (e.g. "sepolia")
     * @param tokenAddress Token contract address (e.g. "0x84637EaB3d14d481E7242D124e5567B72213D7F2")
     * @return Token details
     */
    public BlockscoutTokenInfo getTokenByAddress(String network, String tokenAddress) {
        try {
            String baseUrl = appProperties.getBlockscout().get("eth").get(network);
            if (baseUrl == null) {
                throw new IllegalArgumentException("Unsupported blockscout network: " + network);
            }
            String rootUrl = baseUrl.replace("/api/v2/addresses", "");
            
            String url = rootUrl + "/api/v2/tokens/" + tokenAddress;
            logger.info("Requesting token details from: {}", url);
            
            HttpEntity<?> entity = buildJsonHeaders();
            ResponseEntity<BlockscoutTokenInfo> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    BlockscoutTokenInfo.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error from Blockscout API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Blockscout API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error fetching token details", e);
            throw new RuntimeException("Failed to fetch token details: " + e.getMessage(), e);
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(BlockscoutProvider.class);
    
    private final AppProperties appProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public BlockscoutProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * Get transactions as raw JSON string
     */
    public String getTransactions(String network, String address, String filter) {
        try {
            // Build URL with optional filter
            String url = buildTransactionUrl(network, address, filter);
            
            // Make request with proper headers
            HttpEntity<?> entity = buildJsonHeaders();
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error from Blockscout API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Blockscout API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error fetching transactions", e);
            throw new RuntimeException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get transactions as parsed object
     */
    public BlockscoutTransactionResponse getTransactionsAsObject(String network, String address, String filter) {
        try {
            // Build URL with optional filter
            String url = buildTransactionUrl(network, address, filter);
            
            // Make request with proper headers
            HttpEntity<?> entity = buildJsonHeaders();
            ResponseEntity<BlockscoutTransactionResponse> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                BlockscoutTransactionResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error from Blockscout API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Blockscout API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error fetching transactions", e);
            throw new RuntimeException("Failed to fetch transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to build transaction URL
     */
    private String buildTransactionUrl(String network, String address, String filter) {
        // Validate network
        String baseUrl = appProperties.getBlockscout().get("eth").get(network);
        if (baseUrl == null) {
            throw new IllegalArgumentException("Unsupported blockscout network: " + network);
        }
        
        // Build URL - optionally add filter parameter if it's provided and not empty
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(address, "transactions");
        
        // Only add filter parameter if it's provided and not empty/blank
        if (filter != null && !filter.trim().isEmpty()) {
            builder.queryParam("filter", filter);
        }
        
        String url = builder.toUriString();
        logger.info("Requesting transactions from: {}", url);
        return url;
    }
    
    /**
     * Helper method to build JSON headers
     */
    private HttpEntity<?> buildJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        return new HttpEntity<>(headers);
    }
    
    /**
     * Get token transfers for a specific address
     * @param network Network name (e.g. "sepolia")
     * @param address Wallet address to get token transfers for
     * @param tokenAddress Optional token address to filter by specific token (null for all tokens)
     * @param type Optional filter by type (e.g. "from", "to", null for all)
     * @return List of token transfers
     */
    public TokenTransferListResponse getTokenTransfers(String network, String address, String tokenAddress, String type) {
        try {
            String baseUrl = appProperties.getBlockscout().get("eth").get(network);
            if (baseUrl == null) {
                throw new IllegalArgumentException("Unsupported blockscout network: " + network);
            }
            
            // Construct base URL without the /api/v2/addresses part
            String rootUrl = baseUrl.replace("/api/v2/addresses", "");
            
            // Build URL for token transfers
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rootUrl)
                    .path("/api/v2/addresses/" + address + "/token-transfers");
            
            // Add optional filter parameters
            if (tokenAddress != null && !tokenAddress.trim().isEmpty()) {
                builder.queryParam("token", tokenAddress);
            }
            
            if (type != null && !type.trim().isEmpty()) {
                builder.queryParam("type", type);
            }
            
            String url = builder.toUriString();
            logger.info("Requesting token transfers from: {}", url);
            
            // Make request with proper headers
            HttpEntity<?> entity = buildJsonHeaders();
            ResponseEntity<TokenTransferListResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    TokenTransferListResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error from Blockscout API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Blockscout API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error fetching token transfers", e);
            throw new RuntimeException("Failed to fetch token transfers: " + e.getMessage(), e);
        }
    }
}

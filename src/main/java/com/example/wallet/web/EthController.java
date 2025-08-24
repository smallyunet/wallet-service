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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/eth/{network}")
@Validated
public class EthController {

    /**
     * Query ERC-20 and other token information
     * Example: /v1/eth/sepolia/tokens?tokenSymbol=USDT&type=ERC-20,ERC-721,ERC-1155
     */
    @GetMapping("/tokens")
    public ResponseEntity<BlockscoutTokenListResponse> getTokens(
            @PathVariable String network,
            @RequestParam(name = "tokenSymbol") String tokenSymbol,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(blockscoutService.getTokens(network, tokenSymbol, type));
    }
    
    /**
     * Get detailed information about a specific token by its address
     * Example: /v1/eth/sepolia/tokens/0x84637EaB3d14d481E7242D124e5567B72213D7F2
     * 
     * @param network Network name (e.g. "sepolia")
     * @param tokenAddress Token contract address
     * @return Token details
     */
    @GetMapping("/tokens/{tokenAddress}")
    public ResponseEntity<BlockscoutTokenInfo> getTokenByAddress(
            @PathVariable String network,
            @PathVariable @NotBlank String tokenAddress) {
        return ResponseEntity.ok(blockscoutService.getTokenByAddress(network, tokenAddress));
    }

    private final BalanceService balanceService;
    private final BlockscoutService blockscoutService;

    public EthController(BalanceService balanceService, BlockscoutService blockscoutService) {
        this.balanceService = balanceService;
        this.blockscoutService = blockscoutService;
    }
    
    @GetMapping("/{address}/transactions")
    public ResponseEntity<BlockscoutTransactionResponse> getTransactions(
            @PathVariable String network,
            @PathVariable @NotBlank String address,
            @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(blockscoutService.getTransactionsAsObject(network, address, filter));
    }
    
    @GetMapping("/{address}/transactions/raw")
    public ResponseEntity<String> getTransactionsRaw(
            @PathVariable String network,
            @PathVariable @NotBlank String address,
            @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(blockscoutService.getTransactions(network, address, filter));
    }

    @GetMapping("/{address}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(balanceService.ethBalance(network, address));
    }
    
    /**
     * Get ERC-20 token balance for a wallet address
     * 
     * @param network Ethereum network (e.g., "sepolia")
     * @param tokenAddress The token contract address
     * @param walletAddress The wallet address to check balance for
     * @return Token balance and token details
     */
    @GetMapping("/tokens/{tokenAddress}/balances/{walletAddress}")
    public ResponseEntity<TokenBalanceResponse> getTokenBalance(
            @PathVariable String network,
            @PathVariable @NotBlank String tokenAddress,
            @PathVariable @NotBlank String walletAddress) {
        return ResponseEntity.ok(balanceService.getTokenBalance(network, tokenAddress, walletAddress));
    }
    
    /**
     * Get the current nonce for an address
     * This is useful when building transactions client-side
     */
    @GetMapping("/{address}/nonce")
    public ResponseEntity<NonceResponse> getNonce(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(balanceService.getNonce(network, address));
    }
    
    /**
     * Get gas fee suggestions for the specified network
     * This endpoint provides recommended gas price values at different priority levels
     */
    @GetMapping("/gas-fees")
    public ResponseEntity<GasFeeSuggestion> getGasFees(@PathVariable String network) {
        return ResponseEntity.ok(balanceService.getGasFeeSuggestion(network));
    }
    
    /**
     * Send an ETH transaction
     * This endpoint broadcasts a signed transaction to the network
     * The transaction must be signed by the client before sending
     */
    @PostMapping("/transfer")
    public ResponseEntity<EthTransferResponse> sendTransaction(
            @PathVariable String network,
            @RequestBody @Valid EthTransferRequest request) {
        return ResponseEntity.ok(balanceService.sendEthTransaction(network, request));
    }
    
    /**
     * Send an ERC-20 token transfer transaction
     * This endpoint broadcasts a signed token transfer transaction to the network
     * The transaction must be signed by the client before sending
     */
    @PostMapping("/token-transfer")
    public ResponseEntity<TokenTransferResponse> sendTokenTransaction(
            @PathVariable String network,
            @RequestBody @Valid TokenTransferRequest request) {
        return ResponseEntity.ok(balanceService.sendTokenTransaction(network, request));
    }

    /**
     * Get transaction status and details by transaction hash
     * This endpoint queries detailed information about a transaction including receipt
     */
    @GetMapping("/tx/{txHash}")
    public ResponseEntity<TransactionStatusResponse> getTransactionStatus(
            @PathVariable String network,
            @PathVariable @NotBlank String txHash) {
        return ResponseEntity.ok(balanceService.getTransactionStatus(network, txHash));
    }

    @GetMapping("/config/rpc")
    public ResponseEntity<String> rpc(@PathVariable String network) {
        return ResponseEntity.ok(balanceService.effectiveEthRpc(network));
    }
    
    /**
     * Get token transfers for a specific address
     * 
     * @param network Ethereum network (e.g., "sepolia")
     * @param address Wallet address to get token transfers for
     * @param tokenAddress Optional token address to filter by specific token
     * @param type Optional filter by type (e.g. "from", "to")
     * @return List of token transfers
     */
    @GetMapping("/{address}/token-transfers")
    public ResponseEntity<TokenTransferListResponse> getTokenTransfers(
            @PathVariable String network,
            @PathVariable @NotBlank String address,
            @RequestParam(required = false) String tokenAddress,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(blockscoutService.getTokenTransfers(network, address, tokenAddress, type));
    }
}

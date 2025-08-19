package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
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
}

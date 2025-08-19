package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.service.BalanceService;
import com.example.wallet.service.BlockscoutService;
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

    @GetMapping("/config/rpc")
    public ResponseEntity<String> rpc(@PathVariable String network) {
        return ResponseEntity.ok(balanceService.effectiveEthRpc(network));
    }
}

package com.example.wallet.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/v1/health/ping")
    public String ping() {
        return "pong";
    }
}

package com.example.wallet;

import com.example.wallet.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class WalletServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletServerApplication.class, args);
    }
}

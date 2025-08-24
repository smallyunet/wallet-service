package com.example.wallet.config;

import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerHeaderConfig {
    @Bean
    public OpenApiCustomizer customGlobalHeaders() {
        return openApi -> {
            Parameter clientType = new HeaderParameter()
              .name("X-Client-Type")
              .description("Client type (android/ios)")
              .required(false)
              .example("android");
            Parameter clientKey = new HeaderParameter()
              .name("X-Client-Key")
              .description("Client key")
              .required(false)
              .example("android-demo-key");
            openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    operation.addParametersItem(clientType);
                    operation.addParametersItem(clientKey);
                })
            );
        };
    }
}

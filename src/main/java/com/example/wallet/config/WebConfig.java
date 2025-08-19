package com.example.wallet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AppProperties props;

    public WebConfig(AppProperties props) {
        this.props = props;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = props.getSecurity().getAllowOrigins().split(",");
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(false);
    }
}

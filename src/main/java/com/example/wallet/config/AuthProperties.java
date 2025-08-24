package com.example.wallet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
    private boolean enabled = false;
    private String androidKey;
    private String iosKey;

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public String getAndroidKey() {
        return androidKey;
    }
    public void setAndroidKey(String androidKey) {
        this.androidKey = androidKey;
    }
    public String getIosKey() {
        return iosKey;
    }
    public void setIosKey(String iosKey) {
        this.iosKey = iosKey;
    }
}

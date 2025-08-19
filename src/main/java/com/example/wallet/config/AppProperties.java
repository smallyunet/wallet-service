package com.example.wallet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String env;
    private Rpc rpc = new Rpc();
    private Security security = new Security();

    public static class Rpc {
        private String ethUrl;
        private String btcUrl;
        public String getEthUrl() { return ethUrl; }
        public void setEthUrl(String ethUrl) { this.ethUrl = ethUrl; }
        public String getBtcUrl() { return btcUrl; }
        public void setBtcUrl(String btcUrl) { this.btcUrl = btcUrl; }
    }
    public static class Security {
        private String allowOrigins;
        public String getAllowOrigins() { return allowOrigins; }
        public void setAllowOrigins(String allowOrigins) { this.allowOrigins = allowOrigins; }
    }

    public String getEnv() { return env; }
    public void setEnv(String env) { this.env = env; }
    public Rpc getRpc() { return rpc; }
    public void setRpc(Rpc rpc) { this.rpc = rpc; }
    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }
}

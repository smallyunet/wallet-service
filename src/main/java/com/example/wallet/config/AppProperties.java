package com.example.wallet.config;


import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String env;
    private Map<String, Map<String, String>> blockscout;
    private Rpc rpc = new Rpc();
    private Security security = new Security();

    public static class Rpc {
        private Map<String, String> eth;
        private Map<String, String> btc;

        public Map<String, String> getEth() { return eth; }
        public void setEth(Map<String, String> eth) { this.eth = eth; }
        public Map<String, String> getBtc() { return btc; }
        public void setBtc(Map<String, String> btc) { this.btc = btc; }
    }
    public static class Security {
        private String allowOrigins;
        public String getAllowOrigins() { return allowOrigins; }
        public void setAllowOrigins(String allowOrigins) { this.allowOrigins = allowOrigins; }
    }

    public String getEnv() { return env; }
    public void setEnv(String env) { this.env = env; }
    public Map<String, Map<String, String>> getBlockscout() { return blockscout; }
    public void setBlockscout(Map<String, Map<String, String>> blockscout) { this.blockscout = blockscout; }
    public Rpc getRpc() { return rpc; }
    public void setRpc(Rpc rpc) { this.rpc = rpc; }
    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }
}

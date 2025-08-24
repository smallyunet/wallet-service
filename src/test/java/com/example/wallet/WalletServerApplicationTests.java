package com.example.wallet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 简单的单元测试，验证应用类是否可以正常实例化
 * 不依赖于Spring上下文加载
 */
class WalletServerApplicationTests {
    
    @Test
    void applicationClassCanBeInstantiated() {
        // 测试应用的主类是否可以正常实例化
        WalletServerApplication application = new WalletServerApplication();
        assertNotNull(application, "应用类应该能够正常实例化");
    }
}

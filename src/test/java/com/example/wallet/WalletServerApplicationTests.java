package com.example.wallet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Simple unit test to verify that the application class can be instantiated normally
 * Does not depend on Spring context loading
 */
class WalletServerApplicationTests {
    
    @Test
    void applicationClassCanBeInstantiated() {
        // Test if the main application class can be instantiated normally
        WalletServerApplication application = new WalletServerApplication();
        assertNotNull(application, "Application class should be instantiated properly");
    }
}

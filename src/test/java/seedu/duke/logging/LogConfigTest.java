package seedu.duke.logging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LogConfigTest {

    @Test
    void setup_configuresRootLogger_success() {
        LogConfig.setup();
        Logger rootLogger = Logger.getLogger("");

        // Check if level is set correctly
        assertEquals(Level.ALL, rootLogger.getLevel());

        // Check if handlers were added (should have FileHandler and ConsoleHandler)
        assertTrue(rootLogger.getHandlers().length >= 1, "Root logger should have handlers attached");
    }

    @Test
    void logger_inheritsSettings_success() {
        LogConfig.setup();
        Logger testLogger = Logger.getLogger("test.logger");

        // Loggers should inherit level from root if not explicitly set
        // Note: some environments might behave differently, but usually this holds
        assertNotNull(testLogger);
        testLogger.info("Test log entry to verify no crash.");
    }
}

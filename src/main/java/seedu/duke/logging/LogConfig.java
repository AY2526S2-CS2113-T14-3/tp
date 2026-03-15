package seedu.duke.logging;

import java.util.logging.*;
import java.io.IOException;

public class LogConfig {
    public static void setup() {
        LogManager.getLogManager().reset();
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        try {
            // File Handler: Saves EVERYTHING to 'unitasker.log'
            FileHandler fileHandler = new FileHandler("unitasker.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);

        } catch (IOException e) {
            System.err.println("Logging system failed to initialize: " + e.getMessage());
        }
    }
}

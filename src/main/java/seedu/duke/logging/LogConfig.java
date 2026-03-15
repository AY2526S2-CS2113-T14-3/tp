package seedu.duke.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.WARNING);
            rootLogger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Logging system failed to initialize: " + e.getMessage());
        }
    }
}

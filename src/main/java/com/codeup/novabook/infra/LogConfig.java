package com.codeup.novabook.infra;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.*;

/**
 * Configures a FileHandler for java.util.logging to write app.log at project root.
 */
public final class LogConfig {
    private static boolean configured = false;

    private LogConfig() {}

    public static synchronized void configure() {
        if (configured) return;
        try {
            Logger root = Logger.getLogger("");
            // Remove default console handlers duplication
            for (Handler h : root.getHandlers()) {
                root.removeHandler(h);
            }
            // Console
            ConsoleHandler console = new ConsoleHandler();
            console.setLevel(Level.INFO);
            console.setFormatter(new SimpleFormatter());
            root.addHandler(console);

            // File
            Path logPath = Path.of("app.log");
            FileHandler file = new FileHandler(logPath.toString(), true);
            file.setLevel(Level.INFO);
            file.setFormatter(new SimpleFormatter());
            root.addHandler(file);

            root.setLevel(Level.INFO);
            configured = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to configure logging", e);
        }
    }
}

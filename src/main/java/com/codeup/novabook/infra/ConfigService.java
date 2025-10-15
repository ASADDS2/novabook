package com.codeup.novabook.infra;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Reads custom config from src/main/sources/config.properties without changing your structure.
 */
public class ConfigService {
    private final Properties props = new Properties();

    public ConfigService() {
        Path p = Path.of("src/main/sources/config.properties");
        if (Files.exists(p)) {
            try (FileInputStream fis = new FileInputStream(p.toFile())) {
                props.load(fis);
            } catch (IOException e) {
                // ignore, optional config
            }
        }
    }

    public String get(String key, String def) {
        return props.getProperty(key, def);
    }

    public int getInt(String key, int def) {
        try { return Integer.parseInt(get(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    public long getLong(String key, long def) {
        try { return Long.parseLong(get(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }
}

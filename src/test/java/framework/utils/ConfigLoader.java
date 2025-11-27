package framework.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple environment/config loader with the following precedence:
 * 1. System properties (-Dkey=value)
 * 2. Environment variables
 * 3. .env file values (and optional .env.<env> overlay)
 */
public class ConfigLoader {

    private static final Map<String, String> DOT_ENV_VALUES = new HashMap<>();

    static {
        // Base .env
        loadEnvFile(".env");

        // Optional environment specific file (e.g., .env.jenkins, .env.dev)
        String env = getRawEnvOrProperty("ENV");
        if (env != null && !env.isEmpty()) {
            loadEnvFile(".env." + env.toLowerCase());
        }
    }

    private static void loadEnvFile(String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) return;

        try {
            for (String line : Files.readAllLines(path)) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                int idx = line.indexOf('=');
                if (idx <= 0) continue;

                String key = line.substring(0, idx).trim();
                String val = line.substring(idx + 1).trim();

                // Strip surrounding quotes if present
                if ((val.startsWith("\"") && val.endsWith("\"")) ||
                        (val.startsWith("'") && val.endsWith("'"))) {
                    val = val.substring(1, val.length() - 1);
                }

                DOT_ENV_VALUES.putIfAbsent(key, val);
            }
        } catch (IOException e) {
            System.err.println("Could not read " + filename + ": " + e.getMessage());
        }
    }

    private static String getRawEnvOrProperty(String key) {
        String sys = System.getProperty(key);
        if (sys != null) return sys;
        return System.getenv(key);
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        // 1) System property
        String value = System.getProperty(key);
        if (value != null) return value;

        // 2) Environment variable
        value = System.getenv(key);
        if (value != null) return value;

        // 3) .env
        value = DOT_ENV_VALUES.get(key);
        if (value != null) return value;

        return defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultVal) {
        String v = get(key);
        if (v == null) return defaultVal;
        return Boolean.parseBoolean(v);
    }
}

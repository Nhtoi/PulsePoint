package org.nhtoi.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class EnvManager {
    private static final String ENV_FILE_PATH = ".env";

    public static void setEnv(String key, String value) {
        Dotenv dotenv = Dotenv.load();
        Properties properties = new Properties();
        dotenv.entries().forEach(entry -> properties.setProperty(entry.getKey(), entry.getValue()));

        if (value == null) {
            properties.remove(key);
        } else {
            properties.setProperty(key, value);
        }

        try (OutputStream out = new FileOutputStream(ENV_FILE_PATH)) {
            properties.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dotenv loadEnv() {
        return Dotenv.load();
    }
}

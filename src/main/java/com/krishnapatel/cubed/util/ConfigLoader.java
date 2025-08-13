package com.krishnapatel.cubed.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_PATH = "config.properties";

    public static String getApiKey() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            props.load(fis);
            return props.getProperty("API_KEY");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDB() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            props.load(fis);
            return props.getProperty("DB_PATH");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

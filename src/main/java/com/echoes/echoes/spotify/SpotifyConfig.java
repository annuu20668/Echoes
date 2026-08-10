package com.echoes.echoes.spotify;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SpotifyConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     SpotifyConfig.class.getClassLoader()
                             .getResourceAsStream("spotify.properties")) {

            if (input == null) {
                throw new RuntimeException("spotify.properties not found");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load spotify.properties", e);
        }
    }

    public static String getClientId() {
        return properties.getProperty("spotify.client.id");
    }

    public static String getClientSecret() {
        return properties.getProperty("spotify.client.secret");
    }

}
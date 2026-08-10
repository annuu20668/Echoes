package com.echoes.echoes.spotify;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class SpotifyAuthService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getAccessToken() {

        String clientId = SpotifyConfig.getClientId();
        String clientSecret = SpotifyConfig.getClientSecret();

        String credentials = clientId + ":" + clientSecret;

        String encodedCredentials =
                Base64.getEncoder()
                        .encodeToString(credentials.getBytes());

        URI uri = URI.create(
                "https://accounts.spotify.com/api/token"
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(
                        "Authorization",
                        "Basic " + encodedCredentials
                )
                .header(
                        "Content-Type",
                        "application/x-www-form-urlencoded"
                )
                .POST(
                        HttpRequest.BodyPublishers.ofString(
                                "grant_type=client_credentials"
                        )
                )
                .build();

        try {

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Spotify authentication failed. Status: "
                                + response.statusCode()
                );
            }

            ObjectMapper mapper = new ObjectMapper();

            SpotifyTokenResponse tokenResponse =
                    mapper.readValue(
                            response.body(),
                            SpotifyTokenResponse.class
                    );

            return tokenResponse.getAccessToken();

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
            return "";

        }
    }
}
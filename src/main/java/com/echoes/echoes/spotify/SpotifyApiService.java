package com.echoes.echoes.spotify;
import com.echoes.echoes.model.Track;
import java.util.List;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
public class SpotifyApiService {

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    private final SpotifyAuthService authService =
            new SpotifyAuthService();
    public List<Track> searchTracks(String query) {
        String token = authService.getAccessToken();
        String encodedQuery =
                URLEncoder.encode(
                        query,
                        StandardCharsets.UTF_8
                );
        String url =
                "https://api.spotify.com/v1/search?q="
                        + encodedQuery
                        + "&type=track";
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .GET()
                .build();
        try {

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );
            System.out.println("Status: " + response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Spotify search failed. Status: "
                                + response.statusCode()
                );
            }
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root =
                    mapper.readTree(response.body());
            JsonNode items =
                    root.path("tracks")
                            .path("items");
            List<Track> tracks = new ArrayList<>();
            for (JsonNode item : items) {

                Track track = new Track();
                track.setSpotifyTrackId(
                        item.path("id").asText()
                );
                track.setTitle(
                        item.path("name").asText()
                );

                track.setArtist(
                        item.path("artists")
                                .get(0)
                                .path("name")
                                .asText()
                );

                track.setAlbum(
                        item.path("album")
                                .path("name")
                                .asText()
                );

                track.setSpotifyUrl(
                        item.path("external_urls")
                                .path("spotify")
                                .asText()
                );

                track.setAlbumCoverUrl(
                        item.path("album")
                                .path("images")
                                .get(0)
                                .path("url")
                                .asText()
                );

                tracks.add(track);
            }
            return tracks;
        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
            return List.of();

        }


    }

}
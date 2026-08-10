package com.echoes.echoes.music;

import com.echoes.echoes.model.Track;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItunesApiService {

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    public List<Track> searchTracks(String query) {

        String encodedQuery =
                URLEncoder.encode(
                        query,
                        StandardCharsets.UTF_8
                );

        String url =
                "https://itunes.apple.com/search?term="
                        + encodedQuery
                        + "&entity=song";

        URI uri = URI.create(url);

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();

        try {

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {

                throw new RuntimeException(
                        "iTunes search failed. Status: "
                                + response.statusCode()
                );
            }

            ObjectMapper mapper =
                    new ObjectMapper();

            JsonNode root =
                    mapper.readTree(response.body());

            JsonNode results =
                    root.path("results");

            List<Track> tracks =
                    new ArrayList<>();

            for (JsonNode item : results) {

                Track track = new Track();

                track.setSpotifyTrackId(
                        item.path("trackId").asText()
                );

                track.setTitle(
                        item.path("trackName").asText()
                );

                track.setArtist(
                        item.path("artistName").asText()
                );

                track.setAlbum(
                        item.path("collectionName").asText()
                );

                track.setAlbumCoverUrl(
                        item.path("artworkUrl100").asText()
                );

                track.setSpotifyUrl(
                        item.path("trackViewUrl").asText()
                );
                track.setPreviewUrl(
                        item.path("previewUrl").asText()
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
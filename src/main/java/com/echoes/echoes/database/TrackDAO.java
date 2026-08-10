package com.echoes.echoes.database;

import com.echoes.echoes.model.Track;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackDAO {

    public int saveTrack(Track track) {

        Integer existingTrackId =
                getTrackIdBySpotifyId(
                        track.getSpotifyTrackId()
                );

        if (existingTrackId != null) {
            return existingTrackId;
        }

        return insertTrack(track);
    }


    private Integer getTrackIdBySpotifyId(
            String spotifyTrackId) {

        String sql = """
                SELECT track_id
                FROM tracks
                WHERE spotify_track_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, spotifyTrackId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("track_id");
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve track.",
                    e
            );
        }

        return null;
    }


    public Track getTrackById(int trackId) {

        String sql = """
                SELECT
                    track_id,
                    spotify_track_id,
                    title,
                    artist,
                    album,
                    album_cover_url,
                    spotify_url,
                    preview_url
                FROM tracks
                WHERE track_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, trackId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    Track track = new Track();

                    track.setTrackId(
                            resultSet.getInt("track_id")
                    );

                    track.setSpotifyTrackId(
                            resultSet.getString(
                                    "spotify_track_id"
                            )
                    );

                    track.setTitle(
                            resultSet.getString("title")
                    );

                    track.setArtist(
                            resultSet.getString("artist")
                    );

                    track.setAlbum(
                            resultSet.getString("album")
                    );

                    track.setAlbumCoverUrl(
                            resultSet.getString(
                                    "album_cover_url"
                            )
                    );

                    track.setSpotifyUrl(
                            resultSet.getString(
                                    "spotify_url"
                            )
                    );

                    track.setPreviewUrl(
                            resultSet.getString(
                                    "preview_url"
                            )
                    );

                    return track;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve track by ID.",
                    e
            );
        }

        return null;
    }


    private int insertTrack(Track track) {

        String sql = """
                INSERT INTO tracks
                (
                    spotify_track_id,
                    title,
                    artist,
                    album,
                    album_cover_url,
                    spotify_url,
                    preview_url
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                PreparedStatement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    track.getSpotifyTrackId()
            );

            statement.setString(
                    2,
                    track.getTitle()
            );

            statement.setString(
                    3,
                    track.getArtist()
            );

            statement.setString(
                    4,
                    track.getAlbum()
            );

            statement.setString(
                    5,
                    track.getAlbumCoverUrl()
            );

            statement.setString(
                    6,
                    track.getSpotifyUrl()
            );

            statement.setString(
                    7,
                    track.getPreviewUrl()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    return generatedKeys.getInt(1);
                }
            }

            throw new RuntimeException(
                    "Failed to retrieve generated track ID."
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to insert track.",
                    e
            );
        }
    }
}
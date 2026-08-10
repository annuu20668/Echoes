package com.echoes.echoes.database;

import com.echoes.echoes.model.Memory;
import com.echoes.echoes.model.Track;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoryDAO {

    private final TrackDAO trackDAO = new TrackDAO();


    // =========================================================
    // INSERT MEMORY
    // =========================================================

    public void insertMemory(Memory memory) {

        int trackId = trackDAO.saveTrack(memory.getTrack());

        String sql = """
                INSERT INTO memories
                (
                    user_id,
                    track_id,
                    song_name,
                    artist,
                    title,
                    story,
                    emotion,
                    memory_date,
                    location,
                    audio_path
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    memory.getUserId()
            );

            statement.setInt(
                    2,
                    trackId
            );

            statement.setString(
                    3,
                    memory.getTrack().getTitle()
            );

            statement.setString(
                    4,
                    memory.getTrack().getArtist()
            );

            statement.setString(
                    5,
                    memory.getTitle()
            );

            statement.setString(
                    6,
                    memory.getStory()
            );

            statement.setString(
                    7,
                    memory.getEmotion()
            );

            statement.setDate(
                    8,
                    Date.valueOf(
                            memory.getMemoryDate()
                    )
            );

            statement.setString(
                    9,
                    memory.getLocation()
            );

            statement.setString(
                    10,
                    memory.getAudioPath()
            );

            statement.executeUpdate();

            System.out.println(
                    "Memory Saved Successfully!"
            );

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save memory.",
                    e
            );
        }
    }


    // =========================================================
    // UPDATE MEMORY
    // =========================================================

    public void updateMemory(Memory memory) {

        int trackId =
                trackDAO.saveTrack(
                        memory.getTrack()
                );

        String sql = """
                UPDATE memories
                SET track_id = ?,
                    title = ?,
                    story = ?,
                    emotion = ?,
                    memory_date = ?,
                    location = ?,
                    audio_path = ?
                WHERE memory_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    trackId
            );

            statement.setString(
                    2,
                    memory.getTitle()
            );

            statement.setString(
                    3,
                    memory.getStory()
            );

            statement.setString(
                    4,
                    memory.getEmotion()
            );

            statement.setDate(
                    5,
                    Date.valueOf(
                            memory.getMemoryDate()
                    )
            );

            statement.setString(
                    6,
                    memory.getLocation()
            );

            statement.setString(
                    7,
                    memory.getAudioPath()
            );

            statement.setInt(
                    8,
                    memory.getMemoryId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update memory.",
                    e
            );
        }
    }


    // =========================================================
    // DELETE MEMORY
    // =========================================================

    public boolean deleteMemory(int memoryId) {

        String sql = """
                DELETE FROM memories
                WHERE memory_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    memoryId
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected > 0) {

                System.out.println(
                        "Memory Deleted Successfully!"
                );

                return true;
            }

            return false;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete memory.",
                    e
            );
        }
    }


    // =========================================================
    // GET MEMORIES BY USER
    // =========================================================

    public List<Memory> getMemoriesByUser(int userId) {

        String sql = """
                SELECT
                    m.*,
                    t.spotify_track_id,
                    t.title AS track_title,
                    t.artist,
                    t.album,
                    t.album_cover_url,
                    t.spotify_url,
                    t.preview_url
                FROM memories m
                JOIN tracks t
                    ON m.track_id = t.track_id
                WHERE m.user_id = ?
                ORDER BY m.memory_date DESC
                """;

        List<Memory> memories =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    memories.add(
                            mapMemory(resultSet)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve memories.",
                    e
            );
        }

        return memories;
    }


    // =========================================================
    // GET LATEST MEMORY
    // =========================================================

    public Memory getLatestMemory(int userId) {

        String sql = """
                SELECT
                    m.*,
                    t.spotify_track_id,
                    t.title AS track_title,
                    t.artist,
                    t.album,
                    t.album_cover_url,
                    t.spotify_url,
                    t.preview_url
                FROM memories m
                JOIN tracks t
                    ON m.track_id = t.track_id
                WHERE m.user_id = ?
                ORDER BY m.memory_date DESC
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapMemory(
                            resultSet
                    );
                }

                return null;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve latest memory.",
                    e
            );
        }
    }


    // =========================================================
    // UPDATE FAVORITE STATUS
    // =========================================================

    public boolean updateFavoriteStatus(
            int memoryId,
            boolean favorite) {

        String sql = """
                UPDATE memories
                SET is_favorite = ?
                WHERE memory_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setBoolean(
                    1,
                    favorite
            );

            statement.setInt(
                    2,
                    memoryId
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update favorite status.",
                    e
            );
        }
    }


    // =========================================================
    // GET FAVORITE MEMORIES
    // =========================================================

    public List<Memory> getFavoriteMemoriesByUser(
            int userId) {

        String sql = """
                SELECT
                    m.*,
                    t.spotify_track_id,
                    t.title AS track_title,
                    t.artist,
                    t.album,
                    t.album_cover_url,
                    t.spotify_url,
                    t.preview_url
                FROM memories m
                JOIN tracks t
                    ON m.track_id = t.track_id
                WHERE m.user_id = ?
                  AND m.is_favorite = TRUE
                ORDER BY m.memory_date DESC
                """;

        List<Memory> favoriteMemories =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    userId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    favoriteMemories.add(
                            mapMemory(resultSet)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve favorite memories.",
                    e
            );
        }

        return favoriteMemories;
    }


    // =========================================================
    // SEARCH MEMORIES
    // =========================================================

    public List<Memory> searchMemories(
            int userId,
            String keyword) {

        String sql = """
                SELECT
                    m.*,
                    t.spotify_track_id,
                    t.title AS track_title,
                    t.artist,
                    t.album,
                    t.album_cover_url,
                    t.spotify_url,
                    t.preview_url
                FROM memories m
                JOIN tracks t
                    ON m.track_id = t.track_id
                WHERE m.user_id = ?
                  AND (
                        LOWER(m.title) LIKE ?
                     OR LOWER(m.story) LIKE ?
                     OR LOWER(t.title) LIKE ?
                     OR LOWER(t.artist) LIKE ?
                     OR LOWER(m.emotion) LIKE ?
                     OR LOWER(m.location) LIKE ?
                  )
                ORDER BY m.memory_date DESC
                """;

        List<Memory> memories =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String search =
                    "%" + keyword.toLowerCase() + "%";

            statement.setInt(
                    1,
                    userId
            );

            statement.setString(
                    2,
                    search
            );

            statement.setString(
                    3,
                    search
            );

            statement.setString(
                    4,
                    search
            );

            statement.setString(
                    5,
                    search
            );

            statement.setString(
                    6,
                    search
            );

            statement.setString(
                    7,
                    search
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    memories.add(
                            mapMemory(resultSet)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to search memories.",
                    e
            );
        }

        return memories;
    }


    // =========================================================
    // MAP DATABASE ROW → MEMORY OBJECT
    // =========================================================

    private Memory mapMemory(
            ResultSet resultSet)
            throws SQLException {

        Memory memory =
                new Memory();

        memory.setMemoryId(
                resultSet.getInt(
                        "memory_id"
                )
        );

        memory.setUserId(
                resultSet.getInt(
                        "user_id"
                )
        );

        memory.setTitle(
                resultSet.getString(
                        "title"
                )
        );

        memory.setStory(
                resultSet.getString(
                        "story"
                )
        );

        memory.setEmotion(
                resultSet.getString(
                        "emotion"
                )
        );

        memory.setMemoryDate(
                resultSet
                        .getDate("memory_date")
                        .toLocalDate()
        );

        memory.setLocation(
                resultSet.getString(
                        "location"
                )
        );

        memory.setFavorite(
                resultSet.getBoolean(
                        "is_favorite"
                )
        );

        memory.setAudioPath(
                resultSet.getString(
                        "audio_path"
                )
        );


        // =====================================================
        // CREATE TRACK
        // =====================================================

        Track track =
                new Track();

        track.setTrackId(
                resultSet.getInt(
                        "track_id"
                )
        );

        track.setSpotifyTrackId(
                resultSet.getString(
                        "spotify_track_id"
                )
        );

        track.setTitle(
                resultSet.getString(
                        "track_title"
                )
        );

        track.setArtist(
                resultSet.getString(
                        "artist"
                )
        );

        track.setAlbum(
                resultSet.getString(
                        "album"
                )
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

        // ⭐ THIS WAS THE MISSING PIECE
        track.setPreviewUrl(
                resultSet.getString(
                        "preview_url"
                )
        );


        memory.setTrack(track);

        return memory;
    }
}
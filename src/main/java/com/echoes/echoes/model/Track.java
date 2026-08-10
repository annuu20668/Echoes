package com.echoes.echoes.model;

public class Track {

    // ===== Fields =====

    private int trackId;

    private String spotifyTrackId;

    private String title;

    private String artist;

    private String album;

    private String albumCoverUrl;

    private String spotifyUrl;

    private String previewUrl;


    // ===== Constructors =====

    public Track() {

    }

    public Track(
            String spotifyTrackId,
            String title,
            String artist,
            String album,
            String albumCoverUrl,
            String spotifyUrl,
            String previewUrl) {

        this.spotifyTrackId = spotifyTrackId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumCoverUrl = albumCoverUrl;
        this.spotifyUrl = spotifyUrl;
        this.previewUrl = previewUrl;
    }


    // ===== Getters =====

    public int getTrackId() {
        return trackId;
    }

    public String getSpotifyTrackId() {
        return spotifyTrackId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }


    // ===== Setters =====

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public void setSpotifyTrackId(String spotifyTrackId) {
        this.spotifyTrackId = spotifyTrackId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }


    @Override
    public String toString() {
        return title + " • " + artist;
    }
}
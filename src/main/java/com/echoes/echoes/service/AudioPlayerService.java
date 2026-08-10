package com.echoes.echoes.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AudioPlayerService {

    private MediaPlayer mediaPlayer;

    private double volume = 0.7;


    // =========================================================
    // PLAY AUDIO
    // =========================================================

    public void play(String audioSource) {

        stop();

        if (audioSource == null || audioSource.isBlank()) {
            return;
        }

        String mediaSource;


        // =====================================================
        // LOCAL FILE
        // =====================================================

        File file = new File(audioSource);

        if (file.exists()) {

            mediaSource =
                    file.toURI().toString();

        }

        // =====================================================
        // ONLINE URL
        // =====================================================

        else if (
                audioSource.startsWith("http://")
                        || audioSource.startsWith("https://")
        ) {

            mediaSource = audioSource;

        }

        // =====================================================
        // INVALID SOURCE
        // =====================================================

        else {

            System.out.println(
                    "Audio source not found: "
                            + audioSource
            );

            return;
        }


        // =====================================================
        // CREATE MEDIA PLAYER
        // =====================================================

        try {

            Media media =
                    new Media(mediaSource);

            mediaPlayer =
                    new MediaPlayer(media);

            mediaPlayer.setVolume(volume);


            // =================================================
            // MEDIA ERROR
            // =================================================

            media.setOnError(() -> {

                System.out.println(
                        "Media error: "
                                + media.getError()
                );
            });


            mediaPlayer.setOnError(() -> {

                System.out.println(
                        "MediaPlayer error: "
                                + mediaPlayer.getError()
                );
            });


            // =================================================
            // END OF MEDIA
            // =================================================

            mediaPlayer.setOnEndOfMedia(() -> {

                if (mediaPlayer != null) {

                    mediaPlayer.stop();

                    mediaPlayer.dispose();

                    mediaPlayer = null;
                }
            });


            mediaPlayer.play();

        } catch (Exception e) {

            System.out.println(
                    "Unable to play audio."
            );

            e.printStackTrace();

            mediaPlayer = null;
        }
    }


    // =========================================================
    // PAUSE
    // =========================================================

    public void pause() {

        if (mediaPlayer != null) {

            mediaPlayer.pause();
        }
    }


    // =========================================================
    // RESUME
    // =========================================================

    public void resume() {

        if (mediaPlayer != null) {

            mediaPlayer.play();
        }
    }


    // =========================================================
    // STOP
    // =========================================================

    public void stop() {

        if (mediaPlayer != null) {

            mediaPlayer.stop();

            mediaPlayer.dispose();

            mediaPlayer = null;
        }
    }


    // =========================================================
    // PLAYING STATUS
    // =========================================================

    public boolean isPlaying() {

        return mediaPlayer != null
                && mediaPlayer.getStatus()
                == MediaPlayer.Status.PLAYING;
    }


    // =========================================================
    // PLAYER EXISTS
    // =========================================================

    public boolean hasPlayer() {

        return mediaPlayer != null;
    }


    // =========================================================
    // GET MEDIA PLAYER
    // =========================================================

    public MediaPlayer getMediaPlayer() {

        return mediaPlayer;
    }


    // =========================================================
    // CURRENT TIME
    // =========================================================

    public double getCurrentTime() {

        if (mediaPlayer == null) {

            return 0;
        }

        return mediaPlayer
                .getCurrentTime()
                .toSeconds();
    }


    // =========================================================
    // TOTAL DURATION
    // =========================================================

    public double getTotalDuration() {

        if (mediaPlayer == null) {

            return 0;
        }

        return mediaPlayer
                .getTotalDuration()
                .toSeconds();
    }


    // =========================================================
    // SEEK
    // =========================================================

    public void seek(double seconds) {

        if (mediaPlayer != null) {

            mediaPlayer.seek(
                    javafx.util.Duration.seconds(
                            seconds
                    )
            );
        }
    }


    // =========================================================
    // VOLUME
    // =========================================================

    public void setVolume(double volume) {

        this.volume = volume;

        if (mediaPlayer != null) {

            mediaPlayer.setVolume(volume);
        }
    }
}
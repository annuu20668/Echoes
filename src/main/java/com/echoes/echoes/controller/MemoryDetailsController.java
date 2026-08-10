package com.echoes.echoes.controller;

import com.echoes.echoes.model.Memory;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.AudioPlayerService;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.EditMemorySession;
import com.echoes.echoes.session.MemorySession;
import com.echoes.echoes.util.AppConstants;
import com.echoes.echoes.util.DialogUtil;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MemoryDetailsController {

    // =========================
    // FXML COMPONENTS
    // =========================

    @FXML
    private Label songLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label storyLabel;

    @FXML
    private Label emotionLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button favoriteButton;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    @FXML
    private Slider progressSlider;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label totalTimeLabel;


    // =========================
    // SERVICES
    // =========================

    private final MemoryService memoryService =
            new MemoryService();

    private final AudioPlayerService audioPlayerService =
            new AudioPlayerService();


    // =========================
    // CURRENT MEMORY
    // =========================

    private Memory memory;


    // =========================
    // PLAYER TIMER
    // =========================

    private final AnimationTimer playerTimer =
            new AnimationTimer() {

                @Override
                public void handle(long now) {

                    if (!audioPlayerService.hasPlayer()) {

                        progressSlider.setValue(0);

                        currentTimeLabel.setText("00:00");

                        playerTimer.stop();

                        return;
                    }

                    if (!audioPlayerService.isPlaying()) {
                        return;
                    }

                    double current =
                            audioPlayerService.getCurrentTime();

                    double total =
                            audioPlayerService.getTotalDuration();

                    if (total <= 0) {
                        return;
                    }

                    progressSlider.setMax(total);

                    if (!progressSlider.isValueChanging()) {

                        progressSlider.setValue(current);
                    }

                    currentTimeLabel.setText(
                            formatTime(current)
                    );

                    totalTimeLabel.setText(
                            formatTime(total)
                    );
                }
            };


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        memory = MemorySession.getSelectedMemory();

        if (memory == null) {

            SceneManager.switchScene(
                    AppConstants.DASHBOARD_VIEW
            );

            return;
        }


        // =========================
        // MEMORY INFORMATION
        // =========================

        songLabel.setText(
                "🎵 " + memory.getTrack().getTitle()
        );

        titleLabel.setText(
                "\"" + memory.getTitle() + "\""
        );

        storyLabel.setText(
                memory.getStory()
        );

        emotionLabel.setText(
                "😊 " + memory.getEmotion()
        );


        // =========================
        // LOCATION
        // =========================

        if (memory.getLocation() == null
                || memory.getLocation().isBlank()) {

            locationLabel.setText("");

        } else {

            locationLabel.setText(
                    "📍 " + memory.getLocation()
            );
        }


        // =========================
        // DATE
        // =========================

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy",
                        Locale.ENGLISH
                );

        dateLabel.setText(
                "📅 "
                        + memory.getMemoryDate()
                        .format(formatter)
        );


        // =========================
        // PROGRESS SLIDER
        // =========================

        progressSlider.valueChangingProperty().addListener(
                (observable, wasChanging, isChanging) -> {

                    if (!isChanging) {

                        audioPlayerService.seek(
                                progressSlider.getValue()
                        );
                    }
                }
        );


        progressSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (progressSlider.isValueChanging()) {

                        currentTimeLabel.setText(
                                formatTime(
                                        newValue.doubleValue()
                                )
                        );
                    }
                }
        );


        // =========================
        // VOLUME
        // =========================

        volumeSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {

                    double volume =
                            newValue.doubleValue() / 100.0;

                    audioPlayerService.setVolume(
                            volume
                    );
                }
        );


        // =========================
        // FAVORITE
        // =========================

        updateFavoriteButton();
    }


    // =========================
    // FAVORITE BUTTON DISPLAY
    // =========================

    private void updateFavoriteButton() {

        if (memory.isFavorite()) {

            favoriteButton.setText(
                    "♥ Remove from Favorites"
            );

        } else {

            favoriteButton.setText(
                    "♡ Add to Favorites"
            );
        }
    }


    // =========================
    // FAVORITE ACTION
    // =========================

    @FXML
    private void handleFavorite() {

        boolean newFavoriteStatus =
                !memory.isFavorite();

        boolean updated =
                memoryService.updateFavoriteStatus(
                        memory.getMemoryId(),
                        newFavoriteStatus
                );

        if (updated) {

            memory.setFavorite(
                    newFavoriteStatus
            );

            updateFavoriteButton();

        } else {

            DialogUtil.showError(
                    "Error",
                    "Unable to update favorite status."
            );
        }
    }


    // =========================
    // PLAY
    // =========================

    @FXML
    private void handlePlay() {

        /*
         * Priority:
         *
         * 1. Local audio file
         * 2. iTunes preview URL
         * 3. No audio
         */

        String audioSource = null;


        // =========================
        // LOCAL AUDIO
        // =========================

        if (memory.getAudioPath() != null
                && !memory.getAudioPath().isBlank()) {

            audioSource =
                    memory.getAudioPath();
        }


        // =========================
        // iTUNES PREVIEW
        // =========================

        else if (memory.getTrack() != null
                && memory.getTrack().getPreviewUrl() != null
                && !memory.getTrack().getPreviewUrl().isBlank()) {

            audioSource =
                    memory.getTrack().getPreviewUrl();
        }


        // =========================
        // NOTHING AVAILABLE
        // =========================

        if (audioSource == null) {

            DialogUtil.showError(
                    "No Audio",
                    "No audio or music preview is available for this memory."
            );

            return;
        }


        // =========================
        // ALREADY PLAYING
        // =========================

        if (audioPlayerService.isPlaying()) {
            return;
        }


        // =========================
        // RESUME EXISTING PLAYER
        // =========================

        if (audioPlayerService.hasPlayer()) {

            audioPlayerService.resume();

        }

        // =========================
        // START NEW AUDIO
        // =========================

        else {

            audioPlayerService.play(
                    audioSource
            );
        }


        playerTimer.start();
    }


    // =========================
    // PAUSE
    // =========================

    @FXML
    private void handlePause() {

        audioPlayerService.pause();
    }


    // =========================
    // STOP
    // =========================

    @FXML
    private void handleStop() {

        audioPlayerService.stop();

        playerTimer.stop();

        progressSlider.setValue(0);

        currentTimeLabel.setText("00:00");

        totalTimeLabel.setText("00:00");
    }


    // =========================
    // EDIT
    // =========================

    @FXML
    private void handleEdit() {

        audioPlayerService.stop();

        playerTimer.stop();

        EditMemorySession.startEditing(
                memory
        );

        SceneManager.switchScene(
                AppConstants.ADD_MEMORY_VIEW
        );
    }


    // =========================
    // DELETE
    // =========================

    @FXML
    private void handleDelete() {

        boolean confirmed =
                DialogUtil.showConfirmation(
                        "Delete Memory",
                        "Are you sure you want to delete this memory?\n"
                                + "This action cannot be undone."
                );

        if (!confirmed) {
            return;
        }


        boolean deleted =
                memoryService.deleteMemory(
                        memory.getMemoryId()
                );


        if (deleted) {

            audioPlayerService.stop();

            playerTimer.stop();

            MemorySession.clear();

            EditMemorySession.clear();


            DialogUtil.showSuccess(
                    "Success",
                    "Memory deleted successfully!"
            );


            SceneManager.switchScene(
                    AppConstants.DASHBOARD_VIEW
            );

        } else {

            DialogUtil.showError(
                    "Error",
                    "Unable to delete memory."
            );
        }
    }


    // =========================
    // BACK
    // =========================

    @FXML
    private void handleBack() {

        audioPlayerService.stop();

        playerTimer.stop();

        MemorySession.clear();

        SceneManager.switchScene(
                AppConstants.DASHBOARD_VIEW
        );
    }


    // =========================
    // TIME FORMATTER
    // =========================

    private String formatTime(double seconds) {

        int minutes =
                (int) seconds / 60;

        int remainingSeconds =
                (int) seconds % 60;

        return String.format(
                "%02d:%02d",
                minutes,
                remainingSeconds
        );
    }
}
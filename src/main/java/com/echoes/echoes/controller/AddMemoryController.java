package com.echoes.echoes.controller;
import com.echoes.echoes.util.DialogUtil;
import com.echoes.echoes.model.Memory;
import java.io.File;

import javafx.stage.FileChooser;
import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.EditMemorySession;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import com.echoes.echoes.model.Track;
import com.echoes.echoes.music.ItunesApiService;
import java.util.List;
public class AddMemoryController {
    private final ItunesApiService musicApiService =
            new ItunesApiService();
    @FXML
    private Label pageTitle;

    @FXML
    private Label pageSubtitle;
    @FXML
    private ListView<Track> spotifyResultsList;
    @FXML
    private Button saveButton;

    @FXML
    private TextField songField;

    @FXML
    private TextField artistField;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea storyArea;

    @FXML
    private ComboBox<String> emotionBox;
    @FXML
    private Button chooseSongButton;

    @FXML
    private Label selectedAudioLabel;

    private String selectedAudioPath;
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField locationField;

    private final MemoryService memoryService = new MemoryService();

    private Memory editingMemory;
    private Track selectedTrack;
    @FXML
    private void handleMusicSearch() {

        List<Track> tracks =
                musicApiService.searchTracks(
                        songField.getText()
                );

        spotifyResultsList.getItems().setAll(tracks);

        spotifyResultsList.setManaged(true);
        spotifyResultsList.setVisible(true);
    }

    @FXML
    public void initialize() {

        emotionBox.getItems().addAll(
                "😊 Happy",
                "❤️ Loved",
                "🌧 Nostalgic",
                "😢 Sad",
                "✨ Excited",
                "😌 Peaceful"
        );
        spotifyResultsList.setOnMouseClicked(event -> {

            Track track =
                    spotifyResultsList.getSelectionModel()
                            .getSelectedItem();

            if (track == null) {
                return;
            }

            selectedTrack = track;

            songField.setText(track.getTitle());
            artistField.setText(track.getArtist());

            spotifyResultsList.setVisible(false);
            spotifyResultsList.setManaged(false);
        });

        if (EditMemorySession.isEditing()) {

            editingMemory = EditMemorySession.getMemory();
            selectedTrack = editingMemory.getTrack();

            pageTitle.setText("Edit Memory");
            pageSubtitle.setText("Update a memory you've already captured.");
            saveButton.setText("💙 Update Memory");

            songField.setText(editingMemory.getTrack().getTitle());
            artistField.setText(editingMemory.getTrack().getArtist());
            titleField.setText(editingMemory.getTitle());
            storyArea.setText(editingMemory.getStory());
            emotionBox.setValue(editingMemory.getEmotion());
            datePicker.setValue(editingMemory.getMemoryDate());
            locationField.setText(editingMemory.getLocation());
            selectedAudioPath = editingMemory.getAudioPath();

            if (selectedAudioPath != null && !selectedAudioPath.isBlank()) {

                File file = new File(selectedAudioPath);

                selectedAudioLabel.setText(file.getName());

            }
        }
    }

    @FXML
    private void handleSaveMemory() {

        User currentUser = Session.getCurrentUser();

        if (currentUser == null) {
            DialogUtil.showError(
                    "Error",
                    "No user is logged in."
            );
            return;
        }

        if (songField.getText().trim().isEmpty()
                || artistField.getText().trim().isEmpty()
                || titleField.getText().trim().isEmpty()
                || storyArea.getText().trim().isEmpty()
                || emotionBox.getValue() == null
                || datePicker.getValue() == null) {

            DialogUtil.showError(
                    "Validation Error",
                    "Please fill all required fields."
            );
            return;
        }

        Memory memory;

        if (EditMemorySession.isEditing()) {

            memory = editingMemory;
        } else {

            memory = new Memory();
            memory.setUserId(currentUser.getUserId());
        }

        Track track;

        if (selectedTrack != null) {
            track = selectedTrack;
        } else {
            track = new Track();

            track.setTitle(songField.getText().trim());
            track.setArtist(artistField.getText().trim());
        }

        memory.setTrack(track);
        memory.setTitle(titleField.getText().trim());
        memory.setStory(storyArea.getText().trim());
        memory.setEmotion(emotionBox.getValue());
        memory.setMemoryDate(datePicker.getValue());
        memory.setLocation(locationField.getText().trim());
        memory.setAudioPath(selectedAudioPath);
        if (EditMemorySession.isEditing()) {

            memoryService.updateMemory(memory);

            EditMemorySession.clear();

            DialogUtil.showSuccess(
                    "Success",
                    "Memory updated successfully!"
            );

        } else {

            memoryService.saveMemory(memory);

            DialogUtil.showSuccess(
                    "Success",
                    "Memory saved successfully!"
            );
        }

        SceneManager.switchScene(AppConstants.DASHBOARD_VIEW);
    }

    @FXML
    private void handleBack() {

        EditMemorySession.clear();

        SceneManager.switchScene(AppConstants.DASHBOARD_VIEW);
    }

    @FXML
    private void handleChooseAudio() {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Choose Audio");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Audio Files",
                        "*.mp3",
                        "*.wav",
                        "*.m4a"
                )
        );

        File file = chooser.showOpenDialog(
                saveButton.getScene().getWindow()
        );

        if (file == null) {
            return;
        }

        selectedAudioPath = file.getAbsolutePath();

        selectedAudioLabel.setText(file.getName());
    }
}
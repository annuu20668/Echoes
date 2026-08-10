package com.echoes.echoes.controller;

import com.echoes.echoes.animation.HoverAnimator;
import com.echoes.echoes.model.Memory;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.session.MemorySession;
import com.echoes.echoes.util.AppConstants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class MemoryCardController {

    @FXML
    private VBox cardRoot;

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

    private Memory memory;
    @FXML
    private ImageView albumCoverImage;
    @FXML
    public void initialize() {

        HoverAnimator.attach(cardRoot);

        cardRoot.setOnMouseClicked(event -> {

            if (memory == null) {
                return;
            }

            MemorySession.setSelectedMemory(memory);

            SceneManager.switchScene(AppConstants.MEMORY_DETAILS_VIEW);

        });

    }

    public void setMemory(Memory memory) {

        this.memory = memory;
        String albumCoverUrl =
                memory.getTrack().getAlbumCoverUrl();

        if (albumCoverUrl != null && !albumCoverUrl.isBlank()) {

            Image image =
                    new Image(
                            albumCoverUrl,
                            true
                    );

            albumCoverImage.setImage(image);

            albumCoverImage.setVisible(true);
            albumCoverImage.setManaged(true);

        } else {

            albumCoverImage.setVisible(false);
            albumCoverImage.setManaged(false);
        }
        songLabel.setText("♫ " + memory.getTrack().getTitle());

        titleLabel.setText("\"" + memory.getTitle() + "\"");

        storyLabel.setText(memory.getStory());

        emotionLabel.setText(memory.getEmotion());

        if (memory.getLocation() == null || memory.getLocation().isBlank()) {
            locationLabel.setManaged(false);
            locationLabel.setVisible(false);
        } else {
            locationLabel.setManaged(true);
            locationLabel.setVisible(true);
            locationLabel.setText("📍 " + memory.getLocation());
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        dateLabel.setText("📅 " + memory.getMemoryDate().format(formatter));
    }
}
package com.echoes.echoes.controller;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.application.Platform;
public class EmotionRowController {

    @FXML
    private Label emotionLabel;

    @FXML
    private Label countLabel;

    @FXML
    private Region progressBar;

    public void setEmotion(
            String emotion,
            int count,
            int maxCount,
            int totalCount)  {

        if (emotion.matches("^[^a-zA-Z0-9].*")) {

            emotionLabel.setText(emotion);

        } else {

            emotionLabel.setText(
                    getEmoji(emotion) + " " + emotion
            );
        }

        int percentage = (int) Math.round((count * 100.0) / totalCount);

        countLabel.setText(
                percentage + "% (" + count + ")"
        );

        double width = 420.0 * count / maxCount;

// Start hidden
        progressBar.setPrefWidth(0);

        Platform.runLater(() -> {

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(800),
                            new KeyValue(
                                    progressBar.prefWidthProperty(),
                                    width
                            )
                    )
            );
            System.out.println("Animating to width = " + width);
            timeline.play();
        });

    }
    private String getEmoji(String emotion) {

        return switch (emotion.toLowerCase()) {

            case "happy" -> "😊";
            case "sad" -> "😢";
            case "excited" -> "✨";
            case "calm" -> "😌";
            case "angry" -> "😠";
            case "love" -> "❤️";
            case "fear" -> "😨";
            case "nostalgic" -> "🌙";
            case "grateful" -> "🙏";
            default -> "💙";
        };
    }
}
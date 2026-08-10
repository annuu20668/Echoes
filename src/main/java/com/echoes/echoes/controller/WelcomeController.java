package com.echoes.echoes.controller;

import com.echoes.echoes.navigation.SceneManager;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.util.Duration;

import java.util.Random;

public class WelcomeController {

    @FXML
    private BorderPane root;

    @FXML
    private Pane animationLayer;

    @FXML
    private Label journalIcon;

    @FXML
    private VBox welcomeContent;


    private boolean animationStarted = false;

    private final Random random = new Random();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        // Start welcome screen invisible
        root.setOpacity(0);


        // Fade the entire welcome screen in
        FadeTransition fade =
                new FadeTransition(
                        Duration.seconds(1.2),
                        root
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        fade.play();


        // -----------------------------------------------------
        // Gentle journal breathing animation
        // -----------------------------------------------------

        ScaleTransition idleScale =
                new ScaleTransition(
                        Duration.seconds(2.2),
                        journalIcon
                );

        idleScale.setFromX(1.0);
        idleScale.setFromY(1.0);

        idleScale.setToX(1.06);
        idleScale.setToY(1.06);

        idleScale.setAutoReverse(true);

        idleScale.setCycleCount(
                ScaleTransition.INDEFINITE
        );

        idleScale.play();
    }


    // =========================================================
    // BEGIN JOURNEY
    // =========================================================

    @FXML
    private void handleBegin() {

        // Prevent multiple clicks
        if (animationStarted) {
            return;
        }

        animationStarted = true;

        // Disable interaction during animation
        welcomeContent.setMouseTransparent(true);


        // Start journal animation
        animateJournal();


        // Release the music
        createMusicNotes();


        // Fade the welcome content
        fadeWelcomeContent();
    }


    // =========================================================
    // JOURNAL ANIMATION
    // =========================================================

    private void animateJournal() {

        // -----------------------------------------------------
        // Journal opens / grows
        // -----------------------------------------------------

        ScaleTransition openingScale =
                new ScaleTransition(
                        Duration.seconds(0.8),
                        journalIcon
                );

        openingScale.setFromX(1.0);
        openingScale.setFromY(1.0);

        openingScale.setToX(1.22);
        openingScale.setToY(1.22);


        RotateTransition openingRotate =
                new RotateTransition(
                        Duration.seconds(0.8),
                        journalIcon
                );

        openingRotate.setFromAngle(0);
        openingRotate.setToAngle(-6);


        ParallelTransition opening =
                new ParallelTransition(
                        openingScale,
                        openingRotate
                );


        // -----------------------------------------------------
        // Journal settles
        // -----------------------------------------------------

        ScaleTransition settleScale =
                new ScaleTransition(
                        Duration.seconds(0.45),
                        journalIcon
                );

        settleScale.setFromX(1.22);
        settleScale.setFromY(1.22);

        settleScale.setToX(1.08);
        settleScale.setToY(1.08);


        RotateTransition settleRotate =
                new RotateTransition(
                        Duration.seconds(0.45),
                        journalIcon
                );

        settleRotate.setFromAngle(-6);
        settleRotate.setToAngle(4);


        ParallelTransition settle =
                new ParallelTransition(
                        settleScale,
                        settleRotate
                );


        // -----------------------------------------------------
        // Return to neutral position
        // -----------------------------------------------------

        RotateTransition finalRotate =
                new RotateTransition(
                        Duration.seconds(0.25),
                        journalIcon
                );

        finalRotate.setFromAngle(4);
        finalRotate.setToAngle(0);


        SequentialTransition journalAnimation =
                new SequentialTransition(
                        opening,
                        settle,
                        finalRotate
                );

        journalAnimation.play();
    }


    // =========================================================
    // MUSIC NOTES
    // =========================================================

    private void createMusicNotes() {

        String[] notes = {
                "♪",
                "♫",
                "♬",
                "♪",
                "♫",
                "♩"
        };


        /*
         * Convert the journal's coordinates into the
         * animation layer's coordinate system.
         */
        javafx.geometry.Point2D journalCenter =
                journalIcon.localToScene(
                        journalIcon.getBoundsInLocal().getWidth() / 2,
                        journalIcon.getBoundsInLocal().getHeight() / 2
                );

        javafx.geometry.Point2D layerCenter =
                animationLayer.sceneToLocal(
                        journalCenter
                );


        for (int i = 0; i < notes.length; i++) {

            Label note =
                    new Label(notes[i]);


            // -------------------------------------------------
            // Styling
            // -------------------------------------------------

            note.getStyleClass().add(
                    "floating-note"
            );

            note.setMouseTransparent(true);


            // -------------------------------------------------
            // Add to animation layer
            // -------------------------------------------------

            animationLayer
                    .getChildren()
                    .add(note);


            // -------------------------------------------------
            // Starting position
            // -------------------------------------------------

            double startX =
                    layerCenter.getX()
                            + random.nextDouble() * 40
                            - 20;

            double startY =
                    layerCenter.getY()
                            + random.nextDouble() * 18
                            - 9;


            note.setLayoutX(startX);
            note.setLayoutY(startY);


            // -------------------------------------------------
            // Initial state
            // -------------------------------------------------

            note.setOpacity(0);

            note.setScaleX(0.55);
            note.setScaleY(0.55);


            // -------------------------------------------------
            // Random movement
            // -------------------------------------------------

            double driftX =
                    random.nextDouble() * 160
                            - 80;

            double rise =
                    110
                            + random.nextDouble() * 100;


            // -------------------------------------------------
            // Fade in
            // -------------------------------------------------

            FadeTransition fadeIn =
                    new FadeTransition(
                            Duration.seconds(0.18),
                            note
                    );

            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);


            // -------------------------------------------------
            // Scale
            // -------------------------------------------------

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.seconds(1.7),
                            note
                    );

            scale.setFromX(0.55);
            scale.setFromY(0.55);

            scale.setToX(
                    0.9
                            + random.nextDouble() * 0.35
            );

            scale.setToY(
                    0.9
                            + random.nextDouble() * 0.35
            );


            // -------------------------------------------------
            // Float upward
            // -------------------------------------------------

            TranslateTransition move =
                    new TranslateTransition(
                            Duration.seconds(1.7),
                            note
                    );

            move.setByX(driftX);
            move.setByY(-rise);


            // -------------------------------------------------
            // Rotate
            // -------------------------------------------------

            RotateTransition rotate =
                    new RotateTransition(
                            Duration.seconds(1.7),
                            note
                    );

            rotate.setFromAngle(
                    random.nextDouble() * 30 - 15
            );

            rotate.setToAngle(
                    random.nextDouble() * 70 - 35
            );


            // -------------------------------------------------
            // Fade out
            // -------------------------------------------------

            FadeTransition fadeOut =
                    new FadeTransition(
                            Duration.seconds(0.7),
                            note
                    );

            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);


            // -------------------------------------------------
            // Combine movement
            // -------------------------------------------------

            ParallelTransition movement =
                    new ParallelTransition(
                            scale,
                            move,
                            rotate
                    );


            // -------------------------------------------------
            // Complete note animation
            // -------------------------------------------------

            SequentialTransition noteAnimation =
                    new SequentialTransition(
                            new PauseTransition(
                                    Duration.millis(
                                            i * 110
                                    )
                            ),

                            fadeIn,

                            movement,

                            fadeOut
                    );


            // -------------------------------------------------
            // Remove note after animation
            // -------------------------------------------------

            noteAnimation.setOnFinished(
                    event ->
                            animationLayer
                                    .getChildren()
                                    .remove(note)
            );


            noteAnimation.play();
        }
    }


    // =========================================================
    // FADE OUT + NAVIGATE
    // =========================================================

    private void fadeWelcomeContent() {

        PauseTransition delay =
                new PauseTransition(
                        Duration.seconds(0.65)
                );


        FadeTransition fade =
                new FadeTransition(
                        Duration.seconds(0.9),
                        welcomeContent
                );

        fade.setFromValue(1);
        fade.setToValue(0);


        SequentialTransition transition =
                new SequentialTransition(
                        delay,
                        fade
                );


        transition.setOnFinished(
                event -> {

                    PauseTransition navigationDelay =
                            new PauseTransition(
                                    Duration.seconds(0.35)
                            );


                    navigationDelay.setOnFinished(
                            e ->
                                    SceneManager.switchScene(
                                            "login-view.fxml"
                                    )
                    );


                    navigationDelay.play();
                }
        );


        transition.play();
    }
}
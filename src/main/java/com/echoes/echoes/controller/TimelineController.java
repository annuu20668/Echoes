package com.echoes.echoes.controller;

import com.echoes.echoes.model.Memory;
import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.MemorySession;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TimelineController {

    @FXML
    private VBox timelineContainer;

    @FXML
    private VBox emptyState;

    @FXML
    private Label emptyLabel;

    private final MemoryService memoryService =
            new MemoryService();


    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        User currentUser = Session.getCurrentUser();

        if (currentUser == null) {
            return;
        }

        loadTimeline(currentUser.getUserId());
    }


    // =========================
    // LOAD TIMELINE
    // =========================

    private void loadTimeline(int userId) {

        List<Memory> memories =
                memoryService.getMemoriesByUser(userId);

        timelineContainer.getChildren().clear();

        if (memories.isEmpty()) {

            emptyState.setVisible(true);
            emptyState.setManaged(true);

            return;
        }

        emptyState.setVisible(false);
        emptyState.setManaged(false);

        int currentYear = -1;
        int index = 0;

        for (Memory memory : memories) {

            int memoryYear =
                    memory.getMemoryDate().getYear();


            // =========================
            // YEAR HEADER
            // =========================

            if (memoryYear != currentYear) {

                currentYear = memoryYear;

                Label yearLabel =
                        new Label(
                                String.valueOf(memoryYear)
                        );

                yearLabel.getStyleClass().add(
                        "timeline-year"
                );

                timelineContainer
                        .getChildren()
                        .add(yearLabel);
            }


            // =========================
            // TIMELINE ROW
            // =========================

            HBox row =
                    createTimelineRow(
                            memory,
                            index
                    );

            timelineContainer
                    .getChildren()
                    .add(row);

            index++;
        }
    }


    // =========================
    // CREATE TIMELINE ROW
    // =========================

    private HBox createTimelineRow(
            Memory memory,
            int index) {

        HBox row = new HBox();

        row.setAlignment(Pos.CENTER);
        row.setMinHeight(170);


        // =========================
        // LEFT SIDE
        // =========================

        HBox leftSide = new HBox();

        leftSide.setPrefWidth(400);
        leftSide.setMaxWidth(400);
        leftSide.setAlignment(Pos.CENTER_RIGHT);


        // =========================
        // CENTER DOT
        // =========================

        VBox center = new VBox();

        center.setAlignment(Pos.CENTER);
        center.setPrefWidth(70);
        center.setMinWidth(70);

        Label dot = new Label("●");

        dot.getStyleClass().add(
                "timeline-dot"
        );

        center.getChildren().add(dot);


        // =========================
        // RIGHT SIDE
        // =========================

        HBox rightSide = new HBox();

        rightSide.setPrefWidth(400);
        rightSide.setMaxWidth(400);
        rightSide.setAlignment(Pos.CENTER_LEFT);


        // =========================
        // MEMORY CARD
        // =========================

        VBox card =
                createTimelineCard(memory);


        // =========================
        // CONNECTOR LINE
        // =========================

        Region connector = new Region();

        connector.setPrefWidth(35);
        connector.setMinWidth(35);
        connector.setMaxWidth(35);

        connector.setPrefHeight(2);
        connector.setMinHeight(2);
        connector.setMaxHeight(2);

        connector.getStyleClass().add(
                "timeline-connector"
        );

        // =========================
        // ALTERNATING SIDES
        // =========================

        if (index % 2 == 0) {

            // CARD ───── ●

            leftSide.getChildren().addAll(
                    card,
                    connector
            );

        } else {

            // ● ───── CARD

            rightSide.getChildren().addAll(
                    connector,
                    card
            );
        }


        row.getChildren().addAll(
                leftSide,
                center,
                rightSide
        );

        return row;
    }


    // =========================
    // CREATE TIMELINE CARD
    // =========================

    private VBox createTimelineCard(
            Memory memory) {

        VBox card = new VBox(8);

        card.setPrefWidth(330);
        card.setMaxWidth(330);

        card.getStyleClass().add(
                "timeline-card"
        );


        // =========================
        // DATE
        // =========================

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM",
                        Locale.ENGLISH
                );

        Label dateLabel =
                new Label(
                        memory.getMemoryDate()
                                .format(formatter)
                );

        dateLabel.getStyleClass().add(
                "timeline-date"
        );


        // =========================
        // SONG
        // =========================

        Label songLabel =
                new Label(
                        "♫ " + memory.getTrack().getTitle()
                );

        songLabel.getStyleClass().add(
                "timeline-song"
        );


        // =========================
        // ARTIST
        // =========================

        Label artistLabel =
                new Label(
                        memory.getTrack().getArtist()
                );

        artistLabel.getStyleClass().add(
                "timeline-artist"
        );


        // =========================
        // TITLE
        // =========================

        Label titleLabel =
                new Label(
                        memory.getTitle()
                );

        titleLabel.setWrapText(true);

        titleLabel.getStyleClass().add(
                "timeline-title"
        );


        // =========================
        // STORY PREVIEW
        // =========================

        Label storyLabel =
                new Label(
                        createStoryPreview(
                                memory.getStory()
                        )
                );

        storyLabel.setWrapText(true);

        storyLabel.getStyleClass().add(
                "timeline-story"
        );


        // =========================
        // EMOTION + LOCATION
        // =========================

        String info =
                memory.getEmotion() == null
                        ? ""
                        : memory.getEmotion();

        if (memory.getLocation() != null
                && !memory.getLocation().isBlank()) {

            info += "   •   📍 "
                    + memory.getLocation();
        }

        Label infoLabel =
                new Label(info);

        infoLabel.setWrapText(true);

        infoLabel.getStyleClass().add(
                "timeline-info"
        );


        // =========================
        // ADD EVERYTHING TO CARD
        // =========================

        card.getChildren().addAll(
                dateLabel,
                songLabel,
                artistLabel,
                titleLabel,
                storyLabel,
                infoLabel
        );


        // =========================
        // CLICK CARD
        // =========================

        card.addEventHandler(
                MouseEvent.MOUSE_CLICKED,
                event -> openMemory(memory)
        );

        return card;
    }


    // =========================
    // STORY PREVIEW
    // =========================

    private String createStoryPreview(
            String story) {

        if (story == null
                || story.isBlank()) {

            return "";
        }

        int maxLength = 100;

        if (story.length() <= maxLength) {
            return story;
        }

        return story.substring(
                0,
                maxLength
        ) + "...";
    }


    // =========================
    // OPEN MEMORY
    // =========================

    private void openMemory(
            Memory memory) {

        MemorySession.setSelectedMemory(
                memory
        );

        SceneManager.switchScene(
                AppConstants.MEMORY_DETAILS_VIEW
        );
    }


    // =========================
    // BACK
    // =========================

    @FXML
    private void handleBack() {

        SceneManager.switchScene(
                AppConstants.DASHBOARD_VIEW
        );
    }
}
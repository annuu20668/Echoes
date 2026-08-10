package com.echoes.echoes.controller;

import com.echoes.echoes.ui.IconFactory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Button;
import com.echoes.echoes.model.Memory;
import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import com.echoes.echoes.session.MemorySession;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private FlowPane recentMemoriesPane;

    @FXML
    private TextField searchField;

    @FXML
    private Label latestSongLabel;

    @FXML
    private Button homeButton;

    @FXML
    private Button memoriesButton;

    @FXML
    private Button favoritesButton;

    @FXML
    private Button timelineButton;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Label latestTitleLabel;

    @FXML
    private Label latestStoryLabel;

    @FXML
    private Label latestEmotionLabel;

    @FXML
    private Label latestLocationLabel;

    @FXML
    private Label latestDateLabel;

    private final MemoryService memoryService =
            new MemoryService();

    private List<Memory> allMemories =
            new ArrayList<>();

    private Memory latestMemory;


    @FXML
    public void initialize() {

        User currentUser =
                Session.getCurrentUser();

        if (currentUser == null) {
            return;
        }

        // Initialize sidebar icons
        initializeSidebarIcons();

        // Set time-based greeting
        welcomeLabel.setText(
                getGreeting()
                        + ", "
                        + currentUser.getName()
                        + " "
                        + getGreetingIcon()
        );

        // Load today's/latest echo
        loadLatestMemory(
                currentUser.getUserId()
        );

        // Load all memories
        allMemories =
                memoryService.getMemoriesByUser(
                        currentUser.getUserId()
                );

        displayMemories(allMemories);

        // Search listener
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filterMemories(newValue)
        );
    }


    private void filterMemories(String keyword) {

        if (keyword == null || keyword.isBlank()) {

            displayMemories(allMemories);

            return;
        }

        String search =
                keyword.toLowerCase();

        List<Memory> filtered =
                new ArrayList<>();

        for (Memory memory : allMemories) {

            if (
                    contains(
                            memory.getTrack().getArtist(),
                            search
                    )
                            ||
                            contains(
                                    memory.getTitle(),
                                    search
                            )
                            ||
                            contains(
                                    memory.getStory(),
                                    search
                            )
                            ||
                            contains(
                                    memory.getEmotion(),
                                    search
                            )
                            ||
                            contains(
                                    memory.getLocation(),
                                    search
                            )
            ) {

                filtered.add(memory);
            }
        }

        displayMemories(filtered);
    }


    private boolean contains(
            String text,
            String keyword
    ) {

        return text != null
                &&
                text.toLowerCase()
                        .contains(keyword);
    }


    private void displayMemories(
            List<Memory> memories
    ) {

        recentMemoriesPane
                .getChildren()
                .clear();

        for (Memory memory : memories) {

            try {

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/fxml/memory-card.fxml"
                                )
                        );

                VBox card =
                        loader.load();

                MemoryCardController controller =
                        loader.getController();

                controller.setMemory(memory);

                recentMemoriesPane
                        .getChildren()
                        .add(card);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    private void loadLatestMemory(
            int userId
    ) {

        latestMemory =
                memoryService.getLatestMemory(
                        userId
                );

        Memory memory =
                latestMemory;

        if (memory == null) {

            latestSongLabel.setText(
                    "🎵 No Memories Yet"
            );

            latestTitleLabel.setText(
                    "Create your first memory"
            );

            latestStoryLabel.setText(
                    "Your newest memory will appear here."
            );

            latestEmotionLabel.setText("");

            latestLocationLabel.setText("");

            latestDateLabel.setText("");

            return;
        }

        latestSongLabel.setText(
                "🎵 "
                        + memory.getTrack().getArtist()
        );

        latestTitleLabel.setText(
                "\""
                        + memory.getTitle()
                        + "\""
        );

        latestStoryLabel.setText(
                memory.getStory()
        );

        latestEmotionLabel.setText(
                memory.getEmotion()
        );

        if (
                memory.getLocation() == null
                        ||
                        memory.getLocation().isBlank()
        ) {

            latestLocationLabel.setText("");

        } else {

            latestLocationLabel.setText(
                    "📍 "
                            + memory.getLocation()
            );
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy",
                        Locale.ENGLISH
                );

        latestDateLabel.setText(
                "📅 "
                        + memory
                        .getMemoryDate()
                        .format(formatter)
        );
    }


    /*
     * =====================================================
     * TIME-BASED GREETING
     * =====================================================
     *
     * 00:00 - 04:59  → Good Night
     * 05:00 - 11:59  → Good Morning
     * 12:00 - 16:59  → Good Afternoon
     * 17:00 - 20:59  → Good Evening
     * 21:00 - 23:59  → Good Night
     *
     * This ensures midnight is NOT treated as morning.
     */

    private String getGreeting() {

        LocalTime now =
                LocalTime.now();

        if (
                now.isBefore(
                        LocalTime.of(5, 0)
                )
        ) {

            return "Good Night";
        }

        if (
                now.isBefore(
                        LocalTime.NOON
                )
        ) {

            return "Good Morning";
        }

        if (
                now.isBefore(
                        LocalTime.of(17, 0)
                )
        ) {

            return "Good Afternoon";
        }

        if (
                now.isBefore(
                        LocalTime.of(21, 0)
                )
        ) {

            return "Good Evening";
        }

        return "Good Night";
    }


    private String getGreetingIcon() {

        LocalTime now =
                LocalTime.now();

        if (
                now.isBefore(
                        LocalTime.of(5, 0)
                )
        ) {

            return "🌙";
        }

        if (
                now.isBefore(
                        LocalTime.NOON
                )
        ) {

            return "☀️";
        }

        if (
                now.isBefore(
                        LocalTime.of(17, 0)
                )
        ) {

            return "🌤️";
        }

        if (
                now.isBefore(
                        LocalTime.of(21, 0)
                )
        ) {

            return "🌆";
        }

        return "🌙";
    }


    @FXML
    private void handleAddMemory() {

        SceneManager.switchScene(
                AppConstants.ADD_MEMORY_VIEW
        );
    }


    @FXML
    private void handleMyMemories() {

        SceneManager.switchScene(
                AppConstants.MY_MEMORIES_VIEW
        );
    }


    @FXML
    private void handleFavorites() {

        SceneManager.switchScene(
                AppConstants.FAVORITES_VIEW
        );
    }


    @FXML
    private void handleTimeline() {

        SceneManager.switchScene(
                AppConstants.TIMELINE_VIEW
        );
    }


    @FXML
    private void handleOpenLatestMemory() {

        if (latestMemory == null) {
            return;
        }

        MemorySession.setSelectedMemory(
                latestMemory
        );

        SceneManager.switchScene(
                AppConstants.MEMORY_DETAILS_VIEW
        );
    }


    private void initializeSidebarIcons() {

        homeButton.setGraphic(
                IconFactory.create(
                        FontAwesomeIcon.HOME
                )
        );

        memoriesButton.setGraphic(
                IconFactory.create(
                        FontAwesomeIcon.BOOK
                )
        );

        favoritesButton.setGraphic(
                IconFactory.create(
                        FontAwesomeIcon.HEART
                )
        );

        timelineButton.setGraphic(
                IconFactory.create(
                        FontAwesomeIcon.CLOCK_ALT
                )
        );

        statisticsButton.setGraphic(
                IconFactory.create(
                        FontAwesomeIcon.BAR_CHART
                )
        );

        settingsButton.setGraphic(
                IconFactory.create(
                        FontAwesomeIcon.COG
                )
        );
    }


    @FXML
    private void handleStatistics() {

        SceneManager.switchScene(
                AppConstants.STATISTICS_VIEW
        );
    }


    @FXML
    private void handleSettings() {

        SceneManager.switchScene(
                AppConstants.SETTINGS_VIEW
        );
    }
}
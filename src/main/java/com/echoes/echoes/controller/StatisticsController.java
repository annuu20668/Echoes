package com.echoes.echoes.controller;

import com.echoes.echoes.model.Memory;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsController {

    @FXML
    private Label calendarMonthLabel;

    @FXML
    private FlowPane statsPane;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private VBox emotionContainer;

    @FXML
    private Label topEmotionEmojiLabel;

    @FXML
    private Label totalMemoriesLabel;

    @FXML
    private Label favoriteMemoriesLabel;

    @FXML
    private Label topEmotionLabel;

    @FXML
    private Label topArtistLabel;

    @FXML
    private Label thisMonthLabel;

    @FXML
    private Label thisYearLabel;

    private final MemoryService memoryService =
            new MemoryService();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {

            SceneManager.switchScene(
                    AppConstants.LOGIN_VIEW
            );

            return;
        }

        int userId =
                Session.getCurrentUser().getUserId();


        // =====================================================
        // LOAD USER MEMORIES ONCE
        // =====================================================

        List<Memory> memories =
                memoryService.getMemoriesByUser(userId);


        // =====================================================
        // BASIC STATISTICS
        // =====================================================

        totalMemoriesLabel.setText(
                String.valueOf(memories.size())
        );


        favoriteMemoriesLabel.setText(
                String.valueOf(
                        memoryService
                                .getFavoriteMemoriesByUser(userId)
                                .size()
                )
        );


        // =====================================================
        // TOP EMOTION
        // =====================================================

        String topEmotion =
                findTopEmotion(memories);

        topEmotionLabel.setText(
                cleanEmotionName(topEmotion)
        );

        topEmotionEmojiLabel.setText(
                getEmotionEmoji(topEmotion)
        );


        // =====================================================
        // TOP ARTIST
        // =====================================================

        topArtistLabel.setText(
                findTopArtist(memories)
        );


        // =====================================================
        // TIME STATISTICS
        // =====================================================

        thisMonthLabel.setText(
                String.valueOf(
                        getMemoriesThisMonth(memories)
                )
        );

        thisYearLabel.setText(
                String.valueOf(
                        getMemoriesThisYear(memories)
                )
        );


        // =====================================================
        // EMOTION ANALYTICS
        // =====================================================

        loadEmotionAnalytics(memories);


        // =====================================================
        // MEMORY ACTIVITY CALENDAR
        // =====================================================

        createCalendar(memories);
    }


    // =========================================================
    // EMOTION ANALYTICS
    // =========================================================

    private void loadEmotionAnalytics(
            List<Memory> memories) {

        emotionContainer
                .getChildren()
                .clear();


        Map<String, Integer> emotionCount =
                new HashMap<>();


        for (Memory memory : memories) {

            String emotion =
                    memory.getEmotion();

            if (emotion == null
                    || emotion.isBlank()) {

                continue;
            }

            emotionCount.put(
                    emotion,
                    emotionCount.getOrDefault(
                            emotion,
                            0
                    ) + 1
            );
        }


        if (emotionCount.isEmpty()) {
            return;
        }


        int max = 1;

        for (Integer count :
                emotionCount.values()) {

            if (count > max) {
                max = count;
            }
        }


        List<Map.Entry<String, Integer>>
                sortedEmotions =
                new ArrayList<>(
                        emotionCount.entrySet()
                );


        sortedEmotions.sort(
                (first, second) ->
                        Integer.compare(
                                second.getValue(),
                                first.getValue()
                        )
        );


        for (Map.Entry<String, Integer> entry :
                sortedEmotions) {

            try {

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/fxml/emotion-row.fxml"
                                )
                        );


                HBox row =
                        loader.load();


                EmotionRowController controller =
                        loader.getController();


                controller.setEmotion(
                        entry.getKey(),
                        entry.getValue(),
                        max,
                        memories.size()
                );


                emotionContainer
                        .getChildren()
                        .add(row);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    // =========================================================
    // CALENDAR
    // =========================================================

    private void createCalendar(
            List<Memory> memories) {

        calendarGrid
                .getChildren()
                .clear();


        // -----------------------------------------------------
        // CURRENT MONTH
        // -----------------------------------------------------

        YearMonth currentMonth =
                YearMonth.now();


        calendarMonthLabel.setText(
                currentMonth.getMonth()
                        .name()
                        .substring(0, 1)
                        +
                        currentMonth.getMonth()
                                .name()
                                .substring(1)
                                .toLowerCase()
                        +
                        " "
                        +
                        currentMonth.getYear()
        );


        // -----------------------------------------------------
        // DAY HEADERS
        // -----------------------------------------------------

        String[] days = {
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat",
                "Sun"
        };


        for (int i = 0;
             i < days.length;
             i++) {

            Label dayLabel =
                    new Label(days[i]);

            dayLabel
                    .getStyleClass()
                    .add("calendar-header");


            calendarGrid.add(
                    dayLabel,
                    i,
                    0
            );
        }


        // -----------------------------------------------------
        // MONTH INFORMATION
        // -----------------------------------------------------

        LocalDate firstDay =
                currentMonth.atDay(1);

        int daysInMonth =
                currentMonth.lengthOfMonth();


        int startColumn =
                firstDay
                        .getDayOfWeek()
                        .getValue() - 1;


        int row = 1;

        int column =
                startColumn;


        // -----------------------------------------------------
        // CREATE CALENDAR CELLS
        // -----------------------------------------------------

        for (int day = 1;
             day <= daysInMonth;
             day++) {


            LocalDate currentDate =
                    currentMonth.atDay(day);


            Label cell =
                    new Label(
                            String.valueOf(day)
                    );


            cell.getStyleClass()
                    .add("calendar-cell");


            // -------------------------------------------------
            // CHECK WHETHER A MEMORY EXISTS ON THIS DATE
            // -------------------------------------------------

            if (hasMemoryOnDate(
                    memories,
                    currentDate)) {

                cell.getStyleClass()
                        .add(
                                "calendar-cell-active"
                        );
            }


            calendarGrid.add(
                    cell,
                    column,
                    row
            );


            column++;


            if (column == 7) {

                column = 0;
                row++;
            }
        }
    }


    // =========================================================
    // CHECK MEMORY DATE
    // =========================================================

    private boolean hasMemoryOnDate(
            List<Memory> memories,
            LocalDate date) {

        for (Memory memory : memories) {

            if (memory.getMemoryDate() != null
                    && date.equals(
                    memory.getMemoryDate())) {

                return true;
            }
        }

        return false;
    }


    // =========================================================
    // TOP EMOTION
    // =========================================================

    private String findTopEmotion(
            List<Memory> memories) {

        if (memories.isEmpty()) {
            return "-";
        }


        Map<String, Integer> emotionCount =
                new HashMap<>();


        for (Memory memory : memories) {

            String emotion =
                    memory.getEmotion();


            if (emotion == null
                    || emotion.isBlank()) {

                continue;
            }


            emotionCount.put(
                    emotion,
                    emotionCount.getOrDefault(
                            emotion,
                            0
                    ) + 1
            );
        }


        String topEmotion = "-";

        int max = 0;


        for (Map.Entry<String, Integer> entry :
                emotionCount.entrySet()) {

            if (entry.getValue() > max) {

                max =
                        entry.getValue();

                topEmotion =
                        entry.getKey();
            }
        }


        return topEmotion;
    }


    // =========================================================
    // CLEAN EMOTION NAME
    // =========================================================

    private String cleanEmotionName(
            String emotion) {

        if (emotion == null
                || emotion.equals("-")) {

            return "-";
        }


        return emotion
                .replace("😊 ", "")
                .replace("❤️ ", "")
                .replace("🌧 ", "")
                .replace("😢 ", "")
                .replace("✨ ", "")
                .replace("😌 ", "")
                .trim();
    }


    // =========================================================
    // EMOTION EMOJI
    // =========================================================

    private String getEmotionEmoji(
            String emotion) {

        if (emotion == null
                || emotion.isBlank()
                || emotion.equals("-")) {

            return "💙";
        }


        String value =
                emotion.toLowerCase();


        if (value.contains("happy")) {
            return "😊";
        }

        if (value.contains("loved")
                || value.contains("love")) {

            return "❤️";
        }

        if (value.contains("nostalgic")) {
            return "🌧";
        }

        if (value.contains("sad")) {
            return "😢";
        }

        if (value.contains("excited")) {
            return "✨";
        }

        if (value.contains("peaceful")
                || value.contains("calm")) {

            return "😌";
        }


        return "💙";
    }


    // =========================================================
    // TOP ARTIST
    // =========================================================

    private String findTopArtist(
            List<Memory> memories) {

        if (memories.isEmpty()) {
            return "-";
        }


        Map<String, Integer> artistCount =
                new HashMap<>();


        for (Memory memory : memories) {

            if (memory.getTrack() == null) {
                continue;
            }


            String artist =
                    memory.getTrack()
                            .getArtist();


            if (artist == null
                    || artist.isBlank()) {

                continue;
            }


            artistCount.put(
                    artist,
                    artistCount.getOrDefault(
                            artist,
                            0
                    ) + 1
            );
        }


        String topArtist = "-";

        int max = 0;


        for (Map.Entry<String, Integer> entry :
                artistCount.entrySet()) {

            if (entry.getValue() > max) {

                max =
                        entry.getValue();

                topArtist =
                        entry.getKey();
            }
        }


        return topArtist;
    }


    // =========================================================
    // MEMORIES THIS MONTH
    // =========================================================

    private int getMemoriesThisMonth(
            List<Memory> memories) {

        int count = 0;

        LocalDate today =
                LocalDate.now();


        for (Memory memory : memories) {

            LocalDate date =
                    memory.getMemoryDate();


            if (date != null
                    && date.getYear()
                    == today.getYear()
                    && date.getMonth()
                    == today.getMonth()) {

                count++;
            }
        }


        return count;
    }


    // =========================================================
    // MEMORIES THIS YEAR
    // =========================================================

    private int getMemoriesThisYear(
            List<Memory> memories) {

        int count = 0;

        int currentYear =
                LocalDate.now().getYear();


        for (Memory memory : memories) {

            LocalDate date =
                    memory.getMemoryDate();


            if (date != null
                    && date.getYear()
                    == currentYear) {

                count++;
            }
        }


        return count;
    }


    // =========================================================
    // BACK
    // =========================================================

    @FXML
    private void handleBack() {

        SceneManager.switchScene(
                AppConstants.DASHBOARD_VIEW
        );
    }
}
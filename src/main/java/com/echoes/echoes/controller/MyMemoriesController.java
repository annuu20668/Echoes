package com.echoes.echoes.controller;

import com.echoes.echoes.model.Memory;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MyMemoriesController {

    @FXML
    private VBox memoriesContainer;

    private final MemoryService memoryService = new MemoryService();

    @FXML
    public void initialize() {

        if (Session.getCurrentUser() == null) {
            return;
        }

        int userId = Session.getCurrentUser().getUserId();

        List<Memory> memories = memoryService.getMemoriesByUser(userId);

        for (Memory memory : memories) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fxml/memory-card.fxml")
                );

                Parent card = loader.load();

                MemoryCardController controller = loader.getController();

                controller.setMemory(memory);

                memoriesContainer.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleHome() {
        SceneManager.switchScene(AppConstants.DASHBOARD_VIEW);
    }
}
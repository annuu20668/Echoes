package com.echoes.echoes.controller;

import com.echoes.echoes.model.Memory;
import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.MemoryService;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class FavoritesController {

    @FXML
    private VBox favoritesPane;

    @FXML
    private Label emptyLabel;

    private final MemoryService memoryService =
            new MemoryService();


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        User currentUser =
                Session.getCurrentUser();

        if (currentUser == null) {

            SceneManager.switchScene(
                    AppConstants.LOGIN_VIEW
            );

            return;
        }

        loadFavorites(
                currentUser.getUserId()
        );
    }


    // =========================================================
    // LOAD FAVORITES
    // =========================================================

    private void loadFavorites(int userId) {

        List<Memory> favorites =
                memoryService.getFavoriteMemoriesByUser(userId);

        favoritesPane.getChildren().clear();


        // =====================================================
        // NO FAVORITES
        // =====================================================

        if (favorites.isEmpty()) {

            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);

            return;
        }


        // =====================================================
        // FAVORITES FOUND
        // =====================================================

        emptyLabel.setVisible(false);
        emptyLabel.setManaged(false);


        for (Memory memory : favorites) {

            try {

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/fxml/memory-card.fxml"
                                )
                        );

                VBox card = loader.load();

                MemoryCardController controller =
                        loader.getController();

                controller.setMemory(memory);

                favoritesPane
                        .getChildren()
                        .add(card);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
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
package com.echoes.echoes.controller;

import com.echoes.echoes.database.UserDAO;
import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;
import com.echoes.echoes.util.DialogUtil;
import com.echoes.echoes.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
public class SettingsController {

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> themeBox;
    @FXML
    private TextField emailField;

    @FXML
    private Label statusLabel;

    private final UserDAO userDAO =
            new UserDAO();

    private User currentUser;


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        currentUser =
                Session.getCurrentUser();

        if (currentUser == null) {

            SceneManager.switchScene(
                    AppConstants.LOGIN_VIEW
            );

            return;
        }


        // =========================================================
        // LOAD USER PROFILE
        // =========================================================

        nameField.setText(
                currentUser.getName()
        );

        emailField.setText(
                currentUser.getEmail()
        );


        // =========================================================
        // LOAD THEMES
        // =========================================================

        themeBox.getItems().setAll(
                "🌙 Echoes Midnight",
                "☀ Soft Day"
        );


        // =========================================================
        // SELECT CURRENT THEME
        // =========================================================

        if (ThemeManager.getCurrentTheme()
                == ThemeManager.Theme.SOFT_DAY) {

            themeBox.setValue(
                    "☀ Soft Day"
            );

        } else {

            themeBox.setValue(
                    "🌙 Echoes Midnight"
            );
        }


        // =========================================================
        // THEME CHANGE
        // =========================================================

        themeBox.setOnAction(event ->
                handleThemeChange()
        );
    }
    private void handleThemeChange() {

        String selectedTheme =
                themeBox.getValue();


        if ("☀ Soft Day".equals(selectedTheme)) {

            ThemeManager.setTheme(
                    ThemeManager.Theme.SOFT_DAY
            );

        } else {

            ThemeManager.setTheme(
                    ThemeManager.Theme.MIDNIGHT
            );
        }


        // Re-load current page so the theme is visible immediately

        SceneManager.switchScene(
                AppConstants.SETTINGS_VIEW
        );
    }

    // =========================================================
    // SAVE CHANGES
    // =========================================================

    @FXML
    private void handleSave() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();


        // =========================
        // VALIDATION
        // =========================

        if (name.isEmpty()
                || email.isEmpty()) {

            DialogUtil.showError(
                    "Validation Error",
                    "Name and email cannot be empty."
            );

            return;
        }


        if (!email.contains("@")
                || !email.contains(".")) {

            DialogUtil.showError(
                    "Invalid Email",
                    "Please enter a valid email address."
            );

            return;
        }


        try {

            boolean updated =
                    userDAO.updateUser(
                            currentUser.getUserId(),
                            name,
                            email
                    );

            if (!updated) {

                DialogUtil.showError(
                        "Update Failed",
                        "Unable to update your profile."
                );

                return;
            }


            // =========================
            // UPDATE SESSION
            // =========================

            currentUser.setName(name);
            currentUser.setEmail(email);


            statusLabel.setText(
                    "Changes saved successfully."
            );


        } catch (RuntimeException e) {

            DialogUtil.showError(
                    "Update Failed",
                    "Unable to update your profile."
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    @FXML
    private void handleLogout() {

        Session.clearSession();

        SceneManager.switchScene(
                AppConstants.LOGIN_VIEW
        );
    }


    // =========================================================
    // NAVIGATION
    // =========================================================

    @FXML
    private void handleHome() {

        SceneManager.switchScene(
                AppConstants.DASHBOARD_VIEW
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
    private void handleStatistics() {

        SceneManager.switchScene(
                AppConstants.STATISTICS_VIEW
        );
    }
}
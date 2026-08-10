package com.echoes.echoes.controller;

import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.UserService;
import com.echoes.echoes.util.AppConstants;
import com.echoes.echoes.util.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleRegister() {

        if (nameField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()
                || passwordField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields.");
            alert.showAndWait();

            return;
        }

        User user = new User();

        user.setName(nameField.getText().trim());
        user.setEmail(emailField.getText().trim());

        String hashedPassword = PasswordUtil.hashPassword(passwordField.getText());
        user.setPasswordHash(hashedPassword);

        try {

            userService.registerUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully!");
            alert.showAndWait();

            SceneManager.switchScene(AppConstants.LOGIN_VIEW);

        } catch (IllegalArgumentException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogin() {
        SceneManager.switchScene(AppConstants.LOGIN_VIEW);
    }
}
package com.echoes.echoes.controller;

import com.echoes.echoes.model.User;
import com.echoes.echoes.navigation.SceneManager;
import com.echoes.echoes.service.AuthenticationService;
import com.echoes.echoes.session.Session;
import com.echoes.echoes.util.AppConstants;
import com.echoes.echoes.util.DialogUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button togglePasswordButton;

    private boolean passwordVisible = false;

    private final AuthenticationService authenticationService =
            new AuthenticationService();

    @FXML
    private void handleLogin() {

        String email = emailField.getText().trim();

        String password = passwordVisible
                ? visiblePasswordField.getText()
                : passwordField.getText();

        if (email.isEmpty() || password.trim().isEmpty()) {

            DialogUtil.showError(
                    "Login Error",
                    "Please fill all the fields."
            );
            return;
        }

        User user = authenticationService.login(email, password);

        if (user != null) {

            Session.setCurrentUser(user);

            DialogUtil.showSuccess(
                    "Success",
                    "Login Successful!"
            );

            SceneManager.switchScene(AppConstants.DASHBOARD_VIEW);

        } else {

            DialogUtil.showError(
                    "Login Failed",
                    "Invalid email or password."
            );
        }
    }

    @FXML
    private void togglePassword() {

        if (passwordVisible) {

            passwordField.setText(visiblePasswordField.getText());

            passwordField.setVisible(true);
            passwordField.setManaged(true);

            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);

            togglePasswordButton.setText("Show");

        } else {

            visiblePasswordField.setText(passwordField.getText());

            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);

            passwordField.setVisible(false);
            passwordField.setManaged(false);

            togglePasswordButton.setText("Hide");
        }

        passwordVisible = !passwordVisible;
    }

    @FXML
    private void handleRegister() {
        SceneManager.switchScene(AppConstants.REGISTER_VIEW);
    }
}
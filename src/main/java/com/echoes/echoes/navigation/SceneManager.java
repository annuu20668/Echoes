package com.echoes.echoes.navigation;

import com.echoes.echoes.util.ThemeManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;


    // =========================================================
    // SET STAGE
    // =========================================================

    public static void setStage(Stage primaryStage) {

        stage = primaryStage;
    }


    // =========================================================
    // SWITCH SCENE
    // =========================================================

    public static void switchScene(String fxmlFile) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            SceneManager.class.getResource(
                                    "/fxml/" + fxmlFile
                            )
                    );

            Parent root = loader.load();


            // =================================================
            // CREATE SCENE
            // =================================================

            Scene scene =
                    new Scene(
                            root,
                            900,
                            600
                    );


            // =================================================
            // BASE ECHOES DESIGN
            // =================================================

            scene.getStylesheets().add(
                    SceneManager.class
                            .getResource(
                                    "/css/style.css"
                            )
                            .toExternalForm()
            );


            // =================================================
            // APPLY SELECTED THEME
            // =================================================

            String themeStylesheet =
                    ThemeManager.getThemeStylesheet();

            if (themeStylesheet != null) {

                scene.getStylesheets().add(
                        themeStylesheet
                );
            }


            // =================================================
            // SHOW SCENE
            // =================================================

            stage.setScene(scene);

            stage.show();


        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
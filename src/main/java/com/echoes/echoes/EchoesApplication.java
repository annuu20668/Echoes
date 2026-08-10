package com.echoes.echoes;

import com.echoes.echoes.navigation.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class EchoesApplication extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Echoes 🎵");
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        SceneManager.setStage(stage);
        SceneManager.switchScene("welcome-view.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
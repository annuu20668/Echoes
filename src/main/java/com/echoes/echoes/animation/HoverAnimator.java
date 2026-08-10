package com.echoes.echoes.animation;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class HoverAnimator {

    public static void attach(Node node) {

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(180), node);
        scaleUp.setToX(1.03);
        scaleUp.setToY(1.03);

        TranslateTransition liftUp = new TranslateTransition(Duration.millis(180), node);
        liftUp.setToY(-4);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(180), node);
        scaleDown.setToX(1);
        scaleDown.setToY(1);

        TranslateTransition liftDown = new TranslateTransition(Duration.millis(180), node);
        liftDown.setToY(0);

        node.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
            liftUp.playFromStart();
        });

        node.setOnMouseExited(e -> {
            scaleDown.playFromStart();
            liftDown.playFromStart();
        });

    }

}
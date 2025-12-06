package com.comp2042.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.Random;

public class Particle extends Rectangle {
    private static final Random random = new Random();
    private static final int PARTICLE_SIZE = 5;
    private static final double VELOCITY_RANGE = 50.0;
    private static final double ANIMATION_DURATION = 500.0;

    public Particle(double x, double y, Color color) {
        super(PARTICLE_SIZE, PARTICLE_SIZE, color);
        setLayoutX(x);
        setLayoutY(y);
        setArcWidth(3);
        setArcHeight(3);
        animate();
    }

    private void animate() {
        double dx = (random.nextDouble() - 0.5) * VELOCITY_RANGE;
        double dy = (random.nextDouble() - 0.5) * VELOCITY_RANGE;

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), this);
        translateTransition.setByX(dx);
        translateTransition.setByY(dy);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), this);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition);
        parallelTransition.setOnFinished(e -> {
            javafx.scene.Parent parent = getParent();
            if (parent instanceof javafx.scene.Group) {
                ((javafx.scene.Group) parent).getChildren().remove(this);
            } else if (parent instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) parent).getChildren().remove(this);
            }
        });
        parallelTransition.play();
    }
}


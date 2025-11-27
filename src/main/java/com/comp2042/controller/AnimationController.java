package com.comp2042.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AnimationController {
    private final GuiController guiController;
    private Timeline timeline;

    private static final double BASE_DROP_SPEED_MILLIS = 400;
    private static final double MIN_DROP_SPEED_MILLIS = 50;
    private static final double SPEED_INCREASE_PER_LEVEL = 25;

    public AnimationController(GuiController guiController) {
        this.guiController = guiController;
    }

    public void start() {
        updateSpeed(1);
    }

    public void updateSpeed(int level) {
        double dropSpeed = Math.max(MIN_DROP_SPEED_MILLIS, BASE_DROP_SPEED_MILLIS - (level - 1) * SPEED_INCREASE_PER_LEVEL);

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(
                Duration.millis(dropSpeed),
                e -> guiController.moveDownFromTimer()
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void pause() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    public void resume() {
        if (timeline != null) {
            timeline.play();
        }
    }
}


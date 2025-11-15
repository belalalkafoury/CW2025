package com.comp2042.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AnimationController {
    private final GuiController guiController;
    private Timeline timeline;

    private static final double DROP_SPEED_MILLIS = 400;

    public AnimationController(GuiController guiController) {
        this.guiController = guiController;
    }

    public void start() {
        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(DROP_SPEED_MILLIS),
                    e -> guiController.moveDownFromTimer()
            ));
            timeline.setCycleCount(Timeline.INDEFINITE);
        }
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


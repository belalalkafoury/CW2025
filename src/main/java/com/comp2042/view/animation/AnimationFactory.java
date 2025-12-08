package com.comp2042.view.animation;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Factory for creating common game animations.
 * Centralizes animation creation logic to reduce duplication.
 */
public final class AnimationFactory {
    
    private AnimationFactory() {
    }
    
    /**
     * Creates a floating text animation that scales, fades, and moves upward.
     * @param text The text to display
     * @param x The X position
     * @param y The Y position
     * @param color The text color
     * @param container The container group to add the text to
     * @return A ParallelTransition that can be played
     */
    public static ParallelTransition createFloatingTextAnimation(
            String text, double x, double y, Color color, Group container) {
        Text textNode = new Text(text);
        if (com.comp2042.util.FontLoader.loadPressStart2P(20)) {
            textNode.setFont(Font.font("Press Start 2P", 20));
        } else {
            textNode.setFont(Font.font("Arial", 20));
        }
        textNode.setFill(color);
        textNode.setStroke(Color.WHITE);
        textNode.setStrokeWidth(1);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(color);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.5);
        textNode.setEffect(dropShadow);
        
        container.getChildren().add(textNode);
        
        javafx.application.Platform.runLater(() -> {
            double textWidth = textNode.getBoundsInLocal().getWidth();
            textNode.setLayoutX(x - (textWidth / 2));
        });
        
        textNode.setLayoutX(x);
        textNode.setLayoutY(y);
        
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), textNode);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), textNode);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), textNode);
        translateTransition.setByY(-50);
        
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition, translateTransition);
        parallelTransition.setOnFinished(e -> container.getChildren().remove(textNode));
        
        return parallelTransition;
    }
    
    /**
     * Creates a fade transition for blinking effect.
     * @param target The node to animate
     * @param durationMillis Duration in milliseconds
     * @param cycleCount Number of cycles
     * @return A FadeTransition configured for blinking
     */
    public static FadeTransition createBlinkAnimation(
            javafx.scene.Node target, long durationMillis, int cycleCount) {
        FadeTransition blink = new FadeTransition(Duration.millis(durationMillis), target);
        blink.setFromValue(1.0);
        blink.setToValue(0.3);
        blink.setCycleCount(cycleCount);
        blink.setAutoReverse(true);
        return blink;
    }
}


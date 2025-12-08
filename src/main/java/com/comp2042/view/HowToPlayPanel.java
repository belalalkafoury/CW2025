package com.comp2042.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class HowToPlayPanel extends StackPane {
    private Button backButton;
    
    public HowToPlayPanel() {
        setAlignment(Pos.CENTER);
        setPrefWidth(700);
        setPrefHeight(600);
        
        try {
            Font.loadFont(getClass().getClassLoader().getResourceAsStream("press-start-2p-font/PressStart2P-vaV7.ttf"), 18);
        } catch (Exception e) {
            System.err.println("Could not load Press Start 2P font: " + e.getMessage());
        }
        
        ImageView imageView = new ImageView();
        try {
            Image howToPlayImage = new Image(getClass().getClassLoader().getResourceAsStream("images/How to play.jpg"));
            imageView.setImage(howToPlayImage);
            imageView.setFitWidth(700);
            imageView.setFitHeight(600);
            imageView.setPreserveRatio(false);
        } catch (Exception e) {
            System.err.println("Could not load How to Play image: " + e.getMessage());
        }
        
        VBox container = new VBox(20);
        container.setAlignment(Pos.BOTTOM_CENTER);
        container.setPrefWidth(700);
        container.setPrefHeight(600);
        container.setPadding(new Insets(0, 0, 30, 0));
        
        backButton = new Button("← BACK");
        backButton.setStyle(
            "-fx-border-width: 2px; " +
            "-fx-border-color: #00BFFF; " +
            "-fx-border-radius: 6px; " +
            "-fx-background-color: rgba(0, 0, 0, 0.8); " +
            "-fx-background-radius: 6px; " +
            "-fx-text-fill: white; " +
            "-fx-font-family: \"Press Start 2P\"; " +
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-pref-width: 150px; " +
            "-fx-pref-height: 40px; " +
            "-fx-cursor: hand;"
        );
        
        backButton.setOnMouseEntered(e -> {
            backButton.setStyle(
                "-fx-border-width: 2px; " +
                "-fx-border-color: #00BFFF; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-color: rgba(0, 191, 255, 0.2); " +
                "-fx-background-radius: 6px; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: \"Press Start 2P\"; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-pref-width: 150px; " +
                "-fx-pref-height: 40px; " +
                "-fx-cursor: hand;"
            );
        });
        
        backButton.setOnMouseExited(e -> {
            backButton.setStyle(
                "-fx-border-width: 2px; " +
                "-fx-border-color: #00BFFF; " +
                "-fx-border-radius: 6px; " +
                "-fx-background-color: rgba(0, 0, 0, 0.8); " +
                "-fx-background-radius: 6px; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: \"Press Start 2P\"; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-pref-width: 150px; " +
                "-fx-pref-height: 40px; " +
                "-fx-cursor: hand;"
            );
        });
        
        container.getChildren().add(backButton);
        
        getChildren().add(imageView);
        getChildren().add(container);
    }
    
    public void playAnimation() {
        setOpacity(0);
        setScaleX(0.8);
        setScaleY(0.8);
        
        FadeTransition fade = new FadeTransition(Duration.millis(300), this);
        fade.setFromValue(0);
        fade.setToValue(1);
        
        ScaleTransition scale = new ScaleTransition(Duration.millis(300), this);
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1.0);
        scale.setToY(1.0);
        
        fade.play();
        scale.play();
    }
    
    public Button getBackButton() {
        return backButton;
    }
}

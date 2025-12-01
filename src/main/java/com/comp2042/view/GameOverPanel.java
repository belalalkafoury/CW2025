package com.comp2042.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameOverPanel extends StackPane {
    private Label gameOverLabel;
    private Label finalScoreLabel;
    private Label highScoreLabel;
    private Button playAgainButton;
    private Button mainMenuButton;
    private VBox menuPanel;
    private int finalScore = 0;

    public GameOverPanel() {
        setAlignment(Pos.CENTER);
        setBackground(Background.EMPTY);
        
        try {
            Font.loadFont(getClass().getClassLoader().getResourceAsStream("press-start-2p-font/PressStart2P-vaV7.ttf"), 18);
        } catch (Exception e) {
            System.err.println("Could not load Press Start 2P font: " + e.getMessage());
        }
        
        createContent();
    }

    private void createContent() {
        menuPanel = new VBox(20);
        menuPanel.setAlignment(Pos.CENTER);
        menuPanel.setPadding(new Insets(30, 40, 30, 40));
        menuPanel.setPrefWidth(300);
        menuPanel.setStyle(
            "-fx-border-width: 3px;" +
            "-fx-border-color: #BF5FFF;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.85);" +
            "-fx-background-radius: 10px;"
        );
        
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setStyle(
            "-fx-font-family: \"Let's go Digital\";" +
            "-fx-font-size: 36px;" +
            "-fx-text-fill: #BF5FFF;" +
            "-fx-font-weight: bold;"
        );
        
        finalScoreLabel = new Label("FINAL SCORE: " + finalScore);
        finalScoreLabel.setStyle(
            "-fx-font-family: \"Let's go Digital\";" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        
        highScoreLabel = new Label("👑 HIGH SCORE: 0");
        highScoreLabel.setStyle(
            "-fx-font-family: \"Let's go Digital\";" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: #FFD700;" +
            "-fx-font-weight: bold;"
        );
        
        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(15, 0, 0, 0));
        
        playAgainButton = new Button("PLAY AGAIN");
        playAgainButton.setStyle(
            "-fx-border-width: 2px;" +
            "-fx-border-color: #00FFFF;" +
            "-fx-border-radius: 6px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-background-radius: 6px;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-pref-width: 220px;" +
            "-fx-pref-height: 40px;" +
            "-fx-cursor: hand;"
        );
        playAgainButton.setOnMouseEntered(e -> {
            playAgainButton.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #00FFFF;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(0, 255, 255, 0.2);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 220px;" +
                "-fx-pref-height: 40px;" +
                "-fx-cursor: hand;"
            );
        });
        playAgainButton.setOnMouseExited(e -> {
            playAgainButton.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #00FFFF;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 220px;" +
                "-fx-pref-height: 40px;" +
                "-fx-cursor: hand;"
            );
        });
        
        mainMenuButton = new Button("MAIN MENU");
        mainMenuButton.setStyle(
            "-fx-border-width: 2px;" +
            "-fx-border-color: #FF0000;" +
            "-fx-border-radius: 6px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-background-radius: 6px;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-pref-width: 220px;" +
            "-fx-pref-height: 40px;" +
            "-fx-cursor: hand;"
        );
        mainMenuButton.setOnMouseEntered(e -> {
            mainMenuButton.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #FF0000;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(255, 0, 0, 0.2);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 220px;" +
                "-fx-pref-height: 40px;" +
                "-fx-cursor: hand;"
            );
        });
        mainMenuButton.setOnMouseExited(e -> {
            mainMenuButton.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #FF0000;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 220px;" +
                "-fx-pref-height: 40px;" +
                "-fx-cursor: hand;"
            );
        });
        
        buttonsBox.getChildren().addAll(playAgainButton, mainMenuButton);
        menuPanel.getChildren().addAll(gameOverLabel, finalScoreLabel, highScoreLabel, buttonsBox);
        
        getChildren().add(menuPanel);
    }

    public void playAnimation() {
        setVisible(true);
        menuPanel.setOpacity(0);
        menuPanel.setScaleX(0.5);
        menuPanel.setScaleY(0.5);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), menuPanel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), menuPanel);
        scaleUp.setFromX(0.5);
        scaleUp.setFromY(0.5);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);
        
        ParallelTransition animation = new ParallelTransition(fadeIn, scaleUp);
        animation.play();
    }

    public void setFinalScore(int score) {
        this.finalScore = score;
        if (finalScoreLabel != null) {
            finalScoreLabel.setText("FINAL SCORE: " + score);
        }
    }

    public void setHighScore(int highScore) {
        if (highScoreLabel != null) {
            highScoreLabel.setText("👑 HIGH SCORE: " + highScore);
        }
    }

    public Button getPlayAgainButton() {
        return playAgainButton;
    }

    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}

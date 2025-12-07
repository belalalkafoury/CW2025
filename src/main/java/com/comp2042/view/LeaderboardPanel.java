package com.comp2042.view;

import com.comp2042.logic.score.HighScoreManager;
import com.comp2042.model.GameMode;
import com.comp2042.model.HighScoreEntry;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.application.Platform;

import java.util.List;

/**
 * Panel displaying high scores for different game modes.
 * Allows switching between game modes (Classic, Time Attack, Puzzle, Reverted)
 * and displays the top 10 scores for the selected mode.
 */
public class LeaderboardPanel extends StackPane {
    private Label titleLabel;
    private VBox scoresContainer;
    private ScrollPane scrollPane;
    private Button backButton;
    private VBox menuPanel;
    private HighScoreManager highScoreManager;
    private GameMode currentMode = GameMode.CLASSIC;
    
    private Button classicButton;
    private Button timeAttackButton;
    private Button puzzleButton;
    private Button revertedButton;
    private HBox modeButtonsContainer;

    /**
     * Constructs a LeaderboardPanel with mode selection buttons and score display.
     * Loads custom font and initializes the UI components.
     */
    public LeaderboardPanel() {
        setAlignment(Pos.CENTER);
        setBackground(Background.EMPTY);
        setMaxWidth(500);
        setMaxHeight(500);
        setPrefWidth(500);
        setPrefHeight(500);
        
        try {
            Font.loadFont(getClass().getClassLoader().getResourceAsStream("press-start-2p-font/PressStart2P-vaV7.ttf"), 18);
        } catch (Exception e) {
            System.err.println("Could not load Press Start 2P font: " + e.getMessage());
        }
        
        createContent();
    }

    /**
     * Sets the HighScoreManager and refreshes the display.
     * @param highScoreManager The HighScoreManager to use for retrieving scores
     */
    public void setHighScoreManager(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
        refresh();
    }

    private void createContent() {
        menuPanel = new VBox(15);
        menuPanel.setAlignment(Pos.CENTER);
        menuPanel.setPadding(new Insets(20, 30, 20, 30));
        menuPanel.setMaxWidth(460);
        menuPanel.setMaxHeight(460);
        menuPanel.setPrefWidth(460);
        menuPanel.setPrefHeight(460);
        menuPanel.setStyle(
            "-fx-border-width: 3px;" +
            "-fx-border-color: #00FFFF;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.85);" +
            "-fx-background-radius: 10px;"
        );
        
        titleLabel = new Label("HALL OF FAME");
        titleLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: #00FFFF;" +
            "-fx-font-weight: bold;"
        );
        
        modeButtonsContainer = new HBox(5);
        modeButtonsContainer.setAlignment(Pos.CENTER);
        modeButtonsContainer.setPadding(new Insets(5, 0, 10, 0));
        
        classicButton = createModeButton("CLASSIC", GameMode.CLASSIC);
        timeAttackButton = createModeButton("TIME", GameMode.TIME_ATTACK);
        puzzleButton = createModeButton("PUZZLE", GameMode.PUZZLE);
        revertedButton = createModeButton("REVERTED", GameMode.REVERTED);
        
        modeButtonsContainer.getChildren().addAll(classicButton, timeAttackButton, puzzleButton, revertedButton);
        
        scoresContainer = new VBox(8);
        scoresContainer.setAlignment(Pos.TOP_CENTER);
        scoresContainer.setPadding(new Insets(10, 15, 10, 15));
        scoresContainer.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.5);" +
            "-fx-background-radius: 5px;"
        );
        
        scrollPane = new ScrollPane(scoresContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(250);
        scrollPane.setMaxHeight(250);
        scrollPane.getStyleClass().add("leaderboard-scroll-pane");
        scrollPane.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #00FFFF;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 5px;"
        );
        
        scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        
        Platform.runLater(() -> {
            javafx.scene.Node viewport = scrollPane.lookup(".viewport");
            if (viewport != null) {
                viewport.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
            }
        });
        
        backButton = createBackButton();
        
        menuPanel.getChildren().addAll(titleLabel, modeButtonsContainer, scrollPane, backButton);
        getChildren().add(menuPanel);
        
        refresh();
    }

    private Button createModeButton(String text, GameMode mode) {
        Button button = new Button(text);
        boolean isSelected = mode == currentMode;
        updateModeButtonStyle(button, isSelected);
        
        button.setPrefWidth(120);
        button.setPrefHeight(30);
        button.setOnAction(e -> {
            currentMode = mode;
            updateModeButtons();
            refresh();
        });
        
        return button;
    }

    private void updateModeButtons() {
        updateModeButtonStyle(classicButton, currentMode == GameMode.CLASSIC);
        updateModeButtonStyle(timeAttackButton, currentMode == GameMode.TIME_ATTACK);
        updateModeButtonStyle(puzzleButton, currentMode == GameMode.PUZZLE);
        if (revertedButton != null) {
            updateModeButtonStyle(revertedButton, currentMode == GameMode.REVERTED);
        }
    }

    private void updateModeButtonStyle(Button button, boolean isSelected) {
        String baseStyle = "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 8px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;";
        
        if (isSelected) {
            button.setStyle(baseStyle +
                "-fx-border-width: 2px;" +
                "-fx-border-color: #00FFFF;" +
                "-fx-background-color: rgba(0, 255, 255, 0.3);" +
                "-fx-text-fill: #00FFFF;"
            );
        } else {
            button.setStyle(baseStyle +
                "-fx-border-width: 2px;" +
                "-fx-border-color: #555555;" +
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                "-fx-text-fill: #AAAAAA;"
            );
        }
        
        button.setOnMouseEntered(e -> {
            if (!isSelected) {
                button.setStyle(baseStyle +
                    "-fx-border-width: 2px;" +
                    "-fx-border-color: #00FFFF;" +
                    "-fx-background-color: rgba(0, 255, 255, 0.15);" +
                    "-fx-text-fill: #00FFFF;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            updateModeButtonStyle(button, isSelected);
        });
    }

    private Button createBackButton() {
        Button button = new Button("BACK");
        button.setStyle(
            "-fx-border-width: 2px;" +
            "-fx-border-color: #00FFFF;" +
            "-fx-border-radius: 6px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-background-radius: 6px;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-pref-width: 180px;" +
            "-fx-pref-height: 35px;" +
            "-fx-cursor: hand;"
        );
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #00FFFF;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(0, 255, 255, 0.2);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 180px;" +
                "-fx-pref-height: 35px;" +
                "-fx-cursor: hand;"
            );
        });
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #00FFFF;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 180px;" +
                "-fx-pref-height: 35px;" +
                "-fx-cursor: hand;"
            );
        });
        return button;
    }

    /**
     * Refreshes the leaderboard display with scores for the current game mode.
     * Clears existing scores and populates with top scores from the HighScoreManager.
     */
    public void refresh() {
        scoresContainer.getChildren().clear();
        
        if (highScoreManager != null) {
            List<HighScoreEntry> scores = highScoreManager.getTopScores(currentMode);
            
            if (scores.isEmpty()) {
                Label noScoresLabel = new Label("NO SCORES YET");
                noScoresLabel.setStyle(
                    "-fx-font-family: \"Press Start 2P\";" +
                    "-fx-font-size: 10px;" +
                    "-fx-text-fill: white;"
                );
                scoresContainer.getChildren().add(noScoresLabel);
            } else {
                for (int i = 0; i < scores.size(); i++) {
                    HighScoreEntry entry = scores.get(i);
                    HBox row = createScoreRow(i + 1, entry.getName(), entry.getScore());
                    scoresContainer.getChildren().add(row);
                }
            }
        } else {
            Label noScoresLabel = new Label("NO SCORES YET");
            noScoresLabel.setStyle(
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: 10px;" +
                "-fx-text-fill: white;"
            );
            scoresContainer.getChildren().add(noScoresLabel);
        }
    }

    private HBox createScoreRow(int rank, String name, int score) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefWidth(400);
        row.setStyle("-fx-background-color: transparent;");
        
        Label rankLabel = new Label(rank + ".");
        rankLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #FFD700;" +
            "-fx-min-width: 30px;"
        );
        
        Label nameLabel = new Label(name);
        nameLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: white;" +
            "-fx-min-width: 150px;"
        );
        HBox.setHgrow(nameLabel, javafx.scene.layout.Priority.ALWAYS);
        
        Label scoreLabel = new Label(String.valueOf(score));
        scoreLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #00FFFF;"
        );
        scoreLabel.setAlignment(Pos.CENTER_RIGHT);
        
        row.getChildren().addAll(rankLabel, nameLabel, scoreLabel);
        return row;
    }

    /**
     * Plays an entrance animation for the panel (fade in and scale up).
     */
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

    /**
     * Gets the back button for navigation.
     * @return The back button component
     */
    public Button getBackButton() {
        return backButton;
    }
}

package com.comp2042.view;

import com.comp2042.controller.SoundController;
import com.comp2042.logic.score.HighScoreManager;
import com.comp2042.model.GameSettings;
import com.comp2042.util.FontLoader;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.util.Duration;

public class SettingsPanel extends StackPane {
    private Label titleLabel;
    private CheckBox showAnimationsCheckbox;
    private CheckBox showGhostPieceCheckbox;
    private Label soundVolumeLabel;
    private Label musicVolumeLabel;
    private Button soundVolumeMinus;
    private Button soundVolumePlus;
    private Button musicVolumeMinus;
    private Button musicVolumePlus;
    private Button resetHighScoresButton;
    private Button resetOptionsButton;
    private Button doneButton;
    private VBox menuPanel;
    private SoundController soundController;
    private HighScoreManager highScoreManager;

    public SettingsPanel() {
        setAlignment(Pos.CENTER);
        setBackground(Background.EMPTY);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(400, USE_COMPUTED_SIZE);
        
        com.comp2042.util.FontLoader.loadPressStart2P(18);
        
        createContent();
    }

    public void setSoundController(SoundController soundController) {
        this.soundController = soundController;
        updateVolumeDisplays();
    }

    public void setHighScoreManager(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
    }

    private void createContent() {
        menuPanel = new VBox(20);
        menuPanel.setAlignment(Pos.CENTER);
        menuPanel.setPadding(new Insets(30, 40, 30, 40));
        menuPanel.setPrefWidth(400);
        menuPanel.setStyle(
            "-fx-border-width: 3px;" +
            "-fx-border-color: #FFFF00;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.85);" +
            "-fx-background-radius: 10px;"
        );
        
        titleLabel = new Label("SETTINGS");
        titleLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 28px;" +
            "-fx-text-fill: #FFFF00;" +
            "-fx-font-weight: bold;"
        );

        VBox settingsContainer = new VBox(15);
        settingsContainer.setAlignment(Pos.CENTER);

        HBox animationsRow = createCheckboxRow("SHOW ANIMATIONS", GameSettings.isShowAnimations(), 
            value -> {
                GameSettings.setShowAnimations(value);
                showAnimationsCheckbox.setSelected(value);
            });
        showAnimationsCheckbox = (CheckBox) animationsRow.getChildren().get(1);
        
        HBox ghostPieceRow = createCheckboxRow("SHOW GHOST PIECE", GameSettings.isShowGhostPiece(),
            value -> {
                GameSettings.setShowGhostPiece(value);
                showGhostPieceCheckbox.setSelected(value);
            });
        showGhostPieceCheckbox = (CheckBox) ghostPieceRow.getChildren().get(1);

        HBox soundVolumeRow = createVolumeRow("SOUND FX VOLUME", GameSettings.getSoundVolume(),
            this::updateSoundVolume);
        HBox musicVolumeRow = createVolumeRow("MUSIC VOLUME", GameSettings.getMusicVolume(),
            this::updateMusicVolume);

        HBox resetRow = new HBox(15);
        resetRow.setAlignment(Pos.CENTER);
        resetHighScoresButton = createResetButton("RESET HIGH SCORES", this::resetHighScores);
        resetOptionsButton = createResetButton("RESET OPTIONS", this::resetOptions);
        resetRow.getChildren().addAll(resetHighScoresButton, resetOptionsButton);

        doneButton = new Button("DONE");
        doneButton.getStyleClass().add("yellow-neon-button");
        doneButton.setPrefWidth(220);
        doneButton.setPrefHeight(40);
        doneButton.setOnAction(e -> hide());

        settingsContainer.getChildren().addAll(
            animationsRow,
            ghostPieceRow,
            soundVolumeRow,
            musicVolumeRow,
            resetRow,
            doneButton
        );

        menuPanel.getChildren().addAll(titleLabel, settingsContainer);
        getChildren().add(menuPanel);

        updateVolumeDisplays();
    }

    private HBox createCheckboxRow(String labelText, boolean initialValue, java.util.function.Consumer<Boolean> onChanged) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER);
        
        Label label = new Label(labelText);
        label.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: white;"
        );

        CheckBox checkbox = new CheckBox();
        checkbox.setSelected(initialValue);
        checkbox.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-text-fill: white;"
        );
        checkbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            onChanged.accept(newVal);
        });

        row.getChildren().addAll(label, checkbox);
        return row;
    }

    private HBox createVolumeRow(String labelText, double initialVolume, java.util.function.Consumer<Double> onVolumeChanged) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 10px;" +
            "-fx-text-fill: white;"
        );
        label.setMinWidth(180);
        label.setPrefWidth(180);
        label.setAlignment(Pos.CENTER_LEFT);

        Button minusButton = new Button("-");
        minusButton.getStyleClass().add("volume-button");
        minusButton.setPrefWidth(40);
        minusButton.setPrefHeight(40);

        Label volumeLabel = new Label(formatVolume(initialVolume));
        volumeLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #00FFFF;" +
            "-fx-min-width: 60px;" +
            "-fx-alignment: center;"
        );

        Button plusButton = new Button("+");
        plusButton.getStyleClass().add("volume-button");
        plusButton.setPrefWidth(40);
        plusButton.setPrefHeight(40);

        if (labelText.contains("SOUND FX")) {
            soundVolumeLabel = volumeLabel;
            soundVolumeMinus = minusButton;
            soundVolumePlus = plusButton;
        } else {
            musicVolumeLabel = volumeLabel;
            musicVolumeMinus = minusButton;
            musicVolumePlus = plusButton;
        }

        minusButton.setOnAction(e -> {
            double currentVolume = labelText.contains("SOUND FX") ? GameSettings.getSoundVolume() : GameSettings.getMusicVolume();
            double newVolume = Math.max(0.0, currentVolume - 0.1);
            onVolumeChanged.accept(newVolume);
            updateVolumeDisplays();
        });

        plusButton.setOnAction(e -> {
            double currentVolume = labelText.contains("SOUND FX") ? GameSettings.getSoundVolume() : GameSettings.getMusicVolume();
            double newVolume = Math.min(1.0, currentVolume + 0.1);
            onVolumeChanged.accept(newVolume);
            updateVolumeDisplays();
        });

        row.getChildren().addAll(label, minusButton, volumeLabel, plusButton);
        return row;
    }

    private void updateSoundVolume(double volume) {
        GameSettings.setSoundVolume(volume);
        if (soundController != null) {
            soundController.setSoundVolume(volume);
        }
    }

    private void updateMusicVolume(double volume) {
        GameSettings.setMusicVolume(volume);
        if (soundController != null) {
            soundController.setMusicVolume(volume);
        }
    }

    private void updateVolumeDisplays() {
        if (soundVolumeLabel != null) {
            soundVolumeLabel.setText(formatVolume(GameSettings.getSoundVolume()));
        }
        if (musicVolumeLabel != null) {
            musicVolumeLabel.setText(formatVolume(GameSettings.getMusicVolume()));
        }
    }

    private String formatVolume(double volume) {
        return (int)(volume * 100) + "%";
    }

    private Button createResetButton(String text, Runnable action) {
        Button button = new Button(text);
        int fontSize = text.contains("HIGH") ? 7 : 10;
        String style = "-fx-border-width: 2px;" +
            "-fx-border-color: #FF0000;" +
            "-fx-border-radius: 6px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-background-radius: 6px;" +
            "-fx-text-fill: white;" +
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: " + fontSize + "px;" +
            "-fx-font-weight: bold;" +
            "-fx-pref-width: 150px;" +
            "-fx-pref-height: 35px;" +
            "-fx-cursor: hand;";
        button.setStyle(style);
        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-border-width: 2px;" +
                "-fx-border-color: #FF0000;" +
                "-fx-border-radius: 6px;" +
                "-fx-background-color: rgba(255, 0, 0, 0.2);" +
                "-fx-background-radius: 6px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: \"Press Start 2P\";" +
                "-fx-font-size: " + fontSize + "px;" +
                "-fx-font-weight: bold;" +
                "-fx-pref-width: 150px;" +
                "-fx-pref-height: 35px;" +
                "-fx-cursor: hand;"
            );
        });
        button.setOnMouseExited(e -> {
            button.setStyle(style);
        });
        button.setOnAction(e -> action.run());
        return button;
    }

    private void resetHighScores() {
        if (highScoreManager != null) {
            highScoreManager.resetAllHighScores();
        }
    }

    private void resetOptions() {
        GameSettings.reset();
        showAnimationsCheckbox.setSelected(GameSettings.isShowAnimations());
        showGhostPieceCheckbox.setSelected(GameSettings.isShowGhostPiece());
        updateVolumeDisplays();
        if (soundController != null) {
            soundController.setSoundVolume(GameSettings.getSoundVolume());
            soundController.setMusicVolume(GameSettings.getMusicVolume());
        }
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

    public void hide() {
        setVisible(false);
    }

    public Button getDoneButton() {
        return doneButton;
    }
}


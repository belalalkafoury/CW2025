package com.comp2042.view;

import com.comp2042.util.FontLoader;
import com.comp2042.view.factory.ButtonFactory;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.function.Consumer;

public class NameEntryPanel extends StackPane {
    private Label titleLabel;
    private TextField nameField;
    private Button playButton;
    private Button guestButton;
    private Button cancelButton;
    private VBox menuPanel;
    private Consumer<String> onNameEntered;  
    private Runnable onCancel;

    public NameEntryPanel() {
        setAlignment(Pos.CENTER);
        setBackground(Background.EMPTY);
        
        FontLoader.loadPressStart2P(18);
        
        createContent();
    }

    public void setOnNameEntered(Consumer<String> callback) {
        this.onNameEntered = callback;
    }
    
    public void setOnCancel(Runnable callback) {
        this.onCancel = callback;
    }
    
    public Button getCancelButton() {
        return cancelButton;
    }

    private void createContent() {
        menuPanel = new VBox(15);
        menuPanel.setAlignment(Pos.CENTER);
        menuPanel.setPadding(new Insets(20, 20, 20, 20));
        menuPanel.setPrefWidth(400);
        menuPanel.setMaxWidth(400);
        menuPanel.setPrefHeight(300);
        menuPanel.setMaxHeight(300);
        menuPanel.setStyle(
            "-fx-border-width: 3px;" +
            "-fx-border-color: #00FFFF;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-color: rgba(0, 0, 0, 0.85);" +
            "-fx-background-radius: 10px;"
        );
        
        titleLabel = new Label("ENTER NAME");
        titleLabel.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #00FFFF;" +
            "-fx-font-weight: bold;"
        );
        VBox.setMargin(titleLabel, new Insets(0, 0, 10, 0));
        
        nameField = new TextField();
        nameField.setPromptText("PLAYER");
        nameField.setMaxWidth(360);
        nameField.setPrefHeight(35);
        nameField.setStyle(
            "-fx-font-family: \"Press Start 2P\";" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-border-color: #00FFFF;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 4px;"
        );
        
        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handlePlay();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                handleCancel();
            }
        });
        
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > 8) {
                nameField.setText(oldVal);
            } else if (newVal != null) {
                nameField.setText(newVal.toUpperCase());
            }
        });
        
        playButton = ButtonFactory.createCyanButton("PLAY", this::handlePlay);
        
        guestButton = ButtonFactory.createCyanButton("GUEST", () -> {
            if (onNameEntered != null) {
                onNameEntered.accept("GUEST");
            }
            nameField.clear();
        });
        
        cancelButton = ButtonFactory.createCancelButton("CANCEL", this::handleCancel);
        
        VBox buttonsBox = new VBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(playButton, guestButton, cancelButton);
        
        menuPanel.getChildren().addAll(titleLabel, nameField, buttonsBox);
        getChildren().add(menuPanel);
    }

    private void handlePlay() {
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            name = "PLAYER";
        } else {
            name = name.trim().toUpperCase();
            if (name.length() > 8) {
                name = name.substring(0, 8);
            }
        }
        
        if (onNameEntered != null) {
            onNameEntered.accept(name);
        }
        
        nameField.clear();
    }

    private void handleCancel() {
        nameField.clear();
        setVisible(false);
        if (onCancel != null) {
            onCancel.run();
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
        
        javafx.application.Platform.runLater(() -> {
            nameField.requestFocus();
            nameField.selectAll();
        });
    }

    public void reset() {
        nameField.clear();
        nameField.setText("PLAYER");
    }
}


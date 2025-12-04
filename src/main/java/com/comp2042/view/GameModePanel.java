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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameModePanel extends StackPane {
    private VBox mainContainer;
    private Button backButton;
    private Runnable classicAction;
    private Runnable timeAttackAction;
    private Runnable puzzleAction;
    private Runnable revertedAction;
    private Runnable backAction;
    
    public GameModePanel() {
        setAlignment(Pos.CENTER);
        setPrefSize(700, 600);
        setMaxSize(700, 600);
        setBackground(Background.EMPTY);
        
        ImageView backgroundImageView = new ImageView();
        try {
            Image backgroundImage = new Image(getClass().getClassLoader().getResourceAsStream("background_image.png"));
            backgroundImageView.setImage(backgroundImage);
            backgroundImageView.setFitWidth(700);
            backgroundImageView.setFitHeight(600);
            backgroundImageView.setPreserveRatio(false);
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }
        StackPane.setAlignment(backgroundImageView, Pos.CENTER);
        getChildren().add(backgroundImageView);
        
        try {
            Font.loadFont(getClass().getClassLoader().getResourceAsStream("press-start-2p-font/PressStart2P-vaV7.ttf"), 18);
        } catch (Exception e) {
            System.err.println("Could not load Press Start 2P font: " + e.getMessage());
        }
        
        createContent();
    }
    
    private void createContent() {
        mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(30, 0, 30, 0));
        Label titleLabel = new Label("GAME MODE SELECTION");
        titleLabel.getStyleClass().add("game-mode-title");
        
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setFillHeight(false);
        
        Button classicButton = createImageButton("classicmode.png", () -> {
            if (classicAction != null) {
                classicAction.run();
            }
        });
        
        Button timeAttackButton = createImageButton("timeattackmode.png", () -> {
            if (timeAttackAction != null) {
                timeAttackAction.run();
            }
        });
        
        Button puzzleButton = createImageButton("puzzlemode.png", () -> {
            if (puzzleAction != null) {
                puzzleAction.run();
            }
        });
        
        Button revertedButton = createImageButton("revertedmode.png", () -> {
            if (revertedAction != null) {
                revertedAction.run();
            }
        });
        
        buttonsContainer.getChildren().addAll(classicButton, timeAttackButton, puzzleButton, revertedButton);
        
        backButton = new Button("BACK");
        backButton.getStyleClass().add("mode-back-button");
        backButton.setOnAction(e -> {
            if (backAction != null) {
                backAction.run();
            } else {
                setVisible(false);
            }
        });
        
        mainContainer.getChildren().addAll(titleLabel, buttonsContainer, backButton);
        StackPane.setAlignment(mainContainer, Pos.CENTER);
        getChildren().add(mainContainer);
    }
    
    private Button createImageButton(String imagePath, Runnable action) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));
            imageView.setImage(image);
            imageView.setFitWidth(155);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("Could not load image: " + imagePath + " - " + e.getMessage());
        }
        
        Button button = new Button();
        button.setGraphic(imageView);
        button.setBackground(Background.EMPTY);
        button.setPadding(Insets.EMPTY);
        button.setCursor(javafx.scene.Cursor.HAND);
        button.setContentDisplay(javafx.scene.control.ContentDisplay.GRAPHIC_ONLY);
        button.getStyleClass().add("mode-image-button");
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        button.setOnAction(e -> {
            if (action != null) {
                action.run();
            }
        });
        
        button.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), button);
            scaleUp.setFromX(button.getScaleX());
            scaleUp.setFromY(button.getScaleY());
            scaleUp.setToX(1.1);
            scaleUp.setToY(1.1);
            scaleUp.play();
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), button);
            scaleDown.setFromX(button.getScaleX());
            scaleDown.setFromY(button.getScaleY());
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.play();
        });
        
        return button;
    }
    
    public void setClassicAction(Runnable action) {
        this.classicAction = action;
    }
    
    public void setTimeAttackAction(Runnable action) {
        this.timeAttackAction = action;
    }
    
    public void setPuzzleAction(Runnable action) {
        this.puzzleAction = action;
    }
    
    public void setRevertedAction(Runnable action) {
        this.revertedAction = action;
    }

    public void setBackAction(Runnable action) {
        this.backAction = action;
    }
    
    public void playAnimation() {
        setVisible(true);
        mainContainer.setOpacity(0);
        mainContainer.setScaleX(0.5);
        mainContainer.setScaleY(0.5);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), mainContainer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), mainContainer);
        scaleUp.setFromX(0.5);
        scaleUp.setFromY(0.5);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);
        
        ParallelTransition animation = new ParallelTransition(fadeIn, scaleUp);
        animation.play();
    }
}

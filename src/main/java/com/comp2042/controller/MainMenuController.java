package com.comp2042.controller;

import com.comp2042.model.Board;
import com.comp2042.model.GameBoard;
import com.comp2042.view.HowToPlayPanel;
import com.comp2042.view.TetrisLogo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button howToPlayButton;

    @FXML
    private Pane animationContainer;

    @FXML
    private VBox logoContainer;

    @FXML
    private TetrisLogo tetrisLogo;

    @FXML
    private Group howToPlayOverlay;

    @FXML
    private HowToPlayPanel howToPlayPanel;

    private Stage primaryStage;
    private MainMenuAnimationController animationController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (animationContainer != null) {
            animationController = new MainMenuAnimationController(animationContainer);
            animationController.start();
        }
        if (howToPlayOverlay != null) {
            howToPlayOverlay.setVisible(false);
        }
        if (howToPlayPanel != null && howToPlayPanel.getBackButton() != null) {
            howToPlayPanel.getBackButton().setOnAction(e -> hideHowToPlayFromMenu());
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    @FXML
    private void startGame(ActionEvent event) {
        if (animationController != null) {
            animationController.stop();
        }
        
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            ResourceBundle resources = null;
            FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
            Parent root = fxmlLoader.load();
            GuiController guiController = fxmlLoader.getController();

            Scene gameScene = new Scene(root, 700, 600);
            primaryStage.setScene(gameScene);

            guiController.setPrimaryStage(primaryStage);

            Board board = new GameBoard(25, 10);
            new GameController(guiController, board);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings(ActionEvent event) {
    }

    @FXML
    private void quitGame(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void showHowToPlayFromMenu(ActionEvent event) {
        if (howToPlayOverlay != null) {
            howToPlayOverlay.setVisible(true);
            if (howToPlayPanel != null) {
                howToPlayPanel.playAnimation();
            }
        }
    }

    private void hideHowToPlayFromMenu() {
        if (howToPlayOverlay != null) {
            howToPlayOverlay.setVisible(false);
        }
    }
}


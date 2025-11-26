package com.comp2042.controller;

import com.comp2042.model.Board;
import com.comp2042.model.GameBoard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void startGame(ActionEvent event) {
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
}


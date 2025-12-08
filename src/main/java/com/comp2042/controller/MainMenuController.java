package com.comp2042.controller;

import com.comp2042.model.Board;
import com.comp2042.model.GameBoard;
import com.comp2042.model.GameMode;
import com.comp2042.view.HowToPlayPanel;
import com.comp2042.view.SettingsPanel;
import com.comp2042.view.TetrisLogo;
import com.comp2042.view.GameModePanel;
import com.comp2042.view.NameEntryPanel;
import com.comp2042.logic.score.HighScoreManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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

    @FXML
    private Group settingsOverlay;

    @FXML
    private SettingsPanel settingsPanel;

    @FXML
    private Group gameModeOverlay;

    @FXML
    private GameModePanel gameModePanel;

    @FXML
    private Group nameEntryOverlay;

    @FXML
    private NameEntryPanel nameEntryPanel;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private Pane rootPane;

    private Stage primaryStage;
    private MainMenuAnimationController animationController;
    private SoundController soundController;
    private HighScoreManager highScoreManager;
    private GameMode selectedMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundController = new SoundController();
        soundController.playTitleMusic();
        highScoreManager = new HighScoreManager();
        
        if (backgroundImageView != null && rootPane != null) {
            backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        }
        
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
        if (settingsOverlay != null) {
            settingsOverlay.setVisible(false);
        }
        if (settingsPanel != null) {
            settingsPanel.setSoundController(soundController);
            settingsPanel.setHighScoreManager(highScoreManager);
            if (settingsPanel.getDoneButton() != null) {
                settingsPanel.getDoneButton().setOnAction(e -> hideSettingsFromMenu());
            }
        }
        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(false);
        }
        if (gameModePanel != null) {
            gameModePanel.setClassicAction(() -> showNameEntry(GameMode.CLASSIC));
            gameModePanel.setTimeAttackAction(() -> showNameEntry(GameMode.TIME_ATTACK));
            gameModePanel.setPuzzleAction(() -> showNameEntry(GameMode.PUZZLE));
            gameModePanel.setRevertedAction(() -> showNameEntry(GameMode.REVERTED));
        }
        if (nameEntryOverlay != null) {
            nameEntryOverlay.setVisible(false);
        }
        if (nameEntryPanel != null) {
            nameEntryPanel.setOnNameEntered(name -> {
                launchGame(selectedMode, name);
            });
            nameEntryPanel.setOnCancel(this::hideNameEntry);
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    
    @FXML
    private void showGameModeSelection(ActionEvent event) {
        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(true);
            gameModeOverlay.toFront();
            if (gameModePanel != null) {
                gameModePanel.playAnimation();
            }
        }
    }

    private void showNameEntry(GameMode gameMode) {
        selectedMode = gameMode;
        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(false);
        }
        if (nameEntryOverlay != null) {
            nameEntryOverlay.setVisible(true);
            nameEntryOverlay.toFront();
            if (nameEntryPanel != null) {
                nameEntryPanel.reset();
                nameEntryPanel.playAnimation();
            }
        }
    }

    private void launchGame(GameMode gameMode, String playerName) {
        if (animationController != null) {
            animationController.stop();
        }
        
        if (nameEntryOverlay != null) {
            nameEntryOverlay.setVisible(false);
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
            new GameController(guiController, board, soundController, gameMode, playerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGameWithMode(GameMode gameMode) {
        launchGame(gameMode, "GUEST");
    }

    private void startGameInternal() {
        startGameWithMode(GameMode.CLASSIC);
    }

    @FXML
    private void startGame(ActionEvent event) {
        startGameInternal();
    }

    @FXML
    private void openSettings(ActionEvent event) {
        if (settingsOverlay != null) {
            settingsOverlay.setVisible(true);
            if (settingsPanel != null) {
                settingsPanel.playAnimation();
            }
        }
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

    private void hideSettingsFromMenu() {
        if (settingsOverlay != null) {
            settingsOverlay.setVisible(false);
        }
    }
    
    private void hideNameEntry() {
        if (nameEntryOverlay != null) {
            nameEntryOverlay.setVisible(false);
        }
        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(true);
            gameModeOverlay.toFront();
        }
    }
}


package com.comp2042.controller;

import com.comp2042.logic.board.DownData;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.NotificationPanel;
import com.comp2042.view.ViewData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private BorderPane gameBoard;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;
    
    @FXML
    private Group gameOverOverlay;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label linesLabel;

    @FXML
    private GridPane holdPanel;

    @FXML
    private GridPane nextPiece1;

    @FXML
    private Button pauseButton;

    @FXML
    private Group pauseMenuOverlay;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] nextPieceRectangles;

    private boolean gridCreated = false;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private InputHandler inputHandler;

    private AnimationController animationController;

    private Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            
            if (code == KeyCode.ESCAPE) {
                pauseGame(null);
                keyEvent.consume();
                return;
            }
            
            if (!isPause.get() && !isGameOver.get() && inputHandler != null) {
                inputHandler.handleKey(code);
            }

            if (code == KeyCode.N) {
                newGame(null);
            }


            gamePanel.requestFocus();
            keyEvent.consume();

        });
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(false);
        }
        
        // Wire up game over panel buttons
        if (gameOverPanel != null) {
            if (gameOverPanel.getPlayAgainButton() != null) {
                gameOverPanel.getPlayAgainButton().setOnAction(this::newGame);
            }
            if (gameOverPanel.getMainMenuButton() != null) {
                gameOverPanel.getMainMenuButton().setOnAction(this::returnToMainMenu);
            }
        }
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        if (!gridCreated) {
            createGameBoardGrid(boardMatrix);
            gridCreated = true;
        }
        
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        double gameBoardX = gameBoard.getLayoutX() + 12;
        double gameBoardY = gameBoard.getLayoutY() + 12;
        brickPanel.setLayoutX(gameBoardX + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
        brickPanel.setLayoutY(gameBoardY - 42 + brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE));

        initNextPiecePanel(brick);
    }

    private void createGameBoardGrid(int[][] boardMatrix) {
        Group gridGroup = new Group();
        gridGroup.setMouseTransparent(true);
        gridGroup.setManaged(false);
        Color gridColor = Color.rgb(74, 158, 255, 0.3);
        
        int rows = boardMatrix.length - 2;
        int cols = boardMatrix[0].length;
        double cellSize = BRICK_SIZE + 1;
        
        for (int i = 0; i <= rows; i++) {
            Line horizontalLine = new Line(0, i * cellSize, cols * cellSize, i * cellSize);
            horizontalLine.setStroke(gridColor);
            horizontalLine.setStrokeWidth(0.5);
            horizontalLine.setMouseTransparent(true);
            gridGroup.getChildren().add(horizontalLine);
        }
        
        for (int j = 0; j <= cols; j++) {
            Line verticalLine = new Line(j * cellSize, 0, j * cellSize, rows * cellSize);
            verticalLine.setStroke(gridColor);
            verticalLine.setStrokeWidth(0.5);
            verticalLine.setMouseTransparent(true);
            gridGroup.getChildren().add(verticalLine);
        }
        
        gamePanel.getChildren().add(0, gridGroup);
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            double gameBoardX = gameBoard.getLayoutX() + 12;
            double gameBoardY = gameBoard.getLayoutY() + 12;
            brickPanel.setLayoutX(gameBoardX + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
            brickPanel.setLayoutY(gameBoardY - 42 + brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE));
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            updateNextPiecePanel(brick);
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.setLayoutX(-110);
                notificationPanel.setLayoutY(-100);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("%d"));
    }

    public void bindLines(IntegerProperty integerProperty) {
        linesLabel.textProperty().bind(integerProperty.asString("%d"));
        
        int initialLines = integerProperty.get();
        int initialLevel = (initialLines / 10) + 1;
        levelLabel.setText(String.valueOf(initialLevel));
        if (animationController != null) {
            animationController.updateSpeed(initialLevel);
        }
        
        integerProperty.addListener((obs, oldVal, newVal) -> {
            int lines = newVal.intValue();
            int level = (lines / 10) + 1;
            levelLabel.setText(String.valueOf(level));
            
            if (animationController != null) {
                animationController.updateSpeed(level);
            }
        });
    }

    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
    }

    private void initNextPiecePanel(ViewData brick) {
        if (nextPiece1 == null || brick == null) return;

        nextPiece1.getChildren().clear();
        
        int[][] nextBrickData = brick.getNextBrickData();
        if (nextBrickData == null || nextBrickData.length == 0) return;
        
        nextPieceRectangles = new Rectangle[nextBrickData.length][];
        for (int i = 0; i < nextBrickData.length; i++) {
            if (nextBrickData[i] != null) {
                nextPieceRectangles[i] = new Rectangle[nextBrickData[i].length];
            }
        }
        
        for (int i = 0; i < nextBrickData.length; i++) {
            if (nextBrickData[i] != null) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(getFillColor(nextBrickData[i][j]));
                    setRectangleData(nextBrickData[i][j], rectangle);
                    nextPieceRectangles[i][j] = rectangle;
                    nextPiece1.add(rectangle, j, i);
                }
            }
        }
    }

    private void updateNextPiecePanel(ViewData brick) {
        if (nextPiece1 == null || brick == null) return;
        
        int[][] nextBrickData = brick.getNextBrickData();
        if (nextBrickData == null || nextBrickData.length == 0) return;

        if (nextPieceRectangles == null || 
            nextPieceRectangles.length != nextBrickData.length ||
            (nextPieceRectangles.length > 0 && nextPieceRectangles[0] != null && 
             nextBrickData[0] != null && nextPieceRectangles[0].length != nextBrickData[0].length)) {
            initNextPiecePanel(brick);
            return;
        }

        for (int i = 0; i < nextBrickData.length && i < nextPieceRectangles.length; i++) {
            if (nextBrickData[i] != null && nextPieceRectangles[i] != null) {
                for (int j = 0; j < nextBrickData[i].length && j < nextPieceRectangles[i].length; j++) {
                    if (nextPieceRectangles[i][j] != null) {
                        setRectangleData(nextBrickData[i][j], nextPieceRectangles[i][j]);
                    }
                }
            }
        }
    }

    public void gameOver() {
        // Set final score from current score label
        try {
            int finalScore = Integer.parseInt(scoreLabel.getText());
            gameOverPanel.setFinalScore(finalScore);
        } catch (NumberFormatException e) {
            gameOverPanel.setFinalScore(0);
        }
        
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
        }
        gameOverPanel.playAnimation();
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        isPause.setValue(!isPause.getValue());
        if (isPause.getValue()) {
            if (pauseButton != null) {
                pauseButton.setText("▶");
            }
            if (pauseMenuOverlay != null) {
                pauseMenuOverlay.setVisible(true);
            }
            if (animationController != null) {
                animationController.pause();
            }
        } else {
            if (pauseButton != null) {
                pauseButton.setText("⏸");
            }
            if (pauseMenuOverlay != null) {
                pauseMenuOverlay.setVisible(false);
            }
            if (animationController != null) {
                animationController.resume();
            }
        }
        gamePanel.requestFocus();
    }

    public void resumeGame(ActionEvent actionEvent) {
        isPause.setValue(false);
        if (pauseButton != null) {
            pauseButton.setText("⏸");
        }
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(false);
        }
        if (animationController != null) {
            animationController.resume();
        }
        gamePanel.requestFocus();
    }

    public void showHowToPlay(ActionEvent actionEvent) {
        System.out.println("How to Play (placeholder)");
    }

    public void openSettings(ActionEvent actionEvent) {
        System.out.println("Settings (placeholder)");
    }

    public void returnToMainMenu(ActionEvent actionEvent) {
        try {
            URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            MainMenuController controller = fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);

            Scene menuScene = new Scene(root, 700, 600);
            primaryStage.setScene(menuScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
    public void moveDownFromTimer() {
        if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
            moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
        }
    }

    public void setInputHandler(InputHandler handler) {
        this.inputHandler = handler;
    }


}
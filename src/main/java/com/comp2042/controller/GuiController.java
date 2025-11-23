package com.comp2042.controller;

import com.comp2042.logic.board.DownData;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.NotificationPanel;
import com.comp2042.view.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


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


    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] nextPieceRectangles;


    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private InputHandler inputHandler;

    private AnimationController animationController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        gamePanel.setOnKeyPressed(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (!isPause.get() && !isGameOver.get() && inputHandler != null) {
                inputHandler.handleKey(code);
            }

            if (code == KeyCode.N) {
                newGame(null);
            }


            gamePanel.requestFocus();
            keyEvent.consume();

        });
        gameOverPanel.setVisible(false);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
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
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        isPause.setValue(!isPause.getValue());
        if (isPause.getValue()) {
            pauseButton.setText("▶");
        } else {
            pauseButton.setText("⏸");
        }
        gamePanel.requestFocus();
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
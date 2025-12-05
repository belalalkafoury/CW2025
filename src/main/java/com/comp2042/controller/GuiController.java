package com.comp2042.controller;

import com.comp2042.logic.board.DownData;
import com.comp2042.logic.score.HighScoreService;
import com.comp2042.model.Board;
import com.comp2042.model.GameBoard;
import com.comp2042.model.GameMode;
import com.comp2042.view.GameModePanel;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.HowToPlayPanel;
import com.comp2042.view.NotificationPanel;
import com.comp2042.view.SettingsPanel;
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
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.List;

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
    private GridPane ghostPanel;



    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Group gameOverOverlay;

    @FXML
    private Label scoreHeaderLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label levelHeaderLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label linesHeaderLabel;

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

    @FXML
    private Group howToPlayOverlay;

    @FXML
    private HowToPlayPanel howToPlayPanel;

    @FXML
    private Group countdownOverlay;

    @FXML
    private Label countdownLabel;

    @FXML
    private Group settingsOverlay;

    @FXML
    private SettingsPanel settingsPanel;

    @FXML
    private Group gameModeOverlay;

    @FXML
    private GameModePanel gameModePanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private GameController gameController;

    private Rectangle[][] rectangles;

    private Rectangle[][] nextPieceRectangles;

    private Rectangle[][] ghostRectangles;

    private java.util.List<Rectangle> activeGhostRects = new java.util.ArrayList<>();

    private Board board;

    private boolean gridCreated = false;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private InputHandler inputHandler;

    private AnimationController animationController;

    private Stage primaryStage;

    private HighScoreService highScoreService;

    private SoundController soundController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        try {
            Font.loadFont(getClass().getClassLoader().getResourceAsStream("press-start-2p-font/PressStart2P-vaV7.ttf"), 18);
        } catch (Exception e) {
            System.err.println("Could not load Press Start 2P font: " + e.getMessage());
        }
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
        if (howToPlayOverlay != null) {
            howToPlayOverlay.setVisible(false);
        }
        if (countdownOverlay != null) {
            countdownOverlay.setVisible(false);
        }

        if (gameOverPanel != null) {
            if (gameOverPanel.getPlayAgainButton() != null) {
                gameOverPanel.getPlayAgainButton().setOnAction(this::newGame);
            }
            if (gameOverPanel.getMainMenuButton() != null) {
                gameOverPanel.getMainMenuButton().setOnAction(this::returnToMainMenu);
            }
        }

        if (howToPlayPanel != null && howToPlayPanel.getBackButton() != null) {
            howToPlayPanel.getBackButton().setOnAction(e -> hideHowToPlay());
        }

        if (settingsOverlay != null) {
            settingsOverlay.setVisible(false);
        }

        highScoreService = new HighScoreService();

        if (settingsPanel != null) {
            settingsPanel.setHighScoreService(highScoreService);
            if (settingsPanel.getDoneButton() != null) {
                settingsPanel.getDoneButton().setOnAction(e -> hideSettings());
            }
        }

        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(false);
        }

        if (gameModePanel != null) {
            gameModePanel.setClassicAction(() -> changeGameMode(GameMode.CLASSIC));
            gameModePanel.setTimeAttackAction(() -> changeGameMode(GameMode.TIME_ATTACK));
            gameModePanel.setPuzzleAction(() -> changeGameMode(GameMode.PUZZLE));
            gameModePanel.setRevertedAction(() -> changeGameMode(GameMode.REVERTED));
            gameModePanel.setBackAction(() -> hideGameModeSelection());
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
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        double gameBoardX = gameBoard.getLayoutX() + 8;
        double gameBoardY = gameBoard.getLayoutY() + 8;
        double cellSize = BRICK_SIZE + 1;
        brickPanel.setLayoutX(gameBoardX + brick.getxPosition() * cellSize);
        brickPanel.setLayoutY(gameBoardY - 42 + (brick.getyPosition() - 2) * cellSize);

        initNextPiecePanel(brick);
        initGhostPiece(brick);
    }

    private void initGhostPiece(ViewData brick) {
        int[][] shape = brick.getBrickData();
        ghostRectangles = new Rectangle[shape.length][shape[0].length];
        ghostPanel.setHgap(1);
        ghostPanel.setVgap(1);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                rect.setFill(Color.TRANSPARENT);
                rect.setArcHeight(9);
                rect.setArcWidth(9);
                ghostRectangles[i][j] = rect;
                ghostPanel.add(rect, j, i);
            }
        }
        updateGhostPiece(brick);
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
            double gameBoardX = gameBoard.getLayoutX() + 8;
            double gameBoardY = gameBoard.getLayoutY() + 8;
            double cellSize = BRICK_SIZE + 1;
            
            updateGhostPiece(brick);
            
            if (brickPanel.getParent() != null && 
                !isPause.get() && 
                !isGameOver.get() &&
                (pauseMenuOverlay == null || !pauseMenuOverlay.isVisible()) &&
                (howToPlayOverlay == null || !howToPlayOverlay.isVisible()) &&
                (settingsOverlay == null || !settingsOverlay.isVisible()) &&
                (gameOverOverlay == null || !gameOverOverlay.isVisible()) &&
                (countdownOverlay == null || !countdownOverlay.isVisible())) {
                brickPanel.toFront();
            }
            
            int currentY = brick.getyPosition();
            int ghostY = board.getGhostY(brick.getxPosition(), currentY);
            boolean isAtLandingPosition = (ghostY == currentY);
            
            brickPanel.setLayoutX(gameBoardX + brick.getxPosition() * cellSize);
            if (isAtLandingPosition) {
                brickPanel.setLayoutY(gameBoardY - 42 + currentY * cellSize);
            } else {
                brickPanel.setLayoutY(gameBoardY - 42 + (currentY - 2) * cellSize);
            }
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    rectangles[i][j].setOpacity(1.0);
                }
            }
            updateNextPiecePanel(brick);
        }
    }

    private void updateGhostPiece(ViewData brick) {
        if (board == null || ghostPanel == null || ghostRectangles == null) {
            return;
        }

        if (!com.comp2042.model.GameSettings.isShowGhostPiece()) {
            ghostPanel.setVisible(false);
            return;
        }

        for (Rectangle rect : activeGhostRects) {
            if (rect != null) {
                rect.setFill(Color.TRANSPARENT);
                rect.setVisible(false);
            }
        }
        activeGhostRects.clear();

        int currentX = brick.getxPosition();
        int currentY = brick.getyPosition();
        int ghostY = board.getGhostY(currentX, currentY);

        int[][] shape = board.getCurrentShape();

        if (shape == null || shape.length == 0 || shape[0].length == 0) {
            ghostPanel.setVisible(false);
            return;
        }

        int distance = ghostY - currentY;

        if (distance < 0) {
            ghostPanel.setVisible(false);
            return;
        }

        ghostPanel.setVisible(true);

        if (ghostRectangles.length != shape.length ||
                (ghostRectangles.length > 0 && ghostRectangles[0].length != shape[0].length)) {
            ghostPanel.getChildren().clear();
            ghostRectangles = new Rectangle[shape.length][shape[0].length];
            ghostPanel.setHgap(1);
            ghostPanel.setVgap(1);
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    Rectangle rect = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                    rect.setFill(Color.TRANSPARENT);
                    rect.setArcHeight(9);
                    rect.setArcWidth(9);
                    ghostRectangles[i][j] = rect;
                    ghostPanel.add(rect, j, i);
                }
            }
        }

        double gameBoardX = gameBoard.getLayoutX() + 8;
        double gameBoardY = gameBoard.getLayoutY() + 8;
        double cellSize = BRICK_SIZE + 1;

        ghostPanel.setLayoutX(gameBoardX + currentX * cellSize);
        ghostPanel.setLayoutY(gameBoardY - 42 + ghostY * cellSize);

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (i < ghostRectangles.length && j < ghostRectangles[i].length) {
                    Rectangle rect = ghostRectangles[i][j];
                    if (shape[i][j] != 0) {
                        Paint color = getFillColor(shape[i][j]);
                        if (color instanceof Color) {
                            rect.setFill(Color.TRANSPARENT);
                            rect.setStroke((Color) color);
                            rect.setStrokeWidth(2.0);
                            rect.setOpacity(1.0);
                            rect.setVisible(true);
                            activeGhostRects.add(rect);
                        }
                    } else {
                        rect.setVisible(false);
                    }
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    public void hideGhostPiece() {
        if (ghostPanel != null) {
            ghostPanel.setVisible(false);
        }
    }

    public void hideFallingBrick() {
        if (brickPanel != null) {
            brickPanel.setVisible(false);
        }
    }

    public void showFallingBrick() {
        if (brickPanel != null) {
            brickPanel.setVisible(true);
        }
    }

    public void animateClear(List<Integer> rows, Runnable onFinished) {
        if (rows == null || rows.isEmpty() || displayMatrix == null) {
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        if (!com.comp2042.model.GameSettings.isShowAnimations()) {
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        ParallelTransition parallelTransition = new ParallelTransition();

        for (Integer rowIndex : rows) {
            if (rowIndex >= 2 && rowIndex < displayMatrix.length) {
                for (int col = 0; col < displayMatrix[rowIndex].length; col++) {
                    Rectangle rect = displayMatrix[rowIndex][col];
                    if (rect != null && rect.isVisible() && rect.getOpacity() > 0.1) {
                        Color originalColor = (Color) rect.getFill();
                        
                        FillTransition flash = new FillTransition(Duration.millis(100), rect);
                        flash.setFromValue(originalColor);
                        flash.setToValue(Color.WHITE);
                        
                        FadeTransition fade = new FadeTransition(Duration.millis(200), rect);
                        fade.setFromValue(1.0);
                        fade.setToValue(0.0);
                        
                        SequentialTransition sequence = new SequentialTransition(flash, fade);
                        parallelTransition.getChildren().add(sequence);
                    }
                }
            }
        }

        if (parallelTransition.getChildren().isEmpty()) {
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        parallelTransition.setOnFinished(e -> {
            for (int i = 2; i < displayMatrix.length; i++) {
                for (int j = 0; j < displayMatrix[i].length; j++) {
                    if (displayMatrix[i][j] != null) {
                        displayMatrix[i][j].setOpacity(1.0);
                    }
                }
            }
            if (onFinished != null) {
                onFinished.run();
            }
        });

        parallelTransition.play();
    }

    public void animatePlacedBlocks(ViewData lastBrick) {
        if (lastBrick == null || displayMatrix == null) {
            return;
        }

        int[][] shape = lastBrick.getBrickData();
        int xPos = lastBrick.getxPosition();
        int yPos = lastBrick.getyPosition();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[j][i] != 0) {
                    int boardRow = yPos + j;
                    int boardCol = xPos + i;

                    if (boardRow >= 2 && boardRow < displayMatrix.length &&
                            boardCol >= 0 && boardCol < displayMatrix[0].length) {
                        Rectangle rect = displayMatrix[boardRow][boardCol];
                        if (rect != null) {
                            FadeTransition blink = new FadeTransition(Duration.millis(150), rect);
                            blink.setFromValue(1.0);
                            blink.setToValue(0.3);
                            blink.setCycleCount(4);
                            blink.setAutoReverse(true);
                            blink.play();
                        }
                    }
                }
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
        if (eventListener instanceof GameController) {
            this.gameController = (GameController) eventListener;
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("%d"));
    }

    public void bindLines(IntegerProperty integerProperty, SoundController soundController) {
        this.soundController = soundController;
        if (settingsPanel != null) {
            settingsPanel.setSoundController(soundController);
        }
        linesLabel.textProperty().bind(integerProperty.asString("%d"));

        int initialLines = integerProperty.get();
        int initialLevel = (initialLines / 10) + 1;
        levelLabel.setText(String.valueOf(initialLevel));
        if (animationController != null) {
            animationController.updateSpeed(initialLevel);
        }

        integerProperty.addListener((obs, oldVal, newVal) -> {
            int oldLines = oldVal.intValue();
            int newLines = newVal.intValue();
            int oldLevel = (oldLines / 10) + 1;
            int newLevel = (newLines / 10) + 1;
            
            levelLabel.setText(String.valueOf(newLevel));

            if (animationController != null) {
                animationController.updateSpeed(newLevel);
            }
            
            if (soundController != null && newLevel > oldLevel) {
                soundController.playLevelUp();
            }
        });
    }

    public void configureTimeAttackMode(int initialTime, int targetScore) {
        if (levelHeaderLabel != null) {
            levelHeaderLabel.setText("TIME");
        }
        if (linesHeaderLabel != null) {
            linesHeaderLabel.setText("GOAL");
        }
        
        if (linesLabel != null) {
            linesLabel.textProperty().unbind();
            linesLabel.setText(String.valueOf(targetScore));
        }
        updateTimer(initialTime);
    }

    public void updateTimer(int seconds) {
        if (levelLabel != null) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            String timeString = String.format("%d:%02d", minutes, remainingSeconds);
            levelLabel.setText(timeString);
        }
    }

    public void configurePuzzleMode(int lineGoal) {
        if (levelHeaderLabel != null) {
            levelHeaderLabel.setText("GOAL");
        }
        if (levelLabel != null) {
            levelLabel.textProperty().unbind();
            levelLabel.setText(String.valueOf(lineGoal));
        }
        if (linesHeaderLabel != null) {
            linesHeaderLabel.setText("DONE");
        }
    }

    private void restoreClassicModeLabels() {
        if (levelHeaderLabel != null) {
            levelHeaderLabel.setText("LEVEL");
        }
        if (linesHeaderLabel != null) {
            linesHeaderLabel.setText("LINES");
        }
    }

    private void changeGameMode(GameMode newMode) {
        if (animationController != null) {
            animationController.stop();
        }
        if (gameController != null) {
            gameController.pauseTimer();
        }

        if (scoreLabel != null) {
            scoreLabel.textProperty().unbind();
        }
        if (linesLabel != null) {
            linesLabel.textProperty().unbind();
        }

        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        
        hideGameModeSelection();
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(false);
        }
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        if (brickPanel != null) {
            brickPanel.setVisible(true);
        }

        if (board != null) {
            board.newGame();
        } else {
            board = new GameBoard(25, 10);
        }

        if (newMode != GameMode.TIME_ATTACK && newMode != GameMode.PUZZLE) {
            restoreClassicModeLabels();
        }

        gameController = new GameController(this, board, soundController, newMode);
        
        refreshGameBackground(board.getBoardMatrix());
        gamePanel.requestFocus();
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

    public void gameOver(SoundController soundController) {
        hideGhostPiece();
        if (brickPanel != null) {
            brickPanel.setVisible(false);
        }

        if (gameOverPanel != null) {
            gameOverPanel.setMessage("GAME OVER");
        }

        try {
            int finalScore = Integer.parseInt(scoreLabel.getText());
            gameOverPanel.setFinalScore(finalScore);
            
            if (highScoreService != null) {
                boolean isNewHighScore = highScoreService.updateHighScore(finalScore);
                gameOverPanel.setHighScore(highScoreService.getHighScore());
                
                if (soundController != null) {
                    if (isNewHighScore) {
                        soundController.playHighScore();
                    } else {
                        soundController.playGameOver();
                    }
                }
            } else if (soundController != null) {
                soundController.playGameOver();
            }
        } catch (NumberFormatException e) {
            gameOverPanel.setFinalScore(0);
            if (highScoreService != null) {
                gameOverPanel.setHighScore(highScoreService.getHighScore());
            }
            if (soundController != null) {
                soundController.playGameOver();
            }
        }

        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.toFront();
        }
        gameOverPanel.playAnimation();
        isGameOver.setValue(Boolean.TRUE);
    }

    public void gameWin(SoundController soundController) {
        hideGhostPiece();
        if (brickPanel != null) {
            brickPanel.setVisible(false);
        }

        if (gameOverPanel != null) {
            gameOverPanel.setMessage("YOU WIN");
        }

        try {
            int finalScore = Integer.parseInt(scoreLabel.getText());
            gameOverPanel.setFinalScore(finalScore);
            
            if (highScoreService != null) {
                boolean isNewHighScore = highScoreService.updateHighScore(finalScore);
                gameOverPanel.setHighScore(highScoreService.getHighScore());
            }
        } catch (NumberFormatException e) {
            gameOverPanel.setFinalScore(0);
            if (highScoreService != null) {
                gameOverPanel.setHighScore(highScoreService.getHighScore());
            }
        }

        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.toFront();
        }
        gameOverPanel.playAnimation();
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        if (brickPanel != null) {
            brickPanel.setVisible(true);
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
                pauseMenuOverlay.toFront();
            }
            if (animationController != null) {
                animationController.pause();
            }
            if (gameController != null) {
                gameController.pauseTimer();
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
            if (gameController != null) {
                gameController.resumeTimer();
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
        if (gameController != null) {
            gameController.resumeTimer();
        }
        gamePanel.requestFocus();
    }

    public void showHowToPlay(ActionEvent actionEvent) {
        if (howToPlayOverlay != null) {
            howToPlayOverlay.setVisible(true);
            howToPlayOverlay.toFront();
            if (howToPlayPanel != null) {
                howToPlayPanel.playAnimation();
            }
        }
    }

    public void hideHowToPlay() {
        if (howToPlayOverlay != null) {
            howToPlayOverlay.setVisible(false);
        }
    }

    @FXML
    public void showGameModeSelection(ActionEvent actionEvent) {
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(false);
        }
        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(true);
            gameModeOverlay.toFront();
            if (gameModePanel != null) {
                gameModePanel.playAnimation();
            }
        }
    }

    private void hideGameModeSelection() {
        if (gameModeOverlay != null) {
            gameModeOverlay.setVisible(false);
        }
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(true);
            pauseMenuOverlay.toFront();
        }
    }

    public void showCountdown(SoundController soundController, Runnable onComplete) {
        if (countdownOverlay == null || countdownLabel == null) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        countdownOverlay.setVisible(true);
        countdownOverlay.toFront();
        countdownLabel.setVisible(true);

        Timeline countdownTimeline = new Timeline();
        String[] countdownTexts = {"3", "2", "1", "GO!"};

        for (int i = 0; i < countdownTexts.length; i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(i * 1000),
                e -> {
                    countdownLabel.setText(countdownTexts[index]);
                    countdownLabel.setOpacity(1.0);
                    countdownLabel.setScaleX(1.5);
                    countdownLabel.setScaleY(1.5);

                    FadeTransition fadeOut = new FadeTransition(Duration.millis(800), countdownLabel);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(Duration.millis(800), countdownLabel);
                    scaleDown.setFromX(1.5);
                    scaleDown.setFromY(1.5);
                    scaleDown.setToX(1.0);
                    scaleDown.setToY(1.0);

                    ParallelTransition transition = new ParallelTransition(fadeOut, scaleDown);
                    transition.play();
                }
            );
            countdownTimeline.getKeyFrames().add(keyFrame);
        }

        KeyFrame hideFrame = new KeyFrame(
            Duration.millis(countdownTexts.length * 1000),
            e -> {
                countdownOverlay.setVisible(false);
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        );
        countdownTimeline.getKeyFrames().add(hideFrame);

        countdownTimeline.play();
    }

    public void openSettings(ActionEvent actionEvent) {
        if (settingsOverlay != null) {
            settingsOverlay.setVisible(true);
            settingsOverlay.toFront();
            if (settingsPanel != null) {
                settingsPanel.playAnimation();
            }
        }
    }

    private void hideSettings() {
        if (settingsOverlay != null) {
            settingsOverlay.setVisible(false);
        }
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
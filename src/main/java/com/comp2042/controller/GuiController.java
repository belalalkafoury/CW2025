package com.comp2042.controller;

import com.comp2042.logic.board.DownData;
import com.comp2042.logic.score.HighScoreManager;
import com.comp2042.model.Board;
import com.comp2042.model.HighScoreEntry;
import com.comp2042.model.GameBoard;
import com.comp2042.model.GameMode;
import com.comp2042.view.GameModePanel;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.HowToPlayPanel;
import com.comp2042.view.LeaderboardPanel;
import com.comp2042.view.SettingsPanel;
import com.comp2042.view.ViewData;
import com.comp2042.view.Particle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import java.util.List;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Manages the GUI and view updates for the Tetris game.
 * Handles rendering of the game board, bricks, ghost pieces, animations, and UI panels.
 * Coordinates between the game logic and JavaFX UI components.
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int NEXT_PIECE_BRICK_SIZE = 16;
    private static final int CELL_GAP = 1;
    private static final int BORDER_OFFSET = 8;
    private static final int ARC_SIZE = 9;
    private static final int PANEL_OFFSET_X = 12;
    private static final int PANEL_OFFSET_Y = 42;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Pane gameAreaPane;
    
    @FXML
    private Pane rootPane;

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
    private Label highScoreLabel;

    @FXML
    private Label highScoreNameLabel;

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
    private GridPane nextPiece2;

    @FXML
    private GridPane nextPiece3;

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

    @FXML
    private Group leaderboardOverlay;

    @FXML
    private LeaderboardPanel leaderboardPanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private GameController gameController;

    private Rectangle[][] rectangles;

    private Rectangle[][] nextPiece1Rectangles;
    private Rectangle[][] nextPiece2Rectangles;
    private Rectangle[][] nextPiece3Rectangles;

    private Rectangle[][] ghostRectangles;

    private java.util.List<Rectangle> activeGhostRects = new java.util.ArrayList<>();

    private Board board;

    private boolean gridCreated = false;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private InputHandler inputHandler;

    private AnimationController animationController;

    private Stage primaryStage;

    private HighScoreManager highScoreManager;
    private GameMode currentGameMode;

    private SoundController soundController;

    /**
     * Initializes the GUI controller and sets up UI components.
     * @param location The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
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

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gameAreaPane.widthProperty());
        clip.heightProperty().bind(gameAreaPane.heightProperty());
        gameAreaPane.setClip(clip);

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
            if (gameOverPanel.getLeaderboardButton() != null) {
                gameOverPanel.getLeaderboardButton().setOnAction(e -> showLeaderboard());
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

        highScoreManager = new HighScoreManager();

        if (leaderboardOverlay != null) {
            leaderboardOverlay.setVisible(false);
        }

        if (leaderboardPanel != null) {
            leaderboardPanel.setHighScoreManager(highScoreManager);
            if (leaderboardPanel.getBackButton() != null) {
                leaderboardPanel.getBackButton().setOnAction(e -> hideLeaderboard());
            }
        }

        if (settingsPanel != null) {
            settingsPanel.setHighScoreManager(highScoreManager);
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

    public void resetView() {
        if (brickPanel != null) {
            brickPanel.getChildren().clear();
            brickPanel.setLayoutX(0);
            brickPanel.setLayoutY(0);
        }

        if (ghostPanel != null) {
            ghostPanel.getChildren().clear();
            ghostPanel.setLayoutX(0);
            ghostPanel.setLayoutY(0);
        }

        if (holdPanel != null) {
            holdPanel.getChildren().clear();
        }

        if (nextPiece1 != null) {
            nextPiece1.getChildren().clear();
        }
        if (nextPiece2 != null) {
            nextPiece2.getChildren().clear();
        }
        if (nextPiece3 != null) {
            nextPiece3.getChildren().clear();
        }

        if (displayMatrix != null) {
            for (int i = 0; i < displayMatrix.length; i++) {
                if (displayMatrix[i] != null) {
                    for (int j = 0; j < displayMatrix[i].length; j++) {
                        if (displayMatrix[i][j] != null) {
                            displayMatrix[i][j].setFill(Color.TRANSPARENT);
                        }
                    }
                }
            }
        }

        if (scoreLabel != null) {
            scoreLabel.setText("0");
        }
        if (levelLabel != null) {
            levelLabel.setText("1");
        }
        if (linesLabel != null) {
            linesLabel.setText("0");
        }
    }

    /**
     * Initializes the game view with the board matrix and initial brick data.
     * Creates the game board grid if it doesn't exist.
     * @param boardMatrix The initial game board state
     * @param brick The initial brick view data
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        resetView();

        if (!gridCreated) {
            createGameBoardGrid(boardMatrix);
            gridCreated = true;
        }

        if (displayMatrix != null) {
            for (int i = 0; i < displayMatrix.length; i++) {
                for (int j = 0; j < displayMatrix[i].length; j++) {
                    if (displayMatrix[i][j] != null) {
                        displayMatrix[i][j].setFill(Color.TRANSPARENT);
                    }
                }
            }
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
                rectangle.setArcHeight(ARC_SIZE);
                rectangle.setArcWidth(ARC_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        double cellSize = BRICK_SIZE + CELL_GAP;
        double borderOffset = BORDER_OFFSET;
        brickPanel.setLayoutX(0);
        brickPanel.setLayoutY(0);
        
        double startX = borderOffset + brick.getxPosition() * cellSize;
        double startY = borderOffset + (brick.getyPosition() - 2) * cellSize;
        brickPanel.setLayoutX(startX);
        brickPanel.setLayoutY(startY);

        updateNextPieces(brick.getNextPieces());
        updateHeldPiece(brick.getHeldBrickData());
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
        double cellSize = BRICK_SIZE + CELL_GAP;

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

    /**
     * Refreshes the display of the current falling brick and ghost piece.
     * @param brick The view data containing current brick position and shape
     */
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            double cellSize = BRICK_SIZE + CELL_GAP;
            double borderOffset = BORDER_OFFSET;
            
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
            int currentX = brick.getxPosition();
            
            double startX = borderOffset + currentX * cellSize;
            double startY = borderOffset + (currentY - 2) * cellSize;
            brickPanel.setLayoutX(startX);
            brickPanel.setLayoutY(startY);
            drawBrick(brickPanel, brick.getBrickData(), rectangles);
            updateNextPieces(brick.getNextPieces());
            updateHeldPiece(brick.getHeldBrickData());
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
                    rect.setArcHeight(ARC_SIZE);
                    rect.setArcWidth(ARC_SIZE);
                    ghostRectangles[i][j] = rect;
                    ghostPanel.add(rect, j, i);
                }
            }
        }

        double cellSize = BRICK_SIZE + CELL_GAP;
        double borderOffset = BORDER_OFFSET;
        
        ghostPanel.setLayoutX(0);
        ghostPanel.setLayoutY(0);
        
        double ghostStartX = borderOffset + currentX * cellSize;
        double ghostStartY = borderOffset + (ghostY - 2) * cellSize;
        ghostPanel.setLayoutX(ghostStartX);
        ghostPanel.setLayoutY(ghostStartY);

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

    /**
     * Refreshes the game board background with the current board state.
     * @param board The current game board matrix
     */
    public void refreshGameBackground(int[][] board) {
        if (displayMatrix == null) {
            return;
        }
        for (int i = 0; i < displayMatrix.length; i++) {
            for (int j = 0; j < displayMatrix[i].length; j++) {
                if (displayMatrix[i][j] != null) {
                    displayMatrix[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i < displayMatrix.length && j < displayMatrix[i].length && displayMatrix[i][j] != null) {
                    setRectangleData(board[i][j], displayMatrix[i][j]);
                }
            }
        }
    }

    /**
     * Hides the ghost piece preview.
     */
    public void hideGhostPiece() {
        if (ghostPanel != null) {
            ghostPanel.setVisible(false);
        }
    }

    /**
     * Hides the falling brick display.
     */
    public void hideFallingBrick() {
        if (brickPanel != null) {
            brickPanel.setVisible(false);
        }
    }

    /**
     * Shows the falling brick display.
     */
    public void showFallingBrick() {
        if (brickPanel != null) {
            brickPanel.setVisible(true);
        }
    }

    /**
     * Animates the clearing of specified rows with particle effects.
     * @param rows List of row indices to clear
     * @param onFinished Callback to execute when animation completes
     */
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
                        
                        double cellSize = BRICK_SIZE + CELL_GAP;
                        double blockX = (gameAreaPane != null ? gameAreaPane.getLayoutX() : 250) + BORDER_OFFSET + col * cellSize + cellSize / 2;
                        double blockY = (gameAreaPane != null ? gameAreaPane.getLayoutY() : 50) + BORDER_OFFSET + (rowIndex - 2) * cellSize + cellSize / 2;
                        
                        int particleCount = 5 + (int)(Math.random() * 6);
                        for (int i = 0; i < particleCount; i++) {
                            Particle particle = new Particle(blockX, blockY, originalColor);
                            if (rootPane != null) {
                                rootPane.getChildren().add(particle);
                            } else if (groupNotification != null) {
                                groupNotification.getChildren().add(particle);
                            }
                        }
                        
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

    private void showFloatingText(String text, double x, double y, Color color) {
        if (groupNotification == null) {
            return;
        }

        Text textNode = new Text(text);
        try {
            Font font = Font.loadFont(getClass().getClassLoader().getResourceAsStream("press-start-2p-font/PressStart2P-vaV7.ttf"), 20);
            textNode.setFont(font);
        } catch (Exception e) {
            textNode.setFont(Font.font("Arial", 20));
        }
        textNode.setFill(color);
        textNode.setStroke(Color.WHITE);
        textNode.setStrokeWidth(1);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(color);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.5);
        textNode.setEffect(dropShadow);

        groupNotification.getChildren().add(textNode);
        
        javafx.application.Platform.runLater(() -> {
            double textWidth = textNode.getBoundsInLocal().getWidth();
            textNode.setLayoutX(x - (textWidth / 2));
        });
        
        textNode.setLayoutX(x);
        textNode.setLayoutY(y);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), textNode);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), textNode);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), textNode);
        translateTransition.setByY(-50);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition, translateTransition);
        parallelTransition.setOnFinished(e -> groupNotification.getChildren().remove(textNode));
        parallelTransition.play();
    }

    public void showComboAnimation(int combo) {
        if (combo < 2 || gameAreaPane == null || groupNotification == null) {
            return;
        }

        double boardCenterX = gameAreaPane.getLayoutX() + (gameAreaPane.getWidth() / 2);
        double boardCenterY = gameAreaPane.getLayoutY() + (gameAreaPane.getHeight() / 2);
        
        double relativeX = boardCenterX - groupNotification.getLayoutX();
        double relativeY = boardCenterY - groupNotification.getLayoutY();
        
        showFloatingText("COMBO x" + combo, relativeX, relativeY, Color.CYAN);
    }

    public void showScoreAnimation(String scoreText) {
        if (gameAreaPane == null || groupNotification == null) {
            return;
        }

        double boardCenterX = gameAreaPane.getLayoutX() + (gameAreaPane.getWidth() / 2);
        double boardCenterY = gameAreaPane.getLayoutY() + (gameAreaPane.getHeight() / 2);
        
        double relativeX = boardCenterX - groupNotification.getLayoutX();
        double relativeY = boardCenterY - groupNotification.getLayoutY();
        
        showFloatingText(scoreText, relativeX, relativeY, Color.YELLOW);
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(ARC_SIZE);
        rectangle.setArcWidth(ARC_SIZE);
    }

    private Rectangle[][] drawMatrixToGrid(GridPane targetGrid, int[][] matrix, int brickSize) {
        if (targetGrid == null || matrix == null || matrix.length == 0) {
            return null;
        }

        targetGrid.getChildren().clear();

        Rectangle[][] rectangles = new Rectangle[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] != null) {
                rectangles[i] = new Rectangle[matrix[i].length];
                for (int j = 0; j < matrix[i].length; j++) {
                    Rectangle rectangle = new Rectangle(brickSize, brickSize);
                    rectangle.setFill(getFillColor(matrix[i][j]));
                    setRectangleData(matrix[i][j], rectangle);
                    rectangles[i][j] = rectangle;
                    targetGrid.add(rectangle, j, i);
                }
            }
        }
        return rectangles;
    }

    private void drawBrick(GridPane targetPanel, int[][] shapeData, Rectangle[][] rectArray) {
        if (targetPanel == null || shapeData == null || shapeData.length == 0) {
            return;
        }

        for (int i = 0; i < shapeData.length; i++) {
            if (shapeData[i] != null && rectArray != null && rectArray[i] != null) {
                for (int j = 0; j < shapeData[i].length; j++) {
                    if (rectArray[i][j] != null) {
                        setRectangleData(shapeData[i][j], rectArray[i][j]);
                        rectArray[i][j].setOpacity(1.0);
                    }
                }
            }
        }
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Sets the event listener for input events.
     * @param eventListener The listener to handle game input events
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        if (eventListener instanceof GameController) {
            this.gameController = (GameController) eventListener;
            if (gameController != null) {
                setCurrentGameMode(gameController.getGameMode());
            }
        }
    }

    /**
     * Sets the game board instance.
     * @param board The Board instance to use
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Binds the score label to a score property for reactive updates.
     * @param integerProperty The score property to bind
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("%d"));
    }

    /**
     * Binds the lines label to a lines property and updates level based on lines cleared.
     * @param integerProperty The lines property to bind
     * @param soundController The sound controller for level-up sounds
     */
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

    /**
     * Configures the UI for Time Attack mode.
     * Updates labels to show time and target score instead of level and lines.
     * @param initialTime Initial time remaining in seconds
     * @param targetScore Target score to achieve
     */
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

    /**
     * Updates the timer display for Time Attack mode.
     * @param seconds The number of seconds remaining
     */
    public void updateTimer(int seconds) {
        if (levelLabel != null) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            String timeString = String.format("%d:%02d", minutes, remainingSeconds);
            levelLabel.setText(timeString);
        }
    }

    /**
     * Configures the UI for Puzzle mode.
     * Updates labels to show lines cleared and goal.
     * @param lineGoal The target number of lines to clear
     */
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

        setCurrentGameMode(newMode);
        gameController = new GameController(this, board, soundController, newMode);
        
        refreshGameBackground(board.getBoardMatrix());
        gamePanel.requestFocus();
    }

    /**
     * Sets the animation controller for managing game animations.
     * @param animationController The AnimationController instance
     */
    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
    }

    private void updateHeldPiece(int[][] heldBrickData) {
        if (holdPanel == null) return;
        drawMatrixToGrid(holdPanel, heldBrickData, NEXT_PIECE_BRICK_SIZE);
    }

    private void updateNextPieces(List<int[][]> nextShapes) {
        if (nextPiece1 == null || nextPiece2 == null || nextPiece3 == null || nextShapes == null) return;

        GridPane[] grids = {nextPiece1, nextPiece2, nextPiece3};

        for (int pieceIndex = 0; pieceIndex < 3 && pieceIndex < nextShapes.size(); pieceIndex++) {
            GridPane grid = grids[pieceIndex];
            if (grid == null) continue;

            int[][] nextBrickData = nextShapes.get(pieceIndex);
            Rectangle[][] rects = drawMatrixToGrid(grid, nextBrickData, NEXT_PIECE_BRICK_SIZE);

            if (pieceIndex == 0) {
                nextPiece1Rectangles = rects;
            } else if (pieceIndex == 1) {
                nextPiece2Rectangles = rects;
            } else if (pieceIndex == 2) {
                nextPiece3Rectangles = rects;
            }
        }
    }

    /**
     * Displays the game over screen and saves the score if applicable.
     * @param soundController The sound controller for playing game over sounds
     */
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
            
            if (highScoreManager != null && currentGameMode != null) {
                String playerName = gameController != null ? gameController.getPlayerName() : "GUEST";
                if (playerName != null && !playerName.equalsIgnoreCase("GUEST")) {
                    highScoreManager.addScore(currentGameMode, playerName, finalScore);
                    
                    if (soundController != null) {
                        HighScoreEntry topEntry = highScoreManager.getHighestScore(currentGameMode);
                        int currentHighScore = topEntry != null ? topEntry.getScore() : 0;
                        if (finalScore > currentHighScore) {
                            soundController.playHighScore();
                        } else {
                            soundController.playGameOver();
                        }
                    }
                } else {
                    if (soundController != null) {
                        soundController.playGameOver();
                    }
                }
            } else if (soundController != null) {
                soundController.playGameOver();
            }
        } catch (NumberFormatException e) {
            gameOverPanel.setFinalScore(0);
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

    /**
     * Displays the win screen and saves the score if applicable.
     * @param soundController The sound controller for playing win sounds
     */
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
            
            if (highScoreManager != null && currentGameMode != null) {
                String playerName = gameController != null ? gameController.getPlayerName() : "GUEST";
                if (playerName != null && !playerName.equalsIgnoreCase("GUEST")) {
                    highScoreManager.addScore(currentGameMode, playerName, finalScore);
                }
            }
        } catch (NumberFormatException e) {
            gameOverPanel.setFinalScore(0);
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

    public void showLeaderboard() {
        if (leaderboardOverlay != null) {
            leaderboardOverlay.setVisible(true);
            leaderboardOverlay.toFront();
            if (leaderboardPanel != null) {
                leaderboardPanel.refresh();
                leaderboardPanel.playAnimation();
            }
        }
    }

    public void hideLeaderboard() {
        if (leaderboardOverlay != null) {
            leaderboardOverlay.setVisible(false);
        }
    }

    public void updateHighScoreDisplay(String name, int score) {
        if (highScoreLabel != null) {
            highScoreLabel.setText(String.valueOf(score));
        }
        if (highScoreNameLabel != null) {
            highScoreNameLabel.setText(name != null && !name.isEmpty() ? name : "-");
        }
    }

    public void initializeHighScoreDisplay() {
        if (highScoreManager != null && currentGameMode != null) {
            HighScoreEntry topEntry = highScoreManager.getHighestScore(currentGameMode);
            if (topEntry != null) {
                updateHighScoreDisplay(topEntry.getName(), topEntry.getScore());
            } else {
                updateHighScoreDisplay("-", 0);
            }
        } else {
            updateHighScoreDisplay("-", 0);
        }
    }
    
    public void setCurrentGameMode(GameMode gameMode) {
        this.currentGameMode = gameMode;
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

    /**
     * Displays a countdown animation (3, 2, 1, GO!) before starting the game.
     * @param soundController The sound controller for countdown audio
     * @param onComplete Callback to execute when countdown finishes
     */
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
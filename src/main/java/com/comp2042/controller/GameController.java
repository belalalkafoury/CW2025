package com.comp2042.controller;

import com.comp2042.logic.score.ScoreService;
import com.comp2042.model.Board;
import com.comp2042.model.GameMode;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.DownData;
import com.comp2042.view.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;

/**
 * Orchestrates the game loop and coordinates between the board, view, and game logic.
 * Handles input events, manages game state (countdown, clearing, game over), and implements
 * special game mode logic (Time Attack, Puzzle mode win conditions).
 */
public class GameController implements InputEventListener {

    private final Board board;

    private final GuiController viewGuiController;

    private final AnimationController animationController;

    private final InputHandler inputHandler;

    private final ScoreService scoreService;

    private final SoundController soundController;

    private final GameMode gameMode;

    private boolean isClearing = false;

    private boolean isCountdown = true;

    private int timeAttackTargetScore;
    private int timeAttackSecondsRemaining;
    private Timeline gameTimer;
    private boolean isTimeAttackActive = false;
    private boolean gameWon = false;
    private boolean gameLost = false;
    private String playerName = "GUEST";

    /**
     * Constructs a GameController with default player name "GUEST".
     * @param c The GUI controller for view updates
     * @param board The game board instance
     * @param soundController The sound controller for audio feedback
     * @param gameMode The game mode to play
     */
    public GameController(GuiController c, Board board, SoundController soundController, GameMode gameMode) {
        this(c, board, soundController, gameMode, "GUEST");
    }

    /**
     * Constructs a GameController with specified player name.
     * Initializes the game, sets up event listeners, and configures game mode-specific features.
     * @param c The GUI controller for view updates
     * @param board The game board instance
     * @param soundController The sound controller for audio feedback
     * @param gameMode The game mode to play
     * @param playerName The name of the player
     */
    public GameController(GuiController c, Board board, SoundController soundController, GameMode gameMode, String playerName) {
        this.viewGuiController = c;
        this.board = board;
        this.soundController = soundController;
        this.gameMode = gameMode;
        this.playerName = playerName != null && !playerName.trim().isEmpty() ? playerName.trim().toUpperCase() : "GUEST";

        if (soundController != null) {
            soundController.stopTitleMusic();
        }

        board.newGame();
        viewGuiController.setEventListener(this);
        this.inputHandler = new InputHandler(this, this.gameMode);
        viewGuiController.setInputHandler(this.inputHandler);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.setBoard(board);
        viewGuiController.bindScore(board.getScore().scoreProperty());
        this.animationController = new AnimationController(viewGuiController);
        viewGuiController.setAnimationController(this.animationController);
        this.scoreService = new ScoreService(board.getScore());
        
        if (gameMode == GameMode.TIME_ATTACK) {
            initializeTimeAttackMode();
        } else if (gameMode == GameMode.PUZZLE) {
            board.setupPuzzleMode();
            viewGuiController.configurePuzzleMode(40);
            viewGuiController.bindLines(board.getScore().linesProperty(), soundController);
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else {
            viewGuiController.bindLines(board.getScore().linesProperty(), soundController);
        }

        board.getScore().scoreProperty().addListener((obs, oldVal, newVal) -> {
            if (isTimeAttackActive && !gameWon && !gameLost) {
                checkWinCondition(newVal.intValue());
            }
        });

        board.getScore().linesProperty().addListener((obs, oldVal, newVal) -> {
            if (gameMode == GameMode.PUZZLE && !gameWon && !gameLost) {
                if (newVal.intValue() >= 40) {
                    checkPuzzleWinCondition();
                }
            }
        });

        viewGuiController.initializeHighScoreDisplay();
        
        if (soundController != null) {
            soundController.playCountdown();
        }
        viewGuiController.showCountdown(soundController, () -> {
            isCountdown = false;
            animationController.start();
            if (isTimeAttackActive && gameTimer != null) {
                gameTimer.play();
            }
        });
    }

    /**
     * Handles downward movement events (soft drop).
     * Applies scoring for user-initiated drops and triggers brick landing if movement fails.
     * @param event The move event containing source information
     * @return DownData containing clear row information and updated view data
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        if (isClearing || isCountdown) {
            return new DownData(null, board.getViewData());
        }
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            clearRow = handleBrickLanding();
        } else {
            updateScoreOnUserSoftDrop(event);
            if (soundController != null && event.getEventSource() == com.comp2042.controller.EventSource.USER) {
                soundController.playMove();
            }
            viewGuiController.refreshBrick(board.getViewData());
        }

        return new DownData(clearRow, board.getViewData());
    }



    /**
     * Handles leftward movement events.
     * @param event The move event containing source information
     * @return Updated view data after the move
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        if (isClearing || isCountdown) {
            return board.getViewData();
        }
        board.moveBrickLeft();
        if (soundController != null && event.getEventSource() == com.comp2042.controller.EventSource.USER) {
            soundController.playMove();
        }
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }


    /**
     * Handles rightward movement events.
     * @param event The move event containing source information
     * @return Updated view data after the move
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        if (isClearing || isCountdown) {
            return board.getViewData();
        }
        board.moveBrickRight();
        if (soundController != null && event.getEventSource() == com.comp2042.controller.EventSource.USER) {
            soundController.playMove();
        }
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }


    /**
     * Handles rotation events.
     * @param event The move event containing source information
     * @return Updated view data after the rotation
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        if (isClearing || isCountdown) {
            return board.getViewData();
        }
        board.rotateLeftBrick();
        if (soundController != null && event.getEventSource() == com.comp2042.controller.EventSource.USER) {
            soundController.playRotate();
        }
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }

    /**
     * Handles hold brick events.
     * @return Updated view data after holding/swapping the brick
     */
    @Override
    public ViewData onHoldEvent() {
        if (isClearing || isCountdown) {
            return board.getViewData();
        }
        board.holdBrick();
        if (soundController != null) {
            soundController.playMove();
        }
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }

    /**
     * Handles hard drop events (instant drop to bottom).
     * Applies scoring based on drop distance and triggers brick landing.
     * @param event The move event containing source information
     * @return DownData containing clear row information and updated view data
     */
    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        if (isClearing || isCountdown) {
            return new DownData(null, board.getViewData());
        }
        
        int distance = board.hardDrop();
        
        if (distance > 0) {
            if (soundController != null) {
                soundController.playHardDrop();
            }
            scoreService.applyHardDrop(distance);
            ClearRow cleared = handleBrickLanding();
            return new DownData(cleared, board.getViewData());
        }
        return new DownData(null, board.getViewData());
    }

    /**
     * Resets the game and starts a new one.
     * Reinitializes game mode-specific features and shows countdown.
     */
    @Override
    public void createNewGame() {
        stopGameTimer();
        gameWon = false;
        gameLost = false;
        board.newGame();
        isClearing = false;
        isCountdown = true;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        
        if (gameMode == GameMode.TIME_ATTACK) {
            initializeTimeAttackMode();
        } else if (gameMode == GameMode.PUZZLE) {
            board.setupPuzzleMode();
            viewGuiController.configurePuzzleMode(40);
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        
        if (soundController != null) {
            soundController.playCountdown();
        }
        viewGuiController.showCountdown(soundController, () -> {
            isCountdown = false;
            animationController.start();
            if (isTimeAttackActive && gameTimer != null) {
                gameTimer.play();
            }
        });
    }
    /**
     * Handles the logic when a brick lands and merges with the board.
     * Checks for line clears, applies combo system, triggers animations, and spawns new brick.
     * @return ClearRow object containing information about cleared rows
     */
    private ClearRow handleBrickLanding() {
        ViewData lastBrick = board.getViewData();
        viewGuiController.hideGhostPiece();
        
        board.mergeBrickToBackground();
        
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        
        viewGuiController.hideFallingBrick();
        
        ClearRow result = board.checkClears();
        
        if (result.getLinesRemoved() > 0) {
            isClearing = true;
            board.getScore().incrementCombo();
            int comboValue = board.getScore().getCombo();
            
            if (soundController != null) {
                soundController.playComboSound(comboValue);
            }
            
            if (comboValue >= 2) {
                viewGuiController.showComboAnimation(comboValue);
            }
            
            viewGuiController.animateClear(result.getClearedIndices(), () -> {
                board.commitClear(result);
                int linesRemoved = result.getLinesRemoved();
                int scoreBonus = 50 * linesRemoved * linesRemoved;
                scoreService.applyLineClearBonus(result);
                viewGuiController.showScoreAnimation("+" + scoreBonus);
                
                viewGuiController.refreshGameBackground(board.getBoardMatrix());
                
                if (!board.createNewBrick()) {
                    stopGameTimer();
                    animationController.stop();
                    viewGuiController.gameOver(soundController);
                    isClearing = false;
                } else {
                    viewGuiController.showFallingBrick();
                    isClearing = false;
                }
            });
        } else {
            board.getScore().resetCombo();
            scoreService.applyLineClearBonus(result);
            if (!board.createNewBrick()) {
                stopGameTimer();
                animationController.stop();
                viewGuiController.gameOver(soundController);
            } else {
                viewGuiController.showFallingBrick();
            }
            viewGuiController.animatePlacedBlocks(lastBrick);
        }
        
        return result;
    }

    /**
     * Updates score for user-initiated soft drops.
     * @param event The move event to process
     */
    private void updateScoreOnUserSoftDrop(MoveEvent event) {
        scoreService.applySoftDrop(event);
    }

    /**
     * Initializes Time Attack mode with random time limit and target score.
     * Sets up a timer that counts down and checks win/loss conditions.
     */
    private void initializeTimeAttackMode() {
        isTimeAttackActive = true;
        Random random = new Random();
        
        timeAttackSecondsRemaining = 30 + random.nextInt(91);
        
        int pointsPerSecond = 30 + random.nextInt(21);
        
        timeAttackTargetScore = timeAttackSecondsRemaining * pointsPerSecond;
        timeAttackTargetScore = ((timeAttackTargetScore + 50) / 100) * 100;
        
        if (timeAttackTargetScore == 0) {
            timeAttackTargetScore = 100;
        }
        
        viewGuiController.configureTimeAttackMode(timeAttackSecondsRemaining, timeAttackTargetScore);
        
        gameTimer = new Timeline(new KeyFrame(
            Duration.seconds(1),
            e -> {
                timeAttackSecondsRemaining--;
                viewGuiController.updateTimer(timeAttackSecondsRemaining);
                
                if (timeAttackSecondsRemaining <= 0) {
                    checkLossCondition();
                }
            }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Checks if the win condition for Time Attack mode has been met.
     * @param currentScore The current score to check against the target
     */
    private void checkWinCondition(int currentScore) {
        if (currentScore >= timeAttackTargetScore && !gameWon && !gameLost) {
            gameWon = true;
            stopGameTimer();
            animationController.stop();
            
            if (soundController != null) {
                soundController.playLevelUp();
            }
            
            viewGuiController.gameWin(soundController);
        }
    }

    /**
     * Checks if the loss condition for Time Attack mode has been met (time expired).
     */
    private void checkLossCondition() {
        if (timeAttackSecondsRemaining <= 0 && !gameWon && !gameLost) {
            gameLost = true;
            stopGameTimer();
            animationController.stop();
            viewGuiController.gameOver(soundController);
        }
    }

    /**
     * Checks if the win condition for Puzzle mode has been met (40 lines cleared).
     */
    private void checkPuzzleWinCondition() {
        if (board.getScore().getLinesValue() >= 40 && !gameWon && !gameLost) {
            gameWon = true;
            animationController.stop();
            
            if (soundController != null) {
                soundController.playLevelUp();
            }
            
            viewGuiController.gameWin(soundController);
        }
    }

    /**
     * Stops the game timer if it exists.
     */
    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    /**
     * Pauses the Time Attack mode timer.
     */
    public void pauseTimer() {
        if (gameTimer != null && isTimeAttackActive) {
            gameTimer.pause();
        }
    }

    /**
     * Resumes the Time Attack mode timer.
     */
    public void resumeTimer() {
        if (gameTimer != null && isTimeAttackActive) {
            gameTimer.play();
        }
    }

    /**
     * Gets the current game mode.
     * @return The active GameMode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Gets the player's name.
     * @return The player name string
     */
    public String getPlayerName() {
        return playerName;
    }

}
package com.comp2042.controller;

import com.comp2042.logic.score.ScoreService;
import com.comp2042.model.Board;
import com.comp2042.model.GameMode;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.DownData;
import com.comp2042.logic.mode.GameModeStrategy;
import com.comp2042.logic.mode.GameModeStrategyFactory;
import com.comp2042.logic.mode.TimeAttackModeStrategy;
import com.comp2042.view.ViewData;

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
    private final GameModeStrategy modeStrategy;

    private boolean isClearing = false;

    private boolean isCountdown = true;

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
        this.modeStrategy = GameModeStrategyFactory.createStrategy(gameMode);
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
        
        modeStrategy.initialize(board, viewGuiController, soundController);

        board.getScore().scoreProperty().addListener((obs, oldVal, newVal) -> {
            if (!gameWon && !gameLost && modeStrategy.checkWinCondition(board, viewGuiController, soundController)) {
                gameWon = true;
                modeStrategy.stop();
                animationController.stop();
                
                if (soundController != null) {
                    soundController.playLevelUp();
                }
                
                viewGuiController.gameWin(soundController);
            }
            
            if (!gameWon && !gameLost && modeStrategy.checkLossCondition(board, viewGuiController, soundController)) {
                gameLost = true;
                modeStrategy.stop();
                animationController.stop();
                viewGuiController.gameOver(soundController);
            }
        });

        board.getScore().linesProperty().addListener((obs, oldVal, newVal) -> {
            if (!gameWon && !gameLost && modeStrategy.checkWinCondition(board, viewGuiController, soundController)) {
                gameWon = true;
                modeStrategy.stop();
                animationController.stop();
                
                if (soundController != null) {
                    soundController.playLevelUp();
                }
                
                viewGuiController.gameWin(soundController);
            }
        });

        viewGuiController.initializeHighScoreDisplay();
        
        if (soundController != null) {
            soundController.playCountdown();
        }
        viewGuiController.showCountdown(soundController, () -> {
            isCountdown = false;
            animationController.start();
            if (modeStrategy instanceof TimeAttackModeStrategy) {
                TimeAttackModeStrategy timeAttackStrategy = (TimeAttackModeStrategy) modeStrategy;
                timeAttackStrategy.startTimer();
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
        modeStrategy.stop();
        gameWon = false;
        gameLost = false;
        board.newGame();
        isClearing = false;
        isCountdown = true;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        
        modeStrategy.reset(board, viewGuiController);
        
        if (soundController != null) {
            soundController.playCountdown();
        }
        viewGuiController.showCountdown(soundController, () -> {
            isCountdown = false;
            animationController.start();
            if (modeStrategy instanceof TimeAttackModeStrategy) {
                TimeAttackModeStrategy timeAttackStrategy = (TimeAttackModeStrategy) modeStrategy;
                if (timeAttackStrategy.shouldStartTimer()) {
                    timeAttackStrategy.resume();
                }
            }
        });
    }
    /**
     * Handles the logic when a brick lands and merges with the board.
     * Checks for line clears, applies combo system, triggers animations, and spawns new brick.
     * @return ClearRow object containing information about cleared rows
     */
    private ClearRow handleBrickLanding() {
        ViewData lastBrick = mergeBrickToBoard();
        ClearRow result = board.checkClears();
        
        if (result.getLinesRemoved() > 0) {
            handleLineClears(result);
        } else {
            handleNoClears(result, lastBrick);
        }
        
        return result;
    }
    
    /**
     * Merges the current brick to the board and updates the view.
     * @return The view data of the brick that was merged
     */
    private ViewData mergeBrickToBoard() {
        ViewData lastBrick = board.getViewData();
        viewGuiController.hideGhostPiece();
        board.mergeBrickToBackground();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.hideFallingBrick();
        return lastBrick;
    }
    
    /**
     * Handles the logic when lines are cleared.
     * @param result The ClearRow containing information about cleared lines
     */
    private void handleLineClears(ClearRow result) {
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
            spawnNewBrickOrGameOver();
            isClearing = false;
        });
    }
    
    /**
     * Handles the logic when no lines are cleared.
     * @param result The ClearRow result (with 0 lines removed)
     * @param lastBrick The view data of the brick that was placed
     */
    private void handleNoClears(ClearRow result, ViewData lastBrick) {
        board.getScore().resetCombo();
        scoreService.applyLineClearBonus(result);
        viewGuiController.animatePlacedBlocks(lastBrick);
        spawnNewBrickOrGameOver();
    }
    
    /**
     * Attempts to spawn a new brick or triggers game over if spawning fails.
     */
    private void spawnNewBrickOrGameOver() {
        if (!board.createNewBrick()) {
            modeStrategy.stop();
            animationController.stop();
            viewGuiController.gameOver(soundController);
        } else {
            viewGuiController.showFallingBrick();
        }
    }

    /**
     * Updates score for user-initiated soft drops.
     * @param event The move event to process
     */
    private void updateScoreOnUserSoftDrop(MoveEvent event) {
        scoreService.applySoftDrop(event);
    }

    /**
     * Pauses the game mode timer.
     */
    public void pauseTimer() {
        modeStrategy.pause();
    }

    /**
     * Resumes the game mode timer.
     */
    public void resumeTimer() {
        modeStrategy.resume();
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
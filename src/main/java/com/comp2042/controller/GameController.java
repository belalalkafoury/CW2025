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

    public GameController(GuiController c, Board board, SoundController soundController, GameMode gameMode) {
        this.viewGuiController = c;
        this.board = board;
        this.soundController = soundController;
        this.gameMode = gameMode;

        if (soundController != null) {
            soundController.stopTitleMusic();
        }

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        this.inputHandler = new InputHandler(this);
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
    private ClearRow handleBrickLanding() {
        ViewData lastBrick = board.getViewData();
        viewGuiController.hideGhostPiece();
        
        board.mergeBrickToBackground();
        
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        
        viewGuiController.hideFallingBrick();
        
        ClearRow result = board.checkClears();
        
        if (result.getLinesRemoved() > 0) {
            isClearing = true;
        if (soundController != null) {
            soundController.playLineClear();
        }
            viewGuiController.animateClear(result.getClearedIndices(), () -> {
                board.commitClear(result);
                scoreService.applyLineClearBonus(result);
                
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

    private void updateScoreOnUserSoftDrop(MoveEvent event) {
        scoreService.applySoftDrop(event);
    }

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

    private void checkLossCondition() {
        if (timeAttackSecondsRemaining <= 0 && !gameWon && !gameLost) {
            gameLost = true;
            stopGameTimer();
            animationController.stop();
            viewGuiController.gameOver(soundController);
        }
    }

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

    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    public void pauseTimer() {
        if (gameTimer != null && isTimeAttackActive) {
            gameTimer.pause();
        }
    }

    public void resumeTimer() {
        if (gameTimer != null && isTimeAttackActive) {
            gameTimer.play();
        }
    }

}
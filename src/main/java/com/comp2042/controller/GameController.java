package com.comp2042.controller;

import com.comp2042.logic.score.ScoreService;
import com.comp2042.model.Board;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.DownData;
import com.comp2042.view.ViewData;

public class GameController implements InputEventListener {

    private final Board board;

    private final GuiController viewGuiController;

    private final AnimationController animationController;

    private final InputHandler inputHandler;

    private final ScoreService scoreService;

    private final SoundController soundController;

    private boolean isClearing = false;

    private boolean isCountdown = true;

    public GameController(GuiController c, Board board, SoundController soundController) {
        this.viewGuiController = c;
        this.board = board;
        this.soundController = soundController;

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
        viewGuiController.bindLines(board.getScore().linesProperty(), soundController);

        if (soundController != null) {
            soundController.playCountdown();
        }
        viewGuiController.showCountdown(soundController, () -> {
            isCountdown = false;
            animationController.start();
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
        board.newGame();
        isClearing = false;
        isCountdown = true;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        if (soundController != null) {
            soundController.playCountdown();
        }
        viewGuiController.showCountdown(soundController, () -> {
            isCountdown = false;
            animationController.start();
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


}
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



    public GameController(GuiController c, Board board) {
        this.viewGuiController = c;
        this.board = board; // Now it's initialized

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        this.inputHandler = new InputHandler(this);
        viewGuiController.setInputHandler(this.inputHandler);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.setBoard(board);
        viewGuiController.bindScore(board.getScore().scoreProperty());
        this.animationController = new AnimationController(viewGuiController);
        viewGuiController.setAnimationController(this.animationController);
        this.animationController.start();
        this.scoreService = new ScoreService(board.getScore());
        viewGuiController.bindLines(board.getScore().linesProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            clearRow = handleBrickLanding();
        } else {
            updateScoreOnUserSoftDrop(event);
            viewGuiController.refreshBrick(board.getViewData());
        }

        return new DownData(clearRow, board.getViewData());
    }



    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }


    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }


    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        viewGuiController.refreshBrick(board.getViewData());
        return board.getViewData();
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        int distance = board.hardDrop();
        if (distance > 0) {
            scoreService.applyHardDrop(distance);
            viewGuiController.refreshBrick(board.getViewData());
            ClearRow cleared = handleBrickLanding();
            return new DownData(cleared, board.getViewData());
        }
        return new DownData(null, board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        animationController.start();
    }
    private ClearRow handleBrickLanding() {
        ViewData lastBrick = board.getViewData();
        viewGuiController.refreshBrick(lastBrick);
        viewGuiController.hideGhostPiece();
        board.mergeBrickToBackground();
        ClearRow cleared = board.clearRows();
        scoreService.applyLineClearBonus(cleared);
        if (!board.createNewBrick()) {
            animationController.stop();
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.animatePlacedBlocks(lastBrick);
        return cleared;
    }

    private void updateScoreOnUserSoftDrop(MoveEvent event) {
        scoreService.applySoftDrop(event);
    }


}

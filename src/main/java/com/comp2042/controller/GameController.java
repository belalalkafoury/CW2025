package com.comp2042.controller;

import com.comp2042.model.Board;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.DownData;
import com.comp2042.view.ViewData;

public class GameController implements InputEventListener {

    private final Board board;

    private final GuiController viewGuiController;

    public GameController(GuiController c, Board board) {
        this.viewGuiController = c;
        this.board = board; // Now it's initialized

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            clearRow = handleBrickLanding();
        } else {
            updateScoreOnUserSoftDrop(event);
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
    private ClearRow handleBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow cleared = board.clearRows();
        if (cleared.getLinesRemoved() > 0) {
            board.getScore().add(cleared.getScoreBonus());
        }
        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return cleared;
    }

    private void updateScoreOnUserSoftDrop(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            board.getScore().add(1);
        }
    }

}

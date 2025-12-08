package com.comp2042.logic.mode;

import com.comp2042.controller.GuiController;
import com.comp2042.controller.SoundController;
import com.comp2042.model.Board;
import com.comp2042.util.GameConstants;

/**
 * Strategy for Puzzle mode.
 * Player starts with garbage rows and must clear a target number of lines.
 */
public class PuzzleModeStrategy implements GameModeStrategy {
    
    private static final int TARGET_LINES = GameConstants.PUZZLE_MODE_TARGET_LINES;
    private boolean gameWon = false;
    private boolean gameLost = false;
    
    @Override
    public void initialize(Board board, GuiController guiController, SoundController soundController) {
        board.setupPuzzleMode();
        guiController.configurePuzzleMode(TARGET_LINES);
        guiController.bindLines(board.getScore().linesProperty(), soundController);
        guiController.refreshGameBackground(board.getBoardMatrix());
    }
    
    @Override
    public boolean checkWinCondition(Board board, GuiController guiController, SoundController soundController) {
        if (board.getScore().getLinesValue() >= TARGET_LINES && !gameWon && !gameLost) {
            gameWon = true;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkLossCondition(Board board, GuiController guiController, SoundController soundController) {
        return false;
    }
    
    @Override
    public void reset(Board board, GuiController guiController) {
        gameWon = false;
        gameLost = false;
        board.setupPuzzleMode();
        guiController.configurePuzzleMode(TARGET_LINES);
        guiController.refreshGameBackground(board.getBoardMatrix());
    }
    
    @Override
    public void pause() {
    }
    
    @Override
    public void resume() {
    }
    
    @Override
    public void stop() {
    }
}


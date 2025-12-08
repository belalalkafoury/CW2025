package com.comp2042.logic.mode;

import com.comp2042.controller.GuiController;
import com.comp2042.controller.SoundController;
import com.comp2042.model.Board;

/**
 * Strategy for Reverted mode.
 * Standard gameplay with inverted controls (handled by InputHandler).
 */
public class RevertedModeStrategy implements GameModeStrategy {
    
    @Override
    public void initialize(Board board, GuiController guiController, SoundController soundController) {
        guiController.bindLines(board.getScore().linesProperty(), soundController);
    }
    
    @Override
    public boolean checkWinCondition(Board board, GuiController guiController, SoundController soundController) {
        return false;
    }
    
    @Override
    public boolean checkLossCondition(Board board, GuiController guiController, SoundController soundController) {
        return false;
    }
    
    @Override
    public void reset(Board board, GuiController guiController) {
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


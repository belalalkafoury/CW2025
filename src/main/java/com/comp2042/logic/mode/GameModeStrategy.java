package com.comp2042.logic.mode;

import com.comp2042.controller.GuiController;
import com.comp2042.controller.SoundController;
import com.comp2042.model.Board;

/**
 * Strategy interface for different game modes.
 * Each game mode implements its own initialization, win/loss conditions, and UI configuration.
 */
public interface GameModeStrategy {
    
    /**
     * Initializes the game mode with mode-specific setup.
     * @param board The game board instance
     * @param guiController The GUI controller for view updates
     * @param soundController The sound controller for audio feedback
     */
    void initialize(Board board, GuiController guiController, SoundController soundController);
    
    /**
     * Checks if the win condition for this game mode has been met.
     * @param board The game board instance
     * @param guiController The GUI controller for view updates
     * @param soundController The sound controller for audio feedback
     * @return true if the win condition is met, false otherwise
     */
    boolean checkWinCondition(Board board, GuiController guiController, SoundController soundController);
    
    /**
     * Checks if the loss condition for this game mode has been met.
     * @param board The game board instance
     * @param guiController The GUI controller for view updates
     * @param soundController The sound controller for audio feedback
     * @return true if the loss condition is met, false otherwise
     */
    boolean checkLossCondition(Board board, GuiController guiController, SoundController soundController);
    
    /**
     * Resets the game mode for a new game.
     * @param board The game board instance
     * @param guiController The GUI controller for view updates
     */
    void reset(Board board, GuiController guiController);
    
    /**
     * Pauses mode-specific timers or processes.
     */
    void pause();
    
    /**
     * Resumes mode-specific timers or processes.
     */
    void resume();
    
    /**
     * Stops mode-specific timers or processes.
     */
    void stop();
}


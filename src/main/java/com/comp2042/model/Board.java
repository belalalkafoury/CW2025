package com.comp2042.model;

import com.comp2042.logic.board.ClearRow;
import com.comp2042.view.ViewData;

/**
 * Interface contract defining the core game board operations for Tetris.
 * Provides methods for brick movement, rotation, game state management, and special game modes.
 */
public interface Board {

    /**
     * Moves the current brick down by one position.
     * @return true if the move was successful, false if collision occurred
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick left by one position.
     * @return true if the move was successful, false if collision occurred
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick right by one position.
     * @return true if the move was successful, false if collision occurred
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counter-clockwise with wall kick support.
     * @return true if rotation was successful, false if rotation would cause collision
     */
    boolean rotateLeftBrick();

    /**
     * Creates and spawns a new brick at the starting position.
     * @return true if the brick can be placed, false if game over condition is met
     */
    boolean createNewBrick();

    /**
     * Gets a copy of the current game board matrix.
     * @return 2D array representing the board state where 0 is empty and non-zero values represent brick colors
     */
    int[][] getBoardMatrix();

    /**
     * Gets view data containing current brick, next bricks, and held brick information.
     * @return ViewData object containing all information needed for rendering
     */
    ViewData getViewData();

    /**
     * Merges the current falling brick into the background board matrix.
     */
    void mergeBrickToBackground();

    /**
     * Checks for completed rows that should be cleared.
     * @return ClearRow object containing information about cleared rows and the new matrix state
     */
    ClearRow checkClears();

    /**
     * Applies the cleared rows and updates the board matrix.
     * @param clearRow ClearRow object containing the new matrix state after clearing
     */
    void commitClear(ClearRow clearRow);

    /**
     * Gets the Score object tracking game statistics.
     * @return Score object containing score, lines cleared, and combo information
     */
    Score getScore();

    /**
     * Resets the game board to start a new game.
     */
    void newGame();
    
    /**
     * Calculates the Y position where the ghost piece (preview of landing position) should be displayed.
     * @param currentX Current X position of the brick
     * @param currentY Current Y position of the brick
     * @return Y coordinate where the brick would land if dropped
     */
    int getGhostY(int currentX, int currentY);
    
    /**
     * Gets a copy of the current falling brick's shape matrix.
     * @return 2D array representing the current brick's shape
     */
    int[][] getCurrentShape();
    
    /**
     * Instantly drops the current brick to its lowest valid position.
     * @return The number of rows the brick was dropped
     */
    int hardDrop();
    
    /**
     * Gets a list of row indices that are fully filled and ready to be cleared.
     * @return List of row indices (0-based) that should be cleared
     */
    java.util.List<Integer> getRowsToClear();
    
    /**
     * Sets up the board for Puzzle mode by generating garbage rows at the bottom.
     */
    void setupPuzzleMode();
    
    /**
     * Swaps the current brick with the held brick, or holds the current brick if none is held.
     */
    void holdBrick();
}

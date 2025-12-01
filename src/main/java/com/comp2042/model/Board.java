package com.comp2042.model;

import com.comp2042.logic.board.ClearRow;
import com.comp2042.view.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow checkClears();

    void commitClear(ClearRow clearRow);

    Score getScore();

    void newGame();
    
    int getGhostY(int currentX, int currentY);
    
    int[][] getCurrentShape();
    
    int hardDrop();
    
    java.util.List<Integer> getRowsToClear();
}

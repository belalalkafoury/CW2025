package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private GameBoard board;

    @BeforeEach
    void setUp() {
        board = new GameBoard(10, 20);
    }

    @Test
    void testNewGameInitializesEmptyBoard() {
        board.newGame();
        int[][] matrix = board.getBoardMatrix();
        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell, "Board should be empty at the start of a new game");
            }
        }
    }

    @Test
    void testMoveBrickDownReturnsTrueWhenPossible() {
        board.createNewBrick();
        boolean moved = board.moveBrickDown();
        assertTrue(moved, "Brick should be able to move down initially");
    }

    @Test
    void testMoveBrickLeftAndRight() {
        board.createNewBrick();
        assertTrue(board.moveBrickLeft(), "Should move left when possible");
        assertTrue(board.moveBrickRight(), "Should move right when possible");
    }

    @Test
    void testRotateBrickReturnsTrue() {
        board.createNewBrick();
        boolean rotated = board.rotateLeftBrick();
        assertTrue(rotated, "Rotation should be allowed for most starting positions");
    }

    @Test
    void testScoreResetsAfterNewGame() {
        board.getScore().add(50);
        board.newGame();
        assertEquals(0, board.getScore().getValue(), "Score should reset to 0 after newGame()");
    }
}

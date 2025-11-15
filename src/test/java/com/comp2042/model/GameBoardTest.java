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
    @Test
    void testMergeBrickToBackground() {
        board.createNewBrick();
        board.mergeBrickToBackground();

        int[][] matrix = board.getBoardMatrix();
        boolean foundFilledCell = false;

        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) {
                    foundFilledCell = true;
                    break;
                }
            }
        }

        assertTrue(foundFilledCell, "Brick should merge and fill some background cells");
    }
    @Test
    void testClearFullRow() {
        board.newGame();
        int[][] matrix = board.getBoardMatrix();

        int height = matrix.length;
        int width = matrix[0].length;
        int bottomRow = height - 1;

        for (int col = 0; col < width; col++) {
            matrix[bottomRow][col] = 1;
        }

        var result = board.clearRows();
        assertEquals(1, result.getLinesRemoved(),
                "Exactly one full row should be cleared");
    }

    @Test
    void testSpawnBrickReturnsTrueWhenSpaceAvailable() {
        boolean created = board.createNewBrick();
        assertTrue(created, "A new brick should spawn if the board is not blocked");
    }
    @Test
    void testCollisionBlocksInvalidPlacement() {
        board.createNewBrick();


        boolean canMove = true;
        while (canMove) {
            canMove = board.moveBrickLeft();
        }


        assertFalse(board.moveBrickLeft(),
                "Brick should not move outside the left boundary");
    }
    @Test
    void testSpawnBrickFailsWhenBoardIsBlocked() {
        int[][] matrix = board.getBoardMatrix();

        int startX = 4;
        int startY = 0;


        for (int row = startY; row < startY + 4; row++) {
            for (int col = startX; col < startX + 4; col++) {
                matrix[row][col] = 1;
            }
        }

        boolean created = board.createNewBrick();
        assertFalse(created, "Brick spawn should fail when spawn area is blocked");
    }







}

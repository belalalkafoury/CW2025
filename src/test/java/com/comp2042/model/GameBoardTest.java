package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

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

        var result = board.checkClears();
        board.commitClear(result);
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

    private String getBrickTypeName(Brick brick) {
        if (brick == null) return "UNKNOWN";
        String className = brick.getClass().getSimpleName();
        switch (className) {
            case "IBrick": return "I";
            case "JBrick": return "J";
            case "LBrick": return "L";
            case "OBrick": return "O";
            case "SBrick": return "S";
            case "TBrick": return "T";
            case "ZBrick": return "Z";
            default: return "UNKNOWN";
        }
    }

    @Test
    void testSevenBagRandomizer() {
        board.newGame();
        
        Set<String> typesSeen = new HashSet<>();
        
        Brick firstBrick = board.getCurrentBrickForTest();
        typesSeen.add(getBrickTypeName(firstBrick));
        
        for (int i = 0; i < 6; i++) {
            board.createNewBrick();
            Brick currentBrick = board.getCurrentBrickForTest();
            String type = getBrickTypeName(currentBrick);
            typesSeen.add(type);
        }
        
        assertEquals(7, typesSeen.size(), "All 7 brick types should appear in the first 7 bricks");
        assertTrue(typesSeen.contains("I"), "Should contain I brick");
        assertTrue(typesSeen.contains("J"), "Should contain J brick");
        assertTrue(typesSeen.contains("L"), "Should contain L brick");
        assertTrue(typesSeen.contains("O"), "Should contain O brick");
        assertTrue(typesSeen.contains("S"), "Should contain S brick");
        assertTrue(typesSeen.contains("T"), "Should contain T brick");
        assertTrue(typesSeen.contains("Z"), "Should contain Z brick");
        
        board.createNewBrick();
        Brick eighthBrick = board.getCurrentBrickForTest();
        assertNotNull(eighthBrick, "8th brick should exist (starts new sequence)");
    }

    @Test
    void testWallKickFromRightWall() {
        board.newGame();
        board.createNewBrick();
        
        while (board.moveBrickRight()) {
            int currentX = board.getCurrentOffsetForTest().x;
            if (currentX >= 8) {
                break;
            }
        }
        
        int xBeforeRotation = board.getCurrentOffsetForTest().x;
        boolean rotated = board.rotateLeftBrick();
        int xAfterRotation = board.getCurrentOffsetForTest().x;
        
        assertTrue(rotated, "Rotation should succeed with wall kick");
        if (xBeforeRotation >= 9) {
            assertTrue(xAfterRotation <= xBeforeRotation, 
                "Wall kick should move piece left if too far right");
        }
    }

    @Test
    void testWallKickFailsWhenBlocked() {
        board.newGame();
        board.createNewBrick();
        
        int[][] matrix = board.getBoardMatrix();
        
        int startX = 4;
        int startY = 0;
        
        for (int row = startY; row < startY + 6; row++) {
            for (int col = startX - 1; col < startX + 5; col++) {
                if (col >= 0 && col < matrix[0].length && row < matrix.length) {
                    matrix[row][col] = 1;
                }
            }
        }
        
        for (int col = startX - 2; col < startX + 6; col++) {
            if (col >= 0 && col < matrix[0].length) {
                matrix[startY][col] = 1;
            }
        }
        
        boolean rotated = board.rotateLeftBrick();
        assertFalse(rotated, "Rotation should fail when all wall kick positions are blocked");
    }

    @Test
    void testHoldSwapsPiece() {
        board.newGame();
        board.createNewBrick();
        
        Brick originalBrick = board.getCurrentBrickForTest();
        assertNull(board.getHeldBrickForTest(), "Held brick should be null initially");
        
        board.holdBrick();
        
        Brick newCurrentBrick = board.getCurrentBrickForTest();
        Brick heldBrick = board.getHeldBrickForTest();
        
        assertNotNull(heldBrick, "Held brick should not be null after first hold");
        assertEquals(originalBrick.getClass(), heldBrick.getClass(), 
            "Held brick should match original brick type");
        assertNotSame(originalBrick, newCurrentBrick, 
            "Current brick should change after hold");
    }

    @Test
    void testCannotHoldTwicePerTurn() {
        board.newGame();
        board.createNewBrick();
        
        board.holdBrick();
        
        Brick secondBrick = board.getCurrentBrickForTest();
        
        assertFalse(board.canHoldForTest(), "canHold should be false after first hold");
        
        board.holdBrick();
        
        Brick thirdBrick = board.getCurrentBrickForTest();
        assertEquals(secondBrick.getClass(), thirdBrick.getClass(), 
            "Piece should not change on second hold (action ignored)");
        
        while (board.moveBrickDown()) {
        }
        
        board.mergeBrickToBackground();
        board.createNewBrick();
        
        assertTrue(board.canHoldForTest(), "canHold should reset after new piece spawns");
        
        Brick fourthBrick = board.getCurrentBrickForTest();
        board.holdBrick();
        Brick fifthBrick = board.getCurrentBrickForTest();
        
        assertNotSame(fourthBrick, fifthBrick, 
            "Hold should work again after new piece spawns");
    }

    @Test
    void testPuzzleModeInitialization() {
        board.newGame();
        board.setupPuzzleMode();
        
        int[][] matrix = board.getBoardMatrix();
        int height = matrix.length;
        int width = matrix[0].length;
        
        boolean bottomRowsHaveBlocks = false;
        for (int row = height - 10; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (matrix[row][col] != 0) {
                    bottomRowsHaveBlocks = true;
                    break;
                }
            }
            if (bottomRowsHaveBlocks) break;
        }
        
        assertTrue(bottomRowsHaveBlocks, 
            "Bottom 10 rows should contain garbage blocks (non-zero values)");
        
        boolean topRowsEmpty = true;
        for (int row = 0; row < height - 10; row++) {
            for (int col = 0; col < width; col++) {
                if (matrix[row][col] != 0) {
                    topRowsEmpty = false;
                    break;
                }
            }
            if (!topRowsEmpty) break;
        }
        
        assertTrue(topRowsEmpty, "Top rows should be empty (above garbage)");
    }

}

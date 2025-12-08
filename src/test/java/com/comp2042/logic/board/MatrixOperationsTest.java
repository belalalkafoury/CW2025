package com.comp2042.logic.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class MatrixOperationsTest {

    @Test
    void testIntersectWithEmptyBoard() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1, 1},
            {1, 1}
        };
        
        assertFalse(MatrixOperations.intersect(board, shape, 0, 0), 
            "Should not intersect with empty board");
    }

    @Test
    void testIntersectWithFilledCell() {
        int[][] board = new int[10][20];
        board[5][5] = 1;
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(MatrixOperations.intersect(board, shape, 5, 5), 
            "Should intersect with filled cell");
    }

    @Test
    void testIntersectOutOfBounds() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(MatrixOperations.intersect(board, shape, -1, 5), 
            "Should intersect when out of bounds");
        assertTrue(MatrixOperations.intersect(board, shape, 20, 5), 
            "Should intersect when out of bounds");
        assertTrue(MatrixOperations.intersect(board, shape, 5, -1), 
            "Should intersect when out of bounds");
        assertTrue(MatrixOperations.intersect(board, shape, 5, 10), 
            "Should intersect when out of bounds");
    }

    @Test
    void testCopy() {
        int[][] original = new int[][]{
            {1, 2, 3},
            {4, 5, 6}
        };
        
        int[][] copy = MatrixOperations.copy(original);
        
        assertNotSame(original, copy, "Copy should be a different object");
        assertArrayEquals(original, copy, "Copy should have same values");
        
        copy[0][0] = 99;
        assertEquals(1, original[0][0], "Modifying copy should not affect original");
    }

    @Test
    void testMerge() {
        int[][] board = new int[][]{
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] shape = new int[][]{
            {1, 1},
            {1, 1}
        };
        
        int[][] result = MatrixOperations.merge(board, shape, 0, 0);
        
        assertEquals(1, result[0][0], "Should merge shape into board");
        assertEquals(1, result[0][1], "Should merge shape into board");
        assertEquals(1, result[1][0], "Should merge shape into board");
        assertEquals(1, result[1][1], "Should merge shape into board");
        assertEquals(0, board[0][0], "Original board should not be modified");
    }

    @Test
    void testMergeIgnoresZeroCells() {
        int[][] board = new int[][]{
            {0, 0, 0},
            {0, 0, 0}
        };
        int[][] shape = new int[][]{
            {0, 1},
            {1, 0}
        };
        
        int[][] result = MatrixOperations.merge(board, shape, 0, 0);
        
        assertEquals(1, result[0][1], "Should merge non-zero cells");
        assertEquals(1, result[1][0], "Should merge non-zero cells");
        assertEquals(0, result[0][0], "Should ignore zero cells");
    }

    @Test
    void testCheckRemovingSingleFullRow() {
        int[][] matrix = new int[][]{
            {0, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        
        assertEquals(1, result.getLinesRemoved(), "Should detect one full row");
        assertTrue(result.getClearedIndices().contains(1), "Should include row index 1");
    }

    @Test
    void testCheckRemovingMultipleFullRows() {
        int[][] matrix = new int[][]{
            {1, 1, 1},
            {1, 1, 1},
            {0, 0, 0}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        
        assertEquals(2, result.getLinesRemoved(), "Should detect two full rows");
        assertEquals(2, result.getClearedIndices().size(), "Should have two cleared indices");
    }

    @Test
    void testCheckRemovingNoFullRows() {
        int[][] matrix = new int[][]{
            {0, 1, 0},
            {1, 0, 1},
            {0, 0, 0}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        
        assertEquals(0, result.getLinesRemoved(), "Should detect no full rows");
        assertTrue(result.getClearedIndices().isEmpty(), "Should have no cleared indices");
    }

    @Test
    void testCheckRemovingAllFullRows() {
        int[][] matrix = new int[][]{
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        
        assertEquals(3, result.getLinesRemoved(), "Should detect all three full rows");
    }

    @Test
    void testCheckRemovingGravityEffect() {
        int[][] matrix = new int[][]{
            {1, 1, 1},
            {3, 3, 3},
            {0, 0, 0},
            {2, 2, 2}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        
        assertEquals(3, result.getLinesRemoved(), "Should detect three full rows");
        int[][] newMatrix = result.getNewMatrix();
        assertEquals(0, newMatrix[3][0], "Only empty row should remain at bottom after clearing all full rows");
        assertEquals(0, newMatrix[0][0], "Top rows should be empty after gravity");
        assertEquals(0, newMatrix[1][0], "Top rows should be empty after gravity");
        assertEquals(0, newMatrix[2][0], "Top rows should be empty after gravity");
    }

    @Test
    void testCheckRemovingGravityWithNonClearedRows() {
        int[][] matrix = new int[][]{
            {1, 1, 1},
            {4, 0, 4},
            {5, 5, 5},
            {2, 2, 2}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(matrix);
        
        assertEquals(3, result.getLinesRemoved(), "Should detect three full rows (rows 0, 2, and 3)");
        int[][] newMatrix = result.getNewMatrix();
        assertEquals(4, newMatrix[3][0], "Non-cleared row should fall to bottom");
        assertEquals(0, newMatrix[0][0], "Top rows should be empty after gravity");
        assertEquals(0, newMatrix[1][0], "Top rows should be empty after gravity");
        assertEquals(0, newMatrix[2][0], "Top rows should be empty after gravity");
    }

    @Test
    void testDeepCopyList() {
        int[][] matrix1 = {{1, 2}, {3, 4}};
        int[][] matrix2 = {{5, 6}, {7, 8}};
        List<int[][]> original = List.of(matrix1, matrix2);
        
        List<int[][]> copy = MatrixOperations.deepCopyList(original);
        
        assertEquals(original.size(), copy.size(), "Copy should have same size");
        assertNotSame(original.get(0), copy.get(0), "Should be deep copy");
        
        copy.get(0)[0][0] = 99;
        assertEquals(1, original.get(0)[0][0], "Modifying copy should not affect original");
    }

    @Test
    void testGenerateGarbage() {
        int[][] garbage = MatrixOperations.generateGarbage(10, 10, 5);
        
        assertEquals(10, garbage.length, "Should have correct width");
        assertEquals(10, garbage[0].length, "Should have correct height");
        
        boolean hasNonZero = false;
        for (int i = 5; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (garbage[i][j] != 0) {
                    hasNonZero = true;
                }
            }
        }
        assertTrue(hasNonZero, "Garbage rows should have some filled cells");
    }

    @Test
    void testGenerateGarbageHasEmptySpace() {
        int[][] garbage = MatrixOperations.generateGarbage(10, 10, 3);
        
        for (int i = 7; i < 10; i++) {
            int emptyCount = 0;
            for (int j = 0; j < 10; j++) {
                if (garbage[i][j] == 0) {
                    emptyCount++;
                }
            }
            assertTrue(emptyCount >= 1, "Each garbage row should have at least one empty space");
        }
    }
}


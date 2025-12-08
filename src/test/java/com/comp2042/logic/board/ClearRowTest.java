package com.comp2042.logic.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClearRowTest {

    @Test
    void testConstructor() {
        int[][] matrix = new int[10][10];
        List<Integer> indices = new ArrayList<>();
        indices.add(5);
        
        ClearRow clearRow = new ClearRow(1, matrix, indices);
        
        assertEquals(1, clearRow.getLinesRemoved());
        assertNotNull(clearRow.getNewMatrix());
        assertEquals(1, clearRow.getClearedIndices().size());
    }

    @Test
    void testGetLinesRemoved() {
        int[][] matrix = new int[10][10];
        List<Integer> indices = Arrays.asList(3, 4, 5);
        
        ClearRow clearRow = new ClearRow(3, matrix, indices);
        
        assertEquals(3, clearRow.getLinesRemoved());
    }

    @Test
    void testGetNewMatrix() {
        int[][] original = new int[][]{
            {1, 2, 3},
            {4, 5, 6}
        };
        List<Integer> indices = new ArrayList<>();
        
        ClearRow clearRow = new ClearRow(0, original, indices);
        int[][] newMatrix = clearRow.getNewMatrix();
        
        assertNotSame(original, newMatrix, "Should return a copy, not the original");
        assertArrayEquals(original, newMatrix, "Copy should have same values");
    }

    @Test
    void testGetNewMatrixIsDeepCopy() {
        int[][] original = new int[][]{
            {1, 2},
            {3, 4}
        };
        List<Integer> indices = new ArrayList<>();
        
        ClearRow clearRow = new ClearRow(0, original, indices);
        int[][] newMatrix = clearRow.getNewMatrix();
        
        newMatrix[0][0] = 99;
        assertEquals(1, original[0][0], "Modifying copy should not affect original");
    }

    @Test
    void testGetClearedIndices() {
        List<Integer> indices = Arrays.asList(2, 5, 8);
        int[][] matrix = new int[10][10];
        
        ClearRow clearRow = new ClearRow(3, matrix, indices);
        List<Integer> returnedIndices = clearRow.getClearedIndices();
        
        assertEquals(3, returnedIndices.size());
        assertTrue(returnedIndices.contains(2));
        assertTrue(returnedIndices.contains(5));
        assertTrue(returnedIndices.contains(8));
    }

    @Test
    void testGetClearedIndicesEmpty() {
        List<Integer> indices = new ArrayList<>();
        int[][] matrix = new int[10][10];
        
        ClearRow clearRow = new ClearRow(0, matrix, indices);
        
        assertTrue(clearRow.getClearedIndices().isEmpty());
    }

    @Test
    void testGetClearedIndicesIsImmutable() {
        List<Integer> indices = new ArrayList<>();
        indices.add(5);
        int[][] matrix = new int[10][10];
        
        ClearRow clearRow = new ClearRow(1, matrix, indices);
        List<Integer> returned = clearRow.getClearedIndices();
        
        assertEquals(1, returned.size());
    }

    @Test
    void testClearRowWithMultipleIndices() {
        List<Integer> indices = Arrays.asList(0, 1, 2, 3);
        int[][] matrix = new int[10][10];
        
        ClearRow clearRow = new ClearRow(4, matrix, indices);
        
        assertEquals(4, clearRow.getLinesRemoved());
        assertEquals(4, clearRow.getClearedIndices().size());
    }
}


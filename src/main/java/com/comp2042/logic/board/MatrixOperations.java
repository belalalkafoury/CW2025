package com.comp2042.logic.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing matrix manipulation operations for the game board.
 * Includes collision detection, matrix copying, merging, row clearing, and garbage generation.
 * This class cannot be instantiated.
 */
public class MatrixOperations {

    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    /**
     * Checks if a brick would intersect with the board matrix at the specified position.
     * @param matrix The game board matrix
     * @param brick The brick shape matrix to check
     * @param x X coordinate where the brick would be placed
     * @param y Y coordinate where the brick would be placed
     * @return true if the brick would intersect with existing blocks or boundaries, false otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {

                if (brick[row][col] != 0) {
                    int targetRow = y + row;
                    int targetCol = x + col;

                    if (checkOutOfBound(matrix, targetRow, targetCol) ||
                            matrix[targetRow][targetCol] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    private static boolean checkOutOfBound(int[][] matrix, int row, int col) {
        return row < 0 || row >= matrix.length ||
                col < 0 || col >= matrix[0].length;
    }


    /**
     * Creates a deep copy of a 2D integer array.
     * @param original The matrix to copy
     * @return A new matrix with the same dimensions and values
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick shape into the board matrix at the specified position.
     * @param filledFields The current board matrix
     * @param brick The brick shape matrix to merge
     * @param x X coordinate where the brick should be placed
     * @param y Y coordinate where the brick should be placed
     * @return A new matrix with the brick merged into it
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                if (brick[row][col] != 0) {
                    int targetRow = y + row;
                    int targetCol = x + col;
                    copy[targetRow][targetCol] = brick[row][col];
                }
            }
        }
        return copy;
    }

    /**
     * Checks for completed rows and generates a new matrix with those rows removed.
     * Completed rows are identified as rows with no empty spaces (no zeros).
     * @param matrix The current game board matrix
     * @return ClearRow object containing the number of cleared rows, new matrix, and row indices
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                    break;
                }
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                int[] tmpRow = new int[matrix[i].length];
                System.arraycopy(matrix[i], 0, tmpRow, 0, matrix[i].length);
                newRows.add(tmpRow);
            }
        }
        
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                tmp[i][j] = 0;
            }
        }
        
        int rowIndex = matrix.length - 1;
        while (!newRows.isEmpty() && rowIndex >= 0) {
            int[] row = newRows.pollLast();
            if (row != null) {
                if (row.length == tmp[rowIndex].length) {
                    System.arraycopy(row, 0, tmp[rowIndex], 0, row.length);
                } else {
                    for (int j = 0; j < Math.min(row.length, tmp[rowIndex].length); j++) {
                        tmp[rowIndex][j] = row[j];
                    }
                }
                rowIndex--;
            }
        }
        
        while (rowIndex >= 0) {
            for (int j = 0; j < tmp[rowIndex].length; j++) {
                tmp[rowIndex][j] = 0;
            }
            rowIndex--;
        }
        
        return new ClearRow(clearedRows.size(), tmp, clearedRows);
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     * @param list The list of matrices to copy
     * @return A new list containing deep copies of all matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

    /**
     * Generates a garbage-filled matrix for Puzzle mode.
     * Each row is filled with random brick colors, with one empty space per row to ensure solvability.
     * @param width Width of the matrix
     * @param height Height of the matrix
     * @param garbageHeight Number of rows from the bottom to fill with garbage
     * @return A new matrix with garbage rows at the bottom
     */
    public static int[][] generateGarbage(int width, int height, int garbageHeight) {
        int[][] matrix = new int[width][height];
        java.util.Random random = new java.util.Random();
        
        for (int row = width - 1; row >= width - garbageHeight && row >= 0; row--) {
            for (int col = 0; col < height; col++) {
                matrix[row][col] = 1 + random.nextInt(7);
            }
            
            int emptyCol = random.nextInt(height);
            matrix[row][emptyCol] = 0;
        }
        
        return matrix;
    }

}

package com.comp2042.logic.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

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
        
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus, clearedRows);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}

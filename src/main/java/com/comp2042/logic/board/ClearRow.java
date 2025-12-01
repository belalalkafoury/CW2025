package com.comp2042.logic.board;

import java.util.List;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final List<Integer> clearedIndices;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedIndices) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedIndices = clearedIndices;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public List<Integer> getClearedIndices() {
        return clearedIndices;
    }
}

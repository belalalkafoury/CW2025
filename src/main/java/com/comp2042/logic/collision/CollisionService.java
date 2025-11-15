package com.comp2042.logic.collision;

import com.comp2042.logic.board.MatrixOperations;

public class CollisionService {
    public boolean intersects(int[][] board, int[][] shape, int x, int y) {
        return MatrixOperations.intersect(board, shape, x, y);
    }

    public boolean canPlaceAt(int[][] board, int[][] shape, int x, int y) {
        return !MatrixOperations.intersect(board, shape, x, y);
    }

}

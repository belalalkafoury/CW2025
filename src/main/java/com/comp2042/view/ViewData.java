package com.comp2042.view;

import com.comp2042.logic.board.MatrixOperations;
import java.util.List;
import java.util.ArrayList;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextPieces;
    private final int[][] heldBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextPieces, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextPieces = new ArrayList<>(nextPieces);
        this.heldBrickData = heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public List<int[][]> getNextPieces() {
        List<int[][]> copies = new ArrayList<>();
        for (int[][] piece : nextPieces) {
            copies.add(MatrixOperations.copy(piece));
        }
        return copies;
    }

    public int[][] getHeldBrickData() {
        return heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }
}
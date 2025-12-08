package com.comp2042.view.renderer;

import com.comp2042.util.BrickColorMapper;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Handles rendering of next pieces and held piece previews.
 * Manages the display of upcoming pieces and the held piece.
 */
public class NextPieceRenderer {
    
    private final GridPane holdPanel;
    private final GridPane nextPiece1;
    private final GridPane nextPiece2;
    private final GridPane nextPiece3;
    
    private Rectangle[][] nextPiece1Rectangles;
    private Rectangle[][] nextPiece2Rectangles;
    private Rectangle[][] nextPiece3Rectangles;
    
    public NextPieceRenderer(GridPane holdPanel, GridPane nextPiece1, GridPane nextPiece2, GridPane nextPiece3) {
        this.holdPanel = holdPanel;
        this.nextPiece1 = nextPiece1;
        this.nextPiece2 = nextPiece2;
        this.nextPiece3 = nextPiece3;
    }
    
    /**
     * Updates the held piece display.
     * @param heldBrickData The shape matrix of the held piece, or null if no piece is held
     */
    public void updateHeldPiece(int[][] heldBrickData) {
        if (holdPanel == null) {
            return;
        }
        drawMatrixToGrid(holdPanel, heldBrickData, GameConstants.NEXT_PIECE_BRICK_SIZE);
    }
    
    /**
     * Updates the next pieces display.
     * @param nextShapes List of shape matrices for the next pieces
     */
    public void updateNextPieces(List<int[][]> nextShapes) {
        if (nextPiece1 == null || nextPiece2 == null || nextPiece3 == null || nextShapes == null) {
            return;
        }
        
        GridPane[] grids = {nextPiece1, nextPiece2, nextPiece3};
        
        for (int pieceIndex = 0; pieceIndex < 3 && pieceIndex < nextShapes.size(); pieceIndex++) {
            GridPane grid = grids[pieceIndex];
            if (grid == null) {
                continue;
            }
            
            int[][] nextBrickData = nextShapes.get(pieceIndex);
            Rectangle[][] rects = drawMatrixToGrid(grid, nextBrickData, GameConstants.NEXT_PIECE_BRICK_SIZE);
            
            if (pieceIndex == 0) {
                nextPiece1Rectangles = rects;
            } else if (pieceIndex == 1) {
                nextPiece2Rectangles = rects;
            } else if (pieceIndex == 2) {
                nextPiece3Rectangles = rects;
            }
        }
    }
    
    /**
     * Clears all next piece displays.
     */
    public void clearNextPieces() {
        if (nextPiece1 != null) {
            nextPiece1.getChildren().clear();
        }
        if (nextPiece2 != null) {
            nextPiece2.getChildren().clear();
        }
        if (nextPiece3 != null) {
            nextPiece3.getChildren().clear();
        }
    }
    
    private Rectangle[][] drawMatrixToGrid(GridPane targetGrid, int[][] matrix, int brickSize) {
        if (targetGrid == null || matrix == null || matrix.length == 0) {
            return null;
        }
        
        targetGrid.getChildren().clear();
        
        Rectangle[][] rectangles = new Rectangle[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] != null) {
                rectangles[i] = new Rectangle[matrix[i].length];
                for (int j = 0; j < matrix[i].length; j++) {
                    Rectangle rectangle = new Rectangle(brickSize, brickSize);
                    rectangle.setFill(BrickColorMapper.getFillColor(matrix[i][j]));
                    setRectangleData(matrix[i][j], rectangle);
                    rectangles[i][j] = rectangle;
                    targetGrid.add(rectangle, j, i);
                }
            }
        }
        return rectangles;
    }
    
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(BrickColorMapper.getFillColor(color));
        rectangle.setArcHeight(GameConstants.ARC_SIZE);
        rectangle.setArcWidth(GameConstants.ARC_SIZE);
    }
}


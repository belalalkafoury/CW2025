package com.comp2042.view.renderer;

import com.comp2042.model.Board;
import com.comp2042.util.BrickColorMapper;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles rendering of the ghost piece preview.
 * Shows where the current piece will land.
 */
public class GhostPieceRenderer {
    
    private static final int BRICK_SIZE = 20;
    private static final int CELL_GAP = 1;
    private static final int BORDER_OFFSET = 8;
    private static final int ARC_SIZE = 9;
    
    private final GridPane ghostPanel;
    private Rectangle[][] ghostRectangles;
    private final List<Rectangle> activeGhostRects = new ArrayList<>();
    
    public GhostPieceRenderer(GridPane ghostPanel) {
        this.ghostPanel = ghostPanel;
    }
    
    /**
     * Initializes the ghost piece rectangles based on the current brick shape.
     * @param shape The current brick shape matrix
     */
    public void initializeGhostPiece(int[][] shape) {
        ghostRectangles = new Rectangle[shape.length][shape[0].length];
        ghostPanel.setHgap(1);
        ghostPanel.setVgap(1);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                rect.setFill(Color.TRANSPARENT);
                rect.setArcHeight(ARC_SIZE);
                rect.setArcWidth(ARC_SIZE);
                ghostRectangles[i][j] = rect;
                ghostPanel.add(rect, j, i);
            }
        }
    }
    
    /**
     * Updates the ghost piece display based on the current brick position and board state.
     * @param board The game board instance
     * @param currentX Current X position of the brick
     * @param currentY Current Y position of the brick
     * @param shape Current brick shape matrix
     */
    public void updateGhostPiece(Board board, int currentX, int currentY, int[][] shape) {
        if (board == null || ghostPanel == null) {
            return;
        }
        
        if (!com.comp2042.model.GameSettings.isShowGhostPiece()) {
            ghostPanel.setVisible(false);
            return;
        }
        
        for (Rectangle rect : activeGhostRects) {
            if (rect != null) {
                rect.setFill(Color.TRANSPARENT);
                rect.setVisible(false);
            }
        }
        activeGhostRects.clear();
        
        int ghostY = board.getGhostY(currentX, currentY);
        
        if (shape == null || shape.length == 0 || shape[0].length == 0) {
            ghostPanel.setVisible(false);
            return;
        }
        
        int distance = ghostY - currentY;
        
        if (distance < 0) {
            ghostPanel.setVisible(false);
            return;
        }
        
        ghostPanel.setVisible(true);
        
        if (ghostRectangles == null || 
            ghostRectangles.length != shape.length ||
            (ghostRectangles.length > 0 && ghostRectangles[0].length != shape[0].length)) {
            ghostPanel.getChildren().clear();
            initializeGhostPiece(shape);
        }
        
        double cellSize = BRICK_SIZE + CELL_GAP;
        
        double ghostStartX = BORDER_OFFSET + currentX * cellSize;
        double ghostStartY = BORDER_OFFSET + (ghostY - 2) * cellSize;
        ghostPanel.setLayoutX(ghostStartX);
        ghostPanel.setLayoutY(ghostStartY);
        
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (i < ghostRectangles.length && j < ghostRectangles[i].length) {
                    Rectangle rect = ghostRectangles[i][j];
                    if (shape[i][j] != 0) {
                        Paint color = BrickColorMapper.getFillColor(shape[i][j]);
                        if (color instanceof Color) {
                            rect.setFill(Color.TRANSPARENT);
                            rect.setStroke((Color) color);
                            rect.setStrokeWidth(2.0);
                            rect.setOpacity(1.0);
                            rect.setVisible(true);
                            activeGhostRects.add(rect);
                        }
                    } else {
                        rect.setVisible(false);
                    }
                }
            }
        }
    }
    
    /**
     * Hides the ghost piece preview.
     */
    public void hideGhostPiece() {
        if (ghostPanel != null) {
            ghostPanel.setVisible(false);
        }
    }
}


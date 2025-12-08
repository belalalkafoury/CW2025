package com.comp2042.view.renderer;

import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the game board matrix.
 * Manages the display matrix and grid visualization.
 */
public class GameBoardRenderer {
    
    private static final int BRICK_SIZE = 20;
    private static final int CELL_GAP = 1;
    private static final int ARC_SIZE = 9;
    
    private Rectangle[][] displayMatrix;
    private final GridPane gamePanel;
    
    public GameBoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    /**
     * Initializes the display matrix for the game board.
     * @param boardMatrix The board matrix to initialize from
     */
    public void initializeDisplayMatrix(int[][] boardMatrix) {
        if (displayMatrix != null) {
            for (int i = 0; i < displayMatrix.length; i++) {
                for (int j = 0; j < displayMatrix[i].length; j++) {
                    if (displayMatrix[i][j] != null) {
                        displayMatrix[i][j].setFill(Color.TRANSPARENT);
                    }
                }
            }
        }
        
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }
    }
    
    /**
     * Refreshes the game board background with the current board state.
     * @param board The current game board matrix
     */
    public void refreshGameBackground(int[][] board) {
        if (displayMatrix == null) {
            return;
        }
        for (int i = 0; i < displayMatrix.length; i++) {
            for (int j = 0; j < displayMatrix[i].length; j++) {
                if (displayMatrix[i][j] != null) {
                    displayMatrix[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i < displayMatrix.length && j < displayMatrix[i].length && displayMatrix[i][j] != null) {
                    setRectangleData(board[i][j], displayMatrix[i][j]);
                }
            }
        }
    }
    
    /**
     * Creates the grid lines for the game board.
     * @param boardMatrix The board matrix to determine grid dimensions
     */
    public void createGameBoardGrid(int[][] boardMatrix) {
        Group gridGroup = new Group();
        gridGroup.setMouseTransparent(true);
        gridGroup.setManaged(false);
        Color gridColor = Color.rgb(74, 158, 255, 0.3);
        
        int rows = boardMatrix.length - 2;
        int cols = boardMatrix[0].length;
        double cellSize = BRICK_SIZE + CELL_GAP;
        
        for (int i = 0; i <= rows; i++) {
            Line horizontalLine = new Line(0, i * cellSize, cols * cellSize, i * cellSize);
            horizontalLine.setStroke(gridColor);
            horizontalLine.setStrokeWidth(0.5);
            horizontalLine.setMouseTransparent(true);
            gridGroup.getChildren().add(horizontalLine);
        }
        
        for (int j = 0; j <= cols; j++) {
            Line verticalLine = new Line(j * cellSize, 0, j * cellSize, rows * cellSize);
            verticalLine.setStroke(gridColor);
            verticalLine.setStrokeWidth(0.5);
            verticalLine.setMouseTransparent(true);
            gridGroup.getChildren().add(verticalLine);
        }
        
        gamePanel.getChildren().add(0, gridGroup);
    }
    
    /**
     * Gets the display matrix for animation purposes.
     * @return The display matrix array
     */
    public Rectangle[][] getDisplayMatrix() {
        return displayMatrix;
    }
    
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(ARC_SIZE);
        rectangle.setArcWidth(ARC_SIZE);
    }
    
    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.ORANGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }
}


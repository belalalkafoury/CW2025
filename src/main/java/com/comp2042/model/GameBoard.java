
package com.comp2042.model;

import com.comp2042.logic.board.BrickRotator;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.MatrixOperations;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickFactory;
import com.comp2042.logic.bricks.OBrick;
import com.comp2042.logic.collision.CollisionService;
import com.comp2042.view.NextShapeInfo;
import com.comp2042.view.ViewData;
import com.comp2042.logic.board.rotation.NoRotationStrategy;
import com.comp2042.logic.board.rotation.StandardRotationStrategy;


import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core game board implementation managing Tetris game logic.
 * Handles brick movement, rotation with wall kicks, 7-Bag randomizer for piece generation,
 * hold mechanic, and special game modes (Puzzle, Reverted).
 */
public class GameBoard implements Board {
    private static final int START_X = 4;
    private static final int START_Y = 0;
    private static final int NEXT_PIECES_COUNT = 3;
    private static final int PUZZLE_GARBAGE_HEIGHT = 10;
    
    private static final Point[] WALL_KICKS = {
        new Point(0, 0),
        new Point(1, 0),
        new Point(-1, 0),
        new Point(0, -1),
        new Point(2, 0),
        new Point(-2, 0)
    };
    private final int width;
    private final int height;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final BrickFactory brickFactory = new BrickFactory();
    private final CollisionService collisionService = new CollisionService();
    private Brick currentBrick;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();
    private Brick heldBrick;
    private boolean canHold = true;

    /**
     * Constructor for GameBoard with specified dimensions.
     * @param width Width of the game board
     * @param height Height of the game board
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Moves the current brick down by one position.
     * @return true if the move was successful, false if collision occurred
     */
    @Override
    public boolean moveBrickDown() {
        Point p = translated(currentOffset, 0, 1);
        if (!canPlaceAt(brickRotator.getCurrentShape(), p)) return false;
        currentOffset = p;
        return true;

    }


    /**
     * Moves the current brick left by one position.
     * @return true if the move was successful, false if collision occurred
     */
    @Override
    public boolean moveBrickLeft() {
        Point p = translated(currentOffset, -1, 0);
        if (!canPlaceAt(brickRotator.getCurrentShape(), p)) return false;
        currentOffset = p;
        return true;

    }

    /**
     * Moves the current brick right by one position.
     * @return true if the move was successful, false if collision occurred
     */
    @Override
    public boolean moveBrickRight() {
        Point p = translated(currentOffset, 1, 0);
        if (!canPlaceAt(brickRotator.getCurrentShape(), p)) return false;
        currentOffset = p;
        return true;

    }

    /**
     * Rotates the current brick counter-clockwise with wall kick support.
     * Tests multiple offset positions to find a valid rotation spot.
     * @return true if rotation was successful, false if rotation would cause collision
     */
    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        
        for (Point offset : WALL_KICKS) {
            Point testPos = new Point(
                (int)(currentOffset.getX() + offset.getX()),
                (int)(currentOffset.getY() + offset.getY())
            );
            
            if (canPlaceAt(nextShape.getShape(), testPos)) {
                currentOffset = testPos;
                brickRotator.setCurrentShape(nextShape.getPosition());
                return true;
            }
        }
        
        return false;
    }

    /**
     * Creates and spawns a new brick using the 7-Bag randomizer system.
     * The bag ensures all 7 piece types are distributed evenly before refilling.
     * @return true if the brick can be placed, false if game over condition is met
     */
    @Override
    public boolean createNewBrick() {
        canHold = true;
        if (nextBricks.isEmpty()) {
            for (int i = 0; i < NEXT_PIECES_COUNT; i++) {
                nextBricks.add(brickFactory.createRandomBrick());
            }
        }

        currentBrick = nextBricks.poll();
        nextBricks.add(brickFactory.createRandomBrick());

        if (currentBrick instanceof OBrick) {
            brickRotator.setRotationStrategy(new NoRotationStrategy());
        } else {
            brickRotator.setRotationStrategy(new StandardRotationStrategy());
        }

        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(START_X, START_Y);

        return collisionService.canPlaceAt(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    /**
     * Swaps the current brick with the held brick, or holds the current brick if none is held.
     * Can only be used once per brick placement to prevent infinite swapping.
     */
    @Override
    public void holdBrick() {
        if (!canHold) {
            return;
        }

        if (heldBrick == null) {
            heldBrick = currentBrick;
            if (nextBricks.isEmpty()) {
                for (int i = 0; i < NEXT_PIECES_COUNT; i++) {
                    nextBricks.add(brickFactory.createRandomBrick());
                }
            }
            currentBrick = nextBricks.poll();
            nextBricks.add(brickFactory.createRandomBrick());
        } else {
            Brick temp = currentBrick;
            currentBrick = heldBrick;
            heldBrick = temp;
        }

        if (currentBrick instanceof OBrick) {
            brickRotator.setRotationStrategy(new NoRotationStrategy());
        } else {
            brickRotator.setRotationStrategy(new StandardRotationStrategy());
        }

        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(START_X, START_Y);
        canHold = false;
    }



    /**
     * Gets a copy of the current game board matrix.
     * @return 2D array representing the board state where 0 is empty and non-zero values represent brick colors
     */
    @Override
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix);
    }
    
    /**
     * Gets a copy of the current falling brick's shape matrix.
     * @return 2D array representing the current brick's shape
     */
    @Override
    public int[][] getCurrentShape() {
        return MatrixOperations.copy(brickRotator.getCurrentShape());
    }
    
    /**
     * Gets view data containing current brick, next bricks, and held brick information.
     * @return ViewData object containing all information needed for rendering
     */
    @Override
    public ViewData getViewData() {
        List<int[][]> nextShapes = nextBricks.stream()
                .map(b -> b.getShapeMatrix().get(0))
                .collect(Collectors.toList());
        int[][] heldShape = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), nextShapes, heldShape);
    }

    /**
     * Merges the current falling brick into the background board matrix.
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Checks for completed rows that should be cleared.
     * @return ClearRow object containing information about cleared rows and the new matrix state
     */
    @Override
    public ClearRow checkClears() {
        return MatrixOperations.checkRemoving(currentGameMatrix);
    }

    /**
     * Applies the cleared rows and updates the board matrix.
     * @param clearRow ClearRow object containing the new matrix state after clearing
     */
    @Override
    public void commitClear(ClearRow clearRow) {
        // Use the matrix from ClearRow which already has the correct dimensions
        currentGameMatrix = clearRow.getNewMatrix();
    }

    /**
     * Gets the Score object tracking game statistics.
     * @return Score object containing score, lines cleared, and combo information
     */
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game board to start a new game.
     */
    @Override
    public void newGame() {
        // new int[width][height] already initializes all elements to 0
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        canHold = true;
        nextBricks.clear();
        createNewBrick();
    }

    /**
     * Sets up the board for Puzzle mode by generating garbage rows at the bottom.
     * Creates a partially filled board with one empty space per row to make it solvable.
     */
    public void setupPuzzleMode() {
        currentGameMatrix = MatrixOperations.generateGarbage(width, height, PUZZLE_GARBAGE_HEIGHT);
        currentBrick = null;
        nextBricks.clear();
        heldBrick = null;
        canHold = true;
        createNewBrick();
    }

    private static Point translated(Point p, int dx, int dy) {
        Point np = new Point(p);
        np.translate(dx, dy);
        return np;
    }

    private boolean canPlaceAt(int[][] shape, Point p) {
        return collisionService.canPlaceAt(
                currentGameMatrix,
                shape,
                (int) p.getX(),
                (int) p.getY());
    }
    
    /**
     * Instantly drops the current brick to its lowest valid position.
     * @return The number of rows the brick was dropped
     */
    @Override
    public int hardDrop() {
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();
        int ghostY = getGhostY(currentX, currentY);
        int distance = ghostY - currentY;
        
        if (distance > 0) {
            currentOffset = new Point(currentX, ghostY);
            return distance;
        }
        return 0;
    }
    
    /**
     * Gets a list of row indices that are fully filled and ready to be cleared.
     * @return List of row indices (0-based) that should be cleared
     */
    @Override
    public java.util.List<Integer> getRowsToClear() {
        java.util.List<Integer> clearedRows = new java.util.ArrayList<>();
        for (int i = 0; i < currentGameMatrix.length; i++) {
            boolean rowToClear = true;
            for (int j = 0; j < currentGameMatrix[0].length; j++) {
                if (currentGameMatrix[i][j] == 0) {
                    rowToClear = false;
                    break;
                }
            }
            if (rowToClear) {
                clearedRows.add(i);
            }
        }
        return clearedRows;
    }

    /**
     * Calculates the Y position where the ghost piece (preview of landing position) should be displayed.
     * @param currentX Current X position of the brick
     * @param currentY Current Y position of the brick
     * @return Y coordinate where the brick would land if dropped
     */
    @Override
    public int getGhostY(int currentX, int currentY) {
        int[][] shape = brickRotator.getCurrentShape();
        
        if (shape == null || shape.length == 0) {
            return currentY;
        }
        
        int ghostY = currentY;
        
        while (true) {
            int nextY = ghostY + 1;
            Point testPos = new Point(currentX, nextY);
            
            if (!canPlaceAt(shape, testPos)) {
                break;
            }
            
            ghostY = nextY;
        }
        
        return ghostY;
    }

    Brick getCurrentBrickForTest() {
        return currentBrick;
    }

    Brick getHeldBrickForTest() {
        return heldBrick;
    }

    boolean canHoldForTest() {
        return canHold;
    }

    Point getCurrentOffsetForTest() {
        return new Point(currentOffset);
    }
}

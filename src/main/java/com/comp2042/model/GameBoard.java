
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

public class GameBoard implements Board {
    private static final int START_X = 4;
    private static final int START_Y = 0;
    
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

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        Point p = translated(currentOffset, 0, 1);
        if (!canPlaceAt(brickRotator.getCurrentShape(), p)) return false;
        currentOffset = p;
        return true;

    }


    @Override
    public boolean moveBrickLeft() {
        Point p = translated(currentOffset, -1, 0);
        if (!canPlaceAt(brickRotator.getCurrentShape(), p)) return false;
        currentOffset = p;
        return true;

    }

    @Override
    public boolean moveBrickRight() {
        Point p = translated(currentOffset, 1, 0);
        if (!canPlaceAt(brickRotator.getCurrentShape(), p)) return false;
        currentOffset = p;
        return true;

    }

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

    @Override
    public boolean createNewBrick() {
        canHold = true;
        if (nextBricks.isEmpty()) {
            for (int i = 0; i < 3; i++) {
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

    @Override
    public void holdBrick() {
        if (!canHold) {
            return;
        }

        if (heldBrick == null) {
            heldBrick = currentBrick;
            if (nextBricks.isEmpty()) {
                for (int i = 0; i < 3; i++) {
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



    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }
    
    @Override
    public int[][] getCurrentShape() {
        return brickRotator.getCurrentShape();
    }
    
    @Override
    public ViewData getViewData() {
        List<int[][]> nextShapes = nextBricks.stream()
                .map(b -> b.getShapeMatrix().get(0))
                .collect(Collectors.toList());
        int[][] heldShape = (heldBrick != null) ? heldBrick.getShapeMatrix().get(0) : null;
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), nextShapes, heldShape);
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow checkClears() {
        return MatrixOperations.checkRemoving(currentGameMatrix);
    }

    @Override
    public void commitClear(ClearRow clearRow) {
        int[][] newMatrix = clearRow.getNewMatrix();
        for (int i = 0; i < currentGameMatrix.length; i++) {
            for (int j = 0; j < currentGameMatrix[i].length; j++) {
                if (i < newMatrix.length && j < newMatrix[i].length) {
                    currentGameMatrix[i][j] = newMatrix[i][j];
                } else {
                    currentGameMatrix[i][j] = 0;
                }
            }
        }
    }

    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                currentGameMatrix[i][j] = 0;
            }
        }
        score.reset();
        heldBrick = null;
        canHold = true;
        nextBricks.clear();
        createNewBrick();
    }

    public void setupPuzzleMode() {
        currentGameMatrix = MatrixOperations.generateGarbage(width, height, 10);
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

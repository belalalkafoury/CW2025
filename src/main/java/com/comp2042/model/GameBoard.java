
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

public class GameBoard implements Board {
    private static final int START_X = 4;
    private static final int START_Y = 0;
    private final int width;
    private final int height;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final BrickFactory brickFactory = new BrickFactory();
    private final CollisionService collisionService = new CollisionService();
    private Brick currentBrick;
    private Brick nextBrick;

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
        if (!canPlaceAt(nextShape.getShape(), currentOffset)) {
            return false;
        }
        brickRotator.setCurrentShape(nextShape.getPosition());
        return true;
    }

    @Override
    public boolean createNewBrick() {

        if (nextBrick == null) {
            nextBrick = brickFactory.createRandomBrick();
        }

        currentBrick = nextBrick;
        nextBrick = brickFactory.createRandomBrick();

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
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }
    
    @Override
    public int[][] getCurrentShape() {
        return brickRotator.getCurrentShape();
    }
    
    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), nextBrick.getShapeMatrix().get(0)
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
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

}

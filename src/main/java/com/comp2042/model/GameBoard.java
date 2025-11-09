package com.comp2042.model;

import com.comp2042.logic.board.BrickRotator;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.MatrixOperations;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.view.NextShapeInfo;
import com.comp2042.view.ViewData;

import java.awt.*;

public class GameBoard implements Board {
    private static final int START_X = 4;
    private static final int START_Y = 10;
    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
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
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(START_X, START_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
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

    @Override
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
        boolean conflict = MatrixOperations.intersect(
                currentGameMatrix, shape, (int) p.getX(), (int) p.getY());
        return !conflict;
    }

}

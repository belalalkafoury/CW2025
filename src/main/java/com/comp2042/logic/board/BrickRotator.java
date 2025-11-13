package com.comp2042.logic.board;

import com.comp2042.logic.board.rotation.RotationStrategy;
import com.comp2042.view.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;
    private RotationStrategy rotationStrategy;

    public void setRotationStrategy(RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }


    public NextShapeInfo getNextShape() {
        int nextShape = rotationStrategy.getNextRotationIndex(brick, currentShape);
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}

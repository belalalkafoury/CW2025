package com.comp2042.logic.board.rotation;
import com.comp2042.logic.bricks.Brick;

public class StandardRotationStrategy implements RotationStrategy {

    @Override
    public int getNextRotationIndex(Brick brick, int currentIndex) {
        return (currentIndex + 1) % brick.getShapeMatrix().size();
    }
}

package com.comp2042.logic.board.rotation;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NoRotationStrategyTest {

    private NoRotationStrategy strategy;
    private Brick brick;

    @BeforeEach
    void setUp() {
        strategy = new NoRotationStrategy();
        BrickFactory factory = new BrickFactory();
        brick = factory.createBrick(com.comp2042.logic.bricks.BrickType.O);
    }

    @Test
    void testGetNextRotationIndexReturnsSameIndex() {
        int currentIndex = 0;
        int nextIndex = strategy.getNextRotationIndex(brick, currentIndex);
        
        assertEquals(currentIndex, nextIndex, "NoRotationStrategy should return same index");
    }

    @Test
    void testGetNextRotationIndexWithDifferentIndices() {
        assertEquals(0, strategy.getNextRotationIndex(brick, 0));
        assertEquals(1, strategy.getNextRotationIndex(brick, 1));
        assertEquals(2, strategy.getNextRotationIndex(brick, 2));
        assertEquals(3, strategy.getNextRotationIndex(brick, 3));
    }

    @Test
    void testGetNextRotationIndexWithOBrick() {
        Brick oBrick = new BrickFactory().createBrick(com.comp2042.logic.bricks.BrickType.O);
        int index = strategy.getNextRotationIndex(oBrick, 0);
        assertEquals(0, index);
    }
}


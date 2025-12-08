package com.comp2042.logic.board.rotation;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickFactory;
import com.comp2042.logic.bricks.BrickType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StandardRotationStrategyTest {

    private StandardRotationStrategy strategy;
    private BrickFactory brickFactory;

    @BeforeEach
    void setUp() {
        strategy = new StandardRotationStrategy();
        brickFactory = new BrickFactory();
    }

    @Test
    void testGetNextRotationIndexCycles() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        int shapeCount = brick.getShapeMatrix().size();
        
        for (int i = 0; i < shapeCount; i++) {
            int next = strategy.getNextRotationIndex(brick, i);
            int expected = (i + 1) % shapeCount;
            assertEquals(expected, next, "Should cycle to next rotation");
        }
    }

    @Test
    void testGetNextRotationIndexWrapsAround() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        int lastIndex = brick.getShapeMatrix().size() - 1;
        int nextIndex = strategy.getNextRotationIndex(brick, lastIndex);
        
        assertEquals(0, nextIndex, "Should wrap around to 0 after last index");
    }

    @Test
    void testGetNextRotationIndexWithIBrick() {
        Brick iBrick = brickFactory.createBrick(BrickType.I);
        int shapeCount = iBrick.getShapeMatrix().size();
        
        assertEquals(1, strategy.getNextRotationIndex(iBrick, 0));
        assertEquals(0, strategy.getNextRotationIndex(iBrick, shapeCount - 1));
    }

    @Test
    void testGetNextRotationIndexWithTBrick() {
        Brick tBrick = brickFactory.createBrick(BrickType.T);
        int shapeCount = tBrick.getShapeMatrix().size();
        
        for (int i = 0; i < shapeCount; i++) {
            int next = strategy.getNextRotationIndex(tBrick, i);
            assertEquals((i + 1) % shapeCount, next);
        }
    }

    @Test
    void testGetNextRotationIndexMultipleCycles() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        int shapeCount = brick.getShapeMatrix().size();
        int current = 0;
        
        for (int cycle = 0; cycle < 2; cycle++) {
            for (int i = 0; i < shapeCount; i++) {
                int next = strategy.getNextRotationIndex(brick, current);
                assertEquals((current + 1) % shapeCount, next);
                current = next;
            }
        }
    }
}



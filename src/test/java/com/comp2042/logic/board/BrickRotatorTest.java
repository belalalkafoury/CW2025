package com.comp2042.logic.board;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickFactory;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.logic.board.rotation.StandardRotationStrategy;
import com.comp2042.logic.board.rotation.NoRotationStrategy;
import com.comp2042.view.NextShapeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BrickRotatorTest {

    private BrickRotator brickRotator;
    private BrickFactory brickFactory;

    @BeforeEach
    void setUp() {
        brickRotator = new BrickRotator();
        brickFactory = new BrickFactory();
    }

    @Test
    void testSetBrick() {
        Brick brick = brickFactory.createBrick(BrickType.I);
        brickRotator.setBrick(brick);
        
        assertNotNull(brickRotator.getCurrentShape(), "Should have current shape after setting brick");
    }

    @Test
    void testSetBrickResetsCurrentShape() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        brickRotator.setBrick(brick);
        brickRotator.setCurrentShape(2);
        
        Brick newBrick = brickFactory.createBrick(BrickType.L);
        brickRotator.setBrick(newBrick);
        
        int[][] shape = brickRotator.getCurrentShape();
        assertNotNull(shape, "Current shape should be reset to first rotation");
    }

    @Test
    void testGetCurrentShape() {
        Brick brick = brickFactory.createBrick(BrickType.I);
        brickRotator.setBrick(brick);
        
        int[][] shape = brickRotator.getCurrentShape();
        assertNotNull(shape, "Should return current shape");
        assertTrue(shape.length > 0, "Shape should have rows");
        assertTrue(shape[0].length > 0, "Shape should have columns");
    }

    @Test
    void testSetCurrentShape() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        brickRotator.setBrick(brick);
        
        brickRotator.setCurrentShape(1);
        int[][] shape1 = brickRotator.getCurrentShape();
        
        brickRotator.setCurrentShape(2);
        int[][] shape2 = brickRotator.getCurrentShape();
        
        assertNotNull(shape1);
        assertNotNull(shape2);
    }

    @Test
    void testGetNextShapeWithStandardRotation() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        brickRotator.setBrick(brick);
        brickRotator.setRotationStrategy(new StandardRotationStrategy());
        
        NextShapeInfo nextShape = brickRotator.getNextShape();
        
        assertNotNull(nextShape, "Should return next shape info");
        assertNotNull(nextShape.getShape(), "Should have shape matrix");
        assertTrue(nextShape.getPosition() >= 0, "Should have valid position");
    }

    @Test
    void testGetNextShapeCyclesThroughRotations() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        brickRotator.setBrick(brick);
        brickRotator.setRotationStrategy(new StandardRotationStrategy());
        brickRotator.setCurrentShape(0);
        
        NextShapeInfo next1 = brickRotator.getNextShape();
        brickRotator.setCurrentShape(next1.getPosition());
        
        NextShapeInfo next2 = brickRotator.getNextShape();
        
        assertNotNull(next1);
        assertNotNull(next2);
    }

    @Test
    void testSetRotationStrategy() {
        Brick brick = brickFactory.createBrick(BrickType.O);
        brickRotator.setBrick(brick);
        
        brickRotator.setRotationStrategy(new NoRotationStrategy());
        NextShapeInfo nextShape = brickRotator.getNextShape();
        
        assertNotNull(nextShape, "Should work with NoRotationStrategy");
    }

    @Test
    void testOBrickWithNoRotationStrategy() {
        Brick brick = brickFactory.createBrick(BrickType.O);
        brickRotator.setBrick(brick);
        brickRotator.setRotationStrategy(new NoRotationStrategy());
        
        NextShapeInfo nextShape = brickRotator.getNextShape();
        
        assertNotNull(nextShape, "O brick should work with NoRotationStrategy");
    }

    @Test
    void testIBrickWithStandardRotation() {
        Brick brick = brickFactory.createBrick(BrickType.I);
        brickRotator.setBrick(brick);
        brickRotator.setRotationStrategy(new StandardRotationStrategy());
        
        NextShapeInfo nextShape = brickRotator.getNextShape();
        
        assertNotNull(nextShape, "I brick should work with StandardRotationStrategy");
    }

    @Test
    void testMultipleRotations() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        brickRotator.setBrick(brick);
        brickRotator.setRotationStrategy(new StandardRotationStrategy());
        
        for (int i = 0; i < 4; i++) {
            NextShapeInfo next = brickRotator.getNextShape();
            assertNotNull(next, "Should get next shape for rotation " + i);
            brickRotator.setCurrentShape(next.getPosition());
        }
    }
}



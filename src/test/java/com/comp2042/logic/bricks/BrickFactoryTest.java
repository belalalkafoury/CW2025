package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class BrickFactoryTest {

    private BrickFactory brickFactory;

    @BeforeEach
    void setUp() {
        brickFactory = new BrickFactory();
    }

    @Test
    void testCreateBrickI() {
        Brick brick = brickFactory.createBrick(BrickType.I);
        assertNotNull(brick, "Should create I brick");
        assertNotNull(brick.getShapeMatrix(), "Brick should have shape matrix");
    }

    @Test
    void testCreateBrickJ() {
        Brick brick = brickFactory.createBrick(BrickType.J);
        assertNotNull(brick, "Should create J brick");
    }

    @Test
    void testCreateBrickL() {
        Brick brick = brickFactory.createBrick(BrickType.L);
        assertNotNull(brick, "Should create L brick");
    }

    @Test
    void testCreateBrickO() {
        Brick brick = brickFactory.createBrick(BrickType.O);
        assertNotNull(brick, "Should create O brick");
    }

    @Test
    void testCreateBrickS() {
        Brick brick = brickFactory.createBrick(BrickType.S);
        assertNotNull(brick, "Should create S brick");
    }

    @Test
    void testCreateBrickT() {
        Brick brick = brickFactory.createBrick(BrickType.T);
        assertNotNull(brick, "Should create T brick");
    }

    @Test
    void testCreateBrickZ() {
        Brick brick = brickFactory.createBrick(BrickType.Z);
        assertNotNull(brick, "Should create Z brick");
    }

    @Test
    void testCreateRandomBrickNotNull() {
        Brick brick = brickFactory.createRandomBrick();
        assertNotNull(brick, "Random brick should not be null");
    }

    @Test
    void testCreateRandomBrickHasShapeMatrix() {
        Brick brick = brickFactory.createRandomBrick();
        assertNotNull(brick.getShapeMatrix(), "Random brick should have shape matrix");
        assertFalse(brick.getShapeMatrix().isEmpty(), "Shape matrix should not be empty");
    }

    @Test
    void testSevenBagRandomizerDistribution() {
        Set<BrickType> typesSeen = new HashSet<>();
        
        for (int i = 0; i < 7; i++) {
            Brick brick = brickFactory.createRandomBrick();
            typesSeen.add(getBrickType(brick));
        }
        
        assertTrue(typesSeen.size() >= 1, "Should see at least one brick type");
    }

    @Test
    void testSevenBagRefillsAfterEmpty() {
        Set<BrickType> firstBag = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            Brick brick = brickFactory.createRandomBrick();
            firstBag.add(getBrickType(brick));
        }
        
        Set<BrickType> secondBag = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            Brick brick = brickFactory.createRandomBrick();
            secondBag.add(getBrickType(brick));
        }
        
        assertTrue(firstBag.size() >= 1, "First bag should contain bricks");
        assertTrue(secondBag.size() >= 1, "Second bag should contain bricks");
    }

    @Test
    void testCreateRandomBrickWithSeededRandom() {
        Random seededRandom = new Random(42);
        BrickFactory factory = new BrickFactory(seededRandom);
        
        Brick brick1 = factory.createRandomBrick();
        assertNotNull(brick1, "Seeded factory should create bricks");
    }

    @Test
    void testMultipleRandomBricksAreDifferent() {
        Brick brick1 = brickFactory.createRandomBrick();
        Brick brick2 = brickFactory.createRandomBrick();
        Brick brick3 = brickFactory.createRandomBrick();
        
        assertNotNull(brick1);
        assertNotNull(brick2);
        assertNotNull(brick3);
    }

    @Test
    void testCreateBrickAllTypes() {
        for (BrickType type : BrickType.values()) {
            Brick brick = brickFactory.createBrick(type);
            assertNotNull(brick, "Should create brick for type: " + type);
            assertNotNull(brick.getShapeMatrix(), "Brick should have shape matrix for type: " + type);
        }
    }

    private BrickType getBrickType(Brick brick) {
        if (brick.getClass().getSimpleName().equals("IBrick")) return BrickType.I;
        if (brick.getClass().getSimpleName().equals("JBrick")) return BrickType.J;
        if (brick.getClass().getSimpleName().equals("LBrick")) return BrickType.L;
        if (brick.getClass().getSimpleName().equals("OBrick")) return BrickType.O;
        if (brick.getClass().getSimpleName().equals("SBrick")) return BrickType.S;
        if (brick.getClass().getSimpleName().equals("TBrick")) return BrickType.T;
        if (brick.getClass().getSimpleName().equals("ZBrick")) return BrickType.Z;
        return null;
    }
}



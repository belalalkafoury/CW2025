package com.comp2042.logic.bricks;

import java.util.Random;

public class BrickFactory {

    private final Random random;

    public BrickFactory() {
        this.random = new Random();
    }

    public BrickFactory(Random random) {
        this.random = random;
    }

    public Brick createBrick(BrickType type) {
        switch (type) {
            case I:
                return new IBrick();
            case J:
                return new JBrick();
            case L:
                return new LBrick();
            case O:
                return new OBrick();
            case S:
                return new SBrick();
            case T:
                return new TBrick();
            case Z:
                return new ZBrick();
            default:
                throw new IllegalArgumentException("Unknown brick type: " + type);
        }
    }

    public Brick createRandomBrick() {
        BrickType[] types = BrickType.values();
        BrickType randomType = types[random.nextInt(types.length)];
        return createBrick(randomType);
    }
}

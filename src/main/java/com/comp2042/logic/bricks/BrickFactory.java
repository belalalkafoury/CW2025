package com.comp2042.logic.bricks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BrickFactory {

    private final Random random;
    private final List<BrickType> bag = new ArrayList<>();

    public BrickFactory() {
        this.random = new Random();
        refillBag();
    }

    public BrickFactory(Random random) {
        this.random = random;
        refillBag();
    }

    private void refillBag() {
        bag.clear();
        BrickType[] types = BrickType.values();
        for (BrickType type : types) {
            bag.add(type);
        }
        Collections.shuffle(bag, random);
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
        if (bag.isEmpty()) {
            refillBag();
        }
        BrickType nextType = bag.remove(0);
        return createBrick(nextType);
    }
}

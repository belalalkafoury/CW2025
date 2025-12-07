package com.comp2042.logic.bricks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Factory for creating Tetris bricks using the 7-Bag randomizer system.
 * The bag ensures all 7 piece types (I, J, L, O, S, T, Z) are distributed evenly.
 * When the bag is empty, it is automatically refilled with all 7 types in random order.
 */
public class BrickFactory {

    private final Random random;
    private final List<BrickType> bag = new ArrayList<>();

    /**
     * Constructs a BrickFactory with a new Random instance.
     */
    public BrickFactory() {
        this.random = new Random();
        refillBag();
    }

    /**
     * Constructs a BrickFactory with a specified Random instance for testing.
     * @param random Random number generator to use
     */
    public BrickFactory(Random random) {
        this.random = random;
        refillBag();
    }

    /**
     * Refills the bag with all 7 brick types and shuffles them.
     * This ensures an even distribution of pieces before randomness is applied.
     */
    private void refillBag() {
        bag.clear();
        BrickType[] types = BrickType.values();
        for (BrickType type : types) {
            bag.add(type);
        }
        Collections.shuffle(bag, random);
    }

    /**
     * Creates a brick of the specified type.
     * @param type The type of brick to create
     * @return A new Brick instance of the specified type
     * @throws IllegalArgumentException if the brick type is unknown
     */
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

    /**
     * Creates a random brick using the 7-Bag randomizer system.
     * Automatically refills the bag when empty to ensure even distribution.
     * @return A new Brick instance of a randomly selected type from the bag
     */
    public Brick createRandomBrick() {
        if (bag.isEmpty()) {
            refillBag();
        }
        BrickType nextType = bag.remove(0);
        return createBrick(nextType);
    }
}

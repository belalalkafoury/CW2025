package com.comp2042.model;

/**
 * Enumeration of available game modes.
 * CLASSIC: Standard Tetris gameplay
 * TIME_ATTACK: Score as many points as possible within a time limit
 * PUZZLE: Start with garbage rows and clear them
 * REVERTED: Controls are inverted (left is right, right is left)
 */
public enum GameMode {
    CLASSIC,
    TIME_ATTACK,
    PUZZLE,
    REVERTED
}


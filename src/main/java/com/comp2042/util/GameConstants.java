package com.comp2042.util;

/**
 * Centralized constants for the game application.
 * Contains window dimensions, game mode targets, volume settings, and other configuration values.
 */
public final class GameConstants {
    
    /** Window width in pixels. */
    public static final int WINDOW_WIDTH = 700;
    
    /** Window height in pixels. */
    public static final int WINDOW_HEIGHT = 600;
    
    /** Target number of lines to clear in Puzzle mode. */
    public static final int PUZZLE_MODE_TARGET_LINES = 40;
    
    /** Volume adjustment step size. */
    public static final double VOLUME_STEP = 0.1;
    
    /** Maximum volume value (1.0 = 100%). */
    public static final double MAX_VOLUME = 1.0;
    
    /** Minimum volume value (0.0 = 0%). */
    public static final double MIN_VOLUME = 0.0;
    
    /** Rate increment per combo level for sound effects. */
    public static final double COMBO_RATE_INCREMENT = 0.1;
    
    /** Maximum playback rate for combo sound effects. */
    public static final double MAX_COMBO_RATE = 2.0;
    
    /** Base playback rate for sound effects. */
    public static final double BASE_COMBO_RATE = 1.0;
    
    private GameConstants() {
    }
}


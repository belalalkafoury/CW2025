package com.comp2042.util;

/**
 * Centralized constants for the game application.
 * Contains window dimensions, game mode targets, volume settings, and other configuration values.
 */
public final class GameConstants {
    
    // Window dimensions
    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 600;
    
    // Game mode targets
    public static final int PUZZLE_MODE_TARGET_LINES = 40;
    
    // Volume settings
    public static final double VOLUME_STEP = 0.1;
    public static final double MAX_VOLUME = 1.0;
    public static final double MIN_VOLUME = 0.0;
    
    // Sound effects
    public static final double COMBO_RATE_INCREMENT = 0.1;
    public static final double MAX_COMBO_RATE = 2.0;
    public static final double BASE_COMBO_RATE = 1.0;
    
    private GameConstants() {
        // Utility class - prevent instantiation
    }
}


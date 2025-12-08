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
    
    /** Brick size in pixels for main game board. */
    public static final int BRICK_SIZE = 20;
    
    /** Brick size in pixels for next piece preview. */
    public static final int NEXT_PIECE_BRICK_SIZE = 16;
    
    /** Gap between cells in pixels. */
    public static final int CELL_GAP = 1;
    
    /** Border offset in pixels. */
    public static final int BORDER_OFFSET = 8;
    
    /** Arc size for rounded rectangles. */
    public static final int ARC_SIZE = 9;
    
    /** Panel X offset in pixels. */
    public static final int PANEL_OFFSET_X = 12;
    
    /** Panel Y offset in pixels. */
    public static final int PANEL_OFFSET_Y = 42;
    
    private GameConstants() {
    }
}


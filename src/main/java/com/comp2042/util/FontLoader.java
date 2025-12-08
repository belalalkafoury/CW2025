package com.comp2042.util;

import javafx.scene.text.Font;

/**
 * Utility class for loading fonts consistently across the application.
 * Provides centralized font loading to avoid code duplication.
 */
public final class FontLoader {
    
    private static final String PRESS_START_2P_PATH = "fonts/press-start-2p-font/PressStart2P-vaV7.ttf";
    private static final String TETRICIDE_PATH = "fonts/tetricide-brk/tetri.ttf";
    private static final String DIGITAL_PATH = "fonts/digital.ttf";
    
    private FontLoader() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Loads the Press Start 2P font with the specified size.
     * @param size The font size to load
     * @return true if the font was loaded successfully, false otherwise
     */
    public static boolean loadPressStart2P(double size) {
        try {
            Font font = Font.loadFont(
                FontLoader.class.getClassLoader().getResourceAsStream(PRESS_START_2P_PATH),
                size
            );
            return font != null;
        } catch (Exception e) {
            Logger.error("Could not load Press Start 2P font: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads the Tetricide font with the specified size.
     * @param size The font size to load
     * @return true if the font was loaded successfully, false otherwise
     */
    public static boolean loadTetricide(double size) {
        try {
            Font font = Font.loadFont(
                FontLoader.class.getClassLoader().getResourceAsStream(TETRICIDE_PATH),
                size
            );
            return font != null;
        } catch (Exception e) {
            Logger.error("Could not load Tetricide font: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads the Digital font with the specified size.
     * @param size The font size to load
     * @return The loaded Font, or null if loading failed
     */
    public static Font loadDigital(double size) {
        try {
            return Font.loadFont(
                FontLoader.class.getClassLoader().getResource(DIGITAL_PATH).toExternalForm(),
                size
            );
        } catch (Exception e) {
            Logger.error("Could not load Digital font: " + e.getMessage());
            return null;
        }
    }
}


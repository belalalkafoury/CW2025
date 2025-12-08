package com.comp2042.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Utility class for mapping brick color codes to JavaFX Paint objects.
 * Centralizes color mapping logic to avoid duplication across renderers.
 */
public final class BrickColorMapper {
    
    private BrickColorMapper() {
    }
    
    /**
     * Maps a brick color code to its corresponding Paint color.
     * @param colorCode The color code (0-7)
     * @return The Paint color corresponding to the code, or WHITE for unknown codes
     */
    public static Paint getFillColor(int colorCode) {
        switch (colorCode) {
            case 0:
                return Color.TRANSPARENT;
            case 1:
                return Color.AQUA;
            case 2:
                return Color.BLUEVIOLET;
            case 3:
                return Color.DARKGREEN;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.RED;
            case 6:
                return Color.ORANGE;
            case 7:
                return Color.BURLYWOOD;
            default:
                return Color.WHITE;
        }
    }
}


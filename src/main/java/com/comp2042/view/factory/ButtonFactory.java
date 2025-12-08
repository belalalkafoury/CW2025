package com.comp2042.view.factory;

import javafx.scene.control.Button;

/**
 * Factory for creating styled buttons with consistent appearance and hover effects.
 * Reduces code duplication in UI panels.
 */
public final class ButtonFactory {
    
    private static final String BASE_STYLE = 
        "-fx-border-width: 2px;" +
        "-fx-border-radius: 6px;" +
        "-fx-background-radius: 6px;" +
        "-fx-text-fill: white;" +
        "-fx-font-family: \"Press Start 2P\";" +
        "-fx-font-size: 12px;" +
        "-fx-font-weight: bold;" +
        "-fx-pref-width: 200px;" +
        "-fx-pref-height: 40px;" +
        "-fx-cursor: hand;";
    
    private ButtonFactory() {
    }
    
    /**
     * Creates a styled button with cyan border and hover effect.
     * @param text The button text
     * @param action The action to execute on click
     * @return A styled Button instance
     */
    public static Button createCyanButton(String text, Runnable action) {
        String normalStyle = BASE_STYLE +
            "-fx-border-color: #00FFFF;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);";
        
        String hoverStyle = BASE_STYLE +
            "-fx-border-color: #00FFFF;" +
            "-fx-background-color: rgba(0, 255, 255, 0.2);";
        
        return createButtonWithHover(text, action, normalStyle, hoverStyle);
    }
    
    /**
     * Creates a styled button with gray border and red hover effect.
     * @param text The button text
     * @param action The action to execute on click
     * @return A styled Button instance
     */
    public static Button createCancelButton(String text, Runnable action) {
        String normalStyle = BASE_STYLE +
            "-fx-border-color: #555555;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-text-fill: #AAAAAA;";
        
        String hoverStyle = BASE_STYLE +
            "-fx-border-color: #FF0000;" +
            "-fx-background-color: rgba(255, 0, 0, 0.2);" +
            "-fx-text-fill: white;";
        
        return createButtonWithHover(text, action, normalStyle, hoverStyle);
    }
    
    /**
     * Creates a styled button with red border and hover effect.
     * @param text The button text
     * @param fontSize The font size for the button
     * @param action The action to execute on click
     * @return A styled Button instance
     */
    public static Button createRedButton(String text, int fontSize, Runnable action) {
        String baseStyle = BASE_STYLE.replace("12px", fontSize + "px");
        String normalStyle = baseStyle +
            "-fx-border-color: #FF0000;" +
            "-fx-background-color: rgba(0, 0, 0, 0.8);" +
            "-fx-pref-width: 150px;" +
            "-fx-pref-height: 35px;";
        
        String hoverStyle = baseStyle +
            "-fx-border-color: #FF0000;" +
            "-fx-background-color: rgba(255, 0, 0, 0.2);" +
            "-fx-pref-width: 150px;" +
            "-fx-pref-height: 35px;";
        
        return createButtonWithHover(text, action, normalStyle, hoverStyle);
    }
    
    private static Button createButtonWithHover(String text, Runnable action, String normalStyle, String hoverStyle) {
        Button button = new Button(text);
        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
        if (action != null) {
            button.setOnAction(e -> action.run());
        }
        return button;
    }
}


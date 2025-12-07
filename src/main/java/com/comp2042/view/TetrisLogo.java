package com.comp2042.view;

import javafx.animation.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * Animated logo component displaying "TETRIS" with interactive effects.
 * Features custom font loading, beveled gradients, glow effects, and hover/click animations.
 */
public class TetrisLogo extends HBox {
    private static final double FONT_SIZE = 100;
    private Text[] letters;
    private Color[] letterColors;
    private Font tetricideFont;
    
    /**
     * Constructs a TetrisLogo with animated letters and interactive effects.
     * Loads custom font, creates styled letters, and sets up hover/click animations.
     */
    public TetrisLogo() {
        super(5);
        setAlignment(javafx.geometry.Pos.CENTER);
        
        letterColors = new Color[]{
            Color.CYAN,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.LIME,
            Color.RED
        };
        
        loadFont();
        createLogo();
        setupInteractivity();
    }
    
    private void loadFont() {
        try {
            InputStream fontStream = getClass().getClassLoader()
                .getResourceAsStream("tetricide-brk/tetri.ttf");
            if (fontStream != null) {
                tetricideFont = Font.loadFont(fontStream, FONT_SIZE);
                fontStream.close();
            } else {
                tetricideFont = Font.font("Arial", FONT_SIZE);
            }
        } catch (Exception e) {
            System.err.println("Could not load Tetricide font: " + e.getMessage());
            tetricideFont = Font.font("Arial", FONT_SIZE);
        }
    }
    
    private void createLogo() {
        String word = "TETRIS";
        letters = new Text[word.length()];
        
        for (int i = 0; i < word.length(); i++) {
            Text letter = new Text(String.valueOf(word.charAt(i)));
            letter.setFont(tetricideFont);
            letter.setFill(createBeveledGradient(letterColors[i]));
            letter.setScaleX(1.0);
            letter.setScaleY(1.0);
            
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.BLACK);
            dropShadow.setRadius(8);
            dropShadow.setOffsetX(3);
            dropShadow.setOffsetY(3);
            
            Glow glow = new Glow(0.5);
            glow.setInput(dropShadow);
            
            letter.setEffect(glow);
            letter.setStyle("-fx-font-weight: bold;");
            
            letters[i] = letter;
            getChildren().add(letter);
        }
    }
    
    private LinearGradient createBeveledGradient(Color baseColor) {
        Color lighter = baseColor.deriveColor(0, 1, 1.3, 1);
        Color darker = baseColor.deriveColor(0, 1, 0.7, 1);
        
        return new LinearGradient(
            0, 0, 1, 1,
            true,
            javafx.scene.paint.CycleMethod.NO_CYCLE,
            new Stop(0, lighter),
            new Stop(0.5, baseColor),
            new Stop(1, darker)
        );
    }
    
    private void setupInteractivity() {
        for (int i = 0; i < letters.length; i++) {
            final Text letter = letters[i];
            
            letter.setOnMouseEntered(e -> {
                ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), letter);
                scaleUp.setFromX(letter.getScaleX());
                scaleUp.setFromY(letter.getScaleY());
                scaleUp.setToX(1.2);
                scaleUp.setToY(1.2);
                scaleUp.play();
                
                Glow glow = (Glow) letter.getEffect();
                if (glow != null) {
                    glow.setLevel(0.8);
                }
            });
            
            letter.setOnMouseExited(e -> {
                ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), letter);
                scaleDown.setFromX(letter.getScaleX());
                scaleDown.setFromY(letter.getScaleY());
                scaleDown.setToX(1.0);
                scaleDown.setToY(1.0);
                scaleDown.play();
                
                Glow glow = (Glow) letter.getEffect();
                if (glow != null) {
                    glow.setLevel(0.5);
                }
            });
            
            letter.setOnMouseClicked(e -> {
                ScaleTransition scale1 = new ScaleTransition(Duration.millis(100), letter);
                scale1.setFromX(letter.getScaleX());
                scale1.setFromY(letter.getScaleY());
                scale1.setToX(0.9);
                scale1.setToY(0.9);
                
                ScaleTransition scale2 = new ScaleTransition(Duration.millis(100), letter);
                scale2.setFromX(0.9);
                scale2.setFromY(0.9);
                scale2.setToX(1.0);
                scale2.setToY(1.0);
                
                SequentialTransition sequence = new SequentialTransition(scale1, scale2);
                sequence.play();
            });
        }
        
        startIdleAnimation();
    }
    
    private void createClickAnimation(Text letter) {
        ScaleTransition scale1 = new ScaleTransition(Duration.millis(100), letter);
        scale1.setToX(0.9);
        scale1.setToY(0.9);
        
        ScaleTransition scale2 = new ScaleTransition(Duration.millis(100), letter);
        scale2.setToX(1.0);
        scale2.setToY(1.0);
        
        SequentialTransition sequence = new SequentialTransition(scale1, scale2);
        sequence.play();
    }
    
    private void startIdleAnimation() {
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            Text letter = letters[i];
            
            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(2000 + i * 300),
                e -> {
                    FadeTransition fade = new FadeTransition(Duration.millis(300), letter);
                    fade.setFromValue(1.0);
                    fade.setToValue(0.7);
                    fade.setAutoReverse(true);
                    fade.setCycleCount(2);
                    fade.play();
                }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}


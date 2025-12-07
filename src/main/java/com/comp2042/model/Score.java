package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages game score, lines cleared, and combo tracking.
 * Uses JavaFX properties for reactive UI updates when values change.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty lines = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);

    /**
     * Gets the score property for reactive UI binding.
     * @return IntegerProperty representing the current score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the lines property for reactive UI binding.
     * @return IntegerProperty representing the number of lines cleared
     */
    public IntegerProperty linesProperty() {
        return lines;
    }

    /**
     * Adds points to the current score.
     * @param i Points to add to the score
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Adds to the count of lines cleared.
     * @param i Number of lines to add
     */
    public void addLines(int i) {
        lines.setValue(lines.getValue() + i);
    }

    /**
     * Resets all score values to zero (score, lines, and combo).
     */
    public void reset() {
        score.setValue(0);
        lines.setValue(0);
        combo.setValue(0);
    }

    /**
     * Resets the lines cleared count to zero.
     */
    public void resetLines() {
        lines.setValue(0);
    }

    /**
     * Gets the current score value.
     * @return Current score as an integer
     */
    public int getValue() {
        return scoreProperty().get();
    }

    /**
     * Gets the current lines cleared count.
     * @return Number of lines cleared as an integer
     */
    public int getLinesValue() {
        return linesProperty().get();
    }

    /**
     * Increments the combo counter by one.
     * Used when consecutive line clears occur.
     */
    public void incrementCombo() {
        combo.setValue(combo.getValue() + 1);
    }

    /**
     * Resets the combo counter to zero.
     * Called when a line clear sequence is broken.
     */
    public void resetCombo() {
        combo.setValue(0);
    }

    /**
     * Gets the current combo value.
     * @return Current combo count as an integer
     */
    public int getCombo() {
        return combo.get();
    }

    /**
     * Gets the combo property for reactive UI binding.
     * @return IntegerProperty representing the current combo count
     */
    public IntegerProperty comboProperty() {
        return combo;
    }
}

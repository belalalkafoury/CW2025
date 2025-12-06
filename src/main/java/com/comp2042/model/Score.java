package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty lines = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty linesProperty() {
        return lines;
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void addLines(int i) {
        lines.setValue(lines.getValue() + i);
    }

    public void reset() {
        score.setValue(0);
        lines.setValue(0);
        combo.setValue(0);
    }

    public void resetLines() {
        lines.setValue(0);
    }

    public int getValue() {
        return scoreProperty().get();
    }

    public int getLinesValue() {
        return linesProperty().get();
    }

    public void incrementCombo() {
        combo.setValue(combo.getValue() + 1);
    }

    public void resetCombo() {
        combo.setValue(0);
    }

    public int getCombo() {
        return combo.get();
    }

    public IntegerProperty comboProperty() {
        return combo;
    }
}

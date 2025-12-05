package com.comp2042.model;

public class HighScoreEntry {
    private final String name;
    private final int score;

    public HighScoreEntry(String name, int score) {
        this.name = name != null ? name.toUpperCase() : "PLAYER";
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}


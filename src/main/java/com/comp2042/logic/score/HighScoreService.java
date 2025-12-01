package com.comp2042.logic.score;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HighScoreService {
    private static final String HIGHSCORE_FILE = "highscore.dat";
    private int highScore = 0;

    public HighScoreService() {
        loadHighScore();
    }

    public int getHighScore() {
        return highScore;
    }

    public boolean updateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
            saveHighScore();
            return true;
        }
        return false;
    }

    private void loadHighScore() {
        Path filePath = Paths.get(HIGHSCORE_FILE);
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    highScore = Integer.parseInt(line.trim());
                }
            } catch (IOException | NumberFormatException e) {
                highScore = 0;
            }
        }
    }

    private void saveHighScore() {
        Path filePath = Paths.get(HIGHSCORE_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println("Failed to save highscore: " + e.getMessage());
        }
    }

    public void reset() {
        highScore = 0;
        saveHighScore();
    }
}


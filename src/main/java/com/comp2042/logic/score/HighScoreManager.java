package com.comp2042.logic.score;

import com.comp2042.model.GameMode;
import com.comp2042.model.HighScoreEntry;
import com.comp2042.util.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HighScoreManager {
    private static final String HIGHSCORE_FILE = "highscores.dat";
    private static final int MAX_SCORES_PER_MODE = 10;
    
    private final Map<GameMode, List<HighScoreEntry>> scoresByMode;

    public HighScoreManager() {
        scoresByMode = new HashMap<>();
        for (GameMode mode : GameMode.values()) {
            scoresByMode.put(mode, new ArrayList<>());
        }
        loadScores();
    }

    public void addScore(GameMode mode, String name, int score) {
        if (mode == null) {
            mode = GameMode.CLASSIC;
        }
        
        List<HighScoreEntry> scores = scoresByMode.get(mode);
        if (scores == null) {
            scores = new ArrayList<>();
            scoresByMode.put(mode, scores);
        }
        
        boolean found = false;
        for (int i = 0; i < scores.size(); i++) {
            HighScoreEntry entry = scores.get(i);
            if (entry.getName().equalsIgnoreCase(name)) {
                if (score > entry.getScore()) {
                    scores.set(i, new HighScoreEntry(name, score));
                }
                found = true;
                break;
            }
        }
        
        if (!found) {
            scores.add(new HighScoreEntry(name, score));
        }
        
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        
        while (scores.size() > MAX_SCORES_PER_MODE) {
            scores.remove(scores.size() - 1);
        }
        
        saveScores();
    }

    public List<HighScoreEntry> getTopScores(GameMode mode) {
        if (mode == null) {
            mode = GameMode.CLASSIC;
        }
        List<HighScoreEntry> scores = scoresByMode.get(mode);
        return scores != null ? new ArrayList<>(scores) : new ArrayList<>();
    }

    public HighScoreEntry getHighestScore(GameMode mode) {
        if (mode == null) {
            mode = GameMode.CLASSIC;
        }
        List<HighScoreEntry> scores = scoresByMode.get(mode);
        if (scores != null && !scores.isEmpty()) {
            return scores.get(0);
        }
        return null;
    }

    public int getHighestScoreValue(GameMode mode) {
        HighScoreEntry entry = getHighestScore(mode);
        return entry != null ? entry.getScore() : 0;
    }

    private void loadScores() {
        Path filePath = Paths.get(HIGHSCORE_FILE);
        if (!Files.exists(filePath)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(":");
                if (parts.length >= 3) {
                    try {
                        GameMode mode = GameMode.valueOf(parts[0]);
                        String name = parts[1];
                        int score = Integer.parseInt(parts[2]);
                        addScoreInternal(mode, name, score);
                    } catch (IllegalArgumentException e) {
                        Logger.warn("Invalid high score line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Failed to load highscores: " + e.getMessage());
        }
    }

    private void addScoreInternal(GameMode mode, String name, int score) {
        List<HighScoreEntry> scores = scoresByMode.get(mode);
        if (scores == null) {
            scores = new ArrayList<>();
            scoresByMode.put(mode, scores);
        }
        scores.add(new HighScoreEntry(name, score));
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        while (scores.size() > MAX_SCORES_PER_MODE) {
            scores.remove(scores.size() - 1);
        }
    }

    private void saveScores() {
        Path filePath = Paths.get(HIGHSCORE_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Map.Entry<GameMode, List<HighScoreEntry>> entry : scoresByMode.entrySet()) {
                GameMode mode = entry.getKey();
                for (HighScoreEntry scoreEntry : entry.getValue()) {
                    writer.write(mode.name() + ":" + scoreEntry.getName() + ":" + scoreEntry.getScore());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            Logger.error("Failed to save highscores: " + e.getMessage());
        }
    }

    public void resetHighScore(GameMode mode) {
        if (mode != null) {
            scoresByMode.put(mode, new ArrayList<>());
        } else {
            scoresByMode.clear();
            for (GameMode m : GameMode.values()) {
                scoresByMode.put(m, new ArrayList<>());
            }
        }
        saveScores();
    }

    public void resetAllHighScores() {
        resetHighScore(null);
    }
}


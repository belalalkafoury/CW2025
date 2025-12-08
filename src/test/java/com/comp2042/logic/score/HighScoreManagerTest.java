package com.comp2042.logic.score;

import com.comp2042.model.GameMode;
import com.comp2042.model.HighScoreEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HighScoreManagerTest {

    private HighScoreManager manager;
    private static final String TEST_HIGHSCORE_FILE = "test_highscores.dat";

    @BeforeEach
    void setUp() {
        deleteTestFile();
        manager = new HighScoreManager();
    }

    private void deleteTestFile() {
        try {
            Path path = Paths.get("highscores.dat");
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
        }
    }

    @Test
    void testAddScore() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(1, scores.size());
        assertEquals("PLAYER1", scores.get(0).getName());
        assertEquals(1000, scores.get(0).getScore());
    }

    @Test
    void testAddMultipleScores() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        manager.addScore(GameMode.CLASSIC, "PLAYER2", 2000);
        manager.addScore(GameMode.CLASSIC, "PLAYER3", 1500);
        
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(3, scores.size());
        assertEquals(2000, scores.get(0).getScore());
        assertEquals(1500, scores.get(1).getScore());
        assertEquals(1000, scores.get(2).getScore());
    }

    @Test
    void testScoresAreSortedDescending() {
        manager.addScore(GameMode.CLASSIC, "LOW", 100);
        manager.addScore(GameMode.CLASSIC, "HIGH", 500);
        manager.addScore(GameMode.CLASSIC, "MED", 300);
        
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(500, scores.get(0).getScore());
        assertEquals(300, scores.get(1).getScore());
        assertEquals(100, scores.get(2).getScore());
    }

    @Test
    void testUpdateExistingPlayerScore() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 2000);
        
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(1, scores.size());
        assertEquals(2000, scores.get(0).getScore());
    }

    @Test
    void testUpdateExistingPlayerScoreLower() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 2000);
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(1, scores.size());
        assertEquals(2000, scores.get(0).getScore());
    }

    @Test
    void testMaxScoresPerMode() {
        for (int i = 1; i <= 15; i++) {
            manager.addScore(GameMode.CLASSIC, "PLAYER" + i, i * 100);
        }
        
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(10, scores.size());
        assertEquals(1500, scores.get(0).getScore());
        assertEquals(600, scores.get(9).getScore());
    }

    @Test
    void testGetTopScoresDifferentModes() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        manager.addScore(GameMode.TIME_ATTACK, "PLAYER2", 2000);
        manager.addScore(GameMode.PUZZLE, "PLAYER3", 1500);
        
        assertEquals(1, manager.getTopScores(GameMode.CLASSIC).size());
        assertEquals(1, manager.getTopScores(GameMode.TIME_ATTACK).size());
        assertEquals(1, manager.getTopScores(GameMode.PUZZLE).size());
    }

    @Test
    void testGetHighestScore() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        manager.addScore(GameMode.CLASSIC, "PLAYER2", 2000);
        
        HighScoreEntry highest = manager.getHighestScore(GameMode.CLASSIC);
        
        assertNotNull(highest);
        assertEquals(2000, highest.getScore());
    }

    @Test
    void testGetHighestScoreEmpty() {
        HighScoreEntry highest = manager.getHighestScore(GameMode.CLASSIC);
        assertNull(highest);
    }

    @Test
    void testGetHighestScoreValue() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 5000);
        assertEquals(5000, manager.getHighestScoreValue(GameMode.CLASSIC));
    }

    @Test
    void testGetHighestScoreValueEmpty() {
        assertEquals(0, manager.getHighestScoreValue(GameMode.CLASSIC));
    }

    @Test
    void testResetHighScoreForMode() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        manager.addScore(GameMode.TIME_ATTACK, "PLAYER2", 2000);
        
        manager.resetHighScore(GameMode.CLASSIC);
        
        assertEquals(0, manager.getTopScores(GameMode.CLASSIC).size());
        assertEquals(1, manager.getTopScores(GameMode.TIME_ATTACK).size());
    }

    @Test
    void testResetAllHighScores() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        manager.addScore(GameMode.TIME_ATTACK, "PLAYER2", 2000);
        
        manager.resetAllHighScores();
        
        assertEquals(0, manager.getTopScores(GameMode.CLASSIC).size());
        assertEquals(0, manager.getTopScores(GameMode.TIME_ATTACK).size());
    }

    @Test
    void testAddScoreWithNullMode() {
        manager.addScore(null, "PLAYER1", 1000);
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(1, scores.size());
    }

    @Test
    void testGetTopScoresWithNullMode() {
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 1000);
        List<HighScoreEntry> scores = manager.getTopScores(null);
        
        assertEquals(1, scores.size());
    }

    @Test
    void testCaseInsensitiveNameMatching() {
        manager.addScore(GameMode.CLASSIC, "player1", 1000);
        manager.addScore(GameMode.CLASSIC, "PLAYER1", 2000);
        
        List<HighScoreEntry> scores = manager.getTopScores(GameMode.CLASSIC);
        
        assertEquals(1, scores.size());
        assertEquals(2000, scores.get(0).getScore());
    }
}



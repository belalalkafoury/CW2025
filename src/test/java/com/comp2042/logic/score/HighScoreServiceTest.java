package com.comp2042.logic.score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HighScoreServiceTest {

    private HighScoreService service;
    private static final String TEST_FILE = "highscore.dat";

    @BeforeEach
    void setUp() {
        deleteTestFile();
        service = new HighScoreService();
    }

    private void deleteTestFile() {
        try {
            Path path = Paths.get(TEST_FILE);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
        }
    }

    @Test
    void testInitialHighScoreIsZero() {
        assertEquals(0, service.getHighScore());
    }

    @Test
    void testUpdateHighScoreWithHigherValue() {
        boolean updated = service.updateHighScore(1000);
        
        assertTrue(updated, "Should update when new score is higher");
        assertEquals(1000, service.getHighScore());
    }

    @Test
    void testUpdateHighScoreWithLowerValue() {
        service.updateHighScore(1000);
        boolean updated = service.updateHighScore(500);
        
        assertFalse(updated, "Should not update when new score is lower");
        assertEquals(1000, service.getHighScore());
    }

    @Test
    void testUpdateHighScoreWithEqualValue() {
        service.updateHighScore(1000);
        boolean updated = service.updateHighScore(1000);
        
        assertFalse(updated, "Should not update when new score is equal");
        assertEquals(1000, service.getHighScore());
    }

    @Test
    void testUpdateHighScoreMultipleTimes() {
        service.updateHighScore(500);
        service.updateHighScore(1000);
        service.updateHighScore(1500);
        service.updateHighScore(1200);
        
        assertEquals(1500, service.getHighScore());
    }

    @Test
    void testReset() {
        service.updateHighScore(5000);
        service.reset();
        
        assertEquals(0, service.getHighScore());
    }

    @Test
    void testResetHighScore() {
        service.updateHighScore(3000);
        service.resetHighScore();
        
        assertEquals(0, service.getHighScore());
    }

    @Test
    void testUpdateHighScoreWithZero() {
        service.updateHighScore(1000);
        boolean updated = service.updateHighScore(0);
        
        assertFalse(updated);
        assertEquals(1000, service.getHighScore());
    }

    @Test
    void testUpdateHighScoreWithNegative() {
        service.updateHighScore(1000);
        boolean updated = service.updateHighScore(-100);
        
        assertFalse(updated);
        assertEquals(1000, service.getHighScore());
    }

    @Test
    void testPersistenceAfterReset() {
        service.updateHighScore(5000);
        service.reset();
        
        HighScoreService newService = new HighScoreService();
        assertEquals(0, newService.getHighScore());
    }
}



package com.comp2042.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HighScoreEntryTest {

    @Test
    void testConstructorWithValidName() {
        HighScoreEntry entry = new HighScoreEntry("PLAYER1", 1000);
        assertEquals("PLAYER1", entry.getName());
        assertEquals(1000, entry.getScore());
    }

    @Test
    void testConstructorConvertsNameToUpperCase() {
        HighScoreEntry entry = new HighScoreEntry("player1", 500);
        assertEquals("PLAYER1", entry.getName());
    }

    @Test
    void testConstructorWithNullName() {
        HighScoreEntry entry = new HighScoreEntry(null, 200);
        assertEquals("PLAYER", entry.getName());
        assertEquals(200, entry.getScore());
    }

    @Test
    void testConstructorWithEmptyName() {
        HighScoreEntry entry = new HighScoreEntry("", 300);
        assertEquals("", entry.getName(), "Empty string should remain empty (not converted to PLAYER)");
    }

    @Test
    void testConstructorWithZeroScore() {
        HighScoreEntry entry = new HighScoreEntry("TEST", 0);
        assertEquals(0, entry.getScore());
    }

    @Test
    void testConstructorWithNegativeScore() {
        HighScoreEntry entry = new HighScoreEntry("TEST", -100);
        assertEquals(-100, entry.getScore());
    }

    @Test
    void testConstructorWithLargeScore() {
        HighScoreEntry entry = new HighScoreEntry("CHAMP", 999999);
        assertEquals(999999, entry.getScore());
    }

    @Test
    void testGetName() {
        HighScoreEntry entry = new HighScoreEntry("ALICE", 5000);
        String name = entry.getName();
        assertEquals("ALICE", name);
    }

    @Test
    void testGetScore() {
        HighScoreEntry entry = new HighScoreEntry("BOB", 7500);
        int score = entry.getScore();
        assertEquals(7500, score);
    }

    @Test
    void testNameWithSpaces() {
        HighScoreEntry entry = new HighScoreEntry("Player One", 100);
        assertEquals("PLAYER ONE", entry.getName());
    }

    @Test
    void testNameWithSpecialCharacters() {
        HighScoreEntry entry = new HighScoreEntry("Player_123", 200);
        assertEquals("PLAYER_123", entry.getName());
    }
}


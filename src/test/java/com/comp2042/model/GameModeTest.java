package com.comp2042.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameModeTest {

    @Test
    void testGameModeValues() {
        GameMode[] modes = GameMode.values();
        
        assertEquals(4, modes.length, "Should have 4 game modes");
        assertTrue(containsMode(modes, GameMode.CLASSIC));
        assertTrue(containsMode(modes, GameMode.TIME_ATTACK));
        assertTrue(containsMode(modes, GameMode.PUZZLE));
        assertTrue(containsMode(modes, GameMode.REVERTED));
    }

    @Test
    void testGameModeValueOf() {
        assertEquals(GameMode.CLASSIC, GameMode.valueOf("CLASSIC"));
        assertEquals(GameMode.TIME_ATTACK, GameMode.valueOf("TIME_ATTACK"));
        assertEquals(GameMode.PUZZLE, GameMode.valueOf("PUZZLE"));
        assertEquals(GameMode.REVERTED, GameMode.valueOf("REVERTED"));
    }

    @Test
    void testGameModeValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameMode.valueOf("INVALID");
        });
    }

    @Test
    void testGameModeEquality() {
        GameMode mode1 = GameMode.CLASSIC;
        GameMode mode2 = GameMode.CLASSIC;
        
        assertEquals(mode1, mode2);
        assertSame(mode1, mode2);
    }

    @Test
    void testGameModeInequality() {
        assertNotEquals(GameMode.CLASSIC, GameMode.TIME_ATTACK);
        assertNotEquals(GameMode.PUZZLE, GameMode.REVERTED);
    }

    @Test
    void testGameModeToString() {
        assertEquals("CLASSIC", GameMode.CLASSIC.toString());
        assertEquals("TIME_ATTACK", GameMode.TIME_ATTACK.toString());
        assertEquals("PUZZLE", GameMode.PUZZLE.toString());
        assertEquals("REVERTED", GameMode.REVERTED.toString());
    }

    private boolean containsMode(GameMode[] modes, GameMode target) {
        for (GameMode mode : modes) {
            if (mode == target) {
                return true;
            }
        }
        return false;
    }
}


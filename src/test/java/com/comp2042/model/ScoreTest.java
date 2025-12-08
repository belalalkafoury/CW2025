package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScoreIsZero() {
        assertEquals(0, score.getValue(), "Initial score should be 0");
    }

    @Test
    void testInitialLinesIsZero() {
        assertEquals(0, score.getLinesValue(), "Initial lines should be 0");
    }

    @Test
    void testInitialComboIsZero() {
        assertEquals(0, score.getCombo(), "Initial combo should be 0");
    }

    @Test
    void testAddPoints() {
        score.add(100);
        assertEquals(100, score.getValue(), "Score should be 100 after adding 100");
        
        score.add(50);
        assertEquals(150, score.getValue(), "Score should be 150 after adding 50 more");
    }

    @Test
    void testAddLines() {
        score.addLines(1);
        assertEquals(1, score.getLinesValue(), "Lines should be 1 after adding 1");
        
        score.addLines(3);
        assertEquals(4, score.getLinesValue(), "Lines should be 4 after adding 3 more");
    }

    @Test
    void testIncrementCombo() {
        score.incrementCombo();
        assertEquals(1, score.getCombo(), "Combo should be 1 after incrementing");
        
        score.incrementCombo();
        score.incrementCombo();
        assertEquals(3, score.getCombo(), "Combo should be 3 after incrementing twice more");
    }

    @Test
    void testResetCombo() {
        score.incrementCombo();
        score.incrementCombo();
        score.resetCombo();
        assertEquals(0, score.getCombo(), "Combo should be 0 after reset");
    }

    @Test
    void testReset() {
        score.add(100);
        score.addLines(5);
        score.incrementCombo();
        
        score.reset();
        
        assertEquals(0, score.getValue(), "Score should be 0 after reset");
        assertEquals(0, score.getLinesValue(), "Lines should be 0 after reset");
        assertEquals(0, score.getCombo(), "Combo should be 0 after reset");
    }

    @Test
    void testResetLines() {
        score.addLines(10);
        score.resetLines();
        assertEquals(0, score.getLinesValue(), "Lines should be 0 after resetLines");
        
        score.add(50);
        assertEquals(50, score.getValue(), "Score should remain unchanged after resetLines");
    }

    @Test
    void testScorePropertyBinding() {
        score.add(25);
        assertEquals(25, score.scoreProperty().get(), "Score property should reflect current value");
    }

    @Test
    void testLinesPropertyBinding() {
        score.addLines(7);
        assertEquals(7, score.linesProperty().get(), "Lines property should reflect current value");
    }

    @Test
    void testComboPropertyBinding() {
        score.incrementCombo();
        assertEquals(1, score.comboProperty().get(), "Combo property should reflect current value");
    }

    @Test
    void testAddNegativePoints() {
        score.add(100);
        score.add(-50);
        assertEquals(50, score.getValue(), "Score should handle negative additions");
    }

    @Test
    void testAddZeroPoints() {
        score.add(100);
        score.add(0);
        assertEquals(100, score.getValue(), "Adding zero should not change score");
    }
}



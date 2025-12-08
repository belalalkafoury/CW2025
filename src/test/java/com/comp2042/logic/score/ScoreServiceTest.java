package com.comp2042.logic.score;

import com.comp2042.model.Score;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.controller.MoveEvent;
import com.comp2042.controller.EventSource;
import com.comp2042.controller.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceTest {

    private Score score;
    private ScoreService scoreService;

    @BeforeEach
    void setUp() {
        score = new Score();
        scoreService = new ScoreService(score);
    }

    @Test
    void testReset() {
        score.add(100);
        score.addLines(5);
        
        scoreService.reset();
        
        assertEquals(0, score.getValue(), "Score should be reset to 0");
        assertEquals(0, score.getLinesValue(), "Lines should be reset to 0");
    }

    @Test
    void testApplySoftDropUserSource() {
        MoveEvent userEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        
        scoreService.applySoftDrop(userEvent);
        
        assertEquals(1, score.getValue(), "Should award 1 point for user soft drop");
    }

    @Test
    void testApplySoftDropThreadSource() {
        MoveEvent threadEvent = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        
        scoreService.applySoftDrop(threadEvent);
        
        assertEquals(0, score.getValue(), "Should not award points for thread soft drop");
    }

    @Test
    void testApplyHardDrop() {
        scoreService.applyHardDrop(5);
        assertEquals(10, score.getValue(), "Should award 2 points per cell (5 * 2 = 10)");
        
        scoreService.applyHardDrop(3);
        assertEquals(16, score.getValue(), "Should add 6 more points (3 * 2 = 6)");
    }

    @Test
    void testApplyHardDropZeroDistance() {
        scoreService.applyHardDrop(0);
        assertEquals(0, score.getValue(), "Zero distance should award zero points");
    }

    @Test
    void testApplyLineClearBonusSingleLine() {
        int[][] newMatrix = new int[10][10];
        List<Integer> clearedIndices = new ArrayList<>();
        clearedIndices.add(9);
        ClearRow clearRow = new ClearRow(1, newMatrix, clearedIndices);
        
        scoreService.applyLineClearBonus(clearRow);
        
        int expectedPoints = 50 * 1 * 1 * (1 + 0);
        assertEquals(expectedPoints, score.getValue(), "Should award 50 points for single line clear");
        assertEquals(1, score.getLinesValue(), "Should increment lines cleared");
    }

    @Test
    void testApplyLineClearBonusDoubleLine() {
        int[][] newMatrix = new int[10][10];
        List<Integer> clearedIndices = new ArrayList<>();
        clearedIndices.add(8);
        clearedIndices.add(9);
        ClearRow clearRow = new ClearRow(2, newMatrix, clearedIndices);
        
        scoreService.applyLineClearBonus(clearRow);
        
        int expectedPoints = 50 * 2 * 2 * (1 + 0);
        assertEquals(expectedPoints, score.getValue(), "Should award 200 points for double line clear");
        assertEquals(2, score.getLinesValue(), "Should increment lines cleared by 2");
    }

    @Test
    void testApplyLineClearBonusWithCombo() {
        score.incrementCombo();
        score.incrementCombo();
        
        int[][] newMatrix = new int[10][10];
        List<Integer> clearedIndices = new ArrayList<>();
        clearedIndices.add(9);
        ClearRow clearRow = new ClearRow(1, newMatrix, clearedIndices);
        
        scoreService.applyLineClearBonus(clearRow);
        
        int expectedPoints = 50 * 1 * 1 * (1 + 2);
        assertEquals(expectedPoints, score.getValue(), "Should multiply by combo (1 + 2 = 3)");
    }

    @Test
    void testApplyLineClearBonusNullClearRow() {
        scoreService.applyLineClearBonus(null);
        assertEquals(0, score.getValue(), "Null clearRow should not change score");
    }

    @Test
    void testApplyLineClearBonusZeroLines() {
        int[][] newMatrix = new int[10][10];
        List<Integer> clearedIndices = new ArrayList<>();
        ClearRow clearRow = new ClearRow(0, newMatrix, clearedIndices);
        
        scoreService.applyLineClearBonus(clearRow);
        
        assertEquals(0, score.getValue(), "Zero lines cleared should not award points");
    }

    @Test
    void testGetScore() {
        Score returnedScore = scoreService.getScore();
        assertSame(score, returnedScore, "Should return the same Score instance");
    }

    @Test
    void testApplyLineClearBonusTripleLine() {
        int[][] newMatrix = new int[10][10];
        List<Integer> clearedIndices = new ArrayList<>();
        clearedIndices.add(7);
        clearedIndices.add(8);
        clearedIndices.add(9);
        ClearRow clearRow = new ClearRow(3, newMatrix, clearedIndices);
        
        scoreService.applyLineClearBonus(clearRow);
        
        int expectedPoints = 50 * 3 * 3 * (1 + 0);
        assertEquals(expectedPoints, score.getValue(), "Should award 450 points for triple line clear");
    }

    @Test
    void testApplyLineClearBonusTetris() {
        int[][] newMatrix = new int[10][10];
        List<Integer> clearedIndices = new ArrayList<>();
        clearedIndices.add(6);
        clearedIndices.add(7);
        clearedIndices.add(8);
        clearedIndices.add(9);
        ClearRow clearRow = new ClearRow(4, newMatrix, clearedIndices);
        
        scoreService.applyLineClearBonus(clearRow);
        
        int expectedPoints = 50 * 4 * 4 * (1 + 0);
        assertEquals(expectedPoints, score.getValue(), "Should award 800 points for Tetris (4 lines)");
        assertEquals(4, score.getLinesValue(), "Should increment lines cleared by 4");
    }
}


package com.comp2042.logic.score;

import com.comp2042.model.Score;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.controller.MoveEvent;
import com.comp2042.controller.EventSource;

/**
 * Service for calculating and applying scoring based on game actions.
 * Implements the scoring formula: base points = 50 * lines^2, multiplied by (1 + combo).
 * Awards points for soft drops (1 point per cell) and hard drops (2 points per cell).
 */
public class ScoreService {
    private final Score score;

    /**
     * Constructs a ScoreService with the specified Score object.
     * @param score The Score object to manage
     */
    public ScoreService(Score score) {
        this.score = score;
    }

    /**
     * Resets the score and lines cleared count.
     */
    public void reset() {
        score.reset();
        score.resetLines();
    }

    /**
     * Applies scoring for a soft drop (manual downward movement).
     * Awards 1 point per cell moved, only if the move was user-initiated.
     * @param event The move event containing information about the drop
     */
    public void applySoftDrop(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            score.add(1);
        }
    }

    /**
     * Applies scoring for a hard drop (instant drop to bottom).
     * Awards 2 points per cell dropped.
     * @param distance The number of rows the brick was dropped
     */
    public void applyHardDrop(int distance) {
        score.add(distance * 2);
    }

    /**
     * Applies scoring for line clears using the formula: 50 * lines^2 * (1 + combo).
     * Also increments the combo counter and updates lines cleared count.
     * @param cleared ClearRow object containing information about the cleared lines
     */
    public void applyLineClearBonus(ClearRow cleared) {
        if (cleared != null && cleared.getLinesRemoved() > 0) {
            int basePoints = 50 * cleared.getLinesRemoved() * cleared.getLinesRemoved();
            int comboMultiplier = 1 + score.getCombo();
            int points = basePoints * comboMultiplier;
            score.add(points);
            score.addLines(cleared.getLinesRemoved());
        }
    }

    /**
     * Gets the Score object being managed.
     * @return The Score object
     */
    public Score getScore() {
        return score;
    }
}

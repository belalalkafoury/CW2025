package com.comp2042.logic.score;

import com.comp2042.model.Score;
import com.comp2042.logic.board.ClearRow;
import com.comp2042.controller.MoveEvent;
import com.comp2042.controller.EventSource;

public class ScoreService {
    private final Score score;

    public ScoreService(Score score) {
        this.score = score;
    }

    public void reset() {
        score.reset();
        score.resetLines();
    }

    public void applySoftDrop(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            score.add(1);
        }
    }

    public void applyHardDrop(int distance) {
        score.add(distance * 2);
    }

    public void applyLineClearBonus(ClearRow cleared) {
        if (cleared != null && cleared.getLinesRemoved() > 0) {
            int basePoints = 50 * cleared.getLinesRemoved() * cleared.getLinesRemoved();
            int comboMultiplier = 1 + score.getCombo();
            int points = basePoints * comboMultiplier;
            score.add(points);
            score.addLines(cleared.getLinesRemoved());
        }
    }

    public Score getScore() {
        return score;
    }
}

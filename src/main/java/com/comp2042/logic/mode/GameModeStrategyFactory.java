package com.comp2042.logic.mode;

import com.comp2042.model.GameMode;

/**
 * Factory for creating game mode strategy instances.
 */
public final class GameModeStrategyFactory {
    
    private GameModeStrategyFactory() {
    }
    
    /**
     * Creates a strategy instance for the specified game mode.
     * @param gameMode The game mode to create a strategy for
     * @return A GameModeStrategy instance for the specified mode
     */
    public static GameModeStrategy createStrategy(GameMode gameMode) {
        switch (gameMode) {
            case TIME_ATTACK:
                return new TimeAttackModeStrategy();
            case PUZZLE:
                return new PuzzleModeStrategy();
            case REVERTED:
                return new RevertedModeStrategy();
            case CLASSIC:
            default:
                return new ClassicModeStrategy();
        }
    }
}


package com.comp2042.logic.mode;

import com.comp2042.controller.GuiController;
import com.comp2042.controller.SoundController;
import com.comp2042.model.Board;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

/**
 * Strategy for Time Attack mode.
 * Player must reach a target score within a time limit.
 */
public class TimeAttackModeStrategy implements GameModeStrategy {
    
    private int targetScore;
    private int secondsRemaining;
    private Timeline gameTimer;
    private boolean isActive = false;
    private boolean gameWon = false;
    private boolean gameLost = false;
    private GuiController guiController;
    
    @Override
    public void initialize(Board board, GuiController guiController, SoundController soundController) {
        this.guiController = guiController;
        Random random = new Random();
        
        secondsRemaining = 30 + random.nextInt(91);
        
        int pointsPerSecond = 30 + random.nextInt(21);
        
        targetScore = secondsRemaining * pointsPerSecond;
        targetScore = ((targetScore + 50) / 100) * 100;
        
        if (targetScore == 0) {
            targetScore = 100;
        }
        
        guiController.configureTimeAttackMode(secondsRemaining, targetScore);
        
        gameTimer = new Timeline(new KeyFrame(
            Duration.seconds(1),
            e -> {
                secondsRemaining--;
                guiController.updateTimer(secondsRemaining);
                
                if (secondsRemaining <= 0) {
                    gameLost = true;
                }
            }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        isActive = true;
    }
    
    @Override
    public boolean checkWinCondition(Board board, GuiController guiController, SoundController soundController) {
        if (board.getScore().getValue() >= targetScore && !gameWon && !gameLost) {
            gameWon = true;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkLossCondition(Board board, GuiController guiController, SoundController soundController) {
        if (secondsRemaining <= 0 && !gameWon && !gameLost) {
            gameLost = true;
            return true;
        }
        return false;
    }
    
    @Override
    public void reset(Board board, GuiController guiController) {
        gameWon = false;
        gameLost = false;
        Random random = new Random();
        
        secondsRemaining = 30 + random.nextInt(91);
        
        int pointsPerSecond = 30 + random.nextInt(21);
        
        targetScore = secondsRemaining * pointsPerSecond;
        targetScore = ((targetScore + 50) / 100) * 100;
        
        if (targetScore == 0) {
            targetScore = 100;
        }
        
        guiController.configureTimeAttackMode(secondsRemaining, targetScore);
        
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        gameTimer = new Timeline(new KeyFrame(
            Duration.seconds(1),
            e -> {
                secondsRemaining--;
                guiController.updateTimer(secondsRemaining);
                
                if (secondsRemaining <= 0) {
                    gameLost = true;
                }
            }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        isActive = true;
    }
    
    @Override
    public void pause() {
        if (gameTimer != null && isActive) {
            gameTimer.pause();
        }
    }
    
    @Override
    public void resume() {
        if (gameTimer != null && isActive) {
            gameTimer.play();
        }
    }
    
    @Override
    public void stop() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        isActive = false;
    }
    
    /**
     * Starts the timer (called after countdown completes).
     */
    public void startTimer() {
        if (gameTimer != null && isActive) {
            gameTimer.play();
        }
    }
}


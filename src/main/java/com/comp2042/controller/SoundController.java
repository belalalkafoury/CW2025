package com.comp2042.controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundController {
    private AudioClip moveSound;
    private AudioClip rotateSound;
    private AudioClip hardDropSound;
    private AudioClip lineClearSound;
    private AudioClip gameOverSound;
    private AudioClip highScoreSound;
    private AudioClip levelUpSound;
    private AudioClip countdownSound;
    
    private MediaPlayer titleMusicPlayer;
    
    private boolean isMuted = false;

    public SoundController() {
        loadSounds();
    }

    private void loadSounds() {
        try {
            moveSound = loadAudioClip("brick move.mp3");
            rotateSound = loadAudioClip("rotate brick.mp3");
            hardDropSound = loadAudioClip("hard drop.mp3");
            lineClearSound = loadAudioClip("line clear.mp3");
            gameOverSound = loadAudioClip("tetris-gb-25-game-over.mp3");
            highScoreSound = loadAudioClip("highscore.mp3");
            levelUpSound = loadAudioClip("next-level.mp3");
            countdownSound = loadAudioClip("countdown.mp3");
            
            URL titleMusicUrl = getClass().getClassLoader().getResource("title.mp3");
            if (titleMusicUrl != null) {
                Media titleMedia = new Media(titleMusicUrl.toExternalForm());
                titleMusicPlayer = new MediaPlayer(titleMedia);
                titleMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            }
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private AudioClip loadAudioClip(String filename) {
        try {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url != null) {
                return new AudioClip(url.toExternalForm());
            } else {
                System.err.println("Could not find sound file: " + filename);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound file " + filename + ": " + e.getMessage());
        }
        return null;
    }

    public void playMove() {
        if (!isMuted && moveSound != null) {
            moveSound.play();
        }
    }

    public void playRotate() {
        if (!isMuted && rotateSound != null) {
            rotateSound.play();
        }
    }

    public void playHardDrop() {
        if (!isMuted && hardDropSound != null) {
            hardDropSound.play();
        }
    }

    public void playLineClear() {
        if (!isMuted && lineClearSound != null) {
            lineClearSound.play();
        }
    }

    public void playGameOver() {
        if (!isMuted && gameOverSound != null) {
            gameOverSound.play();
        }
    }

    public void playHighScore() {
        if (!isMuted && highScoreSound != null) {
            highScoreSound.play();
        }
    }

    public void playLevelUp() {
        if (!isMuted && levelUpSound != null) {
            levelUpSound.play();
        }
    }

    public void playCountdown() {
        if (!isMuted && countdownSound != null) {
            countdownSound.play();
        }
    }

    public void playTitleMusic() {
        if (!isMuted && titleMusicPlayer != null) {
            titleMusicPlayer.play();
        }
    }

    public void stopTitleMusic() {
        if (titleMusicPlayer != null) {
            titleMusicPlayer.stop();
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            stopTitleMusic();
        } else {
            playTitleMusic();
        }
    }

    public boolean isMuted() {
        return isMuted;
    }
}


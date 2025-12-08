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
    private double soundVolume = 1.0;
    private double musicVolume = 1.0;

    public SoundController() {
        loadSounds();
        soundVolume = com.comp2042.model.GameSettings.getSoundVolume();
        musicVolume = com.comp2042.model.GameSettings.getMusicVolume();
    }

    private void loadSounds() {
        try {
            moveSound = loadAudioClip("sounds/brick move.mp3");
            rotateSound = loadAudioClip("sounds/rotate brick.mp3");
            hardDropSound = loadAudioClip("sounds/hard drop.mp3");
            lineClearSound = loadAudioClip("sounds/line clear.mp3");
            gameOverSound = loadAudioClip("sounds/tetris-gb-25-game-over.mp3");
            highScoreSound = loadAudioClip("sounds/highscore.mp3");
            levelUpSound = loadAudioClip("sounds/next-level.mp3");
            countdownSound = loadAudioClip("sounds/countdown.mp3");
            
            URL titleMusicUrl = getClass().getClassLoader().getResource("sounds/title.mp3");
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
            moveSound.setVolume(soundVolume);
            moveSound.play();
        }
    }

    public void playRotate() {
        if (!isMuted && rotateSound != null) {
            rotateSound.setVolume(soundVolume);
            rotateSound.play();
        }
    }

    public void playHardDrop() {
        if (!isMuted && hardDropSound != null) {
            hardDropSound.setVolume(soundVolume);
            hardDropSound.play();
        }
    }

    public void playLineClear() {
        if (!isMuted && lineClearSound != null) {
            lineClearSound.setVolume(soundVolume);
            lineClearSound.play();
        }
    }

    public void playComboSound(int combo) {
        if (!isMuted && lineClearSound != null) {
            double rate = 1.0 + (combo * 0.1);
            rate = Math.min(rate, 2.0);
            lineClearSound.setRate(rate);
            lineClearSound.setVolume(soundVolume);
            lineClearSound.play();
        }
    }

    public void playGameOver() {
        if (!isMuted && gameOverSound != null) {
            gameOverSound.setVolume(soundVolume);
            gameOverSound.play();
        }
    }

    public void playHighScore() {
        if (!isMuted && highScoreSound != null) {
            highScoreSound.setVolume(soundVolume);
            highScoreSound.play();
        }
    }

    public void playLevelUp() {
        if (!isMuted && levelUpSound != null) {
            levelUpSound.setVolume(soundVolume);
            levelUpSound.play();
        }
    }

    public void playCountdown() {
        if (!isMuted && countdownSound != null) {
            countdownSound.setVolume(soundVolume);
            countdownSound.play();
        }
    }

    public void playTitleMusic() {
        if (!isMuted && titleMusicPlayer != null) {
            titleMusicPlayer.setVolume(musicVolume);
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

    public void setSoundVolume(double volume) {
        this.soundVolume = Math.max(0.0, Math.min(1.0, volume));
        if (moveSound != null) moveSound.setVolume(soundVolume);
        if (rotateSound != null) rotateSound.setVolume(soundVolume);
        if (hardDropSound != null) hardDropSound.setVolume(soundVolume);
        if (lineClearSound != null) lineClearSound.setVolume(soundVolume);
        if (gameOverSound != null) gameOverSound.setVolume(soundVolume);
        if (highScoreSound != null) highScoreSound.setVolume(soundVolume);
        if (levelUpSound != null) levelUpSound.setVolume(soundVolume);
        if (countdownSound != null) countdownSound.setVolume(soundVolume);
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (titleMusicPlayer != null) {
            titleMusicPlayer.setVolume(musicVolume);
        }
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }
}


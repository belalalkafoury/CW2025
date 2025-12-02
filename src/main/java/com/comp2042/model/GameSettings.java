package com.comp2042.model;

import java.util.prefs.Preferences;

public class GameSettings {
    private static final String PREFS_NODE = "com.comp2042.tetris";
    private static final String KEY_SHOW_ANIMATIONS = "showAnimations";
    private static final String KEY_SHOW_GHOST_PIECE = "showGhostPiece";
    private static final String KEY_SOUND_VOLUME = "soundVolume";
    private static final String KEY_MUSIC_VOLUME = "musicVolume";

    private static boolean showAnimations = true;
    private static boolean showGhostPiece = true;
    private static double soundVolume = 1.0;
    private static double musicVolume = 1.0;

    private static Preferences prefs = Preferences.userRoot().node(PREFS_NODE);

    static {
        load();
    }

    public static void load() {
        showAnimations = prefs.getBoolean(KEY_SHOW_ANIMATIONS, true);
        showGhostPiece = prefs.getBoolean(KEY_SHOW_GHOST_PIECE, true);
        soundVolume = prefs.getDouble(KEY_SOUND_VOLUME, 1.0);
        musicVolume = prefs.getDouble(KEY_MUSIC_VOLUME, 1.0);
    }

    public static void save() {
        prefs.putBoolean(KEY_SHOW_ANIMATIONS, showAnimations);
        prefs.putBoolean(KEY_SHOW_GHOST_PIECE, showGhostPiece);
        prefs.putDouble(KEY_SOUND_VOLUME, soundVolume);
        prefs.putDouble(KEY_MUSIC_VOLUME, musicVolume);
    }

    public static void reset() {
        showAnimations = true;
        showGhostPiece = true;
        soundVolume = 1.0;
        musicVolume = 1.0;
        save();
    }

    public static boolean isShowAnimations() {
        return showAnimations;
    }

    public static void setShowAnimations(boolean showAnimations) {
        GameSettings.showAnimations = showAnimations;
        save();
    }

    public static boolean isShowGhostPiece() {
        return showGhostPiece;
    }

    public static void setShowGhostPiece(boolean showGhostPiece) {
        GameSettings.showGhostPiece = showGhostPiece;
        save();
    }

    public static double getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(double soundVolume) {
        GameSettings.soundVolume = Math.max(0.0, Math.min(1.0, soundVolume));
        save();
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(double musicVolume) {
        GameSettings.musicVolume = Math.max(0.0, Math.min(1.0, musicVolume));
        save();
    }
}


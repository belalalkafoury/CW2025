package com.comp2042.model;

import java.util.prefs.Preferences;

/**
 * Manages game settings with persistence using Java Preferences API.
 * Settings are automatically saved and loaded from user preferences.
 */
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

    /**
     * Loads settings from persistent storage.
     * Uses default values if no saved settings exist.
     */
    public static void load() {
        showAnimations = prefs.getBoolean(KEY_SHOW_ANIMATIONS, true);
        showGhostPiece = prefs.getBoolean(KEY_SHOW_GHOST_PIECE, true);
        soundVolume = prefs.getDouble(KEY_SOUND_VOLUME, 1.0);
        musicVolume = prefs.getDouble(KEY_MUSIC_VOLUME, 1.0);
    }

    /**
     * Saves current settings to persistent storage.
     */
    public static void save() {
        prefs.putBoolean(KEY_SHOW_ANIMATIONS, showAnimations);
        prefs.putBoolean(KEY_SHOW_GHOST_PIECE, showGhostPiece);
        prefs.putDouble(KEY_SOUND_VOLUME, soundVolume);
        prefs.putDouble(KEY_MUSIC_VOLUME, musicVolume);
    }

    /**
     * Resets all settings to their default values and saves them.
     */
    public static void reset() {
        showAnimations = true;
        showGhostPiece = true;
        soundVolume = 1.0;
        musicVolume = 1.0;
        save();
    }

    /**
     * Checks if animations are enabled.
     * @return true if animations are enabled, false otherwise
     */
    public static boolean isShowAnimations() {
        return showAnimations;
    }

    /**
     * Sets whether animations should be displayed.
     * @param showAnimations true to enable animations, false to disable
     */
    public static void setShowAnimations(boolean showAnimations) {
        GameSettings.showAnimations = showAnimations;
        save();
    }

    /**
     * Checks if ghost piece preview is enabled.
     * @return true if ghost piece is shown, false otherwise
     */
    public static boolean isShowGhostPiece() {
        return showGhostPiece;
    }

    /**
     * Sets whether the ghost piece preview should be displayed.
     * @param showGhostPiece true to show ghost piece, false to hide
     */
    public static void setShowGhostPiece(boolean showGhostPiece) {
        GameSettings.showGhostPiece = showGhostPiece;
        save();
    }

    /**
     * Gets the current sound effect volume.
     * @return Volume level between 0.0 and 1.0
     */
    public static double getSoundVolume() {
        return soundVolume;
    }

    /**
     * Sets the sound effect volume.
     * @param soundVolume Volume level between 0.0 and 1.0 (clamped automatically)
     */
    public static void setSoundVolume(double soundVolume) {
        GameSettings.soundVolume = Math.max(0.0, Math.min(1.0, soundVolume));
        save();
    }

    /**
     * Gets the current music volume.
     * @return Volume level between 0.0 and 1.0
     */
    public static double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Sets the music volume.
     * @param musicVolume Volume level between 0.0 and 1.0 (clamped automatically)
     */
    public static void setMusicVolume(double musicVolume) {
        GameSettings.musicVolume = Math.max(0.0, Math.min(1.0, musicVolume));
        save();
    }
}



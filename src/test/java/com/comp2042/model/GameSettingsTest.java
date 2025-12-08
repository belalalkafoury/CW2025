package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameSettingsTest {

    @BeforeEach
    void setUp() {
        GameSettings.reset();
    }

    @Test
    void testDefaultShowAnimations() {
        assertTrue(GameSettings.isShowAnimations(), "Animations should be enabled by default");
    }

    @Test
    void testDefaultShowGhostPiece() {
        assertTrue(GameSettings.isShowGhostPiece(), "Ghost piece should be enabled by default");
    }

    @Test
    void testDefaultSoundVolume() {
        assertEquals(1.0, GameSettings.getSoundVolume(), 0.001, "Sound volume should be 1.0 by default");
    }

    @Test
    void testDefaultMusicVolume() {
        assertEquals(1.0, GameSettings.getMusicVolume(), 0.001, "Music volume should be 1.0 by default");
    }

    @Test
    void testSetShowAnimations() {
        GameSettings.setShowAnimations(false);
        assertFalse(GameSettings.isShowAnimations(), "Should be able to disable animations");
        
        GameSettings.setShowAnimations(true);
        assertTrue(GameSettings.isShowAnimations(), "Should be able to enable animations");
    }

    @Test
    void testSetShowGhostPiece() {
        GameSettings.setShowGhostPiece(false);
        assertFalse(GameSettings.isShowGhostPiece(), "Should be able to disable ghost piece");
        
        GameSettings.setShowGhostPiece(true);
        assertTrue(GameSettings.isShowGhostPiece(), "Should be able to enable ghost piece");
    }

    @Test
    void testSetSoundVolume() {
        GameSettings.setSoundVolume(0.5);
        assertEquals(0.5, GameSettings.getSoundVolume(), 0.001, "Should set sound volume to 0.5");
        
        GameSettings.setSoundVolume(0.0);
        assertEquals(0.0, GameSettings.getSoundVolume(), 0.001, "Should set sound volume to 0.0");
        
        GameSettings.setSoundVolume(1.0);
        assertEquals(1.0, GameSettings.getSoundVolume(), 0.001, "Should set sound volume to 1.0");
    }

    @Test
    void testSetSoundVolumeClampsAboveOne() {
        GameSettings.setSoundVolume(2.0);
        assertEquals(1.0, GameSettings.getSoundVolume(), 0.001, "Should clamp volume above 1.0");
    }

    @Test
    void testSetSoundVolumeClampsBelowZero() {
        GameSettings.setSoundVolume(-0.5);
        assertEquals(0.0, GameSettings.getSoundVolume(), 0.001, "Should clamp volume below 0.0");
    }

    @Test
    void testSetMusicVolume() {
        GameSettings.setMusicVolume(0.75);
        assertEquals(0.75, GameSettings.getMusicVolume(), 0.001, "Should set music volume to 0.75");
    }

    @Test
    void testSetMusicVolumeClampsAboveOne() {
        GameSettings.setMusicVolume(1.5);
        assertEquals(1.0, GameSettings.getMusicVolume(), 0.001, "Should clamp music volume above 1.0");
    }

    @Test
    void testSetMusicVolumeClampsBelowZero() {
        GameSettings.setMusicVolume(-1.0);
        assertEquals(0.0, GameSettings.getMusicVolume(), 0.001, "Should clamp music volume below 0.0");
    }

    @Test
    void testReset() {
        GameSettings.setShowAnimations(false);
        GameSettings.setShowGhostPiece(false);
        GameSettings.setSoundVolume(0.3);
        GameSettings.setMusicVolume(0.7);
        
        GameSettings.reset();
        
        assertTrue(GameSettings.isShowAnimations());
        assertTrue(GameSettings.isShowGhostPiece());
        assertEquals(1.0, GameSettings.getSoundVolume(), 0.001);
        assertEquals(1.0, GameSettings.getMusicVolume(), 0.001);
    }

    @Test
    void testVolumeBoundaryValues() {
        GameSettings.setSoundVolume(0.0001);
        assertTrue(GameSettings.getSoundVolume() >= 0.0);
        
        GameSettings.setSoundVolume(0.9999);
        assertTrue(GameSettings.getSoundVolume() <= 1.0);
    }
}



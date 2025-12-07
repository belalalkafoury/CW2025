package com.comp2042.controller;

import com.comp2042.model.GameMode;
import javafx.scene.input.KeyCode;

/**
 * Handles keyboard input and maps keys to game actions.
 * Implements Reverted mode logic by inverting left/right controls when active.
 */
public class InputHandler {
    private final InputEventListener listener;
    private final GameMode gameMode;

    /**
     * Constructs an InputHandler with the specified event listener and game mode.
     * @param listener The event listener to notify of input events
     * @param gameMode The current game mode (affects control mapping)
     */
    public InputHandler(InputEventListener listener, GameMode gameMode) {
        this.listener = listener;
        this.gameMode = gameMode;
    }

    /**
     * Processes a key press and triggers the appropriate game action.
     * In Reverted mode, left and right controls are swapped.
     * @param code The key code that was pressed
     */
    public void handleKey(KeyCode code) {
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            if (gameMode == GameMode.REVERTED) {
                listener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            } else {
                listener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            }
        }
        else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            if (gameMode == GameMode.REVERTED) {
                listener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            } else {
                listener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            }
        }
        else if (code == KeyCode.UP || code == KeyCode.W) {
            listener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        }
        else if (code == KeyCode.DOWN || code == KeyCode.S) {
            listener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
        }
        else if (code == KeyCode.SPACE) {
            listener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
        }
        else if (code == KeyCode.H) {
            listener.onHoldEvent();
        }
    }
}

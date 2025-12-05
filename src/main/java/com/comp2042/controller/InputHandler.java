package com.comp2042.controller;

import com.comp2042.model.GameMode;
import javafx.scene.input.KeyCode;

public class InputHandler {
    private final InputEventListener listener;
    private final GameMode gameMode;

    public InputHandler(InputEventListener listener, GameMode gameMode) {
        this.listener = listener;
        this.gameMode = gameMode;
    }

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
    }
}

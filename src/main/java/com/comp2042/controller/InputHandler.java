package com.comp2042.controller;

import javafx.scene.input.KeyCode;

public class InputHandler {
    private final InputEventListener listener;

    public InputHandler(InputEventListener listener) {
        this.listener = listener;
    }

    public void handleKey(KeyCode code) {
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            listener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        }
        else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            listener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        }
        else if (code == KeyCode.UP || code == KeyCode.W) {
            listener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        }
        else if (code == KeyCode.DOWN || code == KeyCode.S) {
            listener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
        }
    }
}

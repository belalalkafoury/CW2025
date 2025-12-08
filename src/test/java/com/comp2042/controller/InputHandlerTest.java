package com.comp2042.controller;

import com.comp2042.logic.board.ClearRow;
import com.comp2042.logic.board.DownData;
import com.comp2042.model.GameMode;
import com.comp2042.view.ViewData;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class InputHandlerTest {

    private MockInputEventListener mockListener;
    private InputHandler inputHandler;

    @BeforeEach
    void setUp() {
        mockListener = new MockInputEventListener();
    }

    @Test
    void testHandleKeyLeftInClassicMode() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.LEFT);
        
        assertNotNull(mockListener.lastLeftEvent);
        assertEquals(EventType.LEFT, mockListener.lastLeftEvent.getEventType());
        assertEquals(EventSource.USER, mockListener.lastLeftEvent.getEventSource());
    }

    @Test
    void testHandleKeyRightInClassicMode() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.RIGHT);
        
        assertNotNull(mockListener.lastRightEvent);
        assertEquals(EventType.RIGHT, mockListener.lastRightEvent.getEventType());
    }

    @Test
    void testHandleKeyLeftInRevertedMode() {
        inputHandler = new InputHandler(mockListener, GameMode.REVERTED);
        inputHandler.handleKey(KeyCode.LEFT);
        
        assertNotNull(mockListener.lastRightEvent, "Left key should trigger right event in Reverted mode");
        assertEquals(EventType.RIGHT, mockListener.lastRightEvent.getEventType());
    }

    @Test
    void testHandleKeyRightInRevertedMode() {
        inputHandler = new InputHandler(mockListener, GameMode.REVERTED);
        inputHandler.handleKey(KeyCode.RIGHT);
        
        assertNotNull(mockListener.lastLeftEvent, "Right key should trigger left event in Reverted mode");
        assertEquals(EventType.LEFT, mockListener.lastLeftEvent.getEventType());
    }

    @Test
    void testHandleKeyUp() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.UP);
        
        assertNotNull(mockListener.lastRotateEvent);
        assertEquals(EventType.ROTATE, mockListener.lastRotateEvent.getEventType());
    }

    @Test
    void testHandleKeyDown() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.DOWN);
        
        assertNotNull(mockListener.lastDownEvent);
        assertEquals(EventType.DOWN, mockListener.lastDownEvent.getEventType());
    }

    @Test
    void testHandleKeySpace() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.SPACE);
        
        assertNotNull(mockListener.lastHardDropEvent);
        assertEquals(EventType.HARD_DROP, mockListener.lastHardDropEvent.getEventType());
    }

    @Test
    void testHandleKeyH() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.H);
        
        assertTrue(mockListener.holdEventCalled);
    }

    @Test
    void testHandleKeyA() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.A);
        
        assertNotNull(mockListener.lastLeftEvent);
        assertEquals(EventType.LEFT, mockListener.lastLeftEvent.getEventType());
    }

    @Test
    void testHandleKeyD() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.D);
        
        assertNotNull(mockListener.lastRightEvent);
        assertEquals(EventType.RIGHT, mockListener.lastRightEvent.getEventType());
    }

    @Test
    void testHandleKeyW() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.W);
        
        assertNotNull(mockListener.lastRotateEvent);
        assertEquals(EventType.ROTATE, mockListener.lastRotateEvent.getEventType());
    }

    @Test
    void testHandleKeyS() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.S);
        
        assertNotNull(mockListener.lastDownEvent);
        assertEquals(EventType.DOWN, mockListener.lastDownEvent.getEventType());
    }

    @Test
    void testRevertedModeWithWASD() {
        inputHandler = new InputHandler(mockListener, GameMode.REVERTED);
        inputHandler.handleKey(KeyCode.A);
        
        assertNotNull(mockListener.lastRightEvent, "A key should trigger right in Reverted mode");
        inputHandler.handleKey(KeyCode.D);
        assertNotNull(mockListener.lastLeftEvent, "D key should trigger left in Reverted mode");
    }

    @Test
    void testUnhandledKey() {
        inputHandler = new InputHandler(mockListener, GameMode.CLASSIC);
        inputHandler.handleKey(KeyCode.ENTER);
        
        assertNull(mockListener.lastLeftEvent);
        assertNull(mockListener.lastRightEvent);
        assertFalse(mockListener.holdEventCalled);
    }

    private static class MockInputEventListener implements InputEventListener {
        MoveEvent lastLeftEvent;
        MoveEvent lastRightEvent;
        MoveEvent lastDownEvent;
        MoveEvent lastRotateEvent;
        MoveEvent lastHardDropEvent;
        boolean holdEventCalled = false;

        @Override
        public ViewData onLeftEvent(MoveEvent event) {
            this.lastLeftEvent = event;
            return createMockViewData();
        }

        @Override
        public ViewData onRightEvent(MoveEvent event) {
            this.lastRightEvent = event;
            return createMockViewData();
        }

        @Override
        public DownData onDownEvent(MoveEvent event) {
            this.lastDownEvent = event;
            return createMockDownData();
        }

        @Override
        public ViewData onRotateEvent(MoveEvent event) {
            this.lastRotateEvent = event;
            return createMockViewData();
        }

        @Override
        public DownData onHardDropEvent(MoveEvent event) {
            this.lastHardDropEvent = event;
            return createMockDownData();
        }

        @Override
        public ViewData onHoldEvent() {
            this.holdEventCalled = true;
            return createMockViewData();
        }

        @Override
        public void createNewGame() {
            // Mock implementation
        }

        private ViewData createMockViewData() {
            int[][] emptyBrick = new int[1][1];
            return new ViewData(emptyBrick, 0, 0, new ArrayList<>(), null);
        }

        private DownData createMockDownData() {
            int[][] emptyMatrix = new int[20][10];
            ClearRow clearRow = new ClearRow(0, emptyMatrix, new ArrayList<>());
            ViewData viewData = createMockViewData();
            return new DownData(clearRow, viewData);
        }
    }
}


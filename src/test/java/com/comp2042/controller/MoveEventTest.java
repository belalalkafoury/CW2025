package com.comp2042.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveEventTest {

    @Test
    void testConstructor() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        
        assertNotNull(event);
        assertEquals(EventType.DOWN, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    void testGetEventType() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        assertEquals(EventType.ROTATE, event.getEventType());
    }

    @Test
    void testGetEventSource() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.THREAD);
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void testAllEventTypes() {
        for (EventType type : EventType.values()) {
            MoveEvent event = new MoveEvent(type, EventSource.USER);
            assertEquals(type, event.getEventType());
        }
    }

    @Test
    void testAllEventSources() {
        for (EventSource source : EventSource.values()) {
            MoveEvent event = new MoveEvent(EventType.DOWN, source);
            assertEquals(source, event.getEventSource());
        }
    }

    @Test
    void testEventWithHardDrop() {
        MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);
        assertEquals(EventType.HARD_DROP, event.getEventType());
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    void testEventWithHold() {
        MoveEvent event = new MoveEvent(EventType.HOLD, EventSource.USER);
        assertEquals(EventType.HOLD, event.getEventType());
    }
}



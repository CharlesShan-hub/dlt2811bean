package com.ysh.dlt2811bean.transport.goose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GooseStateMachine Unit Test")
class GooseStateMachineTest {

    @Test
    @DisplayName("initial state has stNum=0, sqNum=0")
    void initialState() {
        GooseStateMachine sm = new GooseStateMachine();
        assertEquals(0, sm.getCurrentStNum());
        assertEquals(0, sm.getCurrentSqNum());
        assertFalse(sm.isInFastRetransmit());
    }

    @Test
    @DisplayName("onEvent increments stNum and resets sqNum")
    void onEvent() {
        GooseStateMachine sm = new GooseStateMachine();
        sm.onEvent();
        assertEquals(1, sm.getCurrentStNum());
        assertEquals(0, sm.getCurrentSqNum());
        assertTrue(sm.isInFastRetransmit());
    }

    @Test
    @DisplayName("onTick increments sqNum")
    void onTick() {
        GooseStateMachine sm = new GooseStateMachine();
        sm.onEvent();
        long sqNum = sm.onTick();
        assertEquals(0, sqNum);
        assertEquals(1, sm.getCurrentSqNum());
    }

    @Test
    @DisplayName("multiple events increment stNum sequentially")
    void multipleEvents() {
        GooseStateMachine sm = new GooseStateMachine();
        sm.onEvent();
        assertEquals(1, sm.getCurrentStNum());
        sm.onEvent();
        assertEquals(2, sm.getCurrentStNum());
        sm.onEvent();
        assertEquals(3, sm.getCurrentStNum());
    }

    @Test
    @DisplayName("fast retransmit transitions to steady state")
    void fastRetransmitTransition() {
        GooseStateMachine sm = new GooseStateMachine();
        sm.onEvent();
        assertTrue(sm.isInFastRetransmit());

        for (int i = 0; i < GooseStateMachine.FAST_RETRANSMIT_DELAYS_MS.length; i++) {
            sm.onTick();
        }
        assertFalse(sm.isInFastRetransmit());
    }

    @Test
    @DisplayName("reset clears all state")
    void reset() {
        GooseStateMachine sm = new GooseStateMachine();
        sm.onEvent();
        sm.onTick();
        sm.reset();
        assertEquals(0, sm.getCurrentStNum());
        assertEquals(0, sm.getCurrentSqNum());
        assertFalse(sm.isInFastRetransmit());
    }

    @Test
    @DisplayName("event during fast retransmit restarts fast retransmit")
    void eventDuringFastRetransmit() {
        GooseStateMachine sm = new GooseStateMachine();
        sm.onEvent();
        sm.onTick();
        sm.onTick();
        sm.onEvent();
        assertEquals(2, sm.getCurrentStNum());
        assertEquals(0, sm.getCurrentSqNum());
        assertTrue(sm.isInFastRetransmit());
    }
}

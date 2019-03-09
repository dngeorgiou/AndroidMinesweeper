package com.dng.minesweeper.util;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReasonTest {

    private Reason reason;

    @Test
    public void getReason() {
        // Test REASON_WIN case
        reason = new Reason(Reason.REASON_WIN);
        assertEquals(Reason.REASON_WIN, reason.getReason());

        // Test REASON_LOSS case
        reason = new Reason(Reason.REASON_LOSS);
        assertEquals(Reason.REASON_LOSS, reason.getReason());
    }

    @After
    public void tearDown() {
        reason = null;
    }
}
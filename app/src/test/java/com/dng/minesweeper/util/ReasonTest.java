package com.dng.minesweeper.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReasonTest {

    private Reason reason;
    private Grid grid;

    @Test
    public void getReason() {
        // Test REASON_WIN case
        String expWin = Reason.REASON_WIN;
        grid = new Grid(4, 4);
        grid.setGameOverWin();
        reason = new Reason(grid);
        assertEquals(expWin, reason.getReason());

        // Test REASON_LOSS case
        String expLoss = Reason.REASON_LOSS;
        grid = new Grid(4, 4);
        grid.setGameOverLoss();
        reason = new Reason(grid);
        assertEquals(expLoss, reason.getReason());
    }

    @After
    public void tearDown() {
        reason = null;
        grid = null;
    }
}
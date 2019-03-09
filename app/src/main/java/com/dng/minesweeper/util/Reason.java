package com.dng.minesweeper.util;

/**
 * Class is to be used as a parameter for InterstitialAd, to determine whether the reason for update
 * is for a game over win or for a game over loss.
 */
public class Reason {
    protected static final String REASON_WIN = "reasonWin";
    protected static final String REASON_LOSS = "reasonLoss";

    private final Grid grid;

    // Constructor
    public Reason(Grid grid) {
        this.grid = grid;
    }

    public String getReason() {
        if (grid.getGameOverWin()) {
            return Reason.REASON_WIN;
        } else if (grid.getGameOverLoss()) {
            return Reason.REASON_LOSS;
        } else {
            return null;
        }
    }

}

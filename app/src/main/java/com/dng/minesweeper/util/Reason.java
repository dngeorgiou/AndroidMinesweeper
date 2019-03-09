package com.dng.minesweeper.util;

/**
 * Class is to be used as a parameter for InterstitialAd, to determine whether the reason for update
 * is for a game over win or for a game over loss.
 */
public class Reason {
    public static final String REASON_WIN = "reasonWin";
    public static final String REASON_LOSS = "reasonLoss";

    private final String reason;

    // Constructor
    public Reason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}

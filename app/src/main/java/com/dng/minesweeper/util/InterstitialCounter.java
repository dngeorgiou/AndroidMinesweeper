package com.dng.minesweeper.util;

import android.util.Log;

public class InterstitialCounter {

    private static final String TAG = "InterstitialCounter";

    private final static int winsForInterstitialAd = 2;
    private final static int winsOrLossesForInterstitialAd = 3;

    private int winsTillInterstitialAd;
    private int winsOrLossesTillInterstitialAd;

    public InterstitialCounter() {
        winsTillInterstitialAd = winsForInterstitialAd;
        winsOrLossesTillInterstitialAd = winsOrLossesForInterstitialAd;
    }

    /**
     * Method checks whether an interstitial ad should be displayed.
     * @return true when either condition is met:
     * 1.) winsTillInterstitialAd == 0
     * 2.) winsOrLossesTillInterstitialAd == 0
     */
    public boolean shouldPresentAd(Reason reason) {
        if (winsTillInterstitialAd == 0 || winsOrLossesTillInterstitialAd == 0) {
            // Should show ad
            Log.d(TAG, "show InterstitialAd");
            resetCounters();
            return true;
        }

        // Should not show ad
        handleIncrements(reason);

        return false;
    }

    /**
     * Method handles incrementing appropriate members.
     */
    private void handleIncrements(Reason reason) {
        // Increment member respective to reason
        if (reason.getReason().equals(Reason.REASON_WIN)) {
            incrementWinsTillAd();
            incrementWinsOrLossesTillAd();
        } else if (reason.getReason().equals(Reason.REASON_LOSS)) {
            incrementWinsOrLossesTillAd();
        }
    }

    /**
     * Method increments winsTillInterstitialAd member.
     */
    private void incrementWinsTillAd() {
        winsTillInterstitialAd = winsTillInterstitialAd - 1;
        Log.d(TAG, "winsTillInterstitialAd: " + winsTillInterstitialAd);
    }

    /**
     * Method increments winsOrLossesTillInterstitialAd member.
     */
    private void incrementWinsOrLossesTillAd() {
        winsOrLossesTillInterstitialAd = winsOrLossesTillInterstitialAd - 1;
        Log.d(TAG, "winsOrLossesTillInterstitialAd: " + winsOrLossesTillInterstitialAd);
    }

    /**
     * Method resets winsTillInterstitialAd and winsOrLossesTillInterstitialAd.
     */
    private void resetCounters() {
        // Reset winsTillInterstitialAd and winsOrLossesTillInterstitialAd
        winsTillInterstitialAd = winsForInterstitialAd;
        winsOrLossesTillInterstitialAd = winsOrLossesForInterstitialAd;
    }
}

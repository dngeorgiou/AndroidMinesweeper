package com.dng.minesweeper.util;

import java.util.HashMap;

public class Grid {

    private static final String TAG = "Grid";

    /*
    KEY:
        0 - no mine at this location
        1 - mine at this location
     */
    public static final int NO_MINE_VALUE = 0;
    public static final int MINE_VALUE = 1;

    public Grid() {
        // Required empty public constructor
    }

    // [START create HashMap to be used for Minesweeper grid]
    public HashMap<Integer, Integer> populateGridHashMap(int cells, int mines) {
        // Reference global map HashMap for reason of using HashMap rather than SparseIntArray
        HashMap<Integer, Integer> hm = new HashMap<>();
        int remainingCells = cells;
        int remainingMines = mines;

        for (int i = 0; i < cells; i++) {
            float chance = (float) remainingMines / remainingCells;
            // Math.random() returns a double between 0 (inclusive) and 1 (exclusive)
            if (Math.random() < chance) {
                hm.put(i, MINE_VALUE);
                remainingMines = remainingMines - 1;
            } else {
                hm.put(i, NO_MINE_VALUE);
            }
            remainingCells = remainingCells - 1;
        }

        return hm;
    }
    // [END create HashMap to be used for Minesweeper grid]
}

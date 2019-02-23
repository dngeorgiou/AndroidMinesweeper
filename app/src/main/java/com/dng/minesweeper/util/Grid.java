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

    public int[][] grid;

    public Grid() {
        // Required empty public constructor
    }

    // [START initialization for Minesweeper grid]
    public int[][] initializeGrid(int cells, int mines) {
        int remainingCells = cells;
        int remainingMines = mines;
        int[][] g = new int[cells][cells];

        for (int i = 0; i < cells; i++) {
            for (int j = 0; j < cells; j++) {
                float chance = (float) remainingMines / remainingCells;
                // Math.random() returns a double between 0 (inclusive) and 1 (exclusive)
                if (Math.random() < chance) {
                    g[i][j] = MINE_VALUE;
                    remainingMines = remainingMines - 1;
                } else {
                    g[i][j] = NO_MINE_VALUE;
                }
                remainingCells = remainingCells - 1;
            }
        }

        return g;
    }
    // [END initialization for Minesweeper grid]

}

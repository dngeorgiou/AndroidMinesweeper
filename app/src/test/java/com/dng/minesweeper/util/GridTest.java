package com.dng.minesweeper.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GridTest {

    private Grid grid;
    private int rows;
    private int mines;

    /**
     * Unit Test to verify length of created grid is equal to number of cells.
     */
    @Test
    public void initializeGrid_lengthTest() {
        rows = 8;
        mines = (rows*rows)/2;
        grid = new Grid(rows, mines);
        int[][] mineMap;
        mineMap = grid.getMineMap();

        assertEquals(rows*rows, mineMap.length * mineMap.length);
    }

    /**
     * Unit Test to verify mine count of created grid.
     */
    @Test
    public void initializeGrid_mineCountTest() {
        rows = 8;
        mines = (rows*rows)/2;
        grid = new Grid(rows, mines);
        int[][] mineMap;
        mineMap = grid.getMineMap();
        int mineCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++)
                if (mineMap[i][j] == 1) {
                    mineCount = mineCount + 1;
                }
        }

        assertEquals(mines, mineCount);
    }

    /**
     * Unit Test to verify created surroundMap grid.
     */
    @Test
    public void surroundingMines() {
        int rows = 4;

        int[][] mineMap = new int[rows][rows];
        mineMap[0][0] = 0; mineMap[0][1] = 1; mineMap[0][2] = 0; mineMap[0][3] = 0; // 0 1 0 0
        mineMap[1][0] = 0; mineMap[1][1] = 1; mineMap[1][2] = 1; mineMap[1][3] = 1; // 0 1 1 1
        mineMap[2][0] = 0; mineMap[2][1] = 1; mineMap[2][2] = 0; mineMap[2][3] = 0; // 0 1 0 0
        mineMap[3][0] = 1; mineMap[3][1] = 1; mineMap[3][2] = 1; mineMap[3][3] = 0; // 1 1 1 0

        grid = new Grid(mineMap);

        int[][] expSurMap = new int[rows][rows];
        expSurMap[0][0] = 2; expSurMap[0][1] = 2; expSurMap[0][2] = 4; expSurMap[0][3] = 2; // 2 2 4 2
        expSurMap[1][0] = 3; expSurMap[1][1] = 3; expSurMap[1][2] = 4; expSurMap[1][3] = 1; // 3 3 4 1
        expSurMap[2][0] = 4; expSurMap[2][1] = 5; expSurMap[2][2] = 6; expSurMap[2][3] = 3; // 4 5 6 3
        expSurMap[3][0] = 2; expSurMap[3][1] = 3; expSurMap[3][2] = 2; expSurMap[3][3] = 1; // 2 3 2 1

        int[][] surroundingMap = grid.getSurroundingMines();

        assertEquals(expSurMap, surroundingMap);
    }

    /**
     * Unit Test to verify updating shouldShow grid.
     */
    @Test
    public void shouldShowTest() {
        // gridMap
        // 0 0 0 1 1 0
        // 0 0 0 0 0 0
        // 0 0 0 1 0 0
        // 0 0 1 0 0 0
        // 0 0 0 1 1 0
        // 0 0 0 0 1 0

        int rows = 6;
        int[][] mineMap = new int[rows][rows];
        mineMap[0][0] = 0; mineMap[0][1] = 0; mineMap[0][2] = 0; mineMap[0][3] = 1; mineMap[0][4] = 1; mineMap[0][5] = 0; // 0 0 0 1 1 0
        mineMap[1][0] = 0; mineMap[1][1] = 0; mineMap[1][2] = 0; mineMap[1][3] = 0; mineMap[1][4] = 0; mineMap[1][5] = 0; // 0 0 0 0 0 0
        mineMap[2][0] = 0; mineMap[2][1] = 0; mineMap[2][2] = 0; mineMap[2][3] = 1; mineMap[2][4] = 0; mineMap[2][5] = 0; // 0 0 0 1 0 0
        mineMap[3][0] = 0; mineMap[3][1] = 0; mineMap[3][2] = 1; mineMap[3][3] = 0; mineMap[3][4] = 0; mineMap[3][5] = 0; // 0 0 1 0 0 0
        mineMap[4][0] = 0; mineMap[4][1] = 0; mineMap[4][2] = 0; mineMap[4][3] = 1; mineMap[4][4] = 1; mineMap[4][5] = 0; // 0 0 0 1 1 0
        mineMap[5][0] = 0; mineMap[5][1] = 0; mineMap[5][2] = 0; mineMap[5][3] = 0; mineMap[5][4] = 1; mineMap[5][5] = 0; // 0 0 0 0 1 0
        grid = new Grid(mineMap);

        // Expected result for click on (row 0, column 0), (row 0, column 1), (row 1, column 0), (row 1, column 1)
        boolean[][] expsS = new boolean[rows][rows];
        expsS[0][0] = true; expsS[0][1] = true; expsS[0][2] = true; expsS[0][3] = false; expsS[0][4] = false; expsS[0][5] = false;  // true true true false false false
        expsS[1][0] = true; expsS[1][1] = true; expsS[1][2] = true; expsS[1][3] = false; expsS[1][4] = false; expsS[1][5] = false;  // true true true false false false
        expsS[2][0] = true; expsS[2][1] = true; expsS[2][2] = true; expsS[2][3] = false; expsS[2][4] = false; expsS[2][5] = false;  // true true true false false false
        expsS[3][0] = true; expsS[3][1] = true; expsS[3][2] = false; expsS[3][3] = false; expsS[3][4] = false; expsS[3][5] = false; // true true false false false false
        expsS[4][0] = true; expsS[4][1] = true; expsS[4][2] = true; expsS[4][3] = false; expsS[4][4] = false; expsS[4][5] = false;  // true true true false false false
        expsS[5][0] = true; expsS[5][1] = true; expsS[5][2] = true; expsS[5][3] = false; expsS[5][4] = false; expsS[5][5] = false;  // true true true false false false

        // Test for row 0, column 0 clicked
        grid.setShouldShow(0, 0);
        boolean[][] shouldShow = grid.getShouldShow();

        assertEquals(expsS, shouldShow);

        // Test for row 1, column 1 clicked
        grid = new Grid(mineMap);
        grid.setShouldShow(1, 1);
        boolean[][] shouldShow2 = grid.getShouldShow();

        assertEquals(expsS, shouldShow2);

        // Test for row 0, column 1 clicked
        grid = new Grid(mineMap);
        grid.setShouldShow(0, 1);
        boolean[][] shouldShow3 = grid.getShouldShow();

        assertEquals(expsS, shouldShow3);

        // Test for row 1, column 0 clicked
        grid = new Grid(mineMap);
        grid.setShouldShow(1, 0);
        boolean[][] shouldShow4 = grid.getShouldShow();

        assertEquals(expsS, shouldShow4);
    }

    @After
    public void tearDown() {
        grid = null;
    }
}
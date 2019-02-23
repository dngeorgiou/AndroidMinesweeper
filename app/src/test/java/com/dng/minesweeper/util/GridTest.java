package com.dng.minesweeper.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GridTest {

    private Grid grid;

    @Before
    public void setUp() throws Exception {
        grid = new Grid();
    }

    // UnitTest to verify length of created HashMap is equal to number of cells
    @Test
    public void initializeGrid_lengthTest() {
        int rows = 64;
        int mines = (rows*rows)/2;

        int[][] map;
        map = grid.initializeGrid(rows, mines);

        assertEquals(rows*rows, map.length*map.length);
    }

    // UnitTest to verify mine count of returned HashMap
    @Test
    public void initializeGrid_mineCountTest() {
        int rows = 8;
        int mines = (rows*rows)/2;

        int[][] map;
        map = grid.initializeGrid(rows, mines);
        int mineCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++)
                if (map[i][j] == 1) {
                    mineCount = mineCount + 1;
                }
        }

        assertEquals(mines, mineCount);
    }

    @Test
    public void surroundingMines() {
        int rows = 4;

        int[][] map = new int[rows][rows];
        map[0][0] = 0; map[0][1] = 1; map[0][2] = 0; map[0][3] = 0; // 0 1 0 0
        map[1][0] = 0; map[1][1] = 1; map[1][2] = 1; map[1][3] = 1; // 0 1 1 1
        map[2][0] = 0; map[2][1] = 1; map[2][2] = 0; map[2][3] = 0; // 0 1 0 0
        map[3][0] = 1; map[3][1] = 1; map[3][2] = 1; map[3][3] = 0; // 1 1 1 0

        int[][] expSurMap = new int[rows][rows];
        expSurMap[0][0] = 2; expSurMap[0][1] = 2; expSurMap[0][2] = 4; expSurMap[0][3] = 2; // 2 2 4 2
        expSurMap[1][0] = 3; expSurMap[1][1] = 3; expSurMap[1][2] = 4; expSurMap[1][3] = 1; // 3 3 4 1
        expSurMap[2][0] = 4; expSurMap[2][1] = 5; expSurMap[2][2] = 6; expSurMap[2][3] = 3; // 4 5 6 3
        expSurMap[3][0] = 2; expSurMap[3][1] = 3; expSurMap[3][2] = 2; expSurMap[3][3] = 1; // 2 3 2 1

        int[][] surroundingMap = grid.surroundingMines(map);

        assertEquals(expSurMap, surroundingMap);
    }

    @Test
    public void shouldShowTest() {
        // gridMap
        // 0 0 0 1
        // 0 0 0 0
        // 0 0 0 1
        // 1 1 1 0

        int rows = 4;

        int[][] surMap = new int[rows][rows];
        surMap[0][0] = 0; surMap[0][1] = 0; surMap[0][2] = 1; surMap[0][3] = 0; // 0 0 1 0
        surMap[1][0] = 0; surMap[1][1] = 0; surMap[1][2] = 2; surMap[1][3] = 2; // 0 0 2 2
        surMap[2][0] = 2; surMap[2][1] = 3; surMap[2][2] = 3; surMap[2][3] = 1; // 2 3 3 1
        surMap[3][0] = 1; surMap[3][1] = 2; surMap[3][2] = 2; surMap[3][3] = 2; // 1 2 2 2

        // Expected result for click on (row 0, column 0), (row 0, column 1), (row 1, column 0), (row 1, column 1)
        boolean[][] expsS = new boolean[rows][rows];
        expsS[0][0] = true; expsS[0][1] = true; expsS[0][2] = true; expsS[0][3] = false;    // true true true false
        expsS[1][0] = true; expsS[1][1] = true; expsS[1][2] = true; expsS[1][3] = false;    // true true true false
        expsS[2][0] = true; expsS[2][1] = true; expsS[2][2] = true; expsS[2][3] = false;    // true true true false
        expsS[3][0] = false; expsS[3][1] = false; expsS[3][2] = false; expsS[3][3] = false; // false false false false

        // Test for row 0, column 0 clicked
        boolean[][] shouldShow = new boolean[rows][rows];
        shouldShow = grid.updateShouldShow(shouldShow, surMap, 0, 0);

        assertEquals(expsS, shouldShow);

        // Test for row 1, column 1 clicked
        boolean[][] shouldShow2 = new boolean[rows][rows];
        shouldShow2 = grid.updateShouldShow(shouldShow2, surMap, 1, 1);

        assertEquals(expsS, shouldShow2);

        // Test for row 0, column 1 clicked
        boolean[][] shouldShow3 = new boolean[rows][rows];
        shouldShow3 = grid.updateShouldShow(shouldShow3, surMap, 0, 1);

        assertEquals(expsS, shouldShow3);

        // Test for row 1, column 0 clicked
        boolean[][] shouldShow4 = new boolean[rows][rows];
        shouldShow4 = grid.updateShouldShow(shouldShow4, surMap, 1, 0);

        assertEquals(expsS, shouldShow4);
    }

    @After
    public void tearDown() throws Exception {
        grid = null;
    }
}
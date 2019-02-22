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
    public void populateGridHashMap_lengthTest() {
        int cells = 64;
        int mines = cells/2;

        HashMap<Integer, Integer> map;
        map = grid.populateGridHashMap(cells, mines);

        assertEquals(cells, map.size());
    }

    // UnitTest to verify mine count of returned HashMap
    @Test
    public void populateGridHashMap_mineCountTest() {
        int cells = 8;
        int mines = cells/2;

        Map<Integer, Integer> map;
        map = grid.populateGridHashMap(cells, mines);
        int mineCount = 0;
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i) == 1) {
                mineCount = mineCount + 1;
            }
        }

        assertEquals(mines, mineCount);
    }

    @After
    public void tearDown() throws Exception {
        grid = null;
    }
}
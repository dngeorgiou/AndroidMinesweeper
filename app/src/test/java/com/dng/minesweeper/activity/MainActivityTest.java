package com.dng.minesweeper.activity;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MainActivityTest {

    // UnitTest to verify length of created HashMap is equal to number of cells
    @Test
    public void populateGridHashMap_lengthTest() {
        int cells = 64;
        int mines = cells/2;

        HashMap<Integer, Integer> map;
        map = MainActivity.populateGridHashMap(cells, mines);

        assertEquals(cells, map.size());
    }

    // UnitTest to verify mine count of returned HashMap
    @Test
    public void populateGridHashMap_mineCountTest() {
        int cells = 8;
        int mines = cells/2;

        Map<Integer, Integer> map;
        map = MainActivity.populateGridHashMap(cells, mines);
        int mineCount = 0;
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i) == 1) {
                mineCount = mineCount + 1;
            }
        }

        assertEquals(mines, mineCount);
    }
}
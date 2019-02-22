package com.dng.minesweeper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.MainFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentListener {

    private static final String TAG = "MainActivity";

    /*
    KEY:
        0 - no mine at this location
        1 - mine at this location
     */
    public static final int NO_MINE_VALUE = 0;
    public static final int MINE_VALUE = 1;

    /*
    Using HashMap rather than SparseIntArray (which has better performance when keys are Integers)
    for the trade off of being able to simply pass it to MainFragment.java as a Serializable object.
     */
    private HashMap<Integer, Integer> map = new HashMap<>();

    // number of rows (also number of columns)
    public static final int rows = 8;
    // number of mines in grid matrix
    private static final int mines = rows*rows/2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = populateGridHashMap(rows * rows, mines);

        MainFragment mainFragment = MainFragment.newInstance(map);
        setFragment(mainFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        fragmentTransaction.commit();
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
//        Log.d(TAG, "remainingCells: " + remainingCells);
//        Log.d(TAG, "remainingMines: " + remainingMines);

        return hm;
    }
    // [END create HashMap to be used for Minesweeper grid]

    @Override
    public void onBlockPressed(int row, int column) {
        Log.d(TAG, "row: " + String.valueOf(row));
        Log.d(TAG, "column: " + String.valueOf(column));
    }

}

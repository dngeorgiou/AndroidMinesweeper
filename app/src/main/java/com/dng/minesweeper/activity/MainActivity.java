package com.dng.minesweeper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.MainFragment;
import com.dng.minesweeper.util.Grid;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentListener {

    private static final String TAG = "MainActivity";

    // number of rows (also number of columns)
    public static final int rows = 8;
    // number of mines in grid matrix
    private static final int mines = rows*rows/2;

    /*
    Using HashMap rather than SparseIntArray (which has better performance when keys are Integers)
    for the trade off of being able to simply pass it to MainFragment.java as a Serializable object.
     */
    private HashMap<Integer, Integer> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Grid grid = new Grid();
        map = grid.populateGridHashMap(rows * rows, mines);

        MainFragment mainFragment = MainFragment.newInstance(map);
        setFragment(mainFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBlockPressed(int row, int column) {
        Log.d(TAG, "row: " + String.valueOf(row));
        Log.d(TAG, "column: " + String.valueOf(column));
    }

}

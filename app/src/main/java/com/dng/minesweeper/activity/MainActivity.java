package com.dng.minesweeper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.MainFragment;
import com.dng.minesweeper.util.Grid;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentListener {

    private static final String TAG = "MainActivity";

    // number of rows (also number of columns)
    private static final int rows = 8;
    // number of mines in grid matrix
    private static final int mines = rows*rows/2;

    private Grid grid = new Grid();
    private int[][] gridMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridMap = grid.initializeGrid(rows, mines);

        MainFragment mainFragment = MainFragment.newInstance(rows, mines);
        setFragment(mainFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBlockPressed(int row, int column, TextView textView) {
        Log.d(TAG, "row: " + String.valueOf(row));
        Log.d(TAG, "column: " + String.valueOf(column));



        if (gridMap[row][column] == Grid.MINE_VALUE) {
            Log.d(TAG, String.valueOf(Grid.MINE_VALUE));
            textView.setText(String.valueOf(Grid.MINE_VALUE));
        } else {
            Log.d(TAG, String.valueOf(Grid.NO_MINE_VALUE));
            textView.setText(String.valueOf(Grid.NO_MINE_VALUE));
        }
    }

}

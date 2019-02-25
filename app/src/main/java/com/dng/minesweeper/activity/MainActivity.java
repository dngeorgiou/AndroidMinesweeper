package com.dng.minesweeper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
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
    private static final int mines = rows*rows/6;

    private MainFragment mainFragment;

    private Grid grid = new Grid();
    public static int[][] gridMap;
    public static int[][] surroundingMap;
    public static boolean[][] shouldShow;
    public static boolean[][] flagVisible;
    public static boolean gameOverLoss = false;
    public static int lastClickedRow;
    public static int lastClickedColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize gridMap with mine/noMine values
        System.out.println("gridMap");
        gridMap = grid.initializeGrid(rows, mines);

        // Initialize surroundingMap with values of surrounding mines
        System.out.println("surroundingMap");
        surroundingMap = grid.surroundingMines(gridMap);

        // Initialize shouldShow, which HorizontalListAdapter uses to determine if should update
        // certain cells UI
        shouldShow = new boolean[rows][rows];

        // Initialize flagVisible, which is used to determine setting visibility of textView/imgView
        // when user long clicks on a block
        flagVisible = new boolean[rows][rows];

        mainFragment = MainFragment.newInstance(rows);
        setFragment(mainFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBlockPressed(int row, int column, TextView textView, ImageView mineImgView) {
        Log.d(TAG, "row: " + String.valueOf(row));
        Log.d(TAG, "column: " + String.valueOf(column));

        if (gridMap[row][column] == Grid.MINE_VALUE) {
            // Player pressed on block with a mine; end game and update UI
            Log.d(TAG, "MINE_VALUE: " + String.valueOf(Grid.MINE_VALUE));
            mineImgView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);

            // [START set row and column of mine clicked]
            /*
            * Setting row/column in MainActivity.java is a hack fix for issue of new HorizontalListAdapter
            * object being created (from within VerticalListAdapter) every time the UI is updated, which
            * then re-initializes member variables of HorizontalListAdapter, so can't set row/column from
            * within it.
             */
            lastClickedRow = row;
            lastClickedColumn = column;
            // [END set row and column of mine clicked]

            gameOverLoss = true;
            mainFragment.updateUI();
        } else {
            // Player pressed on block without a mine; update UI
            Log.d(TAG, "MINE_VALUE: " + String.valueOf(Grid.NO_MINE_VALUE));
            shouldShow[row][column] = true;
            shouldShow = grid.updateShouldShow(shouldShow, surroundingMap, flagVisible, row, column);
            mainFragment.updateUI();
        }

    }

    @Override
    public void onResetBtnPressed() {
        gameOverLoss = false;
        surroundingMap = grid.surroundingMines(gridMap);
        shouldShow = new boolean[rows][rows];
        mainFragment.updateUI();
    }

    @Override
    public void onNewGameBtnPressed() {
        gameOverLoss = false;

        // Initialize gridMap with mine/noMine values
        System.out.println("gridMap");
        gridMap = grid.initializeGrid(rows, mines);

        // Initialize surroundingMap with values of surrounding mines
        System.out.println("surroundingMap");

        surroundingMap = grid.surroundingMines(gridMap);
        shouldShow = new boolean[rows][rows];
        mainFragment.updateUI();
    }
}

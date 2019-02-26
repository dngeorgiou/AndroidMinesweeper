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
    public static boolean gameOverWin = false;
    public static boolean gameOverLoss = false;
    public static int lastClickedRow;
    public static int lastClickedColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set member variables for new game
        initializeForNewGame();

        // Create new instance of MainFragment and set it
        mainFragment = MainFragment.newInstance(rows);
        setFragment(mainFragment);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void initializeForNewGame() {
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
    }

    private boolean checkGameOverWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                // [START check if any blocks with a mine have not been flagged]
                if (gridMap[i][j] == 1 && !flagVisible[i][j]) {
                    // Block contains a mine and has not been flagged, user has not won
                    return false;
                }
                // [END check if any blocks with a mine have not been flagged]

                // [START check if any blocks not containing a mine have not been shown]
                if (gridMap[i][j] == 0 && !shouldShow[i][j]) {
                    // Block does not contain a mine and has not been shown, user has not won
                    return false;
                }
                // [END check if any blocks not containing a mine have not been shown]
            }
        }

        // All blocks with a mine have been flagged, all blocks not containing a mine have been shown,
        // user has WON!
        return true;
    }

    @Override
    public void onBlockPressed(int row, int column, TextView textView, ImageView mineImgView) {
        Log.d(TAG, "row: " + String.valueOf(row));
        Log.d(TAG, "column: " + String.valueOf(column));

        if (gridMap[row][column] == Grid.MINE_VALUE) {
            // Player pressed on block with a mine; end game and update UI
            Log.d(TAG, "MINE_VALUE: " + String.valueOf(Grid.MINE_VALUE));

            // Set gameOverLoss to true
            gameOverLoss = true;

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

            // Update UI for gameOverLoss
            mainFragment.updateUIForLoss();
        } else {
            // Player pressed on block without a mine; update UI
            Log.d(TAG, "MINE_VALUE: " + String.valueOf(Grid.NO_MINE_VALUE));
            shouldShow[row][column] = true;
            shouldShow = grid.updateShouldShow(shouldShow, surroundingMap, flagVisible, row, column);

            // Check if user has won
            if (checkGameOverWin()) {
                // User has won
                gameOverWin = true;
                mainFragment.updateUIForWin();
            }
        }

        // Update UI
        mainFragment.updateUI();

    }

    @Override
    public void onBlockLongPressed(int row, int column, TextView textView, ImageView flagImgView) {
        if (flagVisible[row][column]) {
            // Show TextView, hide ImageView, set flagVisible of block to false
            textView.setVisibility(View.VISIBLE);
            flagImgView.setVisibility(View.INVISIBLE);
            flagVisible[row][column] = false;
        } else {
            // Hide TextView, show ImageView, set flagVisible of block to true
            textView.setVisibility(View.INVISIBLE);
            flagImgView.setVisibility(View.VISIBLE);
            flagVisible[row][column] = true;

            // Check if user has won
            if (checkGameOverWin()) {
                // User has won
                gameOverWin = true;
                mainFragment.updateUIForWin();
            }
        }
    }

    @Override
    public void onNewGameBtnPressed() {
        // New game started, set gameOverLoss and gameOverWin member variables to false
        gameOverLoss = false;
        gameOverWin = false;

        // Reset member variables for new game
        initializeForNewGame();

        // Update UI
        mainFragment.updateUIForNewGame();
        mainFragment.updateUI();
    }
}

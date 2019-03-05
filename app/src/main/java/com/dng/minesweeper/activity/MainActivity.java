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


    // References for minesweeper grid
    private Grid grid = new Grid();
    public static int[][] gridMap;          // Contains 0 or 1 for NO_MINE or MINE
    public static int[][] surroundingMap;   // Contains number of mines surrounding block

    // References for UI state
    public static boolean[][] shouldShow;
    public static boolean[][] flagVisible;

    // References for game over outcome
    public static boolean gameOverWin = false;
    public static boolean gameOverLoss = false;

    // References for last blocked clicked
    public static int lastClickedRow;
    public static int lastClickedColumn;

    // Reference for MainFragment
    private MainFragment mainFragment;


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

    /**
     * Method takes in a fragment as a parameter and replaces MainActivity.java's container view with
     * it.
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Method re-initializes game variables.
     */
    private void initializeForNewGame() {
        // Initialize gridMap with new mine/noMine values
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

        // Set gameOverLoss and gameOverWin member variables to false
        gameOverLoss = false;
        gameOverWin = false;
    }

    /**
     * Method checks if user has won.
     * Win condition: All blocks not containing a mine have been uncovered.
     */
    private boolean checkGameOverWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                // [START check if any blocks not containing a mine have not been shown]
                if (gridMap[i][j] == 0 && !shouldShow[i][j]) {
                    // Block does not contain a mine and has not been shown, user has not won
                    return false;
                }
                // [END check if any blocks not containing a mine have not been shown]
            }
        }

        // All blocks not containing a mine have been shown, user has WON!
        return true;
    }

    /**
     * Method returns true if block contains a mine, and returns false otherwise.
     */
    private boolean blockContainsMine(int row, int column) {
        return (gridMap[row][column] == Grid.MINE_VALUE);
    }

    /**
     * Method updates variables when user pressed on block which contains a mine.
     */
    private void handleBlockWithMine(int row, int column) {
        /*
         * Setting row/column in MainActivity.java is a hack fix for issue of new HorizontalListAdapter
         * object being created (from within VerticalListAdapter) every time the UI is updated, which
         * then re-initializes member variables of HorizontalListAdapter, so can't set row/column from
         * within it.
         */
        lastClickedRow = row;
        lastClickedColumn = column;

        // Set gameOverLoss to true
        gameOverLoss = true;

        // Update UI for gameOverLoss
        mainFragment.updateUIForLoss();
    }

    /**
     * Method updates variables when user pressed on block not containing a mine.
     */
    private void handleBlockNoMine(int row, int column) {
        // Show last pressed block
        shouldShow[row][column] = true;

        // Show surrounding blocks which contain 0 surrounding mines and the blocks immediately surrounding those
        shouldShow = grid.updateShouldShow(shouldShow, surroundingMap, flagVisible, row, column);

        // Check if user has won
        if (checkGameOverWin()) {
            // User has won
            gameOverWin = true;
            mainFragment.updateUIForWin();
            return;
        }

        // Update UI
        mainFragment.updateUI();
    }

    /**
     * Method called when user presses on a block.
     */
    @Override
    public void onBlockPressed(int row, int column, TextView textView, ImageView mineImgView) {
        Log.d(TAG, "row: " + String.valueOf(row));
        Log.d(TAG, "column: " + String.valueOf(column));

        // [START handle mine press]
        if (blockContainsMine(row, column)) {
            // Player pressed on block with a mine; end game and update UI
            Log.d(TAG, "MINE_VALUE: " + String.valueOf(Grid.MINE_VALUE));
            handleBlockWithMine(row, column);
            return;
        }
        // [END handle mine press]

        // [START handle not mine press]
        if (!blockContainsMine(row, column)) {
            // Player pressed on block without a mine; update UI
            Log.d(TAG, "MINE_VALUE: " + String.valueOf(Grid.NO_MINE_VALUE));
            handleBlockNoMine(row, column);
            return;
        }
        // [END handle not mine press]
    }

    /**
     * Method called when user long presses on a block.
     */
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
        }
    }

    /**
     * Method called when user on new game button.
     */
    @Override
    public void onNewGameBtnPressed() {
        // New game started

        // Reset member variables for new game
        initializeForNewGame();

        // Update UI
        mainFragment.updateUIForNewGame();
    }
}

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

    // Reference for minesweeper grid
    private Grid grid;

    // References for game over outcome
    public static boolean gameOverWin = false;
    public static boolean gameOverLoss = false;

    // Reference for MainFragment
    private MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set member variables for new game
        initializeForNewGame();
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
        grid  = new Grid(rows, mines);

        // Set gameOverLoss and gameOverWin member variables to false
        gameOverLoss = false;
        gameOverWin = false;

        // Create new instance of MainFragment and set it
        mainFragment = MainFragment.newInstance(grid, rows);
        setFragment(mainFragment);
    }

    /**
     * Method checks if user has won.
     * Win condition: All blocks not containing a mine have been uncovered.
     */
    private boolean checkGameOverWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                // [START check if any blocks not containing a mine have not been shown]
                if (grid.getMineMap()[i][j] == 0 && !grid.getShouldShow()[i][j]) {
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
        return (grid.getMineMap()[row][column] == Grid.MINE_VALUE);
    }

    /**
     * Method updates variables when user pressed on block which contains a mine.
     */
    private void handleBlockWithMine(int row, int column) {
        // Set last block clicked by setting last row and column clicked
        grid.setLastRowClicked(row);
        grid.setLastColumnClicked(column);

        // Set gameOverLoss to true
        gameOverLoss = true;

        // Update UI for gameOverLoss
        mainFragment.updateUIForLoss();
    }

    /**
     * Method updates variables when user pressed on block not containing a mine.
     */
    private void handleBlockNoMine(int row, int column) {
        // Show surrounding blocks which contain 0 surrounding mines and the blocks immediately surrounding those
        grid.setShouldShow(row, column);

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
        // Update flagVisible UI
        if (grid.getFlagVisible()[row][column]) {
            // Show TextView, hide ImageView, set flagVisible of block to false
            textView.setVisibility(View.VISIBLE);
            flagImgView.setVisibility(View.INVISIBLE);
        } else {
            // Hide TextView, show ImageView, set flagVisible of block to true
            textView.setVisibility(View.INVISIBLE);
            flagImgView.setVisibility(View.VISIBLE);
        }

        // Update flagVisible map
        grid.setFlagVisible(row, column);
    }

    /**
     * Method called when user on new game button.
     */
    @Override
    public void onNewGameBtnPressed() {
        // New game started, initialize new game
        initializeForNewGame();
    }
}

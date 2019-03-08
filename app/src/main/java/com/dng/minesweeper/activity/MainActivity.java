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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

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

    // Reference for MainFragment
    private MainFragment mainFragment;

    // References for AdViews
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize MobileAds (AdMob)
        MobileAds.initialize(this, getResources().getString(R.string.admob_app_id));

        // [START setup AdViews]
        // Setup banner ad
        mAdView = findViewById(R.id.activity_main_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Setup interstitial ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        // Load an interstitial ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        setupAdListener(mInterstitialAd);
        // [END setup AdViews]

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

    private void setupAdListener(InterstitialAd interstitialAd) {
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG,"onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d(TAG,"onAdFailedToLoad");

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d(TAG,"onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG,"onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                Log.d(TAG,"onAdClosed");

//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    /**
     * Method re-initializes game variables.
     */
    private void initializeForNewGame() {
        // Initialize gridMap with new mine/noMine values
        System.out.println("gridMap");
        grid  = new Grid(rows, mines);

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
        // Set last block clicked
        grid.setLastBlockClicked(row, column);

        // Set gameOverLoss to true
        grid.setGameOverLoss();

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
            // User has won, set game over win and update UI
            grid.setGameOverWin();
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
     * Method called when user presses new game button.
     */
    @Override
    public void onNewGameBtnPressed() {

        // Show interstitial ad
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        // New game started, initialize new game
        initializeForNewGame();
    }
}

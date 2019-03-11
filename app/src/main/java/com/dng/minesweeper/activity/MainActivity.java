package com.dng.minesweeper.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.MainFragment;
import com.dng.minesweeper.util.Grid;
import com.dng.minesweeper.util.InterstitialCounter;
import com.dng.minesweeper.util.Reason;
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

    // Reference for InterstitialCounter
    private InterstitialCounter mInterstitialCounter;

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
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_unit_id));
        // Load an interstitial ad
        loadNewInterstitialAd(mInterstitialAd);
        setupAdListener(mInterstitialAd);
        // [END setup AdViews]

        // Create InterstitialCounter object
        mInterstitialCounter = new InterstitialCounter();

        // Set member variables for new game
        initializeForNewGame();

        // Setup highscores button
        Button mViewHighscoresBtn = findViewById(R.id.activity_main_highscoresBtn);
        mViewHighscoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HighscoresActivity.class);
                startActivity(intent);
            }
        });
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

        // Create new instance of MainFragment and set it
        mainFragment = MainFragment.newInstance(grid, rows, mines);
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
     * Method handles displaying InterstitialAd.
     */
    private void handleInterstitialAd() {
        // Create Reason object
        Reason mReason = new Reason(grid);

        // Handle when neither gameOverWin nor gameOverLoss is true,
        // i.e.) user presses new game button when game isn't over
        if (mReason.getReason() == null) {
            return;
        }

        // Check if should show InterstitialAd
        if (mInterstitialCounter.shouldPresentAd(mReason)) {
            // Show interstitial ad
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
    }

    /**
     * Method handles InterstitialAd lifecycle.
     */
    private void setupAdListener(InterstitialAd interstitialAd) {
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG,"onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d(TAG,"onAdFailedToLoad");

            }

            @Override
            public void onAdOpened() {
                Log.d(TAG,"onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(TAG,"onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.d(TAG,"onAdClosed");

                // Load the next interstitial.
                loadNewInterstitialAd(mInterstitialAd);
            }
        });
    }

    /**
     * Method loads new InterstitialAd into the passed in InterstitialAd parameter.
     */
    private void loadNewInterstitialAd(InterstitialAd interstitialAd) {
        interstitialAd.loadAd(new AdRequest.Builder().build());
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
            // Handle updating mines count ImageView
            mainFragment.setupMinesCountView(false, false,false);
        } else {
            // Hide TextView, show ImageView, set flagVisible of block to true
            textView.setVisibility(View.INVISIBLE);
            flagImgView.setVisibility(View.VISIBLE);
            // Handle updating mines count ImageView
            mainFragment.setupMinesCountView(false, false,true);
        }

        // Update flagVisible map
        grid.setFlagVisible(row, column);
    }

    /**
     * Method called when user presses new game button.
     */
    @Override
    public void onNewGameBtnPressed() {

        // Handle displaying of InterstitialAd
        handleInterstitialAd();

        // New game started, initialize new game
        initializeForNewGame();
    }
}

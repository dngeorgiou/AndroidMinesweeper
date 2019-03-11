package com.dng.minesweeper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.HighscoresFragment;

public class HighscoresActivity extends AppCompatActivity implements HighscoresFragment.OnHighscoresFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        HighscoresFragment highscoresFragment = HighscoresFragment.newInstance();
        setFragment(highscoresFragment);
    }

    /**
     * Method takes in a fragment as a parameter and replaces MainActivity.java's container view with
     * it.
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_highscores_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackImgBtnPressed() {
        finish();
    }
}

package com.dng.minesweeper.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.DirectionsFragment;

public class DirectionsActivity extends AppCompatActivity implements DirectionsFragment.OnDirectionsFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        DirectionsFragment directionsFragment = DirectionsFragment.newInstance();
        setFragment(directionsFragment);
    }

    /**
     * Method takes in a fragment as a parameter and replaces MainActivity.java's container view with
     * it.
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_directions_frameLayout, fragment);
        fragmentTransaction.commit();
    }


}

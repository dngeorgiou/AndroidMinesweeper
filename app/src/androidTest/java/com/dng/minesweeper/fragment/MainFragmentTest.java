package com.dng.minesweeper.fragment;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.dng.minesweeper.R;
import com.dng.minesweeper.activity.MainActivity;
import com.dng.minesweeper.util.Grid;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MainFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mMainActivity = null;
    private Grid mGrid;

    @Before
    public void setUp() throws Exception {
        mMainActivity = mMainActivityActivityTestRule.getActivity();
        mGrid = new Grid(8, 8);
    }

    // Test if the fragment is successfully launched
    @Test
    public void testLaunch() {
        // Test Container view is not null
        FrameLayout frameLayout = mMainActivity.findViewById(R.id.activity_main_frameLayout);
        assertNotNull(frameLayout);

        // Test fragment vertical recycler view is not null
        MainFragment mainFragment = MainFragment.newInstance(mGrid, 0);

        FragmentTransaction fragmentTransaction = mMainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), mainFragment);
        fragmentTransaction.commit();

        // waits for fragment to load before continuing
        getInstrumentation().waitForIdleSync();

        View view = mainFragment.getView().findViewById(R.id.fragment_main_vertRecyclerView);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mMainActivityActivityTestRule = null;
        mMainActivity = null;
        mGrid = null;
    }
}
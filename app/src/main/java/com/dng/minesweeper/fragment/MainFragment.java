package com.dng.minesweeper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.activity.MainActivity;
import com.dng.minesweeper.adapter.HorizontalListAdapter;
import com.dng.minesweeper.adapter.VerticalListAdapter;
import com.dng.minesweeper.util.Grid;
import com.dng.minesweeper.util.SevenSeg;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnMainFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    // the fragment initialization parameters
    private static final String ARG_GRID = "grid";
    private static final String ARG_ROWS = "rows";
    private static final String ARG_MINES = "mines";

    private Grid mGrid;
    private int mRows = 0;
    private int mMines;

    private RecyclerView vertRecyclerView;
    private VerticalListAdapter verticalListAdapter;

    private ImageButton mNewGameImgBtn;

    private Context mContext;
    private OnMainFragmentListener mListener;

    private ImageView mMinesImgView;

    private ImageView mTimerImgView;
    private CountDownTimer timer;
    private SevenSeg sevenSeg;
    private boolean gameStarted = false;
    private int count = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of fragment using the provided parameters.
     *
     * @param grid Parameter 1.
     * @param rows Parameter 2.
     * @param mines Parameter 3.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(Grid grid, int rows, int mines) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GRID, grid);
        args.putInt(ARG_ROWS, rows);
        args.putInt(ARG_MINES, mines);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentListener) {
            this.mContext = context;
            mListener = (OnMainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGrid = (Grid) getArguments().getSerializable(ARG_GRID);
            mRows = getArguments().getInt(ARG_ROWS);
            mMines = getArguments().getInt(ARG_MINES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Instantiate VerticalListAdapter
        verticalListAdapter = new VerticalListAdapter(mContext, mListener, mGrid, mRows);

        // [START setup vertical recycler view]
        vertRecyclerView = view.findViewById(R.id.fragment_main_vertRecyclerView);
        vertRecyclerView.setHasFixedSize(true);
        vertRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        vertRecyclerView.setAdapter(verticalListAdapter);
        // [END setup vertical recycler view]

        // Setup newGame button
        mNewGameImgBtn = view.findViewById(R.id.fragment_main_newGameImgBtn);
        mNewGameImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNewGameBtnPressed();
            }
        });

        // Setup bombs ImageView
        mMinesImgView = view.findViewById(R.id.fragment_main_bombSevenSegImgView);
        setupMinesCountView(true, false);

        // Setup timer ImageView
        mTimerImgView = view.findViewById(R.id.fragment_main_timeSevenSegImgView);
        setupTimer();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setupMinesCountView(boolean initialSetup, boolean plusFlag) {
        if (initialSetup) {
            // Setup mines display to total mines on grid
            sevenSeg = new SevenSeg(mMines);
            int minesDrawableInt = sevenSeg.getDrawableResourceInt();
            mMinesImgView.setBackgroundResource(minesDrawableInt);
            return;
        }

        // Setup mines display to unflagged mines on grid
        if (plusFlag) {
            // User flagged block, subtract one mine from current mine count
            mMines = mMines - 1;
        } else {
            // User unflagged block, add one mine to current mine count
            mMines = mMines + 1;
        }
        // Update mine display
        sevenSeg = new SevenSeg(mMines);
        int minesDrawableInt = sevenSeg.getDrawableResourceInt();
        mMinesImgView.setBackgroundResource(minesDrawableInt);

    }

    private void setupTimer() {
        // Setup timer display to default to '000'
        sevenSeg = new SevenSeg(count);
        int timerDrawableInt = sevenSeg.getDrawableResourceInt();
        mTimerImgView.setBackgroundResource(timerDrawableInt);

        // Setup timer to run for 999 seconds with 1 second intervals
        timer = new CountDownTimer(999000, 1000) {
            @Override
            public void onTick(long l) {
                count = count + 1;
                sevenSeg = new SevenSeg(count);
                int timerDrawableInt = sevenSeg.getDrawableResourceInt();
                mTimerImgView.setBackgroundResource(timerDrawableInt);
                Log.d(TAG, "count: " + count);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "timer finished");
            }
        };
    }

    public void updateUI() {
        Log.d(TAG, "updateUI");
        verticalListAdapter.notifyDataSetChanged();

        // Start timer when user first clicks on block
        if (!gameStarted) {
            gameStarted = true;
            timer.start();
        }
    }

    // Update newGameImgBtn to display ic_face_win.png
    public void updateUIForWin() {
        mNewGameImgBtn.setBackgroundResource(R.drawable.new_game_from_win_selector);
        updateUI();

        // Stop timer when game is over
        timer.cancel();
    }

    // Update newGameImgBtn to display ic_face_loss.png
    public void updateUIForLoss() {
        mNewGameImgBtn.setBackgroundResource(R.drawable.new_game_from_loss_selector);
        updateUI();

        // Stop timer when game is over
        timer.cancel();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnMainFragmentListener {
        void onBlockPressed(int row, int column, TextView textView, ImageView mineImgView);

        void onBlockLongPressed(int row, int column, TextView textView, ImageView flagImgView);

        void onNewGameBtnPressed();
    }
}

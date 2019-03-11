package com.dng.minesweeper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.dng.minesweeper.R;
import com.dng.minesweeper.adapter.HighscoresListAdapter;
import com.dng.minesweeper.model.Player;
import com.dng.minesweeper.service.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HighscoresFragment.OnHighscoresFragmentListener} interface
 * to handle interaction events.
 * Use the {@link HighscoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HighscoresFragment extends Fragment {

    private static final String TAG = "HighscoresFragment";

    private Context mContext;
    private OnHighscoresFragmentListener mListener;

    public HighscoresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HighscoresFragment.
     */
    public static HighscoresFragment newInstance() {
        return new HighscoresFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHighscoresFragmentListener) {
            mContext = context;
            mListener = (OnHighscoresFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHighscoresFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_highscores, container, false);

        final ProgressBar mProgressBar = view.findViewById(R.id.fragment_highscores_progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        DataService.instance.getHighscores(new DataService.HighScoresInterface() {
            @Override
            public void getHighScoresComplete(List<Player> playerList) {
                mProgressBar.setVisibility(View.INVISIBLE);
                HighscoresListAdapter highscoresListAdapter = new HighscoresListAdapter(mContext, mListener, playerList);

                RecyclerView mRecyclerView = view.findViewById(R.id.fragment_highscores_recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mRecyclerView.setAdapter(highscoresListAdapter);
            }
        });

        ImageButton mBackImgBtn = view.findViewById(R.id.fragment_highscores_backImgBtn);
        mBackImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onBackImgBtnPressed();
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHighscoresFragmentListener {
        void onBackImgBtnPressed();
    }
}

package com.dng.minesweeper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dng.minesweeper.R;

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
        View view = inflater.inflate(R.layout.fragment_highscores, container, false);

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

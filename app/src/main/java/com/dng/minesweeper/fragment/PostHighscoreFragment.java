package com.dng.minesweeper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dng.minesweeper.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostHighscoreFragment.OnPostHighscoreFragmentListener} interface
 * to handle interaction events.
 * Use the {@link PostHighscoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostHighscoreFragment extends Fragment {

    private static final String TAG = "PostHighscoreFragment";

    // the fragment initialization parameters
    private static final String ARG_SCORE = "score";

    private long mScore;

    private Context mContext;
    private OnPostHighscoreFragmentListener mListener;

    public PostHighscoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param score Parameter 1.
     * @return A new instance of fragment PostHighscoreFragment.
     */
    public static PostHighscoreFragment newInstance(long score) {
        PostHighscoreFragment fragment = new PostHighscoreFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SCORE, score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostHighscoreFragmentListener) {
            mContext = context;
            mListener = (OnPostHighscoreFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPostHighscoreFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScore = getArguments().getLong(ARG_SCORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_highscore, container, false);

        ImageButton mCloseImgBtn = view.findViewById(R.id.fragment_post_highscore_closeImgBtn);
        mCloseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCloseImgBtnPressed();
            }
        });

        final EditText mDisplayNameEditText = view.findViewById(R.id.fragment_post_highscore_name_editText);
        Button mPostBtn = view.findViewById(R.id.fragment_post_highscore_postBtn);
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displayName = mDisplayNameEditText.getText().toString();
                mListener.onPostBtnPressed(displayName, mScore);
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
    public interface OnPostHighscoreFragmentListener {
        void onCloseImgBtnPressed();

        void onPostBtnPressed(String displayName, long score);
    }
}
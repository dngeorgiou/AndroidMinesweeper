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

import com.dng.minesweeper.R;
import com.dng.minesweeper.adapter.HorizontalListAdapter;
import com.dng.minesweeper.adapter.VerticalListAdapter;

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
    private static final String ARG_MAP = "map";

    private HashMap<Integer, Integer> mMap = new HashMap<>();

    private Context mContext;
    private OnMainFragmentListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(HashMap<Integer, Integer> map) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MAP, map);
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
            mMap = (HashMap<Integer, Integer>) getArguments().getSerializable(ARG_MAP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // instantiate VerticalListAdapter
        VerticalListAdapter verticalListAdapter = new VerticalListAdapter(mListener);
        verticalListAdapter.mContext = mContext;

        // [START setup vertical recycler view]
        RecyclerView vertRecyclerView = view.findViewById(R.id.fragment_main_vertRecyclerView);
        vertRecyclerView.setHasFixedSize(true);
        vertRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        vertRecyclerView.setAdapter(verticalListAdapter);
        // [END setup vertical recycler view]

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
    public interface OnMainFragmentListener {
        void onBlockPressed(int row, int column);
    }
}

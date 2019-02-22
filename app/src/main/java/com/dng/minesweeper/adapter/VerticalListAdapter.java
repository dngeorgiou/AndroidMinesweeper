package com.dng.minesweeper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dng.minesweeper.R;
import com.dng.minesweeper.activity.MainActivity;
import com.dng.minesweeper.fragment.MainFragment;

import java.util.HashMap;

public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.ViewHolder> {

    private static final String TAG = "VerticalListAdapter";

    public Context mContext;

    private int mTotalRows;
    private HashMap<Integer, Integer> mMap;

    private MainFragment.OnMainFragmentListener mListener;

    public VerticalListAdapter(MainFragment.OnMainFragmentListener listener, int totalRows,
                               HashMap<Integer, Integer> map) {
        this.mListener = listener;
        mTotalRows = totalRows;
        mMap = map;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // instantiate HorizontalListAdapter
        HorizontalListAdapter horizontalListAdapter = new HorizontalListAdapter(mListener, mTotalRows,
                position, mMap);
        horizontalListAdapter.mContext = mContext;

        // [START setup horizontal recycler view]
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.mRecyclerView.setAdapter(horizontalListAdapter);
        // [END setup horizontal recycler view]

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, String.valueOf(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTotalRows;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        private RecyclerView mRecyclerView;

        private ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mRecyclerView = mView.findViewById(R.id.vertical_recycler_view_horizRecyclerView);

        }
    }

}

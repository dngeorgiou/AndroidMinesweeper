package com.dng.minesweeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.activity.MainActivity;
import com.dng.minesweeper.fragment.MainFragment;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ViewHolder> {

    private static final String TAG = "HorizontalListAdapter";

    public Context mContext;

    private MainFragment.OnMainFragmentListener mListener;

    private int row;

    public HorizontalListAdapter(MainFragment.OnMainFragmentListener listener, int row) {
        this.mListener = listener;
        this.row = row;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // [START set height and width of block cells]
        // get width of screen and divide by number of block cells in one row
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int blockwidth = displaymetrics.widthPixels / MainActivity.rows;
        // set height and width of block cells equal to width of screen divided by number of block cells in one row
        holder.mView.getLayoutParams().width = blockwidth;
        holder.mView.getLayoutParams().height = blockwidth;
        // [END set height and width of block cells]

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, String.valueOf(holder.getAdapterPosition()));
                int i = row;
                Log.d(TAG, "row: " + String.valueOf(row));
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.rows;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView mTextView;

        private ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mTextView = mView.findViewById(R.id.list_item_textView);

        }
    }

}

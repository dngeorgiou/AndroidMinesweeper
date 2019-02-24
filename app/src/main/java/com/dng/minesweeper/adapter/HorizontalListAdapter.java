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
import android.widget.ImageView;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.activity.MainActivity;
import com.dng.minesweeper.fragment.MainFragment;
import com.dng.minesweeper.util.Grid;

import java.util.HashMap;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ViewHolder> {

    private static final String TAG = "HorizontalListAdapter";

    public Context mContext;

    private MainFragment.OnMainFragmentListener mListener;

    private int mTotalRows;
    private int mMines;
    private int mCurrentRow;

    public HorizontalListAdapter(MainFragment.OnMainFragmentListener listener, int totalRows, int mines, int currentRow) {
        this.mListener = listener;
        mTotalRows = totalRows;
        mMines = mines;
        mCurrentRow = currentRow;
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
        int blockwidth = displaymetrics.widthPixels / mTotalRows;
        // set height and width of block cells equal to width of screen divided by number of block cells in one row
        holder.mView.getLayoutParams().width = blockwidth;
        holder.mView.getLayoutParams().height = blockwidth;
        // [END set height and width of block cells]

        if (MainActivity.shouldShow[mCurrentRow][position]) {
            int val = MainActivity.surroundingMap[mCurrentRow][position];
            if (val == -1) {
                // Block at this location has zero surrounding mines
                holder.mTextView.setText("");
            } else {
                // Block at this location has more than zero surrounding mines
                holder.mTextView.setText(String.valueOf(val));
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.gameOver) {
                    mListener.onBlockPressed(mCurrentRow, holder.getAdapterPosition(), holder.mTextView);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!MainActivity.gameOver) {
                    if (MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()]) {
                        holder.mImgView.setVisibility(View.INVISIBLE);
                        holder.mTextView.setVisibility(View.VISIBLE);
                        MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()] = false;
                    } else {
                        holder.mImgView.setVisibility(View.VISIBLE);
                        holder.mTextView.setVisibility(View.INVISIBLE);
                        MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()] = true;
                    }
                    Log.d(TAG, "longClicked");
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTotalRows;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView mTextView;
        private ImageView mImgView;

        private ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mTextView = mView.findViewById(R.id.list_item_textView);
            mImgView = mView.findViewById(R.id.list_item_imgView);

        }
    }

}

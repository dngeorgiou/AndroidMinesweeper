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
    private int mCurrentRow;

    public HorizontalListAdapter(MainFragment.OnMainFragmentListener listener, int totalRows, int currentRow) {
        this.mListener = listener;
        mTotalRows = totalRows;
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
        setBlockHeightAndWidth(holder);
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
        } else if (MainActivity.flagVisible[mCurrentRow][position]) {
            // Fixes issue of flag textView being set visible and imgView being set invisible when
            // user clicks on a new block
            holder.mFlagImgView.setVisibility(View.VISIBLE);
            holder.mTextView.setVisibility(View.INVISIBLE);
        }

        // View is clicked, handle it appropriately
        handleOnClicks(holder);

    }

    @Override
    public int getItemCount() {
        return mTotalRows;
    }

    private void setBlockHeightAndWidth(ViewHolder holder) {
        // get width of screen and divide by number of block cells in one row
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        // set height and width of block cells equal to width of screen divided by number of block cells in one row
        int blockwidth = displaymetrics.widthPixels / mTotalRows;
        holder.mView.getLayoutParams().width = blockwidth;
        holder.mView.getLayoutParams().height = blockwidth;
    }

    /**
     * Method checks if clickListener and longClickListener are already attached, and sets click/longClick
     * listeners if they have not, and performs click/longClick if they have.
     * This improves performance by not setting new click/longClick listeners every time the UI is updated,
     * which is often.
     */
    private void handleOnClicks(ViewHolder holder) {
        if (!holder.mView.hasOnClickListeners()) {
            // Click/LongClick listeners have not been set, set them

            // Set clickListener
            setOnClickListener(holder);

            // Set longClickListener
            setOnLongClickListener(holder);

        } else {
            // Click/LongClick listeners have been set, perform click
            holder.mView.performClick();
        }

    }

    private void setOnClickListener(final ViewHolder holder) {
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowClick(holder)) {
                    // Game is not over, block not displayed, no flag on block -> allow click
                    mListener.onBlockPressed(mCurrentRow, holder.getAdapterPosition(), holder.mTextView);
                }
            }
        });
    }

    private void setOnLongClickListener(final ViewHolder holder) {
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (allowLongClick(holder)) {
                    // Game is not over, block not displayed -> allow long click
                    if (MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()]) {
                        holder.mFlagImgView.setVisibility(View.INVISIBLE);
                        holder.mTextView.setVisibility(View.VISIBLE);
                        MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()] = false;
                    } else {
                        holder.mFlagImgView.setVisibility(View.VISIBLE);
                        holder.mTextView.setVisibility(View.INVISIBLE);
                        MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()] = true;
                    }
                    Log.d(TAG, "longClicked");
                }
                return true;
            }
        });
    }

    private boolean allowClick(ViewHolder holder) {
        if (!MainActivity.gameOver &&
                !MainActivity.shouldShow[mCurrentRow][holder.getAdapterPosition()] &&
                !MainActivity.flagVisible[mCurrentRow][holder.getAdapterPosition()]) {
            return true;
        } else {
            return false;
        }
    }

    private boolean allowLongClick(ViewHolder holder) {
        if (!MainActivity.gameOver &&
                !MainActivity.shouldShow[mCurrentRow][holder.getAdapterPosition()]) {
            return true;
        } else {
            return false;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView mTextView;
        private ImageView mFlagImgView;

        private ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mTextView = mView.findViewById(R.id.list_item_textView);
            mFlagImgView = mView.findViewById(R.id.list_item_flagImgView);

        }
    }

}

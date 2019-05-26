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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.activity.MainActivity;
import com.dng.minesweeper.fragment.MainFragment;
import com.dng.minesweeper.util.Grid;

import java.util.HashMap;

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.ViewHolder> {

    private static final String TAG = "HorizontalListAdapter";

    private Context mContext;

    private MainFragment.OnMainFragmentListener mListener;

    private Grid mGrid;
    private int mTotalRows;
    private int mCurrentRow;

    public HorizontalListAdapter(Context context, MainFragment.OnMainFragmentListener listener,
                                 Grid grid, int totalRows, int currentRow) {
        this.mContext = context;
        this.mListener = listener;
        this.mGrid = grid;
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

        // Set height and width of block cells
        setBlockHeightAndWidth(holder, mTotalRows);

        // Setup and handle onClick/onLongClick listeners
        handleOnClicks(holder, mCurrentRow, position);

        // Update UI
        updateUI(holder, mCurrentRow, position);
    }

    @Override
    public int getItemCount() {
        return mTotalRows;
    }

    private void updateUI(ViewHolder holder, int row, int column) {

        // [START handle game over win]
        if (mGrid.getGameOverWin()) {
            updateUIForGameOverWin(holder, row, column);
        }
        // [END handle game over win]

        // [START handle game over loss]
        if (mGrid.getGameOverLoss()) {
            updateUIForGameOverLoss(holder, row, column);
        }
        // [END handle game over loss]

        updateUIForGameInProgress(holder, row, column);
    }

    /**
     * Method handles updating UI for when user has uncovered all blocks not containing a mine
     */
    private void updateUIForGameOverWin(ViewHolder holder, int row, int column) {
        if (mGrid.getMineMap()[row][column] == 1) {
            // Block contains a mine

            // [START display flags on mines]
            if (!mGrid.getFlagVisible()[row][column]) {
                // Block contains a mine and has not been flagged, display flags
                holder.mTextView.setVisibility(View.INVISIBLE);
                holder.mFlagImgView.setVisibility(View.VISIBLE);
            }
            // [END display flags]
        }
    }

    /**
     * Method handles updating UI when user clicks on mine:
     * 1. Display ic_mine.png with red background on last clicked block.
     * 2. Display ic_mine_w_x.png on blocks which have been incorrectly flagged.
     * 3. Display all other mines.
     */
    private void updateUIForGameOverLoss(ViewHolder holder, int row, int column) {
        if (mGrid.getMineMap()[row][column] == 1) {
            // Block contains a mine

            // [START handle correct flags]
            if (mGrid.getFlagVisible()[row][column]) {
                // Block was correctly flagged, keep flag visible
                return;
            }
            // [END handle correct flags]

            // Set red background on block user clicked containing mine
            if (isLosingBlock(row, column)) {
                setBackgroundColor(holder.mMineImgView,
                        mContext.getResources().getColor(R.color.mineBackgroundRed));
            }

            // Display mines
            holder.mTextView.setVisibility(View.INVISIBLE);
            holder.mMineImgView.setVisibility(View.VISIBLE);

        } else {
            // Block does not contain mine, check if incorrectly flagged
            handleIncorrectFlags(holder, row, column);
        }
    }

    private void updateUIForGameInProgress(ViewHolder holder, int row, int column) {
        if (mGrid.getShouldShow()[row][column]) {
            // Get number of mines surrounding block
            int val = mGrid.getSurroundingMines()[row][column];
            // Set text and text color representing blocks surrounding mines
            setTextAndTextColor(holder, val);

            // Set view state as selected
            setAsSelected(holder);
        } else if (mGrid.getFlagVisible()[row][column]) {
            // Fixes issue of flag textView being set visible and imgView being set invisible when
            // user clicks on a new block
            holder.mFlagImgView.setVisibility(View.VISIBLE);
            holder.mTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method returns true if current block is the block user clicked on which contained a mine,
     * returns false otherwise
     */
    private boolean isLosingBlock(int row, int column) {
        return (row == mGrid.getLastRowClicked() && column == mGrid.getLastColumnClicked());
    }

    private void setBackgroundColor(ImageView imageView, int colorID) {
        imageView.setBackgroundColor(colorID);
    }

    /**
     * Method handles setting UI if block has been incorrectly flagged
     * i.e.) block not containing mine was flagged
     */
    private void handleIncorrectFlags(ViewHolder holder, int row, int column) {
        if (mGrid.getMineMap()[row][column] == 0) {
            if (mGrid.getFlagVisible()[row][column]) {
                // Block was incorrectly flagged, display ic_mine_w_x.png
                holder.mMineWXImgView.setVisibility(View.VISIBLE);
                holder.mFlagImgView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Method sets height and width of each block by getting the width of the screen, dividing that
     * by the number of rows, and setting that equal to height and width.
     */
    private void setBlockHeightAndWidth(ViewHolder holder, int rows) {
        // get width of screen and divide by number of block cells in one row
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        // set height and width of block cells equal to width of screen divided by number of block cells in one row
        // Get the screen's density scale
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int uiPadding = (int) (62 * scale);     // 31dp padding on left + 31dp padding on right = 62dp
        int blockwidth = (displaymetrics.widthPixels - uiPadding) / rows;
        holder.mView.getLayoutParams().width = blockwidth;
        holder.mView.getLayoutParams().height = blockwidth;
    }

    /**
     * Method sets clickListener and longClickListener.
     */
    private void handleOnClicks(ViewHolder holder, int row, int column) {
        // Set clickListener
        setOnClickListener(holder, row, column);

        // Set longClickListener
        setOnLongClickListener(holder);
    }

    private void setOnClickListener(final ViewHolder holder, int row, int column) {
        if (!mGrid.getShouldShow()[row][column]) {
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (allowClick(holder)) {
                        Log.d(TAG, "clicked");
                        // Game is not over, block not displayed, no flag on block -> allow click
                        mListener.onBlockPressed(mCurrentRow, holder.getAdapterPosition(),
                                holder.mTextView, holder.mMineImgView);
                    }
                }
            });
        }
    }

    private void setOnLongClickListener(final ViewHolder holder) {
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (allowLongClick(holder)) {
                    Log.d(TAG, "longClicked");
                    // Game is not over, block not displayed -> allow long click
                    mListener.onBlockLongPressed(mCurrentRow, holder.getAdapterPosition(),
                            holder.mTextView, holder.mFlagImgView);
                }
                return true;
            }
        });
    }

    /**
     * Method returns true when all conditions are met:
     * 1: Game not over from loss
     * 2: Game not over from win
     * 3: Block is not already displayed
     * 4: Flag not set on block
     */
    private boolean allowClick(ViewHolder holder) {
        return (!mGrid.getGameOverLoss() && !mGrid.getGameOverWin() &&
                !mGrid.getShouldShow()[mCurrentRow][holder.getAdapterPosition()] &&
                !mGrid.getFlagVisible()[mCurrentRow][holder.getAdapterPosition()]);
    }

    /**
     * Method returns true when all conditions are met:
     * 1: Game not over from loss
     * 2: Game not over from win
     * 3: Block is not already displayed
     */
    private boolean allowLongClick(ViewHolder holder) {
        return (!mGrid.getGameOverLoss() && !mGrid.getGameOverWin() &&
                !mGrid.getShouldShow()[mCurrentRow][holder.getAdapterPosition()]);
    }

    /**
     * Method sets state of view to 'pressed', which updates background resource to ic_block_pressed_background.png,
     * as view is using block_background_selector.xml resource.
     */
    private void setAsSelected(ViewHolder holder) {
        holder.mView.setSelected(true);
    }

    /**
     * Method sets text of TextView to the number of mines surrounding the block, and its text color
     * respective to the number of mines
     * KEY:  1: blue    2: green    3: red    4: darkBlue    5: brown    6: cyan    7: black    8: grey
     * @param holder ViewHolder of current block
     * @param surroundingMines Number of mines surrounding current block
     */
    private void setTextAndTextColor(ViewHolder holder, int surroundingMines) {
        // [START set TextView text]
        if (surroundingMines == -1) {
            // Block has zero surrounding mines, set text to empty string
            holder.mTextView.setText("");
        } else {
            // Block has at least one surrounding mine, set text to number of surrounding mines
            holder.mTextView.setText(String.valueOf(surroundingMines));
        }
        // [END set TextView text]

        // [START set TextView textColor]
        if (surroundingMines == 1) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapOne));
            return;
        }

        if (surroundingMines == 2) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapTwo));
            return;
        }

        if (surroundingMines == 3) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapThree));
            return;
        }

        if (surroundingMines == 4) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapFour));
            return;
        }

        if (surroundingMines == 5) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapFive));
            return;
        }

        if (surroundingMines == 6) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapSix));
            return;
        }

        if (surroundingMines == 7) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapSeven));
            return;
        }

        if (surroundingMines == 8) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.surroundingMapEight));
        }
        // [END set TextView textColor]

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTextView;
        private ImageView mFlagImgView;
        private ImageView mMineImgView;
        private ImageView mMineWXImgView;

        private ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mTextView = mView.findViewById(R.id.list_item_textView);
            mFlagImgView = mView.findViewById(R.id.list_item_flagImgView);
            mMineImgView = mView.findViewById(R.id.list_item_mineImgView);
            mMineWXImgView = mView.findViewById(R.id.list_item_mineWXImgView);
        }
    }

}

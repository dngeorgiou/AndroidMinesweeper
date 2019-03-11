package com.dng.minesweeper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dng.minesweeper.R;
import com.dng.minesweeper.fragment.HighscoresFragment;
import com.dng.minesweeper.model.Player;

import java.util.List;

public class HighscoresListAdapter extends RecyclerView.Adapter<HighscoresListAdapter.ViewHolder> {

    private static final String TAG = "HighscoresListAdapter";

    private Context mContext;
    private HighscoresFragment.OnHighscoresFragmentListener mListener;
    private List<Player> playerList;

    public HighscoresListAdapter(Context context, HighscoresFragment.OnHighscoresFragmentListener listener,
                                 List<Player> playerList) {
        this.mContext = context;
        this.mListener = listener;
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highscore_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Set rank text view
        holder.mRankTextView.setText(String.valueOf(position+1));

        // Set display name text view
        String displayName = playerList.get(position).getDisplayName();
        holder.mDisplayNameTextView.setText(displayName);

        // Set score text view
        long score = playerList.get(position).getScore();
        holder.mScoreTextView.setText(String.valueOf(score));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mRankTextView;
        private TextView mDisplayNameTextView;
        private TextView mScoreTextView;

        private ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mRankTextView = mView.findViewById(R.id.highscore_list_item_rankingTextView);
            mDisplayNameTextView = mView.findViewById(R.id.highscore_list_item_nameTextView);
            mScoreTextView = mView.findViewById(R.id.highscore_list_item_scoreTextView);
        }
    }
}

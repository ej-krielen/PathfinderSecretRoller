package nl.rekijan.pathfindersecretroller.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.MainActivity;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.PlayerModel;
import nl.rekijan.pathfindersecretroller.ui.fragments.EditPlayerFragment;

/**
 * Custom RecyclerView.Adapter for the PlayerModel class
 *
 * @see AppExtension
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.SkillViewHolder> {

    private ArrayList<PlayerModel> players = new ArrayList<>();

    public void add(PlayerModel player) { players.add(player); }

    public void addAll(List<PlayerModel> players) {
        this.players.addAll(players);
    }

    /**
     * Remove a player based on position
     *
     * @param position
     */
    public void remove(int position) {
        if (players.size() > position) {
            players.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * Remove based on the player given
     *
     * @param player
     */
    public void remove(PlayerModel player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == player) {
                remove(i);
            }
        }
    }

    public ArrayList<PlayerModel> getList() {
        return players;
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /* ViewHolder region */
    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        CardView playerCardView;
        TextView playerNameTextView;
        Button editPlayerButton;

        SkillViewHolder(View itemView) {
            super(itemView);
            playerCardView = itemView.findViewById(R.id.player_cardView);
            playerNameTextView = itemView.findViewById(R.id.player_name_textView);
            editPlayerButton = itemView.findViewById(R.id.edit_player_button);
        }
    }
    /* End of Viewholder region */

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_player, parent, false);
        return new SkillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.SkillViewHolder holder, final int position) {

        final PlayerModel player = players.get(position);
        holder.playerNameTextView.setText(player.getName());
        holder.editPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                activity.replaceFragment(EditPlayerFragment.newInstance(position));
            }
        });
    }
}
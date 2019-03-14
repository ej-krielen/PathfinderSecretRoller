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

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.PlayerModel;
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

/**
 * Custom RecyclerView.Adapter for results of skills being rolled
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.PlayerViewHolder> {

    private final AppExtension app;
    private final String skillName;
    // Field for the list of PlayerModels
    private ArrayList<PlayerModel> players = new ArrayList<>();

    public ResultAdapter(AppExtension app, ArrayList<PlayerModel> players, String skillName) {
        this.app = app;
        this.players = players;
        this.skillName = skillName;
    }

    public ArrayList<PlayerModel> getList() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerModel> players) {
        this.players = players;
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * Rolls dice for each player with the modifier for the chosen skill and save the result back in the {@link PlayerModel}
     */
    public void rollDice() {
        for (PlayerModel p : players) {

            SkillModel playerSkill = null;
            for (SkillModel s : p.getSkillModels()) {
                if (s.getName().equals(skillName)) {
                    playerSkill = s;
                }
            }
            int diceRoll = CommonUtil.getInstance(app).rollD20();
            int endResult = diceRoll + (playerSkill != null ? playerSkill.getModifier() : 0);
            p.setDiceRoll(diceRoll);
            p.setResult(endResult);
        }
        notifyDataSetChanged();

    }

    /**
     * Reroll for just the given {@link PlayerModel} for the chosen skill and save the result back in the model
     * @param player
     */
    private void rerollDice(PlayerModel player) {

        SkillModel playerSkill = null;
        for (SkillModel s : player.getSkillModels()) {
            if (s.getName().equals(skillName)) {
                playerSkill = s;
            }
        }
        int diceRoll = CommonUtil.getInstance(app).rollD20();
        int endResult = diceRoll + (playerSkill != null ? playerSkill.getModifier() : 0);
        player.setDiceRoll(diceRoll);
        player.setResult(endResult);
        calculateSuccess(app.getDCvalue());

        notifyDataSetChanged();
    }

    /**
     * Compare the result for each player with the given DC
     * @param dc stored value in {@link AppExtension}
     */
    public void calculateSuccess(int dc) {
        for (PlayerModel p : players) {
            int result = p.getResult();
            if (result >= dc) {
                if ((result-10) >= dc)
                {
                    p.setSuccess(app.getString(R.string.result_critical_success));
                } else {
                    p.setSuccess(app.getString(R.string.result_success));
                }
            } else {
                if (result <= (dc-10)) {
                    p.setSuccess(app.getString(R.string.result_critical_fail));
                } else {
                    p.setSuccess(app.getString(R.string.result_failed));
                }
            }

            //Last because no matter what the result was, a natural 1/20 is always a critical fail/success
            if (p.getDiceRoll() == 1){
                p.setSuccess(app.getString(R.string.result_natural_one));
            } else if (p.getDiceRoll() == 20) {
                p.setSuccess(app.getString(R.string.result_natural_twenty));
            }
        }
        notifyDataSetChanged();
    }

    /* ViewHolder region */
    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        CardView resultCardView;
        TextView playerNameTextView;
        TextView proficiencyTextView;
        TextView modifierTextView;
        TextView dicerollTextView;
        TextView endResultTextView;
        TextView successTextView;
        Button rerollPlayerButton;

        PlayerViewHolder(View itemView) {
            super(itemView);
            resultCardView = itemView.findViewById(R.id.result_cardView);
            playerNameTextView = itemView.findViewById(R.id.result_player_name_textView);
            proficiencyTextView = itemView.findViewById(R.id.result_player_proficiency_textView);
            modifierTextView = itemView.findViewById(R.id.result_player_modifier_textView);
            dicerollTextView = itemView.findViewById(R.id.result_player_dice_roll_textView);
            endResultTextView = itemView.findViewById(R.id.result_player_check_result_textView);
            successTextView = itemView.findViewById(R.id.result_player_outcome_textView);
            rerollPlayerButton = itemView.findViewById(R.id.reroll_player_button);
        }
    }
    /* End of Viewholder region */

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_result, parent, false);
        return new PlayerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultAdapter.PlayerViewHolder holder, final int position) {

        final PlayerModel player = players.get(position);
        SkillModel playerSkill = null;
        for (SkillModel s : player.getSkillModels()) {
            if (s.getName().equals(skillName)) {
                playerSkill = s;
            }
        }
        holder.playerNameTextView.setText(player.getName());
        if (playerSkill != null) {

            holder.proficiencyTextView.setText(playerSkill.getProficiency());
            holder.modifierTextView.setText(String.valueOf(playerSkill.getModifier()));
            holder.dicerollTextView.setText(String.valueOf(player.getDiceRoll()));
            holder.endResultTextView.setText(String.valueOf(player.getResult()));
            holder.successTextView.setText(player.getSuccess());

            holder.rerollPlayerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rerollDice(player);
                }
            });
        }
    }
}
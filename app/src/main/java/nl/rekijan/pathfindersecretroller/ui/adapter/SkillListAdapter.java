package nl.rekijan.pathfindersecretroller.ui.adapter;

import android.content.Intent;
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
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.ui.fragments.ResultActivity;

import static nl.rekijan.pathfindersecretroller.AppConstants.SKILL_NAME;

/**
 * Custom RecyclerView.Adapter for the SkillModel class to interact with and with minimal info
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @see AppExtension
 * @since 3-3-2019
 */
public class SkillListAdapter extends RecyclerView.Adapter<SkillListAdapter.SkillViewHolder> {

    private final AppExtension app;
    private ArrayList<SkillModel> skills = new ArrayList<>();

    public SkillListAdapter(AppExtension app) {
        this.app = app;
    }

    public void add(SkillModel skill) {
        skills.add(skill);
    }

    public void addAll(List<SkillModel> skills) {
        this.skills.addAll(skills);
    }

    /**
     * Remove a skill based on position
     *
     * @param position
     */
    public void remove(int position) {
        if (skills.size() > position) {
            skills.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * Remove based on the skill given
     *
     * @param skill
     */
    public void remove(SkillModel skill) {
        for (int i = 0; i < skills.size(); i++) {
            if (skills.get(i) == skill) {
                remove(i);
            }
        }
    }

    public ArrayList<SkillModel> getList() {
        return skills;
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    /* ViewHolder region */
    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        CardView skillCardView;
        TextView skillNameTextView;
        Button rollPartyButton;

        SkillViewHolder(View itemView) {
            super(itemView);
            skillCardView = itemView.findViewById(R.id.skill_cardView);
            skillNameTextView = itemView.findViewById(R.id.skill_name_textView);
            rollPartyButton = itemView.findViewById(R.id.skill_roll_party_button);
        }
    }
    /* End of Viewholder region */

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_skill, parent, false);
        return new SkillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillListAdapter.SkillViewHolder holder, int position) {
        final SkillModel skill = skills.get(position);
        holder.skillNameTextView.setText(skill.getName());
        holder.rollPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(app, ResultActivity.class);
                intent.putExtra(SKILL_NAME, skill.getName());
                app.startActivity(intent);
            }
        });
    }
}
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
import nl.rekijan.pathfindersecretroller.MainActivity;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.ui.fragments.EditSkillFragment;

/**
 * Custom RecyclerView.Adapter for the SkillModel class to go to its edit activity
 *
 * @see EditSkillFragment
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class SkillEditAdapter extends RecyclerView.Adapter<SkillEditAdapter.SkillViewHolder> {

    private ArrayList<SkillModel> skills;

    public SkillEditAdapter(AppExtension app) {
        skills = app.getSkillAdapter().getList();
    }

    public ArrayList<SkillModel> getList() {
        return skills;
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void setList(ArrayList<SkillModel> list) {
        skills = list;
    }

    /* ViewHolder region */
    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        CardView skillCardView;
        TextView skillNameTextView;
        Button editSkillButton;

        SkillViewHolder(View itemView) {
            super(itemView);
            skillCardView = itemView.findViewById(R.id.skill_edit_cardView);
            skillNameTextView = itemView.findViewById(R.id.skill_name_edit_textView);
            editSkillButton = itemView.findViewById(R.id.edit_skill_button);
        }
    }
    /* End of Viewholder region */

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_skill_edit, parent, false);
        return new SkillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillEditAdapter.SkillViewHolder holder, final int position) {
        final SkillModel skill = skills.get(position);
        holder.skillNameTextView.setText(skill.getName());
        holder.editSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                activity.replaceFragment(EditSkillFragment.newInstance(position));
            }
        });
    }
}
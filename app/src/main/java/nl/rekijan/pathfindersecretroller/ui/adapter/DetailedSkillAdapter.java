package nl.rekijan.pathfindersecretroller.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.listener.GenericTextWatcher;
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.ui.fragments.EditPlayerFragment;

/**
 * Custom RecyclerView.Adapter for the SkillModel class with full details
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @see EditPlayerFragment
 * @since 3-3-2019
 */
public class DetailedSkillAdapter extends RecyclerView.Adapter<DetailedSkillAdapter.SkillViewHolder> {

    private final AppExtension app;
    // Field for the list of SkillModels
    private ArrayList<SkillModel> skills = new ArrayList<>();

    public DetailedSkillAdapter(AppExtension app) {
        this.app = app;
    }

    public ArrayList<SkillModel> getList() {
        return skills;
    }

    public void setList(ArrayList<SkillModel> skills) {
        this.skills = skills;
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    /* ViewHolder region */
    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        CardView skillCardView;
        TextView skillNameTextView;
        Spinner skillProficiencySpinner;
        EditText skillModifierEditText;


        SkillViewHolder(View itemView) {
            super(itemView);
            skillCardView = itemView.findViewById(R.id.detailed_skill_recyclerView);
            skillNameTextView = itemView.findViewById(R.id.skill_name_textView);
            skillProficiencySpinner = itemView.findViewById(R.id.skill_proficiency_spinner);
            skillModifierEditText = itemView.findViewById(R.id.skill_modifier_editText);
        }
    }
    /* End of Viewholder region */

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detailed_skill, parent, false);
        return new SkillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedSkillAdapter.SkillViewHolder holder, int position) {
        //Remove watcher if they exist to avoid double watchers
        GenericTextWatcher oldModifierWatcher = (GenericTextWatcher) holder.skillModifierEditText.getTag();
        if (oldModifierWatcher != null) {
            holder.skillModifierEditText.removeTextChangedListener(oldModifierWatcher);
        }
        final SkillModel skill = skills.get(position);
        holder.skillNameTextView.setText(skill.getName());
        holder.skillModifierEditText.setText(String.valueOf(skill.getModifier()));


        final String[] difficulty = new String[]{app.getString(R.string.proficiency_untrained), app.getString(R.string.proficiency_trained), app.getString(R.string.proficiency_expert), app.getString(R.string.proficiency_master), app.getString(R.string.proficiency_legendary)};
        ArrayAdapter<String> difficultySpinnerAdapter = new ArrayAdapter<>(app, R.layout.spinner_item, difficulty);

        holder.skillProficiencySpinner.setAdapter(difficultySpinnerAdapter);
        // Drop down layout style
        difficultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the value to the default or last picked value
        int currentPos = 0;
        for (int i = 0; i < difficulty.length; i++){
            if (skill.getProficiency().equals(difficulty[i])) {
                currentPos = i;
                break;
            }
        }
        holder.skillProficiencySpinner.setSelection(currentPos);

        //When a new option is picked save it and recalculate the preview DC
        holder.skillProficiencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                skill.setProficiency(difficulty[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Add new text watcher
        GenericTextWatcher newModifierWatcher = new GenericTextWatcher(skill, holder.skillModifierEditText);
        holder.skillModifierEditText.setTag(newModifierWatcher);
        holder.skillModifierEditText.addTextChangedListener(newModifierWatcher);
    }
}
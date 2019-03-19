package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.PlayerModel;
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.ui.adapter.SkillEditAdapter;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

/**
 * Class to manage the skills as a whole. From here user can add new skills or edit existing.
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 4-3-2019
 */
public class SkillsManagerFragment extends Fragment {

    private SkillEditAdapter mAdapter;

    public static SkillsManagerFragment newInstance() {
        return new SkillsManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_skills_manager, container, false);
        final AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        mAdapter = new SkillEditAdapter(app);

        RecyclerView skillListRecyclerView = fragmentView.findViewById(R.id.skills_recyclerView);
        skillListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        skillListRecyclerView.setLayoutManager(llm);
        skillListRecyclerView.setAdapter(mAdapter);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        mAdapter.setList(app.getSkillAdapter().getList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_skills, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add_skill:
                addSkill();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Make a new {@link SkillModel} with a unique name, add it to existing players, refresh the adapter and save the data
     */
    private void addSkill(){
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        //Make sure new skill name is unique. Defaults to "Skill X", where X is a number counting up
        String skillName;
        int itemCount = app.getSkillAdapter().getItemCount()+1;

        do {
            skillName = "Skill " + itemCount;
            itemCount++;
        }
        while (!CommonUtil.getInstance().isSkillNameUnique(skillName, app.getSkillAdapter().getList()));

        //Make a new SkillModel and add it to each existing player
        SkillModel newSkill = new SkillModel(skillName);

        app.getSkillAdapter().add(newSkill);
        for (PlayerModel p : app.getPlayerAdapter().getList()){
            p.getSkillModels().add(newSkill);
        }
        //Add it to the adapter, refresh it, and save all data.
        app.getSkillAdapter().notifyDataSetChanged();
        mAdapter.setList(app.getSkillAdapter().getList());
        mAdapter.notifyDataSetChanged();
        app.saveData();
    }
}
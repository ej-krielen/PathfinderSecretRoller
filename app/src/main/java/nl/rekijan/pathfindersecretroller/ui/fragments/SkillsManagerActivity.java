package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
public class SkillsManagerActivity extends AppCompatActivity {

    private SkillEditAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Back to parent activity

        AppExtension app = (AppExtension) this.getApplicationContext();
        mAdapter = new SkillEditAdapter(app);

        RecyclerView skillListRecyclerView = findViewById(R.id.skills_recyclerView);
        skillListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        skillListRecyclerView.setLayoutManager(llm);
        skillListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppExtension app = (AppExtension) this.getApplicationContext();
        mAdapter.setList(app.getSkillAdapter().getList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skills, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add_skill:
                addSkill();
                return true;
            case R.id.menu_action_about:
                CommonUtil.getInstance(SkillsManagerActivity.this).aboutInfo(SkillsManagerActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Make a new {@link SkillModel} with a unique name, add it to existing players, refresh the adapter and save the data
     */
    public void addSkill(){
        AppExtension app = (AppExtension) this.getApplicationContext();

        //Make sure new skill name is unique. Defaults to "Skill X", where X is a number counting up
        String skillName;
        int itemCount = app.getSkillAdapter().getItemCount()+1;

        do {
            skillName = "Skill " + itemCount;
            itemCount++;
        }
        while (!CommonUtil.getInstance(SkillsManagerActivity.this).isSkillNameUnique(skillName, app.getSkillAdapter().getList()));

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
package nl.rekijan.pathfindersecretroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nl.rekijan.pathfindersecretroller.ui.fragments.PlayersManagerActivity;
import nl.rekijan.pathfindersecretroller.ui.fragments.SkillsManagerActivity;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppExtension app = (AppExtension) this.getApplicationContext();

        //Setup RecyclerView by binding the adapter to it.
        RecyclerView skillsRecyclerView = findViewById(R.id.skills_recyclerView);
        skillsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        skillsRecyclerView.setLayoutManager(llm);
        skillsRecyclerView.setAdapter(app.getSkillAdapter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppExtension app = (AppExtension) this.getApplicationContext();
        app.saveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_manage_players:
                startActivity(new Intent(this, PlayersManagerActivity.class));
                return true;
            case R.id.menu_action_manage_skills:
                startActivity(new Intent(this, SkillsManagerActivity.class));
                return true;
            case R.id.menu_action_about:
                CommonUtil.getInstance(MainActivity.this).aboutInfo(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
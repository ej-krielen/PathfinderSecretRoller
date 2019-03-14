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
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

/**
 * Class to manage the players as a whole. From here user can add new players or edit existing.
 *
 * @see PlayerModel
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 4-3-2019
 */
public class PlayersManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Back to parent activity

        AppExtension app = (AppExtension) this.getApplicationContext();

        RecyclerView playersRecyclerView = findViewById(R.id.players_recyclerView);
        playersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        playersRecyclerView.setLayoutManager(llm);
        playersRecyclerView.setAdapter(app.getPlayerAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_players, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add_player:
                addPlayer();
                return true;
            case R.id.menu_action_about:
                CommonUtil.getInstance(PlayersManagerActivity.this).aboutInfo(PlayersManagerActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Make a new {@link PlayerModel} with a unique name, add existing skills to it, refresh the adapter and save the data
     */
    public void addPlayer(){
        AppExtension app = (AppExtension) this.getApplicationContext();

        //Make sure new player name is unique. Defaults to "Player X", where X is a number counting up
        String playerName;
        int itemCount = app.getPlayerAdapter().getItemCount()+1;

        do {
            playerName = "Player " + itemCount;
            itemCount++;
        }
        while (!CommonUtil.getInstance(PlayersManagerActivity.this).isPlayerNameUnique(playerName, app.getPlayerAdapter().getList()));

        //Make a new PlayerModel and each existing skill to it
        PlayerModel playerModel = new PlayerModel(playerName);
        for (SkillModel s : app.getSkillAdapter().getList())
        {
            playerModel.getSkillModels().add(new SkillModel(s.getName()));
        }
        //Add it to the adapter, refresh it, and save all data.
        app.getPlayerAdapter().add(playerModel);
        app.getPlayerAdapter().notifyDataSetChanged();
        app.saveData();
    }
}
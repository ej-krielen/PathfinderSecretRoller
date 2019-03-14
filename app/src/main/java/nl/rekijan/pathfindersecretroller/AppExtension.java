package nl.rekijan.pathfindersecretroller;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nl.rekijan.pathfindersecretroller.models.PlayerModel;
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.ui.adapter.PlayerAdapter;
import nl.rekijan.pathfindersecretroller.ui.adapter.SkillListAdapter;

import static nl.rekijan.pathfindersecretroller.AppConstants.DC_DIFFICULTY;
import static nl.rekijan.pathfindersecretroller.AppConstants.DC_LEVEL;
import static nl.rekijan.pathfindersecretroller.AppConstants.DC_VALUE;
import static nl.rekijan.pathfindersecretroller.AppConstants.GSON_TAG_PLAYER_LIST;
import static nl.rekijan.pathfindersecretroller.AppConstants.GSON_TAG_SKILL_LIST;
import static nl.rekijan.pathfindersecretroller.AppConstants.SHARED_PREF_TAG;

/**
 * /Class for methods and variables that need to be app-wide
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class AppExtension extends Application {

    private SkillListAdapter mSkillListAdapter;
    private PlayerAdapter mPlayerAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        mSkillListAdapter = new SkillListAdapter(this);
        mPlayerAdapter = new PlayerAdapter(this);
        initializeData();
    }

    /**
     * If data has been saved from previous usage use that, otherwise use default data.
     */
    private void initializeData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        Gson gsonSkillList = new Gson();
        String jsonSkillList = sharedPreferences.getString(GSON_TAG_SKILL_LIST, null);
        Type typeSkill = new TypeToken<ArrayList<SkillModel>>() {
        }.getType();
        ArrayList<SkillModel> skills;
        skills = gsonSkillList.fromJson(jsonSkillList, typeSkill);
        if (skills != null) {
            getSkillAdapter().addAll(skills);
        } else {
            //Skills that have secret rolls you will often need to roll for
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_perception)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_arcana)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_crafting)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_deception)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_nature)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_occultism)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_religion)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_society)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_stealth)));
            getSkillAdapter().add(new SkillModel(getString(R.string.default_skill_lore)));
        }

        Gson gsonPlayerList = new Gson();
        String jsonPlayerList = sharedPreferences.getString(GSON_TAG_PLAYER_LIST, null);
        Type typePlayer = new TypeToken<ArrayList<PlayerModel>>() {
        }.getType();
        ArrayList<PlayerModel> players;
        players = gsonPlayerList.fromJson(jsonPlayerList, typePlayer);
        if (players != null) {
            getPlayerAdapter().addAll(players);
        } else {
            //Default to making 4 PCs
            getPlayerAdapter().add(new PlayerModel("Player 1"));
            getPlayerAdapter().add(new PlayerModel("Player 2"));
            getPlayerAdapter().add(new PlayerModel("Player 3"));
            getPlayerAdapter().add(new PlayerModel("Player 4"));

            for (PlayerModel p : getPlayerAdapter().getList()) {
                for (SkillModel s : getSkillAdapter().getList()) {
                    p.getSkillModels().add(new SkillModel(s.getName()));
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////
    // Save and load data in shared preferences
    //////////////////////////////////////////////////////////////////
    public void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gsonSkillList = new Gson();
        ArrayList<SkillModel> skills = mSkillListAdapter.getList();
        String jsonSkillList = gsonSkillList.toJson(skills);
        editor.putString(GSON_TAG_SKILL_LIST, jsonSkillList);

        Gson gsonPlayerList = new Gson();
        ArrayList<PlayerModel> players = mPlayerAdapter.getList();
        String jsonPlayerList = gsonPlayerList.toJson(players);
        editor.putString(GSON_TAG_PLAYER_LIST, jsonPlayerList);

        editor.apply();
    }

    public void saveDCvalue(int dc) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DC_VALUE, dc);
        editor.apply();
    }

    public int getDCvalue() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DC_VALUE, 13);
    }

    public void saveDClevel(int level) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DC_LEVEL, level);
        editor.apply();
    }

    public int getDClevel() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DC_LEVEL, 1);
    }

    public void saveDCdifficulty(int dc) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DC_DIFFICULTY, dc);
        editor.apply();
    }

    public int getDCdifficulty() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_TAG, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DC_DIFFICULTY, 1);
    }

    //////////////////////////////////////////////////////////////////
    // End of region
    //////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////
    // Getters and setters
    //////////////////////////////////////////////////////////////////
    public SkillListAdapter getSkillAdapter() {
        return mSkillListAdapter;
    }

    public void setSkillAdapter(SkillListAdapter mSkillListAdapter) {
        this.mSkillListAdapter = mSkillListAdapter;
    }

    public PlayerAdapter getPlayerAdapter() {
        return mPlayerAdapter;
    }

    public void setPlayerAdapter(PlayerAdapter mPlayerAdapter) {
        this.mPlayerAdapter = mPlayerAdapter;
    }
}
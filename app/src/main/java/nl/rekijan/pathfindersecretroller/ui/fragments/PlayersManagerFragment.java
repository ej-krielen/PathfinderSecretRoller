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
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

/**
 * Class to manage the players as a whole. From here user can add new players or edit existing.
 *
 * @see PlayerModel
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 4-3-2019
 */
public class PlayersManagerFragment extends Fragment {

    public static Fragment newInstance() {
        return new PlayersManagerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_players_manager, container, false);
        final AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        RecyclerView playersRecyclerView = fragmentView.findViewById(R.id.players_recyclerView);
        playersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        playersRecyclerView.setLayoutManager(llm);
        playersRecyclerView.setAdapter(app.getPlayerAdapter());

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_players, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_add_player:
                addPlayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Make a new {@link PlayerModel} with a unique name, add existing skills to it, refresh the adapter and save the data
     */
    private void addPlayer(){
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        //Make sure new player name is unique. Defaults to "Player X", where X is a number counting up
        String playerName;
        int itemCount = app.getPlayerAdapter().getItemCount()+1;

        do {
            playerName = "Player " + itemCount;
            itemCount++;
        }
        while (!CommonUtil.getInstance().isPlayerNameUnique(playerName, app.getPlayerAdapter().getList()));

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
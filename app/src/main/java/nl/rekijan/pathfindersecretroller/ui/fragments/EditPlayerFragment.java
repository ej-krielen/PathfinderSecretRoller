package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Toast;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.MainActivity;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.PlayerModel;
import nl.rekijan.pathfindersecretroller.ui.adapter.DetailedSkillAdapter;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

import static nl.rekijan.pathfindersecretroller.AppConstants.PLAYER_MODEL_POSITION;

/**
 * Class to handle the editing of a PlayerModel
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 7-3-2019
 */
public class EditPlayerFragment extends Fragment {

    private EditText playerNameEditText;
    private int position;

    public static EditPlayerFragment newInstance(int position) {
        EditPlayerFragment fragment = new EditPlayerFragment();
        Bundle args = new Bundle();
        args.putInt(PLAYER_MODEL_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(PLAYER_MODEL_POSITION);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_edit_player, container, false);
        final AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        // Capture the layout's EditText and set the proper text
        playerNameEditText = fragmentView.findViewById(R.id.player_name_editText);
        PlayerModel player = app.getPlayerAdapter().getList().get(position);
        playerNameEditText.setText(player.getName());

        DetailedSkillAdapter mAdapter = new DetailedSkillAdapter(app);
        mAdapter.setList(player.getSkillModels());

        RecyclerView skillRecyclerView = fragmentView.findViewById(R.id.detailed_skill_recyclerView);
        skillRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        skillRecyclerView.setLayoutManager(llm);
        skillRecyclerView.setAdapter(mAdapter);
        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_player, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_save_edit_player:
                savePlayerInfo();
                return true;
            case R.id.menu_action_remove_edit_player:
                removePlayerConfirm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * <p>Before actually removing show a confirmation dialog</p>
     * calls {@link #removePlayer()}
     */
    private void removePlayerConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setMessage(this.getString(R.string.dialog_delete_message_player))
                .setTitle(this.getString(R.string.dialog_delete_title_player));
        builder.setPositiveButton(this.getString(R.string.dialog_about_confirm_deletion), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removePlayer();
            }
        });
        builder.setNegativeButton(this.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * <p>Removes the player from the app</p>
     * <p>Then close this activity</p>
     * called by {@link #removePlayerConfirm()}
     */
    private void removePlayer() {
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        app.getPlayerAdapter().remove(position);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.removeFragment(this);
    }

    /**
     * <p>When user clicks save player from menu check if the name is unique and then save it</p>
     * <p>Save all the data and close this activity</p>
     * <p>Or instead just display an error when user tries to make a duplicate name</p>
     */
    private void savePlayerInfo() {
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        String textFromEditText = playerNameEditText.getText().toString();
        boolean isNameUnique = CommonUtil.getInstance().isPlayerNameUnique(textFromEditText, app.getPlayerAdapter().getList());
        if (isNameUnique) {
            app.getPlayerAdapter().getList().get(position).setName(playerNameEditText.getText().toString());
        } else if (!textFromEditText.equals(app.getPlayerAdapter().getList().get(position).getName())) {
            Toast.makeText(app, app.getText(R.string.error_name_not_unique), Toast.LENGTH_LONG).show();
        }

        app.getPlayerAdapter().notifyDataSetChanged();
        app.saveData();
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.removeFragment(this);
    }
}
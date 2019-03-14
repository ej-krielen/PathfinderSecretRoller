package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import nl.rekijan.pathfindersecretroller.AppExtension;
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
public class EditPlayerActivity extends AppCompatActivity {

    private DetailedSkillAdapter mAdapter;
    private EditText playerNameEditText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Back to parent activity

        AppExtension app = (AppExtension) this.getApplicationContext();

        // Get the Intent that started this activity and extract the information sent with it
        Intent intent = getIntent();
        position = intent.getIntExtra(PLAYER_MODEL_POSITION, -1);

        // Capture the layout's EditText and set the proper text
        playerNameEditText = findViewById(R.id.player_name_editText);
        PlayerModel player = app.getPlayerAdapter().getList().get(position);
        playerNameEditText.setText(player.getName());

        mAdapter = new DetailedSkillAdapter(app);
        mAdapter.setList(player.getSkillModels());

        RecyclerView skillRecyclerView = findViewById(R.id.detailed_skill_recyclerView);
        skillRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        skillRecyclerView.setLayoutManager(llm);
        skillRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_player, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_save_edit_player:
                savePlayerInfo();
                return true;
            case R.id.menu_action_remove_edit_player:
                removePlayerConfirm();
                return true;
            case R.id.menu_action_about:
                CommonUtil.getInstance(EditPlayerActivity.this).aboutInfo(EditPlayerActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
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
    public void removePlayer() {
        AppExtension app = (AppExtension) this.getApplicationContext();
        app.getPlayerAdapter().remove(position);
        finish();
    }

    /**
     * <p>When user clicks save player from menu check if the name is unique and then save it</p>
     * <p>Save all the data and close this activity</p>
     * <p>Or instead just display an error when user tries to make a duplicate name</p>
     */
    public void savePlayerInfo() {
        AppExtension app = (AppExtension) this.getApplicationContext();

        String textFromEditText = playerNameEditText.getText().toString();
        boolean isNameUnique = CommonUtil.getInstance(EditPlayerActivity.this).isPlayerNameUnique(textFromEditText, app.getPlayerAdapter().getList());
        if (isNameUnique) {
            app.getPlayerAdapter().getList().get(position).setName(playerNameEditText.getText().toString());
        } else if (!textFromEditText.equals(app.getPlayerAdapter().getList().get(position).getName())) {
            Toast.makeText(app, app.getText(R.string.error_name_not_unique), Toast.LENGTH_LONG).show();
        }

        app.getPlayerAdapter().notifyDataSetChanged();
        app.saveData();
        finish();
    }
}
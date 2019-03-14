package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.ui.adapter.ResultAdapter;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;
import nl.rekijan.pathfindersecretroller.utilities.DcPickerDialog;

import static nl.rekijan.pathfindersecretroller.AppConstants.SKILL_NAME;

/**
 * Class to handle the results of rolling a skill
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 9-3-2019
 */
public class ResultActivity extends AppCompatActivity implements DcPickerDialog.DcPickerDialogListener {
    private ResultAdapter mAdapter;
    private String skillName;
    private EditText dcEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Back to parent activity

        final AppExtension app = (AppExtension) this.getApplicationContext();

        // Get the Intent that started this activity and extract the string to know which Skill to roll for
        Intent intent = getIntent();
        skillName = intent.getStringExtra(SKILL_NAME);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(skillName); //Make the action bar title the name of the skill so user knows what is being rolled for

        mAdapter = new ResultAdapter(app, app.getPlayerAdapter().getList(), skillName);

        //Setup RecyclerView by binding the adapter to it.
        RecyclerView skillsRecyclerView = findViewById(R.id.result_recyclerView);
        skillsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        skillsRecyclerView.setLayoutManager(llm);
        skillsRecyclerView.setAdapter(mAdapter);

        //Setup the calculate button to recalculate successes for when the editText has been changed
        Button calculateButton = findViewById(R.id.calculate_dc_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dcValue = Integer.parseInt(dcEditText.getText().toString());
                app.saveDCvalue(dcValue);
                mAdapter.calculateSuccess(dcValue);
            }
        });

        dcEditText = findViewById(R.id.dc_editText);
        String oldDcValue = String.valueOf(app.getDCvalue());
        if (!TextUtils.isEmpty(oldDcValue)) {
            dcEditText.setText(oldDcValue);
            mAdapter.rollDice();
            mAdapter.calculateSuccess(Integer.parseInt(oldDcValue));
        } else {
            mAdapter.rollDice();
        }

        //Setup the pick DC button to open a dialog from where the user can select a DC based on level and difficulty
        Button pickDCbutton = findViewById(R.id.pick_dc_button);
        pickDCbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDCpickerDialog();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //Make sure the last picked DC gets saved
        AppExtension app = (AppExtension) this.getApplicationContext();
        int dcValue = Integer.parseInt(dcEditText.getText().toString());
        app.saveDCvalue(dcValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_reroll:
                reRoll();
                return true;
            case R.id.menu_action_about:
                CommonUtil.getInstance(ResultActivity.this).aboutInfo(ResultActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Restart this activity again to make it go through the process of rolling again for all players
     */
    public void reRoll(){
        AppExtension app = (AppExtension) this.getApplicationContext();
        Intent intent = new Intent(app, ResultActivity.class);
        intent.putExtra(SKILL_NAME, skillName);

        int dcValue = Integer.parseInt(dcEditText.getText().toString());
        app.saveDCvalue(dcValue);

        this.startActivity(intent);
        this.overridePendingTransition(0, 0); //Cancels any transition animation so the user doesn't notice the whole activity is being rebuild
        finish();
    }

    /**
     * Open a custom DialogFragment {@link DcPickerDialog}
     */
    public void showDCpickerDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DcPickerDialog();
        dialog.show(getFragmentManager(), "DcPickerDialog");
    }

    /**
     * <p>Listener from {@link DcPickerDialog}</p>
     * Once dialog is closed redo the view based on the new DC value
     */
    @Override
    public void onDialogPositiveClick() {
        AppExtension app = (AppExtension) this.getApplicationContext();
        dcEditText.setText(String.valueOf(app.getDCvalue()));
        mAdapter.calculateSuccess(app.getDCvalue());
        mAdapter.notifyDataSetChanged();
    }
}
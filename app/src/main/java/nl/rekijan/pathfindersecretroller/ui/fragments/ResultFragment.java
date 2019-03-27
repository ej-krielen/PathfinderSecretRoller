package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.MainActivity;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.listener.DcDialogPickerListener;
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
public class ResultFragment extends Fragment implements DcDialogPickerListener {
    private ResultAdapter mAdapter;
    private String skillName;
    private EditText dcEditText;

    public static ResultFragment newInstance(String skillName) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(SKILL_NAME, skillName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            skillName = getArguments().getString(SKILL_NAME);
        }
        setHasOptionsMenu(true);
        CommonUtil.getInstance().hideSoftKeyboard(requireActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_result, container, false);
        final AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        mAdapter = new ResultAdapter(app, app.getPlayerAdapter().getList(), skillName);

        //Setup RecyclerView by binding the adapter to it.
        RecyclerView skillsRecyclerView = fragmentView.findViewById(R.id.result_recyclerView);
        skillsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        skillsRecyclerView.setLayoutManager(llm);
        skillsRecyclerView.setAdapter(mAdapter);

        //Setup the calculate button to recalculate successes for when the editText has been changed
        Button calculateButton = fragmentView.findViewById(R.id.calculate_dc_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dcValue = TextUtils.isEmpty(dcEditText.getText().toString()) ? 13 : Integer.parseInt(dcEditText.getText().toString());
                app.saveDCvalue(dcValue);
                mAdapter.calculateSuccess(dcValue);
            }
        });

        dcEditText = fragmentView.findViewById(R.id.dc_editText);
        String oldDcValue = String.valueOf(app.getDCvalue());
        if (!TextUtils.isEmpty(oldDcValue)) {
            dcEditText.setText(oldDcValue);
            mAdapter.rollDice();
            mAdapter.calculateSuccess(Integer.parseInt(oldDcValue));
        } else {
            mAdapter.rollDice();
        }

        //Setup the pick DC button to open a dialog from where the user can select a DC based on level and difficulty
        Button pickDCbutton = fragmentView.findViewById(R.id.pick_dc_button);
        pickDCbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDCpickerDialog();
            }
        });

        return fragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        //Make sure the last picked DC gets saved
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        int dcValue = Integer.parseInt(dcEditText.getText().toString());
        app.saveDCvalue(dcValue);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_result, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_reroll:
               reRoll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Restart this activity again to make it go through the process of rolling again for all players
     */
    private void reRoll(){
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        int dcValue = Integer.parseInt(dcEditText.getText().toString());
        app.saveDCvalue(dcValue);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.removeFragment(this);
        activity.replaceFragment(ResultFragment.newInstance(skillName));
        //Make the action bar title the name of the skill so user knows what is being rolled for
        activity.setActionBarTitle(getResources().getBoolean(R.bool.isTablet) ? getString(R.string.title_tablet, skillName) : skillName);

    }

    /**
     * Open a custom DialogFragment {@link DcPickerDialog}
     */
    private void showDCpickerDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DcPickerDialog();
        dialog.setTargetFragment(this, 0);
        assert getFragmentManager() != null;
        dialog.show(getFragmentManager(), "DcPickerDialog");
    }

    /**
     * <p>Listener from {@link DcPickerDialog}</p>
     * Once dialog is closed redo the view based on the new DC value
     */
    @Override
    public void onPositiveClick() {
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        dcEditText.setText(String.valueOf(app.getDCvalue()));
        mAdapter.calculateSuccess(app.getDCvalue());
        mAdapter.notifyDataSetChanged();
    }
}
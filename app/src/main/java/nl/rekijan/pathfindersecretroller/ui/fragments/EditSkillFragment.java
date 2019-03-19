package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import nl.rekijan.pathfindersecretroller.models.SkillModel;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

import static nl.rekijan.pathfindersecretroller.AppConstants.SKILL_MODEL_POSITION;

/**
 * Class to handle the editing of a SkillModel
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 7-3-2019
 */
public class EditSkillFragment extends Fragment {

    private EditText skillNameEditText;
    private int position;

    public static EditSkillFragment newInstance(int position) {
        EditSkillFragment fragment = new EditSkillFragment();
        Bundle args = new Bundle();
        args.putInt(SKILL_MODEL_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(SKILL_MODEL_POSITION);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_edit_skill, container, false);
        final AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        // Capture the layout's EditText and set the proper text
        skillNameEditText = fragmentView.findViewById(R.id.skill_name_editText);
        SkillModel skill = app.getSkillAdapter().getList().get(position);
        skillNameEditText.setText(skill.getName());

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_skill, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_save_edit_skill:
                saveSkillInfo();
                return true;
            case R.id.menu_action_delete_edit_skill:
                deleteSkillConfirm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * <p>Before actually removing show a confirmation dialog</p>
     * calls {@link #deleteSkill()}
     */
    private void deleteSkillConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setMessage(this.getString(R.string.dialog_delete_message_skill))
                .setTitle(this.getString(R.string.dialog_delete_title_skill));
        builder.setPositiveButton(this.getString(R.string.dialog_about_confirm_deletion), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteSkill();
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
     * <p>Removes the skill from the app and all its players</p>
     * <p>Then close this activity</p>
     * called by {@link #deleteSkillConfirm()} ()} ()}
     */
    private void deleteSkill() {
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        for (PlayerModel p : app.getPlayerAdapter().getList()) {
            p.getSkillModels().remove(position);
        }
        app.getSkillAdapter().remove(position);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.removeFragment(this);
    }

    /**
     * <p>When user clicks save skill from menu check for a unique name, and save it. Also update all existing {@link PlayerModel}s with the new name</p>
     * <p>Save all the data and close this activity</p>
     * <p>Or instead just display an error when user tries to make a duplicate name</p>
     */
    private void saveSkillInfo() {
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        String textFromEditText = skillNameEditText.getText().toString();
        boolean isNameUnique = CommonUtil.getInstance().isSkillNameUnique(textFromEditText, app.getSkillAdapter().getList());
        if (isNameUnique) {
            //Update all players, to reflect the change
            String oldName = app.getSkillAdapter().getList().get(position).getName();
            String newName = skillNameEditText.getText().toString();
            for (PlayerModel p : app.getPlayerAdapter().getList()) {
                for (SkillModel s : p.getSkillModels()) {
                    if (s.getName().equals(oldName)) {
                        s.setName(newName);
                    }
                }
            }
            app.getSkillAdapter().getList().get(position).setName(newName);
            app.saveData();
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
            activity.removeFragment(this);
        } else if (textFromEditText.equals(app.getSkillAdapter().getList().get(position).getName())) {
            MainActivity activity = (MainActivity) getActivity();
            assert activity != null;
            activity.removeFragment(this);
        } else {
            Toast.makeText(app, app.getText(R.string.error_name_not_unique), Toast.LENGTH_LONG).show();
        }
    }
}
package nl.rekijan.pathfindersecretroller.utilities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.listener.DcDialogPickerListener;

/**
 * Custom class for a dialog to pick a DC value by level and difficulty
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 9-3-2019
 */
public class DcPickerDialog extends DialogFragment {

    private TextView dcValueTextView;
    private DcDialogPickerListener callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (DcDialogPickerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dc_picker_dialog, null);
        final AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        //Get textView where the user gets a preview of the value his selection will pick
        dcValueTextView = dialogLayout.findViewById(R.id.dc_value_textView);

        //Setup the spinner to choose the level with
        Spinner levelSpinner = dialogLayout.findViewById(R.id.level_spinner);

        Integer[] levels = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        ArrayAdapter<Integer> levelSpinnerAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, levels);
        levelSpinner.setAdapter(levelSpinnerAdapter);
        // Drop down layout style
        levelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the value to the default or last picked value
        levelSpinner.setSelection(app.getDClevel());

        //When a new option is picked save it and recalculate the preview DC
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                app.saveDClevel(pos);
                calculateDC();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Setup the spinner to choose the difficulty with
        Spinner difficultySpinner = dialogLayout.findViewById(R.id.difficulty_spinner);
        String[] difficulty = new String[]{requireActivity().getString(R.string.dc_picker_label_easy), requireActivity().getString(R.string.dc_picker_label_medium), requireActivity().getString(R.string.dc_picker_label_hard), requireActivity().getString(R.string.dc_picker_label_incredible), requireActivity().getString(R.string.dc_picker_label_ultimate)};
        ArrayAdapter<String> difficultySpinnerAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, difficulty);
        difficultySpinner.setAdapter(difficultySpinnerAdapter);
        // Drop down layout style
        difficultySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the value to the default or last picked value
        difficultySpinner.setSelection(app.getDCdifficulty());

        //When a new option is picked save it and recalculate the preview DC
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                app.saveDCdifficulty(pos);
                calculateDC();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogStyle);

        builder.setTitle(R.string.dc_picker_dialog_title)
                .setView(dialogLayout) //Save our custom layout to it
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Save the DC from the level/difficulty combination, trigger the listener event, and close the dialog
                        app.saveDCvalue(Integer.parseInt(dcValueTextView.getText().toString()));
                        callback.onPositiveClick();
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * Calculate the DC based on the level and difficulty and show a preview of it in the textView
     */
    private void calculateDC() {
        AppExtension app = (AppExtension) requireActivity().getApplicationContext();
        int level = app.getDClevel();
        int difficulty = app.getDCdifficulty();
        int[][] dcValuesArray = new int[][]{
                /*    Easy Med Hard Inc Ult */
                /* 0 */ {7, 11, 13, 14, 16},
                /* 1 */ {8, 13, 15, 16, 18},
                /* 2 */ {9, 14, 16, 17, 19},
                /* 3 */ {10, 15, 17, 19, 20},
                /* 4 */ {11, 16, 18, 20, 21},
                /* 5 */ {12, 18, 20, 22, 23},
                /* 6 */ {13, 19, 21, 23, 25},
                /* 7 */ {14, 21, 22, 26, 27},
                /* 8 */ {15, 22, 24, 27, 28},
                /* 9 */ {16, 23, 26, 29, 30},
                /* 10 */{17, 24, 27, 31, 32},
                /* 11 */{18, 25, 28, 32, 33},
                /* 12 */{19, 26, 29, 33, 34},
                /* 13 */{20, 28, 30, 35, 36},
                /* 14 */{21, 29, 31, 36, 38},
                /* 15 */{22, 30, 33, 37, 40},
                /* 16 */{23, 32, 34, 38, 41},
                /* 17 */{24, 33, 36, 40, 43},
                /* 18 */{25, 34, 37, 41, 44},
                /* 19 */{26, 35, 38, 42, 45},
                /* 20 */{27, 36, 39, 43, 47},
                /* 21 */{28, 38, 41, 45, 49},
                /* 22 */{29, 39, 43, 47, 51},
                /* 23 */{30, 41, 45, 49, 53}
        };
        int dcValue = dcValuesArray[level][difficulty];
        dcValueTextView.setText(String.valueOf(dcValue));
    }
}
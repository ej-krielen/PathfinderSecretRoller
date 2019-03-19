package nl.rekijan.pathfindersecretroller.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.PlayerModel;
import nl.rekijan.pathfindersecretroller.models.SkillModel;

/**
 * Class to handle common tasks
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 5-3-2019
 */
public class CommonUtil {

    private static CommonUtil sInstance = null;
    private Random rng;

    public static synchronized CommonUtil getInstance() {
        if (sInstance == null) {
            sInstance = new CommonUtil();
        }
        return sInstance;
    }

    /**
     * Show dialog explaining more info is available on the website
     */
    public void aboutInfo(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogStyle);
        builder.setMessage(activity.getString(R.string.dialog_about_info))
                .setTitle(activity.getString(R.string.dialog_about_info_title));
        builder.setPositiveButton(activity.getString(R.string.dialog_about_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent siteIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.rekijan.nl/"));
                activity.startActivity(siteIntent);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method to check if newName String isn't already being used by one of the {@link PlayerModel}
     *
     * @param newName String to compare to existing names
     * @param players ArrayList of existing players
     * @return false if the string is empty and when it is already used by another {@link PlayerModel}
     */
    public boolean isPlayerNameUnique(String newName, ArrayList<PlayerModel> players) {
        if (TextUtils.isEmpty(newName)) return false;

        for (PlayerModel p : players) {
            if (p.getName().trim().equalsIgnoreCase(newName.trim())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to check if newName String isn't already being used by one of the {@link SkillModel}
     *
     * @param newName String to compare to existing names
     * @param skills  ArrayList of existing skills
     * @return false if the string is empty and when it is already used by another {@link SkillModel}
     */
    public boolean isSkillNameUnique(String newName, ArrayList<SkillModel> skills) {
        if (TextUtils.isEmpty(newName)) return false;

        for (SkillModel p : skills) {
            if (p.getName().trim().equalsIgnoreCase(newName.trim())) {
                return false;
            }
        }
        return true;
    }

    private Random getRandom() {
        if (rng == null) {
            rng = new Random();
            return rng;
        } else {
            return rng;
        }
    }

    private int rollDice(int maxRange) {
        return getRandom().nextInt(maxRange) + 1;
    }

    public int rollD20() {
        return rollDice(20);
    }
}
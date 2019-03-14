package nl.rekijan.pathfindersecretroller.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Model for a Player
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class PlayerModel implements Parcelable {

    private String name;
    private String success;
    private int result;
    private int diceRoll;
    private ArrayList<SkillModel> skillModels = new ArrayList<SkillModel>();

    // Constructors
    public PlayerModel(String name) {
        this.name = name;
        success = "";
    }

    protected PlayerModel(Parcel in) {
        name = in.readString();
        success = in.readString();
        result = in.readInt();
        diceRoll = in.readInt();
    }

    public static final Creator<PlayerModel> CREATOR = new Creator<PlayerModel>() {
        @Override
        public PlayerModel createFromParcel(Parcel in) {
            return new PlayerModel(in);
        }

        @Override
        public PlayerModel[] newArray(int size) {
            return new PlayerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(success);
        parcel.writeInt(result);
        parcel.writeInt(diceRoll);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getDiceRoll() {
        return diceRoll;
    }

    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    public ArrayList<SkillModel> getSkillModels() {
        return skillModels;
    }

    public void setSkillModels(ArrayList<SkillModel> skillModels) {
        this.skillModels = skillModels;
    }
}
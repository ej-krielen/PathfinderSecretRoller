package nl.rekijan.pathfindersecretroller.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for a Skill
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class SkillModel implements Parcelable {

    private String name;
    private int modifier;
    private String proficiency;

    // Constructors
    public SkillModel(String name) {
        this.name = name;
        proficiency = "Untrained";
        modifier = 0;
    }

    protected SkillModel(Parcel in) {
        name = in.readString();
        modifier = in.readInt();
        proficiency = in.readString();
    }

    public static final Creator<SkillModel> CREATOR = new Creator<SkillModel>() {
        @Override
        public SkillModel createFromParcel(Parcel in) {
            return new SkillModel(in);
        }

        @Override
        public SkillModel[] newArray(int size) {
            return new SkillModel[size];
        }
    };

    public SkillModel(String name, String proficiency, int modifier) {
        this.name = name;
        this.proficiency = proficiency;
        this.modifier = modifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(modifier);
        parcel.writeString(proficiency);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }
}
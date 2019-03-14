package nl.rekijan.pathfindersecretroller.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import nl.rekijan.pathfindersecretroller.R;
import nl.rekijan.pathfindersecretroller.models.SkillModel;

/**
 * <p>Listener for EditText</p>
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @see nl.rekijan.pathfindersecretroller.ui.adapter.DetailedSkillAdapter
 * @since 3-3-2019
 */
public class GenericTextWatcher implements TextWatcher {
    private View view;
    private SkillModel skill;

    public GenericTextWatcher(SkillModel skill, View view) {
        this.skill = skill;
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        switch (view.getId()) {
            case R.id.skill_modifier_editText:
                skill.setModifier(TextUtils.isEmpty(text) ? 0 : Integer.parseInt(text));
        }
    }
}
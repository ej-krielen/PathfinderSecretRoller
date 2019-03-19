package nl.rekijan.pathfindersecretroller.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nl.rekijan.pathfindersecretroller.AppExtension;
import nl.rekijan.pathfindersecretroller.MainActivity;
import nl.rekijan.pathfindersecretroller.R;

/**
 * Class to handle the initial fragment to be shown
 */
public class StartFragment extends Fragment {

    public StartFragment() {
        // Required empty public constructor
    }
    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_start, container, false);

        AppExtension app = (AppExtension) requireActivity().getApplicationContext();

        //Setup RecyclerView by binding the adapter to it.
        RecyclerView skillsRecyclerView = fragmentView.findViewById(R.id.skills_recyclerView);
        skillsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        skillsRecyclerView.setLayoutManager(llm);
        skillsRecyclerView.setAdapter(app.getSkillAdapter());

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_start, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        switch (item.getItemId()) {
            case R.id.menu_action_manage_players:
                activity.replaceFragment(PlayersManagerFragment.newInstance());
                return true;
            case R.id.menu_action_manage_skills:
                activity.replaceFragment(SkillsManagerFragment.newInstance());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
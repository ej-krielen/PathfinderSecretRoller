package nl.rekijan.pathfindersecretroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import nl.rekijan.pathfindersecretroller.ui.fragments.ResultFragment;
import nl.rekijan.pathfindersecretroller.ui.fragments.StartFragment;
import nl.rekijan.pathfindersecretroller.utilities.CommonUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppExtension app = (AppExtension) this.getApplicationContext();

        //Add first fragment if fresh start
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, StartFragment.newInstance())
                    .commit();

            if (getResources().getBoolean(R.bool.isTablet) && !app.getSkillAdapter().getList().isEmpty()) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_result, ResultFragment.newInstance(app.getSkillAdapter().getList().get(0).getName()))
                        .commit();
                setActionBarTitle(getString(R.string.title_tablet, app.getSkillAdapter().getList().get(0).getName()));
            }

        } else { //Set the action bar title and showing of the back button to previously stored values
            if (!TextUtils.isEmpty(app.getActionBarTitle())) {
                setActionBarTitle(app.getActionBarTitle());
            }
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(app.showBackNavigation());
        }

        CommonUtil.getInstance().hideSoftKeyboard(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppExtension app = (AppExtension) this.getApplicationContext();
        app.saveData();
    }

    /**
     * Replace the current fragment with the new one<br>
     *     If the device has a large screen and the new fragment is {@link ResultFragment} then replace it in the right fragment view
     *
     * @param newFragment the new fragment to add
     */
    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (newFragment instanceof ResultFragment && getResources().getBoolean(R.bool.isTablet)) {
            transaction.replace(R.id.fragment_result, newFragment);
            transaction.commit();
        } else {
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            //Enable the back button in action bar
            if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            AppExtension app = (AppExtension) this.getApplicationContext();
            app.setShowBackNavigation(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        //Disable the back button in action bar only if it is the last fragment
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 1);
        AppExtension app = (AppExtension) this.getApplicationContext();
        app.setShowBackNavigation(getSupportFragmentManager().getBackStackEntryCount() > 1);
        getSupportFragmentManager().popBackStack();
        setActionBarTitle(app.getActionBarTitle());
        if (getSupportActionBar() != null && getSupportFragmentManager().getBackStackEntryCount() == 1 && !getResources().getBoolean(R.bool.isTablet)) {
            setActionBarTitle(getString(R.string.title_activity_main)); //Reset title if all other fragments are gone
        }
        return true;
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        AppExtension app = (AppExtension) this.getApplicationContext();
        app.setActionBarTitle(title);
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //Other menu items are added by fragments
        switch (item.getItemId()) {
            case R.id.menu_action_about:
                CommonUtil.getInstance().aboutInfo(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
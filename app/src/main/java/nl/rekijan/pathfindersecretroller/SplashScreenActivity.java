package nl.rekijan.pathfindersecretroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Loading screen
 *
 * @author Erik-Jan Krielen ej.krielen@gmail.com
 * @since 3-3-2019
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Show splash screen with a timer.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                // Close this activity
                finish();
            }
        }, BuildConfig.SPLASH_TIME_OUT);
    }
}
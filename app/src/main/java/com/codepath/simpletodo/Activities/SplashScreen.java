package com.codepath.simpletodo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.codepath.simpletodo.R;

public class SplashScreen extends AppCompatActivity {

    // show splash screen for this duration
    private static int SPLASH_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //After the duration, go to the main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                //Close this screen
                finish();
            }
        }, SPLASH_DURATION);
    }
}

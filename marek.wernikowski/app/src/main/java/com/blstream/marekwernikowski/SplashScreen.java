package com.blstream.marekwernikowski;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;


public class SplashScreen extends Activity {

    private Handler splashHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final int DELAY = 5000;
        splashHandler = new Handler();
        splashHandler.postDelayed(new Runnable() {
            public void run() {
                SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", 0);
                if (!preferences.contains("email"))
                    startActivity(new Intent(SplashScreen.this, LoginScreen.class));
                else
                    startActivity(new Intent(SplashScreen.this, MainScreen.class));
                finish();
            }
        }, DELAY);
    }

    @Override
    public void onBackPressed() {

        splashHandler.removeCallbacksAndMessages(null);
    }


}

package com.blstream.marekwernikowski;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainScreen extends Activity {

    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        TextView loginInfo = (TextView) findViewById(R.id.logged_in);
        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", 0);
        loginInfo.setText("Zalogowano jako " + preferences.getString("email", ""));

        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(logoutHandler);
    }

    View.OnClickListener logoutHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("email");
            editor.remove("password");
            editor.commit();
            startActivity(new Intent(MainScreen.this, LoginScreen.class));
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

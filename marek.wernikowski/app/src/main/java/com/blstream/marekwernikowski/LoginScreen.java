package com.blstream.marekwernikowski;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginScreen extends Activity {

    private EditText email = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Button loginButton;
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(loginHandler);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPassword(CharSequence target) {

        int length = target.length();
        if (length < 8)
            return false;

        String targetPassword = target.toString();

        boolean containsUppercase = false;
        boolean containsLowercase = false;
        boolean containsDigit = false;

        for (int i = 0; i < length; i++)
        {
            char c = targetPassword.charAt(i);
            if (c >= 'A' && c <= 'Z')
                containsUppercase = true;
            else if (c >= 'a' && c <= 'z')
                containsLowercase = true;
            else if (c >= '0' && c <= '9')
                containsDigit = true;
        }
        return (containsUppercase && containsLowercase && containsDigit);
    }

    View.OnClickListener loginHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isValidEmail(email.getText())) {
                email.requestFocus();
                email.setError("Niepoprawny adres email!");
            } else if (!isValidPassword(password.getText())) {
                password.requestFocus();
                password.setError("Hasło powinno zawierać co najmniej 8 znaków, jedną wielką literę, jedną małą literę i jedną cyfrę.");
            }  else {
                SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", email.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.apply();
                startActivity(new Intent(LoginScreen.this, MainScreen.class));
                finish();
            }
        }
    };


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

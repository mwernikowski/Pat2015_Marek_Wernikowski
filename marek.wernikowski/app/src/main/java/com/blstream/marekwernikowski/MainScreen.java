package com.blstream.marekwernikowski;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.util.AQUtility;


import java.util.List;


public class MainScreen extends Activity {

    private ProgressBar progressBar;
    private ListView jsonList;
    private Integer currentFile;
    public static JsonCustomList jsonCustomList = null;
    public static boolean loading = false;
    public static boolean allLoaded = false;

    public final static String BASE_SERVER_URL = "http://192.168.0.33:8080";

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ListView getJsonList() {
        return jsonList;
    }

    public JsonCustomList getJsonCustomList() {
        return jsonCustomList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button logoutButton;

        TextView loginInfo = (TextView) findViewById(R.id.logged_in);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        jsonList = (ListView) findViewById(R.id.json_list);
        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", 0);


        currentFile = 1;
        try {
            progressBar.setVisibility(View.VISIBLE);
            new JsonParser(MainScreen.this).execute(BASE_SERVER_URL + "/page_0.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        jsonList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !loading && !allLoaded) {
                    progressBar.setVisibility(View.VISIBLE);
                    loading = true;
                    List<Element> list;
                    try {
                        list = new JsonParser(MainScreen.this).execute(BASE_SERVER_URL + "/page_" + currentFile.toString() + ".json").get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (list == null){
                        allLoaded = true;
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        currentFile++;

                    }
                }
            }
        });

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
            editor.apply();
            startActivity(new Intent(MainScreen.this, LoginScreen.class));
            finish();
        }
    };


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AQUtility.cleanCacheAsync(this);
    }


}

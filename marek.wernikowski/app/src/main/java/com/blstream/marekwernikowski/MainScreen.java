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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainScreen extends Activity {

    private Button logoutButton;
    private ProgressBar progressBar;
    private ListView jsonList;
    //private JsonCustomList jsonCustomList;
    public static JsonCustomList jsonCustomList = null;
    public static boolean loading = false;

    //public final static String BASE_SERVER_URL = "http://10.0.2.2:8080";
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

        TextView loginInfo = (TextView) findViewById(R.id.logged_in);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //TextView jsonInfo = (TextView) findViewById(R.id.json_file);

        jsonList = (ListView) findViewById(R.id.json_list);
        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION", 0);



        //JSONObject jsonObject = jsonParser.getJsonObject();
        try {
            new JsonParser(MainScreen.this).execute(BASE_SERVER_URL + "/page_0.json");
        } catch (Exception e) {
            throw new RuntimeException(e);//, null, null);
        }



        jsonList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0 && !loading) {
                    loading = true;
                    new JsonParser(MainScreen.this).execute(BASE_SERVER_URL + "/page_1.json");
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
            editor.commit();
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

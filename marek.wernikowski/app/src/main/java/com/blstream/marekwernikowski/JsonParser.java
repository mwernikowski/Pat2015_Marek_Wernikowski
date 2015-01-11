package com.blstream.marekwernikowski;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek Wernikowski on 10.01.2015.
 */
public class JsonParser extends AsyncTask<String, Void, List<Element>>{

    private Activity activity;
    private ProgressBar progressBar;
    private String[] titles;
    private String[] descriptions;
    private String[] iconLinks;
    protected JSONObject jsonObject = null;

    protected ArrayList<Element> jsonList = null;
    protected ListView listView = null;


    public JsonParser(Activity _activity) {
        activity = _activity;
        listView = ((MainScreen)activity).getJsonList();
        progressBar = ((MainScreen)activity).getProgressBar();
    }
    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Element> doInBackground(String... args) {

        jsonObject = getJsonFromUrl(args[0]);
        jsonList = new ArrayList<Element>();
        if (((MainScreen)activity).jsonCustomList == null) {
            ((MainScreen)activity).jsonCustomList = new JsonCustomList(activity, jsonList);
        }
        int size = ((MainScreen)activity).jsonCustomList.getCount();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("array");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject row = (JSONObject)jsonArray.get(i);
                String title = row.getString("title");
                String description = row.getString("desc");
                String iconLink = row.getString("url");
                Element element = new Element(title, description, iconLink);
                jsonList.add(element);
                //((MainScreen)activity).getJsonCustomList().add(element);
                size = ((MainScreen)activity).jsonCustomList.getCount();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return jsonList;
    }

    @Override
    protected void onPostExecute(List<Element> jsonList) {

        JsonCustomList jsonCustomList = ((MainScreen)activity).getJsonCustomList();
        listView.setAdapter(jsonCustomList);
        //for (int i = 0; i < jsonList.size(); i++)
        //    jsonCustomList.insert(jsonList.get(i), jsonCustomList.getCount());
        ((MainScreen)activity).getJsonCustomList().addAll(jsonList);
        listView.setAdapter(((MainScreen)activity).jsonCustomList);
        ((MainScreen)activity).jsonCustomList.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        ((MainScreen)activity).loading = false;
    }

    public static JSONObject getJsonFromUrl(String url) {
        InputStream inputStream = null;
        String jsonString = "";
        JSONObject jsonObject = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String jsonLine = null;
            while ((jsonLine = bufferedReader.readLine()) != null)
                stringBuilder.append(jsonLine + "\n");
            inputStream.close();
            jsonString = stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }
}

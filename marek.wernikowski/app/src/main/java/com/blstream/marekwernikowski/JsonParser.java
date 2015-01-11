package com.blstream.marekwernikowski;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    }

    @Override
    protected ArrayList<Element> doInBackground(String... args) {

        jsonObject = getJsonFromUrl(args[0]);
        if (jsonObject == null)
            return null;
        jsonList = new ArrayList<>();
        if (MainScreen.jsonCustomList == null) {
            MainScreen.jsonCustomList = new JsonCustomList(activity, jsonList);
        }

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
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return jsonList;
    }

    @Override
    protected void onPostExecute(List<Element> jsonList) {

        if (jsonList == null)
            return;
        JsonCustomList jsonCustomList = ((MainScreen)activity).getJsonCustomList();
        listView.setAdapter(jsonCustomList);
        ((MainScreen)activity).getJsonCustomList().addAll(jsonList);
        listView.setAdapter(MainScreen.jsonCustomList);
        progressBar.setVisibility(View.GONE);

        MainScreen.loading = false;
    }

    public static JSONObject getJsonFromUrl(String url) {
        InputStream inputStream;
        String jsonString;
        JSONObject jsonObject;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 404)
                return null;

            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String jsonLine;
            while ((jsonLine = bufferedReader.readLine()) != null)
                stringBuilder.append(jsonLine).append("\n");
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

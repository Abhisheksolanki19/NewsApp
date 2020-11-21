package com.shivamsaluja20.android.newsreport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class QueryUtils {
    private QueryUtils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String jsonResponse) {
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        // Create an empty ArrayList that we can start adding
        ArrayList<News> news = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            jsonObject = jsonObject.getJSONObject("response");
            if (!jsonObject.getString("status").equals("ok")) {
                return null;
            }
            JSONArray array = jsonObject.getJSONArray("results");

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String[] timeStamp = object.getString("webPublicationDate").split("[TZ]");
                JSONArray authors = object.getJSONArray("tags");
                String author = "N/A";
                if (authors.length() > 0) {
                    author = authors.getJSONObject(0).getString("webTitle");

                }
                Log.e("k", object.getString("webTitle"));
                news.add(new News(object.getString("sectionName"), object.getString("webTitle"), timeStamp[0], timeStamp[1], object.getString("webUrl"), author));
            }
        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            return null;
        }
        // Return the list
        return news;
    }
}

class myAsyncTask extends AsyncTaskLoader<ArrayList<News>> {
    private Context context;
    private URL url;


    myAsyncTask(Context context, URL url) {
        super(context);
        this.context = context;
        this.url = url;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();

    }

    @Nullable
    @Override
    public ArrayList<News> loadInBackground() {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (jsonResponse == null) {
            return null;
        }

        return QueryUtils.extractNews(jsonResponse);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
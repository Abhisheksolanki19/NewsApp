package com.shivamsaluja20.android.newsreport;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String USGS_URL = "https://content.guardianapis.com/search";
    private RecyclerView newsList;
    private URL url;
    private LinearLayout noData, progLayout;
    private ItemAdapter itemAdapter;
    private TextView textDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        newsList = findViewById(R.id.recyclerView);
        noData = findViewById(R.id.no_data);
        progLayout = findViewById(R.id.progress_layout);
        textDetails = findViewById(R.id.text_details);

        newsList.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        progLayout.setVisibility(View.VISIBLE);

        // Creating and setting a layout manager to recyclerView
        LinearLayoutManager li = new LinearLayoutManager(this);
        newsList.setLayoutManager(li);

        // Create a new {@link ArrayAdapter} of earthquakes
        itemAdapter = new ItemAdapter(this);
        newsList.addItemDecoration(new DividerItemDecoration(newsList.getContext(), DividerItemDecoration.VERTICAL));
        newsList.setAdapter(itemAdapter);
        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");


        // String to Url
        url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //Initialise the loader
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager.getInstance(this).initLoader(0, null, this);
            textDetails.setText(R.string.no_news_found);
        } else {
            progLayout.setVisibility(View.GONE);
            newsList.setVisibility(View.GONE);
            textDetails.setText(R.string.no_internet);
            noData.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, @Nullable Bundle args) {
        // Return the appropriate loader
        return new myAsyncTask(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<News>> loader, ArrayList<News> data) {
        if (data == null || data.isEmpty()) {
            itemAdapter.setContents(new ArrayList<News>());
        } else {
            itemAdapter.setContents(data);
        }
        progLayout.setVisibility(View.GONE);
        // This will be executed when the json received

        itemAdapter.notifyDataSetChanged();
        setEmptyCheck();
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        itemAdapter.setContents(new ArrayList<News>());
        itemAdapter.notifyDataSetChanged();
        setEmptyCheck();
    }

    void setEmptyCheck() {
        Log.e("items", "" + itemAdapter.getItemCount());
        if (itemAdapter.getItemCount() < 1) {
            newsList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        } else {
            newsList.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }
    }
}
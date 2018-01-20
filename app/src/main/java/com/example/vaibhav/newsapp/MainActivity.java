package com.example.vaibhav.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    //constants:
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    public static int LOADER_ID = 0;
    public static String NEWS_BASE_URL_STRING = "https://content.guardianapis.com/search?";
    //example: http://content.guardianapis.com/search?order-by=newest&q=hahaha&api-key=test
    public static String SECTION_NAME = "technology";
    public static String ORDER_BY = "newest";

    //global variables:
    ListView listView;
    TextView emptyView;
    ProgressBar progressBar;
    NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find listView, emptyView and loadingIndicator so that you can set empty view in case of network not connected
        listView = (ListView) findViewById(R.id.list_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        adapter = new NewsAdapter(this, new ArrayList<NewsItem>());

        //setting up click events for list view has no relation with network connectivity
        //Therefore, we set them up here instead of in the onLoadFinished method:

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem currentNewsItem = adapter.getItem(position);

                //setting up intent:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(currentNewsItem.getSourceUrlString()));
                if(i.resolveActivity(getPackageManager())!=null) startActivity(i);
                else Log.e(LOG_TAG, "Error starting activity from intent");
            }
        });

        //check for Network Connectivity
        if(!isNewtorkConnected()){
            emptyView.setText(R.string.no_internet_connection);
            listView.setEmptyView(emptyView);
            progressBar.setVisibility(View.INVISIBLE);
            Log.e(LOG_TAG, "No network connection");
        }
        else {
            Log.i(LOG_TAG, "Loader initialized");
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }

    }

    boolean isNewtorkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "In onCreateLoader");
        return new NewsItemLoader(this, NEWS_BASE_URL_STRING, SECTION_NAME, ORDER_BY);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        Log.i(LOG_TAG, "In onLoadFinished");
        adapter.clear();
        adapter.addAll(data);
        listView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        //implement the change to UI once the backend work is over TODO
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        adapter.clear();
        //what happens when the loader is reset TODO
    }
}

package com.example.vaibhav.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.example.vaibhav.newsapp.MainActivity.LOG_TAG;

/**
 * Created by Vaibhav on 6/4/2017.
 */

class NewsItemLoader extends AsyncTaskLoader<List<NewsItem>> {

    private String baseUrl;
    private String queryUrl;
    private String sortBy;

    NewsItemLoader(Context context, String providedBaseUrl, String providedQueryUrl, String providedSortBy) {
        super(context);
        baseUrl = providedBaseUrl;
        queryUrl = providedQueryUrl;
        sortBy = providedSortBy;
    }

    @Override
    public List<NewsItem> loadInBackground() {
        Log.i(LOG_TAG, "Inside loadInBackground");
        return QueryUtils.fetchNewsItems(baseUrl, queryUrl, sortBy);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}

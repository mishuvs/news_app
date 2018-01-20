package com.example.vaibhav.newsapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.vaibhav.newsapp.MainActivity.LOG_TAG;

/**
 * Created by Vaibhav on 6/4/2017.
 */

public class QueryUtils {

    private QueryUtils(){
        //no one should ever be able to make an object of this class
    }

    private static URL createUrl(String providedBaseUrl, String providedSection, String providedSortBy){

        URL url = null;

        Uri builtUri = Uri.parse(providedBaseUrl)
                .buildUpon()
                .appendQueryParameter("section", providedSection)
                .appendQueryParameter("page-size", "10")
                .appendQueryParameter("order-by", providedSortBy)
                .appendQueryParameter("api-key","test")
                .build();

        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "problem building url from Uri");
        }

        Log.i(LOG_TAG, "This is the built url: " + url);
        return url;
    }

    //returns the string read from stream
    private static InputStream makeHtppRequest(URL url){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Encountered error in makeHttpRequest method: ", e);
            e.printStackTrace();
        }
        return inputStream;
    }

    //returns the json "string" read from input stream
    private static String readFromStream(InputStream inputStream){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder builder = new StringBuilder();

        try {
            line = reader.readLine();
            while(line != null){
                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception encountered while reading from stream", e);
            e.printStackTrace();
        }
        return builder.toString();
    }

    private static List<NewsItem> toObjectList(String jsonString){
        List<NewsItem> newsList = new ArrayList<NewsItem>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray array = root.getJSONObject("response").getJSONArray("results");
            JSONObject object;
            String sectionName;
            String webTitle;
            String webUrl;
            String dateString = null;
            SimpleDateFormat formatRecieved = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat formatNeeded = new SimpleDateFormat("EEE, dd");
            Date date = null;
            Date currentDate = null;
            try {
                currentDate = formatNeeded.parse(formatNeeded.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error creating current date object: " + e);
                return null;
            }
            for(int i=0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                sectionName = object.getString("sectionName");
                webTitle = object.getString("webTitle");
                webUrl = object.getString("webUrl");
                try {
                    date = formatNeeded.parse(formatNeeded.format(formatRecieved.parse(object.getString("webPublicationDate"))));
                    dateString = formatNeeded.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Encountered error while parsing date toObjectList: ", e);
                    Log.e(LOG_TAG, "Date date is null as it is not updated");
                    return null;
                }
                if(date.compareTo(currentDate) == 0) dateString = "Today";
                Log.i(LOG_TAG, "The compareTo gives: " + date.compareTo(new Date()) + " The current date is: " + currentDate
                + " And the date fetched is: " + date);

                newsList.add(new NewsItem(sectionName, webTitle, webUrl, dateString));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Encountered error in stringToJson method: ", e);
            e.printStackTrace();
        }
        return newsList;

    }

    static List<NewsItem> fetchNewsItems(String providedBaseUrl, String providedSection, String providedSortBy){
        return toObjectList(readFromStream(makeHtppRequest(createUrl(providedBaseUrl, providedSection, providedSortBy))));
    }
}

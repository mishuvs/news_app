package com.example.vaibhav.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Vaibhav on 6/3/2017.
 */

class NewsAdapter extends ArrayAdapter<NewsItem> {

    private List<NewsItem> newsItemList;

    public NewsAdapter(@NonNull Context context, @NonNull List<NewsItem> objects) {
        super(context, 0, objects);
        newsItemList = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView;
        final NewsItem currentItem = newsItemList.get(position);

        if(convertView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }
        else
            listItemView = convertView;

        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.section);
        sectionNameTextView.setText(currentItem.getSectionName());

        TextView webTitleTextView = (TextView) listItemView.findViewById(R.id.news_title);
        webTitleTextView.setText(currentItem.getWebTitle());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentItem.getDate());

        return listItemView;
        //if the convertView is not null.. return this view only
        //else if the convertView is null.. inflate the correctView and return that instead
    }
}

package com.example.vaibhav.newsapp;

import java.net.URL;
import java.util.Date;

/**
 * Created by Vaibhav on 6/3/2017.
 */

class NewsItem {

    private String sectionName;
    private String webTitle;
    private String sourceUrl;
    private String date;

    NewsItem(String sectionNameInput, String webTitleInput, String inputSourceUrl, String dateInput){
        sectionName = sectionNameInput;
        webTitle = webTitleInput;
        sourceUrl = inputSourceUrl;
        date = dateInput;
    }

    String getSectionName(){
        return sectionName;
    }

    String getWebTitle(){
        return webTitle;
    }

    public String getSourceUrlString(){
        return sourceUrl;
    }

    public String getDate(){
        return date;
    }
}

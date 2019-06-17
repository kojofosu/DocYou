package com.edue.docyou;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Fosu on 9/16/2017.
 */

public class Note implements Serializable {
    private long mDateTime;
    private String mTitle;
    private String mContent;

    public Note(long dateTime, String title, String content){
        mContent=content;
        mDateTime=dateTime;
        mTitle=title;
    }
    public void setContent(String content){
        mContent = content;
    }
    public void setDateTime(long dateTime){
        mDateTime = dateTime;
    }
    public void setTitle(String title){
        mTitle = title;
    }
    public long getDateTime(){
        return mDateTime;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getContent(){
        return mContent;
    }
    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy " +
                "HH:mm:ss"
                , context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(mDateTime));
    }
}

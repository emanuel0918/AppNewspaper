package com.appnewspaper;

import android.os.Build;

import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class LastDate {
    private long dateInMillis;
    private Date lastDate;

    public LastDate(){
        dateInMillis=0;
        long yormilliseconds=System.currentTimeMillis();
        try{
            List<Article> articles= DBArticles.loadAllArticles();
            long greaterDate=0; //encontrar la fecha más actualizada
            long dateAux;
            Date date;
            for(Article a:articles){
                date=a.getLastUpdate();
                dateAux=date.getTime();
                if(dateAux>greaterDate){
                    greaterDate=dateAux;
                }

            }
            dateInMillis=greaterDate;
        }catch (Exception dbException){
            dateInMillis=0;
        }



        //
        //
    }

    public long getDateInMillis() {
        return dateInMillis;
    }

    public void setDateInMillis(long dateInMillis) {
        this.dateInMillis = dateInMillis;
    }

    public Date getLastDate() {
        Date lastDate;
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.O){
            LocalDateTime lastLocalDateTime= Instant.ofEpochMilli(dateInMillis)
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            lastDate=Date.from(lastLocalDateTime.atZone((ZoneId.systemDefault())).toInstant());
        }else {
            lastDate=new Date(this.dateInMillis);

        }
        return lastDate;
    }

    public String getLastDateString(){
        String dateString="";
        String dateFormat="yyyy-mm-dd hh:ii:ss";
        Date lastDate;
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.O){
            LocalDateTime lastLocalDateTime= Instant.ofEpochMilli(dateInMillis)
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault());
            dateString=dateTimeFormatter.format(lastLocalDateTime);
        }else {
            lastDate=new Date(this.dateInMillis);
            SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
            dateString=sdf.format(lastDate);

        }
        return dateString;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
}

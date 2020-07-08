package com.appnewspaper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SerializationUtils {
    public final static  String IMG_STRING="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";

    private static final String DATE_FORMAT_MYSQL = "yyyy-MM-dd hh:mm:ss";

    public static Date dateFromString(String stringDate) throws java.text.ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MYSQL);
        Date date = sdf.parse(stringDate);
        return date;
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MYSQL);
        //ALGM 2017/09/28 if date is null then use the current date
        String stringDate = sdf.format((date!= null)? date : Calendar.getInstance().getTime());
        return stringDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String imgToBase64String(Bitmap image)
    {
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
        int quality = 100;
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    // Added try catch so it works
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static Bitmap base64StringToImg(String input)
    {
        try {
            byte[] decodedBytes = Base64.decode(input, Base64.NO_WRAP);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static String encodeImage(Bitmap img){
        String base64String = imgToBase64String(img);
        //System.out.println("<img src=\"data:image/png;base64,"+base64String+"\">");
        return base64String;
    }

    public static Bitmap createScaledImage(Bitmap src, int w, int h){

        int finalw = w;
        int finalh = h;
        double factor = 1.0d;
        if(src.getWidth() > src.getHeight()){
            factor = ((double)src.getHeight()/(double)src.getWidth());
            finalh = (int)(finalw * factor);
        }else{
            factor = ((double)src.getWidth()/(double)src.getHeight());
            finalw = (int)(finalh * factor);
        }

        Bitmap resizedImg = Bitmap.createScaledBitmap(src, finalw, finalh, true);
        return resizedImg;
    }

    public static String createScaledStrImage(String strSrc, int w, int h){
        Bitmap src = base64StringToImg(strSrc);
        int finalw = w;
        int finalh = h;
        double factor = 1.0d;
        if(src.getWidth() > src.getHeight()){
            factor = ((double)src.getHeight()/(double)src.getWidth());
            finalh = (int)(finalw * factor);
        }else{
            factor = ((double)src.getWidth()/(double)src.getHeight());
            finalw = (int)(finalh * factor);
        }

        Bitmap resizedImg = Bitmap.createScaledBitmap(src, finalw, finalh, true);
        return imgToBase64String(resizedImg);
    }

}

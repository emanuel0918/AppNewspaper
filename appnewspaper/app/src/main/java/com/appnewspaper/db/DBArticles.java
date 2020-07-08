package com.appnewspaper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;


import com.appnewspaper.model.Article;
import com.appnewspaper.model.Image;
import com.appnewspaper.utils.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class DBArticles {
    public static final String TAG="DBArticles";
    private static ArticleDatabaseHelper helper;
    public static void init(Context context){
        helper=new ArticleDatabaseHelper(context);
    }



    //
    public static ArrayList<Article> loadAllArticles(){
        ArrayList<Article> articles=new ArrayList<>();
        Article article;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor=db.query(Constants.DB_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Constants.DB_TABLE_FIELD_ID+" DESC"
        );
        cursor.moveToFirst();
        int columnIndex;
        while(!cursor.isAfterLast()){
            //reset columnIndex
            columnIndex=0;
            //_id
            int idArticle =cursor.getInt(columnIndex);
            columnIndex++;
            //title
            String title=cursor.getString(columnIndex);
            columnIndex++;
            //subtitle
            String subtitle=cursor.getString(columnIndex);
            columnIndex++;
            //category
            String category = cursor.getString(columnIndex);
            columnIndex++;
            //abstract
            String abstractText=cursor.getString(columnIndex);
            columnIndex++;
            //body
            String body=cursor.getString(columnIndex);
            columnIndex++;
            //image
            String b64Image=cursor.getString(columnIndex);
            columnIndex++;
            //description
            String description = cursor.getString(columnIndex);
            columnIndex++;
            //Id User
            int idUsr = cursor.getInt(columnIndex);

            //
            //
            article=new Article(category,title,abstractText,body,subtitle,idUsr+"");
            try {
                article.addImage(b64Image,description);
            } catch (Exception e) {
            }
            article.setId(idArticle);
            articles.add(article);
            cursor.moveToNext();
        }
        return articles;
    }

    public static Article readArticle(int id_article){
        Article a=null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.query(Constants.DB_TABLE_NAME,
                    null,
                    Constants.DB_TABLE_FIELD_ID + " = " + id_article,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();
            int columnIndex;
            while (!cursor.isAfterLast()) {
                //reset columnIndex
                columnIndex = 0;
                //_id
                int idArticle = cursor.getInt(columnIndex);
                columnIndex++;
                //title
                String title = cursor.getString(columnIndex);
                columnIndex++;
                //subtitle
                String subtitle = cursor.getString(columnIndex);
                columnIndex++;
                //category
                String category = cursor.getString(columnIndex);
                columnIndex++;
                //abstract
                String abstractText = cursor.getString(columnIndex);
                columnIndex++;
                //body
                String body = cursor.getString(columnIndex);
                columnIndex++;
                //image
                String b64Image = cursor.getString(columnIndex);
                columnIndex++;
                //description
                String description = cursor.getString(columnIndex);
                columnIndex++;
                //Id User
                int idUsr = cursor.getInt(columnIndex);

                //
                //
                a = new Article(category, title, abstractText, body, subtitle, idUsr + "");
                try {
                    a.addImage(b64Image, description);
                } catch (Exception e) {
                }
                a.setId(idArticle);
                cursor.moveToNext();
            }
        }catch (Exception exRead){
            a=null;
        }
        return a;
    }

    public static void saveArticle(Article article){
        //Validacion
        if(readArticle(article.getId())==null) {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.DB_TABLE_FIELD_ID, article.getId());
            values.put(Constants.DB_TABLE_FIELD_TITLE, article.getTitleText());
            values.put(Constants.DB_TABLE_FIELD_SUBTITLE, article.getSubtitleText());
            values.put(Constants.DB_TABLE_FIELD_ABSTRACT, article.getAbstractText());
            values.put(Constants.DB_TABLE_FIELD_CATEGORY, article.getCategory());
            values.put(Constants.DB_TABLE_FIELD_BODY, article.getBodyText());
            values.put(Constants.DB_TABLE_FIELD_ID_USER, article.getIdUser());
            String imagenString = "";
            String description = "";
            try {
                Image image = article.getImage();
                imagenString = image.getImage();
                description = image.getDescription();
            } catch (Exception e) {
                imagenString = SerializationUtils.IMG_STRING;
                description = "description";
                Log.i(TAG, "Error insertando la imagen");
            }
            values.put(Constants.DB_TABLE_FIELD_IMAGE, imagenString);
            values.put(Constants.DB_TABLE_FIELD_DESCRIPTION, description);
            long insertId;
            insertId = db.insert(Constants.DB_TABLE_NAME, null, values);
            Log.i(TAG, "Article created");
        }

    }

    public static void updateArticle(int idArticle,Article article){
        //Validacion
        if(readArticle(idArticle)!=null) {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.DB_TABLE_FIELD_TITLE, article.getTitleText());
            values.put(Constants.DB_TABLE_FIELD_SUBTITLE, article.getSubtitleText());
            values.put(Constants.DB_TABLE_FIELD_ABSTRACT, article.getAbstractText());
            values.put(Constants.DB_TABLE_FIELD_CATEGORY, article.getCategory());
            values.put(Constants.DB_TABLE_FIELD_BODY, article.getBodyText());
            values.put(Constants.DB_TABLE_FIELD_ID_USER, article.getIdUser());
            String imagenString = "";
            String description = "";
            try {
                Image image = article.getImage();
                imagenString = image.getImage();
                description = image.getDescription();
            } catch (Exception e) {
                imagenString = SerializationUtils.IMG_STRING;
                description = "description";
                Log.i(TAG, "Error insertando la imagen");
            }
            values.put(Constants.DB_TABLE_FIELD_IMAGE, imagenString);
            values.put(Constants.DB_TABLE_FIELD_DESCRIPTION, description);
            long insertId;
            insertId = db.update(Constants.DB_TABLE_NAME,
                    values, Constants.DB_TABLE_FIELD_ID + " = " + idArticle,
                    null
            );
            Log.i(TAG, "Article created");
        }
    }

    public static void deleteArticle(int idArticle){
        SQLiteDatabase db = helper.getWritableDatabase();
        String where=Constants.DB_TABLE_FIELD_ID+" = ?";
        String id=idArticle+"";
        long insertId;
        insertId=db.delete(Constants.DB_TABLE_NAME,where,new String[]{id});
        Log.i(TAG,"Article deleted");
    }
}

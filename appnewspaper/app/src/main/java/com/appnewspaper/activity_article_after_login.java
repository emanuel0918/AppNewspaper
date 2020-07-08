package com.appnewspaper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appnewspaper.LoadArticleTask;
import com.appnewspaper.MainActivity;
import com.appnewspaper.R;
import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class activity_article_after_login extends AppCompatActivity {
    private boolean stayLogged;
    private boolean session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_after_login);
        //Sesion
        SharedPreferences rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion;
        try {
            mantenerSesion = (Boolean) map.get("stayLogged");
        } catch (Exception e) {
            mantenerSesion = null;
        }
        Boolean sesion1;
        try {
            sesion1 = (Boolean) map.get("session");
        } catch (Exception e) {
            sesion1 = false;
        }
        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            session = false;
            editorTwo.commit();
        } else {
            stayLogged = mantenerSesion;
            session = sesion1;
            if (!sesion1) {
                session = mantenerSesion;
            }
        }
        //

        int id_article;
        id_article=LoadArticleTask.id;

        AsyncTask<Void, Void, Article> p = new LoadArticleTask().execute();
        Article article = null;
        try {
            article = p.get();
        } catch (ExecutionException e) {
            //HORA DE BUSCAR EN LA BD
            article= DBArticles.readArticle(id_article);
        } catch (InterruptedException e) {
            //HORA DE BUSCAR EN LA BD
            article= DBArticles.readArticle(id_article);
        }
        try {
            System.out.println("UPDATE DATE " + article.getAttributes().contains("update_date"));
        }catch (Exception ex1){
            //HORA DE BUSCAR EN LA BD
            article= DBArticles.readArticle(id_article);
        }
        if(article==null){

            article= DBArticles.readArticle(id_article);
        }

        //System.out.println(article);
        Spanned htmlAsSpanned;
        ImageView imageView = (ImageView) findViewById(R.id.imageView4);
        Bitmap bitmap=null;
        try {
            bitmap=base64StringToImg(article.getImage().getImage());
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.img_article);
            imageView.setImageBitmap(bitmap);
        }
        TextView category = (TextView) findViewById(R.id.categoryArticleAferLogin);
        htmlAsSpanned = Html.fromHtml(article.getCategory());
        category.setText(htmlAsSpanned);
        TextView title = (TextView) findViewById(R.id.titleArticleAferLogin);
        htmlAsSpanned = Html.fromHtml(article.getTitleText());
        title.setText(htmlAsSpanned);
        TextView subTitle = (TextView) findViewById(R.id.subtitleArticleAferLogin);
        htmlAsSpanned = Html.fromHtml(article.getAbstractText());
        subTitle.setText(htmlAsSpanned);
        TextView body = (TextView) findViewById(R.id.bodyArticleAferLogin);
        htmlAsSpanned = Html.fromHtml(article.getBodyText());
        body.setText(htmlAsSpanned);

    }



    @Override
    protected void onDestroy() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        if (stayLogged) {
            editorTwo.putBoolean("session", true);
        } else {
            editorTwo.putBoolean("session", false);
        }
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        if (stayLogged) {
            editorTwo.putBoolean("session", true);
        } else {
            editorTwo.putBoolean("session", false);
        }
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        if (stayLogged) {
            editorTwo.putBoolean("session", true);
        } else {
            editorTwo.putBoolean("session", false);
        }
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onStop();
    }
}

package com.appnewspaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.annotation.RequiresApi;

import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class activity_article extends Activity {
    private boolean stayLogged;
    private boolean session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //Sesion
        SharedPreferences rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion;
        try {
            mantenerSesion = (Boolean) map.get("stayLogged");
        }catch (Exception e){
            mantenerSesion=null;
        }
        Boolean sesion1;
        try{
            sesion1=(Boolean)map.get("session");
        }catch (Exception e){
            sesion1=false;
        }
        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            session=false;
            editorTwo.commit();
        }else{
            stayLogged=mantenerSesion;
            session=sesion1;
            if(!sesion1) {
                session = mantenerSesion;
            }
        }
        //
        //Hacer peticion al servidor para coger el articulo

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
            e.printStackTrace();
        } catch (InterruptedException e) {
            //HORA DE BUSCAR EN LA BD
            article= DBArticles.readArticle(id_article);
            e.printStackTrace();
        }
        if(article==null){

            article= DBArticles.readArticle(id_article);
        }
        Spanned htmlAsSpanned;
        ConstraintLayout articleScreen = (ConstraintLayout) findViewById(R.id.layaoutArticle);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewArticle);
        Bitmap bitmap=null;
        try {
            bitmap=base64StringToImg(article.getImage().getImage());
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.img_article);
            imageView.setImageBitmap(bitmap);
        }
        TextView category = (TextView) findViewById(R.id.categoryArticle);
        htmlAsSpanned = Html.fromHtml(article.getCategory());
        category.setText(htmlAsSpanned);
        TextView title = (TextView) findViewById(R.id.titleArticleSelect);
        htmlAsSpanned = Html.fromHtml(article.getTitleText());
        title.setText(htmlAsSpanned);
        TextView subTitle = (TextView) findViewById(R.id.subtitleArticle2);
        htmlAsSpanned = Html.fromHtml(article.getAbstractText());
        subTitle.setText(htmlAsSpanned);
        TextView body = (TextView) findViewById(R.id.bodyArticle2);
         htmlAsSpanned = Html.fromHtml(article.getBodyText());
        body.setText(htmlAsSpanned);

        //if(!session){
        //    FloatingActionButton login_btn=(FloatingActionButton)findViewById(R.id.login_floating_btn);
        //    login_btn.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            login();
        //        }
        //    });
        //    login_btn.show();
        //}else{
        //    FloatingActionButton login_btn=(FloatingActionButton)findViewById(R.id.login_floating_btn);
        //    login_btn.hide();

        //}


    }

    private void login() {
        Intent login_intent=new Intent(activity_article.this,LoginActivity.class);
        startActivity(login_intent);
    }

    @Override
    protected void onPause() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onStop();
    }
}

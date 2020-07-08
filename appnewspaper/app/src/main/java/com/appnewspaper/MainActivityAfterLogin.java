package com.appnewspaper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.appnewspaper.model.Article;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class MainActivityAfterLogin extends AppCompatActivity {
    SharedPreferences rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_after_login);

        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.logout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = rememberMe.edit();
                editor.putBoolean("stayLogged", false);
                editor.commit();

                Intent goMain = new Intent(getBaseContext(), MainActivity.class);
                startActivity(goMain);
            }
        });

        FloatingActionButton addArtcile = (FloatingActionButton) findViewById(R.id.addArticle);
        addArtcile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goNewArtcile = new Intent(getBaseContext(), NewArticleActivity.class);
                startActivity(goNewArtcile);
            }
        });


        AsyncTask<Void, Void, List<Article>> p = new LoadArticlesTask().execute();

        try {
            final List<Article> articles2 = p.get();
            ListView listNews = (ListView) findViewById(R.id.listNewsAfterLogin);
            ArrayList<Article> myData = (ArrayList<Article>) articles2;
            MyAdapter adapter = new MyAdapter(MainActivityAfterLogin.this, myData);
            listNews.setAdapter(adapter);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

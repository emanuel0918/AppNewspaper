package com.appnewspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.model.ArticleAdapter;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ArticleListFragment extends Fragment {
    private boolean session;
    private boolean stayLogged;


    private ListView newListView;

    public ArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        DBArticles.init(getActivity().getApplicationContext());
        SharedPreferences rememberMe = getActivity().getBaseContext().getSharedPreferences(
                "rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion;
        Boolean sesionControlMenu = false;
        try {
            mantenerSesion = (Boolean) map.get("stayLogged");
            sesionControlMenu = (Boolean) map.get("session");
        } catch (Exception e) {
            mantenerSesion = null;
        }

        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            editorTwo.putBoolean("stayLogged", false);
            session = false;
            stayLogged = false;
            editorTwo.commit();
        } else {
            if (mantenerSesion) {
                stayLogged = true;
                session = true;
                LoadLoginTask loginTask;
                try {
                    loginTask = new LoadLoginTask();
                    loginTask.stayLoggin = mantenerSesion;
                    loginTask.user = (String) map.get("user");
                    loginTask.password = (String) map.get("password");
                    loginTask.apiKey = (String) map.get("apiKey");
                    loginTask.authType = (String) map.get("authUser");
                    loginTask.execute();
                    String result = loginTask.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if(sesionControlMenu){
                    session = true;
                }else{
                    session = false;
                }
            }
        }
        //
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newListView=(ListView)getView().findViewById(R.id.news_list);
        ArrayList<String> categorias_por_filtrar=new ArrayList<>(); //Agregamos las categorias del strings.xml
        ArrayList<Article> articles=null;
        List<Article> articles_non_filtered=new LinkedList<>();
        Article a;
        if(isNetworkAvailable()) {
            try {
                AsyncTask<Void, Void, List<Article>> p = new LoadArticlesTask().execute();
                //new FetchDataTask().execute("http://sanger.dia.fi.upm.es/pmd-task/articles");
                //new FetchDataTask().execute("https://DEV_TEAM_07:89423@sanger.dia.fi.upm.es/pmd-task/");
                //articles_non_filtered = p.get();
                List<Article> listSE = p.get();
                for (Article aSE : listSE) {
                    articles_non_filtered.add(aSE);
                }

            } catch (Exception er) {

            }
        }else {
            try {
                List<Article> listDB = DBArticles.loadAllArticles();
                for (Article aDB : listDB) {
                    articles_non_filtered.add(aDB);
                }
            } catch (Exception dbE) {
            }
        }

         //
         //
         //FILTRADO
         articles=new ArrayList<>();
         boolean filtrar=false;
         int filtro=((MainActivity)getActivity()).filter;
         switch (filtro) {
             case 0:
                 categorias_por_filtrar.add("National");
                 categorias_por_filtrar.add("Nacional");
                 filtrar=true;
                 break;
             case 1:
                 categorias_por_filtrar.add("Economy");
                 categorias_por_filtrar.add("Economia");
                 filtrar=true;
                 break;
             case 2:
                 categorias_por_filtrar.add("Sports");
                 categorias_por_filtrar.add("Deportes");
                 filtrar=true;
                 break;
             case 3:
                 categorias_por_filtrar.add("Technology");
                 categorias_por_filtrar.add("Tecnologia");
                 filtrar=true;
                 break;
             default:
                 break;
         }
        if(filtrar){
             for(Article article_category:articles_non_filtered){
                 for(String category_resource:categorias_por_filtrar){
                     if(article_category.getCategory().equals(category_resource)){
                         articles.add(article_category);
                     }
                 }
             }
         }else {
            //Opcion para no filtrar
            for(Article artic:articles_non_filtered){
                articles.add(artic);
            }
         }
        //
        //

        //Interfaz
        //
        if(session){
            //Interfaz del usuario que haya iniciado sesion
            //
            //
            FloatingActionButton publishArticlefloatingButton;
            publishArticlefloatingButton= (FloatingActionButton)getView().findViewById((R.id.publish_new_floating_btn));
            publishArticlefloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publishArticle();
                }
            });
            publishArticlefloatingButton.show();
            MyAdapter articleAdapter= new MyAdapter(getActivity().getBaseContext(),articles);
            //IMPORTANTE
            articleAdapter.setActivity(getActivity());
            //
            newListView.setAdapter(articleAdapter);
            super.onViewCreated(view, savedInstanceState);
        }else{
            //Interfaz del usuario que no haya iniciado sesion
            //
            //
            FloatingActionButton publishArticlefloatingButton;
            publishArticlefloatingButton= (FloatingActionButton)getView().findViewById((R.id.publish_new_floating_btn));
            publishArticlefloatingButton.hide();
            ArticleAdapter articleAdapter= new ArticleAdapter(getActivity().getApplicationContext(),articles);
            newListView.setAdapter(articleAdapter);
            super.onViewCreated(view, savedInstanceState);

        }
    }

    private void publishArticle() {
        ((MainActivity)getActivity()).publishArticle();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
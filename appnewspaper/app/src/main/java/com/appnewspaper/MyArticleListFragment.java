package com.appnewspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.concurrent.ExecutionException;

public class MyArticleListFragment extends Fragment {
    //
    private ListView newListView;
    private SharedPreferences rememberMe;

    public MyArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        DBArticles.init(getActivity().getApplicationContext());;
        //Sesion
        rememberMe = getActivity().getBaseContext().getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_new_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newListView=(ListView)getView().findViewById(R.id.news_list);
        ArrayList<Article> myArticles=null;
        try {
            myArticles = DBArticles.loadAllArticles();
        }catch (Exception e){

        }

        //MyAdapter articleAdapter= new MyAdapter(getActivity().getApplicationContext(),myArticles);
        //newListView.setAdapter(articleAdapter);
        super.onViewCreated(view, savedInstanceState);
    }
}
package com.appnewspaper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.appnewspaper.model.Article;
import com.appnewspaper.utils.SerializationUtils;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;
import com.testlistview.Modify_article_after_login;

public class AddArticleTask extends AsyncTask<Void, Void, Article> {
    public static Article article;
    public static final int OPCION_MAIN_ACTIVITY=1;
    public static final int OPCION_MODIFY_ACTIVITY=2;
    private int opcion;
    private MainActivity mainActivity;
    private Modify_article_after_login modifyActivity;
    private boolean error;

    public void setOpcion(int opcion) {
        this.opcion = opcion;
    }

    public void setMainActivity(MainActivity activity) {
        this.mainActivity = activity;
    }

    public void setModifyActivity(Modify_article_after_login activity) {
        this.modifyActivity = activity;
    }

    @Override
    protected Article doInBackground(Void... voids) {
        error=false;
        try {
            if (ModelManager.isConnected()) {
                ModelManager.saveArticle(article);
            }
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
            error=true;
        }
        return article;
    }

    @Override
    protected void onPostExecute(Article article) {
        switch (opcion){
            case OPCION_MAIN_ACTIVITY:
                if(error){
                    mainActivity.error_result();
                }else {
                    mainActivity.article_created();
                }
                break;
            case OPCION_MODIFY_ACTIVITY:
                if (error){
                    modifyActivity.error_result();
                }else{
                    modifyActivity.article_modified();
                }
                break;
            default:
                break;
        }
        super.onPostExecute(article);

    }
}
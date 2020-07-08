package com.appnewspaper;

import android.os.AsyncTask;
import android.util.Log;

import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.AuthenticationError;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

public class LoadArticleTask extends AsyncTask<Void, Void, Article> {

    private static final String TAG = "LoadArticleTask";
    public static int id;
    private String strIdUser;
    private String strApiKey;
    private String strIdAuthUser;

    Article article = null;


    @Override
    protected Article doInBackground(Void... voids) {
        //ModelManager uses singleton pattern, connecting once per app execution in enough
        try {
            // obtain 6 articles from offset 0
            if (ModelManager.isConnected()) {
                article = ModelManager.getArticle(id);
            } else {
                ModelManager.login("DEV_TEAM_07", "89423");
                article = ModelManager.getArticle(id);
            }

            //Article article2 = ModelManager.getArticle(145);
            Log.i(TAG, String.valueOf(article));
        } catch (ServerCommunicationError e) {
            Log.e(TAG, e.getMessage());
        } catch (AuthenticationError authenticationError) {
            authenticationError.printStackTrace();
        }


        return article;
    }

    public void setId(int id) {
        this.id = id;
    }
}

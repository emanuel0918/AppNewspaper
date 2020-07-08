package com.appnewspaper;

import android.os.AsyncTask;
import android.util.Log;

import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.AuthenticationError;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.LinkedList;
import java.util.List;


public class LoadArticlesTask extends AsyncTask<Void, Void, List<Article>> {

    private static final String TAG = "LoadArticlesTask";
    private String strIdUser;
    private String strApiKey;
    private String strIdAuthUser;

    private  int buffer;

    List<Article> res = null;

    @Override
    protected List<Article> doInBackground(Void... voids) {

        //ModelManager uses singleton pattern, connecting once per app execution in enough


        //ARTICULOS GUARDADOS SOLAMENTE EN LA BD
        int length=0;
        try{
            length=DBArticles.loadAllArticles().size();
        }catch (Exception dbException){
            length=0;

        }
        if(length==0){
            buffer=1000;
        }else {
            buffer=6;
        }


        //ARTICULOS GUARDADOS EN SERVER
        try {
            // obtain 6 articles from offset 0
            if (ModelManager.isConnected()) {
                res = ModelManager.getArticles(buffer,0);
            } else {
                ModelManager.login("DEV_TEAM_07", "89423");
                res = ModelManager.getArticles(buffer, 0);
                //Article article2 = ModelManager.getArticle(145);

            }

            //Guardar en la BD solamente si si ha funcionado exitosamente
            List<Article> arts=new LinkedList<>();
            Article art2;
            try {
                for (Article article : res) {
                    // We print articles in Log
                    //Log.i(TAG, String.valueOf(article));
                    art2 = new Article(article.getCategory(), article.getTitleText(), article.getAbstractText(),
                            article.getBodyText(), article.getSubtitleText(), "16");
                    art2.setImage(article.getImage());
                    art2.setId(article.getId());
                    arts.add(art2);
                    //ESTO LO HIZO MAL PORQUE LOS IDS FUERON INCORRECTOS
                    //DBArticles.saveArticle(article);

                    DBArticles.saveArticle(art2);
                }
            }catch (Exception ee2){

            }
            //for(int i=arts.size()-1;i>=0;i--){
            //}
        } catch (ServerCommunicationError e) {
            Log.e(TAG, e.getMessage());
        } catch (AuthenticationError authenticationError) {
            authenticationError.printStackTrace();
        }

        return res;

    }


    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
    }
}


package com.appnewspaper;

import android.os.AsyncTask;
import android.util.Log;

import com.appnewspaper.model.Article;
import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.AuthenticationError;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import static com.appnewspaper.utils.Logger.TAG;

public class DeleteArticleTask extends AsyncTask<Void, Void, Integer> {
    public static int id;

    /**
     * @param voids
     * @return
     */
    @Override
    protected Integer doInBackground(Void... voids) {
        int isDeleted = 0;
        //ModelManager uses singleton pattern, connecting once per app execution in enough
        try {
            if (ModelManager.isConnected()) {
                //ModelManager.login("DEV_TEAM_07", "89423");
                ModelManager.deleteArticle(id);
            }
        } catch (
                ServerCommunicationError e) {
            Log.e(TAG, e.getMessage());
        }
        return isDeleted;
    }
}

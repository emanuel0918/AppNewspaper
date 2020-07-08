package com.appnewspaper;

import android.os.AsyncTask;

import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.AuthenticationError;

public class LoadLoginTask extends AsyncTask<Void, Void, String> {

    String user;
    String password;
    String userId = "0";
    String apiKey;
    String authType;

   public Boolean stayLoggin = false;

    @Override
    protected String doInBackground(Void... voids) {

        try {
            if (stayLoggin) {
                ModelManager.stayloggedin(user, apiKey, authType);
            } else {
                ModelManager.login(user, password);
                userId = ModelManager.getLoggedIdUSer();
                authType = ModelManager.getLoggedAuthType();
                apiKey = ModelManager.getLoggedApiKey();
            }
        } catch (AuthenticationError authenticationError) {
            authenticationError.printStackTrace();
        }


        return userId;
    }
}

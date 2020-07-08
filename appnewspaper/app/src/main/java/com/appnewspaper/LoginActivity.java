package com.appnewspaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appnewspaper.utils.network.ModelManager;
import com.appnewspaper.utils.network.exceptions.AuthenticationError;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button buttonLogin = (Button) findViewById(R.id.login_btn);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()) {
                    //Obtener los valores introducidos en los campos
                    TextInputEditText usernameForm = (TextInputEditText) findViewById(R.id.user);
                    String username = usernameForm.getText().toString();
                    TextInputEditText passwordForm = (TextInputEditText) findViewById(R.id.password);
                    String password = passwordForm.getText().toString();
                    //Text in Values
                    String warning_string = getResources().getString(R.string.warning);
                    String data_incorrect = getResources().getString(R.string.data_incorrect);
                    String ok = getResources().getString(R.string.ok);


                    System.out.println("Username " + username + " " + " Password " + password);
                    if (username.equals("") || password.equals("")) {
                        String required_string = getResources().getString(R.string.required_data);
                        usernameForm.setError(null);
                        passwordForm.setError(null);
                        //user
                        if (username.equals("")) {
                            usernameForm.setError(required_string, null);

                        }
                        //password
                        if (password.equals("")) {
                            passwordForm.setError(required_string, null);

                        }
                    } else {
                        try {
                            LoadLoginTask loginTask = new LoadLoginTask();
                            CheckBox rememberMe = (CheckBox) findViewById(R.id.rememberMe);
                            loginTask.password = password;
                            loginTask.user = username;
                            loginTask.execute();
                            String userLogger = loginTask.get();
                            if (userLogger.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(warning_string);
                                builder.setMessage(data_incorrect);
                                builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                //  DEV_TEAM_07", "89423"
                                SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorOne = rememberMeTwo.edit();
                                editorOne.putBoolean("session", true);
                                editorOne.putBoolean("stayLogged", rememberMe.isChecked());
                                editorOne.putString("password", password);
                                editorOne.putString("user", username);
                                editorOne.putString("idUser", userLogger);
                                if (rememberMe.isChecked()) {
                                    editorOne.putString("apiKey", loginTask.apiKey);
                                    editorOne.putString("authUser", loginTask.authType);
                                }
                                editorOne.commit();
                                //
                                //
                                //
                                //
                                Intent goMainPage = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(goMainPage);
                                finish();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(warning_string);
                            builder.setMessage(data_incorrect);

                            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(warning_string);
                            builder.setMessage(data_incorrect);

                            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                    }
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle(getResources().getString(R.string.warning));
                    builder.setMessage(getResources().getString(R.string.error_server_login));
                    builder.setPositiveButton(getResources().getString(R.string.accept),null);
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();

                }


            }
        });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

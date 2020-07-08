package com.appnewspaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Map;


//Clase hecha para el Navigation View
public class MainActivity extends AppCompatActivity {
    //Filtrado
    //public boolean CATEGORIES[];
    public int filter;
    //Sesion
    private boolean session;
    private boolean stayLogged;
    //Drawer Layout
    private DrawerLayout drawerLayout;
    //Navigation View que a su vez contiene el toolbar
    private NavigationView navigationView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolbar();
        //FILTRADO
        filter = -1;
        //String array_categories[]=getResources().getStringArray(R.array.category);
        //CATEGORIES =new boolean[4]; // las categorias
        //for(int i = 0; i< CATEGORIES.length; i++){
        //    CATEGORIES[i]=false;
        //}
        //
        //

        //Sesion
        SharedPreferences rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
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
                session = sesionControlMenu;
            }
            /*stayLogged = mantenerSesion;
            session = sesion1;
            if (!sesion1) {
                session = mantenerSesion;
            }*/


        }
        //

        //Interfaz del MainActivity (con navigation view)
        //
        //
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        //Interfaz dinamica
        if (session) {
            //Interfaz para el usuario que previamente ha iniciado sesion
            navigationView.inflateMenu(R.menu.options_user);
        } else {
            //Interfaz para el usuario general
            navigationView.inflateMenu(R.menu.options_general);
        }
        //
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ArticleListFragment()).commit();
        navigationView.getMenu().getItem(1).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());

        //Control de los botones en el menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f = null;
                switch (item.getItemId()) {
                    case R.id.menu_login:
                        logIn();
                        break;
                    case R.id.menu_1:
                        f = new PublishArticleFragment();
                        break;
                    case R.id.menu_2:
                        filter = -1;
                        f = new ArticleListFragment();
                        break;
                    case R.id.menu_logout:
                        drawerLayout.closeDrawers();
                        stayLogged = false;
                        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
                        editorTwo.clear();
                        editorTwo.commit();
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.options_general);
                        getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());
                        navigationView.getMenu().getItem(1).setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ArticleListFragment()).commit();

                        break;
                    case R.id.category_all:
                        filter = -1;
                        f = new ArticleListFragment();
                        break;
                    case R.id.category1:
                        filter = 0;
                        f = new ArticleListFragment();
                        break;
                    case R.id.category2:
                        filter = 1;
                        f = new ArticleListFragment();
                        break;
                    case R.id.category3:
                        filter = 2;
                        f = new ArticleListFragment();
                        break;
                    case R.id.category4:
                        filter = 3;
                        f = new ArticleListFragment();
                        break;
                    //case R.id.otras_1:
                    // f = new MyArticleListFragment();
                    //    break;
                }
                if (f != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
                    item.setChecked(true);
                    getSupportActionBar().setTitle(item.getTitle());
                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });
    }

    private void logIn() {
        Intent login_intent =
                new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login_intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Abrir menu
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void reload_articles() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ArticleListFragment()).commit();
        navigationView.getMenu().getItem(1).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(1).getTitle());

    }

    public void publishArticle() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new PublishArticleFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportActionBar().setTitle(navigationView.getMenu().getItem(0).getTitle());

    }


    public void article_created() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(getResources().getString(
                R.string.new_published
        ));
        builder.setMessage(getResources().getString(
                R.string.new_published
        ));
        builder.setPositiveButton(getResources().getString(
                R.string.ok
        ), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reload_articles();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    public void error_result() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(
                R.string.warning
        ));
        builder.setMessage(getResources().getString(
                R.string.error_transaction
        ));
        builder.setPositiveButton(getResources().getString(
                R.string.ok
        ), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reload_articles();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void article_modified() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(
                R.string.article_modified
        ));
        builder.setMessage(getResources().getString(
                R.string.article_modified
        ));
        builder.setPositiveButton(getResources().getString(
                R.string.ok
        ), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reload_articles();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        if (stayLogged) {
            editorTwo.putBoolean("session", true);
        } else {
            editorTwo.putBoolean("session", false);
        }
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        if (session) {
            editorTwo.putBoolean("session", true);
        } else {
            editorTwo.putBoolean("session", false);
        }
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        if (session) {
            editorTwo.putBoolean("session", true);
        } else {
            editorTwo.putBoolean("session", false);
        }
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onStop();
    }
}

package com.testlistview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.appnewspaper.AddArticleTask;

import com.appnewspaper.LoadArticleTask;
import com.appnewspaper.MainActivity;
import com.appnewspaper.MainActivityAfterLogin;

import com.appnewspaper.R;
import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.model.Image;
import com.appnewspaper.utils.SerializationUtils;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;



import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;
import static com.appnewspaper.utils.SerializationUtils.imgToBase64String;

public class Modify_article_after_login extends AppCompatActivity {
    private boolean stayLogged;
    private boolean session;
    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Article newModifArticle;
    Article article = null;
    SharedPreferences rememberMe;
    Bitmap bitmap;
    String thumbail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_article_after_login);
        //Sesion
        rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        Map<String, ?> map = rememberMe.getAll();
        Boolean mantenerSesion;
        try {
            mantenerSesion = (Boolean) map.get("stayLogged");
        }catch (Exception e){
            mantenerSesion=null;
        }
        Boolean sesion1;
        try{
            sesion1=(Boolean)map.get("session");
        }catch (Exception e){
            sesion1=false;
        }
        if (mantenerSesion == null) {
            SharedPreferences.Editor editorTwo = rememberMe.edit();
            editorTwo = rememberMe.edit();
            editorTwo.putBoolean("session", false);
            session=false;
            editorTwo.commit();
        }else{
            stayLogged=mantenerSesion;
            session=sesion1;
            if(!sesion1) {
                session = mantenerSesion;
            }
        }
        //

        Button chooseImagen =(Button) findViewById(R.id.chooseImagen);
        chooseImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
        //

        final int id_article;
        id_article=LoadArticleTask.id;


        AsyncTask<Void, Void, Article> p = new LoadArticleTask().execute();
        try {
            article = p.get();



        } catch (ExecutionException e) {
            //HORA DE BUSCAR EN LA BD
            article= DBArticles.readArticle(id_article);
            e.printStackTrace();
        } catch (InterruptedException e) {
            //HORA DE BUSCAR EN LA BD
            article= DBArticles.readArticle(id_article);
            e.printStackTrace();
        }
        if(article==null){

            article= DBArticles.readArticle(id_article);
        }
            Spanned htmlAsSpanned;
            imageView = (ImageView)findViewById(R.id.imageModify);
            String base64img;
            try {
                base64img = article.getImage().getImage();
                thumbail = article.getImage().getImage();
            } catch (ServerCommunicationError serverCommunicationError) {
                base64img=SerializationUtils.IMG_STRING;
                thumbail=SerializationUtils.IMG_STRING;
            }
            bitmap=base64StringToImg(base64img);
            imageView.setImageBitmap(bitmap);
            TextView title = (TextView) findViewById(R.id.titleModify);
            htmlAsSpanned = Html.fromHtml(article.getTitleText());
            title.setText(htmlAsSpanned);
            TextView subTitle = (TextView) findViewById(R.id.subtitleModify);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            subTitle.setText(htmlAsSpanned);
            TextView abstractText = (TextView)findViewById(R.id.abstractModify);
            htmlAsSpanned = Html.fromHtml(article.getAbstractText());
            abstractText.setText(htmlAsSpanned);
            TextView body = (TextView)findViewById(R.id.bodyModify);
            htmlAsSpanned = Html.fromHtml(article.getBodyText());
            body.setText(htmlAsSpanned);

            Spinner spinnerCategory = (Spinner)findViewById(R.id.categoryModify);

            String[] opciones = setcategory(article.getCategory());
            ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            spinnerCategory.setAdapter(adapterCategory);


            //
            Button saveArticleModify = (Button)findViewById(R.id.saveModify);
            View.OnClickListener clickListener=null;
            if(isNetworkAvailable()){
                clickListener=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Vamos a coger los valores para modificar el articulo
                        TextView title = (TextView)findViewById(R.id.titleModify);
                        String titleAccept = String.valueOf(title.getText());
                        //System.out.println("Titulo cambiado " + titleAccept);
                        TextView subTitle = (TextView) findViewById(R.id.subtitleModify);
                        String subTitleAccept = String.valueOf(subTitle.getText());
                        //System.out.println("Subtitulo cambiado " + t);
                        TextView abstractText = (TextView)findViewById(R.id.abstractModify);
                        String abstractAccept = String.valueOf(abstractText.getText());
                        //System.out.println("Abstract cambiado " + abstractAccept);
                        TextView body = (TextView)findViewById(R.id.bodyModify);
                        String bodyAccept = String.valueOf(body.getText());
                        //System.out.println("Body cambiado " + bodyAccept);
                        Spinner spinnerCategory = (Spinner) findViewById(R.id.categoryModify);
                        String addCategory = String.valueOf(spinnerCategory.getSelectedItem());
                        //System.out.println("Category cambiado " + addCategory);
                        rememberMe = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
                        Map<String, ?> map = rememberMe.getAll();
                        String userId = (String) map.get("idUser");
                        article.setTitleText(titleAccept);
                        article.setCategory(addCategory);
                        article.setAbstractText(abstractAccept);
                        article.setBodyText(bodyAccept);
                        article.setSubtitleText(subTitleAccept);
                        String imageDescription;
                        try {
                            thumbail = imgToBase64String(bitmap);
                            imageDescription = article.getImage().getDescription();
                        } catch (ServerCommunicationError serverCommunicationError) {
                            thumbail=SerializationUtils.IMG_STRING;
                            imageDescription="";
                        }
                        Image image = new Image(0,imageDescription, article.getId(), thumbail);
                        article.setImage(image);

                        //newModifArticle = new Article(addCategory, titleAccept, abstractAccept, bodyAccept, subTitleAccept, userId);
                        try{DBArticles.updateArticle(id_article,article);}catch (Exception dbEx){}
                        //
                        try {
                            AddArticleTask saveArticle = new AddArticleTask();
                            saveArticle.setOpcion(AddArticleTask.OPCION_MODIFY_ACTIVITY);
                            saveArticle.setModifyActivity(Modify_article_after_login.this);
                            saveArticle.article=article;
                            saveArticle.execute();
                            newModifArticle = saveArticle.get();
                            //SI LA LLAMADA A MODIFICAR EL ARTICULO HA IDO BIEN
                            //AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                            //builder.setTitle(getResources().getString(R.string.warning));
                            //builder.setMessage(getResources().getString(R.string.article_modified));

                            //builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            //    @Override
                            //    public void onClick(DialogInterface dialog, int which) {
                            //        Intent back_intent=new Intent(Modify_article_after_login.this, MainActivity.class);
                            //        startActivity(back_intent);
                            // reload_articles();
                            //    }
                            //});
                            //AlertDialog alertDialog = builder.create();
                            //alertDialog.show();
                            //Intent back_intent=new Intent(Modify_article_after_login.this, MainActivity.class);
                            //startActivity(back_intent);
                            // reload_articles();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                            builder.setTitle(getResources().getString(R.string.warning));
                            builder.setMessage(getResources().getString(R.string.error_server_modify));

                            builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } catch (InterruptedException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                            builder.setTitle(getResources().getString(R.string.warning));
                            builder.setMessage(getResources().getString(R.string.error_server_modify));

                            builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            e.printStackTrace();
                        }

                    }
                };
            }else {
                clickListener= new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder=
                                new AlertDialog.Builder(Modify_article_after_login.this);
                        builder.setTitle(getResources().getString(R.string.warning));
                        builder.setMessage(getResources().getString(R.string.error_server_modify));
                        builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reload_articles();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

                    }
                };
            }
            saveArticleModify.setOnClickListener(clickListener);


            Button cancelArticleModify =(Button) findViewById(R.id.cancelModify);
            cancelArticleModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);
                    builder.setTitle(getResources().getString(R.string.warning));
                    builder.setMessage(getResources().getString(R.string.modify_article));

                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Me quedo donde estoyi
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent goMainAfterLogin = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(goMainAfterLogin);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            });

    }

    public String[] setcategory(String categoryArticle) {
        //Only English
        //Resources standardResources = Modify_article_after_login.this.getResources();
        //AssetManager assets = standardResources.getAssets();
        //DisplayMetrics metrics = standardResources.getDisplayMetrics();
        //Configuration config = new Configuration(standardResources.getConfiguration());
        //config.locale = Locale.US;
        //Resources defaultResources = new Resources(assets, metrics, config);
        //String[] set = defaultResources.getStringArray(R.array.category);
        String[] set = getResources().getStringArray(R.array.category);
        int index;
        if(categoryArticle.equals("National") || categoryArticle.equals("Nacional")){
            index=0;
        }else if(categoryArticle.equals("Economy") || categoryArticle.equals("Economia")){
            index=1;
        }else if(categoryArticle.equals("Sports") || categoryArticle.equals("Deportes")){
            index=2;
        }else if(categoryArticle.equals("Tecnology") || categoryArticle.equals("Tecnologia") ){
            index=3;
        }else{
            index=1;
        }
        set[index]=categoryArticle;
        String new_set[]=new String[set.length];
        for(int i=0;i<set.length;i++){
            new_set[i]=set[i];
        }
        String temp=set[0];
        new_set[0]=categoryArticle;
        new_set[index]=temp;
        return new_set;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void error_result(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);

        builder.setTitle(getResources().getString(
                R.string.warning
        ));
        builder.setMessage(getResources().getString(
                R.string.error_transaction
        ));
        builder.setPositiveButton(getResources().getString(
                R.string.ok
        ),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reload_articles();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    public void article_modified(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_article_after_login.this);

        builder.setTitle(getResources().getString(
                R.string.article_modified
        ));
        builder.setMessage(getResources().getString(
                R.string.article_modified
        ));
        builder.setPositiveButton(getResources().getString(
                R.string.ok
        ),new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reload_articles();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void reload_articles() {
        Intent intent=new Intent(Modify_article_after_login.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap); //FUNCIONA
                try {
                    thumbail = imgToBase64String(bitmap);
                }catch (Exception e){
                    thumbail=com.appnewspaper.utils.SerializationUtils.IMG_STRING;
                }
                if(thumbail==null){

                    thumbail=com.appnewspaper.utils.SerializationUtils.IMG_STRING;
                }else if (thumbail.equals("")){

                    thumbail=com.appnewspaper.utils.SerializationUtils.IMG_STRING;
                }
                //System.out.println("THUMBAIL EEEEEEE " + thumbail);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onPause();
    }

    @Override
    protected void onStop() {
        SharedPreferences rememberMeTwo = getSharedPreferences("rememberMe", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorTwo = rememberMeTwo.edit();
        //editorTwo.putBoolean("session", false);
        editorTwo.putBoolean("stayLogged", stayLogged);
        editorTwo.commit();
        super.onStop();
    }

}

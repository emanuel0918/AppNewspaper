package com.appnewspaper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;


import com.appnewspaper.db.DBArticles;
import com.appnewspaper.model.Article;
import com.appnewspaper.utils.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class PublishArticleFragment extends Fragment {
    private boolean session;
    private boolean stayLogged;

    //
    private SharedPreferences rememberMe;

    public final int REQUEST_OPEN_IMAGE =203;
    //
    private String b64Image;
    private ImageView imageView;
    private ImageButton imageButton;
    private Button publishArticleButton;
    private Button cancelButton;
    private Bitmap bitmap;
    //

    public PublishArticleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // BD
        //DBArticles.init(getActivity().getApplicationContext());
        //Sesion
        rememberMe = getActivity().getBaseContext().getSharedPreferences(
                "rememberMe", Context.MODE_PRIVATE);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publish_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //
        imageView=(ImageView)getView().findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        imageButton=(ImageButton)getView().findViewById(R.id.image_btn_add_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        publishArticleButton =(Button)getView().findViewById(R.id.publish_new_btn);
        publishArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishNew();
            }
        });
        cancelButton =(Button)getView().findViewById(R.id.publish_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
                SomeDialog builder=new SomeDialog();
                builder.setOpcion(SomeDialog.MULTIPLE);
                builder.setTitle(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.warning
                        ));
                builder.setMessage(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.discard_changes
                        ));
                builder.setNegativeButton(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.cancel
                        ),null);
                builder.setPositiveButton(getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.accept
                        ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload_articles();
                    }
                });
                builder.show(transaction,"dialog");
            }
        });

        Spinner spinnerCategory = (Spinner) getView().findViewById(R.id.category_new);
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(), R.array.category, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        super.onViewCreated(view, savedInstanceState);
    }

    private void publishNew() {
        ((Button)getView().findViewById(R.id.publish_new_btn)).setEnabled(false);
            //
            String required_data = getActivity().getBaseContext().getResources().getString(R.string.required_data);
            //
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            SomeDialog builder = new SomeDialog();
            DialogInterface.OnClickListener clickListener = null;
            EditText title_form = ((EditText) getView().findViewById(R.id.title));
            EditText subtitle_form = ((EditText) getView().findViewById(R.id.subtitle));
            Spinner categorySpinner = (Spinner) getView().findViewById(R.id.category_new);
            EditText abstrac_form = ((EditText) getView().findViewById(R.id.abstrac));
            EditText description_form = ((EditText) getView().findViewById(R.id.description));
            EditText body_form = ((EditText) getView().findViewById(R.id.body));
            if (!((title_form.getText().toString().equals("")) &&
                    !(subtitle_form.getText().toString().equals(""))) &&
                    !(abstrac_form.getText().toString().equals("")) &&
                    !(description_form.getText().toString().equals("")) &&
                    !(body_form.getText().toString().equals(""))) {
                if(isNetworkAvailable()) {
                //CREATED
                String new_published = "";
                try {
                    String title = title_form.getText().toString();
                    String subtitle = subtitle_form.getText().toString();
                    String category = String.valueOf(categorySpinner.getSelectedItem());
                    String abstrac = abstrac_form.getText().toString();
                    String body = body_form.getText().toString();
                    String description = description_form.getText().toString();
                    Map<String, ?> map = rememberMe.getAll();
                    String userId = (String) map.get("idUser");
                    Article article = new Article(category, title, abstrac, body, subtitle, userId);
                    b64Image = null;

                    try {
                        b64Image = SerializationUtils.encodeImage(bitmap);
                        //article.addImage(b64Image, description);

                        //article.setImage(new Image());
                    } catch (Exception ee) {
                        b64Image = SerializationUtils.IMG_STRING;

                    }
                    if (b64Image == null) {
                        b64Image = SerializationUtils.IMG_STRING;

                    } else if (b64Image.equals("")) {
                        b64Image = SerializationUtils.IMG_STRING;

                    }
                    article.addImage(b64Image, description);
                    //long timestamp=0;
                    //set datetime


                    // CREAR SOLO EN LA BD
                    //try {
                    //DBArticles.saveArticle(article);
                    //}catch (Exception e){

                    //}

                    //CREAR EN EL WEB SERVICE

                    AddArticleTask addArticleTask;

                    addArticleTask = (AddArticleTask) new AddArticleTask();
                    addArticleTask.setOpcion(AddArticleTask.OPCION_MAIN_ACTIVITY);
                    addArticleTask.setMainActivity((MainActivity) getActivity());
                    addArticleTask.article = article;
                    try {
                        addArticleTask.execute();
                    } catch (Exception e) {

                    }
                } catch (Exception e) {

                }

            }else {
                    builder.setTitle(getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.warning
                            ));
                    builder.setMessage(getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.error_server_add
                            ));
                    builder.setPositiveButton(getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.accept
                            ), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //reload_articles();
                        }
                    });
                    builder.show(transaction,"dialog");
                }
            } else {
                title_form.setError(null);
                if (title_form.getText().toString().equals("")) {
                    title_form.setError(required_data, null);
                }
                subtitle_form.setError(null);
                if (subtitle_form.getText().toString().equals("")) {
                    subtitle_form.setError(required_data, null);
                }
                abstrac_form.setError(null);
                if (abstrac_form.getText().toString().equals("")) {
                    abstrac_form.setError(required_data, null);
                }
                body_form.setError(null);
                if (body_form.getText().toString().equals("")) {
                    body_form.setError(required_data, null);
                }
                description_form.setError(null);
                if (description_form.getText().toString().equals("")) {
                    description_form.setError(required_data, null);
                }

                String missingData = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.missing_data
                        );
                String missingValue = "";
                if (title_form.getText().toString().equals("")) {
                    missingValue = getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.title
                            );
                } else if (subtitle_form.getText().toString().equals("")) {
                    missingValue = getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.subtitle
                            );

                } else if (abstrac_form.getText().toString().equals("")) {
                    missingValue = getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.abstrac
                            );

                } else if (body_form.getText().toString().equals("")) {
                    missingValue = getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.body
                            );

                } else if (description_form.getText().toString().equals("")) {
                    missingValue = getActivity().getApplicationContext()
                            .getResources().getString(
                                    R.string.description
                            );

                }
                builder.setTitle(missingData);
                builder.setMessage(missingValue);

                String accept_btn = getActivity().getApplicationContext()
                        .getResources().getString(
                                R.string.ok
                        );

                builder.setPositiveButton(accept_btn, clickListener);
                builder.show(transaction, "dialog");
            }
            //
            //
        publishArticleButton.setEnabled(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void reload_articles() {
        ((MainActivity)getActivity()).reload_articles();
    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/jpg","image/png"});
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_OPEN_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_OPEN_IMAGE:
                if(resultCode== Activity.RESULT_OK){
                    InputStream stream=null;
                    try{
                        stream=getActivity()
                                .getApplicationContext()
                                .getContentResolver()
                                .openInputStream(data.getData());
                        bitmap= BitmapFactory.decodeStream(stream);
                        imageView.setImageBitmap(bitmap);
                    }catch (Exception e){

                    }finally {
                        if(stream!=null){
                            try {
                                stream.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                break;
        }
    }

}
package com.appnewspaper;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appnewspaper.model.Article;
import com.appnewspaper.model.Image;
import com.appnewspaper.utils.network.exceptions.ServerCommunicationError;

import java.util.ArrayList;

import static com.appnewspaper.utils.SerializationUtils.base64StringToImg;

public class AdapterBeforeLogin extends ArrayAdapter<Article> {

    Context mContext;

    public AdapterBeforeLogin(Context context, ArrayList<Article> records) {
        super(context, 0, records);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Article item = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main, null, true);
        }

        final TextView listTitle = (TextView) convertView.findViewById(R.id.textView2AfterLogin);
        final TextView listCategory = (TextView) convertView.findViewById(R.id.categoryAfterLogin);
        final TextView listAbstract = (TextView) convertView.findViewById(R.id.textView3AfterLogin);
        ImageView imageList = (ImageView) convertView.findViewById(R.id.imageView2);

        /*Image i = null;
        try {
            i = item.getImage();
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
       System.out.println("Image " + i);
       System.out.println("Image " + i.getImage());*/


        try {
            imageList.setImageBitmap(base64StringToImg(item.getImage().getImage()));

            Spanned title = Html.fromHtml(item.getTitleText());
            listTitle.setText(title);
            listCategory.setText(item.getCategory());
            Spanned htmlAsSpanned = Html.fromHtml(item.getAbstractText());
            listAbstract.setText(htmlAsSpanned);
            //listAbstract.setText(item.getAbstractText());
            imageList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int id2 = (int) id;
                    LoadArticleTask.id = item.getId();
                    System.out.println("ID EEEEEE " + item.getId());
                    //if (position == 0 || position < articles2.size()) {
                    Intent goToArticle = new Intent(mContext, activity_article.class);
                    goToArticle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.getApplicationContext().startActivity(goToArticle);
                    //}
                }
            });
        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
        return convertView;
    }


}

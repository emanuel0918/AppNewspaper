package com.appnewspaper.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appnewspaper.LoadArticleTask;
import com.appnewspaper.R;
import com.appnewspaper.activity_article;
import com.appnewspaper.utils.SerializationUtils;

import java.nio.ByteBuffer;
import java.util.List;

public class ArticleAdapter extends BaseAdapter {
    private List<Article> arrayData;
    private Context context;
    public ArticleAdapter(Context ctx, List<Article> list){
        super();
        this.context=ctx;
        this.arrayData=list;
    }
    @Override
    public int getCount() {
        return arrayData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(this.context);
            convertView=layoutInflater.inflate(R.layout.new_template_layout,null);
        }
        final Article article= arrayData.get(position);
        ((TextView)convertView.findViewById(R.id.categoryNew)).setText(article.getCategory());
        ((TextView)convertView.findViewById(R.id.titleNew)).setText(article.getTitleText());
        ((TextView)convertView.findViewById(R.id.abstractNew)).setText(article.getAbstractText());
        Bitmap bitmap=null;
        try {
            bitmap= SerializationUtils.base64StringToImg(article.getImage().getImage());
            ((ImageView)convertView.findViewById(R.id.imageNew)).setImageBitmap(bitmap);
        } catch (Exception e) {
        }
        ((RelativeLayout)convertView.findViewById(R.id.new_template_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int id2 = (int) id;
                LoadArticleTask.id = article.getId();
                System.out.println("ID EEEEEE " + article.getId());
                //if (position == 0 || position < articles2.size()) {
                Intent goToArticle = new Intent(context, activity_article.class);
                goToArticle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(goToArticle);

            }
        });
        return convertView;
    }
}

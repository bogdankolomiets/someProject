package com.example.root.douclient.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.douclient.R;
import com.example.root.douclient.fragments.NewsFragmentTest;
import com.example.root.douclient.objects.NewsArticle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 08.12.15.
 */
public class NewsPageAdapter extends ArrayAdapter<NewsArticle> {

    private Context context;
    private ArrayList<NewsArticle> data;

    public NewsPageAdapter(Context context, ArrayList<NewsArticle> data) {
        super(context, R.layout.listview_item_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewsArticleHolder newsArticleHolder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context) .getLayoutInflater();
            row = inflater.inflate(R.layout.listview_item_row, parent, false);


            newsArticleHolder = new NewsArticleHolder();
            newsArticleHolder.imgNewsArticleIcon = (CircleImageView) row.findViewById(R.id.imgNewsArticleIcon);
            newsArticleHolder.txtNewsArticleAuthor = (TextView) row.findViewById(R.id.txtNewsArticleAuthor);
            newsArticleHolder.txtNewsArticle = (TextView) row.findViewById(R.id.txtNewsArticle);

            row.setTag(newsArticleHolder);
        } else newsArticleHolder = (NewsArticleHolder) row.getTag();

        NewsArticle newsArticle = data.get(position);
        newsArticleHolder.txtNewsArticleAuthor.setText(newsArticle.getNewsArticleTitle());
        newsArticleHolder.txtNewsArticle.setText(newsArticle.getNewsArticleText());
        Picasso.with(context)
                .load(newsArticle.getImageOfArticleURL())
                .into(newsArticleHolder.imgNewsArticleIcon);



        return row;
    }

    private static class NewsArticleHolder {
        CircleImageView imgNewsArticleIcon;
        TextView txtNewsArticleAuthor;
        TextView txtNewsArticle;
    }
}

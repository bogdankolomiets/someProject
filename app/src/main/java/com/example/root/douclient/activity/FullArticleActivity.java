package com.example.root.douclient.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.douclient.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by root on 14.12.15.
 */
public class FullArticleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView toolbarIcon;
    private String fullArticleURL;
    private String sArticleTitle;
    private String douURL = "http://dou.ua";
    private String articleIconURL;
    private Elements articleContent;
    private Element eArticleTitle;
    private TextView textContent;
    private ArticleContentThread articleContentThread = new ArticleContentThread();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_full_article_activity);


        fullArticleURL = douURL + getIntent().getExtras().getString("fullArticleURL");
        articleIconURL = getIntent().getExtras().getString("articleIconURL");
        System.out.println(fullArticleURL);
        toolbarIcon = (ImageView) findViewById(R.id.fullArticleImage);
        textContent = (TextView) findViewById(R.id.txtContent);
        toolbar = (Toolbar) findViewById(R.id.articleToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        articleContentThread.execute();

    }

    private class ArticleContentThread extends AsyncTask<Void, Void, Void> {

        Document HTMLPage;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HTMLPage = Jsoup.connect(fullArticleURL).get();
                eArticleTitle = HTMLPage.select("article.b-typo h1").first();
                sArticleTitle = eArticleTitle.text().replace("&nbsp;", " ");
                articleContent = HTMLPage.select("article.b-typo p");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Picasso.with(getApplicationContext())
                    .load(articleIconURL)
                    .into(toolbarIcon);
            collapsingToolbarLayout.setTitle(sArticleTitle);
            for (Element element : articleContent) {
                textContent.append(element.text().replace("&nbsp;", " ") + "\n");
            }
        }

    }

}

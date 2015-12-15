package com.example.root.douclient.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.douclient.R;
import com.example.root.douclient.objects.NewsArticlePageElements;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 14.12.15.
 */
public class FullArticleActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.layout_full_article_activity;
    private static final int LAYOUT_CONTENT_CONTAINER_ID = R.id.layoutContentContainer;
    private LinearLayout layoutContentContainer;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView toolbarIcon;
    private String fullArticleURL;
    private String sArticleTitle;
    private static final String HTTP_DOU_UA = "http://dou.ua";
    private String articleIconURL;
    private Elements articleContent;
    private Elements eArticleContentImage;
    private Elements articleHeading;
    private Elements articleCode;
    private Element eArticleTitle;
    private TextView textArticleContent;
    private ImageView imageContentArticle;
    private ArrayList<NewsArticlePageElements> contentElements = new ArrayList<>();
    private ArticleContentThread articleContentThread = new ArticleContentThread();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        layoutContentContainer = (LinearLayout) findViewById(LAYOUT_CONTENT_CONTAINER_ID);

        fullArticleURL = HTTP_DOU_UA + getIntent().getExtras().getString("fullArticleURL");
        articleIconURL = getIntent().getExtras().getString("articleIconURL");
        System.out.println(fullArticleURL);
        toolbarIcon = (ImageView) findViewById(R.id.fullArticleImage);
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
                if (articleContent != null)
                    for (Element element : articleContent) {
                        contentElements.add(new NewsArticlePageElements("CONTENT", element.text().replace("&nbsp;", " ")));
                    }
                eArticleContentImage = HTMLPage.select("article.b-typo p img");
                if (eArticleContentImage != null)
                    for (Element element : eArticleContentImage) {
                        String articleContentImageURL = element.attr("src");
                        contentElements.add(new NewsArticlePageElements("IMAGE", articleContentImageURL));
                    }
                articleHeading = HTMLPage.select("article.b-typo h2");
                if (articleHeading != null)
                    for (Element element : articleHeading) {
                        contentElements.add(new NewsArticlePageElements("CONTENT_HEADING", element.text().replace("&nbsp;", " ")));
                    }
                articleCode = HTMLPage.select("article.b-typo pre");
                if (articleCode != null)
                    for (Element element : articleCode) {
                        contentElements.add(new NewsArticlePageElements("CONTENT_CODE", element.text()));
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            LayoutInflater inflater = getLayoutInflater();
            Picasso.with(getApplicationContext())
                    .load(articleIconURL)
                    .into(toolbarIcon);
            collapsingToolbarLayout.setTitle(sArticleTitle);

            for (NewsArticlePageElements element : contentElements) {
                if (element.getElementType() == "CONTENT") {
                    textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
                    textArticleContent.setText(element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                } else if (element.getElementType() == "IMAGE") {
                    imageContentArticle = (ImageView) inflater.inflate(R.layout.image_article_content, null);
                    Picasso.with(getApplicationContext())
                            .load(element.getElementContent())
                            .into(imageContentArticle);
                    layoutContentContainer.addView(imageContentArticle);
                } else if (element.getElementType() == "CONTENT_HEADING") {
                    textArticleContent = (TextView) inflater.inflate(R.layout.text_heading_article_content, null);
                    textArticleContent.setText(element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                }

            }

//            for (Element element : articleContent) {
//
//                textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
//                textArticleContent.setText(element.text().replace("&nbsp;", " "));
//                layoutContentContainer.addView(textArticleContent);
//            }
        }

    }


}

package com.example.root.douclient.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
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
import java.util.regex.*;

/**
 * Created by bogdan on 14.12.15.
 */
public class FullArticleActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.layout_full_article_activity;
    private static final int LAYOUT_CONTENT_CONTAINER_ID = R.id.layoutContentContainer;
    private LinearLayout layoutContentContainer;
    private Toolbar toolbar;
    private String fullArticleURL;
    private String sArticleTitle;
    private String sArticlePageTags = "";
    private Elements eArticlePageTags;
    private static final String HTTP_DOU_UA = "http://dou.ua";
    private Elements articleContent;
    Pattern pattern = Pattern.compile("h\\d");
    Matcher matcher;

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
        initToolbar();
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
                eArticlePageTags = HTMLPage.select(".b-post-tags a");

                for (Element element : eArticlePageTags) {
                    sArticlePageTags += element.text() + ", ";
                    System.out.println(sArticlePageTags);
                }

                articleContent = HTMLPage.select("article.b-typo div");
                for (Element element : articleContent) {
                    for(Element elementsArticleContent : element.children()) {
                        matcher = pattern.matcher(elementsArticleContent.tagName());
                        if (elementsArticleContent.tagName().equals("p") && elementsArticleContent.hasText()) {
                            contentElements.add(new NewsArticlePageElements("CONTENT", elementsArticleContent.text().replace("&nbsp;", " ")));
                        } else if (elementsArticleContent.children() != null) {
                            for(Element content : elementsArticleContent.children()) {
                                if(content.hasAttr("src")) {
                                    String articleContentImageURL = content.attr("src");
                                    contentElements.add(new NewsArticlePageElements("IMAGE", articleContentImageURL));
                                }
                            }

                        } else if (matcher.matches()) {
                            contentElements.add(new NewsArticlePageElements("CONTENT_HEADING", elementsArticleContent.text().replace("&nbsp;", " ")));
                        } else if (elementsArticleContent.tagName().equals("pre")) {
                            contentElements.add(new NewsArticlePageElements("CONTENT_CODE", elementsArticleContent.text()));
                        }
                    }

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

            textArticleContent = (TextView) inflater.inflate(R.layout.title_article_page, null);
            textArticleContent.setText(sArticleTitle);
            layoutContentContainer.addView(textArticleContent);

            textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
            textArticleContent.setTextColor(R.color.darkGrey);
            textArticleContent.setText(sArticlePageTags);
            layoutContentContainer.addView(textArticleContent);

            for (NewsArticlePageElements element : contentElements) {
                if (element.getElementType().equals("CONTENT")) {
                    textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
                    textArticleContent.setText(element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                } else if (element.getElementType().equals("IMAGE")) {
                    imageContentArticle = (ImageView) inflater.inflate(R.layout.image_article_content, null);
                    Picasso.with(getApplicationContext())
                            .load(element.getElementContent())
                            .into(imageContentArticle);
                    layoutContentContainer.addView(imageContentArticle);
                } else if (element.getElementType().equals("CONTENT_HEADING")) {
                    textArticleContent = (TextView) inflater.inflate(R.layout.text_heading_article_content, null);
                    textArticleContent.setText(element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                }

            }

        }

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.articleToolbar);
        toolbar.setTitle("Статья");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}

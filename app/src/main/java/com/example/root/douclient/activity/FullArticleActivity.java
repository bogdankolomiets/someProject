package com.example.root.douclient.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.root.douclient.R;
import com.example.root.douclient.adapter.CommentsAdapter;
import com.example.root.douclient.adapter.CommentsAdapterTest;
import com.example.root.douclient.objects.CommentsItem;
import com.example.root.douclient.objects.NewsArticlePageElements;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Element eArticleAuthor;
    private Button btnShowComments;
    String sArticleAuthor;
    private ArrayList<CommentsItem> commentsItems = new ArrayList<>();
    private Element eArticleDateOfPublication;
    String sArticleDateOfPublication;
    private Elements eArticlePageTags;
    private static final String HTTP_DOU_UA = "http://dou.ua";
    private Elements articleContent;
    private Pattern pattern = Pattern.compile("h\\d");
    private Matcher matcher;
    private String commentsCount;
    private Element eArticleTitle;
    private TextView textArticleContent;
    private ImageView imageContentArticle;
    private HorizontalScrollView tableContentContainer;
    private TableLayout tableContent;
    private Element tableHead;
    private ListView comments;
    private Element tableBody;
    private List<List<String>> tableContentList;
    private List<String> tableRowContent = new ArrayList<>();
    private ArrayList<NewsArticlePageElements> contentElements = new ArrayList<>();
    private ArticleContentThread articleContentThread = new ArticleContentThread();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        layoutContentContainer = (LinearLayout) findViewById(LAYOUT_CONTENT_CONTAINER_ID);
        comments = (ListView) findViewById(R.id.commentsList);
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

                eArticleDateOfPublication = HTMLPage.select(".b-post-info .date").first();
                sArticleDateOfPublication = eArticleDateOfPublication.text();

                eArticleAuthor = HTMLPage.select(".b-post-info .author .name a").first();
                sArticleAuthor = eArticleAuthor.html();

                eArticleTitle = HTMLPage.select("article.b-typo h1").first();
                sArticleTitle = eArticleTitle.text().replace("&nbsp;", " ");
                eArticlePageTags = HTMLPage.select(".b-post-tags a");

                Element eCommentsCount = HTMLPage.getElementById("lblCommentsCount");
                commentsCount = eCommentsCount.text();

                for (Element element : eArticlePageTags) {
                    if(element.nextElementSibling()!= null) {
                        sArticlePageTags += element.text() + ", ";
                    } else
                        sArticlePageTags += element.text();

                }

                articleContent = HTMLPage.select("article.b-typo div");
                for (Element element : articleContent) {
                    for (Element elementsArticleContent : element.children()) {
                        matcher = pattern.matcher(elementsArticleContent.tagName());
                        if (elementsArticleContent.tagName().equals("p") && elementsArticleContent.hasText()) {
                            for (Element paragChildren : elementsArticleContent.children()) {
                                if (paragChildren.tagName().equals("img")) {
                                    String articleContentImageURL = elementsArticleContent.children().attr("src");
                                    contentElements.add(new NewsArticlePageElements("IMAGE", articleContentImageURL));
                                }
                            }
                            contentElements.add(new NewsArticlePageElements("CONTENT", elementsArticleContent.text().replace("&nbsp;", " ")));
                        } else if (elementsArticleContent.children().hasAttr("src")) {
                            String articleContentImageURL = elementsArticleContent.children().attr("src");
                            contentElements.add(new NewsArticlePageElements("IMAGE", articleContentImageURL));
                        } else if (matcher.matches()) {
                            contentElements.add(new NewsArticlePageElements("CONTENT_HEADING", elementsArticleContent.text().replace("&nbsp;", " ")));
                        } else if (elementsArticleContent.tagName().equals("pre")) {
                            contentElements.add(new NewsArticlePageElements("CONTENT_CODE", elementsArticleContent.text()));
                        } else if (elementsArticleContent.tagName().equals("table")) {
                            tableContentList = new ArrayList<>();
                            for (Element elementsTable : elementsArticleContent.children()) {
                                if (elementsTable.tagName().equals("thead")) {
                                    tableHead = elementsTable.children().first();
                                    for (Element tableHeadElements : tableHead.children()) {
                                        tableRowContent.add(tableHeadElements.text());
                                    }
                                    tableContentList.add(tableRowContent);
                                    tableRowContent = new ArrayList<>();
                                } else if (elementsTable.tagName().equals("tbody")) {
                                    for (Element tableBodyContentRow : elementsTable.children()) {
                                        for (Element tableBodyContentColumn : tableBodyContentRow.children()) {
                                            tableRowContent.add(tableBodyContentColumn.text());
                                        }
                                        tableContentList.add(tableRowContent);
                                        tableRowContent = new ArrayList<>();
                                    }
                                }
                            }
                            contentElements.add(new NewsArticlePageElements("CONTENT_TABLE", tableContentList));
                        } else if (elementsArticleContent.tagName().equals("ul")) {
                            for (Element listChildren : elementsArticleContent.children()) {
                                if (listChildren.tagName().equals("li")) {
                                    contentElements.add(new NewsArticlePageElements("CONTENT_LIST_ELEMENT", listChildren.text()));
                                }
                            }
                        } else if (elementsArticleContent.tagName().equals("blockquote")) {
                            contentElements.add(new NewsArticlePageElements("CONTENT_IN_BLOCK", elementsArticleContent.text()));
                        }
                    }
                }

                Element pageComments = HTMLPage.getElementById("commentsList");
                for (Element commentItem : pageComments.children()) {
                    Element eCommentAuthorAvatar = commentItem.select(".g-avatar").first();
                    String sCommentAuthorAvatar = "http://dou.ua/users/viktor-chyzhdzenka/";
                    Element commentAuthorName = commentItem.select(".avatar").first();
                    Element commentDateOfPublication = commentItem.select(".comment-link").first();
                    Element commentText = commentItem.select(".text.b-typo").first();
                    System.out.println(commentText.text());
                    commentsItems.add(new CommentsItem(sCommentAuthorAvatar, commentAuthorName.text(),
                            commentDateOfPublication.text(), commentText.text()));
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

            textArticleContent = (TextView) findViewById(R.id.dateOfPublication);
            textArticleContent.setText(sArticleDateOfPublication);

            textArticleContent = (TextView) findViewById(R.id.authorOfPublication);
            textArticleContent.setText(sArticleAuthor);

            textArticleContent = (TextView) inflater.inflate(R.layout.title_article_page, null);
            textArticleContent.setText(sArticleTitle);
            layoutContentContainer.addView(textArticleContent);

            textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
            textArticleContent.setTextColor(R.color.darkGrey);
            textArticleContent.setText(sArticlePageTags);
            textArticleContent.setGravity(0x11);
            layoutContentContainer.addView(textArticleContent);

            for (NewsArticlePageElements element : contentElements) {
                if (element.getElementType().equals("CONTENT")) {
                    textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
                    textArticleContent.setText(element.getElementContent());
                    Linkify.addLinks(textArticleContent, Linkify.ALL);
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
                } else if (element.getElementType().equals("CONTENT_CODE")) {
                    textArticleContent = (TextView) inflater.inflate(R.layout.code_layout, null);
                    textArticleContent.setText(element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                } else if (element.getElementType().equals("CONTENT_TABLE")) {
                    tableContentContainer = (HorizontalScrollView) inflater.inflate(R.layout.table_layout, null);
                    tableContent = new TableLayout(getApplicationContext());
                    for(List<String> tableListRow : element.getTableContent()) {
                        TableRow tableRow = new TableRow(getApplicationContext());

                        for(String tableElementText : tableListRow) {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setPadding(20,10,20,10);
                            textView.setTextColor(R.color.darkGrey);
                            textView.setText(tableElementText);
                            tableRow.addView(textView);
                        }

                        tableContent.addView(tableRow);
                    }
                    tableContentContainer.addView(tableContent);
                    layoutContentContainer.addView(tableContentContainer);
                } else if (element.getElementType().equals("CONTENT_LIST_ELEMENT")) {
                    textArticleContent = (TextView) inflater.inflate(R.layout.text_article_content, null);
                    textArticleContent.setText("- " + element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                } else if (element.getElementType().equals("CONTENT_IN_BLOCK")) {
                    textArticleContent = (TextView) inflater.inflate(R.layout.code_layout, null);
                    textArticleContent.setText(element.getElementContent());
                    layoutContentContainer.addView(textArticleContent);
                }

            }

            comments.setAdapter(new CommentsAdapterTest(getApplicationContext(), commentsItems));

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

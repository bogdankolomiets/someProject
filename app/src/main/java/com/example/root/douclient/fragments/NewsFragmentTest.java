package com.example.root.douclient.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.root.douclient.R;
import com.example.root.douclient.activity.FullArticleActivity;
import com.example.root.douclient.adapter.NewsPageAdapter;
import com.example.root.douclient.objects.NewsArticle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 08.12.15.
 */
public class NewsFragmentTest extends ListFragment implements AbsListView.OnScrollListener {

    private int pageNumber = 0;
    private int maxPageNumber = 149;
    private Elements article;
    private ArrayList<NewsArticle> newsContent = new ArrayList<>();
    private NewsThread newsThread = new NewsThread();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsThread.execute();
        getListView().setOnScrollListener(this);

    }

    public static NewsFragmentTest getInstance() {
        NewsFragmentTest newsFragmentTest = new NewsFragmentTest();
        Bundle args = new Bundle();
        newsFragmentTest.setArguments(args);

        return newsFragmentTest;
    }

    private class NewsThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Document HTMLPage;

            pageNumber = pageNumber + 1;
            if (pageNumber <= maxPageNumber) {
                try {

                    HTMLPage = Jsoup.connect("http://dou.ua/lenta/page/" + pageNumber).get();
                    article = HTMLPage.select(".b-lenta article");

                    for (Element element : article) {
                        Element eArticleImageURL = element.select("h2 a img").first();
                        String sArticleImageURL = eArticleImageURL.attr("src");
                        Element eArticleTitle = element.select("h2 a").first();
                        String sFullArticleURL = eArticleTitle.attr("href");
                        String sArticleTitle = eArticleTitle.html().replace("&nbsp;", " ");
                        Element eArticleText = element.select(".b-typo").first();
                        String sArticleText = eArticleText.text().replace("&nbsp;", " ");

                        newsContent.add(new NewsArticle(sArticleImageURL, sArticleTitle, sArticleText, sFullArticleURL));
                    }


                } catch (IOException e) {

                    e.printStackTrace();

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setListAdapter(new NewsPageAdapter(getActivity(), newsContent));
            ListView lv = getListView();
            ColorDrawable colorDrawable = new ColorDrawable(getActivity().getResources().getColor(R.drawable.dividerColor));
            lv.setDivider(colorDrawable);
            lv.setDividerHeight(1);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        boolean loadMore = (firstVisibleItem + visibleItemCount) == totalItemCount;

        if(loadMore && newsThread.getStatus() == AsyncTask.Status.FINISHED) {

            newsThread = new NewsThread();
            newsThread.execute();

        }

    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String fullArticleURL = newsContent.get(position).getFullNewsArticlePageURL();
        String articleIconURL = newsContent.get(position).getImageOfArticleURL();
        Intent intent = new Intent(getActivity(), FullArticleActivity.class);
        intent.putExtra("fullArticleURL", fullArticleURL);
        intent.putExtra("articleIconURL", articleIconURL);
        startActivity(intent);
    }
}

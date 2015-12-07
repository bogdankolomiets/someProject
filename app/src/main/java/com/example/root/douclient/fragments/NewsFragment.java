package com.example.root.douclient.fragments;

import android.os.AsyncTask;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.example.root.douclient.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class NewsFragment extends ListFragment implements AbsListView.OnScrollListener {
    private int pageNumber = 0;
    private int maxPageNumber = 149;
    private Elements content;
    private ArrayList<String> newsContent = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private NewsThread newsThread = new NewsThread();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, newsContent);
        getListView().setOnScrollListener(this);

        newsThread.execute();

    }

    public static NewsFragment getInstance() {
        NewsFragment newsFragment = new NewsFragment();
        Bundle args = new Bundle();
        newsFragment.setArguments(args);

        return newsFragment;
    }

    private class NewsThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Document HTMLPage;
            pageNumber = pageNumber + 1;
                if (pageNumber <= maxPageNumber) {
                    try {

                        HTMLPage = Jsoup.connect("http://dou.ua/lenta/page/" + pageNumber).get();
                        content = HTMLPage.select(".b-lenta article h2 a");

                        for (Element contents : content) {
                            newsContent.add(contents.text());
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
            setListAdapter(adapter);
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



}

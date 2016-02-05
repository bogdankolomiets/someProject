package com.example.root.douclient.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.example.root.douclient.objects.CommentsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bogdan on 05.02.2016.
 */
public class CommentsFragment extends ListFragment {
    private ArrayList<CommentsItem> commentsItems = new ArrayList<>();
    private Document HTMLPage;
    private static String PageURL = "";
    private Element commentBlock;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private class CommentsThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                connectToPage();
                commentBlock = getCommentBlock();
                for (Element commentItem : commentBlock.children()) {
                    String authorIconURL = getAuthorIconURL();
                    String authorName = getAuthorName(commentItem);
                    String dateOfPublication = getDateOfPublication(commentItem);
                    String content = getContent(commentItem);
                    insertCommentItem(authorIconURL, authorName, dateOfPublication, content);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void connectToPage() throws IOException {
        HTMLPage = Jsoup.connect(PageURL).get();
    }

    private Element getCommentBlock() {
        return HTMLPage.getElementById("commentsList");
    }

    private String getAuthorIconURL() {
        return "http://s.developers.org.ua/img/avatars/40x40_121535.jpg";
    }

    private String getAuthorName(Element commentItem) {
        return commentItem.select(".avatar").first().text();
    }

    private String getDateOfPublication(Element commentItem) {
        return commentItem.select("comment-link").first().text();
    }

    private String getContent(Element commentItem) {
        return commentItem.select(".text.b-typo").first().text();
    }

    private void insertCommentItem(String authorIconURL, String authorName, String dateofPublicaton, String content) {
        commentsItems.add(new CommentsItem(authorIconURL, authorName, dateofPublicaton, content));
    }

}

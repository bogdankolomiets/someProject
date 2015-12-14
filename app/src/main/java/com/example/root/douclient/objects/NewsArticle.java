package com.example.root.douclient.objects;

/**
 * Created by root on 08.12.15.
 */
public class NewsArticle {

    private String imageOfArticleURL;
    private String newsArticleTitle;
    private String newsArticleText;

    public NewsArticle(String imageOfArticleURL, String newsArticleTitle, String newsArticleText) {
        this.imageOfArticleURL = imageOfArticleURL;
        this.newsArticleTitle = newsArticleTitle;
        this.newsArticleText = newsArticleText;
    }

    public String getImageOfArticleURL() {
        return imageOfArticleURL;
    }

    public String getNewsArticleTitle() {
        return newsArticleTitle;
    }

    public String getNewsArticleText() {
        return newsArticleText;
    }

    public void setImageOfArticleURL(String imageOfArticleURL) {
        this.imageOfArticleURL = imageOfArticleURL;
    }

    public void setNewsArticleTitle(String newsArticleTitle) {
        this.newsArticleTitle = newsArticleTitle;
    }

    public void setNewsArticleText(String newsArticleText) {
        this.newsArticleText = newsArticleText;
    }

}

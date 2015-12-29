package com.example.root.douclient.objects;

/**
 * Created by root on 28.12.15.
 */
public class CommentsItem {

    private String imageOfCommentAuthorURL;
    private String nameOfCommentAuthor;
    private String dateOfCommentPublication;
    private String textOfComment;
    private String countOfCommentAnswer;

    public CommentsItem(String imageOfCommentAuthorURL, String nameOfCommentAuthor,
                        String dateOfCommentPublication, String textOfComment) {

        this.imageOfCommentAuthorURL = imageOfCommentAuthorURL;
        this.nameOfCommentAuthor = nameOfCommentAuthor;
        this.dateOfCommentPublication = dateOfCommentPublication;
        this.textOfComment = textOfComment;

    }

    public String getImageOfCommentAuthorURL() {
        return imageOfCommentAuthorURL;
    }

    public String getNameOfCommentAuthor() {
        return nameOfCommentAuthor;
    }

    public String getDateOfCommentPublication() {
        return dateOfCommentPublication;
    }

    public String getTextOfComment() {
        return textOfComment;
    }

    public String getCountOfCommentAnswer() {
        return countOfCommentAnswer;
    }
}

package com.example.root.douclient.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.root.douclient.R;
import com.example.root.douclient.objects.CommentsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bogdan on 13.01.2016.
 */
public class CommentsListViewAdapter extends ArrayAdapter {

    private ArrayList<CommentsItem> data;
    private Context context;

    public CommentsListViewAdapter(Context context, ArrayList<CommentsItem> data) {
        super(context, R.layout.comments_item_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItemsHolder commentItemsHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comments_item_row, parent, false);

            commentItemsHolder = new CommentItemsHolder();
            commentItemsHolder.commentAuthorIconURL = (CircleImageView) convertView.findViewById(R.id.imgCommentsAuthor);
            commentItemsHolder.commentAuthorName = (TextView) convertView.findViewById(R.id.txtCommentsAuthor);
            commentItemsHolder.commentDateOfPublication = (TextView) convertView.findViewById(R.id.txtCommentsDate);
            commentItemsHolder.commentContent = (TextView) convertView.findViewById(R.id.txtComments);
            convertView.setTag(commentItemsHolder);
        } else commentItemsHolder = (CommentItemsHolder) convertView.getTag();

        CommentsItem commentsItem = data.get(position);
        Picasso.with(context)
                .load(commentsItem.getImageOfCommentAuthorURL())
                .into(commentItemsHolder.commentAuthorIconURL);
        commentItemsHolder.commentAuthorName.setText(commentsItem.getNameOfCommentAuthor());
        commentItemsHolder.commentDateOfPublication.setText(commentsItem.getDateOfCommentPublication());
        commentItemsHolder.commentContent.setText(commentsItem.getTextOfComment());

        return convertView;
    }

    private static class CommentItemsHolder {
        CircleImageView commentAuthorIconURL;
        TextView commentAuthorName;
        TextView commentDateOfPublication;
        TextView commentContent;
    }
}

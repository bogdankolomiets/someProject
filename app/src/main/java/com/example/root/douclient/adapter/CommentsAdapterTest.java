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
 * Created by root on 26.01.16.
 */
public class CommentsAdapterTest extends ArrayAdapter<CommentsItem> {

    private Context context;
    private ArrayList<CommentsItem> commentsItems;

    public CommentsAdapterTest(Context context, ArrayList<CommentsItem> commentsItems) {
        super(context, R.layout.comments_item_row, commentsItems);
        this.context = context;
        this.commentsItems = commentsItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentsHolder commentsHolder;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.comments_item_row, parent, false);

            commentsHolder = new CommentsHolder();
            commentsHolder.imgCommentsAuthor = (CircleImageView) convertView.findViewById(R.id.imgCommentsAuthor);
            commentsHolder.txtCommentsAuthorName = (TextView) convertView.findViewById(R.id.txtCommentsAuthor);
            commentsHolder.txtCommentsDate = (TextView) convertView.findViewById(R.id.txtCommentsDate);
            commentsHolder.txtCommentsData = (TextView) convertView.findViewById(R.id.txtComments);

            convertView.setTag(commentsHolder);
        } else commentsHolder = (CommentsHolder) convertView.getTag();

        CommentsItem commentsItem = commentsItems.get(position);
        Picasso.with(context)
                .load(commentsItem.getImageOfCommentAuthorURL())
                .into(commentsHolder.imgCommentsAuthor);
        commentsHolder.txtCommentsAuthorName.setText(commentsItem.getNameOfCommentAuthor());
        commentsHolder.txtCommentsDate.setText(commentsItem.getDateOfCommentPublication());
        commentsHolder.txtCommentsData.setText(commentsItem.getTextOfComment());

        return convertView;
    }

    private static class CommentsHolder {
        CircleImageView imgCommentsAuthor;
        TextView txtCommentsAuthorName;
        TextView txtCommentsDate;
        TextView txtCommentsData;
    }
}

package com.example.root.douclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.root.douclient.R;
import com.example.root.douclient.objects.CommentsItem;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 28.12.15.
 */
public class CommentsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CommentsItem> dataComments;
    private HashMap<CommentsItem, ArrayList<CommentsItem>> dataCommentsAnswer;

    public CommentsAdapter(Context context, ArrayList<CommentsItem> dataComments, HashMap<CommentsItem, ArrayList<CommentsItem>> dataCommentsAnswer) {
        this.context = context;
        this.dataComments = dataComments;
        this.dataCommentsAnswer = dataCommentsAnswer;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        CommentsHolder commentsHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.comments_item_row, null);

            commentsHolder = new CommentsHolder();
            commentsHolder.imgCommentsAuthor = (CircleImageView) convertView.findViewById(R.id.imgCommentsAuthor);
            commentsHolder.txtCommentsAuthorName = (TextView) convertView.findViewById(R.id.txtCommentsAuthor);
            commentsHolder.txtCommentsDate = (TextView) convertView.findViewById(R.id.txtCommentsDate);
            commentsHolder.txtCommentsData = (TextView) convertView.findViewById(R.id.txtComments);

            convertView.setTag(commentsHolder);
        } else commentsHolder = (CommentsHolder) convertView.getTag();

        CommentsItem commentsItem = dataComments.get(groupPosition);
        Picasso.with(context)
                .load(commentsItem.getImageOfCommentAuthorURL())
                .into(commentsHolder.imgCommentsAuthor);
        commentsHolder.txtCommentsAuthorName.setText(commentsItem.getNameOfCommentAuthor());
        commentsHolder.txtCommentsDate.setText(commentsItem.getDateOfCommentPublication());
        commentsHolder.txtCommentsData.setText(commentsItem.getTextOfComment());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CommentsHolder commentsHolder;

        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.comments_item_row, null);

            commentsHolder = new CommentsHolder();
            commentsHolder.imgCommentsAuthor = (CircleImageView) convertView.findViewById(R.id.imgCommentsAuthor);
            commentsHolder.txtCommentsAuthorName = (TextView) convertView.findViewById(R.id.txtCommentsAuthor);
            commentsHolder.txtCommentsDate = (TextView) convertView.findViewById(R.id.txtCommentsDate);
            commentsHolder.txtCommentsData = (TextView) convertView.findViewById(R.id.txtComments);

            convertView.setTag(commentsHolder);
        }else commentsHolder = (CommentsHolder) convertView.getTag();

        CommentsItem commentsItem = dataCommentsAnswer.get(groupPosition).get(childPosition);
        Picasso.with(context)
                .load(commentsItem.getImageOfCommentAuthorURL())
                .into(commentsHolder.imgCommentsAuthor);
        commentsHolder.txtCommentsAuthorName.setText(commentsItem.getNameOfCommentAuthor());
        commentsHolder.txtCommentsDate.setText(commentsItem.getDateOfCommentPublication());
        commentsHolder.txtCommentsData.setText(commentsItem.getTextOfComment());

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dataCommentsAnswer.get(this.dataComments.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.dataCommentsAnswer.get(this.dataComments.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataComments.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.dataComments.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class CommentsHolder {
        CircleImageView imgCommentsAuthor;
        TextView txtCommentsAuthorName;
        TextView txtCommentsDate;
        TextView txtCommentsData;
    }
}

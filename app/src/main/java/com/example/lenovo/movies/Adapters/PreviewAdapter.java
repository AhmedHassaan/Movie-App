package com.example.lenovo.movies.Adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.lenovo.movies.Details.reviews;
import com.example.lenovo.movies.R;

import java.util.List;

/**
 * Created by Lenovo on 11/21/2016.
 */

public class PreviewAdapter extends BaseExpandableListAdapter {

    List<reviews> review;

    public PreviewAdapter(List<reviews> review) {
        this.review = review;
    }

    @Override
    public int getGroupCount() {
        return review.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return review.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return review.get(i).getContent();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        reviews rev = review.get(i);
        if(view==null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_single_item_author,viewGroup,false);

        TextView showAuthor = (TextView)view.findViewById(R.id.author);
        showAuthor.setTypeface(null, Typeface.BOLD);
        showAuthor.setText(rev.getAuthor());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        reviews rev = review.get(i);
        if(view==null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_single_item_content,viewGroup,false);
        TextView showContent = (TextView)view.findViewById(R.id.content);
        showContent.setText(rev.getContent());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

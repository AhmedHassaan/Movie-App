package com.example.lenovo.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 11/25/2016.
 */

public class ViewAdapter extends ArrayAdapter<Movies> {
    Context context;
    int resource;
    LayoutInflater vi;
    ArrayList<Movies> movies;

    public ViewAdapter(Context context, int resource, ArrayList<Movies> movies) {
        super(context, resource, movies);
        this.context = context;
        this.resource = resource;
        this.movies = movies;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if(convertView==null){
            convertView = vi.inflate(resource,null);
            holder = new ViewHolder();
            holder.imageview = (ImageView)convertView.findViewById(R.id.movie_image_grid);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();
        Picasso.with(context).load(movies.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageview);

        return convertView;
    }

    //Things in list
    static class ViewHolder {
        public ImageView imageview;
    }
}

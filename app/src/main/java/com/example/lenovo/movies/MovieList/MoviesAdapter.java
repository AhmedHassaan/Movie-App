package com.example.lenovo.movies.MovieList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 10/21/2016.
 */

public class MoviesAdapter extends ArrayAdapter<Movies> {
    Context context;
    int resource;
    LayoutInflater vi;
    ArrayList<Movies> movies;

    public MoviesAdapter(Context context, int resource, ArrayList<Movies> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        movies = objects;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            convertView = vi.inflate(resource,null);
            holder = new ViewHolder();
            holder.imageview = (ImageView)convertView.findViewById(R.id.movieImage);
            holder.name = (TextView)convertView.findViewById(R.id.movieName);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();
        holder.name.setText(movies.get(position).getName());
        Picasso.with(context).load(movies.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageview);

        return convertView;
    }


    //Things in list
    static class ViewHolder {
        public ImageView imageview;
        public TextView name;
    }


}

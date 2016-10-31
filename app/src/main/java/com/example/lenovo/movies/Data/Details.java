package com.example.lenovo.movies.Data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Lenovo on 10/31/2016.
 */

public class Details extends Fragment {

    Movies movie;
    ImageView movieImage;
    TextView title,rate,date,desc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details,container,false);

        movieImage = (ImageView)root.findViewById(R.id.detailImage);
        title = (TextView)root.findViewById(R.id.detail_title);
        rate = (TextView)root.findViewById(R.id.detail_rate);
        date = (TextView)root.findViewById(R.id.detail_date);
        desc = (TextView)root.findViewById(R.id.detail_desc);

        return root;
    }

    public void setNew(Movies movie){
        this.movie=movie;
        update();

    }

    private void update(){
        title.setText("Title : " + movie.getName());
        desc.setText("Overview : " + movie.getOverview());
        rate.setText("Rate : " + movie.getRate());
        date.setText("Date : " + movie.getDate());
        Picasso.with(getActivity()).load(movie.getBackdrop()).placeholder(R.mipmap.ic_launcher).into(movieImage);
    }
}

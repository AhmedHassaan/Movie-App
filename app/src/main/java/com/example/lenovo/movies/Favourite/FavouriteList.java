package com.example.lenovo.movies.Favourite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.lenovo.movies.Adapters.ViewAdapter;
import com.example.lenovo.movies.Data.ControlRealm;
import com.example.lenovo.movies.Data.FavouriteConnection;
import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 11/22/2016.
 */

public class FavouriteList extends Fragment implements AdapterView.OnItemClickListener {


    FavouriteConnection connection;
    GridView list;
    ArrayList<Movies> moviesList;
    ViewAdapter adapter;
    ControlRealm controlRealm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gridview_fragment,container,false);
        list = (GridView)root.findViewById(R.id.movie_gridview);
        moviesList = new ArrayList<>();
        controlRealm = new ControlRealm(getActivity());
        connection = (FavouriteConnection) getActivity();
        list.setOnItemClickListener(this);
        update();
        return root;
    }

    public void update(){
        moviesList = controlRealm.getAllMovies();
        if(getActivity() != null) {
            adapter = new ViewAdapter(getActivity(), R.layout.griditem, moviesList);
        }
        list.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        connection.setFav(moviesList.get(i));
    }
}

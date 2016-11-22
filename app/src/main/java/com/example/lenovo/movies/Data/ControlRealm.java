package com.example.lenovo.movies.Data;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Lenovo on 11/22/2016.
 */

public class ControlRealm {
    Realm realm;
    Movies movie;


    public ControlRealm(Movies movie, Context context) {
        this.movie = movie;
        realm = Realm.getInstance(new RealmConfiguration.Builder(context)
        .name("Movie.Realm").build());
    }

    public ControlRealm(Context context){
        realm = Realm.getInstance(new RealmConfiguration.Builder(context)
                .name("Movie.Realm").build());
    }

    public Movies getMovie() {
        return movie;
    }

    public void putMovie(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FavouriteMovie favMovie = realm.createObject(FavouriteMovie.class);
                favMovie.setBackdrop(movie.getBackdrop());
                favMovie.setDate(movie.getDate());
                favMovie.setDescription(movie.getOverview());
                favMovie.setId(movie.getId());
                favMovie.setImage(movie.getImage());
                favMovie.setName(movie.getName());
                favMovie.setRate(movie.getRate());
            }

        });
    }

    public void removeMovie(final String id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<FavouriteMovie> row = realm.where(FavouriteMovie.class)
                        .equalTo("id",id).findAll();
                row.deleteAllFromRealm();
            }
        });
    }


    public ArrayList<Movies> getAllMovies(){
        ArrayList<Movies> all = new ArrayList<>();
        RealmResults<FavouriteMovie> result = realm.where(FavouriteMovie.class).findAll();
        for(FavouriteMovie f : result){
            Movies m = new Movies(f.getImage(),f.getName(),f.getDate(),f.getDescription(),f.getBackdrop(),f.getRate()
            ,f.getId());
            all.add(m);
        }
        return all;
    }
}

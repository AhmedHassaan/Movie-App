package com.example.lenovo.movies.Data;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lenovo on 11/22/2016.
 */

public class FavouriteMovie extends RealmObject{

    @PrimaryKey
    private String id;

    private String name;
    private String description;
    private int rate;
    private String image;
    private String date;
    private String backdrop;

    @Ignore
    private String preview;

    @Ignore
    private String trailer;

    public FavouriteMovie(String id, String name, String description, int rate, String image, String date, String backdrop) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rate = rate;
        this.image = image;
        this.date = date;
        this.backdrop = backdrop;
    }

    public FavouriteMovie(){};

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRate() {
        return rate;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }
}

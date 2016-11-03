package com.example.lenovo.movies.Data;

/**
 * Created by Lenovo on 10/21/2016.
 */

public class Movies {

    private String image,name,date,overview,backdrop,id,videoURL;
    private int rate;

    public Movies(String image, String name, String date, String overview, String backdrop, int rate, String id) {
        this.image = image;
        this.name = name;
        this.date = date;
        this.overview = overview;
        this.backdrop = backdrop;
        this.rate = rate;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public int getRate() {
        return rate;
    }

    public String getId() {
        return id;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}

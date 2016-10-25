package com.example.lenovo.movies.Data;

/**
 * Created by Lenovo on 10/21/2016.
 */

public class Movies {

    private String image,name,date,rate,overview,backdrop,language;

    public Movies(String image, String name) {
        this.image = image;
        this.name = name;
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

    public String getRate() {
        return rate;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getLanguage() {
        return language;
    }
}

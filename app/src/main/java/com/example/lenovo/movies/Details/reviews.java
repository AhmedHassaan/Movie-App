package com.example.lenovo.movies.Details;

/**
 * Created by Lenovo on 11/21/2016.
 */

public class reviews {
    private String author ,content;

    public reviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

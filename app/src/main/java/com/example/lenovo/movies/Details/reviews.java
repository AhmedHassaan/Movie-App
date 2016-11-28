package com.example.lenovo.movies.Details;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 11/21/2016.
 */

public class reviews implements Parcelable {
    private String author ,content;

    protected reviews(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<reviews> CREATOR = new Creator<reviews>() {
        @Override
        public reviews createFromParcel(Parcel in) {
            return new reviews(in);
        }

        @Override
        public reviews[] newArray(int size) {
            return new reviews[size];
        }
    };

    public reviews(String author, String content) {
        this.author=author;
        this.content=content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
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

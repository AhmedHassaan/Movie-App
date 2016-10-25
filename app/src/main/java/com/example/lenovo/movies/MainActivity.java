package com.example.lenovo.movies;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.lenovo.movies.MovieList.MainList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    // Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder, new MainList());
    // or ft.add(R.id.your_placeholder, new FooFragment());
    // Complete the changes added above
        ft.commit();

    }
}

package com.example.lenovo.movies;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.lenovo.movies.Data.Details;
import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.Data.connection;
import com.example.lenovo.movies.MovieList.MainList;

public class MainActivity extends AppCompatActivity implements connection {
    Details details;
    FrameLayout detailLayout;
    boolean first = false;
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailLayout = (FrameLayout)findViewById(R.id.movie_detail);
        detailLayout.setY(2000);
        details = new Details();
    // Replace the contents of the container with the new fragment
        ft.replace(R.id.movie_list, new MainList());
    // or ft.add(R.id.your_placeholder, new FooFragment());
    // Complete the changes added above
        ft.commit();

    }

    private void show(){
        if(!first)
        {
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.movie_detail,new Details());
            ft2.commit();
            first = true;
        }
    }

    @Override
    public void set(Movies movies) {
        show();
        details.setNew(movies);
    }

    @Override
    public void onBackPressed() {
        first = false;
        ft.hide(new Details());
        ft.commit();
    }
}

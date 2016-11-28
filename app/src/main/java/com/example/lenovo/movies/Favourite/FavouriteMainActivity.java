package com.example.lenovo.movies.Favourite;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lenovo.movies.Data.FavouriteConnection;
import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.Details.Details;
import com.example.lenovo.movies.R;

public class FavouriteMainActivity extends AppCompatActivity implements FavouriteConnection {
    Details details;
    FrameLayout detailLayout;
    Movies movie;
    FavouriteList favList;
    boolean up = false;
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction(),
            ft2 = getSupportFragmentManager().beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_main);
        detailLayout = (FrameLayout)findViewById(R.id.favourite_movie_detail_layout);
        if(savedInstanceState!=null) {
            up = savedInstanceState.getBoolean("first");
        }
        if(!up)
            detailLayout.setY(2000);
        details = new Details();
        favList = new FavouriteList();
        ft.replace(R.id.favourite_movie_list_layout, favList);
        ft.commit();
        ft2.replace(R.id.favourite_movie_detail_layout,details);
        ft2.commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey("name")) {
                movie = new Movies(savedInstanceState.getString("image"),
                        savedInstanceState.getString("name"),
                        savedInstanceState.getString("date"),
                        savedInstanceState.getString("overview"),
                        savedInstanceState.getString("back"),
                        savedInstanceState.getInt("rate"),
                        savedInstanceState.getString("id"));
                setSave();
            }
        }
        details.setInFavourite(true);
    }


    @Override
    public void setFav(Movies movies) {
        this.movie=movies;
        show();
        details.setNew(movies,false);
    }

    private void show(){
        if(!up)
        {
            detailLayout.animate().yBy(-2000).setDuration(200).start();
            up = true;
        }
    }

    public void setSave(){
        Toast.makeText(getApplicationContext(),"Save",Toast.LENGTH_SHORT).show();
        details.setNew(movie,true);
    }

    @Override
    public void onBackPressed() {
        if(!up){
            NavUtils.navigateUpFromSameTask(this);
        }
        up = false;
        detailLayout.animate().yBy(2000).setDuration(200).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("first",up);
        if(up) {
            outState.putString("name", movie.getName());
            outState.putString("overview", movie.getOverview());
            outState.putString("id", movie.getId());
            outState.putString("back", movie.getBackdrop());
            outState.putString("date", movie.getDate());
            outState.putString("image", movie.getImage());
            outState.putInt("rate", movie.getRate());
        }
    }
}

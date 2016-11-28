package com.example.lenovo.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lenovo.movies.Details.Details;
import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.Data.MainConnction;
import com.example.lenovo.movies.Favourite.FavouriteMainActivity;
import com.example.lenovo.movies.MovieList.MainList;

public class MainActivity extends AppCompatActivity implements MainConnction {
    Details details;
    MainList mainList;
    FrameLayout detailLayout;
    Movies movie;
    boolean up = false;
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction(),
            ft2 = getSupportFragmentManager().beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailLayout = (FrameLayout)findViewById(R.id.movie_detail);
        if(savedInstanceState!=null) {
            up = savedInstanceState.getBoolean("first");
        }
        if(!up)
            detailLayout.setY(2000);
        details = new Details();
        mainList = new MainList();
    // Replace the contents of the container with the new fragment
        ft.replace(R.id.movie_list, mainList);
        ft2.replace(R.id.movie_detail,details);
        ft2.commit();
    // or ft.add(R.id.your_placeholder, new FooFragment());
    // Complete the changes added above
        ft.commit();
        //setHasOptionsMenu(true);
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
        details.setInFavourite(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_refresh){
            mainList.update();
            return true;
        }

        else if(id==R.id.action_settings){
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.action_favourite){
            Intent intent = new Intent(MainActivity.this, FavouriteMainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void show(){
        if(!up)
        {
            detailLayout.animate().yBy(-2000).setDuration(200).start();
            up = true;
        }
    }

    @Override
    public void set(Movies movies) {
        this.movie=movies;
        show();
        details.setNew(movies,false);
    }

    public void setSave(){
        Toast.makeText(getApplicationContext(),"Save",Toast.LENGTH_SHORT).show();
        details.setNew(movie,true);
    }

    @Override
    public void onBackPressed() {
        if(!up){
            System.exit(1);
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

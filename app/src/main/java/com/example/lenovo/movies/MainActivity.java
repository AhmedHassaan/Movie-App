package com.example.lenovo.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.lenovo.movies.Data.Details;
import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.Data.Setting;
import com.example.lenovo.movies.Data.connection;
import com.example.lenovo.movies.MovieList.MainList;

public class MainActivity extends AppCompatActivity implements connection {
    Details details;
    MainList mainList;
    FrameLayout detailLayout;
    boolean first = false;
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction(),
            ft2 = getSupportFragmentManager().beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailLayout = (FrameLayout)findViewById(R.id.movie_detail);
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
        return super.onOptionsItemSelected(item);
    }

    private void show(){
        if(!first)
        {
            detailLayout.animate().yBy(-2000).setDuration(200).start();
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
        if(!first){
            finish();
        }
        first = false;
        detailLayout.animate().yBy(2000).setDuration(200).start();
    }
}

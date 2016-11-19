package com.example.lenovo.movies.MovieList;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.Data.OfflineData;
import com.example.lenovo.movies.Data.connection;
import com.example.lenovo.movies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Lenovo on 10/21/2016.
 */

public class MainList extends Fragment implements AdapterView.OnItemClickListener {
    ListView list;
    ArrayList<Movies> moviesList;
    private ProgressDialog dialog;
    connection con;
    MoviesAdapter adapter;
    OfflineData save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mainlist_fragment,container,false);
        list = (ListView)root.findViewById(R.id.movieList);
        moviesList = new ArrayList<>();
        save = new OfflineData(getActivity());
        update();
        con = (connection) getActivity();
        list.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        con.set(moviesList.get(i));
    }

    public void update(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String view = prefs.getString(getString(R.string.key_view),getString(R.string.view_default_val));
        new MoviesAsyncTask().execute(view);
    }


    public void sortMovies(){
        if (getActivity() != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = prefs.getString(getString(R.string.key_sort), getString(R.string.sort_default_val));
            if (sort.equals("rate"))
                Collections.sort(moviesList, Movies.rateComparator);
            else
                Collections.sort(moviesList, Movies.nameComparator);
        }
    }


    public class MoviesAsyncTask extends AsyncTask<String , Void , Boolean>{


        String appKey = "4a09ef88946390c1359f633a7987bf5f";
        String en = "en-US";
        String movieJson;

        private final String LOG_TAG = MoviesAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            moviesList.clear();
            dialog = ProgressDialog.show(getActivity(), "",
                    "Loading..", true);
        }



        private boolean getData(String moviesStr) throws JSONException{
            if(moviesStr == null)
                return false;
            final String title = "title";
            final String image = "poster_path";
            final String result = "results";
            final String backDrop = "backdrop_path";
            final String desc = "overview";
            final String date = "release_date";
            final String rate = "vote_average";
            final String id = "id";
            String baseImageNormal = "https://image.tmdb.org/t/p/w300";
            String baseImageBig = "https://image.tmdb.org/t/p/w300";

            JSONObject movieJson = new JSONObject(moviesStr);
            JSONArray moviesArray = movieJson.getJSONArray(result);

            for(int i=0;i<moviesArray.length();i++){
                JSONObject oneMovie = moviesArray.getJSONObject(i);
                String oneTitle = oneMovie.getString(title);
                String oneImage = baseImageNormal + oneMovie.getString(image);
                String oneDesc = oneMovie.getString(desc);
                String oneDate = oneMovie.getString(date);
                String back = baseImageBig + oneMovie.getString(backDrop);
                int oneRate = oneMovie.getInt(rate);
                int oneId = oneMovie.getInt(id);
                Movies m = new Movies(oneImage,oneTitle,oneDate,oneDesc,back,oneRate, Integer.toString(oneId));
                moviesList.add(m);
//                Log.v(LOG_TAG, "Movie Data : Name : " + moviesList.get(i).getName() + "    Image : " + moviesList.get(i).getImage());
            }
            return true;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Uri builtMovie;
            String baseURL = "http://api.themoviedb.org/3/movie/" + strings[0] + "?";
            final String api_key = "api_key";
            final String lang = "language";
            builtMovie = Uri.parse(baseURL).buildUpon()
                    .appendQueryParameter(lang,en)
                    .appendQueryParameter(api_key, appKey).build();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(builtMovie.toString());
                Log.v(LOG_TAG, "built uri " + builtMovie.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return false;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return false;
                }
                movieJson = buffer.toString();
                Log.v(LOG_TAG, "Movie JSON String: " + movieJson);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                return getData(movieJson);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
//                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if(aBoolean){
                save.setJson(movieJson);
                sortMovies();
                if(getActivity() != null)
                    adapter = new MoviesAdapter(getActivity(),R.layout.listitem,moviesList);
                list.setAdapter(adapter);
            }
            else{
                if(getActivity() != null)
                    Toast.makeText(getActivity(),"No Internet",Toast.LENGTH_LONG).show();
                boolean b =true;
                try {
                    b = getData(save.getJson());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(b){
                    sortMovies();
                    if(getActivity() != null)
                        adapter = new MoviesAdapter(getActivity(),R.layout.listitem,moviesList);
                    list.setAdapter(adapter);
                }
                else
                    Toast.makeText(getActivity(),"No Data",Toast.LENGTH_LONG).show();
            }
        }
    }





}

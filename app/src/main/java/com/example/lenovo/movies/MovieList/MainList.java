package com.example.lenovo.movies.MovieList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.movies.Data.Movies;
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

/**
 * Created by Lenovo on 10/21/2016.
 */

public class MainList extends Fragment {
    ListView list;
    ArrayList<Movies> moviesList;
    Context context = getActivity();
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mainlist_fragment,container,false);
        list = (ListView)root.findViewById(R.id.movieList);
        moviesList = new ArrayList<>();
        new MoviesAsyncTask().execute();


        return root;
    }


    public class MoviesAsyncTask extends AsyncTask<Void , Void , Boolean>{

        String appKey = "4a09ef88946390c1359f633a7987bf5f";
        String movieJson;

        private final String LOG_TAG = MoviesAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        private boolean getData(String moviesStr) throws JSONException{
            if(moviesStr == null)
                return false;
            final String title = "title";
            final String image = "poster_path";
            final String result = "results";
            String baseImage = "https://image.tmdb.org/t/p/w600";

            JSONObject movieJson = new JSONObject(moviesStr);
            JSONArray moviesArray = movieJson.getJSONArray(result);

            for(int i=0;i<moviesArray.length();i++){
                JSONObject oneMovie = moviesArray.getJSONObject(i);
                String oneTitle = oneMovie.getString(title);
                String oneImage = baseImage + oneMovie.getString(image);
                Movies m = new Movies(oneImage,oneTitle);
                moviesList.add(m);
                Log.v(LOG_TAG, "Movie Data : Name : " + moviesList.get(i).getName() + "    Image : " + moviesList.get(i).getImage());
            }
            return true;
        }

        @Override
        protected Boolean doInBackground(Void... strings) {

            final String baseURL = "http://api.themoviedb.org/3/discover/movie?";
            final String api_key = "api_key";

            Uri built = Uri.parse(baseURL).buildUpon()
                    .appendQueryParameter(api_key,appKey).build();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(built.toString());
                Log.v(LOG_TAG, "built uri " + built.toString());

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
                Log.v(LOG_TAG, "Forecast JSON String: " + movieJson);
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
            if(aBoolean){
                MoviesAdapter adapter = new MoviesAdapter(getActivity(),R.layout.listitem,moviesList);
                list.setAdapter(adapter);
            }
            else
                Toast.makeText(getActivity(),"No Internet",Toast.LENGTH_LONG).show();
        }
    }


}

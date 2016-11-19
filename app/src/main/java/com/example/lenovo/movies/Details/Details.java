package com.example.lenovo.movies.Details;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.R;
import com.squareup.picasso.Picasso;

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

/**
 * Created by Lenovo on 10/31/2016.
 */

public class Details extends Fragment {

    Movies movie;
    ImageView movieImage;
    TextView title,rate,date,desc;
    boolean f = false;
    ListView trailer;
    ArrayAdapter<String> s;
    String a[] = {"No Internet Connection"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details,container,false);

        movieImage = (ImageView)root.findViewById(R.id.detailImage);
        title = (TextView)root.findViewById(R.id.detail_title);
        rate = (TextView)root.findViewById(R.id.detail_rate);
        date = (TextView)root.findViewById(R.id.detail_date);
        desc = (TextView)root.findViewById(R.id.detail_desc);
        trailer = (ListView) root.findViewById(R.id.trailers);
        s = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, a);
        trailer.setAdapter(s);
        trailer.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        setRetainInstance(true);
        if(f)
            update();
        return root;
    }

    public void setNew(Movies movie,boolean g){
        this.movie = movie;
        f = g;
        if(getActivity()!=null) {
            update();
        }
    }

    public void update(){
            title.setText(movie.getName());
            desc.setText(movie.getOverview());
            rate.setText(Integer.toString(movie.getRate()));
            date.setText(movie.getDate());
            Picasso.with(getActivity()).load(movie.getBackdrop()).placeholder(R.mipmap.ic_launcher).into(movieImage);
            new VideoAsyncTask().execute(movie.getId());
    }


    public class VideoAsyncTask extends AsyncTask<String ,Void , Boolean>{
        private final String LOG_TAG = VideoAsyncTask.class.getSimpleName();
        String appKey = "4a09ef88946390c1359f633a7987bf5f";
        String appLang = "en-US";
        String videoJson;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movie.clear();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            final String baseUrl = "https://api.themoviedb.org/3/movie/" + strings[0] + "/videos?";
            final String api_key = "api_key";
            final String language = "language";

            Uri built = Uri.parse(baseUrl).buildUpon().appendQueryParameter(api_key,appKey)
                    .appendQueryParameter(language,appLang).build();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(built.toString());
                Log.v(LOG_TAG, "built uri " + built.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

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

                videoJson = buffer.toString();
                Log.v(LOG_TAG, "Video JSON String: " + videoJson);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
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
                return getData(videoJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return false;
        }


        private boolean getData(String videoStr) throws JSONException{
            if(videoStr==null)
                return false;
            final String key = "key";
            final String result = "results";
            String baseVideoUrl = "https://www.youtube.com/watch?v=";

            JSONObject videoJson = new JSONObject(videoStr);
            JSONArray videosArray = videoJson.getJSONArray(result);

            for(int i=0;i<videosArray.length();i++){
                JSONObject oneVideo = videosArray.getJSONObject(i);
                movie.putTrailer(baseVideoUrl + oneVideo.getString(key));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                if (getActivity() != null) {
                    s = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, movie.trailers);
                    trailer.setAdapter(s);
                }
            }
            else{
                if (getActivity() != null) {
                    s = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, a);
                    trailer.setAdapter(s);
                }
            }
        }
    }


}

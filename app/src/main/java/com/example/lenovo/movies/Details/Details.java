package com.example.lenovo.movies.Details;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.movies.Data.ControlRealm;
import com.example.lenovo.movies.Data.Movies;
import com.example.lenovo.movies.Data.OfflineData;
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
import java.util.ArrayList;

/**
 * Created by Lenovo on 10/31/2016.
 */

public class Details extends Fragment {
    ImageButton fav;
    Movies movie;
    ImageView movieImage;
    TextView title,rate,date,desc;
    boolean f = false;
    boolean booleanPreview=false,booleanTrailers=false;
    OfflineData save;
    ControlRealm controlRealm;
    boolean inFavourite=false;
    Button goToTrailer,goToPreview;
    ArrayList<reviews> reviewses = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.test_details,container,false);
        fav = (ImageButton)root.findViewById(R.id.fav);
        movieImage = (ImageView)root.findViewById(R.id.detailImage);
        title = (TextView)root.findViewById(R.id.detail_title);
        rate = (TextView)root.findViewById(R.id.detail_rate);
        date = (TextView)root.findViewById(R.id.detail_date);
        desc = (TextView)root.findViewById(R.id.detail_desc);
        goToPreview = (Button)root.findViewById(R.id.preview);
        goToTrailer = (Button)root.findViewById(R.id.trailers);
        setRetainInstance(true);
        if(f)
            update();
        save = new OfflineData(getActivity());
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlRealm = new ControlRealm(movie,getActivity());
                if(save.inFavourite(movie.getName())){
                    fav.setImageResource(R.drawable.notfavourite);
                    save.removeFromFavourite(movie.getName());
                    message("Removed from Favourite");
                    controlRealm.removeMovie(movie.getId());
                    if(inFavourite){
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                }
                else{
                    fav.setImageResource(R.drawable.favourite);
                    save.putInFavourite(movie.getName());
                    controlRealm.putMovie();
                    message("Added to Favourite");
                }
            }
        });

        goToPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!reviewses.isEmpty()) {
                    Intent intent = new Intent(getActivity(), Previews.class);
                    intent.putParcelableArrayListExtra("reviews", reviewses);
                    startActivity(intent);
                }
                else
                    message("No Review available now");
            }
        });
        goToTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!movie.getTrailers().isEmpty()) {
                    Intent intent = new Intent(getActivity(), Trailers.class);
                    intent.putStringArrayListExtra("trailers", movie.getTrailers());
                    startActivity(intent);
                }
                else
                    message("No Trailer available now");
            }
        });


        return root;
    }

    public void setNew(Movies movie,boolean g){
        this.movie = movie;
        f = g;
        if(getActivity()!=null) {
            update();
        }
    }

    public void setInFavourite(boolean b){
        inFavourite=b;
    }

    public void update(){
        title.setText(movie.getName());
        desc.setText(movie.getOverview());
        rate.setText(Integer.toString(movie.getRate()));
        date.setText(movie.getDate());
        Picasso.with(getActivity()).load(movie.getImage()).placeholder(R.mipmap.ic_launcher).into(movieImage);
        save = new OfflineData(getActivity());
        if(getActivity()!=null) {
            if (!save.inFavourite(movie.getName())) {
                fav.setImageResource(R.drawable.notfavourite);
            }
            else
                fav.setImageResource(R.drawable.favourite);
        }
        new VideoAsyncTask().execute(movie.getId());
    }


    public class VideoAsyncTask extends AsyncTask<String ,Void , Boolean>{
        private final String LOG_TAG = VideoAsyncTask.class.getSimpleName();
        String appKey = "4a09ef88946390c1359f633a7987bf5f";
        String appLang = "en-US";
        String videoJson,previewJson;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movie.clear();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            final String baseUrlVideo = "https://api.themoviedb.org/3/movie/" + strings[0] + "/videos?";
            final String baseUrlPreview = "https://api.themoviedb.org/3/movie/" + strings[0] + "/reviews?";
            final String api_key = "api_key";
            final String language = "language";

            Uri builtVideo = Uri.parse(baseUrlVideo).buildUpon().appendQueryParameter(api_key,appKey)
                    .appendQueryParameter(language,appLang).build();
            Uri builtPreview = Uri.parse(baseUrlPreview).buildUpon().appendQueryParameter(api_key,appKey)
                    .appendQueryParameter(language,appLang).build();

            HttpURLConnection urlConnectionVideo = null;
            BufferedReader readerVideo = null;
            HttpURLConnection urlConnectionPreview = null;
            BufferedReader readerPreview = null;

            try {
                URL urlVideo = new URL(builtVideo.toString());
                Log.v(LOG_TAG, "built uri Video " + builtVideo.toString());
                URL urlPreview = new URL(builtPreview.toString());
                Log.v(LOG_TAG, "built uri Preview " + builtPreview.toString());

                urlConnectionVideo = (HttpURLConnection) urlVideo.openConnection();
                urlConnectionVideo.setRequestMethod("GET");
                urlConnectionVideo.connect();
                urlConnectionPreview = (HttpURLConnection) urlPreview.openConnection();
                urlConnectionPreview.setRequestMethod("GET");
                urlConnectionPreview.connect();

                InputStream inputStreamVideo = urlConnectionVideo.getInputStream();
                StringBuffer bufferVideo = new StringBuffer();
                if (inputStreamVideo == null) {
                    return false;
                }
                readerVideo = new BufferedReader(new InputStreamReader(inputStreamVideo));

                String line1;
                while ((line1 = readerVideo.readLine()) != null) {
                    bufferVideo.append(line1 + "\n");
                }

                InputStream inputStreamPreview = urlConnectionPreview.getInputStream();
                StringBuffer bufferPreview = new StringBuffer();
                if (inputStreamPreview == null) {
                    return false;
                }
                readerPreview = new BufferedReader(new InputStreamReader(inputStreamPreview));

                String line2;
                while ((line2 = readerPreview.readLine()) != null) {
                    bufferPreview.append(line2 + "\n");
                }

                videoJson = bufferVideo.toString();
                previewJson = bufferPreview.toString();
                Log.v(LOG_TAG, "Video JSON String: " + videoJson);
                Log.v(LOG_TAG, "Preview JSON String: " + previewJson);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
                if (urlConnectionVideo != null &&urlConnectionPreview != null) {
                    urlConnectionVideo.disconnect();
                    urlConnectionPreview.disconnect();
                }
                if (readerVideo != null && readerPreview!=null) {
                    try {
                        readerVideo.close();
                        readerPreview.close();
                    } catch (final IOException e) {

                    }
                }
            }


            try {
                return getData(videoJson,previewJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return false;
        }


        private boolean getData(String videoStr , String previewStr) throws JSONException{
            if(videoStr==null && previewStr==null) {
                booleanTrailers=false;
                booleanPreview=false;
                return false;
            }
            final String key = "key";
            final String result = "results";
            final String author = "author";
            final String content = "content";
            String baseVideoUrl = "https://www.youtube.com/watch?v=";

            JSONObject videoJson = new JSONObject(videoStr);
            JSONArray videosArray = videoJson.getJSONArray(result);
            JSONObject previewJson = new JSONObject(previewStr);
            JSONArray previewArray = previewJson.getJSONArray(result);
            for(int i=0;i<videosArray.length();i++){
                JSONObject oneVideo = videosArray.getJSONObject(i);
                movie.putTrailer(baseVideoUrl + oneVideo.getString(key));
            }

            for(int i=0;i<previewArray.length();i++){
                JSONObject onePreview = previewArray.getJSONObject(i);
                reviews oneReview = new reviews(onePreview.getString(author),onePreview.getString(content));
                movie.setOneReview(oneReview);
                reviewses.add(oneReview);
            }

            if(videoStr==null && previewStr!=null) {
                booleanTrailers=false;
                booleanPreview=true;
            }else if(videoStr!=null && previewStr==null) {
                booleanTrailers=true;
                booleanPreview=false;
            }
            else if(videoStr!=null && previewStr!=null){
                booleanTrailers=true;
                booleanPreview=true;
            }
            return true;
        }
    }


    public void message(String s){
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

}

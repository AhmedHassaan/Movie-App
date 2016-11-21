package com.example.lenovo.movies.Details;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.movies.Adapters.PreviewAdapter;
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
    ArrayAdapter<String> videoAdapter;
    String noInterner[] = {"No Internet Connection"};
    ExpandableListView expandableListView;
    PreviewAdapter previewAdapter;
    boolean booleanPreview=false,booleanTrailers=false;
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
        expandableListView = (ExpandableListView)root.findViewById(R.id.previewList);
        videoAdapter = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, noInterner);
        trailer.setAdapter(videoAdapter);
        trailer.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setRetainInstance(true);
        if(f)
            update();

        trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String click = movie.getTrailer(i);
                if(!click.equals("No Internet Connection"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(click));
                    intent.putExtra("force_fullscreen",true);
                    startActivity(intent);
                }
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

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean) {
                if(booleanTrailers && !booleanPreview) {
                    if (getActivity() != null) {
                        String hh[] = new String[movie.trailerNum()];
                        for (int i = 1; i <= movie.trailerNum(); i++)
                            hh[i - 1] = "Trailer " + i + "\n";
                        videoAdapter = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, hh);
                        trailer.setAdapter(videoAdapter);
                        previewAdapter = new PreviewAdapter(movie.getReviews());
                        expandableListView.setAdapter(previewAdapter);
                    }
                }
                else if (booleanPreview && !booleanTrailers){
                    if (getActivity() != null) {
                        String hh[] = new String[movie.trailerNum()];

                        for (int i = 1; i <= movie.trailerNum(); i++)
                            hh[i - 1] = "Trailer " + i;
                        previewAdapter = new PreviewAdapter(movie.getReviews());
                        expandableListView.setAdapter(previewAdapter);
                        videoAdapter = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, noInterner);
                        trailer.setAdapter(videoAdapter);
                    }
                }
                else{
                    if (getActivity() != null) {
                        String hh[] = new String[movie.trailerNum()];
                        for (int i = 1; i <= movie.trailerNum(); i++)
                            hh[i - 1] = "Trailer " + i;
                        videoAdapter = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, hh);
                        trailer.setAdapter(videoAdapter);
                        previewAdapter = new PreviewAdapter(movie.getReviews());
                        expandableListView.setAdapter(previewAdapter);
                    }
                }
            }
            else{
                if (getActivity() != null) {
                    videoAdapter = new ArrayAdapter<String>(getActivity(), R.layout.one_trailer, R.id.one, noInterner);
                    trailer.setAdapter(videoAdapter);
                    previewAdapter = new PreviewAdapter(movie.getReviews());
                    expandableListView.setAdapter(previewAdapter);
                }
            }
        }
    }


}

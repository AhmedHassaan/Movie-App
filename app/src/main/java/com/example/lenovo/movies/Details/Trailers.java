package com.example.lenovo.movies.Details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lenovo.movies.R;

import java.util.ArrayList;

public class Trailers extends AppCompatActivity {
    ArrayList<String> trailers;
    ArrayAdapter<String> videoAdapter;
    ListView trailer;
    ArrayList<String> hh= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        trailer = (ListView)findViewById(R.id.trailers);
        trailers = getIntent().getStringArrayListExtra("trailers");
        int j= trailers.size();
        for (int i = 0; i < j; i++) {
            String s = "Trailer " + (i + 1);
            hh.add(s);
        }
        videoAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.one_trailer, R.id.one, hh);
        if(videoAdapter!=null)
            trailer.setAdapter(videoAdapter);
        trailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(i)));
                intent.putExtra("force_fullscreen",true);
                startActivity(intent);
            }
        });
    }
}

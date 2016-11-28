package com.example.lenovo.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.example.lenovo.movies.Adapters.PreviewAdapter;
import com.example.lenovo.movies.Details.reviews;

import java.util.ArrayList;

public class Previews extends AppCompatActivity {
    ExpandableListView expandableListView;
    ArrayList<reviews> reviewses = new ArrayList<>();
    PreviewAdapter previewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previews);
        expandableListView = (ExpandableListView)findViewById(R.id.previewList);
        reviewses = getIntent().getParcelableArrayListExtra("reviews");
        previewAdapter = new PreviewAdapter(reviewses);
        expandableListView.setAdapter(previewAdapter);
    }
}

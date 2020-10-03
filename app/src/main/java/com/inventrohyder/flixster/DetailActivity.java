package com.inventrohyder.flixster;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.inventrohyder.flixster.models.Movie;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    TextView mTvTitle;
    TextView mTvOverview;
    RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTvTitle = findViewById(R.id.tvTitle);
        mTvOverview = findViewById(R.id.tvOverview);
        mRatingBar = findViewById(R.id.ratingBar);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        assert movie != null;
        mTvTitle.setText(movie.getTitle());
        mTvOverview.setText(movie.getOverview());
        mRatingBar.setRating((float) movie.getRating());
    }
}
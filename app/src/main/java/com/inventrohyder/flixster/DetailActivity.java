package com.inventrohyder.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.inventrohyder.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Locale;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    public static final String YOUTUBE_API_KEY = "AIzaSyCcUpJUWRLjXUTmwqHPFrw-4drSAVgwwbU";
    public static final String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String TAG = getClass().getSimpleName();
    TextView mTvTitle;
    TextView mTvOverview;
    RatingBar mRatingBar;

    YouTubePlayerView mYouTubePlayerView;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTvTitle = findViewById(R.id.tvTitle);
        mTvOverview = findViewById(R.id.tvOverview);
        mRatingBar = findViewById(R.id.ratingBar);
        mYouTubePlayerView = findViewById(R.id.player);

        mMovie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        assert mMovie != null;
        mTvTitle.setText(mMovie.getTitle());
        mTvOverview.setText(mMovie.getOverview());
        mRatingBar.setRating((float) mMovie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(Locale.ENGLISH, VIDEO_URL, mMovie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0) {
                        return;
                    }
                    String key = results.getJSONObject(0).getString("key");
                    Log.d(TAG, "onSuccess: Youtube Key: " + key);
                    initializeYoutube(key);
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: Failed to parse JSON", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }

    private void initializeYoutube(final String key) {
        mYouTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess: ");

                if (mMovie.isPopular()) {
                    youTubePlayer.loadVideo(key);
                    youTubePlayer.play();
                } else {
                    // do any work here to cue video, play video, etc.
                    youTubePlayer.cueVideo(key);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: ");
            }
        });
    }
}
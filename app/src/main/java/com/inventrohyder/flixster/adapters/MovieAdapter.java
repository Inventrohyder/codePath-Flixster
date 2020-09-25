package com.inventrohyder.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inventrohyder.flixster.R;
import com.inventrohyder.flixster.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    Context mContext;
    List<Movie> mMovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    // Involves inflating the layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View movieView = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }


    // Usually involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        // Get the movie at the passed in position
        Movie movie = mMovies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        TextView mTvOverview;
        ImageView mTvPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tvTitle);
            mTvOverview = itemView.findViewById(R.id.tvOverview);
            mTvPoster = itemView.findViewById(R.id.tvPoster);
        }

        public void bind(Movie movie) {
            mTvTitle.setText(movie.getTitle());
            mTvOverview.setText(movie.getOverview());
            String imageUrl;
            // if phone is in Landscape
            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // then imageUrl = drop image
                imageUrl = movie.getBackdropPath();
            } else {
                // else imageUrl = poster image
                imageUrl = movie.getPosterPath();
            }
            Glide.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_movie)
                    .error(R.drawable.ic_broken_image)  // Error image
                    .into(mTvPoster);
        }
    }
}

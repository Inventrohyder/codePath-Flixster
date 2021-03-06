package com.inventrohyder.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.inventrohyder.flixster.DetailActivity;
import com.inventrohyder.flixster.R;
import com.inventrohyder.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private final int POPULAR = 1;
    private final int NOT_POPULAR = 0;

    Context mContext;
    List<Movie> mMovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMovies.get(position).isPopular()) {
            return POPULAR;
        } else {
            return NOT_POPULAR;
        }
    }

    // Involves inflating the layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case POPULAR:
                View v1 = inflater.inflate(R.layout.item_popular_movie, parent, false);
                viewHolder = new ViewHolder1(v1);
                break;
            case NOT_POPULAR:
            default:
                View v2 = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new ViewHolder2(v2);
        }
        return viewHolder;
    }


    // Usually involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);

        // Get the movie at the passed in position
        Movie movie = mMovies.get(position);

        // Bind the movie data into the VH
        switch (holder.getItemViewType()) {
            case POPULAR:
                ViewHolder1 vh1 = (ViewHolder1) holder;
                vh1.bind(movie);
                break;
            case NOT_POPULAR:
            default:
                ViewHolder2 vh2 = (ViewHolder2) holder;
                vh2.bind(movie);
        }
    }


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public abstract static class BaseViewHolder extends RecyclerView.ViewHolder {

        ViewGroup mContainer;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.container);
        }
    }

    public class ViewHolder1 extends BaseViewHolder {

        private ImageView mPopTvPoster;
        private ProgressBar mProgressBar;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            mPopTvPoster = itemView.findViewById(R.id.tvPoster);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        public void bind(final Movie movie) {

            Glide.with(mContext)
                    .load(movie.getBackdropPath())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false; // important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.ic_broken_image)  // Error image
                    .transform(new RoundedCorners(50))
                    .into(mPopTvPoster);

            // 1. Register click lister on the whole row
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 2. Navigate to a new Activity on tap
                    Intent i = new Intent(mContext, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, mPopTvPoster, "banner");
                    mContext.startActivity(i, options.toBundle());
                }
            });
        }
    }

    public class ViewHolder2 extends BaseViewHolder {

        ImageView mTvPoster;
        ProgressBar mProgressBar;
        private TextView mTvTitle;
        private TextView mTvOverview;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tvTitle);
            mTvOverview = itemView.findViewById(R.id.tvOverview);
            mTvPoster = itemView.findViewById(R.id.tvPoster);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        public void bind(final Movie movie) {
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
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false; // important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.ic_broken_image)  // Error image
                    .into(mTvPoster);

            // 1. Register click lister on the whole row
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 2. Navigate to a new Activity on tap
                    Intent i = new Intent(mContext, DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    Pair<View, String> p1 = Pair.create((View) mTvPoster, "banner");
                    Pair<View, String> p2 = Pair.create((View) mTvTitle, "title");
                    Pair<View, String> p3 = Pair.create((View) mTvOverview, "overview");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, p1, p2, p3);
                    mContext.startActivity(i, options.toBundle());
                }
            });
        }
    }

}

package com.example.movies;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    // adapter is only for showing the data.
    private List<Movie> movies = new ArrayList<>();
    private OnReachEndListener onReachEndListener;

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.movie_item,
                parent,
                false
        );
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //fix initial size of imageView
        Log.d("MovieAdapter", "onBindViewHolder: " + position);
        Movie movie = movies.get(position);
        if (position >= movies.size() - 10 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }

        //sometimes there is no poster on movies with bad ratings
        if (movie.getPoster() != null) {
            Glide.with(holder.itemView)
                    .load(movie.getPoster().getUrl())
                    //there is a poster but without url in json
                    .error(R.drawable.picture)
                    .into(holder.imageViewPoster);
        } else {
            Glide.with(holder.itemView)
                    .load(R.drawable.picture)
                    .into(holder.imageViewPoster);
        }


        double rating = movie.getRating().getKp();
        int backgroudId;
        if (rating > 8) {
            backgroudId = R.drawable.circle_green;
        } else if (rating > 7) {
            backgroudId = R.drawable.circle_orange;
        } else {
            backgroudId = R.drawable.circle_red;
        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), backgroudId);
        holder.textViewRating.setBackground(background);
        holder.textViewRating.setText(String.format("%.1f", rating));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    interface OnReachEndListener {
        void onReachEnd();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
    //static when we don't use variables of methods of parent

        private final ImageView imageViewPoster;
        private final TextView textViewRating;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }
    }
}

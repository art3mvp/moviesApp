package com.example.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView textViewYear;
    private TextView textViewTitle;
    private ImageView imageViewDetailPoster;
    private TextView textViewDescription;
    private static final String EXTRA_MOVIE = "movie";
    private static final String TAG = "TAG";
    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);

        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        Glide.with(this)
                .load(movie.getPoster().getUrl())
                .error(R.drawable.picture)
                .into(imageViewDetailPoster);
        textViewTitle.setText(movie.getName());
        textViewYear.setText(String.valueOf(movie.getYear()));
        textViewDescription.setText(movie.getDescription());


        movieDetailViewModel.loadTrailers(movie.getId());
        movieDetailViewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                Log.d(TAG, trailers.toString());
            }
        });

    }

    private void initViews() {
        textViewYear = findViewById(R.id.textViewYear);
        textViewTitle = findViewById(R.id.textViewTitle);
        imageViewDetailPoster = findViewById(R.id.imageViewDetailPoster);
        textViewDescription = findViewById(R.id.textViewDescription);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}
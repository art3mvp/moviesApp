package com.example.movies;

import com.google.gson.internal.GsonBuildConfig;
import java.util.Properties;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {


    @GET("movie?limit=30&sortField=votes.kp&sortType=-1&rating.kp=4-10")
    @Headers("X-API-KEY:"+BuildConfig.API_KEY)
    Single<MovieResponse> loadMovies(@Query("page") int page);
}

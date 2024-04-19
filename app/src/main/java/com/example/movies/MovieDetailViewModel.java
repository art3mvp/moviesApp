package com.example.movies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {
    private static final String TAG = "TAG";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public void loadTrailers(int id) {
        Disposable disposable = ApiFactory.apiService.loadTrailers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TrailersResponse>() {
                    @Override
                    public void accept(TrailersResponse trailersResponse) throws Throwable {
                        trailers.setValue(trailersResponse.getTrailersList().getTrailers());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.getMessage().toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

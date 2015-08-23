package com.sachin.app.popularmovies.Movies;

import android.app.Application;
import android.content.Context;

/**
 * Created by Sachin on 22/08/15.
 */
public class MoviesApplication extends Application {

    private static MoviesApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MoviesApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}

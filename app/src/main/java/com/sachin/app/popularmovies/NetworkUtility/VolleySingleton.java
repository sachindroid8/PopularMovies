package com.sachin.app.popularmovies.NetworkUtility;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sachin.app.popularmovies.Movies.MoviesApplication;

/**
 * Created by Sachin on 22/08/15.
 */
public class VolleySingleton {

    private RequestQueue mRequestQueue;

    private static VolleySingleton ourInstance = null;

    public static VolleySingleton getInstance() {

        if (ourInstance == null) {

            ourInstance = new VolleySingleton();
        }

        return ourInstance;
    }

    private VolleySingleton() {

        mRequestQueue = Volley.newRequestQueue(MoviesApplication.getAppContext());

    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}

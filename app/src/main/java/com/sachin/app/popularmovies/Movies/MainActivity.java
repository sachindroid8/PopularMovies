package com.sachin.app.popularmovies.Movies;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sachin.app.popularmovies.Helper.CheckConnection;
import com.sachin.app.popularmovies.NetworkUtility.VolleySingleton;
import com.sachin.app.popularmovies.Helper.Constants;
import com.sachin.app.popularmovies.Pojo.Movie;
import com.sachin.app.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String MOVIE_LIST_STATE = "movie_list_state";
    private Toolbar toolBar;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private JSONObject jsonObject;
    private JSONObject jsonResultObject;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementExitTransition();

        }

        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolBar);

        //CheckInternetConnection();
        gridView = (GridView) findViewById(R.id.gridView);

        if (savedInstanceState != null) {
            CheckInternetConnection();
            movieArrayList.clear();
            movieArrayList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE);
            gridViewAdapter = new GridViewAdapter(MainActivity.this, movieArrayList);
            gridView.setAdapter(gridViewAdapter);
        } else {
            CheckInternetConnection();
            String url = getRequestURL("popularity");
            sendAPIRequest(url);
        }

        //Parcelable state = gridView.onSaveInstanceState();
        //gridView.onRestoreInstanceState(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings" + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }*/

        if (id == R.id.sort) {
            View menuItemView = findViewById(R.id.sort); // SAME ID AS MENU ID
            PopupMenu popupMenu = new PopupMenu(this, menuItemView);
            popupMenu.inflate(R.menu.menu_sort);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int menu_id = item.getItemId();

                    if (menu_id == R.id.popular) {
                        CheckInternetConnection();
                        String url = getRequestURL("popularity");
                        movieArrayList.clear();
                        gridViewAdapter.notifyDataSetChanged();
                        sendAPIRequest(url);
                        return true;
                    }

                    if (menu_id == R.id.rated) {
                        CheckInternetConnection();
                        String url = getRequestURL("vote_count");
                        movieArrayList.clear();
                        gridViewAdapter.notifyDataSetChanged();
                        sendAPIRequest(url);
                        return true;
                    }

                    return false;
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getRequestURL(String criteria) {
        return Constants.BASE_URL
                + Constants.URL_CHAR_PARAM_QUESTION
                + Constants.URL_PARAM_SORT_BY
                + criteria
                + Constants.URL_PARAM_DESCENDING
                + Constants.URL_CHAR_PARAM_AMPERSAND
                + Constants.URL_PARAM_API_KEY
                + Constants.API_KEY;
    }

    private void sendAPIRequest(String url) {

        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        parseJsonResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void parseJsonResponse(String response) {

        if (response != null || response.length() > 0) {
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.has("results")) {
                    JSONArray jsonResultArray = jsonObject.getJSONArray("results");
                    items = new String[jsonResultArray.length()];
                    for (int i = 0; i < jsonResultArray.length(); i++) {
                        jsonResultObject = jsonResultArray.getJSONObject(i);
                        //items[i] = Constants.IMAGE_BASE_URL + jsonResultObject.get("poster_path").toString();

                        String title = null;
                        if (jsonResultObject.has("title")) {
                            title = jsonResultObject.getString("title");
                        }

                        long id = 0;
                        if (jsonResultObject.has("id")) {
                            id = jsonResultObject.getLong("id");
                        }

                        String original_title = null;
                        if (jsonResultObject.has("original_title")) {
                            original_title = jsonResultObject.getString("original_title");
                        }

                        String backdrop_path = null;
                        if (jsonResultObject.has("backdrop_path")) {
                            backdrop_path = jsonResultObject.getString("backdrop_path");
                        }

                        String original_language = null;
                        if (jsonResultObject.has("original_language")) {
                            original_language = jsonResultObject.getString("original_language");
                        }

                        String overview = null;
                        if (jsonResultObject.has("overview")) {
                            overview = jsonResultObject.getString("overview");
                        }

                        String poster_path = null;
                        if (jsonResultObject.has("poster_path")) {
                            poster_path = jsonResultObject.getString("poster_path");
                        }

                        String release_date = null;
                        if (jsonResultObject.has("release_date")) {
                            release_date = jsonResultObject.getString("release_date");
                        }

                        double popularity = 0;
                        if (jsonResultObject.has("popularity")) {
                            popularity = jsonResultObject.getDouble("popularity");
                        }

                        double vote_average = 0;
                        if (jsonResultObject.has("vote_average")) {
                            vote_average = jsonResultObject.getDouble("vote_average");
                        }

                        long vote_count = 0;
                        if (jsonResultObject.has("vote_count")) {
                            vote_count = jsonResultObject.getLong("vote_count");
                        }

                        Date date = dateFormat.parse(release_date);
                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);
                        movie.setOriginal_title(original_title);
                        movie.setOriginal_language(original_language);
                        movie.setRelease_date(date);
                        movie.setBackdrop_path(Constants.IMAGE_BASE_URL_500W + backdrop_path);
                        movie.setOverview(overview);
                        movie.setPopularity(popularity);
                        movie.setPoster_path(Constants.IMAGE_BASE_URL + poster_path);
                        movie.setVote_average(vote_average);
                        movie.setVote_count(vote_count);

                        movieArrayList.add(movie);

                    }
                }
                gridViewAdapter = new GridViewAdapter(MainActivity.this, movieArrayList);
                gridView.setAdapter(gridViewAdapter);


            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, "Something is broken", Toast.LENGTH_SHORT).show();
            }

        }
        return;
    }

    private void CheckInternetConnection() {
        if (!CheckConnection.isConnection(MainActivity.this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            //Intent i = new Intent(MainActivity.this, ExceptionActivity.class);
            //startActivity(i);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_STATE, movieArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieArrayList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE);
    }

}

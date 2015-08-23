package com.sachin.app.popularmovies.Movies;

import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.sachin.app.popularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private ImageView backdropImage, posterImage;
    private TextView title, release_date, ratings, users, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition();

        }
        setContentView(R.layout.activity_details);

        backdropImage = (ImageView) findViewById(R.id.movie_backdrop);

        posterImage = (ImageView) findViewById(R.id.movie_poster);

        title = (TextView) findViewById(R.id.movie_name);
        title.setText(getIntent().getExtras().getString("title"));

        release_date = (TextView) findViewById(R.id.releaseDateTxt);
        String dateval = getIntent().getExtras().getString("date");
        release_date.setText("Release Date: " + getIntent().getExtras().getString("date").substring(0, 10) + " ," + dateval.substring(dateval.length() - 4, dateval.length()));

        ratings = (TextView) findViewById(R.id.ratingsTxt);
        ratings.setText("Ratings: " + getIntent().getExtras().getDouble("ratings") + "/10");

        users = (TextView) findViewById(R.id.usersTxt);
        users.setText("From " + getIntent().getExtras().getLong("users") + " users");

        overview = (TextView) findViewById(R.id.overviewTxt);
        overview.setText(getIntent().getExtras().getString("overview"));

        Picasso.with(this).load(getIntent().getExtras().getString("backdrop")).placeholder(R.drawable.backdrop).fit().centerCrop().into(backdropImage);
        Picasso.with(this).load(getIntent().getExtras().getString("poster")).fit().centerInside().into(posterImage);
        toolBar = (Toolbar) findViewById(R.id.app_bar);
        toolBar.setTitle(getIntent().getExtras().getString("title"));
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
            return true;
        }*/

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}

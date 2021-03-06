package com.sachin.app.popularmovies.Movies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sachin.app.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private ImageView backdropImage, posterImage;
    private TextView title, release_date, ratings, users, overview;
    Palette palette;
    float[] vibrant = null;
    float[] vibrantlight = null;
    private View bgViewGroup;
    private static final long ANIM_DURATION = 1000;

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

        // Converting the image into a bitmap
        //Bitmap img = getBitmapFromURL(getIntent().getExtras().getString("poster"));


        // Getting the different types of colors from the Image


        toolBar = (Toolbar) findViewById(R.id.app_bar);
        toolBar.setTitle(getIntent().getExtras().getString("title"));
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupLayout();
            setupWindowAnimations();

        }


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

    private void setupLayout() {
        bgViewGroup = findViewById(R.id.cv);
    }

    @TargetApi(21)
    private void setupWindowAnimations() {
        setupEnterAnimations();
        //setupExitAnimations();
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            if (urls.length > 0) {
                URL url;
                try {
                    url = new URL(urls[0]);
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = null;
                    input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);

            if (bmp != null) {
                try {
                    Bitmap img = bmp;
                    Palette.from(img).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            //work with the palette here
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (vibrantSwatch != null) {
                                vibrant = vibrantSwatch.getHsl();
                                //  setStatusBarColor only works above API 21!
                                if (Build.VERSION.SDK_INT >= 21)
                                    getWindow().setStatusBarColor(Color.HSVToColor(vibrant));
                            }


                            Palette.Swatch vibrantLightSwatch = palette.getLightVibrantSwatch();
                            if (vibrantLightSwatch != null) {
                                vibrantlight = vibrantLightSwatch.getHsl();
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.HSVToColor(vibrantlight)));
                            }

                        }
                    });
                } catch (Exception ex) {

                }

            }
        }
    }

    @TargetApi(21)
    private void setupEnterAnimations() {
        Transition enterTransition = getWindow().getSharedElementEnterTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                new MyAsyncTask().execute(getIntent().getExtras().getString("backdrop"));
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevealShow(bgViewGroup);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    @TargetApi(21)
    private void setupExitAnimations() {
        Transition sharedElementReturnTransition = getWindow().getSharedElementReturnTransition();
        sharedElementReturnTransition.setStartDelay(ANIM_DURATION);


        Transition returnTransition = getWindow().getReturnTransition();
        returnTransition.setDuration(ANIM_DURATION);
        returnTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                animateRevealHide(bgViewGroup);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    @TargetApi(21)
    private void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    @TargetApi(21)
    private void animateRevealHide(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int initialRadius = viewRoot.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewRoot.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }


}

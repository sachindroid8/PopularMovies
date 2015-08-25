package com.sachin.app.popularmovies.Movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sachin.app.popularmovies.Pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sachin on 21/08/15.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Movie> items;

    public GridViewAdapter(Context context, ArrayList<Movie> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ImageView thumbnails;
        if (convertView == null) {
            thumbnails = new ImageView(context);
            convertView = thumbnails;
            thumbnails.setPadding(4, 4, 4, 4);
            thumbnails.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            thumbnails = (ImageView) convertView;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightRatio = 4;
        int widthRatio = 2;
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            widthRatio = 2;
            heightRatio = 2;
        }
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Picasso.with(context).load(String.valueOf(items.get(position).getPoster_path())).resize(width / widthRatio, height / heightRatio).into(thumbnails);
        thumbnails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    v.setTransitionName("selectedImage");
                    options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, v, v.getTransitionName());
                }

                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("title", items.get(position).getTitle());
                i.putExtra("backdrop", items.get(position).getBackdrop_path());
                i.putExtra("poster", items.get(position).getPoster_path());
                i.putExtra("date", items.get(position).getRelease_date().toString());
                i.putExtra("ratings", items.get(position).getVote_average());
                i.putExtra("users", items.get(position).getVote_count());
                i.putExtra("overview", items.get(position).getOverview());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(i, options.toBundle());
                } else {
                    context.startActivity(i);
                }


            }
        });
        return convertView;

    }
}

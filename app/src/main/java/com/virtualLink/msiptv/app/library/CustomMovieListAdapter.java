package com.virtualLink.msiptv.app.library;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.virtualLink.msiptv.app.R;

import java.util.ArrayList;

public class CustomMovieListAdapter extends BaseAdapter {
    // private Context mContext;
    private ArrayList<CategoriesList> catList;
    private Activity activity;
    private ArrayList<MovieItemList> movieItemList;
    private final String base_url = "http://10.0.2.2/tvserver";

    public CustomMovieListAdapter(Activity activity, ArrayList<MovieItemList> movieList) {
        this.activity = activity;
        this.movieItemList = movieList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.movieItemList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.movieItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MovieItemList Movie = (MovieItemList) getItem(position);

        if (convertView == null) {
            grid = new View(this.activity);
            grid = inflater.inflate(R.layout.movie_list_itmes, null);

//            TextView year = (TextView) grid.findViewById(R.id.year);
//            year.setText(String.valueOf(Movie.getYear()));

//            TextView description = (TextView) grid.findViewById(R.id.description);
           // description.setText(Movie.getDescription());

            ImageView imageView = (ImageView) grid.findViewById(R.id.thumbnail);

            Picasso.with(this.activity)
                    .load( Movie.getImage_url()).resize(120, 110)
                    .into(imageView);

            TextView title = (TextView) grid.findViewById(R.id.movie_title);
            title.setText(Movie.getTitle());

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}

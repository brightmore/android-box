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

/**
 * Created by bright on 10/7/14.
 */
//public class CategoryListAdapter extends BaseAdapter{
//
//    private Activity activity;
//    private LayoutInflater inflater;
//    private ArrayList<CategoriesList> list;
//    private ImageLoader mImageLoader;
//    public CategoryListAdapter(Activity activity, ArrayList<CategoriesList> categoriesLists,ImageLoader mImageLoader) {
//        this.activity = activity;
//        this.list = categoriesLists;
//        this.mImageLoader = mImageLoader;
//    }
//
//    @Override
//    public int getCount() {
//        return this.list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return this.list.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        View grid;
//        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        CategoriesList  category = (CategoriesList) this.list.get(i);
//
//        if (view == null) {
//System.out.println(category.toString());
//            grid = new View(this.activity);
//            grid = inflater.inflate(R.layout.activity_movie_category_list, null);
//            TextView title = (TextView) grid.findViewById(R.id.title);
//            title.setText(category.getCategory());
//
//            TextView description = (TextView) grid.findViewById(R.id.description);
//            description.setText(category.getDescription());
//
//            NetworkImageView imageView = (NetworkImageView)grid.findViewById(R.id.thumbnail);
//            imageView.setImageUrl(category.getImageUrl(),this.mImageLoader);
//
//        } else {
//            grid = (View) view;
//        }
//        return grid;
//    }
//}

public class MovieCategoryListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<CategoriesList> categoryItems;
//    private static final String baseUrl = "http://10.0.2.2/tvserver";

    public MovieCategoryListAdapter(Activity activity, ArrayList<CategoriesList> items) {
        this.activity = activity;
        this.categoryItems = items;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return categoryItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.movie_category_list, null);


        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
       // TextView description = (TextView) convertView.findViewById(R.id.description);

        // getting movie data for the row
        CategoriesList category = (CategoriesList) getItem(position);

        title.setText(category.getCategory());

        Picasso.with(this.activity)
                .load(category.getImageUrl()).resize(120, 110)
                .into(thumbNail);
      //  description.setText(category.getDescription());

        return convertView;
    }

}
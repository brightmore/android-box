package com.virtualLink.msiptv.app.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.virtualLink.msiptv.app.R;

import java.util.ArrayList;

/**
 * Created by bright on 10/5/14.
 */
public class LazyAdapter extends ArrayAdapter<MovieItemList> {
    ImageLoader imageLoader;
    Context context;
    ArrayList<MovieItemList> movieItemLists;
    // declaring our ArrayList of items
    private ArrayList<MovieItemList> objects;
    public LazyAdapter(Context context,ArrayList<MovieItemList> itemList,ImageLoader imageLoader,RequestQueue requestQueue){
        super(context, 0, itemList);
        this.context = context;
        this.movieItemLists = itemList;

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);


            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    @Override
    public int getCount() {
        return this.movieItemLists.size();
    }

    @Override
    public MovieItemList getItem(int i) {
        return this.movieItemLists.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (viewGroup == null) {
           // viewGroup = context.getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.movie_list_itmes, null);
        }
        MovieItemList m = objects.get(i);

        if (m != null) {
           // TextView year = (TextView) view.findViewById(R.id.year);
            TextView title = (TextView) view.findViewById(R.id.title);
           // TextView description = (TextView) view.findViewById(R.id.description);
          //  NetworkImageView thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);
            // thumbnail image
           // thumbNail.setImageUrl(m.getImage_url(), imageLoader);
            // title
            title.setText(m.getTitle());
           // year.setText(String.valueOf(m.getYear()));
           // description.setText(m.getDescription());
        }

        return view;
    }
}

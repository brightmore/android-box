package com.virtualLink.msiptv.app.library;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;
import com.virtualLink.msiptv.app.R;

import java.util.ArrayList;

/**
 * Created by bright on 11/5/14.
 */
public class MusicCategoryAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<MusicCategory> musicCategoryList;

    public MusicCategoryAdapter(Activity activity, ArrayList<MusicCategory> musicCategoryArrayList) {
        this.activity = activity;
        this.musicCategoryList = musicCategoryArrayList;
    }

    @Override
    public int getCount() {
        return this.musicCategoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.musicCategoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.category_player_items, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.poster);
        TextView title = (TextView) view.findViewById(R.id.category);

        MusicCategory musicCategory =  (MusicCategory) getItem(i);

        Picasso.with(this.activity)
                .load(musicCategory.getPosterUrl()).resize(180, 170)
                .into(imageView);
        title.setText(musicCategory.getCategoryTitle());

        return view;
    }
}

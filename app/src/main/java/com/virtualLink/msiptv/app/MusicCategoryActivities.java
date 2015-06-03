package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.virtualLink.msiptv.app.library.CategoriesList;
import com.virtualLink.msiptv.app.library.MusicCategory;
import com.virtualLink.msiptv.app.library.MusicCategoryAdapter;
import com.virtualLink.msiptv.app.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MusicCategoryActivities extends Activity {

    private final String POSTER = "poster";
    private final String CATEGORY_TITLE = "categoryTitle";

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;

    private  String base_url;
    private HashMap<String,String> config;
    private String baseUrl ;
    public JSONArray categories = null;
    private ArrayList<MusicCategory> musicCategoryList = new ArrayList<MusicCategory>();
    private GridView gridlistView;
    private JSONArray musicCategories;
    final int SHOW_SUBACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_category_view);

        config = Utilities.configuration(this);
        baseUrl = config.get("baseUrl");

        gridlistView = (GridView) findViewById(R.id.gridview);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MusicCategoryActivities.this,MusicListAllCategories.class);
                intent.putExtra("category","all");
                startActivityForResult(intent,SHOW_SUBACTIVITY);
            }
        });

        gridlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               MusicCategory musicCategory =  (MusicCategory)gridlistView.getAdapter().getItem(i);
               Bundle b = new Bundle();
                Intent intent = new Intent(MusicCategoryActivities.this,MusicListAllCategories.class);
              //  b.putString("category",musicCategory.getCategoryTitle());
                intent.putExtra("category",musicCategory.getCategoryTitle());
                startActivityForResult(intent,SHOW_SUBACTIVITY);
            }
        });

        Thread background = new Thread(new Runnable() {
            final String url = baseUrl+"/index.php/welcome/musicCategories";
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String input = Utilities.readJSON(url);
                        try{
                            JSONObject json = new JSONObject(input);
                            musicCategories = json.getJSONArray("result");

                            for(int i = 0; i < musicCategories.length(); i++){
                                JSONObject m = musicCategories.getJSONObject(i);
                                Log.d("record",m.getString(CATEGORY_TITLE));
                                MusicCategory musicCategory =  new MusicCategory(m.getString(CATEGORY_TITLE),baseUrl+"/"+m.getString(POSTER));
                                musicCategoryList.add(musicCategory);
                            }

                            MusicCategoryAdapter adapter = new MusicCategoryAdapter(MusicCategoryActivities.this,musicCategoryList);
                            gridlistView.setAdapter(adapter);
                        }catch (JSONException ex){
                            Log.d("music_category_error",ex.getMessage());
                        }
                    };
                });
            }
        });

        background.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_category_activities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

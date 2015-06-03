package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.virtualLink.msiptv.app.library.CategoriesList;
import com.virtualLink.msiptv.app.library.CustomMovieListAdapter;

import org.json.JSONArray;

import java.util.ArrayList;


public class categoryListActivity extends Activity {

    private static final String TAG = MovieListActivity.class.getSimpleName();
    // Movies json url
    private static final String url = "http://10.0.2.2/msiptv/movieCategories.php";
    private final String TAG_CATID = "category_id";
    private final String TAG_DESCRIPTION = "description";
    private final String TAG_IMAGEURL = "image_url";
    private final String TAG_CATEGORY = "category";

    public JSONArray categories = null;
    private ArrayList<CategoriesList> catList = new ArrayList<CategoriesList>();
    private GridView gridlistView;
    private CustomMovieListAdapter adapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_category_list);

        gridlistView = (GridView)findViewById(R.id.grid);

        // findViewById(R.id.gellery);
        // gridlistView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_list, menu);
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

package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.virtualLink.msiptv.app.library.CategoriesList;
import com.virtualLink.msiptv.app.library.MovieCategoryListAdapter;
import com.virtualLink.msiptv.app.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MovieCategoryListActivity extends Activity {
    // Log tag

    private static final String TAG = MovieCategoryListActivity.class.getSimpleName();
    // Movies json url
    private static String url = "";
    private  String base_url;
    private HashMap<String,String> config;
    private final String TAG_CATID = "category_id";
    private final String TAG_DESCRIPTION = "description";
    private final String TAG_IMAGEURL = "image_url";
    private final String TAG_CATEGORY = "category";

    public JSONArray categories = null;
    private ArrayList<CategoriesList> catList = new ArrayList<CategoriesList>();
    private GridView gridlistView;
    private MovieCategoryListAdapter adapter;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        config = Utilities.configuration(this);
        base_url = config.get("baseUrl");
        url = base_url + "/index.php/welcome/categories";

        // NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_category_list);
        gridlistView = (GridView)findViewById(R.id.grid);


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        Thread background = new Thread(new Runnable() {

            String description = null,category = null,image = null,catID;

            @Override
            public void run() {
               // JSONParser jParser = new JSONParser();
                // Getting JSON from URL

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                String input = Utilities.readJSON(url);
                                JSONObject json  = new JSONObject(input);

                                categories = json.getJSONArray("result");

                                for (int i = 0; i < categories.length(); i++) {
                                    JSONObject categoryG = categories.getJSONObject(i);
                                    description = categoryG.getString(TAG_DESCRIPTION);
                                    category = categoryG.getString(TAG_CATEGORY);
                                    catID = categoryG.getString(TAG_CATID);
                                    image = base_url+categoryG.getString(TAG_IMAGEURL);
                                    System.out.println("category == "+image);
                                    catList.add(new CategoriesList(category,description,image,catID));
                                    System.out.println(new CategoriesList(category,description,image,catID).toString());
                                }
                                MovieCategoryListAdapter adapter = new MovieCategoryListAdapter(MovieCategoryListActivity.this,catList);

                                gridlistView.setAdapter(adapter);

                                gridlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                       final CategoriesList categoriesList = (CategoriesList)gridlistView.getAdapter().getItem(i);
                                        view.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                final int SHOWSUBACTIVITY = 1;
                                               // Bundle b = new Bundle();
                                                Utilities.showToast(MovieCategoryListActivity.this,categoriesList.getCategory());
                                                System.out.println(categoriesList.getCategory());
                                                Log.d("find category --- ",categoriesList.getCategory());
                                               // b.putString("category",categoriesList.getCategory());
                                                Intent intent = new Intent(MovieCategoryListActivity.this,MovieListActivity.class);
                                                intent.putExtra("category",categoriesList.getCategory());
                                                startActivityForResult(intent,SHOWSUBACTIVITY);

                                            }
                                        });

                                    }
                                });

                            } catch (JSONException e) {

                                //@Todo, log the error and notification to the system admin

                                e.printStackTrace();
                            }
                        }
                    });
            }
        });
        background.start();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movie_gellary, menu);
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

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.virtualLink.msiptv.app.library.CustomMovieListAdapter;
import com.virtualLink.msiptv.app.library.MovieItemList;
import com.virtualLink.msiptv.app.util.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MovieListActivity extends Activity {
    // Log tag
    private static final String TAG = MovieListActivity.class.getSimpleName();
    // Movies json url
    private static String url = null;
    private final String TAG_TITLE = "title";
    private final String TAG_DESCRIPTION = "description";
    private final String TAG_IMAGEURL = "image_url";
    private final String TAG_CATEGORY = "category_id";
    private final String TAG_YEAR = "year";
    private final String TAG_VIDEO = "video_url";
    private  String base_url;
    private HashMap<String,String> config;

    public String category = null;
    public JSONArray movies = null;
    private ArrayList<MovieItemList> movieList = new ArrayList<MovieItemList>();
    private GridView gridlistView;
    private CustomMovieListAdapter adapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Just for testing, allow network access in the main thread
        // NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        config = Utilities.configuration(this);
        base_url = config.get("baseUrl");
        category = getIntent().getExtras().getString("category");

        url = base_url+"/index.php/welcome/moviesList/"+category;

        Thread background = new Thread(new Runnable() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String input = Utilities.readJSON(url);
                            JSONObject json = new JSONObject(input);
                            movies = json.getJSONArray("result");

                            Log.d("Programs onload", movies.toString());
                            for (int i = 0; i < movies.length(); i++) {
                                MovieItemList movie = new MovieItemList();
                                JSONObject m = movies.getJSONObject(i);
                                movie.setDescription(m.getString(TAG_DESCRIPTION));
                                movie.setCategory(m.getString(TAG_CATEGORY));
                                movie.setImage_url(base_url+m.getString(TAG_IMAGEURL));
                                movie.setTitle(m.getString(TAG_TITLE));
                                movie.setYear(m.getString(TAG_YEAR));
                                movie.setVideo_url(m.getString(TAG_VIDEO));

                                movieList.add(movie);
                            }

                            CustomMovieListAdapter adapter = new CustomMovieListAdapter(
                                    MovieListActivity.this,
                                    movieList
                            );

                            gridlistView.setAdapter(adapter);

                            gridlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    MovieItemList movies = (MovieItemList)gridlistView.getAdapter().getItem(position);
                                    Intent intent = new Intent(v.getContext(),VideoDetails.class);
                                    intent.putExtra("video_url",base_url+movies.getVideo_url());
                                    intent.putExtra("description",movies.getDescription());
                                    intent.putExtra("title",movies.getTitle());
                                    intent.putExtra("poster",movies.getImage_url());
                                    Log.d("videoUrl",base_url+movies.getVideo_url());
                                    startActivityForResult(intent,1);
                                   // finish();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace(); //@Todo
                        }
                    }
                });
            }
        });
        background.start();

        setContentView(R.layout.activity_movies_gellary);
        gridlistView =(GridView) findViewById(R.id.grid);
    }

    @Override
    public void onActivityResult(int requestID, int resultID, Intent i){
        super.onActivityResult(requestID, resultID, i);
        Bundle response=i.getExtras();
        category = response.getString("category");
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

    public String readBugzilla() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e("Network=>", "Failed to download file = "+statusCode);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}

package com.virtualLink.msiptv.app;

//import android.app.ListActivity;
//import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.videolan.libvlc.LibVLC;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualLink.msiptv.app.library.JSONParser;
import com.virtualLink.msiptv.app.library.TVChannel;
import com.virtualLink.msiptv.app.util.Utilities;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;

public class UrlListActivity extends Activity {
    LibVLC mLibVLC;
    //URL to get JSON Array
    private String baseUrl ;
    private HashMap<String,String> config;

    //JSON Node Names
    private static final String TAG_ID = "id";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_CHANNEL_NAME = "channel_name";
    private static final String TAG_MULTICAST = "multicast";
    private static final String TAG_TV_PROGRAMS = "success";

    public HashMap<String, String> channels = new HashMap<String, String>();
    public ArrayList<String> channelList = new ArrayList<String>();
    public ArrayList<String> categories = new ArrayList<String>();

    public ArrayList<TVChannel> oslist = new ArrayList<TVChannel>();
    public HorizontalScrollView scrollView = null;
    public ListView list = null;
    public TextView name;
    public TextView txtId;
    public JSONArray programs = null;
    LinearLayout buttonLayout;
    ArrayList<TVChannel> mylist;
    public String curName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Just for testing, allow network access in the main thread
        // NEVER use this is productive code
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = Utilities.configuration(this);
        baseUrl = config.get("baseUrl");


        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        list = (ListView) findViewById(R.id.list);

        Thread background = new Thread(new Runnable() {

            String channel_name = null,category = null,multicast = null,id = null;

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String input = Utilities.readJSON(baseUrl+"/index.php/welcome/tvprograms");
                            JSONObject json = new JSONObject(input);

                            programs = json.getJSONArray("result");
                            for (int i = 0; i < programs.length(); i++) {
                                JSONObject program = programs.getJSONObject(i);
                                channel_name = program.getString(TAG_CHANNEL_NAME);
                                category = program.getString(TAG_CATEGORY);
                                multicast = program.getString(TAG_MULTICAST);
                                id = program.getString(TAG_ID);

                                channels.put(id + ":" + category, channel_name + "," + multicast);

                                if (!categories.contains(category)) {
                                    categories.add(category);
                                }
                            }

                            for (int i = 0; i < categories.toArray().length; i++) {
                                Resources resources = getResources();

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                int leftandRight = 0, topAndButton = 0, padding = 0;

                                // Create Button
                                final Button btn = new Button(UrlListActivity.this);
                                Log.d(categories.get(i), categories.get(i));
                                // Give button an ID
                                btn.setId(i);
                                btn.setText(categories.get(i));

                                //set margin and padding
                                leftandRight = (int) resources.getDimension(R.dimen.dynamicBttnLeftRight);
                                topAndButton = (int) resources.getDimension(R.dimen.dynamicalbttnTopDown);
                                padding = (int) resources.getDimension(R.dimen.padding);

                                params.setMargins(leftandRight, topAndButton, leftandRight, topAndButton);
                                btn.setPadding(padding, padding, padding, padding);

                                // set the layoutParams on the button
                                btn.setLayoutParams(params);

                                buttonLayout.addView(btn);
                                // Set click listener for button
                                btn.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View v) {
                                        final Button b = (Button) v;
                                        v.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                final String categoryName = b.getText().toString();

                                                ArrayList<TVChannel> tvChannels = UrlListActivity.getChannelDetail(categoryName, channels);

                                                ArrayAdapter adapter = new ArrayAdapter<TVChannel>(UrlListActivity.this, android.R.layout.simple_list_item_1, tvChannels);
                                                Log.d("programs", tvChannels.toString());
                                                list.setAdapter(adapter);

                                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        final TVChannel urlList = (TVChannel) list.getAdapter().getItem(i);

                                                        runOnUiThread(new Runnable() {

                                                            @Override
                                                            public void run() {
                                                                //@TODO play video

                                                                Toast.makeText(getBaseContext(), urlList.name + " -> " + urlList.url, Toast.LENGTH_LONG).show();

                                                                Intent intent = new Intent(UrlListActivity.this,VideoActivity.class);
                                                                Bundle b = new Bundle();
                                                                b.putString("url", urlList.url);
                                                                b.putString("name", urlList.name);
                                                                //  b.putString("category", urlList.category);
                                                                intent.putExtras(b);
                                                                //setResult(1, intent);
                                                                startActivityForResult(intent,1);

                                                            }
                                                        });
                                                    }
                                                });

                                                //  list.invalidateViews();
                                                Log.d(categoryName, categoryName);
                                            }
                                        });
                                    }
                                });
                            }

                            Button movieBtn = new Button(UrlListActivity.this);
                            movieBtn.setText("Movies");
                            movieBtn.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    final View v = view;
                                    System.out.println("Systems out movies");
                                    startActivity(new Intent(v.getContext(), MovieCategoryListActivity.class));
                                }
                            });
                            buttonLayout.addView(movieBtn);
                        } catch (JSONException e) {
                            e.printStackTrace(); //@Todo
                        }
                    }
                });
            }
        });
        background.start();

        //   pDialog.dismiss();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 100;
        layoutParams.y = 100;
        layoutParams.width = 500;
        layoutParams.height = 500;
        layoutParams.alpha = 50;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        window.setAttributes(layoutParams);

        //        initVLC()
        list = (ListView) findViewById(R.id.list);
        //scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                Bundle b = data.getExtras();
                curName = b.getString("name");
                break;
        }
    }

 // Called after onCreate has finished, use to restore UI state
//    @Override

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    public static ArrayList<TVChannel> getChannelDetail(String category, HashMap<String, String> categories) {
        ArrayList<TVChannel> channels = new ArrayList<TVChannel>();

        String[] splitKey = null, splitValue = null;

        for (String value : categories.keySet()) {
            splitKey = value.split(":");
            splitValue = categories.get(value).split(",");
            if (!channels.contains(splitKey[1]) && category.equals(splitKey[1])) {
                channels.add(new TVChannel(splitValue[0], splitValue[1], splitKey[1]));
            }
        }
        return channels;
    }

    public static ArrayList<String> getUniqueCategories(ArrayList<String> categories) {
        ArrayList<String> channels = new ArrayList<String>();

        String[] splitKey = null;
        for (String key : categories) {
            splitKey = key.split(":");

            if (!channels.contains(splitKey[1])) {
                channels.add(splitKey[1]);
            }
        }

        return channels;
    }

    private void playUrl(String url) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(VideoActivity.LOCATION, url);
        startActivity(intent);
    }

    private void initVLC() {
        try {
            mLibVLC = LibVLC.getInstance();
            mLibVLC.init(this);
        } catch (LibVlcException e) {
            Toast.makeText(this,
                    "Error initializing the libVLC multimedia framework!",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
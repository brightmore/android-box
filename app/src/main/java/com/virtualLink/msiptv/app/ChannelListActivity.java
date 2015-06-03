package com.virtualLink.msiptv.app;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualLink.msiptv.app.library.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.widget.Button;
/**
 * Created by bright on 9/26/14.
 */
public class ChannelListActivity extends Activity {


    //URL to get JSON Array
    private static String url = "http://10.0.2.2/msiptv/get_channels.php";
    //JSON Node Names
    private static final String TAG_ID = "id";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_CHANNEL_NAME = "channel_name";
    private static final String TAG_MULTICAST = "multicast";
    private static final String TAG_TV_PROGRAMS = "tvprogram";
    public HashMap<String, String> channel = new HashMap<String, String>();
    public ArrayList<String> channelList = new ArrayList<String>();
    //private ArrayAdapter<String> listAdapter ;

    JSONArray programs = null;

    protected void onCreate(Bundle savedInstanceState){
        JSONParser jParser = new JSONParser();
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
final Button button = new Button(this);
        new Thread(new Runnable() {
            public void run() {

                button.post(new Runnable() {
                    public void run() {

                    }
                });
            }
        }).start();

        try{
            programs = json.getJSONArray(TAG_TV_PROGRAMS);
           // Log.d("json", programs.toString());
            for (int i = 0; i < programs.length(); i++) {
                JSONObject c = programs.getJSONObject(i);
                // Storing  JSON item in a Variable
                String id = c.getString(TAG_ID);
                String channel_name = c.getString(TAG_CHANNEL_NAME);
                String category = c.getString(TAG_CATEGORY);
                String multicast = c.getString(TAG_MULTICAST);
             //   System.out.println(id + "-" + channel_name + " - category " + category + "- multicast " + multicast);
                channelList.add(channel_name);
                channel.put(channel_name, multicast);

                // Adding value HashMap key => value
                // HashMap<String, String> map = new HashMap<String, String>();
                //  map.put(TAG_CHANNEL_NAME, channel_name);
                // map.put(TAG_category, api);
                //oslist.add(map);
            }
        }catch (Exception ex){
            Log.d("json_error",ex.toString());
        }
       // list = (ListView) findViewById(R.id.list);
System.out.println("Total Number of Channel is: "+channelList.size());
       // setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, (String[]) channelList.toArray()));
    }

}

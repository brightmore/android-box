package com.virtualLink.msiptv.app.library;

import android.util.Log;

import com.virtualLink.msiptv.app.util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bright on 11/9/14.
 */
public class SongManager {
    private String baseUrl = "http://10.0.2.2/tvserver/";
    final String url ;
    private StringBuilder urlBuilder = new StringBuilder();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    JSONArray musicRecords = null;
    public SongManager(String category) throws JSONException {
        urlBuilder.append(baseUrl);
        urlBuilder.append("index.php/ipservermain/musicUnderCategory/category/");
        urlBuilder.append(category);
        urlBuilder.append("/format/json");
        url = urlBuilder.toString();
        String input = Utilities.readJSON(url);
        JSONObject json = new JSONObject(input);
        musicRecords = json.getJSONArray("result");

        System.out.println("music url// "+url);
        //Log.d("music url// ",url);
    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    public ArrayList<HashMap<String, String>> getPlayList() throws JSONException {

//            JSONObject json = new JSONObject(input);
//            musicRecords = json.getJSONArray("result");
            if(musicRecords.length() > 0) {
                for (int i = 0; i < musicRecords.length(); i++) {
                    JSONObject m = musicRecords.getJSONObject(i);

                    HashMap<String, String> song = new HashMap<String, String>();
                    song.put("songTitle", m.getString("title"));
                    song.put("songPath", m.getString("musicfile"));

                    // Adding each song to SongList
                    songsList.add(song);
                }
                // return songs list array
                return songsList;
            }else{
                return null;
            }
    }

    public ArrayList<String> getMusicTitles() throws JSONException {
        ArrayList<String> musicTitles = new ArrayList<String>();
        if(musicRecords.length() > 0) {
            for (int i = 0; i < musicRecords.length(); i++) {
                JSONObject m = musicRecords.getJSONObject(i);

                // Adding each song to SongList
                musicTitles.add(m.getString("title"));
            }
            return musicTitles;
        }else{
            return null;
        }

    }
}

package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {

	// Declare variables
	ProgressDialog pDialog;
	VideoView videoView;
    private String videoUrl = null;
	// Insert your Video URL
	String VideoURL ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the layout from video_main.xml
		setContentView(R.layout.videoview_main);
		// Find your VideoView in your video_main.xml layout
		videoView = (VideoView) findViewById(R.id.VideoView);
		// Execute StreamVideo AsyncTask

		// Create a progressbar
		pDialog = new ProgressDialog(VideoViewActivity.this);
		// Set progressbar title
		pDialog.setTitle("Video Streaming");
		// Set progressbar message
		pDialog.setMessage("Buffering...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		// Show progressbar
		pDialog.show();

		try {

            videoUrl = getIntent().getExtras().getString("video_url");

			// Start the MediaController
			MediaController mediacontroller = new MediaController(
					VideoViewActivity.this);
			mediacontroller.setAnchorView(videoView);
			// Get the URL from String VideoURL
			Uri video = Uri.parse(videoUrl);
			videoView.setMediaController(mediacontroller);
			videoView.setVideoURI(video);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		videoView.requestFocus();
		videoView.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				pDialog.dismiss();
				videoView.start();
			}
		});

	}

    @Override
    public void onActivityResult(int requestID, int resultID, Intent i){
        super.onActivityResult(requestID, resultID, i);
        switch (resultID) {
            case 1:
                videoUrl = i.getExtras().getString("video_url");
                break;
        }
    }

}

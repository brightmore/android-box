package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


public class VideoDetails extends Activity {

    private String posterUrl,description,videoUrl,title;
    // Declare variables
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        // Create a progressbar
        pDialog = new ProgressDialog(VideoDetails.this);
        // Set progressbar title
        //pDialog.setTitle("Video Streaming");
        // Set progressbar message
        pDialog.setMessage("load...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        // Show progressbar
        pDialog.show();
        ImageView banner = (ImageView) findViewById(R.id.poster);
        TextView txtDescription = (TextView) findViewById(R.id.txtDetail);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        ImageButton viewVideoBtn = (ImageButton) findViewById(R.id.btnView);

        posterUrl =  getIntent().getExtras().getString("poster");
        description = getIntent().getExtras().getString("description");
        videoUrl = getIntent().getExtras().getString("video_url");
        title = getIntent().getExtras().getString("title");

        Log.d("posterUrl",posterUrl);
        txtTitle.setText(title);
        txtDescription.setText(description);
        Picasso.with(this)
                .load(posterUrl).resize(250, 300)
                .into( banner);

        pDialog.dismiss();

        viewVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),VideoViewActivity.class);
                intent.putExtra("video_url", videoUrl);
                Log.d("videoUrl", posterUrl);
                startActivityForResult(intent,1);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_details, menu);
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

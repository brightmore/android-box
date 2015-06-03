package com.virtualLink.msiptv.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.EventHandler;

import java.lang.ref.WeakReference;

public class VideoActivity extends Activity implements SurfaceHolder.Callback,
        IVideoPlayer {
    public final static String TAG = "LibVLCAndroidSample/VideoActivity";

    public final static String LOCATION = "com.compdigitec.libvlcandroidsample.VideoActivity.location";
    private final static int VideoSizeChanged = -1;
    private final static int VideoLOading = 1;
    private final static int NOVideoLOading = 3;

    private String mFilePath;
    // display surface
    private SurfaceView mSurface;
    private SurfaceHolder holder;
    // media player
    private LibVLC libvlc;
    private int mVideoWidth;
    private int mVideoHeight;
    private String mCurName;
    private ProgressBar mProgressBar;


    private Handler mHandler = new MyHandler(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        // Receive path to play from intent
//        Intent intent = getIntent();
//        mFilePath = intent.getExtras().getString(LOCATION);
        mFilePath = "";
        Log.d(TAG, "Playing back " + mFilePath);

        mSurface = (SurfaceView) findViewById(R.id.surface);

        createProgressBar();
        createPlayer();

       // startUrlListActivity();
        startProcVideoState();
        //createProgressBar();
       // startProcVideoState();

    }

    private void createProgressBar() {
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProgressBar = new ProgressBar(this);
        mProgressBar.setVisibility(View.GONE);
        addContentView(mProgressBar, layoutParams);

    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        releasePlayer();
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    /**
     * **********
     * Surface
     * ***********
     */

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int format,
                               int width, int height) {
        if (libvlc != null)
            libvlc.attachSurface(holder.getSurface(), this);
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
    }

    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

//        if (screenAR < videoAR)
//            h = (int) (w / videoAR);
//        else
//            w = (int) (h * videoAR);

        // force surface buffer size
        holder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

    @Override
    public void setSurfaceSize(int width, int height, int visible_width,
                               int visible_height, int sar_num, int sar_den) {
        Message msg = Message.obtain(mHandler, VideoSizeChanged, width, height);
        msg.sendToTarget();
    }

    /**
     * **********
     * Player
     * ***********
     */

    private void createPlayer() {
        releasePlayer();
        try {

            // Create a new media player
            holder = mSurface.getHolder();
            holder.addCallback(this);
            libvlc = LibVLC.getInstance();
            //libvlc.setHardwareAcceleration(LibVLC.HW_ACCELERATION_DISABLED);
            libvlc.setHardwareAcceleration(libvlc.HW_ACCELERATION_AUTOMATIC);
            libvlc.setSubtitlesEncoding("");
            libvlc.setAout(LibVLC.AOUT_OPENSLES);
            libvlc.setTimeStretching(true);
            libvlc.setChroma("RV32");
            libvlc.setVerboseMode(true);
            LibVLC.restart(this);
            EventHandler.getInstance().addHandler(mHandler);
            holder.setFormat(PixelFormat.RGBX_8888);
            holder.setKeepScreenOn(true);
            // MediaList list = libvlc.getMediaList();
            // list.clear();
            // list.add(new Media(libvlc, LibVLC.PathToURI(media)), false);
            // libvlc.playIndex(0);
        } catch (Exception e) {
            Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        EventHandler.getInstance().removeHandler(mHandler);
        libvlc.stop();
        libvlc.detachSurface();
        holder.removeCallback(this);
        holder = null;
        libvlc.closeAout();
        libvlc.destroy();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                Bundle b = data.getExtras();
                mCurName = b.getString("name");
                libvlc.playMRL(b.getString("url"));
                break;
        }
    }

    void startUrlListActivity() {
        Intent intent = new Intent();
        intent.setClass(this, UrlListActivity.class);
        intent.putExtra("name", mCurName);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            startUrlListActivity();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void startProcVideoState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               while (true) {
                    try {
                        Thread.sleep(1000);
                        if (libvlc != null) {
                        //    mProgressBar.getHandler().post(new Runnable()
//                            mProgressBar.post(new Runnable(){
//                                public void run() {
//                                    int state = libvlc.getState();
//                                    Log.d("vlc", "test vlc state = " + state);
//                                    if (state == libvlc.libvlc_Buffering || state == libvlc.libvlc_Opening){
//
//                                        Message msg = new Message();
//                                        msg.what = VideoLOading;
//                                        mmmHandler.sendMessage(msg);
//                                    }
//                                       // mProgressBar.setVisibility(View.VISIBLE);
//                                    else
//                                        mProgressBar.setVisibility(View.INVISIBLE);
//                                }
//                            });
                            int state = libvlc.getState();
                                //    Log.d("vlc", "test vlc state = " + state);
                                   if (state == libvlc.libvlc_Buffering || state == libvlc.libvlc_Opening) {
                                       Message msg = new Message();
                                        msg.what = VideoLOading;
                                        mmmHandler.sendMessage(msg);
                                   }
                                   else {
                                       Message msg = new Message();
                                       msg.what = NOVideoLOading;
                                       mmmHandler.sendMessage(msg);

                                     //  Log.d("vlc2222222222222222222", "test 22222222222222222222vlc state = " + state);
                                   } //     mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
      /*  if (libvlc != null) {
            mProgressBar.getHandler().post(new Runnable() {

                @Override
                public void run() {
                    int state = libvlc.getState();
                    Log.d("vlc", "test vlc state = " + state);
                    if (state == libvlc.libvlc_Buffering || state == libvlc.libvlc_Opening)
                        mProgressBar.setVisibility(View.VISIBLE);
                    else
                        mProgressBar.setVisibility(View.INVISIBLE);
                }
            });

        }*/
    }
    //定义一个Handler
    private Handler mmmHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case VideoLOading:
                  //  Log.d("V111ideoLOading", "Video111111111111111111LOading vlc state = " + VideoLOading);
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case NOVideoLOading:
                mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private static class MyHandler extends Handler {
        private WeakReference<VideoActivity> mOwner;

        public MyHandler(VideoActivity owner) {
            mOwner = new WeakReference<VideoActivity>(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoActivity player = mOwner.get();

            // SamplePlayer events
            if (msg.what == VideoSizeChanged) {
                player.setSize(msg.arg1, msg.arg2);
                return;
            }
            // SamplePlayer events
            // Libvlc events
            Bundle b = msg.getData();
            switch (b.getInt("event")) {
                case EventHandler.MediaPlayerEndReached:
                    player.releasePlayer();
                    break;
                case EventHandler.MediaPlayerPlaying:
                case EventHandler.MediaPlayerPaused:
                case EventHandler.MediaPlayerStopped:
                    break;
                default:
                    break;
            }
        }
    }
}

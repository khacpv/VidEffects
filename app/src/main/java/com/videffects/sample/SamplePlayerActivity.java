package com.videffects.sample;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import com.sherazkhilji.sample.R;
import com.sherazkhilji.videffects.DuotoneEffect;
import com.sherazkhilji.videffects.GrainEffect;
import com.sherazkhilji.videffects.GreyScaleEffect;
import com.sherazkhilji.videffects.InvertColorsEffect;
import com.sherazkhilji.videffects.NoEffect;
import com.sherazkhilji.videffects.SaturationEffect;
import com.sherazkhilji.videffects.VignetteEffect;
import com.sherazkhilji.videffects.view.VideoSurfaceView;

public class SamplePlayerActivity extends Activity {

    private static final String TAG = "SamplePlayerActivity";

    protected Resources mResources;

    private VideoSurfaceView mVideoView = null;
    private MediaPlayer mMediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResources = getResources();
        mMediaPlayer = new MediaPlayer();

        try {
            // Load video file from SD Card
            // File dir = Environment
            // .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // File file = new File(dir,
            // "sample.mp4");
            // mMediaPlayer.setDataSource(file.getAbsolutePath());
            // -----------------------------------------------------------------------
            // Load video file from Assets directory
            AssetFileDescriptor afd = getAssets().openFd("vibbidi_01.mp4");
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                afd.getLength());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        // Initialize VideoSurfaceView using code
        // mVideoView = new VideoSurfaceView(this);
        // setContentView(mVideoView);
        // or
        setContentView(R.layout.activity_sampleplayer);
        mVideoView = (VideoSurfaceView) findViewById(R.id.mVideoSurfaceView);
        mVideoView.init(mMediaPlayer, new NoEffect());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();

        //resetSize();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //resetSize();
    }

    private void resetSize(){
        RelativeLayout.LayoutParams params =
            (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        float ratio = 1366 / 580;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                float height = size.y;
                float width = ratio * height;
                params.height = (int)height;
                params.width = (int) width;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                width = size.x;
                height = (1/ratio) * width;
                params.height = (int) height;
                params.width = (int)width;
                break;
        }

        mVideoView.setLayoutParams(params);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
    }

    public void onBtnClicked(View view) {
        int currentPos = mMediaPlayer.getCurrentPosition();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        switch (view.getId()) {
            case R.id.btn_noeffect:
                mVideoView.init(mMediaPlayer, new NoEffect());
                break;
            case R.id.btn_vintage:
                mVideoView.init(mMediaPlayer, new VignetteEffect(0.5f));
                break;
            case R.id.btn_invert:
                mVideoView.init(mMediaPlayer, new InvertColorsEffect());
                break;
            case R.id.btn_satuation:
                mVideoView.init(mMediaPlayer, new SaturationEffect(0.2f));
                break;
            case R.id.btn_grain:
                mVideoView.init(mMediaPlayer, new GrainEffect(0.2f));
                break;
            case R.id.btn_duotone:
                mVideoView.init(mMediaPlayer,
                    new DuotoneEffect(Color.parseColor("#3498DB"), Color.YELLOW));
                break;
            case R.id.btn_grayscale:
                mVideoView.init(mMediaPlayer, new GreyScaleEffect());
                break;
            default:
                break;
        }
        mMediaPlayer.seekTo(currentPos);
        mMediaPlayer.start();
    }
}

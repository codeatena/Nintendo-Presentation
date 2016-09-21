package com.virtusventures.nintendopresentation;

import android.app.Presentation;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by mac on 9/21/16.
 */
public class VideoPresentation extends Presentation {

    VideoView videoView;

    public VideoPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the custom layout
        setContentView(R.layout.presentation_video);

        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                // play looping video
                videoView.setVideoPath("");
                videoView.start();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });


    }

    public void playVideo(String videoPath)
    {
        videoView.setVideoPath(videoPath);
        videoView.start();
    }
}

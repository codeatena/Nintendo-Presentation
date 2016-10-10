package com.virtusventures.nintendopresentation;

import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import static android.media.MediaPlayer.MEDIA_ERROR_IO;
import static android.media.MediaPlayer.MEDIA_ERROR_MALFORMED;
import static android.media.MediaPlayer.MEDIA_ERROR_TIMED_OUT;
import static android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED;

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
                videoView.requestFocus();
                videoView.setVideoPath("/mnt/external_sd/loopvideo.mp4");
                videoView.start();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");

                String errorMsg = "";
                switch (extra)
                {
                    case MEDIA_ERROR_IO:
                        errorMsg = "File or network related operation errors. ";
                        break;
                    case MEDIA_ERROR_MALFORMED:
                        errorMsg = "Bitstream is not conforming to the related coding standard or file spec. ";
                        break;
                    case MEDIA_ERROR_UNSUPPORTED:
                        errorMsg = "Bitstream is conforming to the related coding standard or file spec, but the media framework does not support the feature. ";
                        break;
                    case MEDIA_ERROR_TIMED_OUT:
                        errorMsg = "Some operation takes too long to complete, usually more than 3-5 seconds. ";
                        break;
                }

                new AlertDialog.Builder(VideoPresentation.this.getContext())
                        .setTitle("Error")
                        .setMessage(errorMsg)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();


                return true;
            }
        });

    }

    public void playVideo(String videoPath)
    {
        videoView.requestFocus();
        videoView.setVideoPath(videoPath);
        videoView.start();
    }
}

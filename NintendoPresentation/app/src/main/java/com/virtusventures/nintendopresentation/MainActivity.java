package com.virtusventures.nintendopresentation;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaRouter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    private MediaRouter mMediaRouter;
    // Active Presentation, set to null if no secondary screen is enabled
    private VideoPresentation mPresentation;

    private final MediaRouter.SimpleCallback mMediaRouterCallback =
            new MediaRouter.SimpleCallback() {

                // BEGIN_INCLUDE(SimpleCallback)
                /**
                 * A new route has been selected as active. Disable the current
                 * route and enable the new one.
                 */
                @Override
                public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
                    updatePresentation();
                }

                /**
                 * The route has been unselected.
                 */
                @Override
                public void onRouteUnselected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
                    updatePresentation();

                }

                /**
                 * The route's presentation display has changed. This callback
                 * is called when the presentation has been activated, removed
                 * or its properties have changed.
                 */
                @Override
                public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo info) {
                    updatePresentation();
                }
                // END_INCLUDE(SimpleCallback)
            };

    private final DialogInterface.OnDismissListener mOnDismissListener =
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialog == mPresentation) {
                        mPresentation = null;
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mMediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // BEGIN_INCLUDE(addCallback)
        // Register a callback for all events related to live video devices
        mMediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, mMediaRouterCallback);
        // END_INCLUDE(addCallback)

        // Update the displays based on the currently active routes
        updatePresentation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // BEGIN_INCLUDE(onPause)
        // Stop listening for changes to media routes.
        mMediaRouter.removeCallback(mMediaRouterCallback);
        // END_INCLUDE(onPause)
    }

    @Override
    protected void onStop() {
        super.onStop();

        // BEGIN_INCLUDE(onStop)
        // Dismiss the presentation when the activity is not visible.
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
        // BEGIN_INCLUDE(onStop)
    }

    public void onCharacter(View v) {

        if (mPresentation != null)
            mPresentation.playVideo("mnt/extsd/video1.mp4");

    }

    public void onHunter(View v) {

        if (mPresentation != null)
            mPresentation.playVideo("mnt/extsd/video2.mp4");

    }

    private void updatePresentation() {

        // BEGIN_INCLUDE(updatePresentationInit)
        // Get the selected route for live video
        MediaRouter.RouteInfo selectedRoute = mMediaRouter.getSelectedRoute(
                MediaRouter.ROUTE_TYPE_LIVE_VIDEO);

        // Get its Display if a valid route has been selected
        Display selectedDisplay = null;
        if (selectedRoute != null) {
            selectedDisplay = selectedRoute.getPresentationDisplay();
        }
        // END_INCLUDE(updatePresentationInit)

        // BEGIN_INCLUDE(updatePresentationDismiss)
        /*
         * Dismiss the current presentation if the display has changed or no new
         * route has been selected
         */
        if (mPresentation != null && mPresentation.getDisplay() != selectedDisplay) {
            mPresentation.dismiss();
            mPresentation = null;

        }
        // END_INCLUDE(updatePresentationDismiss)

        // BEGIN_INCLUDE(updatePresentationNew)
        /*
         * Show a new presentation if the previous one has been dismissed and a
         * route has been selected.
         */
        if (mPresentation == null && selectedDisplay != null) {

            // Initialise a new Presentation for the Display
            mPresentation = new VideoPresentation(this, selectedDisplay);
            mPresentation.setOnDismissListener(mOnDismissListener);

            // Try to show the presentation, this might fail if the display has
            // gone away in the mean time
            try {
                mPresentation.show();
                // play looping video
                mPresentation.playVideo("mnt/extsd/loopvideo.mp4");

            } catch (WindowManager.InvalidDisplayException ex) {
                // Couldn't show presentation - display was already removed
                mPresentation = null;
            }
        }
        // END_INCLUDE(updatePresentationNew)

    }
}

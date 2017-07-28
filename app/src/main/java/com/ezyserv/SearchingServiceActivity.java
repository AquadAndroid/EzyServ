package com.ezyserv;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.ezyserv.custome.CustomActivity;
import com.skyfishjy.library.RippleBackground;

public class SearchingServiceActivity extends CustomActivity {
    private static final int SPLASH_DURATION_MS = 3000;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_service);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                rippleBackground.stopRippleAnimation();
                finish();
            }
        }, SPLASH_DURATION_MS);

    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}

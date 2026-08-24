package com.android.support;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.ViewGroup;
import android.graphics.Color;
import android.util.Log;

/**
 * Sparta Menu launcher activity.
 * Shows a native loading screen and starts the mod menu service.
 * If the game's activity isn't found (standalone install), we still
 * start the overlay so users can verify the menu works.
 */
public class MainActivity extends Activity {
    private static final String TAG = "SpartaMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // simple dark splash while things boot
        android.widget.LinearLayout root = new android.widget.LinearLayout(this);
        root.setOrientation(android.widget.LinearLayout.VERTICAL);
        root.setGravity(android.view.Gravity.CENTER);
        root.setBackgroundColor(Color.parseColor("#0a0e1a"));
        TextView tv = new TextView(this);
        tv.setText("⚔️\n\nSparta Menu");
        tv.setTextSize(22);
        tv.setTextColor(Color.parseColor("#b39dff"));
        tv.setGravity(android.view.Gravity.CENTER);
        root.addView(tv);
        setContentView(root);

        // start overlay via Loader/Main; catch anything and report on screen
        new Thread(() -> {
            String error = null;
            try {
                Loader.Start(this);
            } catch (Throwable t) {
                Log.e(TAG, "Loader.Start failed", t);
                error = t.getClass().getSimpleName() + ": " + t.getMessage();
            }
            final String err = error;
            runOnUiThread(() -> {
                if (err != null) {
                    TextView etv = new TextView(this);
                    etv.setText("Overlay failed to start:\n" + err +
                            "\n\n(This template expects to be injected into a target "
                            + "app. Standalone mode only shows this test screen.)");
                    etv.setTextColor(Color.parseColor("#ff5c7a"));
                    etv.setTextSize(13);
                    etv.setPadding(40, 20, 40, 0);
                    ((android.view.ViewGroup) root).addView(etv);
                } else {
                    ((TextView) root.getChildAt(0)).append(
                            "\n\n✓ Overlay started.\nOpen a target app to see the menu.");
                }
            });
        }).start();
    }
}

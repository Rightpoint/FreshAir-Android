package com.raizlabs.freshair.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.raizlabs.freshair.FreshAir;
import com.raizlabs.freshair.FreshAirLog;
import com.raizlabs.freshair.ReleaseInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.showOnboarding(MainActivity.this);
            }
        });

        findViewById(R.id.activity_main_onboarding_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.clearOnboardingVersion();
            }
        });

        findViewById(R.id.activity_main_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream releaseInput = getResources().openRawResource(R.raw.release_notes);
                String releaseString = MainActivity.readStream(releaseInput);
                try {
                    JSONObject releaseJson = new JSONObject(releaseString);
                    ReleaseInfo release = ReleaseInfo.fromJson(releaseJson);
                    FreshAir.showUpdatePrompt(release);
                } catch (JSONException e) {
                }
//                FreshAir.showUpdatePrompt(50, false);
            }
        });

        findViewById(R.id.activity_main_forcedUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.showUpdatePrompt(50, true);
            }
        });

        findViewById(R.id.activity_main_update_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.clearUpdatePromptVersion();
            }
        });

        findViewById(R.id.activity_main_delayedStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                    }
                }, 3000);
            }
        });
    }

    /**
     * Utility method for pulling plain text from an InputStream object
     */
    public static String readStream(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            FreshAirLog.e(MainActivity.class.getSimpleName(), "Error reading stream", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }

            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}

package com.raizlabs.freshair.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.raizlabs.freshair.FreshAir;

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
                FreshAir.clearOnboardingVersion(MainActivity.this);
            }
        });

        findViewById(R.id.activity_main_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.showUpdatePrompt(false);
            }
        });

        findViewById(R.id.activity_main_forcedUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.showUpdatePrompt(true);
            }
        });
    }
}

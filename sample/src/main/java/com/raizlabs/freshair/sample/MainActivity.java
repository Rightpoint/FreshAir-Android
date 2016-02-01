package com.raizlabs.freshair.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.raizlabs.freshair.FeatureInfo;
import com.raizlabs.freshair.FreshAir;
import com.raizlabs.freshair.ReleaseInfo;

public class MainActivity extends FragmentActivity {

    private static final ReleaseInfo RELEASE_INFO = new ReleaseInfo.Builder()
            .addFeature(
                    new FeatureInfo.Builder()
                            .setImageResource(R.mipmap.ic_launcher)
                            .setTitleResource(R.string.Feature1_Title)
                            .setDescriptionResource(R.string.Feature1_Description)
                            .build()
            )
            .addFeature(
                    new FeatureInfo.Builder()
                            .setImageResource(R.drawable.feature2)
                            .setTitleResource(R.string.Feature2_Title)
                            .setDescriptionResource(R.string.Feature2_Description)
                            .build()
            )
            .addFeature(
                    new FeatureInfo.Builder()
                            .setImageResource(R.drawable.feature3)
                            .setTitleResource(R.string.Feature3_Title)
                            .setDescriptionResource(R.string.Feature3_Description)
                            .build()
            )
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.activity_main_onboarding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreshAir.showOnboarding(MainActivity.this, RELEASE_INFO);
            }
        });
    }
}

package com.raizlabs.freshair.sample;

import android.app.Application;

import com.raizlabs.freshair.FeatureInfo;
import com.raizlabs.freshair.FreshAir;
import com.raizlabs.freshair.LogLevel;
import com.raizlabs.freshair.ReleaseNotes;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FreshAir.initialize(this);
        FreshAir.setLogLevel(LogLevel.ALL);
        FreshAir.setReleaseNotes(new ReleaseNotes.Builder()
                .addFeature(
                        new FeatureInfo.Builder()
                                .setImageResource(R.mipmap.ic_launcher)
                                .setTitleResource(R.string.Feature1_Title)
                                .setDescriptionResource(R.string.Feature1_Description)
                )
                .addFeature(
                        new FeatureInfo.Builder()
                                .setImageResource(R.drawable.feature2)
                                .setTitleResource(R.string.Feature2_Title)
                                .setDescriptionResource(R.string.Feature2_Description)
                )
                .addFeature(
                        new FeatureInfo.Builder()
                                .setImageResource(R.drawable.feature3)
                                .setTitleResource(R.string.Feature3_Title)
                                .setDescriptionResource(R.string.Feature3_Description)
                )
                        // Sometimes handy for minor updates: see docs
                .setVersionCode(1));
    }
}

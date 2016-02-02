package com.raizlabs.freshair.sample;

import android.app.Application;

import com.raizlabs.freshair.FeatureInfo;
import com.raizlabs.freshair.FreshAir;
import com.raizlabs.freshair.FreshAirLog;
import com.raizlabs.freshair.ReleaseInfo;

public class SampleApplication extends Application {

    public static final ReleaseInfo RELEASE_INFO = new ReleaseInfo.Builder()
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
            .setVersionCode(1);

    @Override
    public void onCreate() {
        super.onCreate();

        FreshAir.init(this);
        FreshAirLog.setLogLevel(FreshAirLog.LogLevel.ALL);
    }
}

package com.raizlabs.freshair;

import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SimpleReleaseInfo implements ReleaseInfo {

    private List<FeatureInfo> features;

    public SimpleReleaseInfo() {
        setFeatures(new LinkedList<FeatureInfo>());
    }

    @Override
    public List<FeatureInfo> getFeatures() {
        return features;
    }

    public SimpleReleaseInfo setFeatures(List<FeatureInfo> features) {
        this.features = features;
        return this;
    }

    public SimpleReleaseInfo addFeature(FeatureInfo feature) {
        this.features.add(feature);
        return this;
    }

    public SimpleReleaseInfo addFeatures(Collection<FeatureInfo> features) {
        this.features.addAll(features);
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(features.size());
        for (FeatureInfo info : features) {
            dest.writeString(info.getClass().getName());
            dest.writeParcelable(info, 0);
        }
    }

    public static final Creator<SimpleReleaseInfo> CREATOR = new Creator<SimpleReleaseInfo>() {
        @Override
        public SimpleReleaseInfo createFromParcel(Parcel source) {
            final int featureCount = source.readInt();
            List<FeatureInfo> features = new ArrayList<>(featureCount);

            for (int i = 0; i < featureCount; i++) {
                try {
                    Class featureClass = Class.forName(source.readString());
                    FeatureInfo feature = source.readParcelable(featureClass.getClassLoader());
                    features.add(feature);
                } catch (ClassNotFoundException e) {
                    Log.e(FreshAir.LOG_TAG, "Error unparceling " + FeatureInfo.class.getSimpleName() + " for " + SimpleReleaseInfo.class.getSimpleName(), e);
                    return null;
                }
            }

            SimpleReleaseInfo releaseInfo = new SimpleReleaseInfo();
            releaseInfo.setFeatures(features);
            return releaseInfo;
        }

        @Override
        public SimpleReleaseInfo[] newArray(int size) {
            return new SimpleReleaseInfo[0];
        }
    };
}

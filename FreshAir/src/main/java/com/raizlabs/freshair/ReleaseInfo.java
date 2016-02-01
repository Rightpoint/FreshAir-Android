package com.raizlabs.freshair;

import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public interface ReleaseInfo extends Parcelable {

    List<FeatureInfo> getFeatures();

    class Builder {

        private List<FeatureInfo> features;

        public Builder() {
            features = new LinkedList<>();
        }

        public Builder addFeature(FeatureInfo featureInfo) {
            features.add(featureInfo);
            return this;
        }

        public ReleaseInfo build() {
            return new SimpleReleaseInfo()
                    .setFeatures(features);
        }
    }
}

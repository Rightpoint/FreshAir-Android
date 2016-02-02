package com.raizlabs.freshair;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public interface ReleaseInfo extends Parcelable {

    int getVersionCode();
    List<FeatureInfo> getFeatures();

    class Builder implements ReleaseInfo {

        private int versionCode;
        private List<FeatureInfo> features;

        public Builder() {
            setFeatures(new LinkedList<FeatureInfo>());
        }

        @Override
        public int getVersionCode() {
            return versionCode;
        }

        public Builder setVersionCode(int versionCode) {
            this.versionCode = versionCode;
            return this;
        }

        @Override
        public List<FeatureInfo> getFeatures() {
            return features;
        }

        public Builder setFeatures(List<FeatureInfo> features) {
            this.features = features;
            return this;
        }

        public Builder addFeature(FeatureInfo feature) {
            this.features.add(feature);
            return this;
        }

        public Builder addFeatures(Collection<FeatureInfo> features) {
            this.features.addAll(features);
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(versionCode);
            dest.writeInt(features.size());
            for (FeatureInfo info : features) {
                dest.writeString(info.getClass().getName());
                dest.writeParcelable(info, 0);
            }
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                Builder builder = new Builder();
                builder.setVersionCode(source.readInt());

                final int featureCount = source.readInt();
                List<FeatureInfo> features = new ArrayList<>(featureCount);

                for (int i = 0; i < featureCount; i++) {
                    try {
                        Class featureClass = Class.forName(source.readString());
                        FeatureInfo feature = source.readParcelable(featureClass.getClassLoader());
                        features.add(feature);
                    } catch (ClassNotFoundException e) {
                        FreshAirLog.e("Error unparceling " + FeatureInfo.class.getSimpleName() + " for " + Builder.class.getSimpleName(), e);
                        return null;
                    }
                }

                builder.setFeatures(features);
                return builder;
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[0];
            }
        };
    }
}

package com.raizlabs.freshair;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Interface which defines the information for release notes in a new version. See
 * {@link ReleaseNotes.Builder} for easy creation.
 */
public interface ReleaseNotes extends Parcelable {

    /**
     * Gets the version code these notes are associated with. This is what is used to determine whether this info has
     * been shown or not.
     * @return The version code.
     */
    int getVersionCode();

    /**
     * Gets a list of {@link FeatureInfo} that are provided by this version.
     * @return The new {@link FeatureInfo} for this version.
     */
    List<FeatureInfo> getFeatures();

    /**
     * Class which helps to easily construct {@link ReleaseNotes}s.
     */
    class Builder implements ReleaseNotes {

        private int versionCode;
        private List<FeatureInfo> features;

        /**
         * Constructs a new builder, assuming that the version code is the current app version.
         */
        public Builder() {
            setVersionCode(Math.min(FreshAir.getCurrentApplicationVersion(), 0));
            setFeatures(new LinkedList<FeatureInfo>());
        }

        @Override
        public int getVersionCode() {
            return versionCode;
        }

        /**
         * Sets the version that these notes are associated with. Can be handy to override in minor updates
         * where you would still like to show the previous update release notes if the user still hasn't seen them.
         * @param versionCode The version code the notes are associated with.
         * @return This {@link ReleaseNotes.Builder} for chaining method calls.
         */
        public Builder setVersionCode(int versionCode) {
            this.versionCode = versionCode;
            return this;
        }

        @Override
        public List<FeatureInfo> getFeatures() {
            return features;
        }

        /**
         * Sets the list of features provided by this version.
         * @param features The list of features provided by this version.
         * @return This {@link ReleaseNotes.Builder} for chaining method calls.
         */
        public Builder setFeatures(List<FeatureInfo> features) {
            this.features = features;
            return this;
        }

        /**
         * Adds the given feature to the list of features provided by this version.
         * @param feature The feature to add.
         * @return This {@link ReleaseNotes.Builder} for chaining method calls.
         */
        public Builder addFeature(FeatureInfo feature) {
            this.features.add(feature);
            return this;
        }

        /**
         * Adds the given features to the list of features provided by this version.
         * @param features The features to add.
         * @return This {@link ReleaseNotes.Builder} for chaining method calls.
         */
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

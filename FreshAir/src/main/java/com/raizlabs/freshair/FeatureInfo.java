package com.raizlabs.freshair;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Interface which defines the information about a feature. See {@link com.raizlabs.freshair.FeatureInfo.Builder} for
 * easy creation.
 */
public interface FeatureInfo extends Parcelable {

    /**
     * Gets the string to display as the title of the feature.
     * @param context A context for access to resources.
     * @return The string to display as the title of the feature.
     */
    String getTitle(Context context);

    /**
     * Gets the string to display as the description of the feature.
     * @param context A context for access to resources.
     * @return The string to display as the description of the feature.
     */
    String getDescription(Context context);

    /**
     * Populates the given {@link ImageView} with the image for this feature.
     * @param view The view to display the image in.
     */
    void populateImage(ImageView view);

    /**
     * Class which helps to easily construct {@link FeatureInfo}s.
     */
    class Builder implements FeatureInfo {

        private int titleRes;
        private int descriptionRes;
        private int imageRes;

        /**
         * Sets a string resource to be used as the title of the feature.
         * @param titleRes The resource ID of the string to use as the title.
         * @return This {@link com.raizlabs.freshair.FeatureInfo.Builder} for chaining method calls.
         */
        public Builder setTitleResource(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        @Override
        public String getTitle(Context context) {
            return context.getString(titleRes);
        }

        /**
         * Sets a string resource to be used as the description of the feature.
         * @param descriptionRes The resource ID of the string to use as the description.
         * @return This {@link com.raizlabs.freshair.FeatureInfo.Builder} for chaining method calls.
         */
        public Builder setDescriptionResource(int descriptionRes) {
            this.descriptionRes = descriptionRes;
            return this;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(descriptionRes);
        }

        /**
         * Sets a drawable resource to be used as the image of the feature.
         * @param imageRes The resource ID of the bitmap to use as the image.
         * @return This {@link com.raizlabs.freshair.FeatureInfo.Builder} for chaining method calls.
         */
        public Builder setImageResource(int imageRes) {
            this.imageRes = imageRes;
            return this;
        }

        @Override
        public void populateImage(ImageView view) {
            view.setImageResource(imageRes);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(titleRes);
            dest.writeInt(descriptionRes);
            dest.writeInt(imageRes);
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                Builder builder = new Builder();
                builder.setTitleResource(source.readInt());
                builder.setDescriptionResource(source.readInt());
                builder.setImageResource(source.readInt());

                return builder;
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[0];
            }
        };
    }
}

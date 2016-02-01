package com.raizlabs.freshair;

import android.content.Context;
import android.os.Parcelable;
import android.widget.ImageView;

public interface FeatureInfo extends Parcelable {

    String getTitle(Context context);
    String getDescription(Context context);

    void populateImage(ImageView view);

    class Builder {

        private int titleRes;
        private int descriptionRes;
        private int imageRes;

        public Builder setTitleResource(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        public Builder setDescriptionResource(int descriptionRes) {
            this.descriptionRes = descriptionRes;
            return this;
        }

        public Builder setImageResource(int imageRes) {
            this.imageRes = imageRes;
            return this;
        }

        public FeatureInfo build() {
            return new SimpleFeatureInfo()
                    .setTitleResource(titleRes)
                    .setDescriptionResource(descriptionRes)
                    .setImageResource(imageRes);
        }
    }
}

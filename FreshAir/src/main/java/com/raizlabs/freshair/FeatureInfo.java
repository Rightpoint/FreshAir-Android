package com.raizlabs.freshair;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public interface FeatureInfo extends Parcelable {

    String getTitle(Context context);
    String getDescription(Context context);

    void populateImage(ImageView view);

    class Builder implements FeatureInfo {

        private int titleRes;
        private int descriptionRes;
        private int imageRes;

        public Builder setTitleResource(int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        @Override
        public String getTitle(Context context) {
            return context.getString(titleRes);
        }

        public Builder setDescriptionResource(int descriptionRes) {
            this.descriptionRes = descriptionRes;
            return this;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(descriptionRes);
        }

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

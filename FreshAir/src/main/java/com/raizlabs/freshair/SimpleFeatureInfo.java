package com.raizlabs.freshair;

import android.content.Context;
import android.os.Parcel;
import android.widget.ImageView;

public class SimpleFeatureInfo implements FeatureInfo {

    private int titleRes;
    private int descriptionRes;
    private int imageRes;

    public SimpleFeatureInfo setTitleResource(int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(titleRes);
    }

    public SimpleFeatureInfo setDescriptionResource(int descriptionRes) {
        this.descriptionRes = descriptionRes;
        return this;
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(descriptionRes);
    }

    public SimpleFeatureInfo setImageResource(int imageRes) {
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

    public static final Creator<SimpleFeatureInfo> CREATOR = new Creator<SimpleFeatureInfo>() {
        @Override
        public SimpleFeatureInfo createFromParcel(Parcel source) {
            SimpleFeatureInfo info = new SimpleFeatureInfo();
            info.setTitleResource(source.readInt());
            info.setDescriptionResource(source.readInt());
            info.setImageResource(source.readInt());

            return info;
        }

        @Override
        public SimpleFeatureInfo[] newArray(int size) {
            return new SimpleFeatureInfo[0];
        }
    };
}

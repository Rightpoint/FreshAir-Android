package com.raizlabs.freshair;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReleaseInfo implements Parcelable {

    public static final int MINIMUM_VERSION_NONE = -1;

    public static ReleaseInfo fromJson(JSONObject json) throws JSONException {
        ReleaseInfo releaseInfo = new ReleaseInfo();
        releaseInfo.releases = Release.fromJson(json.getJSONArray("releases"));
        releaseInfo.forcedMinimumVersion = json.optInt("minimumVersion", MINIMUM_VERSION_NONE);

        return releaseInfo;
    }

    private int forcedMinimumVersion;
    private List<Release> releases;

    public int getForcedMinimumVersion() {
        return forcedMinimumVersion;
    }

    public void setForcedMinimumVersion(int forcedMinimumVersion) {
        this.forcedMinimumVersion = forcedMinimumVersion;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(forcedMinimumVersion);

        dest.writeTypedList(releases);
    }

    public static final Creator<ReleaseInfo> CREATOR = new Creator<ReleaseInfo>() {
        @Override
        public ReleaseInfo createFromParcel(Parcel source) {
            ReleaseInfo releaseInfo = new ReleaseInfo();
            releaseInfo.forcedMinimumVersion = source.readInt();

            releaseInfo.releases = new ArrayList<>();
            source.readTypedList(releaseInfo.releases, Release.CREATOR);

            return releaseInfo;
        }

        @Override
        public ReleaseInfo[] newArray(int size) {
            return new ReleaseInfo[size];
        }
    };
}

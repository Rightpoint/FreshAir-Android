package com.raizlabs.freshair;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an individual release.
 */
public class Release implements Parcelable {

    /**
     * Value for indicating there is no minimum version specified.
     */
    public static final int MINIMUM_VERSION_NONE = -1;

    /**
     * Parses a list of {@link Release} from a {@link JSONArray} as specified by the current schema.
     * @param json The JSON to parse.
     * @return The list of {@link Release}s that were parsed.
     * @throws JSONException If there was an error parsing the JSON.
     */
    public static List<Release> fromJson(JSONArray json) throws JSONException {
        final int releaseCount = json.length();
        List<Release> releases = new ArrayList<>(releaseCount);
        for (int i = 0; i < releaseCount; i++) {
            Release release = Release.fromJson(json.getJSONObject(i));
            releases.add(release);
        }

        return releases;
    }

    /**
     * Parses an individual {@link Release} from a {@link JSONObject} as specified by the current schema.
     * @param json The JSON to parse.
     * @return The {@link Release} that was parsed.
     * @throws JSONException If there was an error parsing the JSON.
     */
    public static Release fromJson(JSONObject json) throws JSONException {
        Release release = new Release();
        release.setVersionCode(json.getInt("version"));
        release.setMinimumSystemVersion(json.optInt("minimumSystemVersion", MINIMUM_VERSION_NONE));

        return release;
    }

    private int versionCode;
    private int minimumSystemVersion;

    /**
     * @return The version code of this release.
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     * Sets the version code for this release.
     * @param versionCode The version code to set.
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return The minimum system version (API Level) supported by this release.
     */
    public int getMinimumSystemVersion() {
        return minimumSystemVersion;
    }

    /**
     * Sets the minimum system version (API Level) supported by this release.
     * @param minimumSystemVersion The minimum supported system version.
     */
    public void setMinimumSystemVersion(int minimumSystemVersion) {
        this.minimumSystemVersion = minimumSystemVersion;
    }

    /**
     * @return True if the current device satisfies the constraints of this {@link Release}.
     */
    public boolean isApplicable() {
        return (minimumSystemVersion == MINIMUM_VERSION_NONE ||
                minimumSystemVersion < Build.VERSION.SDK_INT);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(versionCode);
        dest.writeInt(minimumSystemVersion);
    }

    public static final Creator<Release> CREATOR = new Creator<Release>() {
        @Override
        public Release createFromParcel(Parcel source) {
            Release release = new Release();
            release.setVersionCode(source.readInt());
            release.setMinimumSystemVersion(source.readInt());
            return release;
        }

        @Override
        public Release[] newArray(int size) {
            return new Release[size];
        }
    };
}

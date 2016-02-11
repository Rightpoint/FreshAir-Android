package com.raizlabs.freshair;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents a set of available releases and their requirements.
 */
public class ReleaseInfo implements Parcelable {

    /**
     * Value for indicating there is no minimum version specified.
     */
    public static final int MINIMUM_VERSION_NONE = -1;

    /**
     * Parses a {@link ReleaseInfo} from a {@link JSONObject} as specified by the current schema.
     * @param json The JSON to parse.
     * @return The {@link ReleaseInfo} that was parsed.
     * @throws JSONException If there was an error parsing the JSON.
     */
    public static ReleaseInfo fromJson(JSONObject json) throws JSONException {
        ReleaseInfo releaseInfo = new ReleaseInfo();
        releaseInfo.releases = Release.fromJson(json.getJSONArray("releases"));
        releaseInfo.forcedMinimumVersion = json.optInt("minimumVersion", MINIMUM_VERSION_NONE);

        return releaseInfo;
    }

    private int forcedMinimumVersion;
    private List<Release> releases;

    /**
     * Gets the forced minimum version. Anything lower than this is no longer permitted to run and users must upgrade
     * to at least this version to continue using the app.
     * @return The forced minimum version.
     */
    public int getForcedMinimumVersion() {
        return forcedMinimumVersion;
    }

    /**
     * Sets the forced minimum version. Anything lower will not be permitted to run.
     * @see #getForcedMinimumVersion()
     * @param forcedMinimumVersion The forced minimum version.
     */
    public void setForcedMinimumVersion(int forcedMinimumVersion) {
        this.forcedMinimumVersion = forcedMinimumVersion;
    }

    /**
     * @return A list of all available releases.
     */
    public List<Release> getReleases() {
        return releases;
    }

    /**
     * Sets the list of available releases for this app.
     * @param releases The set of releases which are available.
     */
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

package com.raizlabs.freshair;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class which requests {@link ReleaseInfo} from an endpoint.
 */
class ReleaseInfoRequest {

    private JsonRequest jsonRequest;

    /**
     * Constructs a {@link ReleaseInfoRequest} that requests the info from the given URL.
     * @param url The url to fetch {@link ReleaseInfo} data from.
     */
    public ReleaseInfoRequest(String url) {
        jsonRequest = new JsonRequest(url);
    }

    /**
     * Executes the request and returns the {@link ReleaseInfo}. This performs the network operations synchronously.
     * @return The fetched {@link ReleaseInfo}.
     */
    public ReleaseInfo run() {
        JSONObject json = jsonRequest.run();
        try {
            return ReleaseInfo.fromJson(json);
        } catch (JSONException e) {
            FreshAirLog.e("Error parsing ReleaseInfo from: " + jsonRequest.getUrl(), e);
            return null;
        }
    }
}

package com.raizlabs.freshair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class which allows accessing of JSON data from an endpoint.
 */
class JsonRequest {

    private final String url;

    /**
     * Constructs a {@link JsonRequest} that requests JSON from the given URL.
     * @param url The url to fetch JSON from.
     */
    public JsonRequest(String url) {
        this.url = url;

    }

    /**
     * @return The url that this requests points to.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Executes the request and returns the JSON data. This performs the network operations synchronously.
     * @return The fetched JSON data.
     */
    public JSONObject run() {
        try {
            URL url = new URL(JsonRequest.this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream responseStream = connection.getInputStream();
            String response = Utils.readStream(responseStream);

            return new JSONObject(response);
        } catch (java.io.IOException | JSONException e) {
            FreshAirLog.e("Error executing JSONRequest", e);
            return null;
        }
    }
}

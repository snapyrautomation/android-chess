package com.snapyr.sdk;

import android.util.Log;

import com.snapyr.analytics.ConnectionFactory;
import java.net.HttpURLConnection;
import java.io.IOException;
import android.util.Base64;

public class SnapyrConnectionFactory extends ConnectionFactory {

    private String authorizationHeader(String writeKey) {
        return "Basic " + Base64.encodeToString((writeKey + ":").getBytes(), Base64.NO_WRAP);
    }

    /**
     * Return a {@link HttpURLConnection} that writes batched payloads to {@code
     * https://api.segment.io/v1/import}.
     */
    public HttpURLConnection upload(String writeKey) throws IOException {
        HttpURLConnection connection = openConnection("https://api.segment.io/v1/import");
        connection.setRequestProperty("Authorization", authorizationHeader(writeKey));
        // SNAPYR: Disabling gzip cause currently snapyr endpoint doesn't support it
        // connection.setRequestProperty("Content-Encoding", "gzip");
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        return connection;
    }

    /**
     * Redirects analytics to Snapyr
     */
    protected HttpURLConnection openConnection(String url) throws IOException {
        Log.i("Snapyr", "Returning Snapyr connection!");
        return super.openConnection("https://dev-engine.snapyr.com/v1/batch");
    }
}

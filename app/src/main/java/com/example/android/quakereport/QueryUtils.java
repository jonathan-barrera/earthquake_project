package com.example.android.quakereport;

import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = "QueryUtils.java";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractEarthquakes(String jsonResponseString) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Convert the String JSON into a JSONObject
            JSONObject sampleEarthquakeData = new JSONObject (jsonResponseString);

            // Extract the "features" object from the JSONObject
            JSONArray features = sampleEarthquakeData.getJSONArray("features");

            // Loop through each feature in the array
            for (int i = 0; i < features.length(); i++) {
                // Extract each "feature" JSONObject
                JSONObject feature = features.getJSONObject(i);
                // Extract the "properties" JSONObject
                JSONObject properties = feature.getJSONObject("properties");
                // Extract the "magnitude" decimal value and convert to String
                double magnitude = properties.getDouble("mag");
                // Extract the "place" String value
                String location = properties.getString("place");
                // Extract the "time" int value and convert to String
                long unixTimeDate = properties.getLong("time");
                // Extract the "url" String value
                String Url = properties.getString("url");
                // Create a new Earthquake object to store this data
                Earthquake currentEarthquake = new Earthquake(location, magnitude, unixTimeDate, Url);
                // Add the new Earthquake object to the ArrayList
                earthquakes.add(currentEarthquake);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /** Query the USGS dataset and get the Earthquakes data */
    public static List<Earthquake> earthquakeDataSet(String requestUrl){
        Log.v("EarthquakeActivity.java", "earthquakeDataSet activated");
        // Create URL object
        URL url = createURL(requestUrl);

        // Perform HTTP Request to the URL and get a JSONObject back
        String jsonResponseString = null;
        try {
            jsonResponseString = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream");
        }

        // Extract relevant fields to create a List of Earthquake objects
        List<Earthquake> earthquakesList = extractEarthquakes(jsonResponseString);

        return earthquakesList;
    }

    /** Convert the String url into a URL url */
    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /** Take the given URL and make an HTTP request, return a String. */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200)
            // Then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /** Convert the InputStream into a String */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }


}
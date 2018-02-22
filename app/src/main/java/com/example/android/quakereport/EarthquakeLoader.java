package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by jonathanbarrera on 2/16/18.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        // Don't perform the task if the first url is empty or null
        if (mUrl == null){
            return null;
        }
        // Perform the HTTP Request for earthquakes data and process the response.
        List<Earthquake> earthquakes = QueryUtils.earthquakeDataSet(mUrl);

        return earthquakes;
    }
}
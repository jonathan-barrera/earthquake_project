package com.example.android.quakereport;

/**
 * Created by jonathanbarrera on 2/13/18.
 * {@link Earthquake} contains information regarding a single earthquake incident.
 */

public class Earthquake {

    /** Location of the earthquake. */
    private String mLocation;

    /** The magnitude of the earthquake. */
    private double mMagnitude;

    /** The date the earthquake occurred. */
    private Long mUnixDate;

    /** The url link to the USGS web page with more info. */
    private String mUrl;

    public Earthquake(String location, double magnitude, long unixDate, String Url) {
        mLocation = location;
        mMagnitude = magnitude;
        mUnixDate = unixDate;
        mUrl = Url;
    }

    /** Retrieve the location */
    public String getLocation() {
        return mLocation;
    }

    /** Retrieve the magnitude */
    public double getMagnitude() {
        return mMagnitude;
    }

    /** Retrieve the date */
    public long getUnixDate() {
        return mUnixDate;
    }

    /** Retrieve the URL */
    public String getUrl() {
        return mUrl;
    }
}

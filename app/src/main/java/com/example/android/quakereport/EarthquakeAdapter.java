package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jonathanbarrera on 2/13/18.
 * This custom EarthquakeAdapter class can adapt instances from the Earthquake class
 * to an array for TextView inflation.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        // Get the {@link Earthquake} object located at this position in the list.
        final Earthquake currentEarthquake = getItem(position);


        // Find the TextViews in the list_item.xml for the primary location and location offset
        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location_view);
        TextView locationOffsetTextView = (TextView) listItemView.findViewById(R.id.location_offset_view);
        // Split the location string into two parts: primaryLocation and locationOffset
        String location = currentEarthquake.getLocation();
        String locationOffset;
        String primaryLocation;
        if (location.contains("km")){
            String[] locationStringArray = location.split("of ");
            locationOffset = locationStringArray[0] + "of";
            primaryLocation = locationStringArray[1];
        } else {
            primaryLocation = location;
            locationOffset = "Near the";
        }
        // Set the information from the current Earthquake instance to the TextViews
        primaryLocationTextView.setText(primaryLocation);
        locationOffsetTextView.setText(locationOffset);


        // Find the TextView in the list_item.xml for the magnitude
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude_view);
        // Format magnitude as a decimal with one digit after the decimal point (ex. 0.0)
        String magnitude = formatMagnitude(currentEarthquake.getMagnitude());
        // Set the information from the current Earthquake instance to the TextView
        magnitudeTextView.setText(magnitude);


        // Find the TextView in the list_item.xml for the date and time
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_view);
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_view);

        // Get the Date information from the current Earthquake object.
        Date dateObject = new Date(currentEarthquake.getUnixDate());

        // Format the date and set to the TextView
        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);

        // Format the time and set to the TextView
        String time = formatTime(dateObject);
        timeTextView.setText(time);

        // Set the proper background color on the magnitude circle
        // Fetch the background from the TextView, which is a GradientDrawable
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude.
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Return the list item view that is now showing the proper data.
        return listItemView;
    }

    /** Helper method to properly format the date (ex. "Jan 4, 1989") */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyy");
        String date = dateFormat.format(dateObject);
        return date;
    }

    /** Helper method to properly format the time (ex. "4:20pm") */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String time = timeFormat.format(dateObject);
        return time;
    }

    /** Helper method to properly format the magnitude decimal (ex. "0.0") */
    private String formatMagnitude(double magnitude) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String magnitudeText = decimalFormat.format(magnitude);
        return magnitudeText;
    }

    /** Helper method to return the proper magnitude circle color based on the magnitude
     * value.
     * */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeRange = (int) Math.floor(magnitude);
        int magnitudeColor;
        switch (magnitudeRange) {
            case 0:
            case 1:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 7:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            default:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
        }
        Log.v("EarthquakeActivity.this", String.valueOf(magnitudeColor));
        return magnitudeColor;
    }

}


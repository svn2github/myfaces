package org.apache.myfaces.dateformat;

import java.util.Date;

/**
 * A simple class that contains locale-specific constants used for date
 * parsing and formatting.
 * <p>
 * An instance of this can be created, and the symbols modified before
 * passing it to a SimpleDateFormatter. This allows date formatting and
 * parsing to be localised.
 */
public class DateFormatSymbols
{
	String[] eras = {"BC", "AD"};

	String[] months = {
			"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December", "Undecimber"
    };

	String[] shortMonths = {
			"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec", "Und"
    };

	String[] weekdays = {
			"Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"
    };

	String[] shortWeekdays = {
			"Sun", "Mon", "Tue",
            "Wed", "Thu", "Fri", "Sat"
    };

	String[] ampms = { "AM", "PM" };

	String[] zoneStrings = {
			null, "long-name", "short-name"
	};

	Date threshold;
	Date twoDigitYearStart;

	public DateFormatSymbols()
	{
        threshold = new Date();
        threshold.setYear(threshold.getYear()-80);
        this.twoDigitYearStart = threshold;
	}
}

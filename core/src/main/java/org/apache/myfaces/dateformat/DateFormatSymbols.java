package org.apache.myfaces.dateformat;

import java.util.Date;
import java.util.Locale;

/**
 * A simple class that contains locale-specific constants used for date
 * parsing and formatting.
 * <p>
 * An instance of this can be created, and the symbols modified before
 * passing it to a SimpleDateFormatter. This allows date formatting and
 * parsing to be localised.
 * <p>
 * The standard Java DateFormatSymbols class could be used directly by
 * SimpleDateFormatter, making this class unnecessary. However javascript
 * does not have an equivalent class built-in, so to keep symmetry between
 * the java and javascript versions we have one here too.
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

	// TODO: move these vars out of this "constant" class.
	Date threshold;
	Date twoDigitYearStart;


	public DateFormatSymbols()
	{
        threshold = new Date();
        threshold.setYear(threshold.getYear()-80);
        this.twoDigitYearStart = threshold;
	}

	public DateFormatSymbols(Locale l)
	{
		java.text.DateFormatSymbols src = java.text.DateFormatSymbols.getInstance(l);
		this.eras = src.getEras();
		this.months = src.getMonths();
		this.shortMonths = src.getShortMonths();
		this.weekdays = src.getWeekdays();
		this.shortWeekdays = src.getShortWeekdays();
		this.ampms = src.getAmPmStrings();

		// zoneStrings ??
	}
}

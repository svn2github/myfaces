package org.apache.myfaces.dateformat;


/**
 * A simple class that holds a date represented as a (year, week) pair
 * rather than the more traditional (year, month, day).
 */
public class WeekDate
{
    private int year, week;

    public WeekDate(int y, int w)
    {
        year = y;
        week = w;
    }

    public int getYear() 
    {
    	return year;
    }
    
    public int getWeek()
    {
    	return week;
    }
}

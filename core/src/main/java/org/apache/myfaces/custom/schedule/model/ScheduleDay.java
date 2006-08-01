/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.myfaces.custom.schedule.model;


import java.io.Serializable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.myfaces.custom.schedule.util.ScheduleEntryComparator;


/**
 * <p>
 * This class represents one day in the schedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author: werpu $)
 * @version $Revision: 371736 $
 */
public class ScheduleDay
    extends Day
    implements Serializable, Comparable
{
    //~ Instance fields --------------------------------------------------------

    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;
    private final TreeSet entries;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ScheduleDay object.
     *
     * @param date the date
     */
    public ScheduleDay(Date date)
    {
        super(date);
        this.entries = new TreeSet(new ScheduleEntryComparator());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * @return true if there are no schedule entries
     */
    public boolean isEmpty()
    {
        return entries.isEmpty();
    }

    /**
     * <p>
     * Add an entry to this day
     * </p>
     *
     * @param entry the entry to add
     *
     * @return true if successful
     */
    public boolean addEntry(ScheduleEntry entry)
    {
        if (
            (entry == null) || (entry.getStartTime() == null) ||
                (entry.getEndTime() == null)
        ) {
            return false;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(entry.getEndTime());
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date endDate = cal.getTime();
        cal.setTime(entry.getStartTime());

        while (cal.getTime().before(endDate)) {
            if (equalsDate(cal.getTime())) {
                entries.add(entry);

                return true;
            }

            cal.add(Calendar.DATE, 1);
        }

        return false;
    }

    /**
     * <p>
     * Remove all entries from this day
     * </p>
     */
    public void clear()
    {
        entries.clear();
    }

    /**
     * @return an iterator for the schedule entries of this day
     */
    public Iterator iterator()
    {
        return entries.iterator();
    }

    /**
     * <p>
     * Remove an entry from this day
     * </p>
     *
     * @param entry the entry to remove
     *
     * @return true if successful
     */
    public boolean remove(ScheduleEntry entry)
    {
        return entries.remove(entry);
    }

    /**
     * @return the number of entries that are shown on this day
     */
    public int size()
    {
        return entries.size();
    }
    
    /**
     * Get the non-inclusive hour by which all events on this day have completed.
     * All day events are not included, as their end time is implicit.
     * 
     * @return From 0, where there are no events and 24 where events exist up to the last hour
     */
    public int getLastEventHour()
    {
    	Calendar endTime = GregorianCalendar.getInstance();

    	// Check all events, as the last to finish may not be the last to begin
    	Date lastEnd = null;
    	
    	for (Iterator it = entries.iterator(); it.hasNext(); ) {
    		ScheduleEntry next = (ScheduleEntry) it.next();
    		
    		if (!next.isAllDay() && (lastEnd == null || lastEnd.before(next.getEndTime()))) {
    			lastEnd = next.getEndTime();
    		}
    	}
    	if (lastEnd == null) {
    		
    		return 0;
    	}
    	
    	endTime.setTime(lastEnd);
    	
    	if (endTime.get(Calendar.MINUTE) > 0){
    		// Round up to next hour
    		endTime.add(Calendar.HOUR_OF_DAY, 1);
    	}

    	return equalsDate(endTime.getTime()) ? endTime.get(Calendar.HOUR_OF_DAY) : 24;    	
    }
    
    /**
     * Get the inclusive hour in which the first event on this day starts.
     * All day events are not included, as their start time is implicit.
     * 
     * @return From 0, where there is an event in the first hour to 24 where there are no events
     */
    public int getFirstEventHour()
    {
    	Calendar startTime = null;

    	for (Iterator it = entries.iterator(); it.hasNext(); ) {
    		ScheduleEntry next = (ScheduleEntry) it.next();
    		
    		if (!next.isAllDay()) {
    			startTime = GregorianCalendar.getInstance();
    			startTime.setTime(((ScheduleEntry) entries.first()).getStartTime());
    			break;
    		}
    	}

    	if (startTime == null) {
    		
    		return 24;
    	}

    	return equalsDate(startTime.getTime()) ? startTime.get(Calendar.HOUR_OF_DAY) : 0;
    }
}
//The End

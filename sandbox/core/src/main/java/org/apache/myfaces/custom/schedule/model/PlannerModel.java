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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;


/**
 * <p>
 * The model of a Planner component
 * </p>
 *
 * TODO this should be an interface, with a DefaultPlannerModel implementation
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$
 */
public class PlannerModel
    implements Serializable
{
    //~ Instance fields --------------------------------------------------------

    private HashMap entities;
    private TreeSet days;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PlannerModel object.
     */
    public PlannerModel()
    {
        days = new TreeSet();
        entities = new HashMap();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * <p>
     * Set the day
     * </p>
     *
     * @param date the new day
     *
     * @return true if successful
     */
    public boolean setDay(Date date)
    {
        if (date == null) {
            return false;
        }

        days.clear();
        add(date);

        return true;
    }

    /**
     * <p>
     * Get the day at position <i>index</i>
     * </p>
     *
     * @param index the index
     *
     * @return the day
     */
    public Day getDay(int index)
    {
        Object[] dayArray = days.toArray();

        return (Day) dayArray[index];
    }

    /**
     * <p>
     * Get the entity at position <i>index</i>
     * </p>
     *
     * @param id the index
     *
     * @return the entity
     */
    public PlannerEntity getEntity(String id)
    {
        return (PlannerEntity) entities.get(id);
    }

    /**
     * <p>
     * Assign a special status to a day, e.g. a holiday
     * </p>
     *
     * @param date the date
     * @param isWorkingDay whether the day is a normal working day
     * @param specialDayName the name of the holiday
     *
     * @return true if successful
     */
    public boolean setSpecialDay(
        Date date,
        boolean isWorkingDay,
        String specialDayName
    )
    {
        if ((date == null) || !containsDate(date)) {
            return false;
        }

        for (Iterator dayIterator = dayIterator(); dayIterator.hasNext();) {
            Day day = (Day) dayIterator.next();
            day.setWorkingDay(isWorkingDay);
            day.setSpecialDayName(specialDayName);
        }

        return true;
    }

    /**
     * <p>
     * Add a day to the planner
     * </p>
     *
     * @param date the date that should be added
     * @param isWorkingDay whether the day is a normal working day
     * @param specialDayName the name of the holiday, if any
     *
     * @return the day that was added
     */
    public Day add(
        Date date,
        boolean isWorkingDay,
        String specialDayName
    )
    {
        if (date == null) {
            return null;
        }

        Day day = new Day(date);
        day.setWorkingDay(isWorkingDay);
        day.setSpecialDayName(specialDayName);
        days.add(day);

        return day;
    }

    /**
     * <p>
     * Add an entity to the planner
     * </p>
     *
     * @param entity the entity to add
     *
     * @return true if successful
     */
    public boolean add(PlannerEntity entity)
    {
        if ((entity == null) || entities.containsKey(entity.getId())) {
            return false;
        }

        entities.put(entity.getId(), entity);

        return true;
    }

    /**
     * <p>
     * Add an entity to the planner
     * </p>
     *
     * @param id the id of the entity
     * @param name the name of the entity
     *
     * @return the entity that was added
     */
    public PlannerEntity add(
        String id,
        String name
    )
    {
        PlannerEntity entity = new PlannerEntity(id, name);

        if (add(entity)) {
            return entity;
        }

        return null;
    }

    /**
     * <p>
     * Add a day to the planner
     * </p>
     *
     * @param day the day to add
     *
     * @return the day that was added
     */
    public Day add(Day day)
    {
        if (day != null) {
            days.add(day);
        }

        return day;
    }

    /**
     * <p>
     * Add a day to the planner
     * </p>
     *
     * @param date the day to add
     *
     * @return the day that was added
     */
    public Day add(Date date)
    {
        return add(date, true, null);
    }

    /**
     * <p>
     * Add a schedule entry to the planner
     * </p>
     *
     * @param entityId the id of the entity to which the entry applies
     * @param entry the entry to add
     *
     * @return true if successful
     */
    public boolean addEntry(
        String entityId,
        ScheduleEntry entry
    )
    {
        PlannerEntity entity = getEntity(entityId);

        if (entity == null) {
            return false;
        }

        return entity.addEntry(entry);
    }

    /**
     * <p>
     * Remove all days and all entities
     * </p>
     */
    public void clear()
    {
        days.clear();
        entities.clear();
    }

    /**
     * <p>
     * Check if the planner contains the specified date
     * </p>
     *
     * @param date the date to check
     *
     * @return true if successful
     */
    public boolean containsDate(Date date)
    {
        for (Iterator iterator = days.iterator(); iterator.hasNext();) {
            Day day = (Day) iterator.next();

            if (day.equalsDate(date)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return an iterator for the days of the planner
     */
    public Iterator dayIterator()
    {
        return days.iterator();
    }

    /**
     * @return an iterator for the entities of the planner
     */
    public Iterator entityIterator()
    {
        return entities.values().iterator();
    }

    /**
     * @return the number of days that are shown in the planner
     */
    public int numberOfDays()
    {
        return days.size();
    }

    /**
     * @return the number of entities that are shown in the planner
     */
    public int numberOfEntities()
    {
        return entities.size();
    }
}
//The End

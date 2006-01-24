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

import java.util.Iterator;
import java.util.TreeSet;

import org.apache.myfaces.custom.schedule.util.ScheduleEntryComparator;


/**
 * <p>
 * A Planner entity is a person, room, piece of equipment, ... that is displayed
 * as a row in the planner component.
 * </p>
 *
 * TODO this should be an interface, with a DefaultPlannerEntity implementation
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$
 */
public class PlannerEntity
    implements Serializable
{
    //~ Instance fields --------------------------------------------------------

    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String name;
    private TreeSet entries;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PlannerEntity object.
     *
     * @param id the unique identifier
     * @param name the name
     */
    public PlannerEntity(
        String id,
        String name
    )
    {
        super();
        this.id = id;
        this.name = name;
        this.entries = new TreeSet(new ScheduleEntryComparator());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * @return true if there are no appointments for this entity
     */
    public boolean isEmpty()
    {
        return entries.isEmpty();
    }

    /**
     * @param entries The entries to set.
     */
    public void setEntries(TreeSet entries)
    {
        this.entries = entries;
    }

    /**
     * @return Returns the entries.
     */
    public TreeSet getEntries()
    {
        return entries;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * <p>
     * Add a schedule entry to this planner entity
     * </p>
     *
     * @param entry the entry to be added
     *
     * @return whether the addition was successful
     */
    public boolean addEntry(ScheduleEntry entry)
    {
        if (
            (entry == null) || (entry.getStartTime() == null) ||
                (entry.getEndTime() == null)
        ) {
            return false;
        }

        entries.add(entry);

        return true;
    }

    /**
     * <p>
     * Remove all entries
     * </p>
     */
    public void clear()
    {
        entries.clear();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if (o instanceof PlannerEntity) {
            PlannerEntity other = (PlannerEntity) o;

            return id.equals(other.id);
        }

        return false;
    }

    /**
     * @return an iterator for the entries
     */
    public Iterator iterator()
    {
        return entries.iterator();
    }

    /**
     * <p>
     * Remove an entry from this entity
     * </p>
     *
     * @param entry the entry to be removed
     *
     * @return whether the removal was successful
     */
    public boolean remove(ScheduleEntry entry)
    {
        return entries.remove(entry);
    }

    /**
     * @return the number of entries
     */
    public int size()
    {
        return entries.size();
    }
}
//The End

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

package org.apache.myfaces.custom.schedule.util;

import java.util.Comparator;

import org.apache.myfaces.custom.schedule.model.ScheduleEntry;

/**
 * <p>
 * Comparator for ScheduleEntry objects. This is needed for correctly
 * rendering the schedule.
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision$
 */
public class ScheduleEntryComparator implements Comparator
{
    //~ Methods ----------------------------------------------------------------

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2)
    {
        if (o1 instanceof ScheduleEntry && o2 instanceof ScheduleEntry)
        {
            ScheduleEntry entry1 = (ScheduleEntry) o1;
            ScheduleEntry entry2 = (ScheduleEntry) o2;

            int returnint = entry1.getStartTime().compareTo(
                    entry2.getStartTime());
            if (returnint == 0)
            {
                returnint = entry1.getEndTime().compareTo(entry2.getEndTime());
            }
            if (returnint == 0)
            {
                returnint = entry1.getId().compareTo(entry2.getId());
            }

            return returnint;
        }

        return 1;
    }
}
//The End

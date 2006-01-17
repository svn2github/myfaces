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


/**
 * <p>
 * A default implementation of a Schedule entry
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$
 */
public class DefaultScheduleEntry
    implements Serializable, ScheduleEntry
{
    //~ Instance fields --------------------------------------------------------

    private Date endTime;
    private Date startTime;
    private String description;
    private String id;
    private String subtitle;
    private String title;

    //~ Methods ----------------------------------------------------------------

    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return Returns the endTime.
     */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param startTime The startTime to set.
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    /**
     * @return Returns the startTime.
     */
    public Date getStartTime()
    {
        return startTime;
    }

    /**
     * @param subtitle The subtitle to set.
     */
    public void setSubtitle(String subtitle)
    {
        this.subtitle = subtitle;
    }

    /**
     * @return Returns the subtitle.
     */
    public String getSubtitle()
    {
        return subtitle;
    }

    /**
     * @param title The title to set.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }
}
//The End

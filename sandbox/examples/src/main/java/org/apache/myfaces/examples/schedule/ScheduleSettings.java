/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.examples.schedule;

import java.io.Serializable;

import org.apache.myfaces.custom.schedule.model.ScheduleModel;

public class ScheduleSettings implements Serializable
{
    private ScheduleModel model;
    
    private static final long serialVersionUID = -8547428935814382762L;

    private int compactMonthRowHeight;

    private int compactWeekRowHeight;

    private String headerDateFormat;

    private boolean readonly;

    private String theme;

    private boolean tooltip;

    private int visibleEndHour;

    private int visibleStartHour;

    private int workingEndHour;

    private int workingStartHour;

    public int getCompactMonthRowHeight()
    {
        return compactMonthRowHeight;
    }

    public int getCompactWeekRowHeight()
    {
        return compactWeekRowHeight;
    }

    public String getHeaderDateFormat()
    {
        return headerDateFormat;
    }

    public String getTheme()
    {
        return theme;
    }

    public int getVisibleEndHour()
    {
        return visibleEndHour;
    }

    public int getVisibleStartHour()
    {
        return visibleStartHour;
    }

    public int getWorkingEndHour()
    {
        return workingEndHour;
    }

    public int getWorkingStartHour()
    {
        return workingStartHour;
    }

    public boolean isReadonly()
    {
        return readonly;
    }

    public boolean isTooltip()
    {
        return tooltip;
    }

    public String save()
    {
        model.refresh();
        return "success";
    }

    public void setCompactMonthRowHeight(int compactMonthRowHeight)
    {
        this.compactMonthRowHeight = compactMonthRowHeight;
    }

    public void setCompactWeekRowHeight(int compactWeekRowHeight)
    {
        this.compactWeekRowHeight = compactWeekRowHeight;
    }

    public void setHeaderDateFormat(String headerDateFormat)
    {
        this.headerDateFormat = headerDateFormat;
    }

    public void setReadonly(boolean readonly)
    {
        this.readonly = readonly;
    }

    public void setTheme(String theme)
    {
        this.theme = theme;
    }

    public void setTooltip(boolean tooltip)
    {
        this.tooltip = tooltip;
    }

    public void setVisibleEndHour(int visibleEndHour)
    {
        this.visibleEndHour = visibleEndHour;
    }

    public void setVisibleStartHour(int visibleStartHour)
    {
        this.visibleStartHour = visibleStartHour;
    }

    public void setWorkingEndHour(int workingEndHour)
    {
        this.workingEndHour = workingEndHour;
    }

    public void setWorkingStartHour(int workingStartHour)
    {
        this.workingStartHour = workingStartHour;
    }

    public ScheduleModel getModel()
    {
        return model;
    }

    public void setModel(ScheduleModel model)
    {
        this.model = model;
    }
}

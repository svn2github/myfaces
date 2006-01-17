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

package org.apache.myfaces.custom.schedule;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;

/**
 * <p>
 * JSP tag for the schedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$
 */
public class ScheduleTag extends UIComponentTag
{
    //~ Instance fields --------------------------------------------------------

    private String action;
    private String actionListener;
    private String compactMonthRowHeight;
    private String compactWeekRowHeight;
    private String headerDateFormat;
    private String immediate;
    private String readonly;
    private String rendered;
    private String theme;
    private String tooltip;
    private String value;
    private String visibleEndHour;
    private String visibleStartHour;
    private String workingEndHour;
    private String workingStartHour;
    
    private String columnClass;
    private String backgroundClass;
    private String freeClass;
    private String evenClass;
    private String unevenClass;
    private String gutterClass;
    private String headerClass;
    private String dateClass;
    private String holidayClass;
    private String hoursClass;
    private String minutesClass;
    private String selectedEntryClass;
    private String textClass;
    private String titleClass;
    private String subtitleClass;
    private String entryClass;
    private String foregroundClass;
    
    private String dayClass;
    private String inactiveDayClass;
    private String contentClass;
    private String selectedClass;
    private String monthClass;
    private String weekClass;
    
    private String entryRenderer;
    

    //~ Methods ----------------------------------------------------------------

    /**
     * <p>
     * The action of the JSF component
     * </p>
     *
     * @param action The action to set.
     */
    public void setAction(String action)
    {
        this.action = action;
    }

    /**
     * <p>
     * The action of the JSF component
     * </p>
     *
     * @return Returns the action.
     */
    public String getAction()
    {
        return action;
    }

    /**
     * <p>
     * The actionlistener of the JSF component
     * </p>
     *
     * @param actionListener The actionListener to set.
     */
    public void setActionListener(String actionListener)
    {
        this.actionListener = actionListener;
    }

    /**
     * <p>
     * The actionlistener of the JSF component
     * </p>
     *
     * @return Returns the actionListener.
     */
    public String getActionListener()
    {
        return actionListener;
    }

    /**
     * <p>
     * The height in pixels of a cell in the compact month view
     * </p>
     *
     * @param compactMonthRowHeight The compactMonthRowHeight to set.
     */
    public void setCompactMonthRowHeight(String compactMonthRowHeight)
    {
        this.compactMonthRowHeight = compactMonthRowHeight;
    }

    /**
     * <p>
     * The height in pixels of a cell in the compact month view
     * </p>
     *
     * @return Returns the compactMonthRowHeight.
     */
    public String getCompactMonthRowHeight()
    {
        return compactMonthRowHeight;
    }

    /**
     * <p>
     * The height in pixels of a cell in the compact week view
     * </p>
     *
     * @param compactWeekRowHeight The compactWeekRowHeight to set.
     */
    public void setCompactWeekRowHeight(String compactWeekRowHeight)
    {
        this.compactWeekRowHeight = compactWeekRowHeight;
    }

    /**
     * <p>
     * The height in pixels of a cell in the compact week view
     * </p>
     *
     * @return Returns the compactWeekRowHeight.
     */
    public String getCompactWeekRowHeight()
    {
        return compactWeekRowHeight;
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType()
    {
        return HtmlSchedule.COMPONENT_TYPE;
    }

    /**
     * <p>
     * The dateformat used in the header
     * </p>
     *
     * @param headerDateFormat The headerDateFormat to set.
     */
    public void setHeaderDateFormat(String headerDateFormat)
    {
        this.headerDateFormat = headerDateFormat;
    }

    /**
     * <p>
     * The dateformat used in the header
     * </p>
     *
     * @return Returns the headerDateFormat.
     */
    public String getHeaderDateFormat()
    {
        return headerDateFormat;
    }

    /**
     * <p>
     * The immediate property of the JSF component
     * </p>
     *
     * @param immediate The immediate to set.
     */
    public void setImmediate(String immediate)
    {
        this.immediate = immediate;
    }

    /**
     * <p>
     * The immediate property of the JSF component
     * </p>
     *
     * @return Returns the immediate.
     */
    public String getImmediate()
    {
        return immediate;
    }

    /**
     * <p>
     * The readonly property of the JSF component
     * </p>
     *
     * @param readonly The readonly to set.
     */
    public void setReadonly(String readonly)
    {
        this.readonly = readonly;
    }

    /**
     * <p>
     * The readonly property of the JSF component
     * </p>
     *
     * @return Returns the readonly.
     */
    public String getReadonly()
    {
        return readonly;
    }

    /**
     * <p>
     * Should this component be rendered?
     * </p>
     *
     * @param rendered The rendered to set.
     */
    public void setRendered(String rendered)
    {
        this.rendered = rendered;
    }

    /**
     * <p>
     * Should this component be rendered?
     * </p>
     *
     * @return Returns the rendered.
     */
    public String getRendered()
    {
        return rendered;
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType()
    {
        return UISchedule.DEFAULT_RENDERER_TYPE;
    }

    /**
     * <p>
     * The theme of the schedule component. This is the name of the
     * CSS file that should be loaded when rendering the schedule.
     * Possible values are 'default', 'outlookxp', 'evolution'.
     * </p>
     * 
     * @param theme The theme to set.
     */
    public void setTheme(String theme)
    {
        this.theme = theme;
    }

    /**
     * <p>
     * The theme of the schedule component. This is the name of the
     * CSS file that should be loaded when rendering the schedule.
     * Possible values are 'default', 'outlookxp', 'evolution'.
     * </p>
     * 
     * @return Returns the theme.
     */
    public String getTheme()
    {
        return theme;
    }

    /**
     * <p>
     * Should tooltips be displayed?
     * </p>
     *
     * @param tooltip The tooltip to set.
     */
    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }

    /**
     * <p>
     * Should tooltips be displayed?
     * </p>
     *
     * @return Returns the tooltip.
     */
    public String getTooltip()
    {
        return tooltip;
    }

    /**
     * <p>
     * The value of the JSF component
     * </p>
     *
     * @param value The value to set.
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * <p>
     * The value of the JSF component
     * </p>
     *
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * <p>
     * The visible end hour of the schedule
     * </p>
     *
     * @param visibleEndHour The visibleEndHour to set.
     */
    public void setVisibleEndHour(String visibleEndHour)
    {
        this.visibleEndHour = visibleEndHour;
    }

    /**
     * <p>
     * The visible end hour of the schedule
     * </p>
     *
     * @return Returns the visibleEndHour.
     */
    public String getVisibleEndHour()
    {
        return visibleEndHour;
    }

    /**
     * <p>
     * The visible start hour of the schedule
     * </p>
     *
     * @param visibleStartHour The visibleStartHour to set.
     */
    public void setVisibleStartHour(String visibleStartHour)
    {
        this.visibleStartHour = visibleStartHour;
    }

    /**
     * <p>
     * The visible start hour of the schedule
     * </p>
     *
     * @return Returns the visibleStartHour.
     */
    public String getVisibleStartHour()
    {
        return visibleStartHour;
    }

    /**
     * <p>
     * The working end hour of the schedule
     * </p>
     *
     * @param workingEndHour The workingEndHour to set.
     */
    public void setWorkingEndHour(String workingEndHour)
    {
        this.workingEndHour = workingEndHour;
    }

    /**
     * <p>
     * The working end hour of the schedule
     * </p>
     *
     * @return Returns the workingEndHour.
     */
    public String getWorkingEndHour()
    {
        return workingEndHour;
    }

    /**
     * <p>
     * The working start hour of the schedule
     * </p>
     *
     * @param workingStartHour The workingStartHour to set.
     */
    public void setWorkingStartHour(String workingStartHour)
    {
        this.workingStartHour = workingStartHour;
    }

    /**
     * <p>
     * The working start hour of the schedule
     * </p>
     *
     * @return Returns the workingStartHour.
     */
    public String getWorkingStartHour()
    {
        return workingStartHour;
    }

    public String getBackgroundClass()
    {
        return backgroundClass;
    }

    public void setBackgroundClass(String backgroundClass)
    {
        this.backgroundClass = backgroundClass;
    }

    public String getColumnClass()
    {
        return columnClass;
    }

    public void setColumnClass(String columnClass)
    {
        this.columnClass = columnClass;
    }

    public String getContentClass()
    {
        return contentClass;
    }

    public void setContentClass(String contentClass)
    {
        this.contentClass = contentClass;
    }

    public String getDateClass()
    {
        return dateClass;
    }

    public void setDateClass(String dateClass)
    {
        this.dateClass = dateClass;
    }

    public String getDayClass()
    {
        return dayClass;
    }

    public void setDayClass(String dayClass)
    {
        this.dayClass = dayClass;
    }

    public String getEntryClass()
    {
        return entryClass;
    }

    public void setEntryClass(String entryClass)
    {
        this.entryClass = entryClass;
    }

    public String getEvenClass()
    {
        return evenClass;
    }

    public void setEvenClass(String evenClass)
    {
        this.evenClass = evenClass;
    }

    public String getForegroundClass()
    {
        return foregroundClass;
    }

    public void setForegroundClass(String foregroundClass)
    {
        this.foregroundClass = foregroundClass;
    }

    public String getFreeClass()
    {
        return freeClass;
    }

    public void setFreeClass(String freeClass)
    {
        this.freeClass = freeClass;
    }

    public String getGutterClass()
    {
        return gutterClass;
    }

    public void setGutterClass(String gutterClass)
    {
        this.gutterClass = gutterClass;
    }

    public String getHeaderClass()
    {
        return headerClass;
    }

    public void setHeaderClass(String headerClass)
    {
        this.headerClass = headerClass;
    }

    public String getHolidayClass()
    {
        return holidayClass;
    }

    public void setHolidayClass(String holidayClass)
    {
        this.holidayClass = holidayClass;
    }

    public String getHoursClass()
    {
        return hoursClass;
    }

    public void setHoursClass(String hoursClass)
    {
        this.hoursClass = hoursClass;
    }

    public String getInactiveDayClass()
    {
        return inactiveDayClass;
    }

    public void setInactiveDayClass(String inactiveDayClass)
    {
        this.inactiveDayClass = inactiveDayClass;
    }

    public String getMinutesClass()
    {
        return minutesClass;
    }

    public void setMinutesClass(String minutesClass)
    {
        this.minutesClass = minutesClass;
    }

    public String getMonthClass()
    {
        return monthClass;
    }

    public void setMonthClass(String monthClass)
    {
        this.monthClass = monthClass;
    }

    public String getSelectedClass()
    {
        return selectedClass;
    }

    public void setSelectedClass(String selectedClass)
    {
        this.selectedClass = selectedClass;
    }

    public String getSelectedEntryClass()
    {
        return selectedEntryClass;
    }

    public void setSelectedEntryClass(String selectedEntryClass)
    {
        this.selectedEntryClass = selectedEntryClass;
    }

    public String getSubtitleClass()
    {
        return subtitleClass;
    }

    public void setSubtitleClass(String subtitleClass)
    {
        this.subtitleClass = subtitleClass;
    }

    public String getTextClass()
    {
        return textClass;
    }

    public void setTextClass(String textClass)
    {
        this.textClass = textClass;
    }

    public String getTitleClass()
    {
        return titleClass;
    }

    public void setTitleClass(String titleClass)
    {
        this.titleClass = titleClass;
    }

    public String getUnevenClass()
    {
        return unevenClass;
    }

    public void setUnevenClass(String unevenClass)
    {
        this.unevenClass = unevenClass;
    }

    public String getWeekClass()
    {
        return weekClass;
    }

    public void setWeekClass(String weekClass)
    {
        this.weekClass = weekClass;
    }

    public String getEntryRenderer()
    {
        return entryRenderer;
    }

    public void setEntryRenderer(String entryRenderer)
    {
        this.entryRenderer = entryRenderer;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release()
    {
        super.release();
        visibleStartHour = null;
        visibleEndHour = null;
        workingStartHour = null;
        workingEndHour = null;
        headerDateFormat = null;
        value = null;
        immediate = null;
        actionListener = null;
        action = null;
        readonly = null;
        theme = null;
        tooltip = null;
        rendered = null;
        
        columnClass = null;
        backgroundClass = null;
        freeClass = null;
        evenClass = null;
        unevenClass = null;
        gutterClass = null;
        headerClass = null;
        dateClass = null;
        holidayClass = null;
        hoursClass = null;
        minutesClass = null;
        selectedEntryClass = null;
        textClass = null;
        titleClass = null;
        subtitleClass = null;
        entryClass = null;
        foregroundClass = null;
        
        dayClass = null;
        inactiveDayClass = null;
        contentClass = null;
        selectedClass = null;
        monthClass = null;
        weekClass = null;
        
        entryRenderer = null;
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        HtmlSchedule schedule = (HtmlSchedule) component;
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();

        if (rendered != null)
        {
            if (isValueReference(rendered))
            {
                schedule.setValueBinding("rendered", app
                        .createValueBinding(rendered));
            }
            else
            {
                schedule.setRendered(Boolean.valueOf(rendered).booleanValue());
            }
        }

        if (visibleStartHour != null)
        {
            if (isValueReference(visibleStartHour))
            {
                schedule.setValueBinding("visibleStartHour", app
                        .createValueBinding(visibleStartHour));
            }
            else
            {
                schedule.setVisibleStartHour(new Integer(visibleStartHour)
                        .intValue());
            }
        }

        if (visibleEndHour != null)
        {
            if (isValueReference(visibleEndHour))
            {
                schedule.setValueBinding("visibleEndHour", app
                        .createValueBinding(visibleEndHour));
            }
            else
            {
                schedule.setVisibleEndHour(new Integer(visibleEndHour)
                        .intValue());
            }
        }

        if (workingStartHour != null)
        {
            if (isValueReference(workingStartHour))
            {
                schedule.setValueBinding("workingStartHour", app
                        .createValueBinding(workingStartHour));
            }
            else
            {
                schedule.setWorkingStartHour(new Integer(workingStartHour)
                        .intValue());
            }
        }

        if (workingEndHour != null)
        {
            if (isValueReference(workingEndHour))
            {
                schedule.setValueBinding("workingEndHour", app
                        .createValueBinding(workingEndHour));
            }
            else
            {
                schedule.setWorkingEndHour(new Integer(workingEndHour)
                        .intValue());
            }
        }

        if (immediate != null)
        {
            if (isValueReference(immediate))
            {
                schedule.setValueBinding("immediate", app
                        .createValueBinding(immediate));
            }
            else
            {
                schedule
                        .setImmediate(Boolean.valueOf(immediate).booleanValue());
            }
        }

        if (readonly != null)
        {
            if (isValueReference(readonly))
            {
                schedule.setValueBinding("readonly", app
                        .createValueBinding(readonly));
            }
            else
            {
                schedule.setReadonly(Boolean.valueOf(readonly).booleanValue());
            }
        }

        if (actionListener != null)
        {
            if (isValueReference(actionListener))
            {
                MethodBinding actionListenerBinding = app.createMethodBinding(
                        actionListener, new Class[] { ActionEvent.class });
                schedule.setActionListener(actionListenerBinding);
            }
            else
            {
                throw new IllegalArgumentException(
                        "actionListener property must be a method-binding expression.");
            }
        }

        if (action != null)
        {
            if (isValueReference(action))
            {
                MethodBinding actionBinding = app.createMethodBinding(action,
                        new Class[] { ActionEvent.class });
                schedule.setAction(actionBinding);
            }
            else
            {
                throw new IllegalArgumentException(
                        "action property must be a method-binding expression.");
            }
        }

        if ((value != null) && isValueReference(value))
        {
            schedule.setValueBinding("value", app.createValueBinding(value));
        }
        else
        {
            throw new IllegalArgumentException(
                    "The value property must be a value binding expression that points to a SimpleScheduleModel object.");
        }

        addAttribute(app, schedule, "headerDateFormat", headerDateFormat);
        addAttribute(app, schedule, "theme", theme);
        addAttribute(app, schedule, "tooltip", tooltip);
        addAttribute(app, schedule, "compactWeekRowHeight",
                compactWeekRowHeight);
        addAttribute(app, schedule, "compactMonthRowHeight",
                compactMonthRowHeight);
        addAttribute(app, schedule, "column", columnClass);
        addAttribute(app, schedule, "background", backgroundClass);
        addAttribute(app, schedule, "free", freeClass);
        addAttribute(app, schedule, "even", evenClass);
        addAttribute(app, schedule, "uneven", unevenClass);
        addAttribute(app, schedule, "gutter", gutterClass);
        addAttribute(app, schedule, "header", headerClass);
        addAttribute(app, schedule, "date", dateClass);
        addAttribute(app, schedule, "holiday", holidayClass);
        addAttribute(app, schedule, "hours", hoursClass);
        addAttribute(app, schedule, "minutes", minutesClass);
        addAttribute(app, schedule, "entry-selected", selectedEntryClass);
        addAttribute(app, schedule, "text", textClass);
        addAttribute(app, schedule, "title", titleClass);
        addAttribute(app, schedule, "subtitle", subtitleClass);
        addAttribute(app, schedule, "entry", entryClass);
        addAttribute(app, schedule, "foreground", foregroundClass);
        
        addAttribute(app, schedule, "day", dayClass);
        addAttribute(app, schedule, "inactive-day", inactiveDayClass);
        addAttribute(app, schedule, "content", contentClass);
        addAttribute(app, schedule, "selected", selectedClass);
        addAttribute(app, schedule, "month", monthClass);
        addAttribute(app, schedule, "week", weekClass);
        
        addAttribute(app, schedule, "entryRenderer", entryRenderer);
    }

    private void addAttribute(Application app, UIComponent component,
            String key, String value)
    {
        if ((key != null) && (value != null))
        {
            if (isValueReference(value))
            {
                component.setValueBinding(key, app
                        .createValueBinding((String) value));
            }
            else
            {
                component.getAttributes().put(key, value);
            }
        }
    }
}
//The End

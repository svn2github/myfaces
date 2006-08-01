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

import java.io.Serializable;

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
 * @author Jurgen Lust (latest modification by $Author: jlust $)
 * @version $Revision: 392301 $
 */
public class ScheduleTag extends UIComponentTag implements Serializable 
{
    private static final long serialVersionUID = -5226785969160327763L;
    
    //~ Instance fields --------------------------------------------------------

    private String action;
    private String actionListener;
    private String backgroundClass;
    private String columnClass;
    private String compactMonthRowHeight;
    private String compactWeekRowHeight;
    private String contentClass;
    private String dateClass;
    private String dayClass;
    private String detailedRowHeight;
    private String entryClass;
    private String entryRenderer;
    private String evenClass;
    private String foregroundClass;
    private String freeClass;
    private String gutterClass;
    private String headerClass;
    private String headerDateFormat;
    private String holidayClass;
    private String hoursClass;
    private String immediate;
    private String inactiveDayClass;
    private String minutesClass;
    private String monthClass;
    private String mouseListener;
    private String readonly;
    private String rendered;
    private String selectedClass;
    private String selectedEntryClass;
    private String submitOnClick;
    private String subtitleClass;
    private String textClass;
    private String theme;
    private String titleClass;
    private String tooltip;
    private String unevenClass;
    private String value;
    private String visibleEndHour;
    private String visibleStartHour;
    private String weekClass;
    private String workingEndHour;
    private String workingStartHour;
    private String renderZeroLengthEntries;
    private String expandToFitEntries;

    //~ Methods ----------------------------------------------------------------

    private void addAttribute(final Application app, final UIComponent component,
            final String key, final String value)
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
     * @return Returns the actionListener.
     */
    public String getActionListener()
    {
        return actionListener;
    }

    public String getBackgroundClass()
    {
        return backgroundClass;
    }

    public String getColumnClass()
    {
        return columnClass;
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

    public String getContentClass()
    {
        return contentClass;
    }

    public String getDateClass()
    {
        return dateClass;
    }

    public String getDayClass()
    {
        return dayClass;
    }

    /**
     * The height in pixels of a cell in the detailed day view
     * 
     * @return the detailedRowHeight
     */
    public String getDetailedRowHeight()
    {
        return detailedRowHeight;
    }

    public String getEntryClass()
    {
        return entryClass;
    }

    public String getEntryRenderer()
    {
        return entryRenderer;
    }

    public String getEvenClass()
    {
        return evenClass;
    }

    public String getForegroundClass()
    {
        return foregroundClass;
    }

    public String getFreeClass()
    {
        return freeClass;
    }

    public String getGutterClass()
    {
        return gutterClass;
    }

    public String getHeaderClass()
    {
        return headerClass;
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

    public String getHolidayClass()
    {
        return holidayClass;
    }

    public String getHoursClass()
    {
        return hoursClass;
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

    public String getInactiveDayClass()
    {
        return inactiveDayClass;
    }

    public String getMinutesClass()
    {
        return minutesClass;
    }

    public String getMonthClass()
    {
        return monthClass;
    }

    public String getMouseListener()
    {
        return mouseListener;
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

    public String getSelectedClass()
    {
        return selectedClass;
    }

    public String getSelectedEntryClass()
    {
        return selectedEntryClass;
    }

    /**
     * <p>
     * Should the parent form of this schedule be submitted when the user
     * clicks on a day? Note that this will only work when the readonly
     * property is set to false.
     * </p>
     *
     * @return submit the form on mouse click
     */
    public String getSubmitOnClick()
    {
        return submitOnClick;
    }

    public String getSubtitleClass()
    {
        return subtitleClass;
    }

    public String getTextClass()
    {
        return textClass;
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

    public String getTitleClass()
    {
        return titleClass;
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

    public String getUnevenClass()
    {
        return unevenClass;
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
     * @return Returns the visibleStartHour.
     */
    public String getVisibleStartHour()
    {
        return visibleStartHour;
    }

    public String getWeekClass()
    {
        return weekClass;
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
     * @return Returns the workingStartHour.
     */
    public String getWorkingStartHour()
    {
        return workingStartHour;
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
        submitOnClick = null;
        mouseListener = null;
        theme = null;
        tooltip = null;
        rendered = null;
        compactMonthRowHeight = null;
        compactWeekRowHeight = null;
        detailedRowHeight = null;

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
     * The actionlistener of the JSF component
     * </p>
     *
     * @param actionListener The actionListener to set.
     */
    public void setActionListener(String actionListener)
    {
        this.actionListener = actionListener;
    }

    public void setBackgroundClass(String backgroundClass)
    {
        this.backgroundClass = backgroundClass;
    }

    public void setColumnClass(String columnClass)
    {
        this.columnClass = columnClass;
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
     * The height in pixels of a cell in the compact week view
     * </p>
     *
     * @param compactWeekRowHeight The compactWeekRowHeight to set.
     */
    public void setCompactWeekRowHeight(String compactWeekRowHeight)
    {
        this.compactWeekRowHeight = compactWeekRowHeight;
    }

    public void setContentClass(String contentClass)
    {
        this.contentClass = contentClass;
    }

    public void setDateClass(String dateClass)
    {
        this.dateClass = dateClass;
    }

    public void setDayClass(String dayClass)
    {
        this.dayClass = dayClass;
    }

    /**
     * The height in pixels of a cell in the detailed day view
     * 
     * @param detailedRowHeight the detailedRowHeight
     */
    public void setDetailedRowHeight(String detailedRowHeight)
    {
        this.detailedRowHeight = detailedRowHeight;
    }

    public void setEntryClass(String entryClass)
    {
        this.entryClass = entryClass;
    }

    public void setEntryRenderer(String entryRenderer)
    {
        this.entryRenderer = entryRenderer;
    }

    public void setEvenClass(String evenClass)
    {
        this.evenClass = evenClass;
    }

    public void setForegroundClass(String foregroundClass)
    {
        this.foregroundClass = foregroundClass;
    }

    public void setFreeClass(String freeClass)
    {
        this.freeClass = freeClass;
    }

    public void setGutterClass(String gutterClass)
    {
        this.gutterClass = gutterClass;
    }

    public void setHeaderClass(String headerClass)
    {
        this.headerClass = headerClass;
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

    public void setHolidayClass(String holidayClass)
    {
        this.holidayClass = holidayClass;
    }

    public void setHoursClass(String hoursClass)
    {
        this.hoursClass = hoursClass;
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

    public void setInactiveDayClass(String inactiveDayClass)
    {
        this.inactiveDayClass = inactiveDayClass;
    }

    public void setMinutesClass(String minutesClass)
    {
        this.minutesClass = minutesClass;
    }

    public void setMonthClass(String monthClass)
    {
        this.monthClass = monthClass;
    }

    public void setMouseListener(String mouseListener)
    {
        this.mouseListener = mouseListener;
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
                schedule.setVisibleStartHour(Integer.valueOf(visibleStartHour)
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
                schedule.setVisibleEndHour(Integer.valueOf(visibleEndHour)
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
                schedule.setWorkingStartHour(Integer.valueOf(workingStartHour)
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
                schedule.setWorkingEndHour(Integer.valueOf(workingEndHour)
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

        if (submitOnClick != null)
        {
            if (isValueReference(submitOnClick))
            {
                schedule.setValueBinding("submitOnClick", app
                        .createValueBinding(submitOnClick));
            }
            else
            {
                schedule.setSubmitOnClick(Boolean.valueOf(submitOnClick)
                        .booleanValue());
            }
        }

        if (mouseListener != null)
        {
            if (isValueReference(mouseListener))
            {
                MethodBinding mouseListenerBinding = app
                        .createMethodBinding(mouseListener,
                                new Class[] { ScheduleMouseEvent.class });
                schedule.setMouseListener(mouseListenerBinding);
            }
            else
            {
                throw new IllegalArgumentException(
                        "mouseListener property must be a method-binding expression.");
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
                        null);
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
        
        if (entryRenderer != null && isValueReference(entryRenderer))
        {
            schedule.setValueBinding("entryRenderer", app.createValueBinding(entryRenderer));
        }
        else if (entryRenderer != null)
        {
            throw new IllegalArgumentException("The entryRenderer property must be a value binding expression that point to a ScheduleEntryRenderer instance");
        }

        addAttribute(app, schedule, "headerDateFormat", headerDateFormat);
        addAttribute(app, schedule, "theme", theme);
        addAttribute(app, schedule, "tooltip", tooltip);
        addAttribute(app, schedule, "compactWeekRowHeight",
                compactWeekRowHeight);
        addAttribute(app, schedule, "compactMonthRowHeight",
                compactMonthRowHeight);
        addAttribute(app, schedule, "detailedRowHeight", detailedRowHeight);
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
        
        addAttribute(app, schedule, "renderZeroLengthEntries", renderZeroLengthEntries);
        addAttribute(app, schedule, "expandToFitEntries", expandToFitEntries);
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
     * Should this component be rendered?
     * </p>
     *
     * @param rendered The rendered to set.
     */
    public void setRendered(String rendered)
    {
        this.rendered = rendered;
    }

    public void setSelectedClass(String selectedClass)
    {
        this.selectedClass = selectedClass;
    }

    public void setSelectedEntryClass(String selectedEntryClass)
    {
        this.selectedEntryClass = selectedEntryClass;
    }

    /**
     * <p>
     * Should the parent form of this schedule be submitted when the user
     * clicks on a day? Note that this will only work when the readonly
     * property is set to false.
     * </p>
     *
     * @param submitOnClick submit the form on mouse click
     */
    public void setSubmitOnClick(String submitOnClick)
    {
        this.submitOnClick = submitOnClick;
    }

    public void setSubtitleClass(String subtitleClass)
    {
        this.subtitleClass = subtitleClass;
    }

    public void setTextClass(String textClass)
    {
        this.textClass = textClass;
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

    public void setTitleClass(String titleClass)
    {
        this.titleClass = titleClass;
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

    public void setUnevenClass(String unevenClass)
    {
        this.unevenClass = unevenClass;
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
     * The visible start hour of the schedule
     * </p>
     *
     * @param visibleStartHour The visibleStartHour to set.
     */
    public void setVisibleStartHour(String visibleStartHour)
    {
        this.visibleStartHour = visibleStartHour;
    }

    public void setWeekClass(String weekClass)
    {
        this.weekClass = weekClass;
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
     * When the start- and endtime of an entry are the same, should the entry
     * be rendered, fitting the entry box to the text? 
     * </p>
     * 
     * @return rendered or not
     */
    public String getRenderZeroLengthEntries()
    {
        return renderZeroLengthEntries;
    }

    /**
     * <p>
     * When the start- and endtime of an entry are the same, should the entry
     * be rendered, fitting the entry box to the text? 
     * </p>
     * 
     * @param render
     */
    public void setRenderZeroLengthEntries(String render)
    {
        this.renderZeroLengthEntries = render;
    }

    /**
     * <p>
     * By default, if an appointment exists outside the
     * visibleStartHour/visibleEndHour limits, it does not appear in the day
     * or workweek modes. This setting checks for events outside
     * the visible range and extends the visible range to display the events.
     * If events only occur within the visible range, then no adjustment is made.
     * </p>
     * 
     * @return Returns the expandToFitEntries.
     */
    public String getExpandToFitEntries()
    {
        return expandToFitEntries;
    }

    /**
     * <p>
     * By default, if an appointment exists outside the
     * visibleStartHour/visibleEndHour limits, it does not appear in the day
     * or workweek modes. This setting checks for events outside
     * the visible range and extends the visible range to display the events.
     * If events only occur within the visible range, then no adjustment is made.
     * </p>
     * 
     * @param expandToFitEntries The expandToFitEntries to set.
     */
    public void setExpandToFitEntries(String expandToFitEntries)
    {
        this.expandToFitEntries = expandToFitEntries;
    }

}
//The End

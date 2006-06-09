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

package org.apache.myfaces.custom.schedule.renderer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.model.ScheduleDay;
import org.apache.myfaces.custom.schedule.model.ScheduleEntry;
import org.apache.myfaces.custom.schedule.util.ScheduleUtil;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

/**
 * <p>
 * Renderer for the day and workweek views of the Schedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author: jlust $)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision: 392301 $
 */
public class ScheduleDetailedDayRenderer extends AbstractScheduleRenderer
{
    private static final Log log = LogFactory.getLog(ScheduleDetailedDayRenderer.class);

    //~ Instance fields --------------------------------------------------------

    private final int defaultRowHeightInPixels = 22;

    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        super.encodeBegin(context, component);

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();
        int rowHeight = getRowHeight(schedule.getAttributes());

        //the number of rows in the grid is the number of half hours between
        //visible start hour and visible end hour, plus 1 for the header
        int numberOfRows = ((schedule.getVisibleEndHour() - schedule
                .getVisibleStartHour()) * 2) + 1;

        //the grid height = 22 pixels times the number of rows + 3, for the
        //table border and the cellpadding
        int gridHeight = (numberOfRows * rowHeight) + 3 + 10;

        //container div for the schedule grid
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, "schedule-detailed-"
                + getTheme(schedule), null);
        writer.writeAttribute(HTML.STYLE_ATTR, "height: "
                + String.valueOf(gridHeight) + "px; overflow: hidden;", null);
        writeBackground(context, schedule, writer);
        writeForegroundStart(context, schedule, writer);
    }

    /**
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = schedule.getClientId(context);
        UIForm parentForm = getParentForm(schedule);
        String formId = parentForm == null ? null : parentForm.getClientId(context);

        for (Iterator dayIterator = schedule.getModel().iterator(); dayIterator
                .hasNext();)
        {
            ScheduleDay day = (ScheduleDay) dayIterator.next();
            String dayBodyId = clientId + "_body_" + ScheduleUtil.getDateId(day.getDate());
            writer.startElement(HTML.TD_ELEM, schedule);
            writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                    "column"), null);
            writer.writeAttribute(HTML.STYLE_ATTR, "height: 100%;", null);
            writer.startElement(HTML.DIV_ELEM, schedule);
            writer.writeAttribute(HTML.ID_ATTR, dayBodyId, null);
            writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                    "column"), null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "position: relative; top: 0px; left: 0px; width: 100%; height: 100%; z-index: 0;",
                            null);
            //register an onclick event listener to a column which will capture
            //the y coordinate of the mouse, to determine the hour of day
            if (!schedule.isReadonly() && schedule.isSubmitOnClick()) {
                writer.writeAttribute(
                        HTML.ONMOUSEUP_ATTR,
                        "fireScheduleTimeClicked(this, event, '"
                        + formId + "', '"
                        + clientId
                        + "');",
                        null);
            }
            writeEntries(context, schedule, day, writer);
            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.TD_ELEM);
        }
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        if (!component.isRendered())
        {
            return;
        }

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();

        writeForegroundEnd(context, schedule, writer);
        writer.endElement(HTML.DIV_ELEM);
    }

    private String getCellClass(HtmlSchedule schedule, int column, int row)
    {
        String cellClass = "free";
        ScheduleDay day = (ScheduleDay) schedule.getModel().get(column);

        if (!day.isWorkingDay())
        {
            return getStyleClass(schedule, cellClass);
        }

        if (((schedule.getVisibleStartHour() + (row / 2)) >= schedule
                .getWorkingStartHour())
                && ((schedule.getVisibleStartHour() + (row / 2)) < schedule
                        .getWorkingEndHour()))
        {
            cellClass = ((row % 2) == 0) ? "even" : "uneven";
        }

        return getStyleClass(schedule, cellClass);
    }

    private boolean isSelected(HtmlSchedule schedule, EntryWrapper entry)
    {
        ScheduleEntry selectedEntry = schedule.getModel().getSelectedEntry();

        if (selectedEntry == null)
        {
            return false;
        }

        boolean returnboolean = selectedEntry.getId().equals(
                entry.entry.getId());

        return returnboolean;
    }

    private void maximizeEntries(EntryWrapper[] entries, int numberOfColumns)
    {
        for (int i = 0; i < entries.length; i++)
        {
            EntryWrapper entry = entries[i];

            //now see if we can expand the entry to the columns on the right
            while (((entry.column + entry.colspan) < numberOfColumns)
                    && entry.canFitInColumn(entry.column + entry.colspan))
            {
                entry.colspan++;
            }
        }
    }

    private void scanEntries(EntryWrapper[] entries, int index)
    {
        if (entries.length <= 0)
        {
            return;
        }

        EntryWrapper entry = entries[index];
        entry.column = 0;

        //see what columns are already taken
        for (int i = 0; i < index; i++)
        {
            if (entry.overlaps(entries[i]))
            {
                entry.overlappingEntries.add(entries[i]);
                entries[i].overlappingEntries.add(entry);
            }
        }

        //find an available column
        while (!entry.canFitInColumn(entry.column))
        {
            entry.column++;
        }

        //recursively scan the remaining entries for overlaps
        if (++index < entries.length)
        {
            scanEntries(entries, index);
        }
    }

    private void writeBackground(FacesContext context, HtmlSchedule schedule,
            ResponseWriter writer) throws IOException
    {
        final int rowHeight = getRowHeight(schedule.getAttributes()) - 1;
        final int headerHeight = rowHeight + 10;
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                "background"), null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; z-index: 0;",
                        null);

        //background table for the schedule grid
        writer.startElement(HTML.TABLE_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                "background"), null);
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "1", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "width: 100%; height: 100%",
                null);
        writer.startElement(HTML.TBODY_ELEM, schedule);

        //header row, containing the column names
        writer.startElement(HTML.TR_ELEM, schedule);
        writer.startElement(HTML.TD_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR,
                getStyleClass(schedule, "gutter"), null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "height: "
                                + rowHeight
                                + "px; border-style: none; border-width: 0px; overflow: hidden; padding: 0px",
                        null);
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer
                .writeAttribute(HTML.STYLE_ATTR, "height: 1px; width: 56px",
                        null);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);

        float columnWidth = (schedule.getModel().size() == 0) ? 100
                : (100 / schedule.getModel().size());

        for (Iterator dayIterator = schedule.getModel().iterator(); dayIterator
                .hasNext();)
        {
            ScheduleDay day = (ScheduleDay) dayIterator.next();
            writer.startElement(HTML.TD_ELEM, schedule);
            writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                    "header"), null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "height: " + headerHeight + "px; border-style: none; border-width: 0px; overflow: hidden;",
                            null);
            writer.writeAttribute(HTML.WIDTH_ATTR, String.valueOf(columnWidth)
                    + "%", null);
            writer.startElement(HTML.DIV_ELEM, schedule);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "position: relative; left: 0px; top: 0px; width: 100%; height: 100%;",
                            null);

            //write the date
            writer.startElement(HTML.SPAN_ELEM, schedule);
            writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                    "date"), null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "position: absolute; left: 0px; top: 0px; height: 15px; width: 100%; vertical-align: top; overflow: hidden; white-space: nowrap;",
                            null);
            writer.writeText(getDateString(context, schedule, day.getDate()),
                    null);
            writer.endElement(HTML.SPAN_ELEM);

            //write the name of the holiday, if there is one
            if ((day.getSpecialDayName() != null)
                    && (day.getSpecialDayName().length() > 0))
            {
                writer.startElement(HTML.SPAN_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                        "holiday"), null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "position: absolute; left: 0px; top: 15px; width: 100%; vertical-align: top; overflow: hidden; white-space: nowrap;",
                                null);
                writer.writeText(day.getSpecialDayName(), null);
                writer.endElement(HTML.SPAN_ELEM);
            }

            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);

        int numberOfRows = (schedule.getVisibleEndHour() - schedule
                .getVisibleStartHour()) * 2;

        for (int row = 0; row < numberOfRows; row++)
        {
            writer.startElement(HTML.TR_ELEM, schedule);

            //write the hours of the day on the left
            //this only happens on even rows, or every hour
            if ((row % 2) == 0)
            {
                writer.startElement(HTML.TD_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                        "gutter"), null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "height: "
                                        + rowHeight
                                        + "px; border-style: none; border-width: 0px; overflow: hidden; padding: 0px",
                                null);
                writer.writeAttribute("rowspan", "2", null);
                writer.startElement(HTML.SPAN_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                        "hours"), null);
                writer.writeAttribute(HTML.STYLE_ATTR,
                        "vertical-align: top; height: 100%; padding: 0px;",
                        null);
                writer.writeText(String.valueOf(schedule.getVisibleStartHour()
                        + (row / 2)), null);
                writer.endElement(HTML.SPAN_ELEM);
                writer.startElement(HTML.SPAN_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                        "minutes"), null);
                writer.writeAttribute(HTML.STYLE_ATTR,
                        "vertical-align: top; height: 100%; padding: 0px;",
                        null);
                writer.writeText("00", null);
                writer.endElement(HTML.SPAN_ELEM);
                writer.endElement(HTML.TD_ELEM);
            }

            //write the cells of the day columns on this row
            for (int column = 0; column < schedule.getModel().size(); column++)
            {
                writer.startElement(HTML.TD_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, getCellClass(schedule,
                        column, row), null);
                writer.writeAttribute(HTML.STYLE_ATTR,
                        "overflow: hidden; height: " + rowHeight + "px;", null);
                writer.writeAttribute(HTML.WIDTH_ATTR, String
                        .valueOf(columnWidth)
                        + "%", null);
                writer.write(HTML.NBSP_ENTITY);
                writer.endElement(HTML.TD_ELEM);
            }

            writer.endElement(HTML.TR_ELEM);
        }

        writer.endElement(HTML.TBODY_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeEntries(FacesContext context, HtmlSchedule schedule,
            ScheduleDay day, ResponseWriter writer) throws IOException
    {
        final UIForm parentForm = getParentForm(schedule);
        final String clientId = schedule.getClientId(context);
        final String formId = parentForm == null ? null : parentForm.getClientId(context);

        TreeSet entrySet = new TreeSet();

        for (Iterator entryIterator = day.iterator(); entryIterator.hasNext();)
        {
            entrySet.add(new EntryWrapper((ScheduleEntry) entryIterator.next(),
                    day));
        }

        EntryWrapper[] entries = (EntryWrapper[]) entrySet
                .toArray(new EntryWrapper[entrySet.size()]);

        //determine overlaps
        scanEntries(entries, 0);

        //determine the number of columns within this day
        int maxColumn = 0;

        for (Iterator entryIterator = entrySet.iterator(); entryIterator
                .hasNext();)
        {
            EntryWrapper wrapper = (EntryWrapper) entryIterator.next();
            maxColumn = Math.max(wrapper.column, maxColumn);
        }

        int numberOfColumns = maxColumn + 1;

        //make sure the entries take up all available space horizontally
        maximizeEntries(entries, numberOfColumns);

        //now determine the width in percent of 1 column
        float columnWidth = 100 / numberOfColumns;

        //and now draw the entries in the columns
        for (Iterator entryIterator = entrySet.iterator(); entryIterator
                .hasNext();)
        {
            EntryWrapper wrapper = (EntryWrapper) entryIterator.next();
            boolean selected = isSelected(schedule, wrapper);
            //compose the CSS style for the entry box
            StringBuffer entryStyle = new StringBuffer();
            entryStyle.append(wrapper.getBounds(schedule, columnWidth));
            String entryBorderColor = getEntryRenderer(schedule).getColor(
                    context, schedule, wrapper.entry, selected);
            if (entryBorderColor != null)
            {
                entryStyle.append(" border-color: ");
                entryStyle.append(entryBorderColor);
                entryStyle.append(";");
            }

            if (selected)
            {
                writer.startElement(HTML.DIV_ELEM, schedule);
                writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                        "entry-selected"), null);
                writer.writeAttribute(HTML.STYLE_ATTR, entryStyle.toString(),
                        null);

                //draw the tooltip
                if (showTooltip(schedule))
                {
                    getEntryRenderer(schedule).renderToolTip(context, writer,
                            schedule, wrapper.entry, selected);
                }

                //draw the content
                getEntryRenderer(schedule).renderContent(context, writer,
                        schedule, day, wrapper.entry, false, selected);
                writer.endElement(HTML.DIV_ELEM);
            }
            else
            {
                //if the schedule is read-only, the entries should not be
                //hyperlinks
                writer.startElement(
                        schedule.isReadonly() ? HTML.DIV_ELEM : HTML.ANCHOR_ELEM, schedule);

                //draw the tooltip
                if (showTooltip(schedule))
                {
                    getEntryRenderer(schedule).renderToolTip(context, writer,
                            schedule, wrapper.entry, selected);
                }

                if (!schedule.isReadonly())
                {
                    writer.writeAttribute("href", "#", null);

                    writer.writeAttribute(
                            HTML.ONMOUSEUP_ATTR,
                            "fireEntrySelected('"
                            + formId + "', '"
                            + clientId + "', '"
                            + wrapper.entry.getId()
                            + "');",
                            null);
                }

                writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                        "entry"), null);
                writer.writeAttribute(HTML.STYLE_ATTR, entryStyle.toString(),
                        null);

                //draw the content
                getEntryRenderer(schedule).renderContent(context, writer,
                        schedule, day, wrapper.entry, false, selected);

                writer.endElement(schedule.isReadonly() ? HTML.DIV_ELEM : "a");
            }
        }
    }

    private void writeForegroundEnd(FacesContext context,
            HtmlSchedule schedule, ResponseWriter writer) throws IOException
    {
        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeForegroundStart(FacesContext context,
            HtmlSchedule schedule, ResponseWriter writer) throws IOException
    {
        final int rowHeight = getRowHeight(schedule.getAttributes()) - 1;
        final int headerHeight = rowHeight + 10;
        final String clientId = schedule.getClientId(context);
        final UIForm parentForm = getParentForm(schedule);
        final String formId = parentForm == null ? null : parentForm.getClientId(context);

        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                "foreground"), null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;	z-index: 2;",
                        null);

        writer.startElement(HTML.TABLE_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                "foreground"), null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "1", null);
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "width: 100%; height: 100%",
                null);
        writer.startElement(HTML.TR_ELEM, schedule);
        writer.startElement(HTML.TD_ELEM, schedule);
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer
                .writeAttribute(HTML.STYLE_ATTR, "height: 1px; width: 56px",
                        null);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);

        float columnWidth = (schedule.getModel().size() == 0) ? 100
                : (100 / schedule.getModel().size());

        for (Iterator dayIterator = schedule.getModel().iterator(); dayIterator
                .hasNext();)
        {
            ScheduleDay day = (ScheduleDay) dayIterator.next();
            final String dayHeaderId = clientId + "_header_" + ScheduleUtil.getDateId(day.getDate());
            writer.startElement(HTML.TD_ELEM, schedule);
            writer.writeAttribute(HTML.ID_ATTR, dayHeaderId, null);
            writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule,
                    "header"), null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "height: " + headerHeight + "px; border-style: none; border-width: 0px; overflow: hidden;",
                            null);
            writer.writeAttribute(HTML.WIDTH_ATTR, String.valueOf(columnWidth)
                    + "%", null);
            //register an onclick event listener to a column header which will
            //be used to determine the date
            if (!schedule.isReadonly() && schedule.isSubmitOnClick()) {
                writer.writeAttribute(
                        HTML.ONMOUSEUP_ATTR,
                        "fireScheduleDateClicked(this, event, '"
                        + formId + "', '"
                        + clientId
                        + "');",
                        null);
            }

            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);

        writer.startElement(HTML.TR_ELEM, schedule);
        writer.startElement(HTML.TD_ELEM, schedule);
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer
                .writeAttribute(HTML.STYLE_ATTR, "height: 1px; width: 56px",
                        null);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);
    }

    //~ Inner Classes ----------------------------------------------------------

    protected String getRowHeightProperty()
    {
        return "detailedRowHeight";
    }

    protected int getDefaultRowHeight()
    {
        return defaultRowHeightInPixels;
    }

    /**
     * In the detailed day renderer, we take the y coordinate of the mouse
     * into account when determining the last clicked date.
     */
    protected Date determineLastClickedDate(HtmlSchedule schedule, String dateId, String yPos) {
        Calendar cal = GregorianCalendar.getInstance();
        //the dateId is the schedule client id + "_" + yyyyMMdd 
        String day = dateId.substring(dateId.lastIndexOf("_") + 1);
        Date date = ScheduleUtil.getDateFromId(day);
        
        if (date != null) cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, schedule.getVisibleStartHour());
        //OK, we have the date, let's determine the time
        try {
            int y = Integer.parseInt(yPos);
            int halfHourHeight = getRowHeight(schedule.getAttributes());
            int minutes = y * 30 / halfHourHeight;
            cal.add(Calendar.MINUTE, minutes);
        } catch (NumberFormatException nfe) {
            log.debug("y position is not a number");
        }
        log.debug("last clicked datetime: " + cal.getTime());
        return cal.getTime();
    }
    
    private class EntryWrapper implements Comparable
    {
        //~ Static fields/initializers -----------------------------------------

        private static final int HALF_HOUR = 1000 * 60 * 30;

        //~ Instance fields ----------------------------------------------------

        private final ScheduleDay day;
        private final ScheduleEntry entry;
        private final TreeSet overlappingEntries;
        private int colspan;
        private int column;

        //~ Constructors -------------------------------------------------------

        EntryWrapper(ScheduleEntry entry, ScheduleDay day)
        {
            this.entry = entry;
            this.day = day;
            this.column = 0;
            this.colspan = 1;
            this.overlappingEntries = new TreeSet();
        }

        //~ Methods ------------------------------------------------------------

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Object o)
        {
            return comparator.compare(entry, o);
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object o)
        {
            if (o instanceof EntryWrapper)
            {
                EntryWrapper other = (EntryWrapper) o;

                boolean returnboolean = (entry.getStartTime()
                        .equals(other.entry.getStartTime()))
                        && (entry.getEndTime().equals(other.entry.getEndTime()))
                        && (entry.getId().equals(other.entry.getId()))
                        && (day.equals(other.day));
                /*
                 new EqualsBuilder().append(
                 entry.getStartTime(), other.entry.getStartTime()
                 ).append(entry.getEndTime(), other.entry.getEndTime())
                 .append(
                 entry.getId(), other.entry.getId()
                 ).append(day, other.day).isEquals();
                 */
                return returnboolean;
            }

            return false;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            int returnint = entry.getStartTime().hashCode()
                    ^ entry.getEndTime().hashCode() ^ entry.getId().hashCode();

            return returnint;
        }

        /**
         * <p>
         * Determine the bounds of this entry, in CSS position attributes
         * </p>
         *
         * @param schedule the schedule
         * @param columnWidth the width of a column
         *
         * @return the bounds
         */
        String getBounds(HtmlSchedule schedule, float columnWidth)
        {
            int rowHeight = getRowHeight(schedule.getAttributes());
            float width = (columnWidth * colspan) - 0.5f;
            float left = column * columnWidth;
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(day.getDate());

            int curyear = cal.get(Calendar.YEAR);
            int curmonth = cal.get(Calendar.MONTH);
            int curday = cal.get(Calendar.DATE);

            cal.setTime(entry.getStartTime());
            cal.set(curyear, curmonth, curday);

            long startMillis = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, schedule.getVisibleStartHour());
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long visibleStartMillis = cal.getTimeInMillis();
            startMillis = day.equalsDate(entry.getStartTime()) ? Math.max(
                    startMillis, visibleStartMillis) : visibleStartMillis;
            cal.setTime(entry.getEndTime());
            cal.set(curyear, curmonth, curday);

            long endMillis = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, schedule.getVisibleEndHour());
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long visibleEndMillis = cal.getTimeInMillis();
            endMillis = day.equalsDate(entry.getEndTime()) ? Math.min(
                    endMillis, visibleEndMillis) : visibleEndMillis;

            int top = (int) (((startMillis - visibleStartMillis) * rowHeight) / HALF_HOUR);
            int height = (int) (((endMillis - startMillis) * rowHeight) / HALF_HOUR);
            StringBuffer buffer = new StringBuffer();

            if (height <= 0)
            {
                buffer.append("visibility: hidden; ");
            }
            buffer.append("position: absolute; height: ");
            buffer.append(height);
            buffer.append("px; top: ");
            buffer.append(top);
            buffer.append("px; left: ");
            buffer.append(left);
            buffer.append("%; width: ");
            buffer.append(width);
            buffer
                    .append("%; padding: 0px; overflow: hidden; border-width: 1.0px; border-style:solid;");

            return buffer.toString();
        }

        /**
         * <p>
         * Can this entry fit in the specified column?
         * </p>
         *
         * @param column the column
         *
         * @return whether the entry fits
         */
        boolean canFitInColumn(int column)
        {
            for (Iterator overlapIterator = overlappingEntries.iterator(); overlapIterator
                    .hasNext();)
            {
                EntryWrapper overlap = (EntryWrapper) overlapIterator.next();

                if (overlap.column == column)
                {
                    return false;
                }
            }

            return true;
        }

        /**
         * <p>
         * Does this entry overlap with another?
         * </p>
         *
         * @param other the other entry
         *
         * @return whether the entries overlap
         */
        boolean overlaps(EntryWrapper other)
        {
            if ((entry.getStartTime() == null) || (entry.getEndTime() == null))
            {
                return false;
            }

            boolean returnboolean = (entry.getStartTime().before(
                    other.entry.getEndTime()) && entry.getEndTime().after(
                    other.entry.getStartTime()));

            return returnboolean;
        }
    }
}
//The End
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
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.model.ScheduleDay;
import org.apache.myfaces.renderkit.html.HTML;


/**
 * <p>
 * Renderer for the month view of the Schedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision$
 */
public class ScheduleCompactMonthRenderer
    extends AbstractCompactScheduleRenderer
{
    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeBegin(
        FacesContext context,
        UIComponent component
    )
        throws IOException
    {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();

        //container div for the schedule grid
        writer.startElement(HTML.DIV_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, "schedule-compact-" + getTheme(schedule), null);
        writer.writeAttribute(
            HTML.STYLE_ATTR, "border-style: none; overflow: hidden;", null
        );

        writer.startElement(HTML.TABLE_ELEM, schedule);
        writer.writeAttribute(HTML.CLASS_ATTR, getStyleClass(schedule, "month"), null);
        writer.writeAttribute(
            HTML.STYLE_ATTR, "position: relative; left: 0px; top: 0px; width: 100%;",
            null
        );
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "1", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute(HTML.WIDTH_ATTR, "100%", null);
        writer.startElement(HTML.TBODY_ELEM, schedule);

        Calendar cal = GregorianCalendar.getInstance();

        for (
            Iterator dayIterator = schedule.getModel().iterator();
                dayIterator.hasNext();
        ) {
            ScheduleDay day = (ScheduleDay) dayIterator.next();
            cal.setTime(day.getDate());

            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            boolean isWeekend =
                (dayOfWeek == Calendar.SATURDAY) ||
                (dayOfWeek == Calendar.SUNDAY);

            cal.setTime(day.getDate());

            if (dayOfMonth == 1) { //fill the cells of the previous month

                TreeSet previousMonth = new TreeSet();

                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    cal.add(Calendar.DATE, -1);
                    previousMonth.add(new ScheduleDay(cal.getTime()));
                }

                for (
                    Iterator prevMonthIterator = previousMonth.iterator();
                        prevMonthIterator.hasNext();
                ) {
                    ScheduleDay d = (ScheduleDay) prevMonthIterator.next();
                    cal.setTime(d.getDate());

                    int dow = cal.get(Calendar.DAY_OF_WEEK);
                    int dom = cal.get(Calendar.DAY_OF_MONTH);
                    boolean w =
                        (dow == Calendar.SATURDAY) || (dow == Calendar.SUNDAY);
                    writeDayCell(
                        context, writer, schedule, d, dow, dom, w, false,
                        w ? 1 : 2
                    );
                }
            }

            writeDayCell(
                context, writer, schedule, day, dayOfWeek, dayOfMonth, isWeekend,
                true, isWeekend ? 1 : 2
            );

            if (!dayIterator.hasNext()) { //fill the empty cells of the next month

                while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    cal.add(Calendar.DATE, 1);

                    ScheduleDay d = new ScheduleDay(cal.getTime());
                    int dow = cal.get(Calendar.DAY_OF_WEEK);
                    int dom = cal.get(Calendar.DAY_OF_MONTH);
                    boolean w =
                        (dow == Calendar.SATURDAY) || (dow == Calendar.SUNDAY);
                    writeDayCell(
                        context, writer, schedule, d, dow, dom, w, false,
                        w ? 1 : 2
                    );
                }
            }
        }

        writer.endElement(HTML.TBODY_ELEM);
        writer.endElement(HTML.TABLE_ELEM);

        writer.endElement(HTML.DIV_ELEM);
    }

    /**
     * @see AbstractCompactScheduleRenderer#getDefaultRowHeight()
     */
    protected int getDefaultRowHeight()
    {
        return 120;
    }

    /**
     * @see AbstractCompactScheduleRenderer#getRowHeightProperty()
     */
    protected String getRowHeightProperty()
    {
        return "compactMonthRowHeight";
    }

    /**
     */
    protected void writeDayCell(
        FacesContext context,
        ResponseWriter writer,
        HtmlSchedule schedule,
        ScheduleDay day,
        int dayOfWeek,
        int dayOfMonth,
        boolean isWeekend,
        boolean isCurrentMonth,
        int rowspan
    )
        throws IOException
    {
        if ((dayOfWeek == Calendar.MONDAY) || (dayOfWeek == Calendar.SUNDAY)) {
            writer.startElement(HTML.TR_ELEM, schedule);
        }

        super.writeDayCell(
            context, writer, schedule, day, 100f / 6, dayOfWeek, dayOfMonth,
            isWeekend, isCurrentMonth, rowspan
        );

        if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)) {
            writer.endElement(HTML.TR_ELEM);
        }
    }
}
//The End

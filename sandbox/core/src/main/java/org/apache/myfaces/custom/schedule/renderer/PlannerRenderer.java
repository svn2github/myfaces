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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.custom.schedule.HtmlPlanner;
import org.apache.myfaces.custom.schedule.model.Day;
import org.apache.myfaces.custom.schedule.model.PlannerEntity;
import org.apache.myfaces.custom.schedule.model.ScheduleEntry;
import org.apache.myfaces.custom.schedule.util.ScheduleEntryComparator;
import org.apache.myfaces.renderkit.html.HTML;

/**
 * <p>
 * Renderer for the UIPlanner component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision$
 */
public class PlannerRenderer extends Renderer
{
    //~ Static fields/initializers ---------------------------------------------

    private static final ScheduleEntryComparator comparator = new ScheduleEntryComparator();

    //~ Instance fields --------------------------------------------------------

    private final int ROW_HEIGHT_IN_PIXELS = 22;

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

        HtmlPlanner planner = (HtmlPlanner) component;
        ResponseWriter writer = context.getResponseWriter();
        
        //determine the CSS file for the chosen theme
        String theme = getTheme(planner);
        if (theme == null || theme.length() < 1) theme = "default";
        String css = "css/" + theme + ".css";
        
        //add needed CSS and Javascript files to the header 

        AddResource addResource = AddResource.getInstance(context);
        addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, HtmlPlanner.class, css);
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, 
                HtmlPlanner.class, "javascript/alphaAPI.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlPlanner.class, "javascript/domLib.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlPlanner.class, "javascript/domTT.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlPlanner.class, "javascript/fadomatic.js");


        int numberOfRows = planner.getModel().numberOfEntities();

        int gridHeight = (numberOfRows * 22) + 3 + 47;

        writer.startElement(HTML.DIV_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "planner", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "height: "
                + String.valueOf(gridHeight) + "px; overflow: hidden;", null);
        writeBackground(context, planner, writer);
        writeForegroundStart(context, planner, writer);
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

        HtmlPlanner planner = (HtmlPlanner) component;
        ResponseWriter writer = context.getResponseWriter();

        for (Iterator entityIterator = planner.getModel().entityIterator(); entityIterator
                .hasNext();)
        {
            PlannerEntity entity = (PlannerEntity) entityIterator.next();
            writer.startElement(HTML.TR_ELEM, planner);
            writer.startElement(HTML.TD_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "gutter", null);
            writer.startElement(HTML.DIV_ELEM, planner);
            writer.writeAttribute(HTML.STYLE_ATTR, "height: 1px; width: "
                    + String.valueOf(getGutterWidth(planner))
                    + "px", null);
            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.TD_ELEM);

            int numberOfDays = planner.getModel().numberOfDays();
            float columnWidth = (numberOfDays == 0) ? 100f
                    : (100 / numberOfDays);

            for (Iterator dayIterator = planner.getModel().dayIterator(); dayIterator
                    .hasNext();)
            {
                Day day = (Day) dayIterator.next();
                writer.startElement(HTML.TD_ELEM, planner);
                writer.writeAttribute(HTML.CLASS_ATTR, "row", null);
                writer.writeAttribute(HTML.WIDTH_ATTR, String
                        .valueOf(columnWidth)
                        + "%", null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "height: 21px; border-left-style: solid; border-left-width: 1px; border-top-style: none; border-right-style: none; border-bottom-style: none;",
                                null);
                writer.startElement(HTML.DIV_ELEM, null);
                writer.writeAttribute(HTML.CLASS_ATTR, "row", null);
                writer
                        .writeAttribute(
                                HTML.STYLE_CLASS_ATTR,
                                "position: relative; top: 0px; left: 0px; width: 100%; height: 100%; padding: 0px; z-index: 0; vertical-align: middle;",
                                null);
                writeEntries(context, planner, entity, day, writer);
                writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.TD_ELEM);
            }

            writer.endElement(HTML.TR_ELEM);
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

        HtmlPlanner planner = (HtmlPlanner) component;
        ResponseWriter writer = context.getResponseWriter();
        writeForegroundEnd(context, planner, writer);
        writer.endElement(HTML.DIV_ELEM);
    }

    /**
     * <p>
     * Determine the width of the left gutter
     * </p>
     *
     * @param component the component
     *
     * @return the gutter width in pixels
     */
    protected int getGutterWidth(UIComponent component)
    {
        try
        {
            //first check if the gutterWidthInPixels property is a
            //value binding expression
            ValueBinding binding = component.getValueBinding("gutterWidthInPixels");
            if (binding != null) {
                Integer value =
                    (Integer) binding.getValue(
                        FacesContext.getCurrentInstance()
                    );

                if (value != null) {
                    return value.intValue();
                }
            }
            //it's not a value binding expression, so check for the string value
            //in the attributes
            Map attributes = component.getAttributes();
            Integer width = Integer.valueOf((String) attributes
                    .get("gutterWidthInPixels"));

            return width.intValue();
        }
        catch (NumberFormatException nfe)
        {
            return 200;
        }
    }

    /**
     * <p>
     * The date format that is used in the planner header
     * </p>
     *
     * @param component the component
     *
     * @return Returns the headerDateFormat.
     */
    protected String getHeaderDateFormat(UIComponent component)
    {
        //first check if the headerDateFormat property is a value binding expression
        ValueBinding binding = component.getValueBinding("headerDateFormat");
        if (binding != null) {
            String value =
                (String) binding.getValue(
                    FacesContext.getCurrentInstance()
                );

            if (value != null) {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return (String) attributes.get("headerDateFormat");
    }

    /**
     * <p>
     * Get the parent form of the planner component
     * </p>
     *
     * @param component the component
     *
     * @return the parent form
     */
    protected UIForm getParentForm(UIComponent component)
    {
        UIComponent parent = component.getParent();

        while (parent != null)
        {
            if (parent instanceof UIForm)
            {
                break;
            }

            parent = parent.getParent();
        }

        return (UIForm) parent;
    }

    /**
     * <p>
     * Should the legend be rendered?
     * </p>
     *
     * TODO the rendering of the legend has not been implemented yet
     *
     * @param component the component
     *
     * @return whether to render the legend
     */
    protected boolean showLegend(UIComponent component)
    {
        //first check if the tooltip property is a value binding expression
        ValueBinding binding = component.getValueBinding("legend");
        if (binding != null) {
            Boolean value =
                (Boolean) binding.getValue(
                    FacesContext.getCurrentInstance()
                );

            if (value != null) {
                return value.booleanValue();
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return Boolean.valueOf((String) attributes.get("legend"))
                .booleanValue();
    }

    /**
     * <p>
     * The theme used when rendering the planner
     * </p>
     * 
     * @param component the component
     * 
     * @return the theme
     */
    protected String getTheme(UIComponent component) {
        //first check if the theme property is a value binding expression
        ValueBinding binding = component.getValueBinding("theme");
        if (binding != null) {
            String value =
                (String) binding.getValue(
                    FacesContext.getCurrentInstance()
                );

            if (value != null) {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return (String)attributes.get("theme");
    }
    
    /**
     * <p>
     * Should the tooltip be rendered?
     * </p>
     *
     * @param component the component
     *
     * @return whether or not tooltips should be rendered
     */
    protected boolean showTooltip(UIComponent component)
    {
        //first check if the tooltip property is a value binding expression
        ValueBinding binding = component.getValueBinding("tooltip");
        if (binding != null) {
            Boolean value =
                (Boolean) binding.getValue(
                    FacesContext.getCurrentInstance()
                );

            if (value != null) {
                return value.booleanValue();
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return Boolean.valueOf((String) attributes.get("tooltip")).booleanValue();
    }

    private String getCellClass(HtmlPlanner planner, Day day, int hour)
    {
        if (!day.isWorkingDay())
        {
            return "free";
        }

        if ((hour >= planner.getWorkingStartHour())
                && (hour < planner.getWorkingEndHour()))
        {
            return ((hour % 2) == 0) ? "even" : "uneven";
        }

        return "free";
    }

    private String getDateString(FacesContext context, UIComponent component, Date date)
    {
        DateFormat format;
        String pattern = getHeaderDateFormat(component);

        if ((pattern != null) && (pattern.length() > 0))
        {
            format = new SimpleDateFormat(pattern);
        }
        else
        {
            if (context.getApplication().getDefaultLocale() != null)
            {
                format = DateFormat.getDateInstance(DateFormat.MEDIUM, context
                        .getApplication().getDefaultLocale());
            }
            else
            {
                format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            }
        }

        return format.format(date);
    }

    private String getTooltipText(ScheduleEntry entry, UIComponent component)
    {
        if (!showTooltip(component))
        {
            return null;
        }

         StringBuffer buffer = new StringBuffer();
         buffer.append(
         "return makeTrue(domTT_activate(this, event, 'caption', '"
         );

         if (entry.getTitle() != null) {
         buffer.append(escape(entry.getTitle()));
         }

         buffer.append("', 'content', '<i>");

         if (entry.getSubtitle() != null) {
         buffer.append(escape(entry.getSubtitle()));
         }

         buffer.append("</i>");

         if (entry.getDescription() != null) {
         buffer.append("<br/>");
         buffer.append(escape(entry.getDescription()));
         }

         buffer.append("', 'trail', true));");

         return buffer.toString();
    }

    private boolean containsEntry(HtmlPlanner planner, Day day,
            ScheduleEntry entry)
    {
        if ((day == null) || (entry == null))
        {
            return false;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(day.getDate());
        cal.set(Calendar.HOUR_OF_DAY, planner.getVisibleStartHour());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date dayStart = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, planner.getVisibleEndHour());

        Date dayEnd = cal.getTime();

        return entry.getEndTime().after(dayStart)
                && entry.getStartTime().before(dayEnd);
    }

    private String escape(String text)
    {
        if (text == null)
        {
            return null;
        }

        return text.replaceAll("'", "&quot;");
    }

    /**
     * <p>
     * Draw the planner background, i.e. the grid
     * </p>
     *
     * @param context the FacesContext
     * @param planner the Planner component
     * @param writer the ResponseWriter
     *
     * @throws IOException when the background cannot be drawn
     * @throws NullPointerException
     */
    private void writeBackground(FacesContext context, HtmlPlanner planner,
            ResponseWriter writer) throws IOException
    {
        //a planner component should always be inside a UIForm
        UIForm parentForm = getParentForm(planner);

        if (parentForm == null)
        {
            throw new NullPointerException("No parent UIForm found");
        }

        writer.startElement(HTML.DIV_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "background", null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; z-index: 0;",
                        null);

        //background table for the schedule grid
        writer.startElement(HTML.TABLE_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "background", null);
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "1", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "width: 100%; height: 100%;",
                null);
        writer.startElement(HTML.TBODY_ELEM, planner);

        //header row, containing the day names
        writer.startElement(HTML.TR_ELEM, planner);
        writer.startElement(HTML.TD_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "gutter", null);
        writer.writeAttribute("rowspan", "2", null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "padding: 0px; vertical-align: middle; height: 21px; border-style: none; border-width: 0px; overflow: hidden; whitespace: nowrap;",
                        null);
        writer.startElement(HTML.DIV_ELEM, planner);
        writer.writeAttribute(HTML.STYLE_ATTR, "height: 1px; width: "
                + String.valueOf(getGutterWidth(planner))
                + "px", null);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);

        int numberOfDays = planner.getModel().numberOfDays();
        float columnWidth = (numberOfDays == 0) ? 100f : (100 / numberOfDays);

        for (Iterator dayIterator = planner.getModel().dayIterator(); dayIterator
                .hasNext();)
        {
            Day day = (Day) dayIterator.next();
            writer.startElement(HTML.TD_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "header", null);
            writer.writeAttribute(HTML.WIDTH_ATTR, String.valueOf(columnWidth)
                    + "%", null);
            writer.writeAttribute(HTML.STYLE_ATTR,
                    "overflow: hidden; height: 31px;", null);
            writer.startElement(HTML.DIV_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "header", null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "position: relative; left: 0px; top: 0px; width: 100%; height: 100%;",
                            null);
            writer.startElement(HTML.SPAN_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "date", null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "position: absolute; left: 0px; top: 0px; width: 100%; height: 15px; overflow: hidden; white-space: nowrap;",
                            null);
            writer.writeText(getDateString(context, planner,
                    day.getDate()), null);
            writer.endElement(HTML.SPAN_ELEM);

            //write the name of the holiday, if there is one
            if ((day.getSpecialDayName() != null)
                    && (day.getSpecialDayName().length() > 0))
            {
                writer.startElement(HTML.SPAN_ELEM, planner);
                writer.writeAttribute(HTML.CLASS_ATTR, "holiday", null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "position: absolute; left: 0px; top: 15px; width: 100%; overflow: hidden; white-space: nowrap;",
                                null);
                writer.writeText(day.getSpecialDayName(), null);
                writer.endElement(HTML.SPAN_ELEM);
            }

            writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);

        //second header row, containing the hours
        writer.startElement(HTML.TR_ELEM, planner);

        int numberOfHours = planner.getVisibleEndHour()
                - planner.getVisibleStartHour();
        float hourWidth = (numberOfHours <= 0) ? 100f : (100 / numberOfHours);

        for (Iterator dayIterator = planner.getModel().dayIterator(); dayIterator
                .hasNext();)
        {
            Day day = (Day) dayIterator.next();
            writer.startElement(HTML.TD_ELEM, planner);
            writer.writeAttribute(HTML.WIDTH_ATTR, String.valueOf(columnWidth)
                    + "%", null);
            writer.startElement(HTML.TABLE_ELEM, planner);
            writer.writeAttribute(HTML.STYLE_ATTR, "height: 100%; width: 100%",
                    null);
            writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
            writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
            writer.writeAttribute("border", "0", null);
            writer.startElement(HTML.TR_ELEM, planner);

            for (int hcol = planner.getVisibleStartHour(); hcol < planner
                    .getVisibleEndHour(); hcol++)
            {
                writer.startElement(HTML.TD_ELEM, planner);
                writer.writeAttribute(HTML.CLASS_ATTR, "hours", null);
                writer.writeAttribute(HTML.WIDTH_ATTR, String
                        .valueOf(hourWidth)
                        + "%", null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "overflow: hidden; height: 15px; border-left-style: solid; border-left-width: 1px; border-top-style: none; border-right-style: none; border-bottom-style: none;",
                                null);
                writer.startElement(HTML.DIV_ELEM, planner);
                writer.writeAttribute(HTML.CLASS_ATTR, "header", null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "position: relative; left: 0px; top: 0px; width: 100%; height: 100%;",
                                null);
                writer.startElement(HTML.SPAN_ELEM, planner);
                writer.writeAttribute(HTML.CLASS_ATTR, "hours", null);
                writer
                        .writeAttribute(
                                HTML.STYLE_ATTR,
                                "position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; overflow: hidden; white-space: nowrap;",
                                null);
                writer.writeText(String.valueOf(hcol) + ":00", null);
                writer.endElement(HTML.SPAN_ELEM);
                writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.TD_ELEM);
            }

            writer.endElement(HTML.TR_ELEM);
            writer.endElement(HTML.TABLE_ELEM);
            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);

        //the actual entity rows start here
        for (Iterator entityIterator = planner.getModel().entityIterator(); entityIterator
                .hasNext();)
        {
            PlannerEntity entity = (PlannerEntity) entityIterator.next();
            writer.startElement(HTML.TR_ELEM, planner);
            writer.startElement(HTML.TD_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "gutter", null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "padding: 0px; vertical-align: middle; height: 21px; border-style: none; border-width: 0px; overflow: hidden; whitespace: nowrap;",
                            null);
            writer.startElement(HTML.SPAN_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "title", null);
            writer.writeAttribute(HTML.STYLE_ATTR, "height: 100%;", null);
            writer.writeText(entity.getName(), null);
            writer.endElement(HTML.SPAN_ELEM);
            writer.endElement(HTML.TD_ELEM);

            for (Iterator dayIterator = planner.getModel().dayIterator(); dayIterator
                    .hasNext();)
            {
                Day day = (Day) dayIterator.next();
                writer.startElement(HTML.TD_ELEM, planner);
                writer.writeAttribute(HTML.WIDTH_ATTR, String
                        .valueOf(columnWidth)
                        + "%", null);
                writer.startElement(HTML.TABLE_ELEM, planner);
                writer.writeAttribute(HTML.STYLE_ATTR,
                        "height: 100%; width: 100%", null);
                writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
                writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
                writer.writeAttribute("border", "0", null);
                writer.startElement(HTML.TR_ELEM, planner);

                for (int hcol = planner.getVisibleStartHour(); hcol < planner
                        .getVisibleEndHour(); hcol++)
                {
                    writer.startElement(HTML.TD_ELEM, planner);
                    writer.writeAttribute(HTML.CLASS_ATTR, getCellClass(
                            planner, day, hcol), null);
                    writer.writeAttribute(HTML.WIDTH_ATTR, String
                            .valueOf(hourWidth)
                            + "%", null);
                    writer
                            .writeAttribute(
                                    HTML.STYLE_ATTR,
                                    "height: 21px; overflow: hidden; border-left-style: solid; border-left-width: 1px; border-top-style: none; border-right-style: none; border-bottom-style: none;",
                                    null);
                    writer.write(HTML.NBSP_ENTITY);
                    writer.endElement(HTML.TD_ELEM);
                }

                writer.endElement(HTML.TR_ELEM);
                writer.endElement(HTML.TABLE_ELEM);
                writer.endElement(HTML.TD_ELEM);
            }

            writer.endElement(HTML.TR_ELEM);
        }

        writer.endElement(HTML.TBODY_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    /**
     * <p>
     * Draw the entries on the planner
     * </p>
     *
     * @param context the FacesContext
     * @param planner the Planner component
     * @param entity the planner entity
     * @param day the day
     * @param writer the responsewriter
     *
     * @throws IOException when the entries cannot be drawn
     */
    private void writeEntries(FacesContext context, HtmlPlanner planner,
            PlannerEntity entity, Day day, ResponseWriter writer)
            throws IOException
    {
        for (Iterator entryIterator = entity.iterator(); entryIterator
                .hasNext();)
        {
            ScheduleEntry entry = (ScheduleEntry) entryIterator.next();

            if (containsEntry(planner, day, entry))
            {
                EntryWrapper wrapper = new EntryWrapper(entry, day, entity);
                writer.startElement(HTML.DIV_ELEM, planner);

                //draw the tooltip
                if (showTooltip(planner))
                {
                    writer.writeAttribute("onmouseover", getTooltipText(
                            wrapper.entry, planner), null);
                }

                writer.writeAttribute(HTML.CLASS_ATTR, "entry", null);
                writer.writeAttribute(HTML.STYLE_ATTR, wrapper
                        .getBounds(planner), null);
                writer.endElement(HTML.DIV_ELEM);
            }
        }
    }

    private void writeForegroundEnd(FacesContext context, HtmlPlanner planner,
            ResponseWriter writer) throws IOException
    {
        writer.endElement(HTML.TBODY_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void writeForegroundStart(FacesContext context,
            HtmlPlanner planner, ResponseWriter writer) throws IOException
    {
        writer.startElement(HTML.DIV_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "foreground", null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "position: absolute; left: 0px; top: 0px; width: 100%; height: 100%; z-index: 1;",
                        null);

        //foreground table for the planner grid
        writer.startElement(HTML.TABLE_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "foreground", null);
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "1", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "width: 100%; height: 100%;",
                null);
        writer.startElement(HTML.TBODY_ELEM, planner);

        //header row containing the day names
        writer.startElement(HTML.TR_ELEM, planner);
        writer.startElement(HTML.TD_ELEM, planner);
        writer.writeAttribute(HTML.CLASS_ATTR, "gutter", null);
        writer.writeAttribute("rowspan", "2", null);
        writer
                .writeAttribute(
                        HTML.STYLE_ATTR,
                        "padding: 0px; vertical-align: middle; height: 21px; border-style: none; border-width: 0px; overflow: hidden; whitespace: nowrap;",
                        null);
        writer.startElement(HTML.DIV_ELEM, planner);
        writer.writeAttribute(HTML.STYLE_ATTR, "height: 1px; width: "
                + String.valueOf(getGutterWidth(planner))
                + "px", null);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.TD_ELEM);

        int numberOfDays = planner.getModel().numberOfDays();
        float columnWidth = (numberOfDays == 0) ? 100f : (100 / numberOfDays);

        for (Iterator dayIterator = planner.getModel().dayIterator(); dayIterator
                .hasNext();)
        {
            Day day = (Day) dayIterator.next();
            writer.startElement(HTML.TD_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "header", null);
            writer.writeAttribute(HTML.WIDTH_ATTR, String.valueOf(columnWidth)
                    + "%", null);
            writer.writeAttribute(HTML.STYLE_ATTR,
                    "overflow: hidden; height: 31px;", null);
            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);

        //header row containing the hours
        writer.startElement(HTML.TR_ELEM, planner);

        for (Iterator dayIterator = planner.getModel().dayIterator(); dayIterator
                .hasNext();)
        {
            Day day = (Day) dayIterator.next();
            writer.startElement(HTML.TD_ELEM, planner);
            writer.writeAttribute(HTML.CLASS_ATTR, "hours", null);
            writer.writeAttribute(HTML.WIDTH_ATTR, String.valueOf(columnWidth)
                    + "%", null);
            writer
                    .writeAttribute(
                            HTML.STYLE_ATTR,
                            "overflow: hidden; height: 15px; border-style: none; border-width: 0px",
                            null);
            writer.endElement(HTML.TD_ELEM);
        }

        writer.endElement(HTML.TR_ELEM);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     *
     */
    private class EntryWrapper implements Comparable
    {
        //~ Static fields/initializers -----------------------------------------

        private static final int HOUR = 1000 * 3600;

        //~ Instance fields ----------------------------------------------------

        private final Day day;
        private final PlannerEntity entity;
        private final ScheduleEntry entry;

        //~ Constructors -------------------------------------------------------

        EntryWrapper(ScheduleEntry entry, Day day, PlannerEntity entity)
        {
            this.entry = entry;
            this.day = day;
            this.entity = entity;
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
                 ).append(day, other.day).isEquals();*/

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
         * Determine the bounds of the entry
         * </p>
         *
         * @param planner the planner component
         *
         * @return the bounds, as CSS position attributes
         */
        String getBounds(HtmlPlanner planner)
        {
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(day.getDate());

            int curyear = cal.get(Calendar.YEAR);
            int curmonth = cal.get(Calendar.MONTH);
            int curday = cal.get(Calendar.DATE);

            cal.setTime(entry.getStartTime());
            cal.set(curyear, curmonth, curday);

            long startMillis = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, planner.getVisibleStartHour());
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long visibleStartMillis = cal.getTimeInMillis();
            startMillis = day.equalsDate(entry.getStartTime()) ? Math.max(
                    startMillis, visibleStartMillis) : visibleStartMillis;
            cal.setTime(entry.getEndTime());
            cal.set(curyear, curmonth, curday);

            long endMillis = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, planner.getVisibleEndHour());
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long visibleEndMillis = cal.getTimeInMillis();
            endMillis = day.equalsDate(entry.getEndTime()) ? Math.min(
                    endMillis, visibleEndMillis) : visibleEndMillis;

            float left = (float) ((startMillis - visibleStartMillis) * 100)
                    / (float) (visibleEndMillis - visibleStartMillis);
            float width = (float) ((endMillis - startMillis) * 100)
                    / (float) (visibleEndMillis - visibleStartMillis);
            StringBuffer buffer = new StringBuffer();
            buffer
                    .append("position: absolute; padding: 0px; top: 0px; height: 21px; left: ");
            buffer.append(String.valueOf(left));
            buffer.append("%; width: ");
            buffer.append(String.valueOf(width));
            buffer
                    .append("%; overflow: hidden; border-style-none; border-width: 0px;");

            return buffer.toString();
        }
    }
}
//The End

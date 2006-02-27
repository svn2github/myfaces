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
import java.util.Date;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.util.ScheduleEntryComparator;
import org.apache.myfaces.custom.schedule.util.ScheduleUtil;
import org.apache.myfaces.renderkit.html.HTML;

/**
 * <p>
 * Abstract superclass for all renderer of the UISchedule component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision$
 */
public abstract class AbstractScheduleRenderer extends Renderer
{
    //~ Static fields/initializers ---------------------------------------------

    protected static final ScheduleEntryComparator comparator = new ScheduleEntryComparator();

    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void decode(FacesContext context, UIComponent component)
    {
        if (ScheduleUtil.canModifyValue(component))
        {
            HtmlSchedule schedule = (HtmlSchedule) component;
            Map parameters = context.getExternalContext()
                    .getRequestParameterMap();
            String selectedEntryId = (String) parameters.get((String) schedule
                    .getClientId(context));

            if ((selectedEntryId != null) && (selectedEntryId.length() > 0))
            {
                schedule.setSubmittedEntry(schedule.findEntry(selectedEntryId));
                schedule.queueEvent(new ActionEvent(schedule));
            }
        }
    }

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

        HtmlSchedule schedule = (HtmlSchedule) component;
        ResponseWriter writer = context.getResponseWriter();

        String css = "css/schedule.css";

        //add needed CSS and Javascript files to the header 

        AddResource addResource = AddResourceFactory.getInstance(context);
        addResource.addStyleSheet(context, AddResource.HEADER_BEGIN,
                HtmlSchedule.class, css);
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlSchedule.class, "javascript/alphaAPI.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlSchedule.class, "javascript/domLib.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlSchedule.class, "javascript/domTT.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,
                HtmlSchedule.class, "javascript/fadomatic.js");

        //hidden input field containing the id of the selected entry
        writer.startElement(HTML.INPUT_ELEM, schedule);
        writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
        writer.writeAttribute(HTML.NAME_ATTR, schedule.getClientId(context),
                "clientId");
        writer.endElement(HTML.INPUT_ELEM);
    }

    /**
     * <p>
     * Get the String representation of a date, taking into account the
     * specified date format or the current Locale.
     * </p>
     *
     * @param context the FacesContext
     * @param component the component
     * @param date the date
     *
     * @return the date string
     */
    protected String getDateString(FacesContext context, UIComponent component,
            Date date)
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

    /**
     * <p>
     * The theme used when rendering the schedule
     * </p>
     * 
     * @param component the component
     * 
     * @return the theme
     */
    protected String getTheme(UIComponent component)
    {
        //first check if the theme property is a value binding expression
        ValueBinding binding = component.getValueBinding("theme");
        if (binding != null)
        {
            String value = (String) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        String theme = (String) attributes.get("theme");
        if (theme == null || theme.length() < 1)
            theme = "default";
        return theme;
    }

    /**
     * <p>
     * Allow the developer to specify custom CSS classnames for the schedule
     * component.
     * </p>
     * @param component the component
     * @param className the default CSS classname
     * @return the custom classname
     */
    protected String getStyleClass(UIComponent component, String className)
    {
        //first check if the styleClass property is a value binding expression
        ValueBinding binding = component.getValueBinding(className);
        if (binding != null)
        {
            String value = (String) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value;
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        String returnValue = (String) attributes.get(className);
        return returnValue == null ? className : returnValue;
    }

    /**
     * <p>
     * The date format that is used in the schedule header
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
        if (binding != null)
        {
            String value = (String) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
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
     * Get the parent form of the schedule component
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
     * Should the tooltip be made visible?
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
        if (binding != null)
        {
            Boolean value = (Boolean) binding.getValue(FacesContext
                    .getCurrentInstance());

            if (value != null)
            {
                return value.booleanValue();
            }
        }
        //it's not a value binding expression, so check for the string value
        //in the attributes
        Map attributes = component.getAttributes();
        return Boolean.valueOf((String) attributes.get("tooltip"))
                .booleanValue();
    }

    /**
     * The user of the Schedule component can customize the look and feel
     * by specifying a custom implementation of the ScheduleEntryRenderer.
     * This method gets an instance of the specified class, or if none
     * was specified, takes the default.
     * 
     * @param component the Schedule component
     * @return a ScheduleEntryRenderer instance
     */
    protected ScheduleEntryRenderer getEntryRenderer(UIComponent component)
    {
        ScheduleEntryRenderer entryRenderer = null;
        Map attributes = component.getAttributes();
        //First check if there an entryRenderer has already been instantiated
        Object rendererObject = attributes.get("entryRendererInstance");
        if (rendererObject instanceof ScheduleEntryRenderer)
        {
            entryRenderer = (ScheduleEntryRenderer) rendererObject;
        }
        else
        {
            //No entryRenderer was instantiated, do that here
            String className = null;
            //Was the classname specified as attribute of the component?
            ValueBinding binding = component.getValueBinding("entryRenderer");
            if (binding != null)
            {
                className = (String) binding.getValue(FacesContext
                        .getCurrentInstance());
            }
            if (className == null)
            {
                className = (String) attributes.get("entryRenderer");
            }
            try
            { //try to instantiate a renderer of the specified class
                entryRenderer = (ScheduleEntryRenderer) Class
                        .forName(className).newInstance();
            }
            catch (Exception e)
            {
                //something went wrong, let's take the default
                entryRenderer = new DefaultScheduleEntryRenderer();
            }
            //Store the instance in the component attributes for later use
            attributes.put("entryRendererInstance", entryRenderer);
        }
        return entryRenderer;
    }

    /**
     * @return The default height, in pixels, of one row in the schedule grid
     */
    protected abstract int getDefaultRowHeight();

    /**
     * @return The name of the property that determines the row height
     */
    protected abstract String getRowHeightProperty();

    /**
     * @param attributes
     *            The attributes
     * 
     * @return The row height, in pixels
     */
    protected int getRowHeight(Map attributes)
    {
        int rowHeight = 0;

        try
        {
            Integer rowHeightObject = (Integer) attributes
                    .get(getRowHeightProperty());
            rowHeight = rowHeightObject.intValue();
        }
        catch (Exception e)
        {
            rowHeight = 0;
        }

        if (rowHeight == 0)
        {
            rowHeight = getDefaultRowHeight();
        }

        return rowHeight;
    }
}
//The End

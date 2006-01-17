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
import javax.faces.webapp.UIComponentTag;

/**
 * <p>
 * JSP tag for the UIPlanner component
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$
 */
public class PlannerTag extends UIComponentTag
{
    //~ Instance fields --------------------------------------------------------

    private String gutterWidthInPixels;
    private String headerDateFormat;
    private String legend;
    private String rendered;
    private String theme;
    private String tooltip;
    private String value;
    private String visibleEndHour;
    private String visibleStartHour;
    private String workingEndHour;
    private String workingStartHour;

    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType()
    {
        return HtmlPlanner.COMPONENT_TYPE;
    }

    /**
     * <p>
     * The width of the gutter in pixels
     * </p>
     *
     * @param gutterWidthInPixels The gutterWidthInPixels to set.
     */
    public void setGutterWidthInPixels(String gutterWidthInPixels)
    {
        this.gutterWidthInPixels = gutterWidthInPixels;
    }

    /**
     * <p>
     * The width of the gutter in pixels
     * </p>
     *
     * @return Returns the gutterWidthInPixels.
     */
    public String getGutterWidthInPixels()
    {
        return gutterWidthInPixels;
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
     * Should a legend be displayed?
     * </p>
     *
     * @param legend The legend to set.
     */
    public void setLegend(String legend)
    {
        this.legend = legend;
    }

    /**
     * <p>
     * Should a legend be displayed?
     * </p>
     *
     * @return Returns the legend.
     */
    public String getLegend()
    {
        return legend;
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
        return UIPlanner.DEFAULT_RENDERER_TYPE;
    }

    /**
     * <p>
     * The theme of the planner component. This is the name of the
     * CSS file that should be loaded when rendering the planner.
     * Possible values are 'default', 'outlookxp', 'outlook2003',
     * 'evolution'.
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
     * The theme of the planner component. This is the name of the
     * CSS file that should be loaded when rendering the planner.
     * Possible values are 'default', 'outlookxp', 'outlook2003',
     * 'evolution'.
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
     * The value of the Planner component
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
     * The value of the Planner component
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
     * The visible end hour of the planner component
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
     * The visible end hour of the planner component
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
     * The visible start hour of the planner component
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
     * The visible start hour of the planner component
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
     * The working end hour of the planner component
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
     * The working end hour of the planner component
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
     * The working start hour of the planner component
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
     * The working start hour of the planner component
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
        theme = null;
        tooltip = null;
        legend = null;
        gutterWidthInPixels = null;
        rendered = null;
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        HtmlPlanner planner = (HtmlPlanner) component;
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();

        if (rendered != null)
        {
            if (isValueReference(rendered))
            {
                planner.setValueBinding("rendered", app
                        .createValueBinding(rendered));
            }
            else
            {
                planner.setRendered(Boolean.valueOf(rendered).booleanValue());
            }
        }

        if (visibleStartHour != null)
        {
            if (isValueReference(visibleStartHour))
            {
                planner.setValueBinding("visibleStartHour", app
                        .createValueBinding(visibleStartHour));
            }
            else
            {
                planner.setVisibleStartHour(new Integer(visibleStartHour)
                        .intValue());
            }
        }

        if (visibleEndHour != null)
        {
            if (isValueReference(visibleEndHour))
            {
                planner.setValueBinding("visibleEndHour", app
                        .createValueBinding(visibleEndHour));
            }
            else
            {
                planner.setVisibleEndHour(new Integer(visibleEndHour)
                        .intValue());
            }
        }

        if (workingStartHour != null)
        {
            if (isValueReference(workingStartHour))
            {
                planner.setValueBinding("workingStartHour", app
                        .createValueBinding(workingStartHour));
            }
            else
            {
                planner.setWorkingStartHour(new Integer(workingStartHour)
                        .intValue());
            }
        }

        if (workingEndHour != null)
        {
            if (isValueReference(workingEndHour))
            {
                planner.setValueBinding("workingEndHour", app
                        .createValueBinding(workingEndHour));
            }
            else
            {
                planner.setWorkingEndHour(new Integer(workingEndHour)
                        .intValue());
            }
        }

        if ((value != null) && isValueReference(value))
        {
            planner.setValueBinding("value", app.createValueBinding(value));
        }
        else
        {
            throw new IllegalArgumentException(
                    "The value property must be a value binding expression that points to a PlannerModel object.");
        }

        addAttribute(app, planner, "headerDateFormat", headerDateFormat);
        addAttribute(app, planner, "theme", theme);
        addAttribute(app, planner, "tooltip", tooltip);
        addAttribute(app, planner, "legend", legend);
        addAttribute(app, planner, "gutterWidthInPixels", gutterWidthInPixels);
    }

    private void addAttribute(Application app, UIComponent component,
            String key, String value)
    {
        if ((key != null) && (value != null))
        {
            if (isValueReference(value))
            {
                component.setValueBinding(key, app
                        .createValueBinding(value));
            }
            else
            {
                component.getAttributes().put(key, value);
            }
        }
    }
}
//The End

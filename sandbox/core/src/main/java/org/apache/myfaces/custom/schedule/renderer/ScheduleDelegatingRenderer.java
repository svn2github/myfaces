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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;

/**
 * <p>
 * Renderer for the Schedule component that delegates the actual rendering
 * to a compact or detailed renderer, depending on the mode of the ScheduleModel
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision$
 */
public class ScheduleDelegatingRenderer extends Renderer
{
    //~ Instance fields --------------------------------------------------------

    private final ScheduleCompactMonthRenderer monthDelegate = new ScheduleCompactMonthRenderer();
    private final ScheduleCompactWeekRenderer weekDelegate = new ScheduleCompactWeekRenderer();
    private final ScheduleDetailedDayRenderer dayDelegate = new ScheduleDetailedDayRenderer();

    //~ Methods ----------------------------------------------------------------

    /**
     * @see javax.faces.render.Renderer#decode(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void decode(FacesContext context, UIComponent component)
    {
        getDelegateRenderer(component).decode(context, component);
    }

    /**
     * @see javax.faces.render.Renderer#encodeBegin(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        getDelegateRenderer(component).encodeBegin(context, component);
    }

    /**
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException
    {
        getDelegateRenderer(component).encodeChildren(context, component);
    }

    /**
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        getDelegateRenderer(component).encodeEnd(context, component);
    }

    private Renderer getDelegateRenderer(UIComponent component)
    {
        HtmlSchedule schedule = (HtmlSchedule) component;

        if ((schedule == null) || (schedule.getModel() == null))
        {
            return dayDelegate;
        }

        switch (schedule.getModel().getMode())
        {
        case ScheduleModel.WEEK:
            return weekDelegate;

        case ScheduleModel.MONTH:
            return monthDelegate;

        default:
            return dayDelegate;
        }
    }
}
//The End

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
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.myfaces.custom.schedule.model.PlannerEntity;
import org.apache.myfaces.custom.schedule.model.PlannerModel;
import org.apache.myfaces.custom.schedule.model.ScheduleEntry;
import org.apache.myfaces.custom.schedule.util.ScheduleUtil;

/**
 * <p>
 * A meeting planner component, similar to the ones found in Outlook or
 * Evolution.
 * </p>
 *
 * @author Jurgen Lust (latest modification by $Author$)
 * @author Bruno Aranda (adaptation of Jurgen's code to myfaces)
 * @version $Revision$
 */
public class UIPlanner extends UIComponentBase implements ValueHolder,
        Serializable
{
    //~ Static fields/initializers ---------------------------------------------

    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    /** Logger for this class */
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Planner";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Planner";
    private static final long serialVersionUID = 4396986521172225477L;

    //~ Instance fields --------------------------------------------------------

    private Converter _converter;
    private Object _value;
    private Integer _visibleEndHour;
    private Integer _visibleStartHour;
    private Integer _workingEndHour;
    private Integer _workingStartHour;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new UIPlanner object.
     */
    public UIPlanner()
    {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * <p>
     * Find the entry with the given id
     * </p>
     *
     * @param id the id
     *
     * @return the entry
     */
    public ScheduleEntry findEntry(String id)
    {
        if (id == null)
        {
            return null;
        }

        for (Iterator entityIterator = getModel().entityIterator(); entityIterator
                .hasNext();)
        {
            PlannerEntity entity = (PlannerEntity) entityIterator.next();

            for (Iterator iter = entity.iterator(); iter.hasNext();)
            {
                ScheduleEntry entry = (ScheduleEntry) iter.next();

                if (id.equals(entry.getId()))
                {
                    return entry;
                }
            }
        }

        return null;
    }

    /**
     * @see javax.faces.component.ValueHolder#getConverter()
     */
    public Converter getConverter()
    {
        return _converter;
    }

    /**
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    /**
     * @see javax.faces.component.ValueHolder#getLocalValue()
     */
    public Object getLocalValue()
    {
        return _value;
    }

    /**
     * <p>
     * The underlying planner model
     * </p>
     *
     * @return the model
     */
    public PlannerModel getModel()
    {
        if (getValue() instanceof PlannerModel)
        {
            return (PlannerModel) getValue();
        }
        else
        {
            return new PlannerModel();
        }
    }

    /**
     * @see javax.faces.component.UIComponent#getRendersChildren()
     */
    public boolean getRendersChildren()
    {
        return true;
    }

    /**
     * @see javax.faces.component.ValueHolder#getValue()
     */
    public Object getValue()
    {
        return ScheduleUtil.getObjectProperty(this, _value, "value", _value);
    }

    /**
     * <p>
     * The last hour of the day that is visible in the planner
     * </p>
     *
     * @return Returns the visibleEndHour.
     */
    public int getVisibleEndHour()
    {
        return ScheduleUtil.getIntegerProperty(this, _visibleEndHour,
                "visibleEndHour", 22);
    }

    /**
     * <p>
     * the first hour of the day that is visible in the planner
     * </p>
     *
     * @return Returns the visibleStartHour.
     */
    public int getVisibleStartHour()
    {
        return ScheduleUtil.getIntegerProperty(this, _visibleStartHour,
                "visibleStartHour", 8);
    }

    /**
     * <p>
     * the last hour of the working day
     * </p>
     *
     * @return Returns the workingEndHour.
     */
    public int getWorkingEndHour()
    {
        return ScheduleUtil.getIntegerProperty(this, _workingEndHour,
                "workingEndHour", 17);
    }

    /**
     * <p>
     * The first hour of the working day
     * </p>
     *
     * @return Returns the workingStartHour.
     */
    public int getWorkingStartHour()
    {
        return ScheduleUtil.getIntegerProperty(this, _workingStartHour,
                "workingStartHour", 9);
    }

    /**
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        _visibleStartHour = (Integer) values[1];
        _visibleEndHour = (Integer) values[2];
        _workingStartHour = (Integer) values[3];
        _workingEndHour = (Integer) values[4];
        _value = values[5];
    }

    /**
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _visibleStartHour;
        values[2] = _visibleEndHour;
        values[3] = _workingStartHour;
        values[4] = _workingEndHour;
        values[5] = _value;

        return values;
    }

    /**
     * @see javax.faces.component.ValueHolder#setConverter(javax.faces.convert.Converter)
     */
    public void setConverter(Converter converter)
    {
        this._converter = converter;
    }

    /**
     * <p>
     * The underlying planner model
     * </p>
     *
     * @param model the model
     */
    public void setModel(PlannerModel model)
    {
        setValue(model);
    }

    /**
     * @see javax.faces.component.ValueHolder#setValue(java.lang.Object)
     */
    public void setValue(Object value)
    {
        this._value = value;
    }

    /**
     * <p>
     * the last hour of the day that is visible in the planner
     * </p>
     *
     * @param visibleEndHour The visibleEndHour to set.
     */
    public void setVisibleEndHour(int visibleEndHour)
    {
        this._visibleEndHour = new Integer(visibleEndHour);
    }

    /**
     * <p>
     * the first hour of the day that is visible in the planner
     * </p>
     *
     * @param visibleStartHour The visibleStartHour to set.
     */
    public void setVisibleStartHour(int visibleStartHour)
    {
        this._visibleStartHour = new Integer(visibleStartHour);
    }

    /**
     * <p>
     * the last hour of the working day
     * </p>
     *
     * @param workingEndHour The workingEndHour to set.
     */
    public void setWorkingEndHour(int workingEndHour)
    {
        this._workingEndHour = new Integer(workingEndHour);
    }

    /**
     * <p>
     * The first hour of the working day
     * </p>
     *
     * @param workingStartHour The workingStartHour to set.
     */
    public void setWorkingStartHour(int workingStartHour)
    {
        this._workingStartHour = new Integer(workingStartHour);
    }
}
//The End

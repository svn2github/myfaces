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
package org.apache.myfaces.custom.calendar;

import org.apache.myfaces.component.UserRoleUtils;

import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputCalendar
        extends HtmlInputText
{
    //private static final Log log = LogFactory.getLog(HtmlInputCalendar.class);

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlInputCalendar";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Calendar";

    private String _monthYearRowClass = null;
    private String _weekRowClass = null;
    private String _dayCellClass = null;
    private String _currentDayCellClass = null;
    private Boolean _renderAsPopup = null;
    private Boolean _addResources = null;
    private String _popupButtonString = null;
    private String _popupDateFormat = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    private String _popupGotoString = null;
    private String _popupTodayString = null;
    private String _popupWeekString = null;
    private String _popupScrollLeftMessage = null;
    private String _popupScrollRightMessage = null;
    private String _popupSelectMonthMessage = null;
    private String _popupSelectYearMessage = null;
    private String _popupSelectDateMessage = null;

    public HtmlInputCalendar()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setMonthYearRowClass(String monthYearRowClass)
    {
        _monthYearRowClass = monthYearRowClass;
    }

    public String getMonthYearRowClass()
    {
        if (_monthYearRowClass != null) return _monthYearRowClass;
        ValueBinding vb = getValueBinding("monthYearRowClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setWeekRowClass(String weekRowClass)
    {
        _weekRowClass = weekRowClass;
    }

    public String getWeekRowClass()
    {
        if (_weekRowClass != null) return _weekRowClass;
        ValueBinding vb = getValueBinding("weekRowClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setDayCellClass(String dayCellClass)
    {
        _dayCellClass = dayCellClass;
    }

    public String getDayCellClass()
    {
        if (_dayCellClass != null) return _dayCellClass;
        ValueBinding vb = getValueBinding("dayCellClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setCurrentDayCellClass(String currentDayCellClass)
    {
        _currentDayCellClass = currentDayCellClass;
    }

    public String getCurrentDayCellClass()
    {
        if (_currentDayCellClass != null) return _currentDayCellClass;
        ValueBinding vb = getValueBinding("currentDayCellClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRenderAsPopup(boolean renderAsPopup)
    {
        _renderAsPopup = Boolean.valueOf(renderAsPopup);
    }

    public boolean isRenderAsPopup()
    {
        if (_renderAsPopup != null) return _renderAsPopup.booleanValue();
        ValueBinding vb = getValueBinding("renderAsPopup");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setAddResources(boolean addResources)
    {
        _addResources = Boolean.valueOf(addResources);
    }

    public boolean isAddResources()
    {
        if (_addResources != null) return _addResources.booleanValue();
        ValueBinding vb = getValueBinding("addResources");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setPopupButtonString(String popupButtonString)
    {
        _popupButtonString = popupButtonString;
    }

    public String getPopupButtonString()
    {
        if (_popupButtonString != null) return _popupButtonString;
        ValueBinding vb = getValueBinding("popupButtonString");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupDateFormat(String popupDateFormat)
    {
        _popupDateFormat = popupDateFormat;
    }

    public String getPopupDateFormat()
    {
        if (_popupDateFormat != null) return _popupDateFormat;
        ValueBinding vb = getValueBinding("popupDateFormat");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupGotoString(String popupGotoString)
    {
        _popupGotoString = popupGotoString;
    }

    public String getPopupGotoString()
    {
        if (_popupGotoString != null) return _popupGotoString;
        ValueBinding vb = getValueBinding("popupGotoString");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupTodayString(String popupTodayString)
    {
        _popupTodayString = popupTodayString;
    }

    public String getPopupTodayString()
    {
        if (_popupTodayString != null) return _popupTodayString;
        ValueBinding vb = getValueBinding("popupTodayString");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupWeekString(String popupWeekString)
    {
        _popupWeekString = popupWeekString;
    }

    public String getPopupWeekString()
    {
        if (_popupWeekString != null) return _popupWeekString;
        ValueBinding vb = getValueBinding("popupWeekString");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupScrollLeftMessage(String popupScrollLeftMessage)
    {
        _popupScrollLeftMessage = popupScrollLeftMessage;
    }

    public String getPopupScrollLeftMessage()
    {
        if (_popupScrollLeftMessage != null) return _popupScrollLeftMessage;
        ValueBinding vb = getValueBinding("popupScrollLeftMessage");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupScrollRightMessage(String popupScrollRightMessage)
    {
        _popupScrollRightMessage = popupScrollRightMessage;
    }

    public String getPopupScrollRightMessage()
    {
        if (_popupScrollRightMessage != null) return _popupScrollRightMessage;
        ValueBinding vb = getValueBinding("popupScrollRightMessage");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupSelectMonthMessage(String popupSelectMonthMessage)
    {
        _popupSelectMonthMessage = popupSelectMonthMessage;
    }

    public String getPopupSelectMonthMessage()
    {
        if (_popupSelectMonthMessage != null) return _popupSelectMonthMessage;
        ValueBinding vb = getValueBinding("popupSelectMonthMessage");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupSelectYearMessage(String popupSelectYearMessage)
    {
        _popupSelectYearMessage = popupSelectYearMessage;
    }

    public String getPopupSelectYearMessage()
    {
        if (_popupSelectYearMessage != null) return _popupSelectYearMessage;
        ValueBinding vb = getValueBinding("popupSelectYearMessage");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPopupSelectDateMessage(String popupSelectDateMessage)
    {
        _popupSelectDateMessage = popupSelectDateMessage;
    }

    public String getPopupSelectDateMessage()
    {
        if (_popupSelectDateMessage != null) return _popupSelectDateMessage;
        ValueBinding vb = getValueBinding("popupSelectDateMessage");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[19];
        values[0] = super.saveState(context);
        values[1] = _monthYearRowClass;
        values[2] = _weekRowClass;
        values[3] = _dayCellClass;
        values[4] = _currentDayCellClass;
        values[5] = _renderAsPopup;
        values[6] = _addResources;
        values[7] = _popupButtonString;
        values[8] = _popupDateFormat;
        values[9] = _enabledOnUserRole;
        values[10] = _visibleOnUserRole;
        values[11] = _popupGotoString;
        values[12] = _popupTodayString;
        values[13] = _popupWeekString;
        values[14] = _popupScrollLeftMessage;
        values[15] = _popupScrollRightMessage;
        values[16] = _popupSelectMonthMessage;
        values[17] = _popupSelectYearMessage;
        values[18] = _popupSelectDateMessage;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _monthYearRowClass = (String)values[1];
        _weekRowClass = (String)values[2];
        _dayCellClass = (String)values[3];
        _currentDayCellClass = (String)values[4];
        _renderAsPopup = (Boolean)values[5];
        _addResources = (Boolean)values[6];
        _popupButtonString = (String)values[7];
        _popupDateFormat = (String)values[8];
        _enabledOnUserRole = (String)values[9];
        _visibleOnUserRole = (String)values[10];
        _popupGotoString = (String)values[11];
        _popupTodayString = (String)values[12];
        _popupWeekString = (String)values[13];
        _popupScrollLeftMessage = (String)values[14];
        _popupScrollRightMessage = (String)values[15];
        _popupSelectMonthMessage = (String)values[16];
        _popupSelectYearMessage = (String)values[17];
        _popupSelectDateMessage = (String)values[18];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

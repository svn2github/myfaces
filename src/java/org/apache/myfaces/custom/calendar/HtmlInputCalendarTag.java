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

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.taglib.html.HtmlInputTagBase;
import org.apache.myfaces.renderkit.html.HTML;

import javax.faces.component.UIComponent;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputCalendarTag
        extends HtmlInputTagBase
{
    private String _accesskey;
    private String _align;
    private String _alt;
    private String _disabled;
    private String _maxlength;
    private String _onblur;
    private String _onchange;
    private String _onfocus;
    private String _onselect;
    private String _size;
    private String _tabindex;
    //private static final Log log = LogFactory.getLog(HtmlInputCalendarTag.class);

    public String getComponentType()
    {
        return HtmlInputCalendar.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.Calendar";
    }

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in MyFacesTag

    // UIOutput attributes
    // value and converterId --> already implemented in UIComponentTagBase

    // UIInput attributes
    // --> already implemented in HtmlInputTagBase

    // HtmlCalendar attributes
    private String _monthYearRowClass;
    private String _weekRowClass;
    private String _dayCellClass;
    private String _currentDayCellClass;
    private String _renderAsPopup;
    private String _addResources;
    private String _popupDateFormat;
    private String _popupButtonString;
    private String _popupGotoString = null;
    private String _popupTodayString = null;
    private String _popupWeekString = null;
    private String _popupScrollLeftMessage = null;
    private String _popupScrollRightMessage = null;
    private String _popupSelectMonthMessage = null;
    private String _popupSelectYearMessage = null;
    private String _popupSelectDateMessage = null;

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    public void release() {
        super.release();

        _monthYearRowClass = null;
        _weekRowClass = null;
        _dayCellClass = null;
        _currentDayCellClass = null;
        _renderAsPopup = null;
        _addResources = null;
        _popupDateFormat = null;
        _popupButtonString = null;
        _popupGotoString = null;
        _popupTodayString = null;
        _popupWeekString = null;
        _popupScrollLeftMessage = null;
        _popupScrollRightMessage = null;
        _popupSelectMonthMessage = null;
        _popupSelectYearMessage = null;
        _popupSelectDateMessage = null;
        _enabledOnUserRole = null;
        _visibleOnUserRole = null;

    }


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, HTML.ACCESSKEY_ATTR, _accesskey);
        setStringProperty(component, HTML.ALIGN_ATTR, _align);
        setStringProperty(component, HTML.ALT_ATTR, _alt);
        setBooleanProperty(component, HTML.DISABLED_ATTR, _disabled);
        setIntegerProperty(component, HTML.MAXLENGTH_ATTR, _maxlength);
        setStringProperty(component, HTML.ONBLUR_ATTR, _onblur);
        setStringProperty(component, HTML.ONCHANGE_ATTR, _onchange);
        setStringProperty(component, HTML.ONFOCUS_ATTR, _onfocus);
        setStringProperty(component, HTML.ONSELECT_ATTR, _onselect);
        setIntegerProperty(component, HTML.SIZE_ATTR, _size);
        setStringProperty(component, HTML.TABINDEX_ATTR, _tabindex);


        setStringProperty(component, "monthYearRowClass", _monthYearRowClass);
        setStringProperty(component, "weekRowClass", _weekRowClass);
        setStringProperty(component, "dayCellClass", _dayCellClass);
        setStringProperty(component, "currentDayCellClass", _currentDayCellClass);
        setBooleanProperty(component,"renderAsPopup",_renderAsPopup);
        setBooleanProperty(component,"addResources",(_addResources==null?Boolean.TRUE.toString():_addResources));
        setStringProperty(component,"popupDateFormat",_popupDateFormat);
        setStringProperty(component,"popupButtonString",_popupButtonString);
        setStringProperty(component,"popupGotoString",_popupGotoString);
        setStringProperty(component,"popupTodayString",_popupTodayString);
        setStringProperty(component,"popupWeekString",_popupWeekString);
        setStringProperty(component,"popupScrollLeftMessage",_popupScrollLeftMessage);
        setStringProperty(component,"popupScrollRightMessage",_popupScrollRightMessage);
        setStringProperty(component,"popupSelectMonthMessage",_popupSelectMonthMessage);
        setStringProperty(component,"popupSelectYearMessage",_popupSelectYearMessage);
        setStringProperty(component,"popupSelectDateMessage",_popupSelectDateMessage);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    public void setMonthYearRowClass(String monthYearRowClass)
    {
        _monthYearRowClass = monthYearRowClass;
    }

    public void setWeekRowClass(String weekRowClass)
    {
        _weekRowClass = weekRowClass;
    }

    public void setDayCellClass(String dayCellClass)
    {
        _dayCellClass = dayCellClass;
    }

    public void setCurrentDayCellClass(String currentDayCellClass)
    {
        _currentDayCellClass = currentDayCellClass;
    }

    public void setRenderAsPopup(String renderAsPopup)
    {
        _renderAsPopup = renderAsPopup;
    }

    public void setAddResources(String addResources)
    {
        _addResources = addResources;
    }

    public void setPopupDateFormat(String popupDateFormat)
    {
        _popupDateFormat = popupDateFormat;
    }

    public void setPopupButtonString(String popupButtonString)
    {
        _popupButtonString = popupButtonString;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public void setPopupGotoString(String popupGotoString)
    {
        _popupGotoString = popupGotoString;
    }

    public void setPopupScrollLeftMessage(String popupScrollLeftMessage)
    {
        _popupScrollLeftMessage = popupScrollLeftMessage;
    }

    public void setPopupScrollRightMessage(String popupScrollRightMessage)
    {
        _popupScrollRightMessage = popupScrollRightMessage;
    }

    public void setPopupSelectDateMessage(String popupSelectDateMessage)
    {
        _popupSelectDateMessage = popupSelectDateMessage;
    }

    public void setPopupSelectMonthMessage(String popupSelectMonthMessage)
    {
        _popupSelectMonthMessage = popupSelectMonthMessage;
    }

    public void setPopupSelectYearMessage(String popupSelectYearMessage)
    {
        _popupSelectYearMessage = popupSelectYearMessage;
    }

    public void setPopupTodayString(String popupTodayString)
    {
        _popupTodayString = popupTodayString;
    }

    public void setPopupWeekString(String popupWeekString)
    {
        _popupWeekString = popupWeekString;
    }

    public void setAccesskey(String accesskey)
    {
        _accesskey = accesskey;
    }

    public void setAlign(String align)
    {
        _align = align;
    }

    public void setAlt(String alt)
    {
        _alt = alt;
    }

    public void setDisabled(String disabled)
    {
        _disabled = disabled;
    }

    public void setMaxlength(String maxlength)
    {
        _maxlength = maxlength;
    }

    public void setOnblur(String onblur)
    {
        _onblur = onblur;
    }

    public void setOnchange(String onchange)
    {
        _onchange = onchange;
    }

    public void setOnfocus(String onfocus)
    {
        _onfocus = onfocus;
    }

    public void setOnselect(String onselect)
    {
        _onselect = onselect;
    }

    public void setSize(String size)
    {
        _size = size;
    }

    public void setTabindex(String tabindex)
    {
        _tabindex = tabindex;
    }
}

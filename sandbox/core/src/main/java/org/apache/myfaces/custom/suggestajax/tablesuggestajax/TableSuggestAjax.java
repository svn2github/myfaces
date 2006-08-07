/*
 * Copyright 2004-2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.suggestajax.tablesuggestajax;

import org.apache.myfaces.custom.suggestajax.SuggestAjax;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;

/**
 * @author Gerald Muellan
 *         Date: 25.03.2006
 *         Time: 17:04:58
 */
public class TableSuggestAjax extends SuggestAjax
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.TableSuggestAjax";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.TableSuggestAjax";

    private String _layout;
    private String _popupId;
    private String _popupStyleClass;
    private String _popupStyle;

    private String _tableStyleClass;
    private String _nextPageFieldClass;

    private String _columnHoverClass;
    private String _columnOutClass;

    private Integer _betweenKeyUp;
    private Integer _startRequest;

    private Boolean _acceptValueToField = new Boolean(false);

    private String _var;

    public TableSuggestAjax()
    {
        super();

        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[13];
        values[0] = super.saveState(context);
        values[1] = _var;
        values[2] = _columnHoverClass;
        values[3] = _columnOutClass;
        values[4] = _betweenKeyUp;
        values[5] = _startRequest;
        values[6] = _tableStyleClass;
        values[7] = _nextPageFieldClass;
        values[8] = _acceptValueToField;
        values[9] = _popupId;
        values[10] = _popupStyleClass;
        values[11] = _popupStyle;
        values[12] = _layout;

        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _var = (String) values[1];
        _columnHoverClass = (String) values[2];
        _columnOutClass = (String) values[3];
        _betweenKeyUp = (Integer) values[4];
        _startRequest = (Integer) values[5];
        _tableStyleClass = (String) values[6];
        _nextPageFieldClass = (String) values[7];
        _acceptValueToField = (Boolean) values[8];
        _popupId = (String) values[9];
        _popupStyleClass = (String) values[10];
        _popupStyle = (String) values[11];
        _layout = (String) values[12];
    }

    public boolean getRendersChildren()
    {
        if (getVar() != null)
        {
            return true;
        }
        else
        {
            return super.getRendersChildren();
        }
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        super.encodeChildren(context);
    }

    public Integer getBetweenKeyUp()
    {
        if (_betweenKeyUp != null)
        {
            return _betweenKeyUp;
        }
        ValueBinding vb = getValueBinding("delay");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setBetweenKeyUp(Integer betweenKeyUp)
    {
        _betweenKeyUp = betweenKeyUp;
    }

    public Integer getStartRequest()
    {
        if (_startRequest != null)
        {
            return _startRequest;
        }
        ValueBinding vb = getValueBinding("startRequest");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setStartRequest(Integer startRequest)
    {
        _startRequest = startRequest;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public String getVar()
    {
        if (_var != null) return _var;
        ValueBinding vb = getValueBinding("var");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public String getColumnHoverClass()
    {
        return _columnHoverClass;
    }

    public void setColumnHoverClass(String columnHoverClass)
    {
        _columnHoverClass = columnHoverClass;
    }

    public String getColumnOutClass()
    {
        return _columnOutClass;
    }

    public void setColumnOutClass(String columnOutClass)
    {
        _columnOutClass = columnOutClass;
    }

    public String getTableStyleClass()
    {
        return _tableStyleClass;
    }

    public void setTableStyleClass(String tableStyleClass)
    {
        _tableStyleClass = tableStyleClass;
    }

    public String getNextPageFieldClass()
    {
        return _nextPageFieldClass;
    }

    public void setNextPageFieldClass(String nextPageFieldClass)
    {
        _nextPageFieldClass = nextPageFieldClass;
    }

    public Boolean getAcceptValueToField()
    {
        return _acceptValueToField;
    }

    public void setAcceptValueToField(Boolean acceptValueToField)
    {
        _acceptValueToField = acceptValueToField;
    }

     public String getPopupId()
    {
        if (_popupId != null)
            return _popupId;
        ValueBinding vb = getValueBinding("popupId");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setPopupId(String popupId)
    {
        _popupId = popupId;
    }

    public String getPopupStyleClass()
    {
        if (_popupStyleClass != null)
            return _popupStyleClass;
        ValueBinding vb = getValueBinding("popupStyleClass");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setPopupStyleClass(String popupStyleClass)
    {
        _popupStyleClass = popupStyleClass;
    }

    public String getPopupStyle()
    {
        if (_popupStyle != null)
            return _popupStyle;
        ValueBinding vb = getValueBinding("popupStyle");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setPopupStyle(String popupStyle)
    {
        _popupStyle = popupStyle;
    }

    public String getLayout()
    {
        if (_layout != null)
            return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ? vb.getValue(getFacesContext()).toString() : "default";
    }

    public void setLayout(String layout)
    {
        _layout = layout;
    }
}
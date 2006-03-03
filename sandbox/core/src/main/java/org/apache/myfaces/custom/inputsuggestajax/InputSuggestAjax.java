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
package org.apache.myfaces.custom.inputsuggestajax;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.custom.ajax.api.AjaxComponent;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * @author Gerald Muellan (latest modification by $Author: svieujot $)
 * @author Martin Marinschek
 *
 * @version $Revision: 169662 $ $Date: 2005-05-11 19:57:24 +0200 (Wed, 11 May 2005) $
 */

public class InputSuggestAjax extends HtmlInputText implements AjaxComponent
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.InputSuggestAjax";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.InputSuggestAjax";

    private MethodBinding _suggestedItemsMethod;

    private String _popupId;
    private String _popupStyleClass;
    private String _popupStyle;

    private String _tableStyleClass;
    private String _nextPageFieldClass;

    private String _columnHoverClass;
    private String _columnOutClass;

    private String _listId;
    private String _listStyleClass;
    private String _listStyle;

    private String _listItemStyleClass;
    private String _listItemStyle;

    private String _layout;

    private Integer _maxSuggestedItems;
    private Integer _delay;
    private Integer _startRequest;

    private String _var;

    public InputSuggestAjax()
    {
        super();

        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[19];
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, _suggestedItemsMethod);
        values[2] = _popupId;
        values[3] = _popupStyleClass;
        values[4] = _popupStyle;
        values[5] = _listId;
        values[6] = _listStyleClass;
        values[7] = _listStyle;
        values[8] = _listItemStyleClass;
        values[9] = _listItemStyle;
        values[10] = _layout;
        values[11] = _maxSuggestedItems;
        values[12] = _var;
        values[13] = _columnHoverClass;
        values[14] = _columnOutClass;
        values[15] = _delay;
        values[16] = _startRequest;
        values[17] = _tableStyleClass;
        values[18] = _nextPageFieldClass;

        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _suggestedItemsMethod = (MethodBinding) restoreAttachedState(context, values[1]);
         _popupId = (String) values[2];
        _popupStyleClass = (String) values[3];
        _popupStyle = (String) values[4];
        _listId = (String) values[5];
        _listStyleClass = (String) values[6];
        _listStyle = (String) values[7];
        _listItemStyleClass = (String) values[8] ;
        _listItemStyle = (String) values[9];
        _layout = (String) values[10];
        _maxSuggestedItems = (Integer) values[11];
        _var = (String) values[12];
        _columnHoverClass = (String) values[13];
        _columnOutClass = (String) values[14];
        _delay = (Integer) values[15];
        _startRequest = (Integer) values[16];
        _tableStyleClass = (String) values[17];
        _nextPageFieldClass = (String) values[18];
    }

    public void encodeAjax(FacesContext context)
            throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (!isRendered()) return;
        Renderer renderer = getRenderer(context);
        if (renderer != null && renderer instanceof AjaxRenderer)
        {
            ((AjaxRenderer) renderer).encodeAjax(context, this);
        }
    }

    public void decodeAjax(FacesContext context)
    {

    }

    public boolean getRendersChildren()
    {
        if(getVar()!=null)
            return true;
        else
            return super.getRendersChildren();
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        super.encodeChildren(context);
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

     public void setSuggestedItemsMethod(MethodBinding suggestedItemsMethod)
    {
       _suggestedItemsMethod = suggestedItemsMethod;
    }

    public MethodBinding getSuggestedItemsMethod()
    {
        return _suggestedItemsMethod;
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

    public String getListId()
    {
        if (_listId != null)
            return _listId;
        ValueBinding vb = getValueBinding("listId");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setListId(String listId)
    {
        _listId = listId;
    }

    public String getListStyleClass()
    {
        if (_listStyleClass != null)
            return _listStyleClass;
        ValueBinding vb = getValueBinding("listStyleClass");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setListStyleClass(String listStyleClass)
    {
        _listStyleClass = listStyleClass;
    }

    public String getListStyle()
    {
        if (_listStyle != null)
            return _listStyle;
        ValueBinding vb = getValueBinding("listStyle");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setListStyle(String listStyle)
    {
        _listStyle = listStyle;
    }

    public String getListItemStyleClass()
    {
        if (_listItemStyleClass != null)
            return _listItemStyleClass;
        ValueBinding vb = getValueBinding("listItemStyleClass");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setListItemStyleClass(String listItemStyleClass)
    {
        _listItemStyleClass = listItemStyleClass;
    }

    public String getListItemStyle()
    {
        if (_listItemStyle != null)
            return _listItemStyle;
        ValueBinding vb = getValueBinding("listItemStyle");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setListItemStyle(String listItemStyle)
    {
        _listItemStyle = listItemStyle;
    }

	public Integer getMaxSuggestedItems() {
        if (_maxSuggestedItems != null)
            return _maxSuggestedItems;
        ValueBinding vb = getValueBinding("maxSuggestedItems");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
	}

	public void setMaxSuggestedItems(Integer suggestedItems) {
		_maxSuggestedItems = suggestedItems;
	}

    public Integer getDelay()
    {
        if (_delay != null)
            return _delay;
        ValueBinding vb = getValueBinding("delay");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setDelay(Integer delay)
    {
        _delay = delay;
    }

    public Integer getStartRequest()
    {
        if (_startRequest != null)
            return _startRequest;
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
}

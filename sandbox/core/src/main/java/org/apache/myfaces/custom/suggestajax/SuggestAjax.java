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
package org.apache.myfaces.custom.suggestajax;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.custom.ajax.api.AjaxComponent;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;

import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * @author Gerald Muellan
 *         Date: 25.03.2006
 *         Time: 17:06:04
 */
public class SuggestAjax extends HtmlInputText implements AjaxComponent
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.SuggestAjax";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SuggestAjax";

    private MethodBinding _suggestedItemsMethod;

    private String _popupId;
    private String _popupStyleClass;
    private String _popupStyle;

    private String _layout;

    private Integer _maxSuggestedItems;

    public SuggestAjax()
    {
        super();

        setRendererType(SuggestAjax.DEFAULT_RENDERER_TYPE);
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[7];
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, _suggestedItemsMethod);
        values[2] = _popupId;
        values[3] = _popupStyleClass;
        values[4] = _popupStyle;
        values[5] = _layout;
        values[6] = _maxSuggestedItems;

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
        _layout = (String) values[5];
        _maxSuggestedItems = (Integer) values[6];
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

    public Integer getMaxSuggestedItems() {
        if (_maxSuggestedItems != null)
            return _maxSuggestedItems;
        ValueBinding vb = getValueBinding("maxSuggestedItems");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setMaxSuggestedItems(Integer suggestedItems) {
        _maxSuggestedItems = suggestedItems;
    }

}

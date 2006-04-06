/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.fisheye;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.div.Div;

/**
 * A Mac OSX-style toolbar, using the DOJO toolkit.
 * 
 * @see <a href="http://dojotoolkit.org/">http://dojotoolkit.org/</a>
 * 
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFishEyeList extends Div
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.FishEyeList";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.FishEyeList";

    public static final String EDGE_BOTTOM = "bottom";
    public static final String EDGE_CENTER = "center";
    public static final String EDGE_LEFT = "left";
    public static final String EDGE_RIGHT = "right";
    public static final String EDGE_TOP = "top";
    public static final String HORIZONTAL_ORIENTATION = "horizontal";
    public static final String VERTICAL_ORIENTATION = "vertical";

    private String _attachEdge;
    private Integer _effectUnits;
    private Integer _itemHeight;
    private Integer _itemMaxHeight;
    private Integer _itemMaxWidth;
    private Integer _itemPadding;
    private Integer _itemWidth;
    private String _labelEdge;
    private String _orientation;

    public HtmlFishEyeList()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getAttachEdge()
    {
        if (_attachEdge != null)
        {
            return _attachEdge;
        }
        ValueBinding vb = getValueBinding("attachEdge");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;

    }

    public Integer getEffectUnits()
    {
        if (_effectUnits != null)
        {
            return _effectUnits;
        }
        ValueBinding vb = getValueBinding("effectUnits");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public Integer getItemHeight()
    {
        if (_itemHeight != null)
        {
            return _itemHeight;
        }
        ValueBinding vb = getValueBinding("itemHeight");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public Integer getItemMaxHeight()
    {
        if (_itemMaxHeight != null)
        {
            return _itemMaxHeight;
        }
        ValueBinding vb = getValueBinding("itemMaxHeight");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public Integer getItemMaxWidth()
    {
        if (_itemMaxWidth != null)
        {
            return _itemMaxWidth;
        }
        ValueBinding vb = getValueBinding("itemMaxWidth");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public Integer getItemPadding()
    {
        if (_itemPadding != null)
        {
            return _itemPadding;
        }
        ValueBinding vb = getValueBinding("itemPadding");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public Integer getItemWidth()
    {
        if (_itemWidth != null)
        {
            return _itemWidth;
        }
        ValueBinding vb = getValueBinding("itemWidth");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public String getLabelEdge()
    {
        if (_labelEdge != null)
        {
            return _labelEdge;
        }
        ValueBinding vb = getValueBinding("labelEdge");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getOrientation()
    {
        if (_orientation != null)
        {
            return _orientation;
        }
        ValueBinding vb = getValueBinding("orientation");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public boolean getRendersChildren()
    {
        return false;
    }

    /**
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _itemWidth = (Integer) values[1];
        _itemHeight = (Integer) values[2];
        _itemMaxWidth = (Integer) values[3];
        _itemMaxHeight = (Integer) values[4];
        _orientation = (String) values[5];
        _effectUnits = (Integer) values[6];
        _itemPadding = (Integer) values[7];
        _attachEdge = (String) values[8];
        _labelEdge = (String) values[9];
    }

    /**
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[12];
        values[0] = super.saveState(context);
        values[1] = _itemWidth;
        values[2] = _itemHeight;
        values[3] = _itemMaxWidth;
        values[4] = _itemMaxHeight;
        values[5] = _orientation;
        values[6] = _effectUnits;
        values[7] = _itemPadding;
        values[8] = _attachEdge;
        values[9] = _labelEdge;
        return values;
    }

    public void setAttachEdge(String attachEdge)
    {
        this._attachEdge = attachEdge;
    }

    public void setEffectUnits(Integer effectUnits)
    {
        this._effectUnits = effectUnits;
    }

    public void setItemHeight(Integer itemHeight)
    {
        this._itemHeight = itemHeight;
    }

    public void setItemMaxHeight(Integer itemMaxHeight)
    {
        this._itemMaxHeight = itemMaxHeight;
    }

    public void setItemMaxWidth(Integer itemMaxWidth)
    {
        this._itemMaxWidth = itemMaxWidth;
    }

    public void setItemPadding(Integer itemPadding)
    {
        this._itemPadding = itemPadding;
    }

    public void setItemWidth(Integer itemWidth)
    {
        this._itemWidth = itemWidth;
    }

    public void setLabelEdge(String labelEdge)
    {
        this._labelEdge = labelEdge;
    }

    public void setOrientation(String orientation)
    {
        this._orientation = orientation;
    }
}

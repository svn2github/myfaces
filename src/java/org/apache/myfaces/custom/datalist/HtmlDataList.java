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
package org.apache.myfaces.custom.datalist;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataList
        extends UIData
{
    public void processDecodes(FacesContext context)
    {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0)
        {
            last = getRowCount();
        }
        else
        {
            last = first + rows;
        }
        for (int rowIndex = first; rowIndex < last; rowIndex++)
        {
            setRowIndex(rowIndex);
            if (isRowAvailable())
            {
                for (Iterator it = getChildren().iterator(); it.hasNext();)
                {
                    UIComponent child = (UIComponent) it.next();
                    if (!child.isRendered())
                    {
                        continue;
                    }
                    child.processDecodes(context);
                }
            }
        }

        setRowIndex(-1);
    }

    public void processUpdates(FacesContext context)
    {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0)
        {
            last = getRowCount();
        }
        else
        {
            last = first + rows;
        }
        for (int rowIndex = first; rowIndex < last; rowIndex++)
        {
            setRowIndex(rowIndex);
            if (isRowAvailable())
            {
                for (Iterator it = getChildren().iterator(); it.hasNext();)
                {
                    UIComponent child = (UIComponent) it.next();
                    if (!child.isRendered())
                    {
                        continue;
                    }
                    child.processUpdates(context);
                }
            }
        }

        setRowIndex(-1);
    }


    public void processValidators(FacesContext context)
    {
        int first = getFirst();
        int rows = getRows();
        int last;
        if (rows == 0)
        {
            last = getRowCount();
        }
        else
        {
            last = first + rows;
        }
        for (int rowIndex = first; rowIndex < last; rowIndex++)
        {
            setRowIndex(rowIndex);
            if (isRowAvailable())
            {
                for (Iterator it = getChildren().iterator(); it.hasNext();)
                {
                    UIComponent child = (UIComponent) it.next();
                    if (!child.isRendered())
                    {
                        continue;
                    }
                    child.processValidators(context);
                }
            }
        }

        setRowIndex(-1);
    }


    public void setRowIndex(int rowIndex)
    {
        super.setRowIndex(rowIndex);
        String rowIndexVar = getRowIndexVar();
        String rowCountVar = getRowCountVar();
        if (rowIndexVar != null || rowCountVar != null)
        {
            Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
            if (rowIndex >= 0)
            {
                //regular row index, update request scope variables
                if (rowIndexVar != null)
                {
                    requestMap.put(getRowIndexVar(), new Integer(rowIndex));
                }
                if (rowCountVar != null)
                {
                    requestMap.put(getRowCountVar(), new Integer(getRowCount()));
                }
            }
            else
            {
                //rowIndex == -1 means end of loop --> remove request scope variables
                if (rowIndexVar != null)
                {
                    requestMap.remove(getRowIndexVar());
                }
                if (rowCountVar != null)
                {
                    requestMap.remove(getRowCountVar());
                }
            }
        }
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlDataList";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.List";

    private String _layout = null;
    private String _rowIndexVar = null;
    private String _rowCountVar = null;
    private String _onclick = null;
    private String _ondblclick = null;
    private String _onkeydown = null;
    private String _onkeypress = null;
    private String _onkeyup = null;
    private String _onmousedown = null;
    private String _onmousemove = null;
    private String _onmouseout = null;
    private String _onmouseover = null;
    private String _onmouseup = null;
    private String _style = null;
    private String _styleClass = null;
    private String _title = null;

    public HtmlDataList()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }


    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public String getLayout()
    {
        if (_layout != null) return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRowIndexVar(String rowIndexVar)
    {
        _rowIndexVar = rowIndexVar;
    }

    public String getRowIndexVar()
    {
        if (_rowIndexVar != null) return _rowIndexVar;
        ValueBinding vb = getValueBinding("rowIndexVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRowCountVar(String rowCountVar)
    {
        _rowCountVar = rowCountVar;
    }

    public String getRowCountVar()
    {
        if (_rowCountVar != null) return _rowCountVar;
        ValueBinding vb = getValueBinding("rowCountVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnclick(String onclick)
    {
        _onclick = onclick;
    }

    public String getOnclick()
    {
        if (_onclick != null) return _onclick;
        ValueBinding vb = getValueBinding("onclick");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOndblclick(String ondblclick)
    {
        _ondblclick = ondblclick;
    }

    public String getOndblclick()
    {
        if (_ondblclick != null) return _ondblclick;
        ValueBinding vb = getValueBinding("ondblclick");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeydown(String onkeydown)
    {
        _onkeydown = onkeydown;
    }

    public String getOnkeydown()
    {
        if (_onkeydown != null) return _onkeydown;
        ValueBinding vb = getValueBinding("onkeydown");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeypress(String onkeypress)
    {
        _onkeypress = onkeypress;
    }

    public String getOnkeypress()
    {
        if (_onkeypress != null) return _onkeypress;
        ValueBinding vb = getValueBinding("onkeypress");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnkeyup(String onkeyup)
    {
        _onkeyup = onkeyup;
    }

    public String getOnkeyup()
    {
        if (_onkeyup != null) return _onkeyup;
        ValueBinding vb = getValueBinding("onkeyup");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmousedown(String onmousedown)
    {
        _onmousedown = onmousedown;
    }

    public String getOnmousedown()
    {
        if (_onmousedown != null) return _onmousedown;
        ValueBinding vb = getValueBinding("onmousedown");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmousemove(String onmousemove)
    {
        _onmousemove = onmousemove;
    }

    public String getOnmousemove()
    {
        if (_onmousemove != null) return _onmousemove;
        ValueBinding vb = getValueBinding("onmousemove");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmouseout(String onmouseout)
    {
        _onmouseout = onmouseout;
    }

    public String getOnmouseout()
    {
        if (_onmouseout != null) return _onmouseout;
        ValueBinding vb = getValueBinding("onmouseout");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmouseover(String onmouseover)
    {
        _onmouseover = onmouseover;
    }

    public String getOnmouseover()
    {
        if (_onmouseover != null) return _onmouseover;
        ValueBinding vb = getValueBinding("onmouseover");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setOnmouseup(String onmouseup)
    {
        _onmouseup = onmouseup;
    }

    public String getOnmouseup()
    {
        if (_onmouseup != null) return _onmouseup;
        ValueBinding vb = getValueBinding("onmouseup");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyle(String style)
    {
        _style = style;
    }

    public String getStyle()
    {
        if (_style != null) return _style;
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public String getStyleClass()
    {
        if (_styleClass != null) return _styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setTitle(String title)
    {
        _title = title;
    }

    public String getTitle()
    {
        if (_title != null) return _title;
        ValueBinding vb = getValueBinding("title");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }



    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[17];
        values[0] = super.saveState(context);
        values[1] = _layout;
        values[2] = _rowIndexVar;
        values[3] = _rowCountVar;
        values[4] = _onclick;
        values[5] = _ondblclick;
        values[6] = _onkeydown;
        values[7] = _onkeypress;
        values[8] = _onkeyup;
        values[9] = _onmousedown;
        values[10] = _onmousemove;
        values[11] = _onmouseout;
        values[12] = _onmouseover;
        values[13] = _onmouseup;
        values[14] = _style;
        values[15] = _styleClass;
        values[16] = _title;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _layout = (String)values[1];
        _rowIndexVar = (String)values[2];
        _rowCountVar = (String)values[3];
        _onclick = (String)values[4];
        _ondblclick = (String)values[5];
        _onkeydown = (String)values[6];
        _onkeypress = (String)values[7];
        _onkeyup = (String)values[8];
        _onmousedown = (String)values[9];
        _onmousemove = (String)values[10];
        _onmouseout = (String)values[11];
        _onmouseover = (String)values[12];
        _onmouseup = (String)values[13];
        _style = (String)values[14];
        _styleClass = (String)values[15];
        _title = (String)values[16];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

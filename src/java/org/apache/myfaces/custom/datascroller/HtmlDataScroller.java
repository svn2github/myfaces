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
package org.apache.myfaces.custom.datascroller;

import org.apache.myfaces.component.html.ext.HtmlPanelGroup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataScroller
        extends HtmlPanelGroup
{
    //private static final Log log = LogFactory.getLog(HtmlPanelTabbedPane.class);

    private static final String FIRST_FACET_NAME            = "first";
    private static final String LAST_FACET_NAME             = "last";
    private static final String NEXT_FACET_NAME             = "next";
    private static final String PREVIOUS_FACET_NAME         = "previous";
    private static final String FAST_FORWARD_FACET_NAME     = "fastforward";
    private static final String FAST_REWIND_FACET_NAME      = "fastrewind";

    public void setFirst(UIComponent first)
    {
        getFacets().put(FIRST_FACET_NAME, first);
    }

    public UIComponent getFirst()
    {
        return (UIComponent)getFacets().get(FIRST_FACET_NAME);
    }

    public void setLast(UIComponent last)
    {
        getFacets().put(LAST_FACET_NAME, last);
    }

    public UIComponent getLast()
    {
        return (UIComponent)getFacets().get(LAST_FACET_NAME);
    }

    public void setNext(UIComponent next)
    {
        getFacets().put(NEXT_FACET_NAME, next);
    }

    public UIComponent getNext()
    {
        return (UIComponent)getFacets().get(NEXT_FACET_NAME);
    }

    public void setFastForward(UIComponent previous)
    {
        getFacets().put(FAST_FORWARD_FACET_NAME, previous);
    }

    public UIComponent getFastForward()
    {
        return (UIComponent)getFacets().get(FAST_FORWARD_FACET_NAME);
    }

    public void setFastRewind(UIComponent previous)
    {
        getFacets().put(FAST_REWIND_FACET_NAME, previous);
    }

    public UIComponent getFastRewind()
    {
        return (UIComponent)getFacets().get(FAST_REWIND_FACET_NAME);
    }

    public void setPrevious(UIComponent previous)
    {
        getFacets().put(PREVIOUS_FACET_NAME, previous);
    }

    public UIComponent getPrevious()
    {
        return (UIComponent)getFacets().get(PREVIOUS_FACET_NAME);
    }


    public boolean getRendersChildren()
    {
        return true;
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlDataScroller";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.DataScroller";

    private String _for = null;
    private Integer _fastStep = null;
    private String _pageIndexVar = null;
    private String _pageCountVar = null;
    private String _rowsCountVar = null;
    private String _displayedRowsCountVar = null;
    private String _firstRowIndexVar = null;
    private String _lastRowIndexVar = null;
    private String _style = null;
    private String _styleClass = null;
    private String _columnClasses = null;
    private Boolean _paginator = null;
    private Integer _paginatorMaxPages = null;
    private String _paginatorTableClass = null;
    private String _paginatorTableStyle = null;
    private String _paginatorColumnClass = null;
    private String _paginatorColumnStyle = null;
    private String _paginatorActiveColumnClass = null;
    private String _paginatorActiveColumnStyle = null;
	private Boolean _renderFacetsIfSinglePage = null;

    public HtmlDataScroller()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFor(String forValue)
    {
        _for = forValue;
    }

    public String getFor()
    {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFastStep(int fastStep)
    {
        _fastStep = new Integer(fastStep);
    }

    public int getFastStep()
    {
        if (_fastStep != null) return _fastStep.intValue();
        ValueBinding vb = getValueBinding("fastStep");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : Integer.MIN_VALUE;
    }

    public void setPageIndexVar(String pageIndexVar)
    {
        _pageIndexVar = pageIndexVar;
    }

    public String getPageIndexVar()
    {
        if (_pageIndexVar != null) return _pageIndexVar;
        ValueBinding vb = getValueBinding("pageIndexVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPageCountVar(String pageCountVar)
    {
        _pageCountVar = pageCountVar;
    }

    public String getPageCountVar()
    {
        if (_pageCountVar != null) return _pageCountVar;
        ValueBinding vb = getValueBinding("pageCountVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    
    public void setRowsCountVar(String rowsCountVar)
    {
        _rowsCountVar = rowsCountVar;
    }

    public String getRowsCountVar()
    {
        if (_rowsCountVar != null) return _rowsCountVar;
        ValueBinding vb = getValueBinding("rowsCountVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    
    public void setDisplayedRowsCountVar(String displayedRowsCountVar)
    {
        _displayedRowsCountVar = displayedRowsCountVar;
    }
    
    public String getDisplayedRowsCountVar()
    {
        if (_displayedRowsCountVar != null) return _displayedRowsCountVar;
        ValueBinding vb = getValueBinding("displayedRowsCountVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    
    public void setFirstRowIndexVar(String firstRowIndexVar)
    {
        _firstRowIndexVar = firstRowIndexVar;
    }

    public String getFirstRowIndexVar()
    {
        if (_firstRowIndexVar != null) return _firstRowIndexVar;
        ValueBinding vb = getValueBinding("firstRowIndexVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setLastRowIndexVar(String lastRowIndexVar)
    {
        _lastRowIndexVar = lastRowIndexVar;
    }

    public String getLastRowIndexVar()
    {
        if (_lastRowIndexVar != null) return _lastRowIndexVar;
        ValueBinding vb = getValueBinding("lastRowIndexVar");
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

    public void setPaginator(boolean paginator)
    {
        _paginator = Boolean.valueOf(paginator);
    }

    public boolean isPaginator()
    {
        if (_paginator != null) return _paginator.booleanValue();
        ValueBinding vb = getValueBinding("paginator");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setPaginatorMaxPages(int paginatorMaxPages)
    {
        _paginatorMaxPages = new Integer(paginatorMaxPages);
    }

    public int getPaginatorMaxPages()
    {
        if (_paginatorMaxPages != null) return _paginatorMaxPages.intValue();
        ValueBinding vb = getValueBinding("paginatorMaxPages");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : Integer.MIN_VALUE;
    }

    public void setPaginatorTableClass(String paginatorTableClass)
    {
        _paginatorTableClass = paginatorTableClass;
    }

    public String getPaginatorTableClass()
    {
        if (_paginatorTableClass != null) return _paginatorTableClass;
        ValueBinding vb = getValueBinding("paginatorTableClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPaginatorTableStyle(String paginatorTableStyle)
    {
        _paginatorTableStyle = paginatorTableStyle;
    }

    public String getPaginatorTableStyle()
    {
        if (_paginatorTableStyle != null) return _paginatorTableStyle;
        ValueBinding vb = getValueBinding("paginatorTableStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPaginatorColumnClass(String paginatorColumnClass)
    {
        _paginatorColumnClass = paginatorColumnClass;
    }

    public String getPaginatorColumnClass()
    {
        if (_paginatorColumnClass != null) return _paginatorColumnClass;
        ValueBinding vb = getValueBinding("paginatorColumnClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPaginatorColumnStyle(String paginatorColumnStyle)
    {
        _paginatorColumnStyle = paginatorColumnStyle;
    }

    public String getPaginatorColumnStyle()
    {
        if (_paginatorColumnStyle != null) return _paginatorColumnStyle;
        ValueBinding vb = getValueBinding("paginatorColumnStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPaginatorActiveColumnClass(String paginatorActiveColumnClass)
    {
        _paginatorActiveColumnClass = paginatorActiveColumnClass;
    }

    public String getPaginatorActiveColumnClass()
    {
        if (_paginatorActiveColumnClass != null) return _paginatorActiveColumnClass;
        ValueBinding vb = getValueBinding("paginatorActiveColumnClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setPaginatorActiveColumnStyle(String paginatorActiveColumnStyle)
    {
        _paginatorActiveColumnStyle = paginatorActiveColumnStyle;
    }

    public String getPaginatorActiveColumnStyle()
    {
        if (_paginatorActiveColumnStyle != null) return _paginatorActiveColumnStyle;
        ValueBinding vb = getValueBinding("paginatorActiveColumnStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setRenderFacetsIfSinglePage(boolean renderFacetsIfSinglePage)
    {
		_renderFacetsIfSinglePage = Boolean.valueOf(renderFacetsIfSinglePage);
    }
	
    public boolean isRenderFacetsIfSinglePage()
    {
        if (_renderFacetsIfSinglePage != null) return _renderFacetsIfSinglePage.booleanValue();
        ValueBinding vb = getValueBinding("renderFacetsIfSinglePage");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[21];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _fastStep;
        values[3] = _pageIndexVar;
        values[4] = _pageCountVar;
        values[5] = _rowsCountVar;
        values[6] = _displayedRowsCountVar;
        values[7] = _firstRowIndexVar;
        values[8] = _lastRowIndexVar;
        values[9] = _style;
        values[10] = _styleClass;
        values[11] = _columnClasses;
        values[12] = _paginator;
        values[13] = _paginatorMaxPages;
        values[14] = _paginatorTableClass;
        values[15] = _paginatorTableStyle;
        values[16] = _paginatorColumnClass;
        values[17] = _paginatorColumnStyle;
        values[18] = _paginatorActiveColumnClass;
        values[19] = _paginatorActiveColumnStyle;
		values[20] = _renderFacetsIfSinglePage;
        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _for = (String)values[1];
        _fastStep = (Integer)values[2];
        _pageIndexVar = (String)values[3];
        _pageCountVar = (String)values[4];
        _rowsCountVar = (String)values[5];
        _displayedRowsCountVar = (String)values[6];
        _firstRowIndexVar = (String)values[7];
        _lastRowIndexVar = (String)values[8];
        _style = (String)values[9];
        _styleClass = (String)values[10];
        _columnClasses = (String)values[11];
        _paginator = (Boolean)values[12];
        _paginatorMaxPages = (Integer)values[13];
        _paginatorTableClass = (String)values[14];
        _paginatorTableStyle = (String)values[15];
        _paginatorColumnClass = (String)values[16];
        _paginatorColumnStyle = (String)values[17];
        _paginatorActiveColumnClass = (String)values[18];
        _paginatorActiveColumnStyle = (String)values[19];
		_renderFacetsIfSinglePage = (Boolean)values[20];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

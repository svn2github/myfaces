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

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * JSP tag class for the HtmlDataScroller component.
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataScrollerTag
        extends UIComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataScrollerTag.class);

    private static final String FOR_ATTR                = "for";
    private static final String FAST_STEP_ATTR          = "fastStep";
    private static final String PAGE_INDEX_ATTR         = "pageIndexVar";
    private static final String PAGE_COUNT_ATTR         = "pageCountVar";
    private static final String ROWS_COUNT_ATTR         = "rowsCountVar";
    private static final String DISPLAYED_ROWS_COUNT_ATTR = "displayedRowsCountVar";
    private static final String FIRST_ROW_INDEX_ATTR    = "firstRowIndexVar";
    private static final String LAST_ROW_INDEX_ATTR     = "lastRowIndexVar";
    private static final String STYLE_CLASS_ATTR        = "styleClass";
    private static final String STYLE_ATTR              = "style";
    private static final String PAGINATOR_ATTR          = "paginator";
    private static final String PAGINATOR_MAX_PAGES_ATTR = "paginatorMaxPages";
    private static final String PAGINATOR_TABLE_CLASS_ATTR  = "paginatorTableClass";
    private static final String PAGINATOR_TABLE_STYLE_ATTR  = "paginatorTableStyle";
    private static final String PAGINATOR_COL_CLASS_ATTR    = "paginatorColumnClass";
    private static final String PAGINATOR_COL_STYLE_ATTR    = "paginatorColumnStyle";
    private static final String PAGINATOR_ACTCOL_CLASS_ATTR = "paginatorActiveColumnClass";
    private static final String PAGINATOR_ACTCOL_STYLE_ATTR = "paginatorActiveColumnStyle";
    private static final String RENDER_FACETS_IF_SINGLE_PAGE_ATTR = "renderFacetsIfSinglePage";

    private String _for;
    private String _fastStep;
    private String _pageIndexVar;
    private String _pageCountVar;
    private String _rowsCountVar;
    private String _displayedRowsCountVar;
    private String _firstRowIndexVar;
    private String _lastRowIndexVar;
    private String _paginator;
    private String _styleClass;
    private String _style;
    private String _paginatorMaxPages;
    private String _paginatorTableClass;
    private String _paginatorTableStyle;
    private String _paginatorColumnClass;
    private String _paginatorColumnStyle;
    private String _paginatorActiveColumnClass;
    private String _paginatorActiveColumnStyle;
    private String _renderFacetsIfSinglePage;
    
    private String _immediate;
    private String _actionListener;

    // User Role support
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;

    public void release() {
        super.release();
        _for=null;
        _fastStep=null;
        _pageIndexVar=null;
        _pageCountVar=null;
        _rowsCountVar=null;
        _displayedRowsCountVar=null;
        _firstRowIndexVar=null;
        _lastRowIndexVar=null;
        _paginator=null;
        _styleClass=null;
        _style=null;
        _paginatorMaxPages=null;
        _paginatorTableClass=null;
        _paginatorTableStyle=null;
        _paginatorColumnClass=null;
        _paginatorColumnStyle=null;
        _paginatorActiveColumnClass=null;
        _paginatorActiveColumnStyle=null;
        _renderFacetsIfSinglePage=null;
        _enabledOnUserRole=null;
        _visibleOnUserRole=null;
        _immediate=null;
        _actionListener=null;
    }
    
    public String getComponentType()
    {
        return HtmlDataScroller.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return HtmlDataScrollerRenderer.RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, FOR_ATTR, _for);
        setIntegerProperty(component, FAST_STEP_ATTR, _fastStep);
        setStringProperty(component, PAGE_INDEX_ATTR, _pageIndexVar);
        setStringProperty(component, PAGE_COUNT_ATTR, _pageCountVar);
        setStringProperty(component, ROWS_COUNT_ATTR, _rowsCountVar);
        setStringProperty(component, DISPLAYED_ROWS_COUNT_ATTR, _displayedRowsCountVar);
        setStringProperty(component, FIRST_ROW_INDEX_ATTR, _firstRowIndexVar);
        setStringProperty(component, LAST_ROW_INDEX_ATTR, _lastRowIndexVar);
        setStringProperty(component, STYLE_CLASS_ATTR, _styleClass);
        setStringProperty(component, STYLE_ATTR, _style);
        setBooleanProperty(component, PAGINATOR_ATTR, _paginator);
        setIntegerProperty(component, PAGINATOR_MAX_PAGES_ATTR, _paginatorMaxPages);
        setStringProperty(component, PAGINATOR_TABLE_CLASS_ATTR, _paginatorTableClass);
        setStringProperty(component, PAGINATOR_TABLE_STYLE_ATTR, _paginatorTableStyle);
        setStringProperty(component, PAGINATOR_COL_CLASS_ATTR, _paginatorColumnClass);
        setStringProperty(component, PAGINATOR_COL_STYLE_ATTR, _paginatorColumnStyle);
        setStringProperty(component, PAGINATOR_ACTCOL_CLASS_ATTR, _paginatorActiveColumnClass);
        setStringProperty(component, PAGINATOR_ACTCOL_STYLE_ATTR, _paginatorActiveColumnStyle);
        setBooleanProperty(component, RENDER_FACETS_IF_SINGLE_PAGE_ATTR, _renderFacetsIfSinglePage);
        
        setBooleanProperty(component, JSFAttr.IMMEDIATE_ATTR, _immediate);
        setActionListenerProperty(component, _actionListener);

        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
    }

    // datascroller attributes
    public void setFor(String aFor)
    {
        _for = aFor;
    }

    public void setFastStep(String fastStep)
    {
        _fastStep = fastStep;
    }

    public void setPageCountVar(String pageCountVar)
    {
        _pageCountVar = pageCountVar;
    }

    public void setPageIndexVar(String pageIndexVar)
    {
        _pageIndexVar = pageIndexVar;
    }
    
    public void setRowsCountVar(String rowsCountVar)
    {
        _rowsCountVar = rowsCountVar;
    }
    
    public void setDisplayedRowsCountVar(String displayedRowsCountVar)
    {
        _displayedRowsCountVar = displayedRowsCountVar;
    }
    
    public void setFirstRowIndexVar(String firstRowIndexVar)
    {
        _firstRowIndexVar = firstRowIndexVar;
    }
    
    public void setLastRowIndexVar(String lastRowIndexVar)
    {
        _lastRowIndexVar = lastRowIndexVar;
    }

    public void setStyle(String style)
    {
        _style = style;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public void setPaginator(String paginator)
    {
        _paginator = paginator;
    }

    public void setPaginatorMaxPages(String paginatorMaxPages)
    {
        _paginatorMaxPages = paginatorMaxPages;
    }

    public void setPaginatorTableClass(String paginatorTableClass)
    {
        _paginatorTableClass = paginatorTableClass;
    }

    public void setPaginatorColumnClass(String paginatorColumnClass)
    {
        _paginatorColumnClass = paginatorColumnClass;
    }

    public void setPaginatorColumnStyle(String paginatorColumnStyle)
    {
        _paginatorColumnStyle = paginatorColumnStyle;
    }

    public void setPaginatorTableStyle(String paginatorTableStyle)
    {
        _paginatorTableStyle = paginatorTableStyle;
    }

    public void setPaginatorActiveColumnClass(String paginatorActiveColumnClass)
    {
        this._paginatorActiveColumnClass = paginatorActiveColumnClass;
    }

    public void setPaginatorActiveColumnStyle(String paginatorActiveColumnStyle)
    {
        _paginatorActiveColumnStyle = paginatorActiveColumnStyle;
    }

    public void setRenderFacetsIfSinglePage(String renderFacetsIfSinglePage)
    {
        _renderFacetsIfSinglePage = renderFacetsIfSinglePage;
    }

    public void setImmediate(String immediate)
    {
        _immediate = immediate;
    }

    public void setActionListener(String actionListener)
    {
        _actionListener = actionListener;
    }

    // userrole attributes
    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }
}

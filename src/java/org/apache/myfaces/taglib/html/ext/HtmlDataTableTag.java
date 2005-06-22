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
package org.apache.myfaces.taglib.html.ext;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.taglib.html.HtmlDataTableTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataTableTag
        extends HtmlDataTableTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataTableTag.class);

    public String getComponentType()
    {
        return HtmlDataTable.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.Table";
    }

    private String _preserveDataModel;
    private String _sortColumn;
    private String _sortAscending;
    private String _preserveSort;
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;
    private String _renderedIfEmpty;
    private String _rowIndexVar;
    private String _rowCountVar;
    private String _previousRowDataVar;
    private String _rowOnMouseOver;
    private String _rowOnMouseOut;

    public void release() {
        super.release();

        _preserveDataModel=null;
        _sortColumn=null;
        _sortAscending=null;
        _preserveSort=null;
        _enabledOnUserRole=null;
        _visibleOnUserRole=null;
        _renderedIfEmpty=null;
        _rowIndexVar=null;
        _rowCountVar=null;
        _previousRowDataVar=null;
        _rowOnMouseOver=null;
        _rowOnMouseOut=null;

    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, "preserveDataModel", _preserveDataModel);
        setValueBinding(component, "sortColumn", _sortColumn);
        setValueBinding(component, "sortAscending", _sortAscending);
        setBooleanProperty(component, "preserveSort", _preserveSort);
        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
        setBooleanProperty(component, "renderedIfEmpty", _renderedIfEmpty);
        setStringProperty(component, "rowIndexVar", _rowIndexVar);
        setStringProperty(component, "rowCountVar", _rowCountVar);
        setStringProperty(component, "previousRowDataVar", _previousRowDataVar);
        setStringProperty(component, "rowOnMouseOver", _rowOnMouseOver);
        setStringProperty(component, "rowOnMouseOut", _rowOnMouseOut);
    }

    public void setPreserveDataModel(String preserveDataModel)
    {
        _preserveDataModel = preserveDataModel;
    }

    public void setSortColumn(String sortColumn)
    {
        _sortColumn = sortColumn;
    }

    public void setSortAscending(String sortAscending)
    {
        _sortAscending = sortAscending;
    }

    public void setPreserveSort(String preserveSort)
    {
        _preserveSort = preserveSort;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public void setRenderedIfEmpty(String renderedIfEmpty)
    {
        _renderedIfEmpty = renderedIfEmpty;
    }

    public void setRowIndexVar(String rowIndexVar)
    {
        _rowIndexVar = rowIndexVar;
    }

    public void setRowCountVar(String rowCountVar)
    {
        _rowCountVar = rowCountVar;
    }

    public void setPreviousRowDataVar(String previousRowDataVar)
    {
        _previousRowDataVar = previousRowDataVar;
    }

    public void setRowOnMouseOver(String rowOnMouseOver)
    {
        _rowOnMouseOver = rowOnMouseOver;
    }

    public void setRowOnMouseOut(String rowOnMouseOut)
    {
        _rowOnMouseOut = rowOnMouseOut;
    }
}

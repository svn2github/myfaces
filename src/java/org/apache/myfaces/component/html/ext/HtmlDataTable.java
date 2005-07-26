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
package org.apache.myfaces.component.html.ext;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.custom.crosstable.UIColumns;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class HtmlDataTable extends HtmlDataTableHack implements UserRoleAware
{
    private static final Log log = LogFactory.getLog(HtmlDataTable.class);

    private static final int PROCESS_DECODES = 1;
    private static final int PROCESS_VALIDATORS = 2;
    private static final int PROCESS_UPDATES = 3;

    private static final boolean DEFAULT_SORTASCENDING = true;
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    private _SerializableDataModel _preservedDataModel;

    private String _sortColumn = null;
    private Boolean _sortAscending = null;
    private String _rowOnClick = null;
    private String _rowOnDblClick = null;
    private String _rowOnMouseDown = null;
    private String _rowOnMouseUp = null;
    private String _rowOnMouseOver = null;
    private String _rowOnMouseMove = null;
    private String _rowOnMouseOut = null;
    private String _rowOnKeyPress = null;
    private String _rowOnKeyDown = null;
    private String _rowOnKeyUp = null;
    

    private boolean _isValidChilds = true;

	public void setRowIndex(int rowIndex)
	{
		if(rowIndex < -1)
		{
			throw new IllegalArgumentException("rowIndex is less than -1");
		}
		
		String rowIndexVar = getRowIndexVar();
		String rowCountVar = getRowCountVar();
		String previousRowDataVar = getPreviousRowDataVar();
		if (rowIndexVar != null || rowCountVar != null || previousRowDataVar != null)
		{
			Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

            if (previousRowDataVar != null && rowIndex >= 0) //we only need to provide the previousRowDataVar for a valid rowIndex
            {
                    if (isRowAvailable())
                    {
                            //previous row is available
                            requestMap.put(previousRowDataVar, getRowData());
                    }
                    else
                    {
                            //no previous row available
                            requestMap.put(previousRowDataVar, null);
                    }
            }

            super.setRowIndex(rowIndex);

            if (rowIndex >= 0)
            {
                    //regular row index, update request scope variables
                    if (rowIndexVar != null)
                    {
                            requestMap.put(rowIndexVar, new Integer(rowIndex));
                    }

                    if (rowCountVar != null)
                    {
                            requestMap.put(rowCountVar, new Integer(getRowCount()));
                    }
            }
            else
            {
                    //rowIndex == -1 means end of loop --> remove request scope variables
                    if (rowIndexVar != null)
                    {
                            requestMap.remove(rowIndexVar);
                    }

                    if (rowCountVar != null)
                    {
                            requestMap.remove(rowCountVar);
                    }

                    if (previousRowDataVar != null)
                    {
                            requestMap.remove(previousRowDataVar);
                    }
            }
        }
        else
        {
                // no extended var attributes defined, no special treatment
             super.setRowIndex(rowIndex);
        }
    }

    public void processDecodes(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        super.processDecodes(context);
        setRowIndex(-1);
        processColumns(context, PROCESS_DECODES);
        setRowIndex(-1);
    }
    
    /**
     * @param context
     * @param processAction
     */
    private void processColumns(FacesContext context, int processAction)
    {
        for (Iterator it = getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent) it.next();
            if (child instanceof UIColumns)
            {
                    process(context, child, processAction);
            }
        }
    }
    
    private void process(FacesContext context, UIComponent component, int processAction)
    {
        switch (processAction)
        {
            case PROCESS_DECODES:
                    component.processDecodes(context);
                    break;
            case PROCESS_VALIDATORS:
                    component.processValidators(context);
                    break;
            case PROCESS_UPDATES:
                    component.processUpdates(context);
                    break;
        }
    }
    
    public void processValidators(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        super.processValidators(context);
        processColumns(context, PROCESS_VALIDATORS);
        setRowIndex(-1);

        if (context.getRenderResponse())
        {
            _isValidChilds = false;
        }
    }
    
    public void processUpdates(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        super.processUpdates(context);
        processColumns(context, PROCESS_UPDATES);
        setRowIndex(-1);

        if (isPreserveDataModel())
        {
            updateModelFromPreservedDataModel(context);
        }

        if (context.getRenderResponse())
        {
            _isValidChilds = false;
        }
    }
    
    private void updateModelFromPreservedDataModel(FacesContext context)
    {
        ValueBinding vb = getValueBinding("value");
        if (vb != null && !vb.isReadOnly(context))
        {
            _SerializableDataModel dm = (_SerializableDataModel) getDataModel();
            Class type = vb.getType(context);
            if (DataModel.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm);
            }
            else if (List.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm.getWrappedData());
            }
            else if (OBJECT_ARRAY_CLASS.isAssignableFrom(type))
            {
                List lst = (List) dm.getWrappedData();
                vb.setValue(context, lst.toArray(new Object[lst.size()]));
            }
            else if (ResultSet.class.isAssignableFrom(type))
            {
                throw new UnsupportedOperationException(this.getClass().getName()
                                                + " UnsupportedOperationException");
            }
            else
            {
                //Assume scalar data model
                List lst = (List) dm.getWrappedData();
                if (lst.size() > 0)
                {
                        vb.setValue(context, lst.get(0));
                }
                else
                {
                        vb.setValue(context, null);
                }
            }
        }
        _preservedDataModel = null;
    }
    
    public void encodeBegin(FacesContext context) throws IOException
    {
        if (!isRendered())
            return;

        if (_isValidChilds)
        {
            _preservedDataModel = null;
        }

        if (isRenderedIfEmpty() || getRowCount() > 0)
        {
            super.encodeBegin(context);
            for (Iterator iter = getChildren().iterator(); iter.hasNext();)
            {
                UIComponent component = (UIComponent) iter.next();
                if (component instanceof UIColumns)
                {
                        ((UIColumns) component).encodeTableBegin(context);
                }
            }
        }
    }
    
    /**
     * @see javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context.FacesContext)
     */
    public void encodeChildren(FacesContext context) throws IOException
    {
        if (isRenderedIfEmpty() || getRowCount() > 0)
        {
            super.encodeChildren(context);
        }
    }
    
    /**
     * @see javax.faces.component.UIData#encodeEnd(javax.faces.context.FacesContext)
     */
    public void encodeEnd(FacesContext context) throws IOException
    {
        if (isRenderedIfEmpty() || getRowCount() > 0)
        {
            super.encodeEnd(context);
            for (Iterator iter = getChildren().iterator(); iter.hasNext();)
            {
                UIComponent component = (UIComponent) iter.next();
                if (component instanceof UIColumns)
                {
                    ((UIColumns) component).encodeTableEnd(context);
                }
            }
        }
    }
    
    public int getFirst()
    {
        if (_preservedDataModel != null)
        {
            //Rather get the currently restored DataModel attribute
            return ((_SerializableDataModel) _preservedDataModel).getFirst();
        }
        else
        {
            return super.getFirst();
        }
    }
    
    public void setFirst(int first)
    {
        if (_preservedDataModel != null)
        {
            //Also change the currently restored DataModel attribute
            ((_SerializableDataModel) _preservedDataModel).setFirst(first);
        }
        super.setFirst(first);
    }
    
    public int getRows()
    {
        if (_preservedDataModel != null)
        {
            //Rather get the currently restored DataModel attribute
            return ((_SerializableDataModel) _preservedDataModel).getRows();
        }
        else
        {
            return super.getRows();
        }
    }
    
    public void setRows(int rows)
    {
        if (_preservedDataModel != null)
        {
            //Also change the currently restored DataModel attribute
            ((_SerializableDataModel) _preservedDataModel).setRows(rows);
        }
        super.setRows(rows);
    }
    
    public Object saveState(FacesContext context)
    {
        boolean preserveSort = isPreserveSort();
    	Object values[] = new Object[21];
        values[0] = super.saveState(context);
        values[1] = _preserveDataModel;
        if (isPreserveDataModel())
        {
            values[2] = saveAttachedState(context, getSerializableDataModel());
        }
        else
        {
            values[2] = null;
        }
        values[3] = _preserveSort;
        values[4] = _sortColumn;
        values[5] = _sortAscending;
        values[6] = _renderedIfEmpty;
        values[7] = _rowCountVar;
        values[8] = _rowIndexVar;

        values[9] = _rowOnClick;
        values[10] = _rowOnDblClick;
        values[11] = _rowOnMouseDown;
        values[12] = _rowOnMouseUp;
        values[13] = _rowOnMouseOver;
        values[14] = _rowOnMouseMove;
        values[15] = _rowOnMouseOut;
        values[16] = _rowOnKeyPress;
        values[17] = _rowOnKeyDown;
        values[18] = _rowOnKeyUp;

        values[19] = preserveSort ? getSortColumn() : null;
        values[20] = preserveSort ? Boolean.valueOf(isSortAscending()) : null;
        return values;
    }
    
    /**
     * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#getDataModel()
     */
    protected DataModel getDataModel()
    {
            if (_preservedDataModel != null)
            {
                    setDataModel(_preservedDataModel);
                    _preservedDataModel = null;
            }
            return super.getDataModel();
    }
    
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _preserveDataModel = (Boolean) values[1];
        if (isPreserveDataModel())
        {
                _preservedDataModel = (_SerializableDataModel) restoreAttachedState(context, values[2]);
        }
        else
        {
                _preservedDataModel = null;
        }
        _preserveSort = (Boolean) values[3];
        _sortColumn = (String) values[4];
        _sortAscending = (Boolean) values[5];
        _renderedIfEmpty = (Boolean) values[6];
        _rowCountVar = (String) values[7];
        _rowIndexVar = (String) values[8];

        _rowOnClick = (String) values[9];
        _rowOnDblClick = (String) values[10];
        _rowOnMouseDown = (String) values[11];
        _rowOnMouseUp = (String) values[12];
        _rowOnMouseOver = (String) values[13];
        _rowOnMouseMove = (String) values[14];
        _rowOnMouseOut = (String) values[15];
        _rowOnKeyPress = (String) values[16];
        _rowOnKeyDown = (String) values[17];
        _rowOnKeyUp = (String) values[18];

        if (isPreserveSort())
        {
            String sortColumn = (String) values[19];
            Boolean sortAscending = (Boolean) values[20];
            if (sortColumn != null && sortAscending != null)
            {
                ValueBinding vb = getValueBinding("sortColumn");
                if (vb != null && !vb.isReadOnly(context))
                {
                    vb.setValue(context, sortColumn);
                }

                vb = getValueBinding("sortAscending");
                if (vb != null && !vb.isReadOnly(context))
                {
                    vb.setValue(context, sortAscending);
                }
            }
        }
    }
    
    public _SerializableDataModel getSerializableDataModel()
    {
        DataModel dm = getDataModel();
        if (dm instanceof _SerializableDataModel)
        {
            return (_SerializableDataModel) dm;
        }
        return createSerializableDataModel();
    }
    
    /**
     * @return
     */
    private _SerializableDataModel createSerializableDataModel()
    {
            Object value = getValue();
            if (value == null)
            {
                    return null;
            }
            else if (value instanceof DataModel)
            {
                    return new _SerializableDataModel(getFirst(), getRows(), (DataModel) value);
            }
            else if (value instanceof List)
            {
                    return new _SerializableListDataModel(getFirst(), getRows(), (List) value);
            }
            else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
            {
                    return new _SerializableArrayDataModel(getFirst(), getRows(), (Object[]) value);
            }
            else if (value instanceof ResultSet)
            {
                    return new _SerializableResultSetDataModel(getFirst(), getRows(), (ResultSet) value);
            }
            else if (value instanceof javax.servlet.jsp.jstl.sql.Result)
            {
                    return new _SerializableResultDataModel(getFirst(), getRows(),
                                                    (javax.servlet.jsp.jstl.sql.Result) value);
            }
            else
            {
                    return new _SerializableScalarDataModel(getFirst(), getRows(), value);
            }
    }
    
    public boolean isRendered()
    {
            if (!UserRoleUtils.isVisibleOnUserRole(this))
                    return false;
            return super.isRendered();
    }
    
    public void setSortColumn(String sortColumn)
    {
            _sortColumn = sortColumn;
            // update model is necessary here, because processUpdates is never called
            // reason: HtmlCommandSortHeader.isImmediate() == true
            ValueBinding vb = getValueBinding("sortColumn");
            if (vb != null)
            {
                    vb.setValue(getFacesContext(), _sortColumn);
                    _sortColumn = null;
            }
    }
    
    public String getSortColumn()
    {
            if (_sortColumn != null)
                    return _sortColumn;
            ValueBinding vb = getValueBinding("sortColumn");
            return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public void setSortAscending(boolean sortAscending)
    {
            _sortAscending = Boolean.valueOf(sortAscending);
            // update model is necessary here, because processUpdates is never called
            // reason: HtmlCommandSortHeader.isImmediate() == true
            ValueBinding vb = getValueBinding("sortAscending");
            if (vb != null)
            {
                    vb.setValue(getFacesContext(), _sortAscending);
                    _sortAscending = null;
            }
    }
    
    public boolean isSortAscending()
    {
        if (_sortAscending != null)
            return _sortAscending.booleanValue();
        ValueBinding vb = getValueBinding("sortAscending");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_SORTASCENDING;
    }
    
    public void setRowOnMouseOver(String rowOnMouseOver)
    {
        _rowOnMouseOver = rowOnMouseOver;
    }
    
    public String getRowOnMouseOver()
    {
        if (_rowOnMouseOver != null)
            return _rowOnMouseOver;
        ValueBinding vb = getValueBinding("rowOnMouseOver");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public void setRowOnMouseOut(String rowOnMouseOut)
    {
        _rowOnMouseOut = rowOnMouseOut;
    }
    
    public String getRowOnMouseOut()
    {
        if (_rowOnMouseOut != null)
            return _rowOnMouseOut;
        ValueBinding vb = getValueBinding("rowOnMouseOut");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public void setRowOnClick(String rowOnClick)
    {
        _rowOnClick = rowOnClick;
    }

    public String getRowOnClick()
    {
        if (_rowOnClick != null)
            return _rowOnClick;
        ValueBinding vb = getValueBinding("rowOnClick");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    public void setRowOnDblClick(String rowOnDblClick)
    {
        _rowOnDblClick = rowOnDblClick;
    }

    public String getRowOnDblClick()
    {
        if (_rowOnDblClick != null)
            return _rowOnDblClick;
        ValueBinding vb = getValueBinding("rowOnDblClick");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }


  public String getRowOnKeyDown()
  {
    if (_rowOnKeyDown != null)
      return _rowOnKeyDown;
    ValueBinding vb = getValueBinding("rowOnKeyDown");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnKeyDown(String rowOnKeyDown)
  {
    _rowOnKeyDown = rowOnKeyDown;
  }

  public String getRowOnKeyPress()
  {
    if (_rowOnKeyPress != null)
      return _rowOnKeyPress;
    ValueBinding vb = getValueBinding("rowOnKeyPress");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnKeyPress(String rowOnKeyPress)
  {
    _rowOnKeyPress = rowOnKeyPress;
  }

  public String getRowOnKeyUp()
  {
    if (_rowOnKeyUp != null)
      return _rowOnKeyUp;
    ValueBinding vb = getValueBinding("rowOnKeyUp");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnKeyUp(String rowOnKeyUp)
  {
    _rowOnKeyUp = rowOnKeyUp;
  }

  public String getRowOnMouseDown()
  {
    if (_rowOnMouseDown != null)
      return _rowOnMouseDown;
    ValueBinding vb = getValueBinding("rowOnMouseDown");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnMouseDown(String rowOnMouseDown)
  {
    _rowOnMouseDown = rowOnMouseDown;
  }

  public String getRowOnMouseMove()
  {
    if (_rowOnMouseMove != null)
      return _rowOnMouseMove;
    ValueBinding vb = getValueBinding("rowOnMouseMove");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnMouseMove(String rowOnMouseMove)
  {
    _rowOnMouseMove = rowOnMouseMove;
  }

  public String getRowOnMouseUp()
  {
    if (_rowOnMouseUp != null)
      return _rowOnMouseUp;
    ValueBinding vb = getValueBinding("rowOnMouseUp");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnMouseUp(String rowOnMouseUp)
  {
    _rowOnMouseUp = rowOnMouseUp;
  }

	//------------------ GENERATED CODE BEGIN (do not modify!) --------------------

	public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlDataTable";
	private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
	private static final boolean DEFAULT_PRESERVESORT = true;
	private static final boolean DEFAULT_RENDEREDIFEMPTY = true;

	private Boolean _preserveDataModel = null;
	private Boolean _preserveSort = null;
	private String _enabledOnUserRole = null;
	private String _visibleOnUserRole = null;
	private Boolean _renderedIfEmpty = null;
	private String _rowIndexVar = null;
	private String _rowCountVar = null;
	private String _previousRowDataVar = null;

	public HtmlDataTable()
	{
	}

	public void setPreserveDataModel(boolean preserveDataModel)
	{
		_preserveDataModel = Boolean.valueOf(preserveDataModel);
	}

	public boolean isPreserveDataModel()
	{
		if (_preserveDataModel != null)
			return _preserveDataModel.booleanValue();
		ValueBinding vb = getValueBinding("preserveDataModel");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_PRESERVEDATAMODEL;
	}

	public void setPreserveSort(boolean preserveSort)
	{
		_preserveSort = Boolean.valueOf(preserveSort);
	}

	public boolean isPreserveSort()
	{
		if (_preserveSort != null)
			return _preserveSort.booleanValue();
		ValueBinding vb = getValueBinding("preserveSort");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_PRESERVESORT;
	}

	public void setEnabledOnUserRole(String enabledOnUserRole)
	{
		_enabledOnUserRole = enabledOnUserRole;
	}

	public String getEnabledOnUserRole()
	{
		if (_enabledOnUserRole != null)
			return _enabledOnUserRole;
		ValueBinding vb = getValueBinding("enabledOnUserRole");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setVisibleOnUserRole(String visibleOnUserRole)
	{
		_visibleOnUserRole = visibleOnUserRole;
	}

	public String getVisibleOnUserRole()
	{
		if (_visibleOnUserRole != null)
			return _visibleOnUserRole;
		ValueBinding vb = getValueBinding("visibleOnUserRole");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setRenderedIfEmpty(boolean renderedIfEmpty)
	{
		_renderedIfEmpty = Boolean.valueOf(renderedIfEmpty);
	}

	public boolean isRenderedIfEmpty()
	{
		if (_renderedIfEmpty != null)
			return _renderedIfEmpty.booleanValue();
		ValueBinding vb = getValueBinding("renderedIfEmpty");
		Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
		return v != null ? v.booleanValue() : DEFAULT_RENDEREDIFEMPTY;
	}

	public void setRowIndexVar(String rowIndexVar)
	{
		_rowIndexVar = rowIndexVar;
	}

	public String getRowIndexVar()
	{
		if (_rowIndexVar != null)
			return _rowIndexVar;
		ValueBinding vb = getValueBinding("rowIndexVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setRowCountVar(String rowCountVar)
	{
		_rowCountVar = rowCountVar;
	}

	public String getRowCountVar()
	{
		if (_rowCountVar != null)
			return _rowCountVar;
		ValueBinding vb = getValueBinding("rowCountVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setPreviousRowDataVar(String previousRowDataVar)
	{
		_previousRowDataVar = previousRowDataVar;
	}

	public String getPreviousRowDataVar()
	{
		if (_previousRowDataVar != null)
			return _previousRowDataVar;
		ValueBinding vb = getValueBinding("previousRowDataVar");
		return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	//------------------ GENERATED CODE END ---------------------------------------
}

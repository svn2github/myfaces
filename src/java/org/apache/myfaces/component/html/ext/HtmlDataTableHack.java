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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;

/**
 * Reimplement all UIData functionality to be able to have (protected) access
 * the internal DataModel.
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
abstract class HtmlDataTableHack extends javax.faces.component.html.HtmlDataTable
{
  private Map _dataModelMap = new HashMap();

  // will be set to false if the data should not be refreshed at the beginning of the encode phase
  private boolean _isValidChilds = true;

  // holds for each row the states of the child components of this UIData 
  private Map _rowStates = null;

  // contains the initial row state which is used to initialize each row
  private Object _initialRowState = null;

  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // Every field and method from here is identical to UIData !!!!!!!!!

  private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

  private int _rowIndex = -1;

  public boolean isRowAvailable()
  {
    return getDataModel().isRowAvailable();
  }

  public int getRowCount()
  {
    return getDataModel().getRowCount();
  }

  public Object getRowData()
  {
    return getDataModel().getRowData();
  }

  public int getRowIndex()
  {
    return _rowIndex;
  }

  /**
   * @see javax.faces.component.UIData#processUpdates(javax.faces.context.FacesContext)
   */
  public void processUpdates(FacesContext context)
  {
    super.processUpdates(context);
    // check if a update model error forces the render response for our data
    if (context.getRenderResponse())
    {
      _isValidChilds = false;
    }
  }

  /**
   * @see javax.faces.component.UIData#processValidators(javax.faces.context.FacesContext)
   */
  public void processValidators(FacesContext context)
  {
    super.processValidators(context);
    // check if a validation error forces the render response for our data
    if (context.getRenderResponse())
    {
      _isValidChilds = false;
    }
  }

  /**
   * @see javax.faces.component.UIData#encodeBegin(javax.faces.context.FacesContext)
   */
  public void encodeBegin(FacesContext context) throws IOException
  {
    if (_isValidChilds)
    {
      //Refresh DataModel for rendering:
      _dataModelMap.clear();
      _rowStates = null;
    }
    super.encodeBegin(context);
  }

  /**
   * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
   */
  public void encodeEnd(FacesContext context) throws IOException
  {
    setRowIndex(-1);
    super.encodeEnd(context);
  }

  public void setRowIndex(int rowIndex)
  {
    if (_rowIndex == rowIndex)
    {
      return;
    }

    FacesContext context = getFacesContext();

    if (_rowIndex != -1)
    {
      if (_rowStates == null)
      {
        _rowStates = new HashMap();
      }
      _rowStates.put(getClientId(context), processSaveRowState(context));
    }
    else if (_initialRowState == null)
    {
      _initialRowState = processSaveRowState(context);
    }

    _rowIndex = rowIndex;

    DataModel dataModel = getDataModel();
    dataModel.setRowIndex(rowIndex);

    String var = getVar();
    if (rowIndex == -1)
    {
      if (var != null)
      {
        context.getExternalContext().getRequestMap().remove(var);
      }
    }
    else
    {
      if (var != null)
      {
        if (isRowAvailable())
        {
          Object rowData = dataModel.getRowData();
          context.getExternalContext().getRequestMap().put(var, rowData);
        }
        else
        {
          context.getExternalContext().getRequestMap().remove(var);
        }
      }
    }

    if (_rowIndex != -1 && _rowStates != null)
    {
      Object state = _rowStates.get(getClientId(context));
      if (state == null)
      {
        state = _initialRowState;
      }
      processRestoreRowState(context, state);
    }
    else
    {
      processRestoreRowState(context, _initialRowState);
    }
  }

  private Object processSaveRowState(FacesContext context)
  {
    if (isTransient())
      return null;
    List childStates = null;
    int columnCount = getChildCount();
    if (columnCount > 0)
    {
      for (Iterator colIt = getChildren().iterator(); colIt.hasNext();)
      {
        UIComponent child = (UIComponent) colIt.next();
        if (!child.isTransient())
        {
          if (childStates == null)
          {
            childStates = new ArrayList(columnCount);
          }
          if (child instanceof UIColumn)
          {
            childStates.add(new Object[] {child.saveState(context),
                getColumnChildsState(context, child.getChildren().iterator())});
          }
          else
          {
            childStates.add(child.processSaveState(context));
          }
        }
      }
    }
    return childStates;
  }

  private Object getColumnChildsState(FacesContext context, Iterator compIterator)
  {
    List result = null;
    while (compIterator.hasNext())
    {
      UIComponent colChild = (UIComponent) compIterator.next();
      if (!colChild.isTransient())
      {
        if (result == null)
        {
          result = new ArrayList();
        }
        result.add(colChild.processSaveState(context));
      }
    }
    return result;
  }

  public void processRestoreRowState(FacesContext context, Object state)
  {
    List childrenStates = (List) state;
    int childCount = getChildCount();
    if (childCount > 0)
    {
      int idx = 0;
      for (Iterator it = getChildren().iterator(); it.hasNext();)
      {
        UIComponent child = (UIComponent) it.next();
        if (!child.isTransient())
        {
          Object childState = childrenStates.get(idx++);
          if (childState != null)
          {
            if (child instanceof UIColumn)
            {
              Object[] columnState = (Object[]) childState;
              setColumnChildsState(context, child.getChildren().iterator(), columnState[1]);
              child.restoreState(context, columnState[0]);
            }
            else
            {
              child.processRestoreState(context, childState);
            }
          }
          else
          {
            context.getExternalContext().log(
                "No state found to restore child of component " + getId());
          }
        }
      }
    }
  }

  private void setColumnChildsState(FacesContext context, Iterator compIterator, Object state)
  {
    List childrenList = (List) state;
    if (childrenList != null)
    {
      int idx = 0;
      while (compIterator.hasNext())
      {
        UIComponent child = (UIComponent) compIterator.next();
        if (!child.isTransient())
        {
          Object childState = childrenList.get(idx++);
          if (childState != null)
          {
            child.processRestoreState(context, childState);
          }
          else
          {
            context.getExternalContext().log(
                "No state found to restore child of component " + getId());
          }
        }
      }
    }
  }

  public void setValueBinding(String name, ValueBinding binding)
  {
    if (name == null)
    {
      throw new NullPointerException("name");
    }
    else if (name.equals("value"))
    {
      _dataModelMap.clear();
      _rowStates = null;
    }
    else if (name.equals("var") || name.equals("rowIndex"))
    {
      throw new IllegalArgumentException("name " + name);
    }
    super.setValueBinding(name, binding);
  }

  protected DataModel getDataModel()
  {
    String clientID = getParent().getClientId(getFacesContext());
    DataModel dataModel = (DataModel) _dataModelMap.get(clientID);
    if (dataModel == null)
    {
      dataModel = createDataModel();
      _dataModelMap.put(clientID, dataModel);
    }
    return dataModel;
  }

  protected void setDataModel(DataModel datamodel)
  {
    _dataModelMap.put(getParent().getClientId(getFacesContext()), datamodel);
  }

  /**
   * Creates a new DataModel around the current value.
   */
  protected DataModel createDataModel()
  {
    Object value = getValue();
    if (value == null)
    {
      return EMPTY_DATA_MODEL;
    }
    else if (value instanceof DataModel)
    {
      return (DataModel) value;
    }
    else if (value instanceof List)
    {
      return new ListDataModel((List) value);
    }
    else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
    {
      return new ArrayDataModel((Object[]) value);
    }
    else if (value instanceof ResultSet)
    {
      return new ResultSetDataModel((ResultSet) value);
    }
    else if (value instanceof Result)
    {
      return new ResultDataModel((Result) value);
    }
    else
    {
      return new ScalarDataModel(value);
    }
  }

  private static final DataModel EMPTY_DATA_MODEL = new DataModel()
  {
    public boolean isRowAvailable()
    {
      return false;
    }

    public int getRowCount()
    {
      return 0;
    }

    public Object getRowData()
    {
      throw new IllegalArgumentException();
    }

    public int getRowIndex()
    {
      return -1;
    }

    public void setRowIndex(int i)
    {
      if (i < -1)
        throw new IndexOutOfBoundsException("Index < 0 : " + i);
    }

    public Object getWrappedData()
    {
      return null;
    }

    public void setWrappedData(Object obj)
    {
      if (obj == null)
        return; //Clearing is allowed
      throw new UnsupportedOperationException(this.getClass().getName()
          + " UnsupportedOperationException");
    }
  };

  public Object saveState(FacesContext context)
  {
    Object values[] = new Object[2];
    values[0] = super.saveState(context);
    values[1] = Boolean.valueOf(_isValidChilds);

    return values;
  }

  public void restoreState(FacesContext context, Object state)
  {
    Object values[] = (Object[]) state;
    super.restoreState(context, values[0]);

    Boolean validChilds = (Boolean) values[1];
    if (validChilds != null)
    {
      _isValidChilds = validChilds.booleanValue();
    }
    else
    {
      _isValidChilds = true;
    }
  }

}

package org.apache.myfaces.component.html.ext;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
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
  protected DataModel _dataModel = null;
  protected HashMap _dataModelMap = null;

  // will be set to false if the data should not be refreshed at the beginning of the encode phase
  private boolean _isValidChilds = true;

  private Map _rowState = new HashMap();

  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // Every field and method from here is identical to UIData !!!!!!!!!

  private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

  private static final Integer INTEGER_MINUS1 = new Integer(-1);

  private int _rowIndex = -1;

  private Boolean _isEmbeddedUIData = null;
  private UIData _embeddingUIData = null;

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
   * @see javax.faces.component.UIData#processValidators(javax.faces.context.FacesContext)
   */
  public void processValidators(FacesContext context)
  {
    super.processValidators(context);
    // check if an validation error forces the render response for our data
    if (context.getRenderResponse())
    {
      _isValidChilds = false;
    }
  }

  /**
   * @see javax.faces.component.UIData#processUpdates(javax.faces.context.FacesContext)
   */
  public void processUpdates(FacesContext context)
  {
    super.processUpdates(context);
    // check if an validation error forces the render response for our data
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
    // TODO Auto-generated method stub
    if (_isValidChilds)
    {
      //Refresh DataModel for rendering:
      _dataModel = null;
      if (_dataModelMap != null)
        _dataModelMap.clear();
    }
    super.encodeBegin(context);
  }

  public void setRowIndex(int rowIndex)
  {
    if (_rowIndex == rowIndex)
    {
      return;
    }

    FacesContext context = getFacesContext();

    saveDescendantComponentStates(context, getFacetsAndChildren());

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

    restoreDescendantComponentStates(context, getFacetsAndChildren());
  }

  private void saveDescendantComponentStates(FacesContext context, Iterator childIterator)
  {
    while (childIterator.hasNext())
    {
      UIComponent child = (UIComponent) childIterator.next();
      if (!child.isTransient())
      {
        saveDescendantComponentStates(context, child.getFacetsAndChildren());
        _rowState.put(child.getClientId(context), new ChildStateHolder(context, child));
      }
    }
  }

  private void restoreDescendantComponentStates(FacesContext context, Iterator childIterator)
  {
    while (childIterator.hasNext())
    {
      UIComponent child = (UIComponent) childIterator.next();
      //reset clientId to null
      child.setId(child.getId());
      if (!child.isTransient())
      {
        String clientId = child.getClientId(context);
        ChildStateHolder childStateHolder = (ChildStateHolder) _rowState.get(clientId);
        if (childStateHolder != null)
        {
          childStateHolder.restoreState(context, child);
        }
        restoreDescendantComponentStates(context, child.getFacetsAndChildren());
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
      _dataModel = null;
    }
    else if (name.equals("var") || name.equals("rowIndex"))
    {
      throw new IllegalArgumentException("name " + name);
    }
    super.setValueBinding(name, binding);
  }

  private DataModel getDataModel()
  {
    UIData embeddingUIData = getEmbeddingUIData();
    if (embeddingUIData != null)
    {
      //This UIData is nested in another UIData, so we must not
      //do simple caching of the current DataModel. We must associate
      //the DataModel that we want to cache with the clientId of the
      //embedding UIData. This clientId will be different for every
      //row of the embedding UIData.
      if (_dataModelMap == null)
      {
        _dataModelMap = new HashMap();
      }
      String embeddingClientId = embeddingUIData.getClientId(FacesContext.getCurrentInstance());
      DataModel dataModel = (DataModel) _dataModelMap.get(embeddingClientId);
      if (dataModel == null)
      {
        dataModel = createDataModel();
        _dataModelMap.put(embeddingClientId, dataModel);
      }
      return dataModel;
    }
    else
    {
      //This UIData is not nested within another UIData. So there
      //is no need for the DataModel Map.
      if (_dataModel == null)
      {
        _dataModel = createDataModel();
      }
      return _dataModel;
    }
  }

  /**
   * Creates a new DataModel around the current value.
   */
  private DataModel createDataModel()
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

  /**
   * Looks for an embedding UIData component
   * @return the embedding UIData or null
   */
  private UIData getEmbeddingUIData()
  {
    if (_isEmbeddedUIData == null)
    {
      UIComponent findParentUIData = getParent();
      while (findParentUIData != null && !(findParentUIData instanceof UIData))
      {
        findParentUIData = findParentUIData.getParent();
      }
      if (findParentUIData != null)
      {
        _embeddingUIData = (UIData) findParentUIData;
        _isEmbeddedUIData = Boolean.TRUE;
      }
      else
      {
        _isEmbeddedUIData = Boolean.FALSE;
      }
    }

    if (_isEmbeddedUIData.booleanValue())
    {
      return _embeddingUIData;
    }
    else
    {
      return null;
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

  private static class ChildStateHolder implements Serializable
  {
    private final Object _state;

    public ChildStateHolder(FacesContext context, StateHolder stateHolder)
    {
      _state = stateHolder.saveState(context);
    }

    public void restoreState(FacesContext context, StateHolder stateHolder)
    {
      stateHolder.restoreState(context, _state);
    }
  }

  public Object saveState(FacesContext context)
  {
    Object values[] = new Object[2];
    values[0] = super.saveState(context);
//    if (!_isValidChilds)
//    {
//      values[1] = _rowState;
//    }
    return values;
  }

  public void restoreState(FacesContext context, Object state)
  {
    Object values[] = (Object[]) state;
    super.restoreState(context, values[0]);
//    Map rowState = (Map) values[1];
//    if (rowState != null)
//    {
//      _rowState = rowState;
//    }
  }

}

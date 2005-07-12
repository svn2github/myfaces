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

	// will be set to false if the data should not be refreshed at the beginning of the encode phase
	private boolean _isValidChilds = true;

	private Map _rowState = new HashMap();

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// Every field and method from here is identical to UIData !!!!!!!!!

	private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

	private int _rowIndex = -1;

	// will be set to false if the _rowstate and _datamodel should not be saved
	private boolean _processUIDataState = true;

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
			_dataModel = null;
		}
		super.encodeBegin(context);
	}

	/**
	 * @see javax.faces.component.UIData#encodeEnd(javax.faces.context.FacesContext)
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
				_rowState.put(child.getClientId(context), new ChildStateHolder(context, child));
				if (!(child instanceof UIData))
				{
					saveDescendantComponentStates(context, child.getFacetsAndChildren());
				}
			}
		}
	}

	private void restoreDescendantComponentStates(FacesContext context, Iterator childIterator)
	{
		while (childIterator.hasNext())
		{
			UIComponent child = (UIComponent) childIterator.next();
			// the spec 1.1 (3.1.6) says: setting the id will recalculate the clientid  
			child.setId(child.getId());
			if (!child.isTransient())
			{
				String clientId = child.getClientId(context);
				ChildStateHolder childStateHolder = (ChildStateHolder) _rowState.get(clientId);
				if (childStateHolder != null)
				{
					childStateHolder.restoreState(context, child);
				}
				if (!(child instanceof UIData))
				{
					restoreDescendantComponentStates(context, child.getFacetsAndChildren());
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
		if (_dataModel == null)
		{
			_dataModel = createDataModel();
		}
		return _dataModel;
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

	/**
	 * @see javax.faces.component.UIComponentBase#processSaveState(javax.faces.context.FacesContext)
	 */
	public Object processSaveState(FacesContext context)
	{
		// state of uidata will not be processed
		_processUIDataState = false;
		Object state = super.processSaveState(context);
		_processUIDataState = true;
		return state;
	}

	/**
	 * @see javax.faces.component.UIComponentBase#processRestoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void processRestoreState(FacesContext context, Object state)
	{
		_processUIDataState = false;
		super.processRestoreState(context, state);
		_processUIDataState = true;
	}

	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[4];
		values[0] = super.saveState(context);

		if (_processUIDataState)
		{
			values[1] = _rowState;
			values[2] = _dataModel;
			values[3] = Boolean.valueOf(_isValidChilds);
		}
		return values;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);

		if (_processUIDataState)
		{
			Map rowState = (Map) values[1];
			if (rowState != null)
			{
				_rowState = rowState;
			}
			else
			{
				_rowState.clear();
			}

			_dataModel = (DataModel) values[2];

			Boolean validChilds = (Boolean) values[3];
			if (validChilds != null)
			{
				_isValidChilds = validChilds.booleanValue();
			}
			else
			{
				// defaults to true
				_isValidChilds = true;
			}
		}
	}

}

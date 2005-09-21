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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
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
public abstract class HtmlDataTableHack extends
                javax.faces.component.html.HtmlDataTable
{
    private Map _dataModelMap = new HashMap();

    // will be set to false if the data should not be refreshed at the beginning of the encode phase
    private boolean _isValidChilds = true;

    // holds for each row the states of the child components of this UIData 
    private Map _rowStates = new HashMap();

    // contains the initial row state which is used to initialize each row
    private Object _initialDescendantComponentState = null;

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Every field and method from here is identical to UIData !!!!!!!!!
    // EXCEPTION: we can create a DataModel from a Collection

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
        setRowIndex(-1);
        if (_isValidChilds)
        {
            //Refresh DataModel for rendering:
            _dataModelMap.clear();
            _rowStates.clear();
            _initialDescendantComponentState = null;
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
        if (rowIndex < -1)
        {
            throw new IllegalArgumentException("rowIndex is less than -1");
        }

        if (_rowIndex == rowIndex)
        {
            return;
        }

        FacesContext facesContext = getFacesContext();

        if (_rowIndex == -1)
        {
            if (_initialDescendantComponentState == null)
            {
                _initialDescendantComponentState = saveDescendantComponentStates(getChildren()
                                .iterator(), false);
            }
        }
        else
        {
            _rowStates.put(getClientId(facesContext),
                            saveDescendantComponentStates(getChildren()
                                            .iterator(), false));
        }

        _rowIndex = rowIndex;

        DataModel dataModel = getDataModel();
        dataModel.setRowIndex(rowIndex);

        String var = getVar();
        if (rowIndex == -1)
        {
            if (var != null)
            {
                facesContext.getExternalContext().getRequestMap().remove(var);
            }
        }
        else
        {
            if (var != null)
            {
                if (isRowAvailable())
                {
                    Object rowData = dataModel.getRowData();
                    facesContext.getExternalContext().getRequestMap().put(var,
                                    rowData);
                }
                else
                {
                    facesContext.getExternalContext().getRequestMap().remove(
                                    var);
                }
            }
        }

        if (_rowIndex == -1)
        {
            restoreDescendantComponentStates(getChildren().iterator(),
                            _initialDescendantComponentState, false);
        }
        else
        {
            Object rowState = _rowStates.get(getClientId(facesContext));
            if (rowState == null)
            {
                restoreDescendantComponentStates(getChildren().iterator(),
                                _initialDescendantComponentState, false);
            }
            else
            {
                restoreDescendantComponentStates(getChildren().iterator(),
                                rowState, false);
            }
        }
    }

    private void restoreDescendantComponentStates(Iterator childIterator,
            Object state, boolean restoreChildFacets)
    {
        Iterator descendantStateIterator = null;
        while (childIterator.hasNext())
        {
            if (descendantStateIterator == null && state != null)
            {
                descendantStateIterator = ((Collection) state).iterator();
            }
            UIComponent component = (UIComponent) childIterator.next();
            // reset the client id (see spec 3.1.6)
            component.setId(component.getId());
            if(!component.isTransient())
            {
                Object childState = null;
                Object descendantState = null;
                if (descendantStateIterator != null
                        && descendantStateIterator.hasNext())
                {
                    Object[] object = (Object[]) descendantStateIterator.next();
                    childState = object[0];
                    descendantState = object[1];
                }
                if (component instanceof EditableValueHolder)
                {
                    ((EditableValueHolderState) childState)
                            .restoreState((EditableValueHolder) component);
                }
                Iterator childsIterator;
                if (restoreChildFacets)
                {
                    childsIterator = component.getFacetsAndChildren();
                }
                else
                {
                    childsIterator = component.getChildren().iterator();
                }
                restoreDescendantComponentStates(childsIterator, descendantState,
                        true);
            }
        }
    }

    private Object saveDescendantComponentStates(Iterator childIterator,
            boolean saveChildFacets)
    {
        Collection childStates = null;
        while (childIterator.hasNext())
        {
            if (childStates == null)
            {
                childStates = new ArrayList();
            }
            UIComponent child = (UIComponent) childIterator.next();
            if(!child.isTransient())
            {
                Iterator childsIterator;
                if (saveChildFacets)
                {
                    childsIterator = child.getFacetsAndChildren();
                }
                else
                {
                    childsIterator = child.getChildren().iterator();
                }
                Object descendantState = saveDescendantComponentStates(
                        childsIterator, true);
                Object state = null;
                if (child instanceof EditableValueHolder)
                {
                    state = new EditableValueHolderState(
                            (EditableValueHolder) child);
                }
                childStates.add(new Object[] { state, descendantState });
            }
        }
        return childStates;
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
        }
        else if (name.equals("var") || name.equals("rowIndex"))
        {
            throw new IllegalArgumentException("name " + name);
        }
        super.setValueBinding(name, binding);
    }
    
    /**
     * @see javax.faces.component.UIData#setValue(java.lang.Object)
     */
    public void setValue(Object value)
    {
        super.setValue(value);
        _dataModelMap.clear();
        _rowStates.clear();
        _isValidChilds = true;
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
        _dataModelMap.put(getParent().getClientId(getFacesContext()),
                                        datamodel);
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
        // accept a Collection is not supported in the Spec
        else if (value instanceof Collection)
        {
            return new ListDataModel(new ArrayList((Collection) value));
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

    private static final DataModel EMPTY_DATA_MODEL = new _SerializableDataModel()
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

    private class EditableValueHolderState
    {
        private final Object _value;
        private final boolean _localValueSet;
        private final boolean _valid;
        private final Object _submittedValue;

        public EditableValueHolderState(EditableValueHolder evh)
        {
            _value = evh.getLocalValue();
            _localValueSet = evh.isLocalValueSet();
            _valid = evh.isValid();
            _submittedValue = evh.getSubmittedValue();
        }

        public void restoreState(EditableValueHolder evh)
        {
            evh.setValue(_value);
            evh.setLocalValueSet(_localValueSet);
            evh.setValid(_valid);
            evh.setSubmittedValue(_submittedValue);
        }
    }
}

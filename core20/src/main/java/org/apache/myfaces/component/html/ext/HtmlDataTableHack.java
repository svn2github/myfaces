/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.component.html.util.HtmlComponentUtils;
import org.apache.myfaces.custom.ExtendedComponentBase;

/**
 * Reimplement all UIData functionality to be able to have (protected) access
 * the internal DataModel.
 *
 * @JSFComponent
 *  configExcluded = "true"
 *
 * @author Manfred Geiler (latest modification by $Author: lu4242 $)
 * @version $Revision: 691856 $ $Date: 2008-09-03 21:40:30 -0500 (mié, 03 sep 2008) $
 */
public abstract class HtmlDataTableHack extends
                javax.faces.component.html.HtmlDataTable implements
                ExtendedComponentBase
                
{
    @SuppressWarnings("unchecked")
    private Map<String, DataModel> _dataModelMap = new HashMap<String, DataModel>();

    // will be set to false if the data should not be refreshed at the beginning of the encode phase
    private boolean _isValidChilds = true;

    // holds for each row the states of the child components of this UIData
    private Map<String, Object> _rowStates = new HashMap<String, Object>();

    // contains the initial row state which is used to initialize each row
    private Object _initialDescendantComponentState = null;

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Every field and method from here is identical to UIData !!!!!!!!!
    // EXCEPTION: we can create a DataModel from a Collection

    @SuppressWarnings("unchecked")
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    private static final boolean DEFAULT_PRESERVEROWSTATES = false;

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
     * Hack since RI does not call getRowIndex()
     */
    public String getClientId(FacesContext context)
    {
        // check for forceId
        String clientId = HtmlComponentUtils.getClientId(this, getRenderer(context), context);
        if (clientId == null)
        {
            clientId = super.getClientId(context);
            // remove index if necesssary 
            int rowIndex = getRowIndex();
            if (rowIndex != -1)
            {
                char separator = UINamingContainer.getSeparatorChar(context);
                int index = clientId.lastIndexOf(separator);
                if(index != -1)
                {
                    String rowIndexString = clientId.substring(index + 1);
                    if (rowIndexString.length() > 0 && 
                        StringUtils.isNumeric(rowIndexString) &&
                        rowIndex == Integer.valueOf(rowIndexString).intValue())
                    {
                        clientId = clientId.substring(0,index - 1);
                    }
                }
            }
        }
        return clientId;
    }
    
    @Override
    public String getContainerClientId(FacesContext context)
    {
        // check for forceId
        String clientId = HtmlComponentUtils.getClientId(this, getRenderer(context), context);
        if (clientId == null)
        {
            clientId = super.getClientId(context);
        }
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
        {
            return clientId;
        }
        // the following code tries to avoid rowindex to be twice in the client id
        char separator = UINamingContainer.getSeparatorChar(context);
        int index = clientId.lastIndexOf(separator);
        if(index != -1)
        {
            String rowIndexString = clientId.substring(index + 1);
            if(rowIndexString.length() > 0 && 
               StringUtils.isNumeric(rowIndexString) &&
               Integer.valueOf(rowIndexString) == rowIndex)
            {
                return clientId;
            }
        }
        return clientId + separator + rowIndex;
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
     * This method is used when a custom processUpdates and processValidators
     * is defined, to check if a update model error forces the render 
     * response for our data, because _isValidChilds is a private field
     * and is not available on child components that inherits this 
     * component class like t:dataList. (TOMAHAWK-1225)
     */
    protected void checkUpdateModelError(FacesContext context)
    {
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
        _initialDescendantComponentState = null;
        if (_isValidChilds && !hasErrorMessages(context))
        {
            //Refresh DataModel for rendering:
            _dataModelMap.clear();
            if (!isPreserveRowStates())
            {
                _rowStates.clear();
            }
        }
        super.encodeBegin(context);
    }

    public void setPreserveRowStates(boolean preserveRowStates)
    {
        getStateHelper().put(PropertyKeys.preserveRowStates, preserveRowStates);
    }

    /**
     * Indicates whether the state for each row should not be 
     * discarded before the datatable is rendered again. 
     * 
     * Setting this to true might be hepful if an input 
     * component inside the datatable has no valuebinding and 
     * the value entered in there should be displayed again.
     *  
     * This will only work reliable if the datamodel of the 
     * datatable did not change either by sorting, removing or 
     * adding rows. Default: false
     * 
     * @JSFProperty
     *   defaultValue="false"
     */
    public boolean isPreserveRowStates()
    {
        Object value = getStateHelper().eval(PropertyKeys.preserveRowStates,DEFAULT_PRESERVEROWSTATES);
        if (value != null)
        {
            return (Boolean) value;
        }
        return DEFAULT_PRESERVEROWSTATES;
    }

    protected boolean hasErrorMessages(FacesContext context)
    {
        for(Iterator<FacesMessage> iter = context.getMessages(); iter.hasNext();)
        {
            FacesMessage message = (FacesMessage) iter.next();
            if(FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @see javax.faces.component.UIComponentBase#encodeEnd(javax.faces.context.FacesContext)
     */
    public void encodeEnd(FacesContext context) throws IOException
    {
        setRowIndex(-1);
        super.encodeEnd(context);
    }

    @SuppressWarnings("unchecked")
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
            _rowStates.put(getContainerClientId(facesContext),
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
            Object rowState = _rowStates.get(getContainerClientId(facesContext));
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

    @SuppressWarnings("unchecked")
    protected void restoreDescendantComponentStates(Iterator<UIComponent> childIterator,
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
                if (childState != null && component instanceof EditableValueHolder)
                {
                    ((EditableValueHolderState) childState)
                            .restoreState((EditableValueHolder) component);
                }
                Iterator<UIComponent> childsIterator;
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

    @SuppressWarnings("unchecked")
    protected Object saveDescendantComponentStates(Iterator<UIComponent> childIterator,
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
                Iterator<UIComponent> childsIterator;
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
            throw new IllegalArgumentException(
                    "You can never set the 'rowIndex' or the 'var' attribute as a value-binding. Set the property directly instead. Name " + name);
        }
        super.setValueBinding(name, binding);
    }
    
    public void setValueExpression(String name, ValueExpression binding)
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
            throw new IllegalArgumentException(
                    "You can never set the 'rowIndex' or the 'var' attribute as a value-binding. Set the property directly instead. Name " + name);
        }
        super.setValueExpression(name, binding);
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

    @SuppressWarnings("unchecked")
    protected DataModel getDataModel()
    {
        DataModel dataModel = null;
        String clientID = "";
        
        UIComponent parent = getParent();
        if (parent != null) 
        {
            clientID = parent.getContainerClientId(getFacesContext());
        }
        dataModel = (DataModel) _dataModelMap.get(clientID);
        if (dataModel == null)
        {
            dataModel = createDataModel();
            _dataModelMap.put(clientID, dataModel);
        }               
        
        return dataModel;
    }

    @SuppressWarnings("unchecked")
    protected void setDataModel(DataModel datamodel)
    {
        UIComponent parent = getParent();
        String clientID = "";
        if(parent != null)
        {
            clientID = parent.getContainerClientId(getFacesContext());
        }
        _dataModelMap.put(clientID, datamodel);
    }

    /**
     * Creates a new DataModel around the current value.
     */
    @SuppressWarnings("unchecked")
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
    
    @SuppressWarnings("unchecked")
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
    
    // Property: forceId
    
    /**
     * If true, this component will force the use of the specified id when rendering.
     * 
     * @JSFProperty
     *   literalOnly = "true"
     *   defaultValue = "false"
     *   
     * @return
     */
    public boolean isForceId()
    {
        Object value = getStateHelper().get(PropertyKeys.forceId);
        if (value != null)
        {
            return (Boolean) value;        
        }
        return false;  
    }

    public void setForceId(boolean forceId)
    {
        getStateHelper().put(PropertyKeys.forceId, forceId );
    }
    // Property: forceIdIndex
    
    /**
     * If false, this component will not append a '[n]' suffix 
     * (where 'n' is the row index) to components that are 
     * contained within a "list." This value will be true by 
     * default and the value will be ignored if the value of 
     * forceId is false (or not specified.)
     * 
     * @JSFProperty
     *   literalOnly = "true"
     *   defaultValue = "true"
     *   
     * @return
     */
    public boolean isForceIdIndex()
    {
        Object value = getStateHelper().get(PropertyKeys.forceIdIndex);
        if (value != null)
        {
            return (Boolean) value;        
        }
        return true;
    }

    public void setForceIdIndex(boolean forceIdIndex)
    {
        getStateHelper().put(PropertyKeys.forceIdIndex, forceIdIndex );
    }
    
    /**
     * Remove all preserved row state for the dataTable
     */
    public void clearRowStates()
    {
        _rowStates.clear();
    }
    
    /**
     * Remove preserved row state for deleted row and adjust row state to reflect deleted row.
     * @param deletedIndex index of row to delete
     */
    public void deleteRowStateForRow(int deletedIndex)
    {
        // save row index
        int savedRowIndex = getRowIndex();
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
         setRowIndex(deletedIndex);
        String currentRowStateKey = getContainerClientId(facesContext);

        // copy next rowstate to current row for each row from deleted row onward.
        int rowCount = getRowCount();
        for (int index = deletedIndex + 1; index < rowCount; ++index)
        {
            setRowIndex(index);
            String nextRowStateKey = getContainerClientId(facesContext);

            Object nextRowState = _rowStates.get(nextRowStateKey);
            if (nextRowState == null)
            {
                _rowStates.remove(currentRowStateKey);
            }
            else
            {
                _rowStates.put(currentRowStateKey, nextRowState);
            }
            currentRowStateKey = nextRowStateKey;
        }

        // Remove last row
        _rowStates.remove(currentRowStateKey);

        // restore saved row index
        setRowIndex(savedRowIndex);
    }
    
    protected enum PropertyKeys
    {
        preserveRowStates
        , forceId
        , forceIdIndex
    }
}

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

import java.io.StringReader;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.jstl.sql.Result;
import org.apache.commons.el.Expression;
import org.apache.commons.el.ExpressionString;
import org.apache.commons.el.Logger;
import org.apache.commons.el.parser.ELParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public final class SortableModel extends DataModel implements VariableResolver
{    
    private static final Log log = LogFactory.getLog(SortableModel.class);
    public static final Logger LOGGER = new Logger(System.out);
    
    private static final String ROW_OBJECT_NAME = "rowObjectGet";
    
    private SortCriterion _sortCriterion = null;
    
    private DataModel _model = null;
    private Object _wrappedData = null;
    
    private IntList _sortedIndicesList = null,  // from baseIndex to sortedIndex
                    _baseIndicesList = null;    // from sortedIndex to baseIndex
    
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();
    
    protected static FunctionMapper s_functionMapper = new FunctionMapper()
    {
        public Method resolveFunction(String prefix, String localName)
        {
            throw new ReferenceSyntaxException(
                "Functions not supported in expressions. Function: "
                + prefix + ":" + localName);
        }
    };
    
    /**
     * Create a new SortableModel from the given instance.
     * @param model This will be converted into a {@link DataModel}
     * @see #setWrappedData
     */
    public SortableModel(Object model) 
    {
        setWrappedData(model);
    }
    
    /**
     * No arg constructor for use as a managed-bean.
     * Must call setWrappedData before using this instance.
     */
    public SortableModel(){}
    
    public Object getRowData() 
    {
        return _model.getRowData();
    }
    
    public Object getWrappedData() 
    {
        return _wrappedData;
    }
    
    public boolean isRowAvailable() 
    {
        return _model.isRowAvailable();
    }
    
    /**
     * Sets the underlying data being managed by this instance.
     * @param data This Object will be converted into a
     * {@link DataModel}.
     */
    public void setWrappedData(Object data) 
    {
        _baseIndicesList = null;
        _model = toDataModel(data);
        _sortCriterion = null;
        _sortedIndicesList = null;
        _wrappedData = data;
    }
    
    protected DataModel toDataModel(Object data) 
    {
        if (data == null)
        {
            return EMPTY_DATA_MODEL;
        }
        else if (data instanceof DataModel)
        {
            return (DataModel) data;
        }
        else if (data instanceof List)
        {
            return new ListDataModel((List) data);
        }
        // accept a Collection is not supported in the Spec
        else if (data instanceof Collection)
        {
            return new ListDataModel(new ArrayList((Collection) data));
        }
        else if (OBJECT_ARRAY_CLASS.isAssignableFrom(data.getClass()))
        {
            return new ArrayDataModel((Object[]) data);
        }
        else if (data instanceof ResultSet)
        {
            return new ResultSetDataModel((ResultSet) data);
        }
        else if (data instanceof Result)
        {
            return new ResultDataModel((Result) data);
        }
        else
        {
            return new ScalarDataModel(data);
        }
    }
    
    public int getRowCount() 
    {
        return _model.getRowCount();
    }
    
    public void setRowIndex(int rowIndex) 
    {
        int baseIndex = _toBaseIndex(rowIndex);
        _model.setRowIndex(baseIndex);
    }
    
    public int getRowIndex() 
    {
        int baseIndex = _model.getRowIndex();
        return _toSortedIndex(baseIndex);
    }       
    
    /**
     * Checks to see if the underlying collection is sortable by the given property.
     * @param property The name of the property to sort the underlying collection by.
     * @return true, if the property implements java.lang.Comparable
     */
    public boolean isSortable(String property) 
    {
        final int oldIndex = _model.getRowIndex();
        try 
        {
            _model.setRowIndex(0);
            if (!_model.isRowAvailable())
                return false; // if there is no data in the table then nothing is sortable
            
            try 
            {   
                Object propertyValue = getPropertyValue(property);
                
                // when the value is null, we don't know if we can sort it.
                // by default let's support sorting of null values, and let the user
                // turn off sorting if necessary:
                return (propertyValue instanceof Comparable) || (propertyValue == null);
            } 
            catch (RuntimeException e) 
            {
                // don't propagate this exception out. This is because it might break
                // the VE.

                log.warn(e);
                return false;
            }
        } 
        finally 
        {
            _model.setRowIndex(oldIndex);
        }
    }
    
    public List getSortCriteria() 
    {
        return (_sortCriterion == null) ? Collections.EMPTY_LIST : Collections.singletonList(_sortCriterion);
    }
    
    public void setSortCriteria(List criteria) 
    {
        if ((criteria == null) || (criteria.isEmpty())) 
        {
            _sortCriterion = null;
            // restore unsorted order:
            _baseIndicesList = _sortedIndicesList = null;
        } 
        else 
        {
            SortCriterion sc = (SortCriterion) criteria.get(0);
            if ((_sortCriterion == null) || (!_sortCriterion.equals(sc))) 
            {
                _sortCriterion = sc;
                _sort(_sortCriterion.getProperty(), _sortCriterion.isAscending());
            }
        }
    }
    
    public String toString() 
    {
        return "SortableModel[" + _model + "]";
    }
    
    /**
     * Sorts the underlying collection by the given property, in the
     * given direction.
     * @param property The name of the property to sort by. The value of this
     * property must implement java.lang.Comparable.
     * @param isAscending true if the collection is to be sorted in
     * ascending order.
     * @todo support -1 for rowCount
     */
    private void _sort(String property, boolean isAscending) 
    {
        //TODO: support -1 for rowCount:
        int sz = getRowCount();
        if ((_baseIndicesList == null) || (_baseIndicesList.size() != sz)) 
        {
            // we do not want to mutate the original data.
            // however, instead of copying the data and sorting the copy,
            // we will create a list of indices into the original data, and
            // sort the indices. This way, when certain rows are made current
            // in this Collection, we can make them current in the underlying
            // DataModel as well.            
            _baseIndicesList = new IntList(sz);
        }
        
        final int rowIndex = _model.getRowIndex();
        
        _model.setRowIndex(0);
        // Make sure the model has that row 0! (It could be empty.)
        if (_model.isRowAvailable()) 
        {            
            Comparator comp = new Comp(property);                       
            
            if (!isAscending)
                comp = new Inverter(comp);
            
            Collections.sort(_baseIndicesList, comp);
            _sortedIndicesList = null;
        }
        
        _model.setRowIndex(rowIndex);
    }
    
    private int _toSortedIndex(int baseIndex) 
    {
        if ((_sortedIndicesList == null) && (_baseIndicesList != null)) 
        {
            _sortedIndicesList = (IntList) _baseIndicesList.clone();
            for(int i=0; i<_baseIndicesList.size(); i++) 
            {
                Integer base = (Integer) _baseIndicesList.get(i);
                _sortedIndicesList.set(base.intValue(), new Integer(i));
            }
        }
        
        return _convertIndex(baseIndex, _sortedIndicesList);
    }
    
    private int _toBaseIndex(int sortedIndex) 
    {
        return _convertIndex(sortedIndex, _baseIndicesList);
    }
    
    private int _convertIndex(int index, List indices) 
    {
        if (index < 0) // -1 is special
            return index;
        
        if ((indices != null) && (indices.size() > index)) 
        {
            index = ((Integer) indices.get(index)).intValue();
        }
        return index;
    }       
    
    protected Object getPropertyValue(String property)
    {
        String expressionString = "${"+ROW_OBJECT_NAME+"."+property+"}";
        
        ELParser parser = new ELParser(new StringReader(expressionString));
        try
        {
            Object expression = parser.ExpressionString();
            if (!(expression instanceof Expression) && !(expression instanceof ExpressionString))
                return null;

            Object value = expression instanceof Expression
                                ? ((Expression) expression).evaluate(this, s_functionMapper, LOGGER)
                                : ((ExpressionString) expression).evaluate(this, s_functionMapper, LOGGER);
            return value;
        }
        catch (Exception exc)
        {            
            log.error("Evaluate:", exc);
        }
        
        return null;
    }
    
    public Object resolveVariable(String pName) throws ELException
    {
        if (ROW_OBJECT_NAME.equals(pName))
            return _model.getRowData();

        FacesContext context = FacesContext.getCurrentInstance();        
        return context.getApplication().getVariableResolver().resolveVariable(context, pName);
    }       
    
    private static final class IntList extends ArrayList implements Cloneable 
    {
        public IntList(int size) 
        {
            super(size);
            _expandToSize(size);
        }
        
        private void _expandToSize(int desiredSize) 
        {
            for(int i=0; i<desiredSize; i++) 
                add(new Integer(i));            
        }
    }
    
    private final class Comp implements Comparator 
    {
        private final String _prop;
        
        public Comp(String property) 
        {            
            _prop = property;
        }
        
        public int compare(Object o1, Object o2) 
        {
            int index1 = ((Integer) o1).intValue();
            int index2 = ((Integer) o2).intValue();
            
            _model.setRowIndex(index1);
            Object value1 = getPropertyValue(_prop);
            
            _model.setRowIndex(index2);
            Object value2 = getPropertyValue(_prop);
            
            if (value1 == null)
                return (value2 == null) ? 0 : -1;
            
            if (value2 == null)
                return 1;
            
            // ?? Sometimes, isSortable returns true
            // even if the underlying object is not a Comparable.
            // This happens if the object at rowIndex zero is null.
            // So test before we cast:
            if (value1 instanceof Comparable) 
            {
                return ((Comparable) value1).compareTo(value2);
            } 
            else 
            {
                // if the object is not a Comparable, then
                // the best we can do is string comparison:
                return value1.toString().compareTo(value2.toString());
            }
        }               
    }
    /**
     *
     */
    private static final class Inverter implements Comparator 
    {
        private final Comparator _comp;
        
        public Inverter(Comparator comp) 
        {
            _comp = comp;
        }
        
        public int compare(Object o1, Object o2) 
        {
            return _comp.compare(o2, o1);
        }              
    }      
    /**
     *
     */
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
}

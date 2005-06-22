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

import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
class _SerializableDataModel
        extends DataModel
        implements Serializable
{
    //private static final Log log = LogFactory.getLog(_SerializableDataModel.class);

    protected int _first;
    protected int _rows;
    protected int _rowCount;
    protected List _list;
    private transient int _rowIndex = -1;

    public _SerializableDataModel(int first, int rows, DataModel dataModel)
    {
        _first = first;
        _rows = rows;
        _rowCount = dataModel.getRowCount();
        if (_rows <= 0)
        {
            _rows = _rowCount - first;
        }
        _list = new ArrayList(rows);
        for (int i = 0; i < _rows; i++)
        {
            dataModel.setRowIndex(_first + i);
            if (!dataModel.isRowAvailable()) break;
            _list.add(dataModel.getRowData());
        }
        _rowIndex = -1;

        DataModelListener[] dataModelListeners = dataModel.getDataModelListeners();
        for (int i = 0; i < dataModelListeners.length; i++)
        {
            DataModelListener dataModelListener = dataModelListeners[i];
            addDataModelListener(dataModelListener);
        }
    }

    protected _SerializableDataModel()
    {
    }

    public int getFirst()
    {
        return _first;
    }

    public void setFirst(int first)
    {
        _first = first;
    }

    public int getRows()
    {
        return _rows;
    }

    public void setRows(int rows)
    {
        _rows = rows;
    }

    public boolean isRowAvailable()
    {
        return _rowIndex >= _first &&
            _rowIndex < _first + _rows &&
            _rowIndex < _rowCount &&
            _list.size() > _rowIndex - _first;
    }

    public int getRowCount()
    {
        return _rowCount;
    }

    public Object getRowData()
    {
        if (!isRowAvailable())
        {
            throw new IllegalStateException();
        }
        return _list.get(_rowIndex - _first);
    }

    public int getRowIndex()
    {
        return _rowIndex;
    }

    public void setRowIndex(int rowIndex)
    {
        if (rowIndex < -1)
        {
            throw new IllegalArgumentException();
        }

        int oldRowIndex = _rowIndex;
        _rowIndex = rowIndex;
        if (oldRowIndex != _rowIndex)
        {
            Object data = isRowAvailable() ? getRowData() : null;
            DataModelEvent event = new DataModelEvent(this, _rowIndex, data);
            DataModelListener[] listeners = getDataModelListeners();
            for (int i = 0; i < listeners.length; i++)
            {
                listeners[i].rowSelected(event);
            }
        }
    }

    public Object getWrappedData()
    {
        return _list;
    }

    public void setWrappedData(Object obj)
    {
        if (obj != null)
        {
            throw new IllegalArgumentException("Cannot set wrapped data of _SerializableDataModel");
        }
    }



    /*
    // StateHolder interface

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = new Integer(_first);
        values[1] = new Integer(_rows);
        values[2] = new Integer(_rowCount);
        values[3] = _list;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        _first    = ((Integer)values[0]).intValue();
        _rows     = ((Integer)values[1]).intValue();
        _rowCount = ((Integer)values[2]).intValue();
        _list     = (List)values[3];
    }

    public boolean isTransient()
    {
        return false;
    }

    public void setTransient(boolean newTransientValue)
    {
        throw new UnsupportedOperationException();
    }
    */
}

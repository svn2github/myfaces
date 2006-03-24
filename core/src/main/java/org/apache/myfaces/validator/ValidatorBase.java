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
package org.apache.myfaces.validator;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;

import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 * @author mkienenb (latest modification by $Author$)
 * @version $Revision$
 */
public abstract class ValidatorBase implements Validator, StateHolder {

    // the message to display when validation fails.
    protected String _message;

    //JSF-Field for StateHolder-IF
    protected boolean _transient = false;

    public String getMessage()
    {
        if (_message != null) return _message;
        ValueBinding vb = getValueBinding("message");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setMessage(String message)
    {
        this._message = message;
    }

    // -------------------------------------------------------- StateholderIF

    public Object saveState(FacesContext context) {
        Object value[] = new Object[1];
        value[0] = _message;
        return value;
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
         _message = (String) values[0];
    }

    public boolean isTransient() {
        return _transient;
    }

    public void setTransient(boolean newTransientValue) {
        _transient = newTransientValue;
    }

    // --------------------- borrowed from UIComponentBase ------------

    private Map _valueBindingMap = null;

    public ValueBinding getValueBinding(String name)
    {
        if (name == null) throw new NullPointerException("name");
        if (_valueBindingMap == null)
        {
            return null;
        }
        else
        {
            return (ValueBinding)_valueBindingMap.get(name);
        }
    }

    public void setValueBinding(String name,
                                ValueBinding binding)
    {
        if (name == null) throw new NullPointerException("name");
        if (_valueBindingMap == null)
        {
            _valueBindingMap = new HashMap();
        }
        _valueBindingMap.put(name, binding);
    }

    protected FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

}

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
package org.apache.myfaces.custom.regexprvalidator;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.shared_tomahawk.util.MessageUtils;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

import org.apache.commons.validator.GenericValidator;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class RegExprValidator implements Validator, StateHolder {
	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "org.apache.myfaces.validator.RegExpr";

	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the regex check fails.</p>
	 */
	public static final String REGEXPR_MESSAGE_ID = "org.apache.myfaces.Regexpr.INVALID";

	public RegExprValidator(){
	}

	//the pattern on which the validation is based.
    protected String _pattern= null;

    // the message to display when validation fails.
    protected String _message;

	//JSF-Field for StateHolder-IF
    protected boolean _transient = false;



	public void validate(
		FacesContext facesContext,
		UIComponent uiComponent,
		Object value)
		throws ValidatorException {

			if (facesContext == null) throw new NullPointerException("facesContext");
			if (uiComponent == null) throw new NullPointerException("uiComponent");

			if (value == null)
			{
				return;
		}
		Object[] args = {value.toString()};
		if(!GenericValidator.matchRegexp(value.toString(),"^" + getPattern() + "$")){

            String message = getMessage();
            if (null == message)  message = REGEXPR_MESSAGE_ID;

            throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR, message, args));
            }

	}



	// -------------------------------------------------------- StateholderIF

	public Object saveState(FacesContext context) {
		Object value[] = new Object[2];
        value[0] = _pattern;
        value[1] = _message;
		return value;
	}

	public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        _message = (String) values[1];
        _pattern = (String) values[0];
	}

	public boolean isTransient() {
		return _transient;
	}

	public void setTransient(boolean newTransientValue) {
		_transient = newTransientValue;
	}
	// -------------------------------------------------------- GETTER & SETTER

	/**
	 * @return the pattern, on which a value should be validated
	 */
    public String getPattern()
    {
        if (_pattern != null) return _pattern;
        ValueBinding vb = getValueBinding("pattern");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

	/**
	 * @param string the pattern, on which a value should be validated
	 */
	public void setPattern(String string) {
		_pattern = string;
	}

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

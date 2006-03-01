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
package org.apache.myfaces.custom.equalvalidator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.shared_tomahawk.util.MessageUtils;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class EqualValidator implements Validator, StateHolder {

	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "org.apache.myfaces.validator.Equal";

	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the equal_for check fails.</p>
	 */
	public static final String EQUAL_MESSAGE_ID = "org.apache.myfaces.Equal.INVALID";

	public EqualValidator(){
	}

	//the foreign component_id on which the validation is based.
	private String _for= null;


	//JSF-Field for StateHolder-IF
	private boolean _transient = false;


	// -------------------------------------------------------- ValidatorIF
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

        UIComponent foreignComp = uiComponent.getParent().findComponent(_for);
        if(foreignComp==null)
            throw new FacesException("Unable to find component '" + _for + "' (calling findComponent on component '" + uiComponent.getId() + "')");
        if(false == foreignComp instanceof EditableValueHolder)
            throw new FacesException("Component '" + foreignComp.getId() + "' does not implement EditableValueHolder");
        EditableValueHolder foreignEditableValueHolder = (EditableValueHolder) foreignComp;

        if (foreignEditableValueHolder.isRequired() && foreignEditableValueHolder.getValue()== null ) {
            return;
        }

		Object[] args = {value.toString(),(foreignEditableValueHolder.getValue()==null) ? foreignComp.getId():foreignEditableValueHolder.getValue().toString()};

		if(foreignEditableValueHolder.getValue()==null || !foreignEditableValueHolder.getValue().toString().equals(value.toString())  )
			throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,EQUAL_MESSAGE_ID, args));

	}
	// -------------------------------------------------------- StateholderIF

	public Object saveState(FacesContext context) {
		Object state = _for;
		return state;
	}

	public void restoreState(FacesContext context, Object state) {
		_for = (String) state;
	}

	public boolean isTransient() {
		return _transient;
	}

	public void setTransient(boolean newTransientValue) {
		_transient = newTransientValue;
	}
	// -------------------------------------------------------- GETTER & SETTER

	/**
	 * @return the foreign component_id, on which a value should be validated
	 */
	public String getFor() {
		return _for;
	}

	/**
	 * @param string the foreign component_id, on which a value should be validated
	 */
	public void setFor(String string) {
		_for = string;
	}

}
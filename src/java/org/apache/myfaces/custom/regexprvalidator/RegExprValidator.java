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

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.util.MessageUtils;

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
	 * the creditcard check fails.</p>
	 */
	public static final String REGEXPR_MESSAGE_ID = "org.apache.myfaces.Regexpr.INVALID";

	public RegExprValidator(){
	}

	//the pattern on which the validation is based.
	private String _pattern= null;


	//JSF-Field for StateHolder-IF
	private boolean _transient = false;



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
		if(!GenericValidator.matchRegexp(value.toString(),"^"+_pattern+"$")){
			throw new ValidatorException(MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,REGEXPR_MESSAGE_ID, args));

		}

	}



	// -------------------------------------------------------- StateholderIF

	public Object saveState(FacesContext context) {
		Object state = _pattern;
		return state;
	}

	public void restoreState(FacesContext context, Object state) {
		_pattern = (String) state;
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
	public String getPattern() {
		return _pattern;
	}

	/**
	 * @param string the pattern, on which a value should be validated
	 */
	public void setPattern(String string) {
		_pattern = string;
	}

}

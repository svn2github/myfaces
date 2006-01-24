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
package org.apache.myfaces.custom.comparetovalidator;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * @author Mike Kienenberger (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValidateCompareToTag extends ValidatorTag {
    
	/**
     * serial version id for correct serialisation versioning
     */
    private static final long serialVersionUID = 1L;
    
    private String _foreignComponentName = null;
	protected String _operator = null;
	protected String _comparator = null;
	protected String _message = null;
	protected String _alternateOperatorName = null;

	public ValidateCompareToTag(){
	}

	public void setFor(String string) {
		_foreignComponentName = string;
	}

    public void setAlternateOperatorName(String alternateOperatorName)
    {
        this._alternateOperatorName = alternateOperatorName;
    }

    public void setComparator(String comparator)
    {
        this._comparator = comparator;
    }

    public void setMessage(String message)
    {
        this._message = message;
    }

    public void setOperator(String operator)
    {
        this._operator = operator;
    }

	protected Validator createValidator() throws JspException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		setValidatorId(CompareToValidator.VALIDATOR_ID);
		CompareToValidator validator = (CompareToValidator)super.createValidator();


        if (_comparator != null)
		{
			ValueBinding vb = facesContext.getApplication().createValueBinding(_comparator);
			validator.setComparator(vb.getValue(facesContext).toString());
		}
        
        if (_foreignComponentName != null)
		{
			if (UIComponentTag.isValueReference(_foreignComponentName))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_foreignComponentName);
				validator.setFor(new String(vb.getValue(facesContext).toString()));
			}
			else
			{
				validator.setFor(_foreignComponentName);
			}
		}

        if (_operator != null)
		{
			if (UIComponentTag.isValueReference(_operator))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_operator);
				validator.setOperator(new String(vb.getValue(facesContext).toString()));
			}
			else
			{
				validator.setOperator(_operator);
			}
		}

        if (_message != null)
		{
			if (UIComponentTag.isValueReference(_message))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_message);
				validator.setMessage(new String(vb.getValue(facesContext).toString()));
			}
			else
			{
				validator.setMessage(_message);
			}
		}

        if (_alternateOperatorName != null)
		{
			if (UIComponentTag.isValueReference(_alternateOperatorName))
			{
				ValueBinding vb = facesContext.getApplication().createValueBinding(_alternateOperatorName);
				validator.setAlternateOperatorName(new String(vb.getValue(facesContext).toString()));
			}
			else
			{
				validator.setAlternateOperatorName(_alternateOperatorName);
			}
		}

		return validator;
	}
	
    public void release() {
        super.release();
        _foreignComponentName = null;
        _operator = null;
        _comparator = null;
        _message = null;
        _alternateOperatorName = null;
    }
}

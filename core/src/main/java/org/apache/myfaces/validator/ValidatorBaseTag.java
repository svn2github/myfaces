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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * ValidatorBaseTag provides support for ValidatorBase subclasses.
 * ValidatorBaseTag subclass tld entries should include the following to pick up attribute defintions.
 *         &ext_validator_base_attributes;
 * 
 * @author mkienenb (latest modification by $Author$)
 * @version $Revision$
 */
public class ValidatorBaseTag extends ValidatorTag {
    private static final long serialVersionUID = 4416508071412794682L;
    private String _message = null;

    public void setMessage(String string) {
        _message = string;
    }

    protected Validator createValidator() throws JspException {

        ValidatorBase validator = (ValidatorBase)super.createValidator();

        FacesContext facesContext = FacesContext.getCurrentInstance();

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

        return validator;
    }

    public void release()
    {
        super.release();
        _message= null;
    }
}

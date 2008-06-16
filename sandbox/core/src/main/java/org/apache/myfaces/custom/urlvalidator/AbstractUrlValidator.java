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

package org.apache.myfaces.custom.urlvalidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.validator.ValidatorBase;

/**
 * A custom validator for url format, based upons Jakarta Commons.
 * 
 * @JSFValidator
 *   name = "s:validateUrl"
 *   class = "org.apache.myfaces.custom.urlvalidator.UrlValidator"
 *   tagClass = "org.apache.myfaces.custom.urlvalidator.ValidateUrlTag"
 *   serialuidtag = "6041422002721046221L"
 *
 * @author Fabian Frederick
 *
 * @version $Revision$ $Date$
 */
public abstract class AbstractUrlValidator extends ValidatorBase {

	/**
	 * <p>The standard converter id for this converter.</p>
	 */
	public static final String 	VALIDATOR_ID 	   = "org.apache.myfaces.validator.Url";
	/**
	 * <p>The message identifier of the {@link FacesMessage} to be created if
	 * the maximum length check fails.</p>
	 */
	public static final String URL_MESSAGE_ID = "org.apache.myfaces.Url.INVALID";
	 
	public AbstractUrlValidator(){

	}

	/**
	 * method that validates an url address.
	 * it uses the commons-validator
	 */
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
			
			org.apache.commons.validator.UrlValidator urlValidator = initValidator();
			
			if (!urlValidator.isValid(value.toString())) {
				Object[] args = {value.toString()};
				throw new ValidatorException(getFacesMessage(URL_MESSAGE_ID, args));
            }

	}
	
	private org.apache.commons.validator.UrlValidator initValidator()
	{
        int options = 0;
        
        if (isAllow2Slashes())
        {
            options = options | org.apache.commons.validator.UrlValidator.ALLOW_2_SLASHES; 
        }
        
        if (isAllowAllSchemas())
        {
            options = options | org.apache.commons.validator.UrlValidator.ALLOW_ALL_SCHEMES;
        }
        
        String [] schemesList = getSchemesList(); 
        org.apache.commons.validator.UrlValidator urlValidator = null;
        if (schemesList == null){
            urlValidator = new 
                org.apache.commons.validator.UrlValidator(options);
        }
        else
        {
            urlValidator = new 
                org.apache.commons.validator.UrlValidator(schemesList,options);
        }
        return urlValidator;
	}
	
	private String[] getSchemesList(){
	    if (getSchemes() == null)
	    {
	        return null;
	    }
	    String [] list = getSchemes().split(",");
	    String [] resp = new String [list.length];
	    
	    for (int i = 0; i < list.length; i++)
	    {
	        resp[i] = list[i].trim();
	    }	    
	    return resp;	    
	}
    	
	public abstract void setSchemes(String _schemes);

	/**
	 *  CSV values that indicates the set of schemes to check this url.
	 *  
	 *  If allowAllSchemas = true, the values of this field are ignored.
	 * 
	 *  If no schemes are provided, default to this set ("http", "https", "ftp").
	 * 
	 * @JSFProperty
	 */
    public abstract String getSchemes();

    public abstract void setAllow2Slashes(boolean _allow2Slashes);

    /**
     *  Allow two slashes in the path component of the URL.
     * 
     * @JSFProperty
     *   defaultValue = "false"
     */
    public abstract boolean isAllow2Slashes();

    public abstract void setAllowAllSchemas(boolean _allowAllSchemas);

    /**
     *  Allows all validly formatted schemes to pass validation instead of 
     *  supplying a set of valid schemes.
     *  
     * @JSFProperty
     *   defaultValue = "false"
     */
    public abstract boolean isAllowAllSchemas();

}

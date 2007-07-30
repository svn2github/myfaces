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
package org.apache.myfaces.custom.passwordStrength;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;


public class PasswordStrengthComponent extends UIInput { 

	public static String COMPONENT_TYPE = "org.apache.myfaces.PasswordStrength";

	public static String RENDERER_TYPE = "org.apache.myfaces.PasswordStrength";

	private String _preferredPasswordLength;
	private String _textStrengthDescriptions;
	private String _prefixText;
	private String _showDetails;
	private String _strengthIndicatorType;	

	public String getFamily() {
		return "org.apache.myfaces.PasswordStrength";
	}
	
	
	public String getShowDetails() {
		if (_showDetails != null)
			return _showDetails;

		ValueBinding vb = getValueBinding("showDetails");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb)
				: null;
	}
	
	public String getStrengthIndicatorType() {
		if (_strengthIndicatorType != null)
			return _strengthIndicatorType;

		ValueBinding vb = getValueBinding("strengthIndicatorType");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb)
				: null;
	}		

	public String getPreferredPasswordLength() {
		if (_preferredPasswordLength != null)
			return _preferredPasswordLength;

		ValueBinding vb = getValueBinding("preferredPasswordLength");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb)
				: null;
	}
	
	public String getPrefixText() {
		if (_prefixText != null)
			return _prefixText;

		ValueBinding vb = getValueBinding("prefixText");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb)
				: null;
	}
	
	public String getTextStrengthDescriptions() {
		if (_textStrengthDescriptions != null)
			return _textStrengthDescriptions;

		ValueBinding vb = getValueBinding("textStrengthDescriptions");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb)
				: null;
	}	

	public void setPreferredPasswordLength(String preferredPasswordLength) {
		_preferredPasswordLength = preferredPasswordLength;
	}


	public void setPrefixText(String prefixText) {
		_prefixText = prefixText;
	}

	public void setTextStrengthDescriptions(String strengthDescriptions) {
		_textStrengthDescriptions = strengthDescriptions;
	}
	
	public void setShowDetails(String showDetails) {
		_showDetails = showDetails;
	}
	
	public void setStrengthIndicatorType(String strengthIndicatorType) {
		_strengthIndicatorType = strengthIndicatorType;
	}	

	public Object saveState(FacesContext context) {
		Object values[] = new Object[6];
		values[0] = super.saveState(context);
		values[1] = _preferredPasswordLength;
		values[2] = _prefixText;
		values[3] = _textStrengthDescriptions;
		values[4] = _showDetails;	
		values[5] = _strengthIndicatorType;			
		return ((Object) (values));
	}

	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		_preferredPasswordLength = (String) values[1];
		_prefixText = (String) values[2];
		_textStrengthDescriptions = (String) values[3];
		_showDetails =  (String) values[4];
		_strengthIndicatorType = (String) values[5];
	}	
}

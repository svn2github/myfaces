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
package org.apache.myfaces.renderkit.html.ext;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.clientvalidation.common.CVUtils;

/**
 * @author cagatay (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFormRenderer extends org.apache.myfaces.renderkit.html.HtmlFormRenderer{

	private static String CLIENT_VALIDATON_SCRIPT = "return tomahawk.executeClientLifeCycle();";
	
	public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    	if(CVUtils.isCVEnabled()) {
    		if(!isDecorated(facesContext, component))
    		decorateOnSubmit(facesContext, component);
    	}
    	super.encodeBegin(facesContext, component);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    	super.encodeEnd(facesContext, component);
    	if(CVUtils.isCVEnabled()) {
    		CVUtils.encodeJavascript(facesContext);
			CVUtils.queueCVCalls(facesContext.getViewRoot());
			CVUtils.encodeValidationScript(facesContext);
    	}
    }
    
    private boolean isDecorated(FacesContext facesContext, UIComponent child) {
		String onSubmit= (String) child.getAttributes().get("onsubmit");
		
		if(onSubmit == null || onSubmit.indexOf(CLIENT_VALIDATON_SCRIPT) == -1)
			return false;
		else
			return true;
	}
	
	private void decorateOnSubmit(FacesContext facesContext, UIComponent child) {
		String onSubmitEvent = (String) child.getAttributes().get("onsubmit");
		if(onSubmitEvent == null)
			child.getAttributes().put("onsubmit", CLIENT_VALIDATON_SCRIPT);
		else
			child.getAttributes().put("onsubmit", onSubmitEvent + ";" + CLIENT_VALIDATON_SCRIPT);
	}
}

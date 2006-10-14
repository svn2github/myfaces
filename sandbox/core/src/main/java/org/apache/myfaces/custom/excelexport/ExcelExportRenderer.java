/*
 * Copyright 2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.myfaces.custom.excelexport;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;

/**
 * @author cagatay (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ExcelExportRenderer extends HtmlRenderer {

	public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException{
		if(component.getChildCount() == 0)
			return ;
		
		String tableId = ((ExcelExport) component).getFor();
		UIComponent child = (UIComponent) component.getChildren().get( 0 );
		
		if( !isDecorated(facesContext, child, tableId) )
			decorateOnClick(facesContext, child, tableId);
		
		RendererUtils.renderChildren(facesContext, component);
	}
	
	private boolean isDecorated(FacesContext facesContext, UIComponent child, String tableId) {
		String onClick = (String) child.getAttributes().get("onclick");
		String jsCall = getJSCall(facesContext, tableId);
		
		if(onClick == null || onClick.indexOf(jsCall) == -1)
			return false;
		else
			return true;
	}
	
	private void decorateOnClick(FacesContext facesContext, UIComponent child, String tableId) {
		String jsCall = getJSCall(facesContext, tableId);
		String onclickEvent = (String) child.getAttributes().get("onclick");
		if(onclickEvent == null)
			child.getAttributes().put("onclick", jsCall);
		else
			child.getAttributes().put("onclick", onclickEvent + ";" + jsCall);
	}
	
	private String getJSCall(FacesContext facesContext, String tableId) {
		String viewId = StringUtils.split( facesContext.getViewRoot().getViewId() , "\\.")[0];
		String contextPath = facesContext.getExternalContext().getRequestContextPath();
		return "window.open('" + contextPath + viewId + ".jsf?excelExportTableId=" + tableId + "');return false;";
	}

	
}
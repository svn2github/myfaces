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
package org.apache.myfaces.custom.clientvalidation.common;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

import org.apache.myfaces.custom.util.ComponentUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

/**
 * @author cagatay (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class CVUtils {

	private CVUtils() {
		// nope
	}
	
	public static void addCVCall(CVCall call) {
		getCVCallsHolder( FacesContext.getCurrentInstance() ).addCVCall( call );
	}
	
	public static CVCall createCVCall(UIInput input) {
		CVCall call = new CVCall();
		call.setId( input.getId() );
		call.setClientId( input.getClientId( FacesContext.getCurrentInstance() ) );
		call.setRequired( input.isRequired() );		
		
		//converter
		Converter converter = RendererUtils.findUIOutputConverter( FacesContext.getCurrentInstance(), input );
		if( converter instanceof ClientConverter ) {
			call.setConverterScriptFunction( ( (ClientConverter) converter ).getScriptFunction() );
			call.setConverterScriptResource( ( (ClientConverter) converter ).getScriptResource() );
		}
		
		//validators
		int numberOfValidators = input.getValidators().length;
		call.setValidatorScriptFunctions( ( new String[numberOfValidators] ) );
		call.setValidatorScriptResources( ( new String[numberOfValidators] ) );
		for( int i = 0; i < numberOfValidators; i++ ) {
			Validator validator = input.getValidators()[i];
			if( validator instanceof ClientValidator ) {
				call.getValidatorScriptFunctions()[i] = ((ClientValidator) validator).getScriptFunction();
				call.getValidatorScriptResources()[i] = ((ClientValidator) validator).getScriptResource();
			}
		}
		return call;
	}

	public static CVCallsHolder getCVCallsHolder(FacesContext facesContext) {
		return (CVCallsHolder) facesContext.getApplication().createValueBinding("#{CVCallsHolder}").getValue( facesContext );
	}

	public static boolean isCVEnabled() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		if(context.getInitParameter("org.apache.myfaces.ENABLE_CLIENT_SIDE_VALIDATION") == null)
			return false;
		else
			return Boolean.valueOf(context.getInitParameter("org.apache.myfaces.ENABLE_CLIENT_SIDE_VALIDATION")).booleanValue();
	}
	
	//Traverses the component tree recursively, queues validation calls starting from the root
	public static void queueCVCalls(UIComponent root) {
		if( root.getChildCount() > 0 ) {
			for( int i = 0; i< root.getChildCount() ; i++ ) 
				queueCVCalls( (UIComponent) root.getChildren().get( i ) );
		} else {
			if( root instanceof UIInput ) {
				UIInput input = (UIInput) root;
				addCVCall( createCVCall( input ) );
			}
		}
	}
	
	public static void encodeValidationScript(FacesContext facesContext) throws IOException{
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.write("\n<script type=\"text/javascript\" >\n");
		
		encodeMessageBundle(facesContext);			//renders message bundle map
		encodeCreateViewFunction(facesContext);
		encodeRenderResponseFunction(facesContext);
		
		writer.write("</script>");
	}
	
	//TODO: Remove this later, include message bundle instead of rendering it
	public static void encodeMessageBundle(FacesContext facesContext) throws IOException{
		ResponseWriter writer = facesContext.getResponseWriter();
		
		Locale locale = facesContext.getViewRoot().getLocale();
		String bundleName = "javax.faces.Messages";
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentLoader(bundleName));
		
		writer.write("MessageBundle = new function() {\n");
		writer.write("this.messages = new Array();\n");
		for(Enumeration e = bundle.getKeys() ; e.hasMoreElements() ;) {
			String key = e.nextElement().toString() ;
			String value = bundle.getString(key).replace("\"", "\\\"");
			writer.write("this.messages[\"" + key + "\"] = \"" + value + "\";\n");
		}
		writer.write("this.getString = function(key) { \n");
		writer.write("return this.messages[key];\n");
		writer.write("}\n");
		writer.write("}\n");
	}
	
	public static void encodeCreateViewFunction(FacesContext facesContext) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		
		writer.write("\t tomahawk.createView = function(facesContext) {\n");
		writer.write("\t\t facesContext.clearMessages();\n");
		writer.write("\t\t facesContext.viewRoot = new tomahawk.UIViewRoot();\n");
		
		List cvCalls = getCVCallsHolder( facesContext ).getCvCalls();
		for( int i = 0; i < cvCalls.size() ; i++ ) {
			CVCall call = (CVCall) cvCalls.get( i );
			
			writer.write( "\t\t facesContext.viewRoot.addChild((new tomahawk.UIInput('" + call.getId() + "'," );		//id
			writer.write( "'" + call.getClientId() + "'," );				//clientId
			writer.write( call.isRequired() + ",");							//required
			
			//Converter
			if(call.getConverterScriptFunction() != null )
				writer.write( "new " + call.getConverterScriptFunction() + ", ");
			else
				writer.write( "null,");
			
			//Validators
			writer.write( "new Array(");
			String[] validators = call.getValidatorScriptFunctions();
			if(validators == null )
				writer.write( ")");
			else {
				for(int j=0 ; j < validators.length ; j++ ) {
					writer.write( "new " + validators[j] );
					if( (j + 1) != validators.length )
						writer.write( "," );
				}
			}
			writer.write(" ) ) ) );\n");
		}
		writer.write("\t }\n");
	}
	
	public static void encodeRenderResponseFunction(FacesContext facesContext) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.write("\t tomahawk.renderResponse = function(facesContext) {\n");
		
		HtmlMessages htmlMessages = (HtmlMessages) ComponentUtils.findFirstMessagesComponent( facesContext, facesContext.getViewRoot() );
		
		if(htmlMessages != null)
		writer.write("\t\t tomahawk.RendererUtils.renderMessages(facesContext,'" + htmlMessages.getClientId( facesContext ) + "', '" + htmlMessages.getLayout() + "'); \n"); 
		
		writer.write("\t\t viewRoot = facesContext.viewRoot;\n");
		writer.write("\t\t for(var i = 0; i < viewRoot.children.length ; i ++) { \n");
		writer.write("\t\t\t\t  var uiinput = viewRoot.children[i]; \n");
		writer.write("\t\t\t\t tomahawk.RendererUtils.renderMessage(facesContext,uiinput.clientId); \n");
		writer.write("\t\t\t }\n");
		writer.write("\t\t }\n");
	}
	 
	public static ClassLoader getCurrentLoader(Object defaultObject)
	   {
	       ClassLoader loader = Thread.currentThread().getContextClassLoader();
	       if(loader == null)
	       {
	           loader = defaultObject.getClass().getClassLoader();
	       }
	       return loader;
	  }

}

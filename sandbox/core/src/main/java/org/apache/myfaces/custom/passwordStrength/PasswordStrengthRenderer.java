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

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.accordion.HtmlAccordionPanelRenderer;
import org.apache.myfaces.custom.excelexport.ExcelExport;
import org.apache.myfaces.custom.inputAjax.HtmlInputTextAjax;
import org.apache.myfaces.custom.statechangednotifier.StateChangedNotifierRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.util.MessageUtils;

public class PasswordStrengthRenderer extends Renderer {

	// This private method is used for including all the related resources ...
	private void includeResources(FacesContext context, UIComponent component,
			ResponseWriter writer) throws IOException {
		// Load the resources ...
		AddResource addResource = AddResourceFactory.getInstance(context);
		// Load the css style ...
		String styleLocation = (String) component.getAttributes().get(
				JSFAttr.STYLE_LOCATION);
		if (StringUtils.isNotBlank(styleLocation)) {
			addResource.addStyleSheet(context, AddResource.HEADER_BEGIN,
					styleLocation + "/passwordStrength.css");
		} else {
			addResource.addStyleSheet(context, AddResource.HEADER_BEGIN,
					PasswordStrengthRenderer.class, "css/passwordStrength.css");
		}
		// Load the JS file ...
		String javascriptLocation = (String) component.getAttributes().get(
				JSFAttr.JAVASCRIPT_LOCATION);
		if (javascriptLocation != null) {
			// add user defined javascripts
			addResource.addJavaScriptAtPosition(context,
					AddResource.HEADER_BEGIN, javascriptLocation
							+ "/passwordStrength.js");
		} else {
			addResource.addJavaScriptAtPosition(context,
					AddResource.HEADER_BEGIN, PasswordStrengthRenderer.class,
					"passwordStrength.js");
		}
	}

	private void renderStartDiv(UIComponent component, ResponseWriter writer)
			throws IOException {

		writer.startElement(HTML.DIV_ELEM, component);

		writer.startElement(HTML.TABLE_ELEM, component);

		writer.startElement(HTML.TR_ELEM, component);
		writer.startElement(HTML.TD_ELEM, component);
	}

	private void renderEndDiv(UIComponent component, ResponseWriter writer)
			throws IOException {
		writer.endElement(HTML.TD_ELEM);
		writer.endElement(HTML.TR_ELEM);

		writer.endElement(HTML.TABLE_ELEM);

		writer.endElement(HTML.DIV_ELEM);
	}

	private String getDefaultTextDesc() {
		return MessageUtils.getMessage(BUNDLE_BASE_NAME,
				MessageUtils.getCurrentLocale(),
				"org.apache.myfaces.custom.passwordStrength.DESC", null)
				.getDetail();
	}

	private String getDefaultPrefix() {
		return MessageUtils.getMessage(BUNDLE_BASE_NAME,
				MessageUtils.getCurrentLocale(),
				"org.apache.myfaces.custom.passwordStrength.PREFIX", null)
				.getDetail();
	}

	private void createTextSpan(PasswordStrengthComponent passwordStrength,
			FacesContext context, String clientID) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		String preferredLength = passwordStrength.getPreferredPasswordLength();
		String prefixText = passwordStrength.getPrefixText();
		String textStrengthDescriptions = passwordStrength
				.getTextStrengthDescriptions();
		String txtName = "'" + clientID + "'";

		// Set default values for optional attributes ...

		prefixText = (prefixText == null) ? "'" + getDefaultPrefix() + "'"
				: "'" + prefixText + "'";

		textStrengthDescriptions = (textStrengthDescriptions == null) ? "'"
				+ getDefaultTextDesc() + "'" : "'" + textStrengthDescriptions
				+ "'";

		writer.startElement(HTML.SPAN_ELEM, passwordStrength);

		writer.startElement(HTML.INPUT_ELEM, passwordStrength);
		writer.writeAttribute(HTML.TYPE_ATTR, "password", HTML.TYPE_ATTR);
		writer.writeAttribute(HTML.ID_ATTR, clientID, HTML.ID_ATTR);
		writer.writeAttribute(HTML.NAME_ATTR, clientID, HTML.NAME_ATTR);

		String value = "";
		Object objValue = ((UIInput) passwordStrength).getSubmittedValue();
		if (objValue != null) {
			value = passwordStrength.getValue().toString();
		}
		writer.writeAttribute("value", value, "value");

		writer.writeAttribute("onkeyup", createOnKeyUpString(txtName,
				preferredLength, prefixText, textStrengthDescriptions, true),
				"onkeyup");
		writer.writeAttribute("onblur", ON_BLUR_STRING, "onblur");

		writer.endElement(HTML.INPUT_ELEM);

		writer.endElement(HTML.SPAN_ELEM);
	}

	private void createIndicatorSpan(UIComponent component,
			ResponseWriter writer) throws IOException {
		writer.endElement(HTML.TD_ELEM);
		writer.startElement(HTML.TD_ELEM, component);

		writer.startElement(HTML.SPAN_ELEM, component);
		writer.writeAttribute(HTML.ID_ATTR, "indicatorMessage", HTML.ID_ATTR);
		writer.writeAttribute(HTML.CLASS_ATTR, "indicatorMessage",
				HTML.CLASS_ATTR);
		writer.endElement(HTML.SPAN_ELEM);

		writer.endElement(HTML.TD_ELEM);
		writer.endElement("TR");

		writer.startElement("TR", component);
		writer.startElement(HTML.TD_ELEM, component);

		writer.startElement("div", component);
		writer.writeAttribute("id", "leftCharsMessage", "id");
		writer.endElement("div");
	}

	private void createHTMLComponents(FacesContext facesContext,
			UIComponent component, ResponseWriter writer, String clientID)
			throws IOException {
		PasswordStrengthComponent passwordStrength = (PasswordStrengthComponent) component;

		renderStartDiv(component, writer);

		createTextSpan(passwordStrength, facesContext, clientID);

		createIndicatorSpan(component, writer);

		renderEndDiv(component, writer);
	}

	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		RendererUtils.checkParamValidity(context, component,
				PasswordStrengthComponent.class);

		if (HtmlRendererUtils.isDisplayValueOnly(component)
				|| isDisabled(context, component)) {
			super.encodeEnd(context, component);
			return;
		}

		String clientID = component.getClientId(context);

		ResponseWriter writer = context.getResponseWriter();

		includeResources(context, component, writer);

		createHTMLComponents(context, component, writer, clientID);
	}

	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
	}

	public void decode(FacesContext context, UIComponent component) {
		Map requestMap = context.getExternalContext().getRequestParameterMap();

		String clientID = component.getClientId(context);

		String newValue = (String) requestMap.get(clientID);

		((UIInput) component).setSubmittedValue(newValue);
	}

	protected boolean isDisabled(FacesContext facesContext,
			UIComponent component) {
		if (component instanceof HtmlInputText) {
			return ((HtmlInputText) component).isDisabled();
		} else {
			return org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils
					.getBooleanAttribute(component, HTML.DISABLED_ATTR, false);
		}
	}

	private String createOnKeyUpString(String txtName, String preferredLength,
			String prefix, String textStrengthDescriptions,
			boolean showMessageIndicator) {
		String showMessageIndicatorString = "";
		if (showMessageIndicator == true)
			showMessageIndicatorString = "show('indicatorMessage')";
		return "updateStatusValue( " + txtName + "," + preferredLength + ", "
				+ prefix + ", " + textStrengthDescriptions + ", " + "'true');"
				+ showMessageIndicatorString;
	}

	// The constants ...
	final String ON_BLUR_STRING = "hide('indicatorMessage');"
			+ "hide('leftCharsMessage');";

	final String BUNDLE_BASE_NAME = "org.apache.myfaces.custom.passwordStrength.resource.PasswordStrength";
}

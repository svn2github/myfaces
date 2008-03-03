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
package org.apache.myfaces.custom.captcha;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class CAPTCHARenderer extends Renderer {
	
	private static final String CAPTCHA_SERVLET_NAME = "apache_captcha_servlet_url";

	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {

		CAPTCHAComponent captchaComponent = (CAPTCHAComponent) component;
		
		renderCAPTCHA(context, captchaComponent);
	}
	
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeEnd(context, component);
	}

	/*
	 * This helper method renders the img tag that will
	 * call the CAPTCHAServlet to render the CAPTCHA image. 
	 */
	private void renderCAPTCHA(FacesContext context, CAPTCHAComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		writer.startElement("img", component);
		writer.writeAttribute("src", CAPTCHA_SERVLET_NAME + "?"
				+ appendParameters(component), "src");
		writer.endElement("img");
	}

	/*
	 * This helper method is used for appending the parameters to the
	 * CAPTCHA servlet.
	 */
	private String appendParameters(CAPTCHAComponent component) {
		return CAPTCHAComponent.ATTRIBUTE_CAPTCHASESSIONKEYNAME + "="
				+ component.getCaptchaSessionKeyName();
	}
}

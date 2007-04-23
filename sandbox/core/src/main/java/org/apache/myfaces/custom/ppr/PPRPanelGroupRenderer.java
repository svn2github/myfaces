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
package org.apache.myfaces.custom.ppr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlGroupRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroupRenderer extends HtmlGroupRenderer {
	public static final String PPR_INITIALIZED = "org.apache.myfaces.ppr.INITIALIZED";

	public static final String PPR_RESPONSE = "org.apache.myfaces.ppr.RESPONSE";

	private static Log log = LogFactory.getLog(PPRPanelGroupRenderer.class);

	private static final String MY_FACES_PPR_INITIALIZED = "/*MyFaces PPR initialized*/";

	private static final String ADD_PARTIAL_TRIGGER_FUNCTION = "addPartialTrigger";

	private static final String ADD_PARTIAL_TRIGGER_PATTERN_FUNCTION = "addPartialTriggerPattern";

	private static final String ADD_INLINE_LOADING_MESSAGE_FUNCTION = "addInlineLoadingMessage";

	private static final String PPR_JS_FILE = "ppr.js";

	private static final String MY_FACES_PPR_INIT_CODE = "new org.apache.myfaces.PPRCtrl";

	public void encodeJavaScript(FacesContext facesContext, PPRPanelGroup pprGroup) throws IOException {
		
		final ExternalContext externalContext = facesContext.getExternalContext();
		
		final Map requestMap = externalContext.getRequestMap();
		
		if (requestMap.containsKey(PPR_RESPONSE)) {
			return;
		}

		FormInfo fi = RendererUtils.findNestingForm(pprGroup, facesContext);
		if (fi == null) {
			throw new FacesException("PPRPanelGroup must be embedded in a form.");
		}

		if (!requestMap.containsKey(PPR_INITIALIZED)) {
			requestMap.put(PPR_INITIALIZED, Boolean.TRUE);

			String javascriptLocation = (String) pprGroup.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
			AddResource addResource = AddResourceFactory.getInstance(facesContext);
			DojoUtils.addMainInclude(facesContext, pprGroup, javascriptLocation, new DojoConfig());
			DojoUtils.addRequire(facesContext, pprGroup, "dojo.io.*");
			DojoUtils.addRequire(facesContext, pprGroup, "dojo.event.*");
			addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, MY_FACES_PPR_INITIALIZED);

			addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, PPRPanelGroup.class, PPR_JS_FILE);
		}

		StringBuffer script = new StringBuffer();
		final String formName = fi.getFormName();
		
		String pprCtrlReference = "dojo.byId('" + formName + "').myFacesPPRCtrl";

		if (!requestMap.containsKey(PPR_INITIALIZED + "." + formName)) {
			requestMap.put(PPR_INITIALIZED + "." + formName, Boolean.TRUE);

			script.append(pprCtrlReference + "=" + MY_FACES_PPR_INIT_CODE + "('" + formName + "',"
					+ pprGroup.getShowDebugMessages().booleanValue() + "," + pprGroup.getStateUpdate().booleanValue() + ");\n");

			if (pprGroup.getPeriodicalUpdate() != null) {
				script.append(pprCtrlReference + ".registerOnSubmitInterceptor();");
			}

			renderInlineScript(facesContext, pprGroup, script.toString());
		}

		String clientId = pprGroup.getClientId(facesContext);

		if (pprGroup.getPeriodicalUpdate() != null) {
			script = new StringBuffer();
			script.append(pprCtrlReference + ".startPeriodicalUpdate(" + pprGroup.getPeriodicalUpdate() + ",'" + clientId
					+ "');");

			renderInlineScript(facesContext, pprGroup, script.toString());
		}

		String partialTriggerId;
		String partialTriggerClientId;
		UIComponent partialTriggerComponent;

		String partialTriggers = pprGroup.getPartialTriggers();
		
		String partialTriggerPattern = pprGroup.getPartialTriggerPattern();
		if (partialTriggerPattern != null && partialTriggerPattern.trim().length() > 0) {
			script = new StringBuffer();
			script.append(pprCtrlReference + "." + ADD_PARTIAL_TRIGGER_PATTERN_FUNCTION + "('" + partialTriggerPattern
					+ "','" + clientId + "');");

			renderInlineScript(facesContext, pprGroup, script.toString());
		}

		String inlineLoadingMessage = pprGroup.getInlineLoadingMessage();

		if (inlineLoadingMessage != null && inlineLoadingMessage.trim().length() > 0) {
			script = new StringBuffer();
			script.append(pprCtrlReference + "." + ADD_INLINE_LOADING_MESSAGE_FUNCTION + "('" + inlineLoadingMessage + "','"
					+ clientId + "');");

			renderInlineScript(facesContext, pprGroup, script.toString());

		}

		if (partialTriggers != null && partialTriggers.trim().length() > 0) {
			StringTokenizer st = new StringTokenizer(partialTriggers, ",; ", false);
			while (st.hasMoreTokens()) {
				partialTriggerId = st.nextToken();
				partialTriggerComponent = pprGroup.findComponent(partialTriggerId);
				if (partialTriggerComponent == null) {
					partialTriggerComponent = facesContext.getViewRoot().findComponent(partialTriggerId);
				}
				if (partialTriggerComponent != null) {
					partialTriggerClientId = partialTriggerComponent.getClientId(facesContext);
					script = new StringBuffer();
					script.append(pprCtrlReference + "." + ADD_PARTIAL_TRIGGER_FUNCTION + "('" + partialTriggerClientId + "','"
							+ clientId + "');");

					renderInlineScript(facesContext, pprGroup, script.toString());

				} else {
					if (log.isDebugEnabled()) {
						log.debug("PPRPanelGroupRenderer Component with id " + partialTriggerId + " not found!");
					}
				}
			}
		}
	}

	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		if (uiComponent.getId() == null || uiComponent.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
			throw new IllegalArgumentException("'id' is a required attribute for the PPRPanelGroup");
		}
		super.encodeBegin(facesContext, uiComponent);
	}

	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		super.encodeEnd(facesContext, uiComponent);
		if (uiComponent instanceof PPRPanelGroup) {
			PPRPanelGroup pprGroup = (PPRPanelGroup) uiComponent;

			final String triggers = pprGroup.getPartialTriggers();
			final String triggerPattern = pprGroup.getPartialTriggerPattern();
			
			if ((triggers != null && triggers.length() > 0)
					|| (triggerPattern != null && triggerPattern.length() > 0)
					|| pprGroup.getPeriodicalUpdate() != null) {
				encodeJavaScript(facesContext, pprGroup);
			}
		}
	}

	/**
	 * Helper to write an inline javascript at the exact resource location of the
	 * call.
	 * 
	 * @param facesContext
	 *          The current faces-context.
	 * @param component
	 *          The component for which the script is written.
	 * @param script
	 *          The script to be written.
	 * @throws IOException
	 *           A forwarded exception from the underlying renderer.
	 */
	private static void renderInlineScript(FacesContext facesContext, UIComponent component, String script)
			throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.startElement(HTML.SCRIPT_ELEM, component);
		writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
		writer.write(script);
		writer.endElement(HTML.SCRIPT_ELEM);
	}
}

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
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlResponseWriterImpl;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Ernst Fastl
 */
public class PPRPhaseListener implements PhaseListener {
	private static Log log = LogFactory.getLog(PPRPhaseListener.class);

	private static final String PPR_PARAMETER = "org.apache.myfaces.PPRCtrl.ajaxRequest";

	private static final String TRIGGERED_COMPONENTS_PARAMETER = "org.apache.myfaces.PPRCtrl.triggeredComponents";

	private static final String XML_HEADER = "<?xml version=\"1.0\"?>\n";

	public void afterPhase(PhaseEvent phaseEvent) {
	}

	public void beforePhase(PhaseEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("In PPRPhaseListener beforePhase");
		}

		FacesContext context = event.getFacesContext();
		final ExternalContext externalContext = context.getExternalContext();
		Map externalRequestMap = externalContext.getRequestParameterMap();

		if (externalRequestMap.containsKey(PPR_PARAMETER)) {
			processPartialPageRequest(context, externalContext, externalRequestMap);
		}
	}

	private void processPartialPageRequest(FacesContext context, final ExternalContext externalContext, Map externalRequestMap) {
        //If the PhaseListener is invoked the second time do nothing
        if(externalContext.getRequestMap().containsKey(PPRPanelGroupRenderer.PPR_RESPONSE))
            return;
        externalContext.getRequestMap().put(PPRPanelGroupRenderer.PPR_RESPONSE, Boolean.TRUE);

		ServletResponse response = (ServletResponse) externalContext.getResponse();
		ServletRequest request = (ServletRequest) externalContext.getRequest();

		UIViewRoot viewRoot = context.getViewRoot();
		final String characterEncoding = request.getCharacterEncoding();
		String contentType = getContentType("text/xml", characterEncoding);
		response.setContentType(contentType);
		response.setLocale(viewRoot.getLocale());
		String triggeredComponents = (String) externalRequestMap.get(TRIGGERED_COMPONENTS_PARAMETER);
		
		try {
			PrintWriter out = response.getWriter();
			context.setResponseWriter(new HtmlResponseWriterImpl(out, contentType, characterEncoding));
			out.print(XML_HEADER);
			out.print("<response>\n");
			encodeTriggeredComponents(out, triggeredComponents, viewRoot, context);
			out.print("</response>");
			out.flush();
		} catch (IOException e) {
			throw new FacesException(e);
		}

		context.responseComplete();
	}

	private String getContentType(String contentType, String charset) {
		if (charset == null || charset.trim().length() == 0)
			return contentType;
		else
			return contentType + ";charset=" + charset;
	}

	private void encodeTriggeredComponents(PrintWriter out, String triggeredComponents, UIViewRoot viewRoot,
			FacesContext context) {
		StringTokenizer st = new StringTokenizer(triggeredComponents, ",", false);
		String clientId;
		UIComponent component;
		while (st.hasMoreTokens()) {
			clientId = st.nextToken();
			component = viewRoot.findComponent(clientId);
			if (component != null) {
				out.print("<component id=\"" + component.getClientId(context) + "\"><![CDATA[");
				boolean oldValue = HtmlRendererUtils.isAllowedCdataSection(context);
				HtmlRendererUtils.allowCdataSection(context, false);
				try {
					RendererUtils.renderChildren(context, component);
				} catch (IOException e) {
					throw new FacesException(e);
				}
				HtmlRendererUtils.allowCdataSection(context, oldValue);
				out.print("]]></component>");
			} else {
				log.debug("PPRPhaseListener component with id" + clientId + "not found!");
			}
		}
		out.print("<state>");
		FacesContext facesContext = FacesContext.getCurrentInstance();
		StateManager stateManager = facesContext.getApplication().getStateManager();
		StateManager.SerializedView serializedView = stateManager.saveSerializedView(facesContext);
		try {
			stateManager.writeState(facesContext, serializedView);
		} catch (IOException e) {
			throw new FacesException(e);
		}

		out.print("</state>");

	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}

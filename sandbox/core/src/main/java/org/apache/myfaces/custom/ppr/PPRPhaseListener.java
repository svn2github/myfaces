/**
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.ppr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlResponseWriterImpl;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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
public class PPRPhaseListener implements PhaseListener
{
	private static Log log = LogFactory.getLog(PPRPhaseListener.class);
	private static final String PPR_PARAMETER = "org.apache.myfaces.PPRCtrl.ajaxRequest";
	private static final String TRIGGERED_COMPONENTS_PARAMETER = "org.apache.myfaces.PPRCtrl.triggeredComponents";
	private static final String XML_HEADER = "<?xml version=\"1.0\"?>\n";

	public void afterPhase(PhaseEvent phaseEvent)
	{
	}

	public void beforePhase(PhaseEvent event)
	{
		if (log.isDebugEnabled())
		{
			log.debug("In PPRPhaseListener beforePhase");
		}

		FacesContext context = event.getFacesContext();
		Map externalRequestMap = context.getExternalContext().getRequestParameterMap();

		if (externalRequestMap.containsKey(PPR_PARAMETER))
		{
			ServletResponse response =
				(ServletResponse) context.getExternalContext().getResponse();
			ServletRequest request =
				(ServletRequest) context.getExternalContext().getRequest();
			UIViewRoot viewRoot = context.getViewRoot();
			ResponseWriter writer = context.getResponseWriter();
			response.setContentType("text/xml;charset=" + request.getCharacterEncoding());
			response.setLocale(viewRoot.getLocale());
			String triggeredComponents = (String) externalRequestMap.get(TRIGGERED_COMPONENTS_PARAMETER);
			try
			{
				PrintWriter out = response.getWriter();
				context.setResponseWriter(new HtmlResponseWriterImpl(out,
					null,
					request.getCharacterEncoding()));
				out.print(XML_HEADER);
				out.print("<response>\n");
				encodeTriggeredComponents(out, triggeredComponents, viewRoot, context);
				out.print("</response>");
				out.flush();
			}
			catch (IOException e)
			{
				throw new FacesException(e);
			}

			context.responseComplete();
		}
	}

	private void encodeTriggeredComponents(PrintWriter out, String triggeredComponents, UIViewRoot viewRoot, FacesContext context)
	{
		StringTokenizer st = new StringTokenizer(triggeredComponents, ",", false);
		String clientId = null;
		UIComponent component = null;
		while (st.hasMoreTokens())
		{
			clientId = st.nextToken();
			component = viewRoot.findComponent(clientId);
			out.print("<component id=\"" +
				component.getClientId(context) +
				"\"><![CDATA[");
			try
			{
				RendererUtils.renderChild(context, component);
			}
			catch (IOException e)
			{
				throw new FacesException(e);
			}
			out.print("]]></component>");
		}
	}

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}
}

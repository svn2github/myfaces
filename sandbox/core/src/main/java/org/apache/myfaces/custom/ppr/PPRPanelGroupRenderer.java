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
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlGroupRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.FacesException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroupRenderer extends HtmlGroupRenderer
{
	private static Log log = LogFactory.getLog(PPRPanelGroupRenderer.class);

	private static final String MY_FACES_PPR_INITIALIZED = "/*MyFaces PPR initialized*/";
	private static final String ADD_PARTIAL_TRIGGER_FUNCTION = "myFacesPPRCtrl.addPartialTrigger";
	private static final String PPR_JS_FILE = "ppr.js";
	private static final String BODY_SCRIPT_INFOS_ATTRIBUTE_NAME = "bodyScriptInfos";
	private static final String MY_FACES_PPR_INIT_CODE =
		"var myFacesPPRCtrl = new org.apache.myfaces.PPRCtrl";

	public void encodeJavaScript(FacesContext facesContext, PPRPanelGroup uiComponent) throws IOException
	{
		FormInfo fi = _ComponentUtils.findNestingForm(uiComponent, facesContext);
		if (fi == null)
		{
			throw new FacesException("PPRPanelGroup must be embedded in an form.");
		}

		if (!isInlineScriptSet(facesContext, MY_FACES_PPR_INITIALIZED))
		{
			String javascriptLocation = (String) uiComponent.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
			AddResource addResource = AddResourceFactory.getInstance(facesContext);
			DojoUtils.addMainInclude(facesContext, uiComponent, javascriptLocation, new DojoConfig());
			DojoUtils.addRequire(facesContext, uiComponent, "dojo.io.*");
			DojoUtils.addRequire(facesContext, uiComponent, "dojo.event.*");
			addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, MY_FACES_PPR_INITIALIZED);

			addResource.addJavaScriptAtPosition(facesContext,
				AddResource.HEADER_BEGIN,
				PPRPanelGroup.class,
				PPR_JS_FILE);
		}

		writeInlineScript(facesContext, uiComponent, MY_FACES_PPR_INIT_CODE + "('" + fi.getFormName() + "');");

		String partialTriggerId = null;
		String partialTriggerClientId = null;
		UIComponent partialTriggerComponent = null;
		String partialTriggers = ((PPRPanelGroup) uiComponent).getPartialTriggers();
		String clientId = uiComponent.getClientId(facesContext);
		StringTokenizer st = new StringTokenizer(partialTriggers, ",; ", false);
		while (st.hasMoreTokens())
		{
			partialTriggerId = st.nextToken();
			partialTriggerComponent = uiComponent.findComponent(partialTriggerId);
			if (partialTriggerComponent != null)
			{
				partialTriggerClientId = partialTriggerComponent.getClientId(facesContext);
				writeInlineScript(facesContext, uiComponent,
					ADD_PARTIAL_TRIGGER_FUNCTION +
						"('" +
						partialTriggerClientId +
						"','" +
						clientId +
						"');");
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug("PPRPanelGroupRenderer Component with id " + partialTriggerId + " not found!");
				}
			}
		}

	}

	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
	{
		super.encodeEnd(facesContext, uiComponent);
		if (uiComponent instanceof PPRPanelGroup)
		{
			PPRPanelGroup pprGroup = (PPRPanelGroup) uiComponent;
			if (pprGroup.getPartialTriggers() != null &&
				pprGroup.getPartialTriggers().length() > 0)
			{
				encodeJavaScript(facesContext, pprGroup);
			}
		}
	}

	/**
	 * helper to write an inline javascript at the
	 * exact resource location of the call
	 *
	 * @param facesContext
	 * @param component
	 * @param script
	 * @throws IOException
	 */
	private static void writeInlineScript(FacesContext facesContext, UIComponent component, String script) throws IOException
	{
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.startElement(HTML.SCRIPT_ELEM, component);
		writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
		writer.write(script);
		writer.endElement(HTML.SCRIPT_ELEM);
	}

	public static boolean isInlineScriptSet(FacesContext context, String inlineScript)
	{
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		Set set = getBodyScriptInfos(request);

		if (!set.contains(inlineScript))
		{
			set.add(inlineScript);

			return false;
		}

		return true;
	}

	private static Set getBodyScriptInfos(HttpServletRequest request)
	{
		Set set = (Set) request.getAttribute(BODY_SCRIPT_INFOS_ATTRIBUTE_NAME);

		if (set == null)
		{
			set = new TreeSet();
			request.setAttribute(BODY_SCRIPT_INFOS_ATTRIBUTE_NAME, set);
		}

		return set;
	}

}

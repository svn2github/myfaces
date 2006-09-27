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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroupRenderer extends HtmlGroupRenderer
{
	public static final String PPR_INITIALIZED = "org.apache.myfaces.ppr.INITIALIZED";
	public static final String PPR_RESPONSE = "org.apache.myfaces.ppr.RESPONSE";

	private static Log log = LogFactory.getLog(PPRPanelGroupRenderer.class);

	private static final String MY_FACES_PPR_INITIALIZED = "/*MyFaces PPR initialized*/";
	private static final String ADD_PARTIAL_TRIGGER_FUNCTION = "addPartialTrigger";
	private static final String ADD_PARTIAL_TRIGGER_PATTERN_FUNCTION = "addPartialTriggerPattern";
	private static final String ADD_INLINE_LOADING_MESSAGE_FUNCTION = "addInlineLoadingMessage";
	private static final String PPR_JS_FILE = "ppr.js";

	private static final String MY_FACES_PPR_INIT_CODE = "new org.apache.myfaces.PPRCtrl";

	public void encodeJavaScript(FacesContext facesContext, PPRPanelGroup uiComponent) throws IOException
	{
		if (facesContext.getExternalContext().getRequestMap().containsKey(PPR_RESPONSE))
		{
			return;
		}

		FormInfo fi = _ComponentUtils.findNestingForm(uiComponent, facesContext);
		if (fi == null)
		{
			throw new FacesException("PPRPanelGroup must be embedded in an form.");
		}

		if (!facesContext.getExternalContext().getRequestMap().containsKey(PPR_INITIALIZED))
		{
			facesContext.getExternalContext().getRequestMap().put(PPR_INITIALIZED, Boolean.TRUE);

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
        
        if (!facesContext.getExternalContext().getRequestMap().containsKey(PPR_INITIALIZED  +
                    "." +
                    fi.getFormName()))
		{
			facesContext.getExternalContext().getRequestMap().put(PPR_INITIALIZED  +
                    "." +
                    fi.getFormName(),
                    Boolean.TRUE);
            writeInlineScript(facesContext, uiComponent,
                    "document.getElementById('" +
                    fi.getFormName() +
                    "').myFacesPPRCtrl =" +
                    MY_FACES_PPR_INIT_CODE + "('" + fi.getFormName() + "');");
        }


        String partialTriggerId = null;
		String partialTriggerClientId = null;
		UIComponent partialTriggerComponent = null;
		
		String partialTriggers = ((PPRPanelGroup) uiComponent).getPartialTriggers();
		String clientId = uiComponent.getClientId(facesContext);
		
		String partialTriggerPattern = ((PPRPanelGroup) uiComponent).getPartialTriggerPattern();
		if(partialTriggerPattern != null && partialTriggerPattern.trim().length()>0) 
		{
			writeInlineScript(facesContext, uiComponent,
                    "document.getElementById('" +
                    fi.getFormName() +
                    "').myFacesPPRCtrl." +
                    ADD_PARTIAL_TRIGGER_PATTERN_FUNCTION +
						"('" +
						partialTriggerPattern +
						"','" +
						clientId +
						"');");
		}
		
		String inlineLoadingMessage =((PPRPanelGroup) uiComponent).getInlineLoadingMessage();
		
		if(inlineLoadingMessage!= null && inlineLoadingMessage.trim().length()>0)
		{
			writeInlineScript(facesContext, uiComponent,
                    "document.getElementById('" +
                    fi.getFormName() +
                    "').myFacesPPRCtrl." +
                    ADD_INLINE_LOADING_MESSAGE_FUNCTION +
						"('" +
						inlineLoadingMessage +
						"','" +
						clientId +
						"');");
		}
		
		if(partialTriggers!= null && partialTriggers.trim().length()>0)
		{
			StringTokenizer st = new StringTokenizer(partialTriggers, ",; ", false);
			while (st.hasMoreTokens())
			{
				partialTriggerId = st.nextToken();
				partialTriggerComponent = uiComponent.findComponent(partialTriggerId);
				if(partialTriggerComponent == null)
				{
					partialTriggerComponent = FacesContext.getCurrentInstance().getViewRoot().findComponent(partialTriggerId);
				}
				if (partialTriggerComponent != null)
				{
					partialTriggerClientId = partialTriggerComponent.getClientId(facesContext);
					writeInlineScript(facesContext, uiComponent,
	                    "document.getElementById('" +
	                    fi.getFormName() +
	                    "').myFacesPPRCtrl." +
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

	}

	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
	{
		if (uiComponent.getId() == null || uiComponent.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
		{
			throw new IllegalArgumentException("'id' is a required attribute for the PPRPanelGroup");
		}
		super.encodeBegin(facesContext, uiComponent);
	}

	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
	{
		super.encodeEnd(facesContext, uiComponent);
		if (uiComponent instanceof PPRPanelGroup)
		{
			PPRPanelGroup pprGroup = (PPRPanelGroup) uiComponent;

			if ((pprGroup.getPartialTriggers() != null &&
				pprGroup.getPartialTriggers().length() > 0) ||
				(pprGroup.getPartialTriggerPattern() != null &&
						pprGroup.getPartialTriggerPattern().length() > 0))
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
}

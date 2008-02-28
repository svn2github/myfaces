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
import org.apache.myfaces.custom.subform.SubForm;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroupRenderer extends HtmlGroupRenderer
{
	public static final String PPR_INITIALIZED = "org.apache.myfaces.ppr.INITIALIZED";

	private static Log log = LogFactory.getLog(PPRPanelGroupRenderer.class);

	private static final String ADD_PARTIAL_TRIGGER_FUNCTION = "addPartialTrigger";

	private static final String ADD_PERIODICAL_TRIGGER_FUNCTION = "addPeriodicalTrigger";

	private static final String ADD_PARTIAL_TRIGGER_PATTERN_FUNCTION = "addPartialTriggerPattern";

	private static final String SET_SUBFORM_ID_FUNCTION = "setSubFormId";

	private static final String ADD_INLINE_LOADING_MESSAGE_FUNCTION = "addInlineLoadingMessage";

	private static final String PPR_JS_FILE = "ppr.js";

	private static final String MY_FACES_PPR_INIT_CODE = "new org.apache.myfaces.PPRCtrl";

	private static final String DISABLE_RENDER_CHILDREN = "org.apache.myfaces.PPRPanelGroup.disableRenderChildren";

	public static final String TRANSIENT_MARKER_ATTRIBUTE = "org.apache.myfaces.PPRPanelGroup.transientComponent";

	/**
	 * Renders the start of a span element. Iterates over all child
	 * components and sets transient components to transient=false. Those
	 * components are marked with the TRANSIENT_MARKER_ATTRIBUTE so the
	 * {@link PPRPhaseListener} can reset them to transient in the next
	 * non-PPR Request
	 *
	 * @param facesContext the current {@link FacesContext}
	 * @param uiComponent  the {@link PPRPanelGroup} to render
	 */
	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
	{
		if(uiComponent.getId() == null || uiComponent.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
		{
			throw new IllegalArgumentException("'id' is a required attribute for the PPRPanelGroup");
		}

		// todo: in 1.2, better use a combo of
		// invokeComponent/RendererUtils.renderChildren() instead
		uiComponent.getAttributes().put(DISABLE_RENDER_CHILDREN, Boolean.TRUE);

		// Iterate over the transient child components and set transient to
		// false
		// This is necessary to have those components available for PPR
		// responses later on
		for(Iterator iter = uiComponent.getChildren().iterator(); iter.hasNext();)
		{
			UIComponent child = (UIComponent) iter.next();
			if(child.isTransient())
			{
				child.setTransient(false);
				child.getAttributes().put(TRANSIENT_MARKER_ATTRIBUTE, Boolean.TRUE);
			}
		}

		super.encodeBegin(facesContext, uiComponent);
	}

	/**
	 * todo: in 1.2, better use a combo of
	 * invokeComponent/RendererUtils.renderChildren() instead
	 *
	 * @param context
	 * @param component
	 * @throws IOException
	 */
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException
	{
		Boolean disableRenderChildren = (Boolean) component.getAttributes().get(DISABLE_RENDER_CHILDREN);

		if(disableRenderChildren == null || disableRenderChildren.booleanValue() == false)
		{
			RendererUtils.renderChildren(context, component);
		}
	}

	/**
	 * Encodes the end of the span-element and afterwards the inline
	 * JavaScript for the client side initialization of the
	 * {@link PPRPanelGroup}.
	 *
	 * @param facesContext the current {@link FacesContext}
	 * @param uiComponent  the {@link PPRPanelGroup} to render
	 */
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
	{
		// Render the span end element
		super.encodeEnd(facesContext, uiComponent);
		if(uiComponent instanceof PPRPanelGroup)
		{
			PPRPanelGroup pprGroup = (PPRPanelGroup) uiComponent;

			final String triggers = pprGroup.getPartialTriggers();
			final String triggerPattern = pprGroup.getPartialTriggerPattern();

			// Check if triggers, a pattern or a periodical update is
			// defined
			if((triggers != null && triggers.length() > 0) || (triggerPattern != null && triggerPattern.length() > 0) || pprGroup.getPeriodicalUpdate() != null)
			{
				// encode the initialization inline JavaScript
				encodeJavaScript(facesContext, pprGroup);
			}
		}

		// todo: in 1.2, better use a combo of
		// invokeComponent/RendererUtils.renderChildren() instead
		uiComponent.getAttributes().put(DISABLE_RENDER_CHILDREN, Boolean.FALSE);
	}

	/**
	 * Renders inline JavaScript registering an onLoad function for:
	 * <ul>
	 * <li>Initializing the PPRCtrl for the current Form</li>
	 * <li>Registering partialTriggers</li>
	 * <li>Registering partialTriggerPatterns</li>
	 * <li>Starting periodical updates</li>
	 * <li>Registering inline Loading messages</li>
	 * </ul>
	 *
	 * @param facesContext the current {@link FacesContext}
	 * @param pprGroup	 the currently rendered {@link PPRPanelGroup}
	 * @throws IOException if the underlying Layer throws an {@link IOException}
	 *                     it is passed through
	 */
	private void encodeJavaScript(FacesContext facesContext, PPRPanelGroup pprGroup) throws IOException
	{

		final ExternalContext externalContext = facesContext.getExternalContext();

		final Map requestMap = externalContext.getRequestMap();

		// Do not render the JavaScript if answering to a PPR response
		if(PPRPhaseListener.isPartialRequest(facesContext))
		{
			return;
		}

		FormInfo fi = RendererUtils.findNestingForm(pprGroup, facesContext);
		if(fi == null)
		{
			throw new FacesException("PPRPanelGroup must be embedded in a form.");
		}

		//Initialize the client side PPR engine
		if(!requestMap.containsKey(PPR_INITIALIZED))
		{
			requestMap.put(PPR_INITIALIZED, Boolean.TRUE);

			String encoding = "UTF-8"; // Hardcoded default
			if(facesContext.getResponseWriter().getCharacterEncoding() != null)
			{
				encoding = facesContext.getResponseWriter().getCharacterEncoding();
			}

			DojoConfig currentConfig = DojoUtils.getDjConfigInstance(facesContext);
			currentConfig.setBindEncoding(encoding);

			String javascriptLocation = (String) pprGroup.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
			AddResource addResource = AddResourceFactory.getInstance(facesContext);
			DojoUtils.addMainInclude(facesContext, pprGroup, javascriptLocation, currentConfig);
			DojoUtils.addRequire(facesContext, pprGroup, "dojo.io.*");
			DojoUtils.addRequire(facesContext, pprGroup, "dojo.event.*");
			DojoUtils.addRequire(facesContext, pprGroup, "dojo.xml.*");
			addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, PPRPanelGroup.class, PPR_JS_FILE);
		}

		StringBuffer script = new StringBuffer();

		// all JS is put inside a function passed to dojoOnLoad
		// this is necessary in order to be able to replace all button onClick
		// handlers

		script.append("dojo.addOnLoad( function(){ ");

		final String formName = fi.getFormName();

		String pprCtrlReference = "dojo.byId('" + formName + "').myFacesPPRCtrl";

		//Each form containing PPRPanelGroups has its own PPRCtrl
		if(!requestMap.containsKey(PPR_INITIALIZED + "." + formName))
		{
			requestMap.put(PPR_INITIALIZED + "." + formName, Boolean.TRUE);

			script.append(pprCtrlReference + "=" + MY_FACES_PPR_INIT_CODE + "('" + formName + "'," + pprGroup.getShowDebugMessages().booleanValue() + "," + pprGroup.getStateUpdate().booleanValue() + ");\n");

			if(pprGroup.getPeriodicalUpdate() != null)
			{
				script.append(pprCtrlReference + ".registerOnSubmitInterceptor();");
			}

		}

		String clientId = pprGroup.getClientId(facesContext);

		//Handle periodical updates
		if(pprGroup.getPeriodicalUpdate() != null)
		{
			String periodicalTriggers = pprGroup.getPeriodicalTriggers();
			// If no periodicalTriggers are set just start the periodical
			// update
			if(periodicalTriggers == null || periodicalTriggers.trim().length() <= 0)
			{
				Integer wait =  null;
				if(pprGroup.getExcludeFromStoppingPeriodicalUpdate() != null)
				{
					wait = pprGroup.getWaitBeforePeriodicalUpdate();
				}
				script.append(pprCtrlReference + ".startPeriodicalUpdate(" + pprGroup.getPeriodicalUpdate() + ",'" + clientId + "', " + wait + ");");
			}
			// Otherwise start it when the trigger happens
			else
			{
				List partialTriggers = (new PartialTriggerParser()).parse(periodicalTriggers);
				String periodicalTriggerId;
				String periodicalTriggerClientId;
				UIComponent periodicalTriggerComponent;
				for(int i = 0; i < partialTriggers.size(); i++)
				{
					PartialTriggerParser.PartialTrigger trigger = (PartialTriggerParser.PartialTrigger) partialTriggers
							.get(i);
					periodicalTriggerId = trigger.getPartialTriggerId();
					periodicalTriggerComponent = pprGroup.findComponent(periodicalTriggerId);
					if(periodicalTriggerComponent == null)
					{
						periodicalTriggerComponent = facesContext.getViewRoot().findComponent(periodicalTriggerId);
					}

					// Component found
					if(periodicalTriggerComponent != null)
					{
						periodicalTriggerClientId = periodicalTriggerComponent.getClientId(facesContext);
						script.append(pprCtrlReference + "." + ADD_PERIODICAL_TRIGGER_FUNCTION + "('" + periodicalTriggerClientId + "'," + encodeArray(trigger.getEventHooks()) + ",'" + clientId + "', " + pprGroup.getPeriodicalUpdate() + ");");

						// Component missing
					}
					else
					{
						if(log.isDebugEnabled())
						{
							log.debug("PPRPanelGroupRenderer Component with id " + periodicalTriggerId + " not found!");
						}
					}
				}
			}

			String idRegex = pprGroup.getExcludeFromStoppingPeriodicalUpdate();

			if(idRegex != null)
			{
				script.append(pprCtrlReference + ".excludeFromStoppingPeriodicalUpdate('" + idRegex + "');");
			}
		}

		String partialTriggerId;
		String partialTriggerClientId;
		UIComponent partialTriggerComponent;

		String partialTriggers = pprGroup.getPartialTriggers();

		String partialTriggerPattern = pprGroup.getPartialTriggerPattern();

		//handle partial trigger patterns
		if(partialTriggerPattern != null && partialTriggerPattern.trim().length() > 0)
		{
			script.append(pprCtrlReference + "." + ADD_PARTIAL_TRIGGER_PATTERN_FUNCTION + "('" + partialTriggerPattern + "','" + clientId + "');");
		}

		SubForm subFormParent = findParentSubForm(pprGroup);
		if(subFormParent != null)
		{
			script.append(pprCtrlReference + "." + SET_SUBFORM_ID_FUNCTION + "('" + subFormParent.getId() + "','" + clientId + "');");
		}

		String inlineLoadingMessage = pprGroup.getInlineLoadingMessage();

		//handle inline loading messages
		if(inlineLoadingMessage != null && inlineLoadingMessage.trim().length() > 0)
		{
			script.append(pprCtrlReference + "." + ADD_INLINE_LOADING_MESSAGE_FUNCTION + "('" + inlineLoadingMessage + "','" + clientId + "');");
		}

		//handle partial triggers
		if(partialTriggers != null && partialTriggers.trim().length() > 0)
		{
			List partialTriggerIds = (new PartialTriggerParser()).parse(partialTriggers);
			for(int i = 0; i < partialTriggerIds.size(); i++)
			{
				PartialTriggerParser.PartialTrigger trigger = (PartialTriggerParser.PartialTrigger) partialTriggerIds
						.get(i);
				partialTriggerId = trigger.getPartialTriggerId();
				partialTriggerComponent = pprGroup.findComponent(partialTriggerId);
				if(partialTriggerComponent == null)
				{
					partialTriggerComponent = facesContext.getViewRoot().findComponent(partialTriggerId);
				}
				if(partialTriggerComponent != null)
				{
					partialTriggerClientId = partialTriggerComponent.getClientId(facesContext);
					script.append(pprCtrlReference + "." + ADD_PARTIAL_TRIGGER_FUNCTION + "('" + partialTriggerClientId + "'," + encodeArray(trigger.getEventHooks()) + ",'" + clientId + "');");
				}
				else
				{
					if(log.isDebugEnabled())
					{
						log.debug("PPRPanelGroupRenderer Component with id " + partialTriggerId + " not found!");
					}
				}
			}
		}

		// closing the dojo.addOnLoad call
		script.append("});");

		//Really render the script
		renderInlineScript(facesContext, pprGroup, script.toString());
	}

	private SubForm findParentSubForm(UIComponent base)
	{
		if(base == null)
		{
			return null;
		}

		if(base instanceof SubForm)
		{
			return (SubForm) base;
		}

		return findParentSubForm(base.getParent());
	}

	private String encodeArray(List eventHooks)
	{
		if(eventHooks == null || eventHooks.size() == 0)
		{
			return "null";
		}
		else
		{
			StringBuffer buf = new StringBuffer();
			buf.append("[");

			for(int i = 0; i < eventHooks.size(); i++)
			{
				if(i > 0)
				{
					buf.append(",");
				}
				String eventHook = (String) eventHooks.get(i);
				buf.append("'");
				buf.append(eventHook);
				buf.append("'");
			}
			buf.append("]");

			return buf.toString();
		}
	}

	/**
	 * Helper to write an inline javascript at the exact resource location
	 * of the call.
	 *
	 * @param facesContext The current faces-context.
	 * @param component	The component for which the script is written.
	 * @param script	   The script to be written.
	 * @throws IOException A forwarded exception from the underlying renderer.
	 */
	private static void renderInlineScript(FacesContext facesContext, UIComponent component, String script) throws IOException
	{
		ResponseWriter writer = facesContext.getResponseWriter();
		writer.startElement(HTML.SCRIPT_ELEM, component);
		writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
		writer.write(script);
		writer.endElement(HTML.SCRIPT_ELEM);
	}
}

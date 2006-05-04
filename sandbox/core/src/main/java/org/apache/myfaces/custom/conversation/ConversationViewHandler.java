/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.conversation;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Handles: <br />
 * <ul>
 * <li>cleanup of ended conversations</li>
 * <li>reinject conversation beans</li>
 * </ul>
 * 
 * @author imario@apache.org
 */
public class ConversationViewHandler extends ViewHandler
{
	private final ViewHandler original;
	
	public ConversationViewHandler(ViewHandler original)
	{
		this.original = original;
	}

	public Locale calculateLocale(FacesContext context)
	{
		return original.calculateLocale(context);
	}

	public String calculateRenderKitId(FacesContext context)
	{
		return original.calculateRenderKitId(context);
	}

	public UIViewRoot createView(FacesContext context, String viewId)
	{
		// injectConversationBeans(context);
		
		return original.createView(context, viewId);
	}

	public boolean equals(Object obj)
	{
		return original.equals(obj);
	}

	public String getActionURL(FacesContext context, String viewId)
	{
/*		
		ConversationManager conversationManager = ConversationManager.getInstance(context);
		if (conversationManager.hasConversationContext())
		{
			StringBuffer actionUrl = new StringBuffer(original.getActionURL(context, viewId));
			if (actionUrl.indexOf("?") < 0)
			{
				actionUrl.append("?");
			}
			else
			{
				actionUrl.append("&");
			}
			actionUrl.append(ConversationManager.CONVERSATION_CONTEXT_PARAM);
			actionUrl.append("=");
			actionUrl.append(Long.toString(conversationManager.getConversationContextId().longValue(), Character.MAX_RADIX));
			return actionUrl.toString();
		}
		else
		{
*/		
			return original.getActionURL(context, viewId);
/*			
		}
*/		
	}

	public String getResourceURL(FacesContext context, String path)
	{
		return original.getResourceURL(context, path);
	}

	public int hashCode()
	{
		return original.hashCode();
	}

	public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException
	{
		endConversations(context);
		
		original.renderView(context, viewToRender);
	}

	public UIViewRoot restoreView(FacesContext context, String viewId)
	{
		// injectConversationBeans(context);
		
		return original.restoreView(context, viewId);
	}

	public String toString()
	{
		return original.toString();
	}

	public void writeState(FacesContext context) throws IOException
	{
		original.writeState(context);
	}

	/**
	 * End all conversations registered to be ended.<br />
	 * Will be called from:
	 * <ul>
	 * <li>{@link #createView(FacesContext, String)}</li>
	 * <li>{@link #restoreView(FacesContext, String)}</li>
	 * </ul> 
	 */
	protected void endConversations(FacesContext context)
	{
		ConversationManager conversationManager = ConversationManager.getInstance(context); 
		
		List registeredEndConversations = conversationManager.getRegisteredEndConversations();
		if (registeredEndConversations.size() > 0)
		{
			synchronized (registeredEndConversations)
			{
				Iterator iterRegisteredEndConversations = conversationManager.getRegisteredEndConversations().iterator();
				while (iterRegisteredEndConversations.hasNext())
				{
					String conversationName = (String) iterRegisteredEndConversations.next();
					conversationManager.endConversation(conversationName);
				}
				registeredEndConversations.clear();
			}
		}
	}
	
	/**
	 * @see ConversationManager#injectConversationBeans(FacesContext)
	 * Will be called from:
	 * <ul>
	 * <li>{@link #renderView(FacesContext, UIViewRoot)}</li>
	 * </ul>
	protected void injectConversationBeans(FacesContext context)
	{
		ConversationManager conversationManager = ConversationManager.getInstance(context);
		conversationManager.injectConversationBeans(context);
	}
	 */
}

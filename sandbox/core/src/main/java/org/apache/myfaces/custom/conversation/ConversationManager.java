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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * The manager will deal with the various contexts in the current session.
 * A new context will be created if the current window has none associated.
 *  
 * @author imario@apache.org 
 */
public class ConversationManager
{
	private final static String CONVERSATION_MANAGER_KEY = "org.apache.myfaces.ConversationManager";
	private final static String CONVERSATION_CONTEXT_PARAM = "conversationContext";
	private final static String CONVERSATION_CONTEXT_REQ = "org.apache.myfaces.ConversationManager.conversationContext";
	
	private static long NEXT_CONVERSATION_CONTEXT = 1;  

	private final Map conversationContexts = new HashMap();
	
	private final List registeredEndConversations = new ArrayList(10);
	
	protected ConversationManager()
	{
	}
	
	public static ConversationManager getInstance()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null)
		{
			throw new IllegalStateException("no faces context available");
		}
		return getInstance(context);
	}
	
	public static ConversationManager getInstance(FacesContext context)
	{
		ConversationManager conversationManager = (ConversationManager) context.getExternalContext().getSessionMap().get(CONVERSATION_MANAGER_KEY);
		if (conversationManager == null)
		{
			conversationManager = new ConversationManager();
			context.getExternalContext().getSessionMap().put(CONVERSATION_MANAGER_KEY, conversationManager);
		}
		
		return conversationManager;
	}

	protected Long getConversationContextId()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		
		Long conversationContextId = (Long) context.getExternalContext().getRequestMap().get(CONVERSATION_CONTEXT_REQ);
		if (conversationContextId == null)
		{
			if (context.getExternalContext().getRequestParameterMap().containsKey(CONVERSATION_CONTEXT_PARAM))
			{
				String urlConversationContextId = context.getExternalContext().getRequestParameterMap().get(CONVERSATION_CONTEXT_PARAM).toString();
				conversationContextId = new Long(Long.parseLong(urlConversationContextId, Character.MAX_RADIX));
			}
			else
			{
				synchronized (ConversationManager.class)
				{
					conversationContextId = new Long(NEXT_CONVERSATION_CONTEXT);
					NEXT_CONVERSATION_CONTEXT++;					
				}
			}
			
			context.getExternalContext().getRequestMap().put(CONVERSATION_CONTEXT_REQ, conversationContextId);
		}
		
		return conversationContextId;
	}
	
	protected ConversationContext getConversationContext(Long conversationContextId)
	{
		return (ConversationContext) conversationContexts.get(conversationContextId);
	}
	
	protected ConversationContext getOrCreateConversationContext(Long conversationContextId)
	{
		ConversationContext conversationContext = (ConversationContext) conversationContexts.get(conversationContextId);
		if (conversationContext == null)
		{
			conversationContext = new ConversationContext();			
			conversationContexts.put(conversationContextId, conversationContext);
		}
		
		return conversationContext;
	}
	
	protected void destroyConversationContext(Long conversationContextId)
	{
		conversationContexts.remove(conversationContextId);
	}
	
	public void startConversation(String name)
	{
		Long conversationContextId = getConversationContextId();
		ConversationContext conversationContext = getOrCreateConversationContext(conversationContextId);
		conversationContext.startConversation(name);
	}
	
	public void endConversation(String name)
	{
		Long conversationContextId = getConversationContextId();
		ConversationContext conversationContext = getConversationContext(conversationContextId);
		if (conversationContext != null)
		{
			conversationContext.endConversation(name);
			
			if (!conversationContext.hasConversation())
			{
				destroyConversationContext(conversationContextId);
			}
		}
	}

	public Conversation getConversation(String name)
	{
		Long conversationContextId = getConversationContextId();
		ConversationContext conversationContext = getConversationContext(conversationContextId);
		if (conversationContext == null)
		{
			return null;
		}
		return conversationContext.getConversation(name);
	}

	public void registerEndConversation(String conversationName)
	{
		synchronized (registeredEndConversations)
		{
			registeredEndConversations.add(conversationName);
		}
	}
	
	public List getRegisteredEndConversations()
	{
		return registeredEndConversations;
	}
}
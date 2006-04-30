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

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared_tomahawk.util.ClassUtils;

/**
 * The manager will deal with the various contexts in the current session.
 * A new context will be created if the current window has none associated.
 *  
 * @author imario@apache.org 
 */
public class ConversationManager
{
	public final static String CONVERSATION_CONTEXT_PARAM = "conversationContext";
	
	private final static String INIT_PERSISTENCE_MANAGER_FACOTRY = "org.apache.myfaces.conversation.PERSISTENCE_MANAGER_FACTORY";
	private final static String CONVERSATION_MANAGER_KEY = "org.apache.myfaces.ConversationManager";
	private final static String CONVERSATION_CONTEXT_REQ = "org.apache.myfaces.ConversationManager.conversationContext";
	
	private static long NEXT_CONVERSATION_CONTEXT = 1;  

	private PersistenceManagerFactory persistenceManagerFactory;
	
	private final Map conversationContexts = new HashMap();
	
	private final List registeredEndConversations = new ArrayList(10);
	
	protected ConversationManager()
	{
	}

	/**
	 * Get the conversation manager
	 */
	public static ConversationManager getInstance()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		if (context == null)
		{
			throw new IllegalStateException("no faces context available");
		}
		return getInstance(context);
	}
	
	/**
	 * Get the conversation manager
	 */
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

	/**
	 * Get the current, or create a new unique conversationContextId.<br />
	 * The current conversationContextId will retrieved from the request parameters, if we cant find it there
	 * a new one will be created. In either case the result will be stored within the request for faster lookup. 
	 */
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
	
	/**
	 * Get the conversation context for the given id
	 */
	protected ConversationContext getConversationContext(Long conversationContextId)
	{
		return (ConversationContext) conversationContexts.get(conversationContextId);
	}
	
	/**
	 * Get the conversation context for the given id. <br />
	 * If there is no conversation context a new one will be created
	 */
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

	/**
	 * Destroy the given conversation context
	 */
	protected void destroyConversationContext(Long conversationContextId)
	{
		conversationContexts.remove(conversationContextId);
	}
	
	/**
	 * Start a conversation
	 * @see ConversationContext#startConversation(String) 
	 */
	public void startConversation(String name, boolean persistence)
	{
		Long conversationContextId = getConversationContextId();
		ConversationContext conversationContext = getOrCreateConversationContext(conversationContextId);
		conversationContext.startConversation(name, persistence);
	}
	
	/**
	 * End a conversation
	 * @see ConversationContext#endConversation(String) 
	 */
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

	/**
	 * Get the conversation with the given name
	 * 
	 * @return null if no conversation context is active or if the conversation did not exist. 
	 */
	public Conversation getConversation(String name)
	{
		ConversationContext conversationContext = getConversationContext();
		if (conversationContext == null)
		{
			return null;
		}
		return conversationContext.getConversation(name);
	}

	/**
	 * Get the current conversation context.
	 * @return null if there is no context active
	 */
	protected ConversationContext getConversationContext()
	{
		Long conversationContextId = getConversationContextId();
		ConversationContext conversationContext = getConversationContext(conversationContextId);
		return conversationContext;
	}

	/**
	 * Register the conversation to be ended after the cycle  
	 */
	protected void registerEndConversation(String conversationName)
	{
		synchronized (registeredEndConversations)
		{
			registeredEndConversations.add(conversationName);
		}
	}

	/**
	 * Get all registered conversations
	 */
	protected List getRegisteredEndConversations()
	{
		return registeredEndConversations;
	}

	/**
	 * Inject all beans of the current conversation
	 * @see ConversationContext#injectConversationBeans(FacesContext) 
	protected void injectConversationBeans(FacesContext context)
	{
		ConversationContext conversationContext = getConversationContext();
		if (conversationContext == null)
		{
			return;
		}
		
		conversationContext.injectConversationBeans(context);
	}
	 */

	/**
	 * check if we have a conversation context
	 */
	public boolean hasConversationContext()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		
		return context.getExternalContext().getRequestMap().containsKey(CONVERSATION_CONTEXT_REQ) ||
			context.getExternalContext().getRequestParameterMap().containsKey(CONVERSATION_CONTEXT_PARAM);
	}

	/**
	 * get the persistence manager responsible for the current conversation
	 */
	public PersistenceManager getPersistenceManager()
	{
		ConversationContext conversationContext = getConversationContext();
		if (conversationContext == null)
		{
			return null;
		}
		
		return conversationContext.getPersistenceManager();
	}

	/**
	 * create a persistence manager
	 */
	protected PersistenceManager createPersistenceManager()
	{
		return getPersistenceManagerFactory().create();
	}

	/**
	 * Get the persistenceManagerFactory.<br /> 
	 * The factory can be configured in your web.xml using the init parameter named
	 * <code>org.apache.myfaces.conversation.PERSISTENCE_MANAGER_FACTORY</code>
	 */
	protected PersistenceManagerFactory getPersistenceManagerFactory()
	{
		if (persistenceManagerFactory == null)
		{
			String persistenceManagerFactoryName = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("INIT_PERSISTENCE_MANAGER_FACOTRY");
			if (persistenceManagerFactoryName == null)
			{
				throw new IllegalArgumentException("please configure '" + INIT_PERSISTENCE_MANAGER_FACOTRY + "' in your web.xml");
			}
			try
			{
				persistenceManagerFactory =  (PersistenceManagerFactory) ClassUtils.classForName(persistenceManagerFactoryName).newInstance();
			}
			catch (InstantiationException e)
			{
				throw new FacesException("error creating persistenceManagerFactory named: " + persistenceManagerFactoryName, e);
			}
			catch (IllegalAccessException e)
			{
				throw new FacesException("error creating persistenceManagerFactory named: " + persistenceManagerFactoryName, e);
			}
			catch (ClassNotFoundException e)
			{
				throw new FacesException("error creating persistenceManagerFactory named: " + persistenceManagerFactoryName, e);
			}
		}
		
		return persistenceManagerFactory;
	}
}
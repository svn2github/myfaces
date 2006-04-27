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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * The ConversationContext handles all conversations within the current context 
 *  
 * @author imario@apache.org 
 */
public class ConversationContext
{
	private final Map conversations = new TreeMap();
	private final List conversationStack = new ArrayList(10);
	private Conversation currentConversation;
	
	protected ConversationContext()
	{
	}

	/**
	 * Start a conversation if not already started.<br />
	 * All nested conversations (if any) are closed if the conversation already existed.  
	 * @param name
	 */
	public void startConversation(String name)
	{
		Conversation conversation = (Conversation) conversations.get(name);
		if (conversation == null)
		{
			conversation = new Conversation(name);
			conversations.put(name, conversation);
			conversationStack.add(conversation);
		}
		else
		{
			endNestedConversations(conversation);
		}
		currentConversation = conversation;
	}

	/**
	 * Ends all conversations nested under conversation 
	 */
	protected void endNestedConversations(Conversation conversation)
	{
		while (conversationStack.size()>0)
		{
			int index = conversationStack.size()-1;
			Conversation dependingConversation = (Conversation) conversationStack.get(index);
			if (conversation == dependingConversation)
			{
				return;
			}
			
			endConversation((Conversation) conversationStack.remove(index));			
		}
	}

	/**
	 * End the given conversation
	 */
	protected void endConversation(Conversation conversation)
	{
		conversation.endConversation();
	}

	/**
	 * End the conversation with given name.<br />
	 * This also automatically closes all nested conversations.
	 */
	public void endConversation(String name)
	{
		Conversation conversation = (Conversation) conversations.remove(name);
		if (conversation != null)
		{
			while (conversationStack.size()>0)
			{
				Conversation dependingConversation = (Conversation) conversationStack.remove(conversationStack.size()-1);
				endConversation(dependingConversation);
				if (dependingConversation == conversation)
				{
					if (conversationStack.size() > 0)
					{
						currentConversation = (Conversation) conversationStack.get(conversationStack.size()-1);
					}
					return;
				}
			}
		}
	}
	
	/**
	 * Get the current conversation. The current conversation is the one last seen by the startConversation tag.
	 * @return
	 */
	public Conversation getCurrentConversation()
	{
		return currentConversation;
	}

	/**
	 * see if there is a conversation
	 */
	public boolean hasConversation()
	{
		return conversations.size() > 0;
	}

	/**
	 * get a conversation by name 
	 */
	public Conversation getConversation(String name)
	{
		return (Conversation) conversations.get(name);
	}

	/**
	 * inject all beans from the conversation context to their configured scope 
	protected void injectConversationBeans(FacesContext context)
	{
		Set alreadyAdded = new TreeSet(new ValueBindingKey());
		
		for (int i = conversationStack.size(); i>0; i--)
		{
			Conversation conversation = (Conversation) conversationStack.get(i-1);
			Iterator iterBeans = conversation.iterateBeanEntries();
			while (iterBeans.hasNext())
			{
				Map.Entry bean = (Map.Entry) iterBeans.next();
				if (!alreadyAdded.contains(bean.getKey()))
				{
					((ValueBinding) bean.getKey()).setValue(context, bean.getValue());
				}
			}
		}
	}
	 */

	public Object findBean(String name)
	{
		for (int i = conversationStack.size(); i>0; i--)
		{
			Conversation conversation = (Conversation) conversationStack.get(i-1);
			if (conversation.hasBean(name))
			{
				return conversation.getBean(name);
			}
		}
		
		return null;
	}
}
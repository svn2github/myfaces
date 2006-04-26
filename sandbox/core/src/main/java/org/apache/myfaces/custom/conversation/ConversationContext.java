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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
			endDependingConversations(conversation);
		}
		currentConversation = conversation;
	}

	protected void endDependingConversations(Conversation conversation)
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

	protected void endConversation(Conversation conversation)
	{
		conversation.endConversation();
	}

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
	
	public Conversation getCurrentConversation()
	{
		return currentConversation;
	}

	public boolean hasConversation()
	{
		return conversations.size() > 0;
	}

	public Conversation getConversation(String name)
	{
		return (Conversation) conversations.get(name);
	}
}
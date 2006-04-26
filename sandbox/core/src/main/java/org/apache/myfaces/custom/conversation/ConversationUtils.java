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

import java.util.Iterator;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

public class ConversationUtils
{
	private ConversationUtils()
	{
	}
	
	public static UICommand findParentCommand(UIComponent base)
	{
		UIComponent parent = base;
		do
		{
			parent = parent.getParent();
			if (parent instanceof UICommand)
			{
				return (UICommand) parent;
			}
		}
		while (parent != null);
		
		return null;
	}

	public static AbstractConversationComponent findStartOrEndConversationComponent(UIComponent component, String conversationName)
	{
		Iterator iterComponents = component.getFacetsAndChildren();
		while (iterComponents.hasNext())
		{
			Object child = iterComponents.next();
			AbstractConversationComponent conversation;
			
			if (child instanceof UIStartConversation || child instanceof UIEndConversation)
			{
				conversation = (AbstractConversationComponent) child;
				if (conversation.getName().equals(conversationName))
				{
					return conversation;
				}
			}
			else if (child instanceof UIComponent)
			{
				conversation = findStartOrEndConversationComponent((UIComponent) child, conversationName);
				if (conversation != null)
				{
					return conversation;
				}
			}
		}
		
		return null;
	}
}

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
import java.util.Map;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * handle conversation related stuff like beans
 * @author imario@apache.org 
 */
public class Conversation
{
	private final String name;
	
	// private final Map beans = new TreeMap(new ValueBindingKey());
	private final Map beans = new TreeMap();
	
	protected Conversation(String name)
	{
		this.name = name;
	}

	/**
	 * Add the given valueBinding to the context map. <br/>
	 * This will also resolve the value of the binding. 
	 */
	public void putBean(FacesContext context, ValueBinding vb)
	{
		String name = ConversationUtils.extractBeanName(vb);
		if (name.indexOf('.') > -1)
		{
			throw new IllegalArgumentException("you cant put a property under conversation control. name: " + name);
		}
		if (beans.containsKey(name))
		{
			// already there
			return;
		}
		beans.put(name, vb.getValue(context));
	}

	/**
	 * the conversation name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * end this conversation <br />
	 * <ul>
	 * <li>inform all beans implementing the {@link ConversationListener} about the conversation end</li>
	 * <li>free all beans</li>
	 * </ul>
	 */
	public void endConversation()
	{
		Iterator iterBeans = beans.values().iterator();
		while (iterBeans.hasNext())
		{
			Object bean = iterBeans.next();
			if (bean instanceof ConversationListener)
			{
				((ConversationListener) bean).conversationEnded();				
			}
		}
		beans.clear();
	}

	/**
	 * Iterate all beans associated to this context
	 * 
	 * @return Iterator of {@link Map.Entry} elements
	public Iterator iterateBeanEntries()
	{
		return beans.entrySet().iterator();
	}
	 */

	public boolean hasBean(String name)
	{
		return beans.containsKey(name);
	}

	public Object getBean(String name)
	{
		return beans.get(name);
	}
}

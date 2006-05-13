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

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * base class for all the conversation components
 * @author imario@apache.org
 */
public class AbstractConversationComponent extends UIComponentBase
{
	public static final String COMPONENT_FAMILY = "javax.faces.Component";

	private String name;

	public String getFamily()
	{
		return COMPONENT_FAMILY;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] states = (Object[]) state;
		
		super.restoreState(context, states[0]);
		name = (String) states[1];
	}

	public Object saveState(FacesContext context)
	{
		return new Object[]
		{
			super.saveState(context),
			name
		};
	}

	/**
	 * the conversation name
	 */
	public String getName()
	{
		if (name!= null)
		{
			return name;
		}
		ValueBinding vb = getValueBinding("name");
		if( vb == null )
		{
			return null;
		}
		return (String) vb.getValue(getFacesContext());
	}

	/**
	 * the conversation name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}

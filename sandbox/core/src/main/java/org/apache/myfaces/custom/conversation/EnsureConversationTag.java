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
package org.apache.myfaces.custom.conversation;

import org.apache.myfaces.shared_tomahawk.el.SimpleActionMethodBinding;

import javax.faces.component.UIComponent;
import javax.faces.el.MethodBinding;

/**
 * Checks if a conversation is active, else redirects to another view
 *
 * @author imario@apache.org
 */
public class EnsureConversationTag extends AbstractConversationTag
{
	private String redirectTo;
	private String action;

	public String getComponentType()
	{
		return UIEnsureConversation.COMPONENT_TYPE;
	}

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
		UIEnsureConversation ensureConversation = (UIEnsureConversation) component;

		setStringProperty(component, "redirectTo", getRedirectTo());
		if (getAction() != null)
		{
			if (isValueReference(getAction()))
			{
				MethodBinding mb = getFacesContext().getApplication().createMethodBinding(
					getAction(), null);
				ensureConversation.setAction(mb);
			}
			else
			{
				ensureConversation.setAction(new SimpleActionMethodBinding(getAction()));
			}
		}
		else
		{
				ensureConversation.setAction(null);
		}
    }

	public String getRedirectTo()
	{
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo)
	{
		this.redirectTo = redirectTo;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}
}

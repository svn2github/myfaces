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

import javax.faces.component.UIComponent;

/**
 * Checks if a conversation is active, else redirects to another view
 * 
 * @author imario@apache.org
 */
public class EnsureConversationTag extends AbstractConversationTag
{
	private String redirectTo;

	public String getComponentType()
	{
		return UIEnsureConversation.COMPONENT_TYPE;
	}
	
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, "redirectTo", getRedirectTo());
    }

	public String getRedirectTo()
	{
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo)
	{
		this.redirectTo = redirectTo;
	}
}

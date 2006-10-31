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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * add a bean under context control
 *
 * @author imario@apache.org
 */
public class UIConversation extends AbstractConversationComponent
{
	private final static Log log = LogFactory.getLog(UIConversation.class);

	public static final String COMPONENT_TYPE = "org.apache.myfaces.Conversation";

	public void encodeBegin(FacesContext context) throws IOException
	{
		super.encodeBegin(context);

		elevateBean(context);
	}

	public void elevateBean(FacesContext context)
	{
		ValueBinding vb = getValueBinding("value");
		String name = ConversationUtils.extractBeanName(vb);

		Conversation conversation = ConversationManager.getInstance().getConversation(getName());
		if (conversation == null)
		{
			log.debug("no conversation named '" + getName() + "' running - can't elevate bean '" + name);
			return;
		}

		conversation.putBean(context, vb);

		// remove it from the other contexts
		context.getExternalContext().getRequestMap().remove(name);
		context.getExternalContext().getSessionMap().remove(name);
	}
}

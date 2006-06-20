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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * check if a conversation is active.
 * </p>
 * <p>
 * The way this is done here is sub-optimal, once we are on jsf 1.2 it should be possible to
 * check this before ANY rendering - and maybe to invoke a navigation then
 * </p>
 *
 * @author imario@apache.org
 */
public class UIEnsureConversation extends AbstractConversationComponent
{
	private final static Log log = LogFactory.getLog(UIEnsureConversation.class);

	public static final String COMPONENT_TYPE = "org.apache.myfaces.EnsureConversation";

	private String redirectTo;

	public void encodeBegin(FacesContext context) throws IOException
	{
		super.encodeBegin(context);

		checkConversation(context, getName());
	}

	public void decode(FacesContext context)
	{
		super.decode(context);

		try
		{
			checkConversation(context, getName());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] states = (Object[]) state;

		super.restoreState(context, states[0]);
		redirectTo = (String) states[1];
	}

	public Object saveState(FacesContext context)
	{
		return new Object[]
			{
				super.saveState(context),
				redirectTo
			};
	}

	protected void checkConversation(FacesContext context, String name) throws IOException
	{
		ConversationManager conversationManager = ConversationManager.getInstance();
		if (!conversationManager.hasConversation(name))
		{
			conversationManager.getMessager().setConversationNotActive(context, getName());

			Object response = context.getExternalContext().getResponse();
			if (response instanceof HttpServletResponse)
			{
				((HttpServletResponse) response).sendRedirect(getRedirectTo());

				context.responseComplete();
				return;
			}

			log.error("conversation " + getName() + " not active, but we cant redirect as " + response + " is not a HttpServletResponse");
		}
	}

	public String getRedirectTo()
	{
		if (redirectTo != null)
		{
			return redirectTo;
		}
		ValueBinding vb = getValueBinding("redirectTo");
		if (vb == null)
		{
			return null;
		}
		return (String) vb.getValue(getFacesContext());
	}

	public void setRedirectTo(String redirectTo)
	{
		this.redirectTo = redirectTo;
	}
}

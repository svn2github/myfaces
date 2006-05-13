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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.util.StringUtils;

/**
 * end a conversation
 * 
 * @author imario@apache.org
 */
public class UIEndConversation extends AbstractConversationComponent
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.EndConversation";

    private String onOutcome;
    
    private boolean inited = false;

    /*
	public static class ConversationEndAction extends AbstractConversationActionListener
	{
		public void doConversationAction(AbstractConversationComponent abstractConversationComponent)
		{
			ConversationManager.getInstance().registerEndConversation(getConversationName());
		}
	}
	*/
	
    public void encodeBegin(FacesContext context) throws IOException
	{
		super.encodeBegin(context);
		
		UICommand command = ConversationUtils.findParentCommand(this);
		if (command != null)
		{
			if (!inited)
			{
				/*
				ConversationEndAction actionListener = new ConversationEndAction();
				actionListener.setConversationName(getName());
				command.addActionListener(actionListener);
				*/
				MethodBinding original = command.getAction();
				command.setAction(new EndConversationMethodBindingFacade(getName(), getOnOutcomes(), original));
				inited = true;
			}
		}
		else
		{
			ConversationManager conversationManager = ConversationManager.getInstance();
			conversationManager.endConversation(getName());
		}
	}

	private Collection getOnOutcomes()
	{
		String onOutcome = getOnOutcome();
		if (onOutcome == null || onOutcome.trim().length() < 1)
		{
			return null;
		}
		
		return Arrays.asList(StringUtils.trim(StringUtils.splitShortString(onOutcome, ',')));
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] states = (Object[]) state;
		super.restoreState(context, states[0]);
		inited = ((Boolean) states[1]).booleanValue();
		onOutcome = (String) states[2];
	}

	public Object saveState(FacesContext context)
	{
		return new Object[]
		                  {
				super.saveState(context),
				inited?Boolean.TRUE:Boolean.FALSE,
				onOutcome
		                  };
	}

	public String getOnOutcome()
	{
		if (onOutcome!= null)
		{
			return onOutcome;
		}
		ValueBinding vb = getValueBinding("onOutcome");
		if( vb == null )
		{
			return null;
		}
		return (String) vb.getValue(getFacesContext());
	}

	public void setOnOutcome(String onOutcome)
	{
		this.onOutcome = onOutcome;
	}
}

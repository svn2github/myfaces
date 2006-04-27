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

import javax.faces.application.ViewHandler;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

/**
 * start a conversation
 * 
 * @author imario@apache.org
 */
public class UIStartConversation extends AbstractConversationComponent
{
    public final static String COMPONENT_TYPE = "org.apache.myfaces.StartConversation";
    private final static String CONVERSATION_SYSTEM_SETUP = "org.apache.myfaces.ConversationSystemSetup"; 

    private boolean inited;
    
	public static class ConversationStartAction extends AbstractConversationActionListener
	{
		public void doConversationAction(AbstractConversationComponent abstractConversationComponent)
		{
			ConversationManager conversationManager = ConversationManager.getInstance();
			conversationManager.startConversation(getConversationName());
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		super.encodeBegin(context);
		
		setupConversationSystem(context);
		
		UICommand command = ConversationUtils.findParentCommand(this);
		if (command != null)
		{
			if (!inited)
			{
				ConversationStartAction actionListener = new ConversationStartAction();
				actionListener.setConversationName(getName());
				command.addActionListener(actionListener);
				inited = true;
			}
		}
		else
		{
			ConversationManager conversationManager = ConversationManager.getInstance();
			conversationManager.startConversation(getName());
		}
	}
	
	protected void setupConversationSystem(FacesContext context)
	{
		if (Boolean.TRUE.equals(context.getExternalContext().getApplicationMap().get(CONVERSATION_SYSTEM_SETUP)))
		{
			return;
		}
		
		ViewHandler originalVH = context.getApplication().getViewHandler();
		context.getApplication().setViewHandler(new ConversationViewHandler(originalVH));

		VariableResolver originalVR = context.getApplication().getVariableResolver();
		context.getApplication().setVariableResolver(new ConversationVariableResolver(originalVR));
		
		context.getExternalContext().getApplicationMap().put(CONVERSATION_SYSTEM_SETUP, Boolean.TRUE);
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] states = (Object[]) state;
		super.restoreState(context, states[0]);
		inited = ((Boolean) states[1]).booleanValue();
	}

	public Object saveState(FacesContext context)
	{
		return new Object[]
		                  {
				super.saveState(context),
				inited?Boolean.TRUE:Boolean.FALSE
		                  };
	}
}

/*
 * Copyright 2005-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.valueChangeNotifier;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

/**
 * receives valueChange events and pass them to the manager
 * 
 * @author Mario Ivankovits <imario - at - apache.org> (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValueChangeCollector implements ValueChangeListener,
		StateHolder
{
	private String method = null;
	private boolean isTransient = false;
	
	public ValueChangeCollector()
	{
	}

	protected ValueChangeCollector(String method)
	{
		this.method = method;
	}

	/**
	 * This it the valueChange sink<br />
	 * The received event will be cloned and collected by the manager.  
	 */
	public void processValueChange(ValueChangeEvent event)
			throws AbortProcessingException
	{
		ValueChangeEvent clonedEvent = new ValueChangeEvent(event
				.getComponent(), event.getOldValue(), event.getNewValue());

		ValueChangeManager manager = ValueChangeManager.getManager(FacesContext
				.getCurrentInstance());
		manager.addEvent(method, clonedEvent);
	}

	public Object saveState(FacesContext context)
	{
		return new Object[]
		{ this.method};
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] o = (Object[]) state;
		
		this.method = (String) o[0];
	}

	public boolean isTransient()
	{
		return isTransient;
	}

	public void setTransient(boolean isTransient)
	{
		this.isTransient = isTransient;
	}
}
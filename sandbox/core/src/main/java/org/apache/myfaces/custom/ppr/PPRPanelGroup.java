/**
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.ppr;

import org.apache.myfaces.component.html.ext.HtmlPanelGroup;

import javax.faces.context.FacesContext;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroup extends HtmlPanelGroup
{
	public static final String COMPONENT_TYPE = "org.apache.myfaces.PPRPanelGroup";
	public static final String COMPONENT_FAMILY = "org.apache.myfaces.PPRPanelGroup";
	public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.PPRPanelGroup";

	private String _partialTriggers;

	public PPRPanelGroup()
	{
		setRendererType(DEFAULT_RENDERER_TYPE);
	}

	public String getFamily()
	{
		return COMPONENT_FAMILY;
	}

	public String getPartialTriggers()
	{
		return _partialTriggers;
	}

	public void setPartialTriggers(String partialTriggers)
	{
		this._partialTriggers = partialTriggers;
	}

	public void restoreState(FacesContext context, Object state)
	{

		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		_partialTriggers = (String) values[1];

	}

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = _partialTriggers;
		return values;
	}
}

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

import org.apache.myfaces.taglib.html.ext.HtmlPanelGroupTag;

import javax.faces.component.UIComponent;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroupTag extends HtmlPanelGroupTag
{
	private String _partialTriggers;
	
	private String _partialTriggerPattern;
	
	private String _inlineLoadingMessage;

	public String getComponentType()
	{
		return PPRPanelGroup.COMPONENT_TYPE;
	}

	public String getRendererType()
	{
		return PPRPanelGroup.DEFAULT_RENDERER_TYPE;
	}

	public void release()
	{
		super.release();
		_partialTriggers = null;
	}

	protected void setProperties(UIComponent component)
	{
		super.setProperties(component);

		setStringProperty(component, "partialTriggers", _partialTriggers);
		setStringProperty(component, "partialTriggerPattern", _partialTriggerPattern);
		setStringProperty(component, "inlineLoadingMessage", _inlineLoadingMessage);
	}

	public String getPartialTriggers()
	{
		return _partialTriggers;
	}

	public void setPartialTriggers(String partialTriggers)
	{
		this._partialTriggers = partialTriggers;
	}

	public String getPartialTriggerPattern() {
		return _partialTriggerPattern;
	}

	public void setPartialTriggerPattern(String triggerPattern) {
		_partialTriggerPattern = triggerPattern;
	}

	public String getInlineLoadingMessage() {
		return _inlineLoadingMessage;
	}

	public void setInlineLoadingMessage(String loadingMessage) {
		_inlineLoadingMessage = loadingMessage;
	}
	
	

}

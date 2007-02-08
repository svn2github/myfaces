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

    private String _periodicalUpdate;

    private String _showDebugMessages;

    private String _stateUpdate;

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
        _periodicalUpdate = null;
        _showDebugMessages = null;
        _stateUpdate = null;
    }

	protected void setProperties(UIComponent component)
	{
		super.setProperties(component);

		setStringProperty(component, "partialTriggers", _partialTriggers);
		setStringProperty(component, "partialTriggerPattern", _partialTriggerPattern);
		setStringProperty(component, "inlineLoadingMessage", _inlineLoadingMessage);
        setIntegerProperty(component,"periodicalUpdate", _periodicalUpdate);
        setBooleanProperty(component, "showDebugMessages", _showDebugMessages);
        setBooleanProperty(component, "stateUpdate", _stateUpdate);
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

    public void setPeriodicalUpdate(String periodicalUpdate)
    {
        _periodicalUpdate = periodicalUpdate;
    }

    public void setShowDebugMessages(String showDebugMessages)
    {
        _showDebugMessages = showDebugMessages;
    }

    public void setStateUpdate(String stateUpdate)
    {
        _stateUpdate = stateUpdate;
    }
}

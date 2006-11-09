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

import org.apache.myfaces.component.html.ext.HtmlPanelGroup;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Ernst Fastl
 */
public class PPRPanelGroup extends HtmlPanelGroup
{
	public static final String COMPONENT_TYPE = "org.apache.myfaces.PPRPanelGroup";
	public static final String COMPONENT_FAMILY = "org.apache.myfaces.PPRPanelGroup";
	public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.PPRPanelGroup";

	private String _partialTriggers;
    private Integer _periodicalUpdate;

    private String _partialTriggerPattern;
	
	private String _inlineLoadingMessage;

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
		if (_partialTriggers != null) return _partialTriggers;
        ValueBinding vb = getValueBinding("partialTriggers");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setPartialTriggers(String partialTriggers)
	{
		this._partialTriggers = partialTriggers;
	}

    public Integer getPeriodicalUpdate() 
    {
        if (_periodicalUpdate != null) return _periodicalUpdate;
        ValueBinding vb = getValueBinding("periodicalUpdate");
        return (vb != null) ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setPeriodicalUpdate(Integer periodicalUpdate)
    {
        _periodicalUpdate = periodicalUpdate;
    }

    public String getPartialTriggerPattern()
	{
		if (_partialTriggerPattern != null) return _partialTriggerPattern;
        ValueBinding vb = getValueBinding("partialTriggerPattern");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setPartialTriggerPattern(String partialTriggerPattern)
	{
		this._partialTriggerPattern = partialTriggerPattern;
	}
	
	public String getInlineLoadingMessage()
	{
		if (_inlineLoadingMessage != null) return _inlineLoadingMessage;
        ValueBinding vb = getValueBinding("inlineLoadingMessage");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setInlineLoadingMessage(String inlineLoadingMessage)
	{
		this._inlineLoadingMessage = inlineLoadingMessage;
	}

	public void restoreState(FacesContext context, Object state)
	{

		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		_partialTriggers = (String) values[1];
		_partialTriggerPattern = (String) values[2];
        _periodicalUpdate = (Integer) values[3];

    }

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[4];
		values[0] = super.saveState(context);
		values[1] = _partialTriggers;
		values[2] = _partialTriggerPattern;
        values[3] = _periodicalUpdate;
        return values;
	}
}

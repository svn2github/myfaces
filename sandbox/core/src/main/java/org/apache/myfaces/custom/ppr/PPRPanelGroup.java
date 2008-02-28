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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIComponent;

import org.apache.myfaces.component.html.ext.HtmlPanelGroup;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.custom.subform.SubForm;

import java.util.List;
import java.util.Collections;

/**
 * AJAX component which supports updating its children via AJAX calls. These
 * updates can occur regularly or based on triggering input components.
 *
 * @author Ernst Fastl
 */
public class PPRPanelGroup extends HtmlPanelGroup
{
	public static final String COMPONENT_TYPE = "org.apache.myfaces.PPRPanelGroup";

	public static final String COMPONENT_FAMILY = "org.apache.myfaces.PPRPanelGroup";

	public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.PPRPanelGroup";

	private String _partialTriggers;

	private Integer _periodicalUpdate;

	private String _periodicalTriggers;

	private String _excludeFromStoppingPeriodicalUpdate;

	private Integer _waitBeforePeriodicalUpdate = new Integer(2000);

	private String _partialTriggerPattern;

	private String _inlineLoadingMessage;

	private Boolean _showDebugMessages = new Boolean(false);

	private Boolean _stateUpdate = new Boolean(true);

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
		if(_partialTriggers != null)
		{
			return _partialTriggers;
		}
		ValueBinding vb = getValueBinding("partialTriggers");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setPartialTriggers(String partialTriggers)
	{
		this._partialTriggers = partialTriggers;
	}

	public Integer getPeriodicalUpdate()
	{
		if(_periodicalUpdate != null)
		{
			return _periodicalUpdate;
		}
		ValueBinding vb = getValueBinding("periodicalUpdate");
		return (vb != null) ? (Integer) vb.getValue(getFacesContext()) : null;
	}

	public void setPeriodicalUpdate(Integer periodicalUpdate)
	{
		_periodicalUpdate = periodicalUpdate;
	}

	public String getPeriodicalTriggers()
	{
		if(_periodicalTriggers != null)
		{
			return _periodicalTriggers;
		}
		ValueBinding vb = getValueBinding("periodicalTriggers");
		return (vb != null) ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setPeriodicalTriggers(String periodicalTriggers)
	{
		_periodicalTriggers = periodicalTriggers;
	}

	public String getPartialTriggerPattern()
	{
		if(_partialTriggerPattern != null)
		{
			return _partialTriggerPattern;
		}
		ValueBinding vb = getValueBinding("partialTriggerPattern");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setPartialTriggerPattern(String partialTriggerPattern)
	{
		this._partialTriggerPattern = partialTriggerPattern;
	}

	public String getExcludeFromStoppingPeriodicalUpdate()
	{
		if(_excludeFromStoppingPeriodicalUpdate != null)
		{
			return _excludeFromStoppingPeriodicalUpdate;
		}
		ValueBinding vb = getValueBinding("excludeFromStoppingPeriodicalUpdate");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setExcludeFromStoppingPeriodicalUpdate(String excludeFromStoppingPeriodicalUpdate)
	{
		_excludeFromStoppingPeriodicalUpdate = excludeFromStoppingPeriodicalUpdate;
	}

	public Integer getWaitBeforePeriodicalUpdate()
	{
		if(_waitBeforePeriodicalUpdate != null)
		{
			return _waitBeforePeriodicalUpdate;
		}
		ValueBinding vb = getValueBinding("waitBeforePeriodicalUpdate");
		return (vb != null) ? (Integer) vb.getValue(getFacesContext()) : null;
	}

	public void setWaitBeforePeriodicalUpdate(Integer waitBeforePeriodicalUpdate)
	{
		_waitBeforePeriodicalUpdate = waitBeforePeriodicalUpdate;
	}

	public String getInlineLoadingMessage()
	{
		if(_inlineLoadingMessage != null)
		{
			return _inlineLoadingMessage;
		}
		ValueBinding vb = getValueBinding("inlineLoadingMessage");
		return vb != null ? RendererUtils.getStringValue(getFacesContext(), vb) : null;
	}

	public void setInlineLoadingMessage(String inlineLoadingMessage)
	{
		this._inlineLoadingMessage = inlineLoadingMessage;
	}

	public Boolean getShowDebugMessages()
	{
		if(_showDebugMessages != null)
		{
			return _showDebugMessages;
		}
		ValueBinding vb = getValueBinding("showDebugMessages");
		return vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
	}

	public void setShowDebugMessages(Boolean showDebugMessages)
	{
		_showDebugMessages = showDebugMessages;
	}

	public Boolean getStateUpdate()
	{
		if(_stateUpdate != null)
		{
			return _stateUpdate;
		}
		ValueBinding vb = getValueBinding("stateUpdate");
		return vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
	}

	public void setStateUpdate(Boolean stateUpdate)
	{
		_stateUpdate = stateUpdate;
	}

	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		_partialTriggers = (String) values[1];
		_partialTriggerPattern = (String) values[2];
		_periodicalUpdate = (Integer) values[3];
		_periodicalTriggers = (String) values[4];
		_showDebugMessages = (Boolean) values[5];
		_stateUpdate = (Boolean) values[6];
		_excludeFromStoppingPeriodicalUpdate = (String) values[7];
		_waitBeforePeriodicalUpdate = (Integer) values[8];
	}

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[9];
		values[0] = super.saveState(context);
		values[1] = _partialTriggers;
		values[2] = _partialTriggerPattern;
		values[3] = _periodicalUpdate;
		values[4] = _periodicalTriggers;
		values[5] = _showDebugMessages;
		values[6] = _stateUpdate;
		values[7] = _excludeFromStoppingPeriodicalUpdate;
		values[8] = _waitBeforePeriodicalUpdate;
		return values;
	}


    /**
     * @return {@link PartialTriggerParser.PartialTrigger}
     */
    public List parsePartialTriggers () {
        List list;
            String partialTriggers = getPartialTriggers();
            //handle partial triggers
            if(partialTriggers != null && partialTriggers.trim().length() > 0) {
                list = (new PartialTriggerParser()).parse(partialTriggers);
            } else {
                list = Collections.emptyList();
            }
        return list;
    }

    /**
     * @return {@link PartialTriggerParser.PartialTrigger}
     */
    public List parsePeriodicalTriggers() {
        List list;
        String periodicalTriggers = getPeriodicalTriggers();
        if(periodicalTriggers != null && periodicalTriggers.trim().length() <= 0) {
            list = (new PartialTriggerParser()).parse(periodicalTriggers);
        } else {
            list = Collections.emptyList();
        }
        return list;
    }

}

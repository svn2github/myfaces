/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.myfaces.custom.rssticker;

import javax.faces.component.UIComponent;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.taglib.html.HtmlOutputTextTagBase;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlRssTickerTag extends HtmlOutputTextTagBase{

	public String getComponentType()
	{
		return HtmlRssTicker.COMPONENT_TYPE;
	}

	public String getRendererType()
	{
		return HtmlRssTickerRenderer.RENDERER_TYPE;
	}

	//Ticker attribute
	private String _rssUrl = null;

	// User Role support
	private String _enabledOnUserRole;
	private String _visibleOnUserRole;

    public void release() {
        super.release();

    	_rssUrl=null;
    	_enabledOnUserRole=null;
    	_visibleOnUserRole=null;

    }

	/**
	 * overrides setProperties() form UIComponentTag.
	 */
	protected void setProperties(UIComponent component)
	{
		super.setProperties(component);

		setStringProperty(component, "rssUrl", _rssUrl);
		setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
		setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
	}


   //---------------------------------------------only the Setters

	public void setRssUrl(String string) {
		_rssUrl = string;
	}

	public void setEnabledOnUserRole(String string) {
		_enabledOnUserRole = string;
	}

	public void setVisibleOnUserRole(String string) {
		_visibleOnUserRole = string;
	}
}

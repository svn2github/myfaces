/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.custom.datascroller;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ScrollerActionEvent extends ActionEvent
{
	private static final long serialVersionUID = -5692343289423906802L;

	private final String mScrollerfacet;

	private final int mPageIndex;

	/**
	 * @param component
	 * @param scrollerfacet
	 */
	public ScrollerActionEvent(UIComponent component, String scrollerfacet)
	{
		super(component);
		mScrollerfacet = scrollerfacet;
		mPageIndex = -1;
	}

	/**
	 *
	 */
	public ScrollerActionEvent(UIComponent component, int pageIndex)
	{
		super(component);
		if (pageIndex < 0)
		{
			throw new IllegalArgumentException("wrong pageindex");
		}
		mPageIndex = pageIndex;
		mScrollerfacet = null;
	}

	/**
	 * @return Returns the scrollerfacet.
	 */
	public String getScrollerfacet()
	{
		return mScrollerfacet;
	}

	/**
	 * @return int
	 */
	public int getPageIndex()
	{
		return mPageIndex;
	}
}

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

package org.apache.myfaces.custom.util;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;
import java.util.Iterator;

/**
 * User: treeder
 * Date: Nov 21, 2005
 * Time: 9:20:14 PM
 */
public final class ComponentUtils
{

	private ComponentUtils(){

	}

    /**
     * TR- This was moved from AjaxPhaseListenere on checkin 344383
     *
     * @param context
     * @param root
     * @param clientId
     * @return component referenced by clientId or null if not found
     */
    public static UIComponent findComponentByClientId(FacesContext context, UIComponent root, String clientId)
    {
        UIComponent component = null;
        for (int i = 0; i < root.getChildCount() && component == null; i++)
        {
            UIComponent child = (UIComponent) root.getChildren().get(i);
            component = findComponentByClientId(context, child, clientId);
        }
        if (root.getId() != null)
        {
            if (component == null && root.getClientId(context).equals(clientId))
            {
                component = root;
            }
        }
        return component;
    }

    /**
     * Useful if you don't know the clientId
     * <p/>
     * TR- This was moved from AjaxPhaseListenere on checkin 344383
     * Seems like this could be made more efficient
     *
     * @param context
     * @param root
     * @param id
     * @return component referenced by id or null if not found
     */
    public static UIComponent findComponentById(FacesContext context, UIComponent root, String id)
    {
        UIComponent component = null;
        for (int i = 0; i < root.getChildCount() && component == null; i++)
        {
            UIComponent child = (UIComponent) root.getChildren().get(i);
            component = findComponentById(context, child, id);
        }
        //System.out.println("component looking for: " + id + " - rootid: " + root.getId() + " " + root);
        if (root.getId() != null)
        {
            if (component == null && root.getId().equals(id))
            {
                component = root;
            }
        }
        return component;
    }


	/**
	 * deep scan the tree and see if ANY naming container has a component with the
	 * given id
	 */
	public static UIComponent findDeepComponentById(UIComponent base, String id)
	{
		if (id.equals(base.getId()))
		{
			return base;
		}

		Iterator iter = base.getFacetsAndChildren();
		while (iter.hasNext())
		{
			UIComponent child = (UIComponent) iter.next();

			UIComponent found = findDeepComponentById(child, id);
			if (found != null)
			{
				return found;
			}
		}

		return null;
	}

	public static UIComponent findFirstMessagesComponent(FacesContext context, UIComponent base)
    {
        if (base == null)
        {
            return null;
        }

        if (base instanceof HtmlMessages)
        {
            return base;
        }

        Iterator iterChildren = base.getFacetsAndChildren();
        while (iterChildren.hasNext())
        {
            UIComponent child = (UIComponent) iterChildren.next();

            UIComponent found = findFirstMessagesComponent(context, child);
            if (found != null)
            {
                return found;
            }
        }

        return null;
    }


}

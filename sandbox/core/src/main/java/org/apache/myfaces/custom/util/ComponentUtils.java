/*
 * Copyright 2004-2006 The Apache Software Foundation.
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

package org.apache.myfaces.custom.util;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;

/**
 * User: treeder
 * Date: Nov 21, 2005
 * Time: 9:20:14 PM
 */
public class ComponentUtils
{
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

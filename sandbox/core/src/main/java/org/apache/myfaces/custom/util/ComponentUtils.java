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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * User: treeder
 * Date: Nov 21, 2005
 * Time: 9:20:14 PM
 */
public class ComponentUtils
{
    private static Log log = LogFactory.getLog(ComponentUtils.class);

     /**
     * TR- This was moved from AjaxPhaseListenere on checkin 344383
     *
     * @param context
     * @param root
     * @param clientId
     * @return component referenced by clientId or null if not found
     */
    public static UIComponent findComponent(FacesContext context, UIComponent root, String clientId)
    {
        UIComponent component = null;
        for(int i = 0; i < root.getChildCount() && component == null; i++)
        {
            UIComponent child = (UIComponent)root.getChildren().get(i);
            component = findComponent(context, child, clientId);
        }

        if(component == null && root.getClientId(context).equals(clientId))
        {
            component = root;
        }
        return component;
    }
}

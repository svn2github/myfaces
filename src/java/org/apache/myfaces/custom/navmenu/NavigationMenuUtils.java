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
package org.apache.myfaces.custom.navmenu;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.myfaces.component.UserRoleUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationMenuUtils
{
    private static final Log log = LogFactory.getLog(NavigationMenuUtils.class);

    public static List getNavigationMenuItemList(UIComponent uiComponent)
    {
        List list = new ArrayList(uiComponent.getChildCount());
        for (Iterator children = uiComponent.getChildren().iterator(); children.hasNext(); )
        {
            UIComponent child = (UIComponent)children.next();
            if (child instanceof UINavigationMenuItem)
            {
                NavigationMenuItem item;
                Object value = ((UINavigationMenuItem)child).getValue();
                if (value != null)
                {
                    //get NavigationMenuItem from model via value binding
                    if (!(value instanceof NavigationMenuItem))
                    {
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        throw new IllegalArgumentException("Value binding of UINavigationMenuItem with id " + child.getClientId(facesContext) + " does not reference an Object of type NavigationMenuItem");
                    }
                    item = (NavigationMenuItem)value;
                }
                else
                {
                    UINavigationMenuItem uiItem = (UINavigationMenuItem)child;
                    String label = uiItem.getItemLabel();
                    if (label == null && uiItem.getItemValue() != null)
                    {
                        label = uiItem.getItemValue().toString();
                    }
                    item = new NavigationMenuItem(uiItem.getItemValue(),
                                                  label,
                                                  uiItem.getItemDescription(),
                                                  uiItem.isItemDisabled() || ! UserRoleUtils.isEnabledOnUserRole(uiItem),
                                                  uiItem.isRendered(),
                                                  uiItem.getAction(),
                                                  uiItem.getIcon(),
                                                  uiItem.isSplit());
                }
                list.add(item);
                if (child.getChildCount() > 0)
                {
                    List l = getNavigationMenuItemList(child);
                    item.setNavigationMenuItems((NavigationMenuItem[]) l.toArray(new NavigationMenuItem[l.size()]));
                }
            }
            else if (child instanceof UISelectItems)
            {
                Object value = ((UISelectItems)child).getValue();
                if (value instanceof NavigationMenuItem)
                {
                    list.add(value);
                }
                else if (value instanceof NavigationMenuItem[])
                {
                    for (int i = 0; i < ((NavigationMenuItem[])value).length; i++)
                    {
                        list.add(((NavigationMenuItem[])value)[i]);
                    }
                }
                else if (value instanceof Collection)
                {
                    for (Iterator it = ((Collection)value).iterator(); it.hasNext();)
                    {
                        Object item = it.next();
                        if (!(item instanceof NavigationMenuItem))
                        {
                            FacesContext facesContext = FacesContext.getCurrentInstance();
                            throw new IllegalArgumentException("Collection referenced by UINavigationMenuItems with id " + child.getClientId(facesContext) + " does not contain Objects of type NavigationMenuItem");
                        }
                        list.add(item);
                    }
                }
                else
                {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    throw new IllegalArgumentException("Value binding of UINavigationMenuItems with id " + child.getClientId(facesContext) + " does not reference an Object of type NavigationMenuItem, NavigationMenuItem[], Collection or Map");
                }
            }
            else
            {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                log.error("Invalid child with id " + child.getClientId(facesContext) + "of component with id : "+uiComponent.getClientId(facesContext)
                        +" : must be UINavigationMenuItem or UINavigationMenuItems, is of type : "+((child==null)?"null":child.getClass().getName()));
            }
        }

        return list;
    }

}

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

import javax.faces.model.SelectItem;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationMenuItem
    extends SelectItem
{
    private String _icon;
    private String _action;
    boolean _split;
    private boolean rendered = true;
    private NavigationMenuItem[] _navigationMenuItems = null;

    public NavigationMenuItem(String label, String action, String icon, boolean split)
    {
        super(label, label);
        _action = action;
        _icon = icon;
        _split = split;
    }

    public NavigationMenuItem(Object value,
                              String label,
                              String description,
                              boolean disabled,
                              String action,
                              String icon,
                              boolean split)
    {
        super(value, label, description, disabled);
        _action = action;
        _icon = icon;
        _split = split;
    }

    public NavigationMenuItem(Object value,
                              String label,
                              String description,
                              boolean disabled,
                              boolean rendered,
                              String action,
                              String icon,
                              boolean split)
    {
        this(value, label, description, disabled, action, icon, split);
        this.rendered = rendered;
    }


    public boolean isRendered()
    {
        return rendered;
    }


    public String getAction()
    {
        return _action;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public boolean isSplit()
    {
        return _split;
    }

    public void setSplit(boolean split)
    {
        _split = split;
    }

    public String getIcon()
    {
        return _icon;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public NavigationMenuItem[] getNavigationMenuItems()
    {
        return _navigationMenuItems;
    }

    public void setNavigationMenuItems(NavigationMenuItem[] navigationMenuItems)
    {
        _navigationMenuItems = navigationMenuItems;
    }
}

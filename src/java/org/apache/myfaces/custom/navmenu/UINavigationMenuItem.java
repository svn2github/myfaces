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

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.UserRoleAware;

import javax.faces.component.UISelectItem;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigationMenuItem
    extends UISelectItem
    implements UserRoleAware
{


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.NavigationMenuItem";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItem";

    private String _icon = null;
    private Boolean _split = null;
    private String _action = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    public UINavigationMenuItem()
    {
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setIcon(String icon)
    {
        _icon = icon;
    }

    public String getIcon()
    {
        if (_icon != null) return _icon;
        ValueBinding vb = getValueBinding("icon");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSplit(boolean split)
    {
        _split = Boolean.valueOf(split);
    }

    public boolean isSplit()
    {
        if (_split != null) return _split.booleanValue();
        ValueBinding vb = getValueBinding("split");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setAction(String action)
    {
        _action = action;
    }

    public String getAction()
    {
        if (_action != null) return _action;
        ValueBinding vb = getValueBinding("action");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = _icon;
        values[2] = _split;
        values[3] = _action;
        values[4] = _enabledOnUserRole;
        values[5] = _visibleOnUserRole;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _icon = (String)values[1];
        _split = (Boolean)values[2];
        _action = (String)values[3];
        _enabledOnUserRole = (String)values[4];
        _visibleOnUserRole = (String)values[5];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

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

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.util._ComponentUtils;

import javax.faces.component.ActionSource;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UINavigationMenuItem extends UISelectItem implements UserRoleAware, ActionSource
{
    private static final boolean DEFAULT_IMMEDIATE = true;

    public static final String COMPONENT_TYPE = "org.apache.myfaces.NavigationMenuItem";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItem";

    private String _icon = null;
    private Boolean _split = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    private Boolean _open = null;
    private Boolean _active = null;
    private MethodBinding _action = null;
    private MethodBinding _actionListener = null;
    private Boolean _immediate = null;

    public UINavigationMenuItem()
    {
        super();
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
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
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
        return v != null && v.booleanValue();
    }

    public void setOpen(boolean open)
    {
        _open = Boolean.valueOf(open);
    }

    public boolean isOpen()
    {
        if (_open != null) return _open.booleanValue();
        ValueBinding vb = getValueBinding("open");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null && v.booleanValue();
    }

    public void setActive(boolean active)
    {
        _active = Boolean.valueOf(active);
    }

    public boolean isActive()
    {
        if (_active != null) return _active.booleanValue();
        ValueBinding vb = getValueBinding("active");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null && v.booleanValue();
    }

    public void setImmediate(boolean immediate)
    {
        _immediate = Boolean.valueOf(immediate);
    }

    public boolean isImmediate()
    {
        if (_immediate != null) return _immediate.booleanValue();
        ValueBinding vb = getValueBinding("immediate");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_IMMEDIATE;
    }

    // Action Source

    public void setAction(MethodBinding action)
    {
        _action = action;
    }

    public MethodBinding getAction()
    {
        return _action;
    }

    public void setActionListener(MethodBinding actionListener)
    {
        _actionListener = actionListener;
    }

    public MethodBinding getActionListener()
    {
        return _actionListener;
    }

    public void addActionListener(ActionListener listener)
    {
        addFacesListener(listener);
    }

    public ActionListener[] getActionListeners()
    {
        return (ActionListener[])getFacesListeners(ActionListener.class);
    }

    public void removeActionListener(ActionListener listener)
    {
        removeFacesListener(listener);
    }
    // Action Source

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null) return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null) return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[10];
        values[0] = super.saveState(context);
        values[1] = _icon;
        values[2] = _split;
        values[3] = saveAttachedState(context, _action);
        values[4] = _enabledOnUserRole;
        values[5] = _visibleOnUserRole;
        values[6] = _open;
        values[7] = _active;
        values[8] = saveAttachedState(context, _actionListener);
        values[9] = _immediate;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _icon = (String)values[1];
        _split = (Boolean)values[2];
        _action = (MethodBinding)restoreAttachedState(context, values[3]);
        _enabledOnUserRole = (String)values[4];
        _visibleOnUserRole = (String)values[5];
        _open = (Boolean)values[6];
        _active = (Boolean)values[7];
        _actionListener = (MethodBinding)restoreAttachedState(context, values[8]);
        _immediate = (Boolean)values[9];
    }
}

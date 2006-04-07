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

package org.apache.myfaces.custom.schedule;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 *
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$
 */
public class HtmlPlanner extends UIPlanner implements UserRoleAware
{

    //  ------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    private static final long serialVersionUID = 4985214318279622233L;
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    public static final String COMPONENT_TYPE = "org.apache.myfaces.Planner";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Planner";

    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;

    public HtmlPlanner()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#getEnabledOnUserRole()
     */
    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null)
            return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(),
                vb) : null;
    }

    /**
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#getVisibleOnUserRole()
     */
    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null)
            return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(),
                vb) : null;
    }

    /**
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _enabledOnUserRole = (String) values[1];
        _visibleOnUserRole = (String) values[2];
    }
    //  ------------------ GENERATED CODE END ---------------------------------------

    /**
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _enabledOnUserRole;
        values[2] = _visibleOnUserRole;
        return ((Object) (values));
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#setEnabledOnUserRole(java.lang.String)
     */
    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    /**
     * @see org.apache.myfaces.component.UserRoleAware#setVisibleOnUserRole(java.lang.String)
     */
    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

}
//The End

/**
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.statechangednotifier;

import org.apache.myfaces.util._ComponentUtils;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Shows a confirmation window if some of the input fields of the form have changed its value
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateChangedNotifier extends HtmlInputHidden
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.StateChangedNotifier";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.StateChangedNotifierRenderer";

    private static final String DEFAULT_MESSAGE = "Are you sure?";

    private String confirmationMessage = DEFAULT_MESSAGE;
    private Boolean disabled;
    private String excludeCommandsWithClientIds = null;

    public StateChangedNotifier()
    {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[4];
        values[0] = super.saveState(context);
        values[1] = confirmationMessage;
        values[2] = disabled;
        values[3] = excludeCommandsWithClientIds;

        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        this.confirmationMessage = (String) values[1];
        this.disabled = (Boolean) values[2];
        this.excludeCommandsWithClientIds = (String) values[3];
    }

    public String getConfirmationMessage()
    {
         if (confirmationMessage != null) return confirmationMessage;
        ValueBinding vb = getValueBinding("confirmationMessage");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setConfirmationMessage(String confirmationMessage)
    {
        this.confirmationMessage = confirmationMessage;
    }

    public Boolean getDisabled()
    {
        if (disabled != null) return disabled;
        ValueBinding vb = getValueBinding("disabled");
        return vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
    }

    public void setDisabled(Boolean disabled)
    {
        this.disabled = disabled;
    }

    public String getExcludeCommandsWithClientIds()
    {
        if (excludeCommandsWithClientIds != null) return excludeCommandsWithClientIds;
        ValueBinding vb = getValueBinding("excludeCommandsWithClientIds");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setExcludeCommandsWithClientIds(String excludeCommandsWithClientIds)
    {
        this.excludeCommandsWithClientIds = excludeCommandsWithClientIds;
    }
}


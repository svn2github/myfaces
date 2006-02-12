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

import org.apache.myfaces.taglib.html.HtmlInputHiddenTag;

import javax.faces.component.UIComponent;

/**
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateChangedNotifierTag extends HtmlInputHiddenTag
{
    private String confirmationMessage;
    private String disabled;
    private String excludeCommandsWithClientIds;

    public String getComponentType()
    {
        return StateChangedNotifier.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return StateChangedNotifier.DEFAULT_RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
        confirmationMessage = null;
        disabled = null;
        excludeCommandsWithClientIds = null;
    }

    protected void setProperties(UIComponent component)
    {

        super.setProperties(component);

        setStringProperty(component,"confirmationMessage",confirmationMessage);
        setBooleanProperty(component, "disabled", disabled);
        setStringProperty(component, "excludeCommandsWithClientIds", excludeCommandsWithClientIds);

    }

    public void setConfirmationMessage(String confirmationMessage)
    {
        this.confirmationMessage = confirmationMessage;
    }

    public void setDisabled(String disabled)
    {
        this.disabled = disabled;
    }

    public void setExcludeCommandsWithClientIds(String excludeCommandsWithClientIds)
    {
        this.excludeCommandsWithClientIds = excludeCommandsWithClientIds;
    }
}

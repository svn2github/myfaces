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
package org.apache.myfaces.custom.toggle;

import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;

/**
 * Should be nested within an HtmlToggleGroup component. Controls nested within
 * this component will be displayed in 'view' mode, controls outside this
 * component (within the parent HtmlToggleGroup) will be displayed in 'edit'
 * mode.
 * 
 * @author Sharath Reddy
 */
public class ToggleOutputLink extends HtmlOutputLink
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.ToggleOutputLink";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.ToggleOutputLink";

    private boolean _editMode = false;
    private String _for = null;

    public void setEditMode(boolean val)
    {
        _editMode = val;
    }

    public boolean getEditMode()
    {
        return _editMode;
    }

    public void setFor(String value)
    {
        _for = value;
    }

    public String getFor()
    {
        return _for;
    }

    public ToggleOutputLink()
    {
        super();
        setRendererType(ToggleOutputLink.DEFAULT_RENDERER_TYPE);
    }

    public void processDecodes(FacesContext context)
    {
        super.processDecodes(context);

        String hiddenFieldId = this.getClientId(context) + "_hidden";
        String editMode = (String) context.getExternalContext()
                .getRequestParameterMap().get(hiddenFieldId);

        if (editMode.trim().equals("true")) {

            this.setEditMode(true);
        }

    }

    public void processUpdates(FacesContext context)
    {
        super.processUpdates(context);
        this.setEditMode(false);
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _for;
        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
    }

}
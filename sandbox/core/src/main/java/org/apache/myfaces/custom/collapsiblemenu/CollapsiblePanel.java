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
package org.apache.myfaces.custom.collapsiblemenu;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsiblePanel extends UIPanel implements NamingContainer
{
    
    public static final String COMPONENT_TYPE = "org.apache.myfaces.CollapsiblePanel";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
        
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    private String _title;
    private Boolean _displayed;
        
    public String getTitle() {
        if (_title != null) return _title;
        ValueBinding vb = getValueBinding("title");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }
    
    public void setTitle(String title) {
        _title = title;
    }
    
    public void setDisplayed(boolean displayed) {
        _displayed = Boolean.valueOf(displayed);
    }
    
    public boolean isDisplayed()
    {
        if (_displayed != null) return _displayed.booleanValue();
        ValueBinding vb = getValueBinding("displayed");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null && v.booleanValue();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _title;
        values[2] = _displayed;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _title = (String) values[1];
        _displayed = (Boolean) values[2];
    }
    
}

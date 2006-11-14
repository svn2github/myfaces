/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.custom.toggle;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Container class allows user to toggle between view/edit mode.
 * 
 * @author Sharath
 * 
 */
public class HtmlToggleGroup extends HtmlPanelGroup
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlToggleGroup";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.HtmlToggleGroup";

    public static final boolean DEFAULT_TOGGLED = false;

    private Boolean _toggled = null;
    
    public HtmlToggleGroup()
    {
        super();
        setRendererType(HtmlToggleGroup.DEFAULT_RENDERER_TYPE);
    }

    public boolean isToggled()
    {
        if (_toggled != null) return _toggled.booleanValue();
        ValueBinding vb = getValueBinding("toggled");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_TOGGLED;
    }

    public Object saveState(FacesContext context)
    {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _toggled;
        return values;
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _toggled =  (Boolean)values[2];
    }
}

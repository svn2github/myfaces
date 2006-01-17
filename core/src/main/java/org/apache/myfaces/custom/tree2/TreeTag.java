/*
 * Copyright 2005 The Apache Software Foundation.
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

package org.apache.myfaces.custom.tree2;

import org.apache.myfaces.taglib.UIComponentTagBase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import org.apache.myfaces.renderkit.JSFAttr;

/**
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */

public class TreeTag extends UIComponentTagBase //UIComponentBodyTagBase
{
    private String _value;
    private String _var;
    private String _varNodeToggler;
    private String _showLines;
    private String _showNav;
    private String _clientSideToggle;
    private String _showRootNode;
    private String _preserveToggle;

    public void release()
    {
        super.release();

        _value=null;
        _var=null;
        _varNodeToggler=null;
        _showLines = null;
        _showNav = null;
        _clientSideToggle = null;
        _showRootNode = null;
        _preserveToggle = null;
    }

    public String getComponentType()
    {
        return "org.apache.myfaces.HtmlTree2";
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.HtmlTree2";
    }

    public void setValue(String value)
    {
        _value = value;
    }

    /**
     * @param var The var to set.
     */
    public void setVar(String var)
    {
        _var = var;
    }

    public void setVarNodeToggler(String varNodeToggler)
    {
        _varNodeToggler = varNodeToggler;
    }

    public void setShowLines(String showLines)
    {
        _showLines = showLines;
    }

    public void setShowNav(String showNav)
    {
        _showNav = showNav;
    }

    public void setClientSideToggle(String clientSideToggle)
    {
        _clientSideToggle = clientSideToggle;
    }

    public void setShowRootNode(String showRootNode)
    {
        _showRootNode = showRootNode;
    }

    public void setPreserveToggle(String preserveToggle)
    {
        _preserveToggle = preserveToggle;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        FacesContext context = getFacesContext();

        if (_value != null)
        {
            ValueBinding vb = context.getApplication().createValueBinding(_value);
            component.setValueBinding("value", vb);
        }

        if (_var != null)
        {
            ((HtmlTree)component).setVar(_var);
        }

        if (_varNodeToggler != null)
        {
            ((HtmlTree)component).setVarNodeToggler(_varNodeToggler);
        }

        setBooleanProperty(component, JSFAttr.SHOW_NAV, _showNav);
        setBooleanProperty(component, JSFAttr.SHOW_LINES, _showLines);
        setBooleanProperty(component, JSFAttr.CLIENT_SIDE_TOGGLE, _clientSideToggle);
        setBooleanProperty(component, JSFAttr.SHOW_ROOT_NODE, _showRootNode);
        setBooleanProperty(component, JSFAttr.PRESERVE_TOGGLE, _preserveToggle);
    }
}

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
package org.apache.myfaces.custom.collapsiblepanel;

import org.apache.myfaces.taglib.html.ext.HtmlPanelGroupTag;

import javax.faces.component.UIComponent;

/**
 * @author Kalle Korhonen (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCollapsiblePanelTag
        extends HtmlPanelGroupTag
{
    //private static final Log log = LogFactory.getLog(HtmlCollapsiblePanelTag.class);

    public String getComponentType()
    {
        return HtmlCollapsiblePanel.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.CollapsiblePanel";
    }

    private String _collapsed;
    private String _value;
    private String _var;
    private String _collapsedVar;
    // User Role support --> already handled by HtmlPanelGroupTag

    public void release() {
        super.release();
        _collapsed=null;
        _value=null;
        _var=null;
        _collapsedVar=null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, "collapsed", _collapsed);
        setStringProperty(component, "value", _value);
        setStringProperty(component,"var",_var);
        setStringProperty(component,"collapsedVar",_collapsedVar);
    }


    public void setCollapsed(String collapsed)
    {
        _collapsed = collapsed;
    }
    
    public void setValue(String value)
    {
        _value = value;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public void setCollapsedVar(String collapsedVar)
    {
        _collapsedVar = collapsedVar;
    }
}

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
package org.apache.myfaces.custom.layout;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelLayout
        extends HtmlPanelGroup
{
    //private static final Log log = LogFactory.getLog(HtmlPanelLayout.class);

    //HTML table attributes
    //TODO!

    // typesafe facet getters

    public UIComponent getHeader()
    {
        return (UIComponent)getFacet("header");
    }

    public UIComponent getNavigation()
    {
        return (UIComponent)getFacet("navigation");
    }

    public UIComponent getBody()
    {
        return (UIComponent)getFacet("body");
    }

    public UIComponent getFooter()
    {
        return (UIComponent)getFacet("footer");
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlPanelLayout";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Layout";

    private String _layout = null;
    private String _headerClass = null;
    private String _navigationClass = null;
    private String _bodyClass = null;
    private String _footerClass = null;
    private String _headerStyle = null;
    private String _navigationStyle = null;
    private String _bodyStyle = null;
    private String _footerStyle = null;

    public HtmlPanelLayout()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public String getLayout()
    {
        if (_layout != null) return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setHeaderClass(String headerClass)
    {
        _headerClass = headerClass;
    }

    public String getHeaderClass()
    {
        if (_headerClass != null) return _headerClass;
        ValueBinding vb = getValueBinding("headerClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setNavigationClass(String navigationClass)
    {
        _navigationClass = navigationClass;
    }

    public String getNavigationClass()
    {
        if (_navigationClass != null) return _navigationClass;
        ValueBinding vb = getValueBinding("navigationClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setBodyClass(String bodyClass)
    {
        _bodyClass = bodyClass;
    }

    public String getBodyClass()
    {
        if (_bodyClass != null) return _bodyClass;
        ValueBinding vb = getValueBinding("bodyClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFooterClass(String footerClass)
    {
        _footerClass = footerClass;
    }

    public String getFooterClass()
    {
        if (_footerClass != null) return _footerClass;
        ValueBinding vb = getValueBinding("footerClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setHeaderStyle(String headerStyle)
    {
        _headerStyle = headerStyle;
    }

    public String getHeaderStyle()
    {
        if (_headerStyle != null) return _headerStyle;
        ValueBinding vb = getValueBinding("headerStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setNavigationStyle(String navigationStyle)
    {
        _navigationStyle = navigationStyle;
    }

    public String getNavigationStyle()
    {
        if (_navigationStyle != null) return _navigationStyle;
        ValueBinding vb = getValueBinding("navigationStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setBodyStyle(String bodyStyle)
    {
        _bodyStyle = bodyStyle;
    }

    public String getBodyStyle()
    {
        if (_bodyStyle != null) return _bodyStyle;
        ValueBinding vb = getValueBinding("bodyStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setFooterStyle(String footerStyle)
    {
        _footerStyle = footerStyle;
    }

    public String getFooterStyle()
    {
        if (_footerStyle != null) return _footerStyle;
        ValueBinding vb = getValueBinding("footerStyle");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[10];
        values[0] = super.saveState(context);
        values[1] = _layout;
        values[2] = _headerClass;
        values[3] = _navigationClass;
        values[4] = _bodyClass;
        values[5] = _footerClass;
        values[6] = _headerStyle;
        values[7] = _navigationStyle;
        values[8] = _bodyStyle;
        values[9] = _footerStyle;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _layout = (String)values[1];
        _headerClass = (String)values[2];
        _navigationClass = (String)values[3];
        _bodyClass = (String)values[4];
        _footerClass = (String)values[5];
        _headerStyle = (String)values[6];
        _navigationStyle = (String)values[7];
        _bodyStyle = (String)values[8];
        _footerStyle = (String)values[9];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

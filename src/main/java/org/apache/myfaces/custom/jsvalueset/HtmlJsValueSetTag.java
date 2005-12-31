/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.custom.jsvalueset;

import org.apache.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlJsValueSetTag
        extends UIComponentTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlInputFileUploadTag.class);

    public String getComponentType()
    {
        return HtmlJsValueSet.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.JsValueSet";
    }

    // UIComponent attributes --> already implemented in UIComponentTagBase

    // user role attributes --> already implemented in UIComponentTagBase

    // HTML universal attributes --> already implemented in HtmlComponentTagBase

    // HTML event handler attributes --> already implemented in HtmlComponentTagBase

    // HtmlJsValueSet attributes
    private String _name;

    public void release() {
        super.release();
        _name=null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "name", _name);
    }

    public void setName(String name)
    {
        _name = name;
    }
}



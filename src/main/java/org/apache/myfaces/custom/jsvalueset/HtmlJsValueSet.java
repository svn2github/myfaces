/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.custom.jsvalueset;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.util._ComponentUtils;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlJsValueSet extends UIOutput
{


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlJsValueSet";
    public static final String COMPONENT_FAMILY = "javax.faces.Output";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.JsValueSet";

    private String _name = null;

    public HtmlJsValueSet()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String getName()
    {
        if (_name != null) return _name;
        ValueBinding vb = getValueBinding("name");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }


    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this)) return false;
        return super.isRendered();
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _name;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _name = (String)values[1];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}



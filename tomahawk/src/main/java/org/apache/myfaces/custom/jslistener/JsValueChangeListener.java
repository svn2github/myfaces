/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.custom.jslistener;

import org.apache.myfaces.util._ComponentUtils;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Martin Marinschek (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JsValueChangeListener extends UIOutput
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.JsValueChangeListener";
    public static final String COMPONENT_FAMILY = "javax.faces.Output";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.JsValueChangeListener";

    private String _for = null;
    private String _expressionValue = null;
    private String _property = null;
    private String _bodyTagEvent;

    public JsValueChangeListener()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFor(String forValue)
    {
        _for = forValue;
    }

    public String getFor()
    {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setExpressionValue(String expressionValue)
    {
        _expressionValue = expressionValue;
    }

    public String getExpressionValue()
    {
        if (_expressionValue != null) return _expressionValue;
        ValueBinding vb = getValueBinding("expressionValue");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setProperty(String property)
    {
        _property = property;
    }

    public String getProperty()
    {
        if (_property != null) return _property;
        ValueBinding vb = getValueBinding("property");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public String getBodyTagEvent()
    {
        if (_bodyTagEvent != null) return _bodyTagEvent;
        ValueBinding vb = getValueBinding("bodyTagEvent");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setBodyTagEvent(String bodyTagEvent)
    {
        _bodyTagEvent = bodyTagEvent;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _expressionValue;
        values[3] = _property;
        values[4] = _bodyTagEvent;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _for = (String)values[1];
        _expressionValue = (String)values[2];
        _property = (String)values[3];
        _bodyTagEvent = (String) values[4];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

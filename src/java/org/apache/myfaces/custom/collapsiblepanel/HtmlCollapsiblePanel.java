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

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.util.Iterator;
import java.io.IOException;

/**
 * @author Kalle Korhonen (latest modification by $Author$)
 * 
 * @version $Revision$ $Date$
 *
 */
public class HtmlCollapsiblePanel extends HtmlPanelGroup
{

    //private static final Log log = LogFactory.getLog(HtmlCollapsiblePanel.class);

    public void processDecodes(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");

        if (!isRendered()) return;

        if(isCollapsed())
        {
            UIComponent component = getFacet("closedContent");

            if(component != null)
            {
                component.processDecodes(context);
            }
        }
        else
        {
            for (Iterator it = getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                child.processDecodes(context);
            }
        }

        try
        {
            decode(context);
        }
        catch (RuntimeException e)
        {
            context.renderResponse();
            throw e;
        }
    }

    public void processUpdates(FacesContext context)
    {
        initialiseVars(context);

        super.processUpdates(context);

        removeVars(context);
    }

    private void initialiseVars(FacesContext context)
    {
        if(getCollapsedVar()!=null)
        {
            context.getExternalContext().getRequestMap().put(getCollapsedVar(),
                            Boolean.valueOf(isCollapsed()));
        }

        if(getVar()!=null)
        {
            context.getExternalContext().getRequestMap().put(getVar(),
                            getValue());
        }
    }

    private void removeVars(FacesContext context)
    {
        if(getCollapsedVar()!=null)
        {
            context.getExternalContext().getRequestMap().remove(getCollapsedVar());
        }

        if(getVar()!=null)
        {
            context.getExternalContext().getRequestMap().remove(getVar());
        }
    }

    public void processValidators(FacesContext context)
    {
        initialiseVars(context);

        super.processValidators(context);

        removeVars(context);
    }

    public void encodeChildren(FacesContext context) throws IOException
    {
        initialiseVars(context);

        super.encodeChildren(context);

        removeVars(context);
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------
    private static final boolean DEFAULT_COLLAPSED = true;

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlCollapsiblePanel";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.CollapsiblePanel";

    private Boolean _collapsed = null;
    private String _value = null;
    private String _var = null;
    private String _collapsedVar = null;

    public HtmlCollapsiblePanel()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setCollapsed(boolean collapsed)
    {
        _collapsed = new Boolean(collapsed);
    }

    public boolean isCollapsed()
    {
        if (_collapsed != null) return _collapsed.booleanValue();
        ValueBinding vb = getValueBinding("collapsed");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_COLLAPSED;
    }

    public void setValue(String value)
    {
        _value = value;
    }

    public String getValue()
    {
        if (_value != null) return _value;
        ValueBinding vb = getValueBinding("value");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setVar(String var)
    {
        _var = var;
    }

    public String getVar()
    {
        if (_var != null) return _var;
        ValueBinding vb = getValueBinding("var");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setCollapsedVar(String collapsedVar)
    {
        _collapsedVar = collapsedVar;
    }

    public String getCollapsedVar()
    {
        if (_collapsedVar != null) return _collapsedVar;
        ValueBinding vb = getValueBinding("collapsedVar");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _collapsed;
        values[2] = _value;
        values[3] = _var;
        values[4] = _collapsedVar;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
          Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _collapsed = (Boolean)values[1];
        _value = (String)values[2];
        _var = (String) values[3];
        _collapsedVar = (String) values[4];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

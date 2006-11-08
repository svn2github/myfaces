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
package org.apache.myfaces.custom.navigation;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlPanelGroup;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Panel, that includes navigation items ({@link HtmlCommandNavigation}) and other
 * components (separators).
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelNavigation
        extends HtmlPanelGroup
{
    private static final Log log = LogFactory.getLog(HtmlPanelNavigation.class);

    private static final String PREVIOUS_VIEW_ROOT = HtmlPanelNavigation.class.getName() + ".PREVIOUS_VIEW_ROOT";
    private boolean _itemOpenActiveStatesRestored = false;

    public void decode(FacesContext context)
    {
        super.decode(context);    //To change body of overridden methods use File | Settings | File Templates.
        
        //Save the current view root for later reference...
        context.getExternalContext().getRequestMap().put(PREVIOUS_VIEW_ROOT, context.getViewRoot());
        //...and remember that this instance needs NO special treatment on rendering:
        _itemOpenActiveStatesRestored = true;
    }

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (!_itemOpenActiveStatesRestored && getChildCount() > 0)
        {
            UIViewRoot previousRoot = (UIViewRoot)context.getExternalContext().getRequestMap().get(PREVIOUS_VIEW_ROOT);
            if (previousRoot != null)
            {
                restoreOpenActiveStates(context, previousRoot, getChildren());
            }
            else
            {
                //no previous root, means no decode was done
                //--> a new request
            }
        }
        
        super.encodeBegin(context);    //To change body of overridden methods use File | Settings | File Templates.
    }
    
    public void restoreOpenActiveStates(FacesContext facesContext,
                                        UIViewRoot previousRoot,
                                        List children)
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof HtmlCommandNavigation)
            {
                HtmlCommandNavigation previousItem = (HtmlCommandNavigation)previousRoot.findComponent(child.getClientId(facesContext));
                if (previousItem != null)
                {

                    HtmlCommandNavigation childItem = (HtmlCommandNavigation)child;
                    if(previousItem.getOpenDirectly()!=null)
                    {
                        childItem.setOpen(previousItem.isOpen());
                    }
                    else if(previousItem.getValueBinding("open")!=null)
                    {
                        childItem.setValueBinding("open",previousItem.getValueBinding("open"));
                    }

                    if(previousItem.getActiveDirectly()!=null)
                    {
                        childItem.setActive(previousItem.isActive());
                    }
                    else if(previousItem.getValueBinding("active")!=null)
                    {
                        childItem.setValueBinding("active",previousItem.getValueBinding("active"));
                    }
                }
                else
                {
                    log.error("Navigation item " + child.getClientId(facesContext) + " not found in previous view.");
                }
                if (child.getChildCount() > 0)
                {
                    restoreOpenActiveStates(facesContext, previousRoot, child.getChildren());
                }
            }
        }
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlPanelNavigation";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Navigation";

    private String _itemClass = null;
    private String _openItemClass = null;
    private String _activeItemClass = null;
    private String _separatorClass = null;
    private String _itemStyle = null;
    private String _openItemStyle = null;
    private String _activeItemStyle = null;
    private String _separatorStyle = null;

    public HtmlPanelNavigation()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setItemClass(String itemClass)
    {
        _itemClass = itemClass;
    }

    public String getItemClass()
    {
        if (_itemClass != null) return _itemClass;
        ValueBinding vb = getValueBinding("itemClass");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setOpenItemClass(String openItemClass)
    {
        _openItemClass = openItemClass;
    }

    public String getOpenItemClass()
    {
        if (_openItemClass != null) return _openItemClass;
        ValueBinding vb = getValueBinding("openItemClass");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setActiveItemClass(String activeItemClass)
    {
        _activeItemClass = activeItemClass;
    }

    public String getActiveItemClass()
    {
        if (_activeItemClass != null) return _activeItemClass;
        ValueBinding vb = getValueBinding("activeItemClass");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setSeparatorClass(String separatorClass)
    {
        _separatorClass = separatorClass;
    }

    public String getSeparatorClass()
    {
        if (_separatorClass != null) return _separatorClass;
        ValueBinding vb = getValueBinding("separatorClass");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setItemStyle(String itemStyle)
    {
        _itemStyle = itemStyle;
    }

    public String getItemStyle()
    {
        if (_itemStyle != null) return _itemStyle;
        ValueBinding vb = getValueBinding("itemStyle");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setOpenItemStyle(String openItemStyle)
    {
        _openItemStyle = openItemStyle;
    }

    public String getOpenItemStyle()
    {
        if (_openItemStyle != null) return _openItemStyle;
        ValueBinding vb = getValueBinding("openItemStyle");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setActiveItemStyle(String activeItemStyle)
    {
        _activeItemStyle = activeItemStyle;
    }

    public String getActiveItemStyle()
    {
        if (_activeItemStyle != null) return _activeItemStyle;
        ValueBinding vb = getValueBinding("activeItemStyle");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setSeparatorStyle(String separatorStyle)
    {
        _separatorStyle = separatorStyle;
    }

    public String getSeparatorStyle()
    {
        if (_separatorStyle != null) return _separatorStyle;
        ValueBinding vb = getValueBinding("separatorStyle");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[9];
        values[0] = super.saveState(context);
        values[1] = _itemClass;
        values[2] = _openItemClass;
        values[3] = _activeItemClass;
        values[4] = _separatorClass;
        values[5] = _itemStyle;
        values[6] = _openItemStyle;
        values[7] = _activeItemStyle;
        values[8] = _separatorStyle;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _itemClass = (String)values[1];
        _openItemClass = (String)values[2];
        _activeItemClass = (String)values[3];
        _separatorClass = (String)values[4];
        _itemStyle = (String)values[5];
        _openItemStyle = (String)values[6];
        _activeItemStyle = (String)values[7];
        _separatorStyle = (String)values[8];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

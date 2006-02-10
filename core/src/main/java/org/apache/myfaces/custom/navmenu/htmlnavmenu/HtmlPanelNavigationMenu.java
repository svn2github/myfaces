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
package org.apache.myfaces.custom.navmenu.htmlnavmenu;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlPanelGroup;
import org.apache.myfaces.util._ComponentUtils;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Many thanks to the guys from Swiss Federal Institute of Intellectual Property & Marc Bouquet
 * for helping to develop this component.
 * @author Manfred Geiler
 * @author Thomas Spiegl
 */
public class HtmlPanelNavigationMenu extends HtmlPanelGroup implements NamingContainer
{
    private static final Log log = LogFactory.getLog(HtmlPanelNavigationMenu.class);

    static final String PREVIOUS_VIEW_ROOT = HtmlPanelNavigationMenu.class.getName() + ".PREVIOUS_VIEW_ROOT";
    private boolean _itemOpenActiveStatesRestored = false;
    private Boolean _disabled;
    private String _disabledStyle;
    private String _disabledStyleClass;

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
            if (child instanceof HtmlCommandNavigationItem)
            {
                HtmlCommandNavigationItem previousItem = (HtmlCommandNavigationItem)previousRoot.findComponent(child.getClientId(facesContext));
                if (previousItem != null)
                {
                    ((HtmlCommandNavigationItem)child).setOpen(previousItem.isOpen());
                    ((HtmlCommandNavigationItem)child).setActive(previousItem.isActive());
                }
                else
                {
                    log.debug("Navigation item " + child.getClientId(facesContext) + " not found in previous view.");
                }
                if (child.getChildCount() > 0)
                {
                    restoreOpenActiveStates(facesContext, previousRoot, child.getChildren());
                }
            }
        }
    }

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlPanelNavigationMenu";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.NavigationMenu";

    private String _itemClass = null;
    private String _openItemClass = null;
    private String _activeItemClass = null;
    private String _separatorClass = null;
    private String _itemStyle = null;
    private String _openItemStyle = null;
    private String _activeItemStyle = null;
    private String _separatorStyle = null;
    private String _layout = null;
    private Boolean _preprocessed = Boolean.FALSE;
    private Boolean _expandAll;

    public HtmlPanelNavigationMenu()
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

    public String getLayout()
    {
        if (_layout != null) return _layout;
        ValueBinding vb = getValueBinding("layout");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setLayout(String layout)
    {
        _layout = layout;
    }

    public Boolean getPreprocessed()
    {
        return _preprocessed;
    }

    public void setPreprocessed(Boolean preprocessed)
    {
        _preprocessed = preprocessed;
    }

    public boolean isExpandAll()
    {
        if (_expandAll != null) return _expandAll.booleanValue();
        ValueBinding vb = getValueBinding("expandAll");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null && v.booleanValue();
    }

    public void setExpandAll(boolean expandAll)
    {
        _expandAll = expandAll ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean isDisabled()
    {
        if (_disabled != null) return _disabled.booleanValue();
        ValueBinding vb = getValueBinding("disabled");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null && v.booleanValue();
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getDisabledStyle()
    {
        if (_disabledStyle != null) return _disabledStyle;
        ValueBinding vb = getValueBinding("disabledStyle");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setDisabledStyle(String disabledStyle)
    {
        _disabledStyle = disabledStyle;
    }

    public String getDisabledStyleClass()
    {
        if (_disabledStyleClass != null) return _disabledStyleClass;
        ValueBinding vb = getValueBinding("disabledStyleClass");
        return vb != null ? _ComponentUtils.getStringValue(getFacesContext(), vb) : null;
    }

    public void setDisabledStyleClass(String disabledStyleClass)
    {
        _disabledStyleClass = disabledStyleClass;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[15];
        values[0] = super.saveState(context);
        values[1] = _itemClass;
        values[2] = _openItemClass;
        values[3] = _activeItemClass;
        values[4] = _separatorClass;
        values[5] = _itemStyle;
        values[6] = _openItemStyle;
        values[7] = _activeItemStyle;
        values[8] = _separatorStyle;
        values[9] = _layout;
        values[10] = _preprocessed;
        values[11] = _expandAll;
        values[12] = _disabled;
        values[13] = _disabledStyle;
        values[14] = _disabledStyleClass;
        return values;
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
        _layout = (String)values[9];
        _preprocessed = (Boolean)values[10];
        _expandAll = (Boolean)values[11];
        _disabled = (Boolean) values[12];
        _disabledStyle = (String) values[13];
        _disabledStyleClass = (String) values[14];
    }
}

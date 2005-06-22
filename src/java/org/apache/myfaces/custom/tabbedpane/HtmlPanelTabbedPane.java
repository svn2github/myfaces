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
package org.apache.myfaces.custom.tabbedpane;

import org.apache.myfaces.renderkit.RendererUtils;

import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlPanelTabbedPane
        extends HtmlPanelGroup
{
    //private static final Log log = LogFactory.getLog(HtmlPanelTabbedPane.class);

    private MethodBinding _tabChangeListener = null;

    //TODO: additional HTML Table attributes (see HtmlPanelTabbedPaneTag)

    public void decode(FacesContext context)
    {
        super.decode(context);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void processDecodes(javax.faces.context.FacesContext context)
   {
       if (context == null) throw new NullPointerException("context");
       decode(context);

       int tabIdx = 0;
       int selectedIndex = getSelectedIndex();

       Iterator it = getFacetsAndChildren();

       while (it.hasNext())
       {
           UIComponent childOrFacet = getUIComponent((UIComponent) it.next());
           childOrFacet.processDecodes(context);
       }
   }

    private UIComponent getUIComponent(UIComponent uiComponent)
    {
        if (uiComponent instanceof UINamingContainer || uiComponent instanceof UIForm)
        {
            List children = uiComponent.getChildren();
            for (int i = 0, len = children.size(); i < len; i++)
            {
                uiComponent = getUIComponent((UIComponent)children.get(i));
            }
        }
        return uiComponent;
    }

    public void addTabChangeListener(TabChangeListener listener)
    {
        addFacesListener(listener);
    }

    public void removeTabChangeListener(TabChangeListener listener)
    {
        removeFacesListener(listener);
    }

    public MethodBinding getTabChangeListener()
    {
        return _tabChangeListener;
    }

    public void setTabChangeListener(MethodBinding tabChangeListener)
    {
        _tabChangeListener = tabChangeListener;
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        if (event instanceof TabChangeEvent)
        {
            TabChangeEvent tabChangeEvent = (TabChangeEvent)event;
            if (tabChangeEvent.getComponent() == this)
            {
                setSelectedIndex(tabChangeEvent.getNewTabIndex());
                getFacesContext().renderResponse();
            }
        }
        super.broadcast(event);

        MethodBinding tabChangeListenerBinding = getTabChangeListener();
        if (tabChangeListenerBinding != null)
        {
            try
            {
                tabChangeListenerBinding.invoke(getFacesContext(), new Object[]{event});
            }
            catch (EvaluationException e)
            {
                Throwable cause = e.getCause();
                if (cause != null && cause instanceof AbortProcessingException)
                {
                    throw (AbortProcessingException)cause;
                }
                else
                {
                    throw e;
                }
            }
        }
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlPanelTabbedPane";
    public static final String COMPONENT_FAMILY = "javax.faces.Panel";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.TabbedPane";
    private static final int DEFAULT_SELECTEDINDEX = 0;

    private Integer _selectedIndex = null;
    private String _bgcolor = null;
    private String _activeTabStyleClass = null;
    private String _inactiveTabStyleClass = null;
    private String _disabledTabStyleClass = null;
    private String _activeSubStyleClass = null;
    private String _inactiveSubStyleClass = null;
    private String _tabContentStyleClass = null;

    public HtmlPanelTabbedPane()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        _selectedIndex = new Integer(selectedIndex);
    }

    public int getSelectedIndex()
    {
        if (_selectedIndex != null) return _selectedIndex.intValue();
        ValueBinding vb = getValueBinding("selectedIndex");
        Integer v = vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : DEFAULT_SELECTEDINDEX;
    }

    public void setBgcolor(String bgcolor)
    {
        _bgcolor = bgcolor;
    }

    public String getBgcolor()
    {
        if (_bgcolor != null) return _bgcolor;
        ValueBinding vb = getValueBinding("bgcolor");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveTabStyleClass(String activeTabStyleClass)
    {
        _activeTabStyleClass = activeTabStyleClass;
    }

    public String getActiveTabStyleClass()
    {
        if (_activeTabStyleClass != null) return _activeTabStyleClass;
        ValueBinding vb = getValueBinding("activeTabStyleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setInactiveTabStyleClass(String inactiveTabStyleClass)
    {
        _inactiveTabStyleClass = inactiveTabStyleClass;
    }

    public String getInactiveTabStyleClass()
    {
        if (_inactiveTabStyleClass != null) return _inactiveTabStyleClass;
        ValueBinding vb = getValueBinding("inactiveTabStyleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveSubStyleClass(String activeSubStyleClass)
    {
        _activeSubStyleClass = activeSubStyleClass;
    }

    public String getActiveSubStyleClass()
    {
        if (_activeSubStyleClass != null) return _activeSubStyleClass;
        ValueBinding vb = getValueBinding("activeSubStyleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setInactiveSubStyleClass(String inactiveSubStyleClass)
    {
        _inactiveSubStyleClass = inactiveSubStyleClass;
    }

    public String getInactiveSubStyleClass()
    {
        if (_inactiveSubStyleClass != null) return _inactiveSubStyleClass;
        ValueBinding vb = getValueBinding("inactiveSubStyleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setTabContentStyleClass(String tabContentStyleClass)
    {
        _tabContentStyleClass = tabContentStyleClass;
    }

    public String getTabContentStyleClass()
    {
        if (_tabContentStyleClass != null) return _tabContentStyleClass;
        ValueBinding vb = getValueBinding("tabContentStyleClass");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }


    public String getDisabledTabStyleClass()
    {
        return _disabledTabStyleClass;
    }


    public void setDisabledTabStyleClass(String disabledTabStyleClass)
    {
        this._disabledTabStyleClass = disabledTabStyleClass;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[10];
        values[0] = super.saveState(context);
        values[1] = _selectedIndex;
        values[2] = _bgcolor;
        values[3] = saveAttachedState(context, _tabChangeListener);
        values[4] = _activeTabStyleClass;
        values[5] = _inactiveTabStyleClass;
        values[6] = _activeSubStyleClass;
        values[7] = _inactiveSubStyleClass;
        values[8] = _tabContentStyleClass;
        values[9] = _disabledTabStyleClass;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _selectedIndex = (Integer)values[1];
        _bgcolor = (String)values[2];
        _tabChangeListener = (MethodBinding)restoreAttachedState(context, values[3]);
        _activeTabStyleClass = (String)values[4];
        _inactiveTabStyleClass = (String)values[5];
        _activeSubStyleClass = (String)values[6];
        _inactiveSubStyleClass = (String)values[7];
        _tabContentStyleClass = (String)values[8];
        _disabledTabStyleClass = (String)values[9];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.myfaces.custom.navmenu.NavigationMenuUtils;
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Thomas Spiegl
 * @author Manfred Geiler
 */
public class HtmlNavigationMenuRenderer extends HtmlLinkRenderer
{
    private static final Log log = LogFactory.getLog(HtmlNavigationMenuRenderer.class);

    public static final String RENDERER_TYPE = "org.apache.myfaces.NavigationMenu";

    private static final Integer ZERO_INTEGER = new Integer(0);

    public boolean getRendersChildren()
    {
        return true;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        if (component instanceof HtmlCommandNavigationItem)
        {
            //HtmlCommandNavigation
            super.decode(facesContext, component);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlCommandNavigationItem)
        {
            //HtmlCommandNavigationItem
            super.encodeBegin(facesContext, component);
        }
    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlCommandNavigationItem)
        {
            //HtmlCommandNavigationItem
            super.encodeChildren(facesContext, component);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlCommandNavigationItem)
        {
            //HtmlCommandNavigationItem
            super.encodeEnd(facesContext, component);
            return;
        }
        RendererUtils.checkParamValidity(facesContext, component, HtmlPanelNavigationMenu.class);
        HtmlPanelNavigationMenu panelNav = (HtmlPanelNavigationMenu)component;
        if (HtmlNavigationMenuRendererUtils.isListLayout(panelNav))
        {
            UIViewRoot previousViewRoot = (UIViewRoot)
                facesContext.getExternalContext().getRequestMap().get(HtmlPanelNavigationMenu.PREVIOUS_VIEW_ROOT);
            // get old view
            // preprocess component tree
            preprocessNavigationItems(facesContext, panelNav, previousViewRoot, panelNav.getChildren(), new UniqueId());
            // render list
            if (log.isDebugEnabled())
                HtmlNavigationMenuRendererUtils.debugTree(log, facesContext, panelNav.getChildren(), 0);
            renderListLayout(facesContext, panelNav);
        }
        else
        {
            renderTableLayout(facesContext, panelNav);
        }
    }

    protected void renderListLayout(FacesContext facesContext, HtmlPanelNavigationMenu component) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlPanelNavigationMenu panelNav = (HtmlPanelNavigationMenu)component;

        if (panelNav.getChildCount() > 0)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(HTML.UL_ELEM, component);
            HtmlRendererUtils.renderHTMLAttributes(writer, panelNav, HTML.UL_PASSTHROUGH_ATTRIBUTES);

            HtmlNavigationMenuRendererUtils.renderChildrenListLayout(facesContext, writer, panelNav, panelNav.getChildren(), 0);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.endElement(HTML.UL_ELEM);
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("PangelNavaigationMenu without children.");
        }
    }

    private void renderTableLayout(FacesContext facesContext, HtmlPanelNavigationMenu component) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        HtmlPanelNavigationMenu panelNav = (HtmlPanelNavigationMenu)component;

        if (panelNav.getChildCount() > 0)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(HTML.TABLE_ELEM, component);
            HtmlRendererUtils.renderHTMLAttributes(writer, panelNav, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
            if (panelNav.getStyle() == null && panelNav.getStyleClass() == null)
            {
                writer.writeAttribute(HTML.BORDER_ATTR, ZERO_INTEGER, null);
            }

            HtmlNavigationMenuRendererUtils.renderChildrenTableLayout(facesContext, writer, panelNav, panelNav.getChildren(), 0);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.endElement(HTML.TABLE_ELEM);
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("PangelNavaigationMenu without children.");
        }
    }
    
    /**
     * look for UINavigationMenuItem && UISelectItems & create components
     */
    private void preprocessNavigationItems(FacesContext facesContext, UIComponent parent,
                                           UIViewRoot previousViewRoot, List children, UniqueId uniqueId)
    {
        for (int i = 0; i < children.size(); i++)
        {
            UIComponent child = (UIComponent)children.get(i);
            if (!child.isRendered()) continue;
            if (child instanceof UINavigationMenuItem)
            {
                UINavigationMenuItem uiNavMenuItem = (UINavigationMenuItem) child;
                createHtmlCommandNavigationItem(facesContext, previousViewRoot, parent, i, uiNavMenuItem, uniqueId);
            }
            else if (child instanceof UISelectItems)
            {
                List list = new ArrayList();
                NavigationMenuUtils.addNavigationMenuItems((UISelectItems) child, list);
                addUINavigationMenuItems(facesContext, parent, children, i + 1, list);
            }
        }
    }

    private void addUINavigationMenuItems(FacesContext facesContext, UIComponent parent, List children, int startIndex, List menuItems)
    {
        for (int j = 0, sizej = menuItems.size(); j < sizej; j++)
        {
            NavigationMenuItem uiNavMenuItem = (NavigationMenuItem) menuItems.get(j);
            UINavigationMenuItem newItem =
                (UINavigationMenuItem) facesContext.getApplication().createComponent(UINavigationMenuItem.COMPONENT_TYPE);
            String clientId = newItem.getClientId(facesContext);
            if (facesContext.getViewRoot().findComponent(clientId) == null)
            {
                newItem.setParent(parent);
                children.add(startIndex++, newItem);
                newItem.setAction(uiNavMenuItem.getAction());
                newItem.setIcon(uiNavMenuItem.getIcon());
                newItem.setRendered(uiNavMenuItem.isRendered());
                newItem.setSplit(uiNavMenuItem.isSplit());
                newItem.setItemLabel(uiNavMenuItem.getLabel());
                newItem.setOpen(uiNavMenuItem.isOpen());
                newItem.setActive(uiNavMenuItem.isActive());
                newItem.setTransient(false);
                if (uiNavMenuItem.getNavigationMenuItems() != null && uiNavMenuItem.getNavigationMenuItems().length > 0)
                {
                    addUINavigationMenuItems(facesContext, newItem, newItem.getChildren(), 0,
                                             Arrays.asList(uiNavMenuItem.getNavigationMenuItems()));
                }
            }
        }
    }

    private void createHtmlCommandNavigationItem(FacesContext facesContext, UIViewRoot previousViewRoot,
                                                 UIComponent parent, int i, UINavigationMenuItem uiNavMenuItem, UniqueId uniqueId)
    {
        // Create HtmlCommandNavigationItem
        HtmlCommandNavigationItem newItem = (HtmlCommandNavigationItem)
            facesContext.getApplication().createComponent(HtmlCommandNavigationItem.COMPONENT_TYPE);
        String clientId = newItem.getClientId(facesContext);
        if (facesContext.getViewRoot().findComponent(clientId) == null)
        {
            String parentId = parent.getClientId(facesContext);
            newItem.setRendererType(RENDERER_TYPE);
            parent.getChildren().add(i + 1, newItem);
            int id = uniqueId.next();
            newItem.setId(parentId + "_item" + id);
            newItem.setParent(parent);
            // set action
            newItem.setAction(HtmlNavigationMenuRendererUtils.getMethodBinding(facesContext, uiNavMenuItem.getAction()));
            // transient, rendered
            newItem.setTransient(uiNavMenuItem.isTransient());
            newItem.setRendered(uiNavMenuItem.isRendered());
            // restore state
            HtmlCommandNavigationItem previousItem =
                HtmlNavigationMenuRendererUtils.findPreviousItem(previousViewRoot, newItem.getClientId(facesContext));
            if (previousItem != null)
            {
                newItem.setActive(Boolean.valueOf(previousItem.isActive()));
                newItem.setOpen(Boolean.valueOf(previousItem.isOpen()));
            }
            else
            {
                if (uiNavMenuItem.isOpen()) newItem.toggleOpen();
                newItem.setActive(Boolean.valueOf(uiNavMenuItem.isActive()));
            }
            // Create and add UIOutput
            UIOutput uiOutput = (UIOutput) facesContext.getApplication().createComponent(UIOutput.COMPONENT_TYPE);
            uiOutput.setId(parentId + "_itemout" + id);
            newItem.getChildren().add(uiOutput);
            uiOutput.setParent(newItem);
            if (uiNavMenuItem.getItemLabel() != null)
            {
                if (HtmlNavigationMenuRendererUtils.isValueReference(uiNavMenuItem.getItemLabel()))
                {
                    uiOutput.setValueBinding("value",
                                             facesContext.getApplication().createValueBinding(uiNavMenuItem.getItemLabel()));
                }
                else
                {
                    uiOutput.setValue(uiNavMenuItem.getItemLabel());
                }
            }
            else
            {
                Object value = uiNavMenuItem.getValue();
                if (value != null &&
                    HtmlNavigationMenuRendererUtils.isValueReference(value.toString()))
                {
                    uiOutput.setValueBinding("value",
                                             facesContext.getApplication().createValueBinding(value.toString()));
                }
                else
                {
                    uiOutput.setValue(uiNavMenuItem.getValue());
                }
            }
            // process next level
            preprocessNavigationItems(facesContext, newItem, previousViewRoot, uiNavMenuItem.getChildren(), uniqueId);
        }
    }

// protected

    protected String getStyle(FacesContext facesContext, UIComponent link)
    {
        if (!(link instanceof HtmlCommandNavigationItem))
        {
            throw new IllegalArgumentException("expected instance of " + HtmlCommandNavigationItem.class.getName());
        }

        UIComponent navPanel = HtmlNavigationMenuRendererUtils.getPanel(link);

        HtmlCommandNavigationItem navItem = (HtmlCommandNavigationItem)link;
        if (navItem.isActive())
        {
            return ((HtmlPanelNavigationMenu)navPanel).getActiveItemStyle();
        }
        else if (navItem.isOpen())
        {
            return ((HtmlPanelNavigationMenu)navPanel).getOpenItemStyle();
        }
        else
        {
            return ((HtmlPanelNavigationMenu)navPanel).getItemStyle();
        }
    }

    protected String getStyleClass(FacesContext facesContext, UIComponent link)
    {
        if (!(link instanceof HtmlCommandNavigationItem))
        {
            throw new IllegalArgumentException();
        }

        UIComponent navPanel = HtmlNavigationMenuRendererUtils.getPanel(link);

        HtmlCommandNavigationItem navItem = (HtmlCommandNavigationItem)link;
        if (navItem.isActive())
        {
            return ((HtmlPanelNavigationMenu)navPanel).getActiveItemClass();
        }
        else if (navItem.isOpen())
        {
            return ((HtmlPanelNavigationMenu)navPanel).getOpenItemClass();
        }
        else
        {
            return ((HtmlPanelNavigationMenu)navPanel).getItemClass();
        }
    }

    private static class UniqueId
    {
        private int _id;

        public int next()
        {
            return _id++;
        }
    }

}

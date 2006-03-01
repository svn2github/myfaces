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
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionListener;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Many thanks to the guys from Swiss Federal Institute of Intellectual Property & Marc Bouquet
 * for helping to develop this component.
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
            boolean preprocess = true;
            boolean clientStateSaving = facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext);
            if (clientStateSaving)
            {
                // client statesaving
                HtmlPanelNavigationMenu panelNavPrev = findPreviousPanelNav(facesContext, panelNav);
                if (panelNavPrev != null)
                {
                    preprocess = false;
                    if (!panelNavPrev.equals(panelNav))
                    {
                        // substitute panelnav
                        UIComponent parent = panelNav.getParent();
                        int insertPos = parent.getChildren().indexOf(panelNav);
                        parent.getChildren().set(insertPos, panelNavPrev);
                        panelNavPrev.setParent(parent);
                        panelNav.setParent(null);
                        panelNav = panelNavPrev;
                    }
                }
            }
            else
            {
                // server statesaving
                if (panelNav.getPreprocessed() != null && panelNav.getPreprocessed().booleanValue())
                    preprocess = false;
            }
            if (preprocess)
            {
                panelNav.setPreprocessed(Boolean.TRUE);
                preprocessNavigationItems(facesContext, panelNav, panelNav.getChildren(), new UniqueId());
                if (!clientStateSaving)
                {
                    HtmlPanelNavigationMenu panelNavPrev = findPreviousPanelNav(facesContext, panelNav);
                    if (panelNavPrev != null)
                    {
                        restoreOpenActiveStates(facesContext, panelNav, panelNavPrev.getChildren());
                    }
                }
            }
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

    private void restoreOpenActiveStates(FacesContext facesContext,
                                         HtmlPanelNavigationMenu panelNav, List children)
    {
        for (int i = 0, size = children.size(); i < size; i++)
        {
            UIComponent uiComponent = (UIComponent) children.get(i);
            if (uiComponent instanceof HtmlCommandNavigationItem)
            {
                HtmlCommandNavigationItem prevItem = (HtmlCommandNavigationItem) uiComponent;
                if (prevItem.isOpen() || prevItem.isActive())
                {
                    HtmlCommandNavigationItem item = (HtmlCommandNavigationItem) panelNav.findComponent(uiComponent.getClientId(facesContext));
                    if (item != null)
                    {
                        if (!copyValueBinding(prevItem, item, "active"))
                            item.setActive(prevItem.isActive());
                        if (!copyValueBinding(prevItem, item, "open"))
                            item.setOpen(prevItem.isOpen());
                        if(!panelNav.isExpandAll() || prevItem.isActive() )
                        item.toggleOpen();
                        if (prevItem.isOpen())
                            restoreOpenActiveStates(facesContext, panelNav, prevItem.getChildren());
                    }
                }
            }
        }
    }

    private HtmlPanelNavigationMenu findPreviousPanelNav(FacesContext facesContext, HtmlPanelNavigationMenu panelNav)
    {
        UIViewRoot previousViewRoot = findPreviousRoot(facesContext);
        if (previousViewRoot != null)
        {
            return (HtmlPanelNavigationMenu) previousViewRoot.findComponent(panelNav.getClientId(facesContext));
        }
        return null;
    }

    private UIViewRoot findPreviousRoot(FacesContext facesContext)
    {
        return (UIViewRoot)facesContext.getExternalContext().getRequestMap().get(HtmlPanelNavigationMenu.PREVIOUS_VIEW_ROOT);
    }

    protected void renderListLayout(FacesContext facesContext, HtmlPanelNavigationMenu panelNav) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        if (panelNav.getChildCount() > 0)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(HTML.UL_ELEM, panelNav);
            HtmlRendererUtils.renderHTMLAttributes(writer, panelNav, HTML.UL_PASSTHROUGH_ATTRIBUTES);
            //iterate over the tree and set every item open if expandAll
            if(panelNav.isExpandAll())
            {
                expandAll(panelNav);
            }
            HtmlNavigationMenuRendererUtils.renderChildrenListLayout(facesContext, writer, panelNav, panelNav.getChildren(), 0);

            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.endElement(HTML.UL_ELEM);
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("PangelNavaigationMenu without children.");
        }
    }

    private void renderTableLayout(FacesContext facesContext, HtmlPanelNavigationMenu panelNav) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (panelNav.getChildCount() > 0)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            writer.startElement(HTML.TABLE_ELEM, panelNav);
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
                                           List children, UniqueId uniqueId)
    {
        for (int i = 0; i < children.size(); i++)
        {
            UIComponent child = (UIComponent)children.get(i);

            if (child instanceof HtmlCommandNavigationItem)
            {
            	HtmlCommandNavigationItem navMenuItem = (HtmlCommandNavigationItem) child;
            	preprocessNavigationItems(facesContext, navMenuItem, navMenuItem.getChildren(), uniqueId);
            }
            else if (child instanceof UINavigationMenuItem)
            {
                UINavigationMenuItem uiNavMenuItem = (UINavigationMenuItem) child;
                createHtmlCommandNavigationItem(facesContext, parent, i, uiNavMenuItem, uniqueId);
            }
            else if (child instanceof UISelectItems)
            {
                List list = new ArrayList();
                if (child.getId() == null)
                {
                    child.setId("testit");
                }
                NavigationMenuUtils.addNavigationMenuItems((UISelectItems) child, list);
                addUINavigationMenuItems(facesContext, parent, children, i + 1, list);
            }
        }
    }

    private void addUINavigationMenuItems(FacesContext facesContext, UIComponent parent, List children, int startIndex, List menuItems)
    {
        String clientId = parent.getClientId(facesContext);
        clientId = clientId.replaceAll(":", "_");
        for (int j = 0, sizej = menuItems.size(); j < sizej; j++)
        {
            NavigationMenuItem uiNavMenuItem = (NavigationMenuItem) menuItems.get(j);
            UINavigationMenuItem newItem =
                (UINavigationMenuItem) facesContext.getApplication().createComponent(UINavigationMenuItem.COMPONENT_TYPE);
            newItem.setId(clientId + "_uinavmitem" + (startIndex + j));
            newItem.getClientId(facesContext); // create clientid
            newItem.setParent(parent);
            children.add(startIndex++, newItem);
            if (uiNavMenuItem.getAction() != null)
            {
                newItem.setAction(HtmlNavigationMenuRendererUtils.getMethodBinding(facesContext, uiNavMenuItem.getAction(), false));
            }
            if (uiNavMenuItem.getActionListener() != null)
            {
                newItem.setActionListener(HtmlNavigationMenuRendererUtils.getMethodBinding(facesContext,
                                                                                           uiNavMenuItem.getActionListener(), true));
            }
            newItem.setIcon(uiNavMenuItem.getIcon());
            newItem.setRendered(uiNavMenuItem.isRendered());
            newItem.setSplit(uiNavMenuItem.isSplit());
            newItem.setItemLabel(uiNavMenuItem.getLabel());
            newItem.setOpen(uiNavMenuItem.isOpen());
            newItem.setActive(uiNavMenuItem.isActive());
            newItem.setValue(uiNavMenuItem.getValue());
            newItem.setTransient(false);
            newItem.setTarget(uiNavMenuItem.getTarget());
            newItem.setDisabled(uiNavMenuItem.isDisabled());
            newItem.setDisabledStyle(uiNavMenuItem.getDisabledStyle());
            newItem.setDisabledStyleClass(uiNavMenuItem.getDisabledStyleClass());

            if (uiNavMenuItem.getNavigationMenuItems() != null && uiNavMenuItem.getNavigationMenuItems().length > 0)
            {
                addUINavigationMenuItems(facesContext, newItem, newItem.getChildren(), 0,
                                         Arrays.asList(uiNavMenuItem.getNavigationMenuItems()));
            }
        }
    }

    private HtmlPanelNavigationMenu getParentPanelNavigation(UIComponent uiComponent)
    {
        if(uiComponent instanceof HtmlPanelNavigationMenu)
        {
            return (HtmlPanelNavigationMenu) uiComponent;
        }
        UIComponent parent = uiComponent.getParent();

            // search HtmlPanelNavigation
            UIComponent p = parent;
            while (p != null && !(p instanceof HtmlPanelNavigationMenu))
            {
                p = p.getParent();
            }
            // p is now the HtmlPanelNavigation
           if (!(p instanceof HtmlPanelNavigationMenu))
                {
                    log.error("HtmlCommandNavigation without parent HtmlPanelNavigation ?!");
                    return null;
                }
        return (HtmlPanelNavigationMenu) p;
    }

    private void createHtmlCommandNavigationItem(FacesContext facesContext, UIComponent parent, int i,
                                                 UINavigationMenuItem uiNavMenuItem, UniqueId uniqueId)
    {
        HtmlPanelNavigationMenu menu = getParentPanelNavigation(parent);
        // Create HtmlCommandNavigationItem
        HtmlCommandNavigationItem newItem = (HtmlCommandNavigationItem)
            facesContext.getApplication().createComponent(HtmlCommandNavigationItem.COMPONENT_TYPE);
        String parentId = parent.getClientId(facesContext);
        parentId = parentId.replaceAll(":", "_");
        int id = uniqueId.next();
        newItem.setId(parentId + "_item" + id);
        newItem.getClientId(facesContext); // create clientid
        newItem.setRendererType(RENDERER_TYPE);
        parent.getChildren().add(i + 1, newItem);
        newItem.setParent(parent);
        // set action & actionListner
        newItem.setAction(uiNavMenuItem.getAction());
        newItem.setActionListener(uiNavMenuItem.getActionListener());
        ActionListener[] listeners = uiNavMenuItem.getActionListeners();
        for (int j = 0; j < listeners.length; j++)
        {
            newItem.addActionListener(listeners[j]);
        }
        // value
        newItem.setValue(uiNavMenuItem.getValue());
        // immeditate
        if (!copyValueBinding(uiNavMenuItem, newItem, "immediate"))
            newItem.setImmediate(uiNavMenuItem.isImmediate());
        // transient, rendered
        if (!copyValueBinding(uiNavMenuItem, newItem, "transient"))
            newItem.setTransient(uiNavMenuItem.isTransient());
        if (!copyValueBinding(uiNavMenuItem, newItem, "rendered"))
            newItem.setRendered(uiNavMenuItem.isRendered());

        if (uiNavMenuItem.isOpen() && ! menu.isExpandAll()) newItem.toggleOpen();
        newItem.setActive(uiNavMenuItem.isActive());

        if (!copyValueBinding(uiNavMenuItem, newItem, "target"))
                    newItem.setTarget(uiNavMenuItem.getTarget());
        if (!copyValueBinding(uiNavMenuItem, newItem, "disabled"))
                    newItem.setDisabled(uiNavMenuItem.isDisabled());
        if (!copyValueBinding(uiNavMenuItem, newItem, "disabledStyle"))
                    newItem.setDisabledStyle(uiNavMenuItem.getDisabledStyle());
        if (!copyValueBinding(uiNavMenuItem, newItem, "disabledStyleClass"))
                    newItem.setDisabledStyleClass(uiNavMenuItem.getDisabledStyleClass());
        // If the parent-Element is disabled the child is disabled as well
        if(parent instanceof HtmlPanelNavigationMenu){
            if(newItem.getDisabledStyle()==null)
            {
                newItem.setDisabledStyle(
                        ((HtmlPanelNavigationMenu)parent).getDisabledStyle()
                );
            }
            if(newItem.getDisabledStyleClass()==null)
            {
                newItem.setDisabledStyleClass(
                        ((HtmlPanelNavigationMenu)parent).getDisabledStyleClass()
                );
            }
            if(((HtmlPanelNavigationMenu)parent).isDisabled())
            {
                newItem.setDisabled(true);
            }
        }
        if(parent instanceof HtmlCommandNavigationItem){
            if(newItem.getDisabledStyle()==null)
            {
                newItem.setDisabledStyle(
                        ((HtmlCommandNavigationItem)parent).getDisabledStyle()
                );
            }
            if(newItem.getDisabledStyleClass()==null)
            {
                newItem.setDisabledStyleClass(
                        ((HtmlCommandNavigationItem)parent).getDisabledStyleClass()
                );
            }
            if(((HtmlCommandNavigationItem)parent).isDisabled())
            {
                newItem.setDisabled(true);
            }
        }

        if (uiNavMenuItem.getIcon()!=null)
        {
            UIGraphic uiGraphic = (UIGraphic) facesContext.getApplication().createComponent(UIGraphic.COMPONENT_TYPE);
            uiGraphic.setId(parentId + "_img" + id);
            uiGraphic.getClientId(facesContext);
            newItem.getChildren().add(uiGraphic);
            uiGraphic.setParent(newItem);
            if (HtmlNavigationMenuRendererUtils.isValueReference(uiNavMenuItem.getIcon()))
                {
                    uiGraphic.setValueBinding("value",
                                             facesContext.getApplication().createValueBinding(uiNavMenuItem.getIcon()));
                }
                else
                {
                    uiGraphic.setValue(uiNavMenuItem.getIcon());
                }
        }
        
        else
        {
            // Create and add UIOutput
            UIOutput uiOutput = (UIOutput) facesContext.getApplication().createComponent(UIOutput.COMPONENT_TYPE);
            uiOutput.setId(parentId + "_txt" + id);
            uiOutput.getClientId(facesContext); // create clientid
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
        }
        // process next level
        log.debug("Instance of UINavigationMenuItem, preprocess childrens");
        preprocessNavigationItems(facesContext, newItem, uiNavMenuItem.getChildren(), uniqueId);
    }

    private boolean copyValueBinding(UIComponent source, UIComponent target, String binding)
    {
        ValueBinding valueBinding = source.getValueBinding(binding);
        if (valueBinding == null)
            return false;
        target.setValueBinding(binding, valueBinding);
        return true;
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

        public void decrease()
        {
            _id--;
        }
    }

    private void expandAll(UIComponent parent)
    {   //Recurse over all Children setOpen if child is HtmlCommandNavigationItem
        if(parent instanceof HtmlCommandNavigationItem)
        {
            HtmlCommandNavigationItem navItem = (HtmlCommandNavigationItem) parent;
            navItem.setOpen(true);
        }
        List children = parent.getChildren();
        UIComponent child;
        for (int i = 0; i < children.size(); i++)
        {
            child =  (UIComponent) children.get(i);
            expandAll(child);
        }

    }

}

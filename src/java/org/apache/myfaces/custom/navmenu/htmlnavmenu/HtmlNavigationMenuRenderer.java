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
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Manfred Geiler
 * @author Thomas Spiegl
 */
public class HtmlNavigationMenuRenderer extends HtmlLinkRenderer
{
    private static final Log log = LogFactory.getLog(HtmlNavigationMenuRenderer.class);

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
            renderListLayout(facesContext, panelNav);
        }
        else
        {
            renderTableLayout(facesContext, panelNav);
        }
    }

    private void postProcessComponentTree(List children)
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (!child.isRendered()) continue;
            if (child instanceof HtmlCommandNavigationItem)
            {
                HtmlCommandNavigationItem navItem = (HtmlCommandNavigationItem) child;
                if (navItem.isActive())
                {
                    navItem.getChildren().remove(0);
                    navItem.getChildren().remove(0);
                }
                if (navItem.isOpen() && navItem.getChildren().size() > 0)
                {
                    postProcessComponentTree(navItem.getChildren());
                }
            }
        }
    }

    private void preProcessComponentTree(FacesContext facesContext, UIComponent activeItemPrefix, List children)
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (!child.isRendered()) continue;
            if (child instanceof HtmlCommandNavigationItem)
            {
                HtmlCommandNavigationItem navItem = (HtmlCommandNavigationItem) child;
                if (navItem.isActive())
                {
                    UIOutput uiOutput = (UIOutput) facesContext.getApplication().createComponent(UIOutput.COMPONENT_TYPE);
                    uiOutput.setValue(navItem.getValue());
                    uiOutput.setTransient(true);
                    navItem.setValue(null);
                    navItem.getChildren().add(0, uiOutput);
                    navItem.getChildren().add(0, activeItemPrefix);
                }
                if (navItem.isOpen() && navItem.getChildren().size() > 0)
                {
                    preProcessComponentTree(facesContext, activeItemPrefix, navItem.getChildren());
                }
            }
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

}

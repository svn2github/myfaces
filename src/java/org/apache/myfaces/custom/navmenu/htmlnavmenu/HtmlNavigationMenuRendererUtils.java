package org.apache.myfaces.custom.navmenu.htmlnavmenu;

import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.RendererUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;

/**
 * @author Thomas Spiegl
 */
class HtmlNavigationMenuRendererUtils
{
    private HtmlNavigationMenuRendererUtils() {}

    public static void renderChildrenListLayout(FacesContext facesContext,
                                                ResponseWriter writer,
                                                HtmlPanelNavigationMenu panelNav,
                                                List children,
                                                int level) throws IOException
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (!child.isRendered()) continue;
            if (child instanceof HtmlCommandNavigationItem)
            {
                //navigation item
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);

                HtmlCommandNavigationItem navItem = (HtmlCommandNavigationItem) child;
                String style = HtmlNavigationMenuRendererUtils.getNavigationItemStyle(panelNav, navItem);
                String styleClass = HtmlNavigationMenuRendererUtils.getNavigationItemClass(panelNav, navItem);

                writer.startElement(HTML.LI_ELEM, panelNav);
                HtmlNavigationMenuRendererUtils.writeStyleAttributes(writer, style, styleClass);

                navItem.encodeBegin(facesContext);
                HtmlNavigationMenuRendererUtils.renderChildren(facesContext, navItem);
                navItem.encodeEnd(facesContext);

                if (child.getChildCount() > 0)
                {
                    writer.startElement(HTML.UL_ELEM, panelNav);
                    //HtmlRendererUtils.renderHTMLAttributes(writer, panelNav, HTML.UL_PASSTHROUGH_ATTRIBUTES);
                    renderChildrenListLayout(facesContext, writer, panelNav, child.getChildren(), level + 1);
                    writer.endElement(HTML.UL_ELEM);
                }
                writer.endElement(HTML.LI_ELEM);
            }
        }
    }

    public static void renderChildrenTableLayout(FacesContext facesContext,
                                                 ResponseWriter writer,
                                                 HtmlPanelNavigationMenu panelNav,
                                                 List children,
                                                 int level) throws IOException
    {
        for (Iterator it = children.iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (!child.isRendered()) continue;
            if (child instanceof HtmlCommandNavigationItem)
            {
                //navigation item
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);

                String style = getNavigationItemStyle(panelNav, (HtmlCommandNavigationItem)child);
                String styleClass = getNavigationItemClass(panelNav, (HtmlCommandNavigationItem)child);

                writer.startElement(HTML.TR_ELEM, panelNav);
                writer.startElement(HTML.TD_ELEM, panelNav);
                writeStyleAttributes(writer, style, styleClass);

                if (style != null || styleClass != null)
                {
                    writer.startElement(HTML.SPAN_ELEM, panelNav);
                    writeStyleAttributes(writer, style, styleClass);
                }
                indent(writer, level);
                child.encodeBegin(facesContext);

                child.encodeEnd(facesContext);
                if (style != null || styleClass != null)
                {
                    writer.endElement(HTML.SPAN_ELEM);
                }

                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);

                if (child.getChildCount() > 0)
                {
                    renderChildrenTableLayout(facesContext, writer, panelNav, child.getChildren(), level + 1);
                }
            }
            else
            {
                //separator
                HtmlRendererUtils.writePrettyLineSeparator(facesContext);

                String style = panelNav.getSeparatorStyle();
                String styleClass = panelNav.getSeparatorClass();

                writer.startElement(HTML.TR_ELEM, panelNav);
                writer.startElement(HTML.TD_ELEM, panelNav);
                writeStyleAttributes(writer, style, styleClass);

                if (style != null || styleClass != null)
                {
                    writer.startElement(HTML.SPAN_ELEM, panelNav);
                    writeStyleAttributes(writer, style, styleClass);
                }
                indent(writer, level);
                RendererUtils.renderChild(facesContext, child);
                if (style != null || styleClass != null)
                {
                    writer.endElement(HTML.SPAN_ELEM);
                }

                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
            }
        }
    }

    public static void indent(ResponseWriter writer, int level) throws IOException
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < level; i++)
        {
            buf.append("&#160;&#160;&#160;&#160;");
        }
        writer.write(buf.toString());
    }



    public static String getNavigationItemStyle(HtmlPanelNavigationMenu navPanel, HtmlCommandNavigationItem navItem)
    {
        if (navItem.isActive())
        {
            return navPanel.getActiveItemStyle();
        }
        else if (navItem.isOpen())
        {
            return navPanel.getOpenItemStyle();
        }
        else
        {
            return navPanel.getItemStyle();
        }
    }

    public static String getNavigationItemClass(HtmlPanelNavigationMenu navPanel,
                                                HtmlCommandNavigationItem navItem)
    {
        // MYFACES-117, if a styleClass is supplied for a HtmlCommandNavigationItem,
        // panelNavigation active/open/normal styles for items will be overriden
        if (navItem.getStyleClass() != null)
        {
            return navItem.getStyleClass();
        }

        if (navItem.isActive())
        {
            return navPanel.getActiveItemClass();
        }
        else if (navItem.isOpen())
        {
            return navPanel.getOpenItemClass();
        }
        else
        {
            return navPanel.getItemClass();
        }
    }

    public static void writeStyleAttributes(ResponseWriter writer,
                                            String style,
                                            String styleClass) throws IOException
    {
        HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_ATTR, HTML.STYLE_ATTR, style);
        HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR, styleClass);
    }

    public static UIComponent getPanel(UIComponent link)
    {
        UIComponent navPanel = link.getParent();
        while (navPanel != null && !(navPanel instanceof HtmlPanelNavigationMenu))
        {
            navPanel = navPanel.getParent();
        }
        if (navPanel == null)
        {
            throw new IllegalStateException("HtmlCommandNavigationItem not nested in HtmlPanelNavigation!?");
        }
        return navPanel;
    }

    public static boolean isListLayout(HtmlPanelNavigationMenu panelNav)
    {
        return "List".equalsIgnoreCase(panelNav.getLayout());
    }

    public static void renderChildren(FacesContext facesContext, HtmlCommandNavigationItem component) throws IOException
    {
        if (component.getChildCount() > 0)
        {
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (!(child instanceof HtmlCommandNavigationItem))
                {
                    RendererUtils.renderChild(facesContext, child);
                }
            }
        }
    }

}

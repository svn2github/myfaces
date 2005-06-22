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
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.component.UserRoleUtils;

import javax.faces.application.ViewHandler;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.FacesException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTabbedPaneRenderer
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlTabbedPaneRenderer.class);

    private static final String TABLE_STYLE =
        "border-style: none; " +
        "padding: 0px; " +
        "border-spacing: 0px; " +
        "empty-cells: show; ";

    private static final String ACTIVE_HEADER_CELL_STYLE =
        "border-top: 2px outset #CCCCCC; " +
        "border-right: 2px outset #CCCCCC; " +
        "border-bottom: 0px none; " +
        "border-left: 2px outset #CCCCCC; " +
        "text-align: center; ";

    private static final String INACTIVE_HEADER_CELL_STYLE =
        "border-top: 1px outset #CCCCCC; " +
        "border-right: 1px outset #CCCCCC; " +
        "border-bottom: 0px none; " +
        "border-left: 1px outset #CCCCCC; " +
        "text-align: center; " +
        "background-color: #CCCCCC; ";

    private static final String EMPTY_HEADER_CELL_STYLE =
        "border-top: 0px none; " +
        "border-right: 0px none; " +
        "border-bottom: 0px none; " +
        "border-left: 0px none; ";

    private static final String SUB_HEADER_CELL_STYLE =
        "height: 2px; " +
        "line-height: 0px; font-size: 0px; " +
        "border-bottom: 0px none; ";

    private static final String TAB_CELL_STYLE =
        "border-top: 0px none; " +
        "border-right: 2px outset #CCCCCC; " +
        "border-bottom: 2px outset #CCCCCC; " +
        "border-left: 2px outset #CCCCCC; " +
        "padding: 10px; ";

    private static final String NO_BORDER_STYLE =
        "0px none; ";

    private static final String BORDER_STYLE =
        "2px outset #CCCCCC; ";

    private static final String BUTTON_STYLE_ACTIVE
        = "border-style:none; width:100%; cursor:pointer;";
    private static final String BUTTON_STYLE_INACTIVE
        = "border-style:none; width:100%; cursor:pointer; background-color:#CCCCCC;";

    private static final String BUTTON_STYLE_DISABLED
        = "border-style:none; width:100%; cursor:normal;";

    private static final String DEFAULT_BG_COLOR = "#FFFFFF";

    private static final String AUTO_FORM_SUFFIX = ".autoform";


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException
    {
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPanelTabbedPane.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);

        HtmlPanelTabbedPane tabbedPane = (HtmlPanelTabbedPane)uiComponent;
        int selectedIndex = tabbedPane.getSelectedIndex();

        if (tabbedPane.getBgcolor() == null)
        {
            tabbedPane.setBgcolor(DEFAULT_BG_COLOR);
        }

        UIForm parentForm = RendererUtils.findParentForm(tabbedPane);
        if (parentForm == null)
        {
            writeFormStart(writer, facesContext, tabbedPane);
        }

        writeTableStart(writer, facesContext, tabbedPane);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiComponent);

        //Tab headers
        int tabIdx = 0;
        int visibleTabCount = 0;
        int visibleTabSelectedIdx = -1;
        List children = tabbedPane.getChildren();
        for (int i = 0, len = children.size(); i < len; i++)
        {
            UIComponent child = getUIComponent((UIComponent)children.get(i));
            if (child instanceof HtmlPanelTab)
            {
                if (child.isRendered())
                {
                    writeHeaderCell(writer, facesContext, tabbedPane,
                                    (HtmlPanelTab)child, tabIdx, tabIdx == selectedIndex, isDisabled(facesContext, child));
                    if (tabIdx == selectedIndex)
                    {
                        visibleTabSelectedIdx = visibleTabCount;
                    }
                    visibleTabCount++;
                }
                tabIdx++;
            }
        }

        //Empty tab cell on the right for better look
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        HtmlRendererUtils.writePrettyIndent(facesContext);
        writer.startElement(HTML.TD_ELEM, uiComponent);
        writer.writeAttribute(HTML.STYLE_ATTR, EMPTY_HEADER_CELL_STYLE, null);
        writer.write("&#160;");
        writer.endElement(HTML.TD_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);

        //Sub header cells
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiComponent);
        writeSubHeaderCells(writer,  facesContext, tabbedPane, visibleTabCount, visibleTabSelectedIdx, false);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);

        //Tab
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.TR_ELEM, uiComponent);
        writeTabCell(writer,  facesContext, tabbedPane, visibleTabCount, selectedIndex);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);

        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TABLE_ELEM);

        if (parentForm == null)
        {
            writeFormEnd(writer, facesContext);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPanelTabbedPane.class);

        HtmlPanelTabbedPane tabbedPane = (HtmlPanelTabbedPane)uiComponent;

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();

        int tabIdx = 0;
        List children = tabbedPane.getChildren();
        for (int i = 0, len = children.size(); i < len; i++)
        {
            UIComponent child = getUIComponent((UIComponent)children.get(i));
            if (child instanceof HtmlPanelTab)
            {
                String paramName = tabbedPane.getClientId(facesContext) + "." + tabIdx;
                String paramValue = (String)paramMap.get(paramName);
                if (paramValue != null && paramValue.length() > 0)
                {
                    tabbedPane.queueEvent(new TabChangeEvent(tabbedPane,
                                                             tabbedPane.getSelectedIndex(),
                                                             tabIdx));
                    return;
                }
                tabIdx++;
            }
        }
    }

    protected void writeFormStart(ResponseWriter writer,
                                  FacesContext facesContext,
                                  UIComponent tabbedPane)
        throws IOException
    {
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        String actionURL = viewHandler.getActionURL(facesContext, viewId);

        //write out auto form
        writer.startElement(HTML.FORM_ELEM, null);
        writer.writeAttribute(HTML.NAME_ATTR, tabbedPane.getClientId(facesContext) + AUTO_FORM_SUFFIX, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:inline", null);
        writer.writeAttribute(HTML.METHOD_ATTR, "post", null);
        writer.writeURIAttribute(HTML.ACTION_ATTR,
                                 facesContext.getExternalContext().encodeActionURL(actionURL),
                                 null);
        writer.flush();
    }


    protected void writeTableStart(ResponseWriter writer,
                                   FacesContext facesContext,
                                   HtmlPanelTabbedPane tabbedPane)
        throws IOException
    {
        String oldStyle = tabbedPane.getStyle();
        if (oldStyle == null)
        {
            tabbedPane.setStyle(TABLE_STYLE);
        }
        else
        {
            tabbedPane.setStyle(TABLE_STYLE + "; " + oldStyle);
        }

        String oldBgColor = tabbedPane.getBgcolor();
        tabbedPane.setBgcolor(null);

        writer.startElement(HTML.TABLE_ELEM, tabbedPane);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        HtmlRendererUtils.renderHTMLAttributes(writer, tabbedPane, HTML.TABLE_PASSTHROUGH_ATTRIBUTES);
        writer.flush();

        tabbedPane.setStyle(oldStyle);
        tabbedPane.setBgcolor(oldBgColor);
    }


    protected void writeHeaderCell(ResponseWriter writer,
                                   FacesContext facesContext,
                                   HtmlPanelTabbedPane tabbedPane,
                                   HtmlPanelTab tab,
                                   int tabIndex,
                                   boolean active,
                                   boolean disabled)
        throws IOException
    {
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        HtmlRendererUtils.writePrettyIndent(facesContext);
        writer.startElement(HTML.TD_ELEM, tabbedPane);
        if (active)
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  ACTIVE_HEADER_CELL_STYLE + "background-color:" + tabbedPane.getBgcolor(),
                                  null);

            HtmlRendererUtils.renderHTMLAttribute(writer, tabbedPane, "activeTabStyleClass", HTML.STYLE_CLASS_ATTR);
        }
        else if (disabled)
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  INACTIVE_HEADER_CELL_STYLE,
                                  null);
            HtmlRendererUtils.renderHTMLAttribute(writer, tabbedPane, "disabledTabStyleClass", HTML.STYLE_CLASS_ATTR);
        }
        else
        {
            writer.writeAttribute(HTML.STYLE_ATTR,
                                  INACTIVE_HEADER_CELL_STYLE,
                                  null);
            HtmlRendererUtils.renderHTMLAttribute(writer, tabbedPane, "inactiveTabStyleClass", HTML.STYLE_CLASS_ATTR);
        }


        String label = tab.getLabel();
        if (label == null || label.length() == 0)
        {
            label = "Tab " + tabIndex;
        }

        if (disabled) {
            writer.startElement(HTML.LABEL_ELEM, tabbedPane);
            writer.writeAttribute(HTML.NAME_ATTR, tabbedPane.getClientId(facesContext) + "." + tabIndex, null);
            writer.writeAttribute(HTML.STYLE_ATTR, BUTTON_STYLE_DISABLED, null);
            writer.writeText(label, null);
            writer.endElement(HTML.LABEL_ELEM);
        } else {
            //Button
            writer.startElement(HTML.INPUT_ELEM, tabbedPane);
            writer.writeAttribute(HTML.TYPE_ATTR, "submit", null);
            writer.writeAttribute(HTML.NAME_ATTR, tabbedPane.getClientId(facesContext) + "." + tabIndex, null);
            writer.writeAttribute(HTML.VALUE_ATTR, label, null);

            if (active)
            {
                writer.writeAttribute(HTML.STYLE_ATTR,
                    BUTTON_STYLE_ACTIVE + "background-color:" + tabbedPane.getBgcolor(),
                    null);
            }
            else
            {
                writer.writeAttribute(HTML.STYLE_ATTR,
                    BUTTON_STYLE_INACTIVE,
                    null);
            }
            writer.endElement(HTML.INPUT_ELEM);
        }
        writer.endElement(HTML.TD_ELEM);
    }


    protected void writeSubHeaderCells(ResponseWriter writer,
                                       FacesContext facesContext,
                                       HtmlPanelTabbedPane tabbedPane,
                                       int visibleTabCount,
                                       int visibleTabSelectedIndex,
                                       boolean disabled)
            throws IOException
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0, cnt = visibleTabCount + 1; i < cnt; i++)
        {
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
            HtmlRendererUtils.writePrettyIndent(facesContext);
            writer.startElement(HTML.TD_ELEM, tabbedPane);
            buf.setLength(0);
            buf.append(SUB_HEADER_CELL_STYLE);
            buf.append("border-top:").append(i == visibleTabSelectedIndex ? NO_BORDER_STYLE : BORDER_STYLE);
            buf.append("border-right:").append(i + 1 < cnt ? NO_BORDER_STYLE : BORDER_STYLE);
            buf.append("border-left:").append(i > 0 ? NO_BORDER_STYLE : BORDER_STYLE);
            buf.append("background-color:").append(tabbedPane.getBgcolor());
            writer.writeAttribute(HTML.STYLE_ATTR, buf.toString(), null);

            if (i == visibleTabSelectedIndex) {
                HtmlRendererUtils.renderHTMLAttribute(writer, tabbedPane, "activeSubStyleClass", HTML.STYLE_CLASS_ATTR);
            } else {
                HtmlRendererUtils.renderHTMLAttribute(writer, tabbedPane, "inactiveSubStyleClass", HTML.STYLE_CLASS_ATTR);
            }

            writer.write("&#160;");
            writer.endElement(HTML.TD_ELEM);
        }
    }


    protected void writeTabCell(ResponseWriter writer, FacesContext facesContext, HtmlPanelTabbedPane tabbedPane,
           int tabCount, int selectedIndex) throws IOException {
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
       HtmlRendererUtils.writePrettyIndent(facesContext);
       writer.startElement(HTML.TD_ELEM, tabbedPane);
       writer.writeAttribute(HTML.COLSPAN_ATTR, Integer.toString(tabCount + 1), null);
       writer.writeAttribute(HTML.STYLE_ATTR, TAB_CELL_STYLE + "background-color:" + tabbedPane.getBgcolor(), null);
       HtmlRendererUtils.renderHTMLAttribute(writer, tabbedPane, "tabContentStyleClass", HTML.STYLE_CLASS_ATTR);

       int tabIdx = 0;
       List children = tabbedPane.getChildren();
       for (int i = 0, len = children.size(); i < len; i++) {
           UIComponent child = getUIComponent((UIComponent) children.get(i));
           if (child instanceof HtmlPanelTab) {
               // the inactive tabs are hidden with a div-tag
               if (tabIdx != selectedIndex) {
                   writer.startElement(HTML.DIV_ELEM, tabbedPane);
                   writer.writeAttribute(HTML.STYLE_ATTR, "display:none", null);
                   RendererUtils.renderChild(facesContext, child);
                   writer.endElement(HTML.DIV_ELEM);
               }
               else
               {
                   RendererUtils.renderChild(facesContext, child);
               }

               tabIdx++;
           } else {
               RendererUtils.renderChild(facesContext, child);
           }
       }

       writer.endElement(HTML.TD_ELEM);
   }

    private UIComponent getUIComponent(UIComponent uiComponent)
    {
        if (uiComponent instanceof UIForm || uiComponent instanceof UINamingContainer)
        {
            List children = uiComponent.getChildren();
            for (int i = 0, len = children.size(); i < len; i++)
            {
                uiComponent = getUIComponent((UIComponent)children.get(i));
            }
        }
        return uiComponent;
    }


    protected void writeFormEnd(ResponseWriter writer,
                                FacesContext facesContext)
        throws IOException
    {
        //write state marker
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        viewHandler.writeState(facesContext);

        writer.endElement(HTML.FORM_ELEM);
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        return !UserRoleUtils.isEnabledOnUserRole(uiComponent);
    }
}

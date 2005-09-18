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
package org.apache.myfaces.custom.navmenu.jscookmenu;

import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.myfaces.custom.navmenu.NavigationMenuUtils;
import org.apache.myfaces.custom.navmenu.UINavigationMenuItem;
import org.apache.myfaces.el.SimpleActionMethodBinding;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.util.DummyFormResponseWriter;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class HtmlJSCookMenuRenderer
    extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlJSCookMenuRenderer.class);

    private static final String JSCOOK_ACTION_PARAM = "jscook_action";

    public void decode(FacesContext context, UIComponent component)
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);

        Map parameter = context.getExternalContext().getRequestParameterMap();
        String actionParam = (String)parameter.get(JSCOOK_ACTION_PARAM);
        if (actionParam != null && !actionParam.trim().equals("") &&
                !actionParam.trim().equals("null"))
        {
            String compId = getMenuId(context,component);
            int idx = actionParam.lastIndexOf(':');
            if (idx == -1) {
                return;
            }
            String actionId = actionParam.substring(0, idx);
            if (! compId.equals(actionId)) {
                return;
            }
            actionParam = actionParam.substring(idx + 1);
            actionParam = decodeValueBinding(actionParam, context);
            MethodBinding mb;
            if (UIComponentTag.isValueReference(actionParam))
            {
                mb = context.getApplication().createMethodBinding(actionParam, null);
            }
            else
            {
                mb = new SimpleActionMethodBinding(actionParam);
            }
            ((HtmlCommandJSCookMenu)component).setAction(mb);

            component.queueEvent(new ActionEvent(component));
        }
    }

    private String decodeValueBinding(String actionParam, FacesContext context) 
    {
        int idx = actionParam.indexOf(";#{"); 
        if (idx == -1) {
            return actionParam;
        }
        
        String newActionParam = actionParam.substring(0, idx);
        String vbParam = actionParam.substring(idx + 1);
        
        idx = vbParam.indexOf('=');
        if (idx == -1) {
            return newActionParam;
        }
        String vbExpressionString = vbParam.substring(0, idx);
        String vbValue = vbParam.substring(idx + 1);
        
        ValueBinding vb = 
            context.getApplication().createValueBinding(vbExpressionString);        
        vb.setValue(context, vbValue);
        
        return newActionParam;
    }
    
    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);

        List list = NavigationMenuUtils.getNavigationMenuItemList(component);
        if (list.size() > 0)
        {
            List uiNavMenuItemList = component.getChildren();
            DummyFormResponseWriter dummyFormResponseWriter = DummyFormUtils.getDummyFormResponseWriter(context);
            dummyFormResponseWriter.addDummyFormParameter(JSCOOK_ACTION_PARAM);
            dummyFormResponseWriter.setWriteDummyForm(true);

            String myId = getMenuId(context, component);
            
            ResponseWriter writer = context.getResponseWriter();

            writer.startElement(HTML.SCRIPT_ELEM,component);
            writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            StringBuffer script = new StringBuffer();
            script.append("var ").append(getMenuId(context, component)).append(" =\n[");
            encodeNavigationMenuItems(context, script,
                                      (NavigationMenuItem[]) list.toArray(new NavigationMenuItem[list.size()]),
                                      uiNavMenuItemList,
                                      myId);

            script.append("];");
            writer.writeText(script.toString(),null);
            writer.endElement(HTML.SCRIPT_ELEM);
        }
    }

    private void encodeNavigationMenuItems(FacesContext context,
                                           StringBuffer writer,
                                           NavigationMenuItem[] items,
                                           List uiNavMenuItemList,
                                           String menuId)
        throws IOException
    {
        for (int i = 0; i < items.length; i++)
        {
            NavigationMenuItem item = items[i];
            Object tempObj = null;
            UINavigationMenuItem uiNavMenuItem = null;
            if (i < uiNavMenuItemList.size()) {
                tempObj = uiNavMenuItemList.get(i);
            }
            if (tempObj != null) {
                if (tempObj instanceof UINavigationMenuItem) {
                    uiNavMenuItem = (UINavigationMenuItem) tempObj;
                }
            }

            if (! item.isRendered()) {
                continue;
            }

            if (i > 0)
            {
                writer.append(",\n");
            }

            if (item.isSplit())
            {
                writer.append("_cmSplit,");
            }

            writer.append("[");
            if (item.getIcon() != null)
            {
                String iconSrc = context.getApplication().getViewHandler().getResourceURL(context, item.getIcon());
                writer.append("'<img src=\"");
                writer.append(context.getExternalContext().encodeResourceURL(iconSrc));
                writer.append("\"/>'");
            }
            else
            {
                writer.append("null");
            }
            writer.append(", '");
            if( item.getLabel() != null ) {
                writer.append(JavascriptUtils.encodeString(item.getLabel()));
            }
            writer.append("', ");
            if (item.getAction() != null && ! item.isDisabled())
            {
                writer.append("'");
                writer.append(menuId);
                writer.append(':');
                writer.append(item.getAction());
                if (uiNavMenuItem != null) {
                    encodeValueBinding(writer, uiNavMenuItem, item);
                }
                writer.append("'");
            }
            else
            {
                writer.append("null");
            }
            writer.append(", 'linkDummyForm', null"); // TODO Change here to allow the use of non dummy form if possible.

            if (item.isRendered() && ! item.isDisabled()) {
                // render children only if parent is visible/enabled
                NavigationMenuItem[] menuItems = item.getNavigationMenuItems();
                if (menuItems != null && menuItems.length > 0)
                {
                    writer.append(",");
                    if (uiNavMenuItem != null)
                    {
                        encodeNavigationMenuItems(context, writer, menuItems, 
                                uiNavMenuItem.getChildren(), menuId);
                    } else {
                        encodeNavigationMenuItems(context, writer, menuItems,
                                new ArrayList(1), menuId);
                    } 
                }
            }
            writer.append("]");
        }
    }

    private void encodeValueBinding(StringBuffer writer, UINavigationMenuItem uiNavMenuItem,
            NavigationMenuItem item) throws IOException 
    {
        ValueBinding vb = uiNavMenuItem.getValueBinding("NavMenuItemValue");
        if (vb == null) {
            return;
        }
        String vbExpression = vb.getExpressionString();
        if (vbExpression == null) {
            return;
        }
        Object tempObj = item.getValue();
        if (tempObj == null) {
            return;
        }
        
        writer.append(";");
        writer.append(vbExpression);
        writer.append("=");
        writer.append(tempObj.toString());
    }
    
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, HtmlCommandJSCookMenu.class);
        HtmlCommandJSCookMenu menu = (HtmlCommandJSCookMenu)component;
        
        String theme = menu.getTheme();
        
        AddResource.addJavaScriptToHeader(HtmlJSCookMenuRenderer.class, "JSCookMenu.js", context);
        AddResource.addJavaScriptToHeader(HtmlJSCookMenuRenderer.class, "MyFacesHack.js", context);

        if( theme.equals( "ThemeOffice" ) ){
            StringBuffer buf = new StringBuffer();
            buf.append("myThemeOfficeBase='");
            buf.append(AddResource.getResourceBasePath(HtmlJSCookMenuRenderer.class,context));
            buf.append("/ThemeOffice/';");

        	AddResource.addInlineScriptToHeader(buf.toString(), context);

            AddResource.addJavaScriptToHeader(HtmlJSCookMenuRenderer.class, "ThemeOffice/theme.js", context);
        	AddResource.addStyleSheet(HtmlJSCookMenuRenderer.class, "ThemeOffice/theme.css", context);
        }else if( theme.equals( "ThemeMiniBlack" ) ){
            StringBuffer buf = new StringBuffer();
            buf.append("myThemeMiniBlackBase='");
            buf.append(AddResource.getResourceBasePath(HtmlJSCookMenuRenderer.class,context));
            buf.append("/ThemeMiniBlack/';");

        	AddResource.addInlineScriptToHeader(buf.toString(), context);
            AddResource.addJavaScriptToHeader(HtmlJSCookMenuRenderer.class, "ThemeMiniBlack/theme.js", context);
        	AddResource.addStyleSheet(HtmlJSCookMenuRenderer.class, "ThemeMiniBlack/theme.css", context);
        }else if( theme.equals( "ThemeIE" ) ){
            StringBuffer buf = new StringBuffer();
            buf.append("myThemeIEBase='");
            buf.append(AddResource.getResourceBasePath(HtmlJSCookMenuRenderer.class,context));
            buf.append("/ThemeIE/';");

        	AddResource.addInlineScriptToHeader(buf.toString(), context);
            AddResource.addJavaScriptToHeader(HtmlJSCookMenuRenderer.class, "ThemeIE/theme.js", context);
        	AddResource.addStyleSheet(HtmlJSCookMenuRenderer.class, "ThemeIE/theme.css", context);
        }else if( theme.equals( "ThemePanel" ) ){
            StringBuffer buf = new StringBuffer();
            buf.append("myThemePanelBase='");
            buf.append(AddResource.getResourceBasePath(HtmlJSCookMenuRenderer.class,context));
            buf.append("/ThemePanel/';");

        	AddResource.addInlineScriptToHeader(buf.toString(), context);
            AddResource.addJavaScriptToHeader(HtmlJSCookMenuRenderer.class, "ThemePanel/theme.js", context);
        	AddResource.addStyleSheet(HtmlJSCookMenuRenderer.class, "ThemePanel/theme.css", context);
        }
        	// Otherwise ?? bug ??
        ResponseWriter writer = context.getResponseWriter();

        String menuId = getMenuId(context, component);

        writer.write("<div id=\"");
        writer.write(menuId);
        writer.write("\"></div>\n");
        writer.startElement(HTML.SCRIPT_ELEM,menu);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);

        StringBuffer buf = new StringBuffer();
        buf.append("\tcmDraw ('").
                append(menuId).
                append("', ").
                append(menuId).
                append(", '").
                append(menu.getLayout()).
                append("', cm").
                append(theme).
                append(", '").
                append(theme).
                append("');");

        writer.writeText(buf.toString(),null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    /**
     * Fetch the very last part of the menu id.
     * 
     * @param context
     * @param component
     * @return String id of the menu
     */
    private String getMenuId(FacesContext context, UIComponent component) {
        String menuId = component.getClientId(context).replaceAll(":","_") + "_menu";
        while(menuId.startsWith("_"))
        {
            menuId = menuId.substring(1);
        }
        return menuId;
    }

}

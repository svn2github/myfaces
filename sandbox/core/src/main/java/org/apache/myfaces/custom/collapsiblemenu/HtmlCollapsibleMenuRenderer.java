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
package org.apache.myfaces.custom.collapsiblemenu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.renderkit.html.util.DummyFormUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

/**
 * Renderer for the Collapsible Menu component
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class HtmlCollapsibleMenuRenderer extends HtmlRenderer {
    
    
    private static final Log log = LogFactory.getLog(HtmlCollapsibleMenuRenderer.class);

    private static final String AUTO_FORM_SUFFIX = ".autoform";
        
    public boolean getRendersChildren()
    {
        return true;
    }
  
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, CollapsibleMenu.class);
        CollapsibleMenu collapsibleMenu = (CollapsibleMenu)component;
        
        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        addResource.addStyleSheet(facesContext,AddResource.HEADER_BEGIN, 
                this.getClass(), "collapsibleMenu.css");
        addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, 
                this.getClass(), "collapsibleMenu.js");
        
        boolean preprocessed = collapsibleMenu.getPreprocessed().booleanValue();
        //Preprocessing should happen only once in the lifetime of a component
        if (!preprocessed) {
            this.preprocessChildren(facesContext, collapsibleMenu);
            collapsibleMenu.setPreprocessed(Boolean.TRUE);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, CollapsibleMenu.class);
        CollapsibleMenu collapsibleMenu = (CollapsibleMenu)component;
        
        //If there is no parent form in the component heirarcy, 
        //then use the myfaces 'dummy' form
        if (CollapsibleMenuUtils.getParentForm(component) == null) {
            DummyFormUtils.setWriteDummyForm(facesContext,true);
        }
        
        String formName = CollapsibleMenuUtils.getFormName(component, facesContext);
                                
        ResponseWriter writer = facesContext.getResponseWriter();
 
        String activePanelId = collapsibleMenu.getClientIdOfActivePanel(facesContext);
        
        String javaScript = "showPanel('" + activePanelId + "');";
        
        AddResource addResource = AddResourceFactory.
            getInstance(facesContext);
        addResource.addJavaScriptToBodyTag(facesContext, 
                "onload", javaScript);
        
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.startElement(HTML.DIV_ELEM, collapsibleMenu);
        writer.writeAttribute(HTML.CLASS_ATTR, "outbar", null);
                
        StringBuffer styleAttr = new StringBuffer();
        styleAttr.append("left:" + collapsibleMenu.getLeft() + ";");
        styleAttr.append("top:" + collapsibleMenu.getTop() + ";");
        styleAttr.append("height:" + collapsibleMenu.getHeight() + ";");
        styleAttr.append("width:" + collapsibleMenu.getWidth() + ";");
        writer.writeAttribute(HTML.STYLE_ATTR, styleAttr.toString(), null);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.startElement(HTML.TABLE_ELEM, collapsibleMenu);
        writer.writeAttribute(HTML.CLASS_ATTR, "collapsiblePanel", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        
        Iterator it = collapsibleMenu.getChildren().iterator();
            
        while (it.hasNext()) {
            
            Object o = it.next();
            if (! (o instanceof CollapsiblePanel)) continue;
            
            CollapsiblePanel panel = (CollapsiblePanel) o;
            printPanelTitle(facesContext, panel);
            printPanelBody(facesContext, panel);
        }
        
        writer.endElement(HTML.TABLE_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.DIV_ELEM);
        
   }
   
   private void printPanelTitle(FacesContext facesContext, 
           CollapsiblePanel panel) throws IOException {
        
        ResponseWriter writer = facesContext.getResponseWriter();
        
        writer.startElement(HTML.TR_ELEM, panel);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        
        writer.startElement(HTML.TD_ELEM, panel);
        
        String clientId = panel.getClientId(facesContext);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        
        String onClickJavascript = "showPanel('" + clientId + "')";
        writer.writeAttribute(HTML.ONCLICK_ATTR, onClickJavascript, null);
        
        writer.writeAttribute(HTML.CLASS_ATTR, "groupHeader", null);
                
        String onMouseOver = "this.className='groupHeaderOver'";
        writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, onMouseOver, null);
                
        String onMouseOut = "this.className='groupHeader'";
        writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, onMouseOut, null);
                
        String onMouseDown = "this.className='groupHeaderClick'";
        writer.writeAttribute(HTML.ONMOUSEDOWN_ATTR, onMouseDown, null);
                
        String onMouseUp = "this.className='groupHeaderOver'";
        writer.writeAttribute(HTML.ONMOUSEUP_ATTR, onMouseUp, null);
                
        writer.writeAttribute(HTML.STYLE_ATTR, "cursor:pointer", null);
        
        
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.write(panel.getTitle());
        
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TD_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        writer.endElement(HTML.TR_ELEM);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
    }
    
   private void printPanelBody(FacesContext facesContext,
           CollapsiblePanel panel) throws IOException { 
           
       ResponseWriter writer = facesContext.getResponseWriter();
       writer.startElement(HTML.TR_ELEM, panel);
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                   
       writer.startElement(HTML.TD_ELEM, panel);
       writer.writeAttribute(HTML.CLASS_ATTR, "groupPanelHidden", null);
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                   
       writer.startElement(HTML.DIV_ELEM, panel);
       writer.writeAttribute(HTML.CLASS_ATTR, "clippedRegion", null);
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
                   
       Iterator icons = panel.getChildren().iterator();
       while (icons.hasNext()) {
               Object obj = icons.next();
               if (obj instanceof CollapsibleIcon) {
                   CollapsibleIcon icon = (CollapsibleIcon) obj;
                   printIcon(facesContext, icon);
               }
       }

       writer.endElement(HTML.DIV_ELEM);
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
       
       writer.endElement(HTML.TD_ELEM);
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
       
       writer.endElement(HTML.TR_ELEM);
       HtmlRendererUtils.writePrettyLineSeparator(facesContext);
       
   }
   
   
    private void printIcon(FacesContext facesContext, CollapsibleIcon icon) 
        throws IOException {
    
        ResponseWriter writer = facesContext.getResponseWriter();
        
        icon.encodeEnd(facesContext);
    }
    
    private void preprocessChildren(FacesContext facesContext, 
            CollapsibleMenu collapsibleMenu) {
        
        Iterator it = collapsibleMenu.getChildren().iterator();
        
        while (it.hasNext())
        {
            Object child = it.next();
            if (child instanceof UISelectItems) {
                createDynamicCollapsibleMenu(facesContext, collapsibleMenu, (UISelectItems) child);
            }
        }
    }
    
    //create the collapsible menu dynamically by pulling the menu options from the 
    //backing bean
    private void createDynamicCollapsibleMenu(FacesContext facesContext, 
            CollapsibleMenu collapsibleMenu, UISelectItems child) {
       
       Object value = ((UISelectItems)child).getValue();
              
       if (!(value instanceof NavigationMenuItem[])) {
           //log warning?
           return;
       }
       
       NavigationMenuItem [] panels = (NavigationMenuItem []) value;
              
       for (int i = 0; i < ((NavigationMenuItem [])value).length; i++) {
           
           NavigationMenuItem panel = panels[i];
           CollapsiblePanel uiPanel = (CollapsiblePanel) facesContext.getApplication().
               createComponent(CollapsiblePanel.COMPONENT_TYPE);
           collapsibleMenu.getChildren().add(uiPanel);
           
           UIComponentTagUtils.setStringProperty(facesContext, 
                   uiPanel, "title", panel.getLabel());           
           UIComponentTagUtils.setBooleanProperty(facesContext, 
                   uiPanel, "displayed", panel.getAction());
           
           uiPanel.getClientId(facesContext); //generates client id?
                                 
           NavigationMenuItem [] icons = panel.getNavigationMenuItems();
           
           for (int j = 0; j < icons.length; j++) 
           {
               NavigationMenuItem collapsibleIcon = icons[j]; 
               CollapsibleIcon uiIcon = (CollapsibleIcon) facesContext.getApplication().
                   createComponent(CollapsibleIcon.COMPONENT_TYPE);
               uiPanel.getChildren().add(uiIcon);
               uiIcon.setUrl(collapsibleIcon.getIcon());
               
               UIComponentTagUtils.setStringProperty(facesContext, 
                       uiIcon, "title", collapsibleIcon.getLabel());
               UIComponentTagUtils.setActionListenerProperty(facesContext, 
                       uiIcon, collapsibleIcon.getActionListener());
               UIComponentTagUtils.setActionProperty(facesContext, 
                       uiIcon, collapsibleIcon.getAction());
               
               uiIcon.getClientId(facesContext);
           }
           
       } //end for
    }
    
    private void addChildParametersToHref(UIComponent linkComponent,
            StringBuffer hrefBuf,
            boolean firstParameter,
            String charEncoding) throws IOException {
    
        for (Iterator it = getChildren(linkComponent).iterator(); it.hasNext(); )
        {
            UIComponent child = (UIComponent)it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter)child).getName();
                Object value = ((UIParameter)child).getValue();

                addParameterToHref(name, value, hrefBuf, firstParameter, charEncoding);
                firstParameter = false;
            }
        }
    }
    
    private static void addParameterToHref(String name, 
            Object value, StringBuffer hrefBuf, boolean firstParameter, 
            String charEncoding) throws UnsupportedEncodingException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Unnamed parameter value not allowed within command link.");
        }

        hrefBuf.append(firstParameter ? '?' : '&');
        hrefBuf.append(URLEncoder.encode(name, charEncoding));
        hrefBuf.append('=');
        if (value != null)
        {
            //UIParameter is no ConvertibleValueHolder, so no conversion possible
            hrefBuf.append(URLEncoder.encode(value.toString(), charEncoding));
        }
    }
    
  
}

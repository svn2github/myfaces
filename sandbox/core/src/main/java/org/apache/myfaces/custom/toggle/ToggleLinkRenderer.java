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
package org.apache.myfaces.custom.toggle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;
import org.apache.myfaces.shared_tomahawk.config.MyfacesConfig;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * Renderer for component HtmlAjaxChildComboBox
 * 
 * 
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Output"
 *   type = "org.apache.myfaces.ToggleLink"
 * 
 * @author Sharath Reddy
 */
public class ToggleLinkRenderer extends HtmlLinkRenderer {
    public static final int DEFAULT_MAX_SUGGESTED_ITEMS = 200;

    protected void renderOutputLinkStart(FacesContext facesContext,
            UIOutput output) throws IOException
    {
        if (UserRoleUtils.isEnabledOnUserRole(output))
        {        
            ResponseWriter writer = facesContext.getResponseWriter();
    
            //calculate href
            String href = org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils
                    .getStringValue(facesContext, output);
            
            if (href == null)
            {
                href = "";
            }
            if ("".equals(href) || "#".equals(href))
            {
                //If no value defined the default is #componentId where
                //componentId is the clientId of the component to be 
                //shown. This avoid that the page jumps to top on every click.
                ToggleLink toggleLink = (ToggleLink) output;
                String[] componentsToToggle = toggleLink.getFor().split(",");
                for (int i = 0; i < componentsToToggle.length; i++) {
                    String componentId = componentsToToggle[i].trim();
                    UIComponent componentToShow = toggleLink.findComponent(componentId);
                    if (componentToShow != null){
                        href = "#"+ componentToShow.getClientId(facesContext);
                        break;
                    }
                }                
            }
            if (getChildCount(output) > 0)
            {
                StringBuffer hrefBuf = new StringBuffer(href);
                addChildParametersToHref(facesContext, output, hrefBuf, (href
                        .indexOf('?') == -1), //first url parameter?
                        writer.getCharacterEncoding());
                href = hrefBuf.toString();
            }
            href = facesContext.getExternalContext().encodeResourceURL(href); //TODO: or encodeActionURL ?
    
            String clientId = output.getClientId(facesContext);
    
            //write anchor
            writer.startElement(HTML.ANCHOR_ELEM, output);
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
            writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
            writer.writeURIAttribute(HTML.HREF_ATTR, href, null);
            
            HtmlRendererUtils
                    .renderHTMLAttributes(
                            writer,
                            output,
                            org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.ANCHOR_PASSTHROUGH_ATTRIBUTES_WITHOUT_ONCLICK_WITHOUT_STYLE);
            HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_ATTR, HTML.STYLE_ATTR,
                    output.getAttributes().get(HTML.STYLE_ATTR));
            HtmlRendererUtils.renderHTMLAttribute(writer, HTML.STYLE_CLASS_ATTR, HTML.STYLE_CLASS_ATTR,
                    output.getAttributes().get(HTML.STYLE_CLASS_ATTR));
            
            HtmlRendererUtils.renderHTMLAttribute(writer, HTML.ONCLICK_ATTR, HTML.ONCLICK_ATTR, 
                    buildOnclickToggleFunction(facesContext,output));
            
            writer.flush();
        }
    }
    
    private String buildOnclickToggleFunction(FacesContext facesContext,
            UIOutput output) throws IOException
    {
        ToggleLink toggleLink = (ToggleLink) output;
        String[] componentsToToggle = toggleLink.getFor().split(",");
        StringBuffer idsToShow = new StringBuffer();
        for (int i = 0; i < componentsToToggle.length; i++) {
            String componentId = componentsToToggle[i].trim();
            UIComponent componentToShow = toggleLink.findComponent(componentId);
            if (componentToShow == null) {
                Log log = LogFactory.getLog(ToggleLinkRenderer.class);
                log.error("Unable to find component with id " + componentId);
                continue;
            }
            if( idsToShow.length() > 0 )
                idsToShow.append( ',' );
            idsToShow.append( componentToShow.getClientId(facesContext) );
        }
        
        String outputOnclick = toggleLink.getOnclick();
        StringBuffer onClick = new StringBuffer();
        
        if (outputOnclick != null)
        {
            onClick.append("var cf = function(){");
            onClick.append(outputOnclick);
            onClick.append('}');
            onClick.append(';');
            onClick.append("var oamSF = function(){");            
        }
        
        onClick.append(getToggleJavascriptFunctionName(facesContext, toggleLink) + "('"+idsToShow+"')");
        
        if (outputOnclick != null)
        {
            onClick.append('}');
            onClick.append(';');
            onClick.append("return (cf()==false)? false : oamSF();");        
        }

        return onClick.toString();
    }
    
    //This method is copied from HtmlLinkRendererBase.  
    private void addChildParametersToHref(FacesContext facesContext,
            UIComponent linkComponent, StringBuffer hrefBuf,
            boolean firstParameter, String charEncoding) throws IOException
    {
        boolean strictXhtmlLinks = MyfacesConfig.getCurrentInstance(
                facesContext.getExternalContext()).isStrictXhtmlLinks();
        for (Iterator it = getChildren(linkComponent).iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent) it.next();
            if (child instanceof UIParameter)
            {
                String name = ((UIParameter) child).getName();
                Object value = ((UIParameter) child).getValue();
                addParameterToHref(name, value, hrefBuf, firstParameter,
                        charEncoding, strictXhtmlLinks);
                firstParameter = false;
            }
        }
    }

    //This method is copied from HtmlLinkRendererBase.     
    private static void addParameterToHref(String name, Object value,
            StringBuffer hrefBuf, boolean firstParameter, String charEncoding,
            boolean strictXhtmlLinks) throws UnsupportedEncodingException
    {
        if (name == null)
        {
            throw new IllegalArgumentException(
                    "Unnamed parameter value not allowed within command link.");
        }

        if (firstParameter)
        {
            hrefBuf.append('?');
        }
        else
        {
            if (strictXhtmlLinks)
            {
                hrefBuf.append("&amp;");
            }
            else
            {
                hrefBuf.append('&');
            }
        }

        hrefBuf.append(URLEncoder.encode(name, charEncoding));
        hrefBuf.append('=');
        if (value != null)
        {
            //UIParameter is no ConvertibleValueHolder, so no conversion possible
            hrefBuf.append(URLEncoder.encode(value.toString(), charEncoding));
        }
    }
            
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.checkParamValidity(context, component, ToggleLink.class);

        if(((ToggleLink) component).isDisabled())
            return;

        super.encodeEnd(context, component);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.checkParamValidity(context, component, ToggleLink.class);

        ToggleLink toggleLink = (ToggleLink) component;
        if(toggleLink.isDisabled())
            return;


        super.encodeBegin(context, component);
    }
    
    private String getToggleJavascriptFunctionName(FacesContext context,ToggleLink toggleLink){
        for(UIComponent component = toggleLink.getParent(); component != null; component = component.getParent())
            if( component instanceof TogglePanel )
                return TogglePanelRenderer.getToggleJavascriptFunctionName( context, (TogglePanel)component );

        Log log = LogFactory.getLog(ToggleLinkRenderer.class);
        log.error("The ToggleLink component with id " + toggleLink.getClientId( context )+" isn't enclosed in a togglePanel.");
        return null;
    }
}

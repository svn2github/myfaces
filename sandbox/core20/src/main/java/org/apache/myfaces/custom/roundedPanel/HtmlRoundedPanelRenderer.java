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
package org.apache.myfaces.custom.roundedPanel;

import java.io.IOException;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFRenderer;
import org.apache.myfaces.shared_tomahawk.util.WebConfigParamUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * 
 * @author Leonardo Uribe
 *
 */
@JSFRenderer(renderKitId="HTML_BASIC", 
        family="org.apache.myfaces.custom.roundedPanel.HtmlRoundedPanel",
        type="org.apache.myfaces.custom.roundedPanel.HtmlRoundedPanel")
public class HtmlRoundedPanelRenderer extends Renderer
{
    private static final Log log = LogFactory.getLog(HtmlRoundedPanelRenderer.class);
    
    public static final String INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS = "org.apache.myfaces.ROUNDED_PANEL_MAX_RADIUS";
    public static final int INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS_DEFAULT = 30;
    
    public boolean getRendersChildren()
    {
        return true;
    }
    
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException
    {
    }
    
    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException
    {
    }
    
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        super.encodeEnd(context, component);
        
        AbstractHtmlRoundedPanel roundedPanel = (AbstractHtmlRoundedPanel) component;
        
        Integer maxRadius = WebConfigParamUtils.getIntegerInitParameter(context.getExternalContext(), 
                INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS, INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS_DEFAULT);
        
        if (roundedPanel.getRadius().intValue() > maxRadius.intValue())
        {
            log.error("Radius used for rounded panel is bigger than max allowed."
                    +maxRadius.toString()+
                    " Increase the value of "+INIT_PARAM_ROUNDED_PANEL_MAX_RADIUS+
                    " web config param or reduce the value in your component");
            return;
        }
        
        
        //Create properties from component properties
        RoundedPanelProperties roundedPanelProperties = new RoundedPanelProperties(roundedPanel.getRadius());
        
        roundedPanelProperties.setBackgroundColor(encodeColorProperty(roundedPanel.getBackgroundColor()));
        roundedPanelProperties.setColor(encodeColorProperty(roundedPanel.getColor()));
        
        if ("table".equals(roundedPanel.getLayout()))
        {
            encodeTableRoundedPanel(context, roundedPanel, roundedPanelProperties);
        }
        else
        {
            encodeDivRoundedPanel(context, roundedPanel, roundedPanelProperties);
        }
    }
    
    public void encodeDivRoundedPanel(FacesContext context, AbstractHtmlRoundedPanel roundedPanel,
            RoundedPanelProperties roundedPanelProperties) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "background:"+roundedPanel.getColor()+";"+roundedPanel.getStyle(), null);
        HtmlRendererUtils.renderHTMLAttribute(writer, roundedPanel, JSFAttr.STYLE_CLASS_ATTR, HTML.CLASS_ATTR);
        
        String cornerHeightCss = "height: "+roundedPanel.getRadius().toString()+"px; font-size: 1px;";
        
        writer.startElement(HTML.DIV_ELEM, null);
        Resource tr = getImageResource(context, roundedPanel, roundedPanelProperties,  
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_TOP_RIGHT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+tr.getRequestPath()+") no-repeat top right;"+cornerHeightCss, null);
        
        writer.startElement(HTML.DIV_ELEM, null);
        Resource tl = getImageResource(context, roundedPanel, roundedPanelProperties,  
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_TOP_LEFT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+tl.getRequestPath()+") no-repeat top left;"+cornerHeightCss, null);
        
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        
        writer.startElement(HTML.DIV_ELEM, null);
        HtmlRendererUtils.renderHTMLAttribute(writer, roundedPanel, "contentStyle", HTML.STYLE_ATTR);
        HtmlRendererUtils.renderHTMLAttribute(writer, roundedPanel, "contentStyleClass", HTML.CLASS_ATTR);
        RendererUtils.renderChildren(context, roundedPanel);
        writer.endElement(HTML.DIV_ELEM);
        
        writer.startElement(HTML.DIV_ELEM, null);
        Resource br = getImageResource(context, roundedPanel, roundedPanelProperties,  
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_BOTTOM_RIGHT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+br.getRequestPath()+") no-repeat bottom right;"+cornerHeightCss, null);
        
        writer.startElement(HTML.DIV_ELEM, null);
        Resource bl = getImageResource(context, roundedPanel, roundedPanelProperties,  
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_BOTTOM_LEFT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+bl.getRequestPath()+") no-repeat bottom left;"+cornerHeightCss, null);
        
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        
        writer.endElement(HTML.DIV_ELEM);
    }

    public void encodeTableRoundedPanel(FacesContext context, AbstractHtmlRoundedPanel roundedPanel,
            RoundedPanelProperties roundedPanelProperties) throws IOException
    {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
        writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
        writer.writeAttribute(HTML.BORDER_ATTR, "0", null);
        writer.writeAttribute(HTML.STYLE_ATTR, "background-color: "+roundedPanel.getColor()+";"+roundedPanel.getStyle(), null);
        HtmlRendererUtils.renderHTMLAttribute(writer, roundedPanel, JSFAttr.STYLE_CLASS_ATTR, HTML.CLASS_ATTR);
        
        writer.startElement(HTML.TBODY_ELEM, null);
        
        writer.startElement(HTML.TR_ELEM, null);
        
        String cornerHeightCss = "height: "+roundedPanel.getRadius().toString()+"px; width:"+roundedPanel.getRadius().toString()+"; font-size: 1px;";
        writer.startElement(HTML.TD_ELEM, null);
        Resource tl = getImageResource(context, roundedPanel, roundedPanelProperties,  
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_TOP_LEFT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+tl.getRequestPath()+") no-repeat top left;"+cornerHeightCss, null);
        writer.endElement(HTML.TD_ELEM);
        writer.startElement(HTML.TD_ELEM, null);
        writer.endElement(HTML.TD_ELEM);
        writer.startElement(HTML.TD_ELEM, null);
        Resource tr = getImageResource(context, roundedPanel, roundedPanelProperties, 
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_TOP_RIGHT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+tr.getRequestPath()+") no-repeat top right;"+cornerHeightCss, null);
        writer.endElement(HTML.TD_ELEM);
        
        writer.endElement(HTML.TR_ELEM);

        writer.startElement(HTML.TR_ELEM, null);
        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute(HTML.COLSPAN_ATTR, "3", null);
        HtmlRendererUtils.renderHTMLAttribute(writer, roundedPanel, "contentStyle", HTML.STYLE_ATTR);
        HtmlRendererUtils.renderHTMLAttribute(writer, roundedPanel, "contentStyleClass", HTML.CLASS_ATTR);
        RendererUtils.renderChildren(context, roundedPanel);
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEM);
        
        writer.startElement(HTML.TR_ELEM, null);
        
        writer.startElement(HTML.TD_ELEM, null);
        Resource bl = getImageResource(context, roundedPanel, roundedPanelProperties, 
                        RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_BOTTOM_LEFT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+bl.getRequestPath()+") no-repeat bottom left;"+cornerHeightCss, null);
        writer.endElement(HTML.TD_ELEM);
        writer.startElement(HTML.TD_ELEM, null);
        writer.endElement(HTML.TD_ELEM);
        writer.startElement(HTML.TD_ELEM, null);
        Resource br = getImageResource(context, roundedPanel, roundedPanelProperties, 
                RoundedPanelPropertiesBuilder.ROUNDED_PANEL_PATTERN_BASIC, RoundedPanelPropertiesBuilder.SECTION_BOTTOM_RIGHT);
        writer.writeAttribute(HTML.STYLE_ATTR, "background: url("+br.getRequestPath()+") no-repeat bottom right;"+cornerHeightCss, null);
        writer.endElement(HTML.TD_ELEM);
        
        writer.endElement(HTML.TR_ELEM);
        
        writer.endElement(HTML.BODY_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }
    
    protected Resource getImageResource(FacesContext context, AbstractHtmlRoundedPanel roundedPanel, 
            RoundedPanelProperties roundedPanelProperties, String roundedPanelPattern, String imagePart)
    {
        Resource r = null;
        String resourceName = generateResourceName(roundedPanelProperties, 
                roundedPanelPattern, imagePart); 
        if (roundedPanel.getLibrary() != null)
        {
            r = context.getApplication().getResourceHandler().createResource(resourceName, roundedPanel.getLibrary());
        }
        if (r == null)
        {
            r = context.getApplication().getResourceHandler().createResource(resourceName, RoundedPanelResourceHandlerWrapper.ROUNDED_PANEL_LIBRARY);
        }
        return r;
    }
    
    public String generateResourceName(RoundedPanelProperties roundedPanelProperties,
            String roundedPanelPattern, String imagePart)
    {
        return roundedPanelPattern +"_"+ (imagePart == null ? "all" : imagePart) + encodeRoundedPanelProperties(roundedPanelProperties) + ".png";
    }

    private String encodeRoundedPanelProperties(RoundedPanelProperties roundedPanelProperties)
    {
        return roundedPanelProperties.getEncodedRoundedPanelProperties();
    }

    protected String encodeColorProperty(String color)
    {
        if (color == null)
        {
            return null;
        }
        if (color.length() > 0)
        {
            String returnedColor = HtmlColorUtils.encodeColorProperty(color);
            if (returnedColor == null)
            {
                if (log.isWarnEnabled())
                {
                    log.warn("Cannot encode color:"+color);
                }
            }
            return returnedColor;
        }
        else
        {
            return null;
        }
    }
    
}

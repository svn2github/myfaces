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
package org.apache.myfaces.custom.swapimage;

import java.io.IOException;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.navigation.HtmlCommandNavigation;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Graphic"
 *   type = "org.apache.myfaces.SwapImage"
 * 
 * @author Thomas Spiegl
 * @version $Revision: 709234 $ $Date: 2008-10-30 15:15:18 -0500 (jue, 30 oct 2008) $
 */
@ResourceDependency(library="oam.custom.swapimage",name="swapimage.js")
public class HtmlSwapImageRenderer
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlSwapImageRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, UIGraphic.class);

        ResponseWriter writer = facesContext.getResponseWriter();

        //AddResourceFactory.getInstance(facesContext).addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN,
        //        HtmlSwapImage.class, "swapimage.js");

        String url;
        if (uiComponent instanceof HtmlSwapImage)
        {
            if (uiComponent.getParent() instanceof HtmlCommandNavigation)
            {
                url = ((HtmlCommandNavigation) uiComponent.getParent()).isActive() ?
                    ((HtmlSwapImage) uiComponent).getActiveImageUrl() : ((HtmlSwapImage)uiComponent).getUrl();
            }
            else
            {
                url = ((HtmlSwapImage)uiComponent).getUrl();
            }
        }
        else
        {
            url = (String)uiComponent.getAttributes().get(JSFAttr.URL_ATTR);
        }

        if ((url != null) && (url.length() > 0))
        {
            writer.startElement(HTML.IMG_ELEM, uiComponent);

            renderId(facesContext, uiComponent);

            String src = facesContext.getApplication()
                    .getViewHandler().getResourceURL(facesContext, url);
            writer.writeURIAttribute(HTML.SRC_ATTR,
                                     facesContext.getExternalContext().encodeResourceURL(src),
                                     null);

            if (uiComponent instanceof HtmlSwapImage)
            {
                String swapImageUrl = ((HtmlSwapImage) uiComponent).getSwapImageUrl();
                swapImageUrl = facesContext.getApplication()
                .getViewHandler().getResourceURL(facesContext, swapImageUrl);

                if (swapImageUrl != null)
                {
                    writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, "SI_MM_swapImage('" + getClientId(facesContext, uiComponent) + "','','" + facesContext.getExternalContext().encodeResourceURL(swapImageUrl) + "',1);", null);
                    writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, "SI_MM_swapImgRestore();", null);
                }
            }

            HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.IMG_PASSTHROUGH_ATTRIBUTES_WITHOUT_ONMOUSEOVER_AND_ONMOUSEOUT);

            writer.endElement(HTML.IMG_ELEM);
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("Graphic with id " + uiComponent.getClientId(facesContext) + " has no value (url).");
        }
    }
    
    protected boolean shouldRenderId(
            FacesContext context,
            UIComponent  component)
    {
        //Always return true, since javascript needs it rendered
        return true;
    }
}

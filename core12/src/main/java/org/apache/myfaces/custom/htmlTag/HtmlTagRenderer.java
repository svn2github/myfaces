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
package org.apache.myfaces.custom.htmlTag;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.util.HtmlComponentUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Output"
 *   type = "org.apache.myfaces.HtmlTagRenderer"
 * 
 * @author bdudney (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTagRenderer extends HtmlRenderer
{
    public static final String RENDERER_TYPE = "org.apache.myfaces.HtmlTagRenderer";

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        if ((context == null) || (component == null))
        {
            throw new NullPointerException();
        }
        HtmlTag htmlTag = (HtmlTag) component;

        if (htmlTag.isRendered())
        {
            String tag = htmlTag.getValue().toString();
            if( tag.trim().length() == 0 ) // Don't render the tag, but render the children.
                return;

            ResponseWriter writer = context.getResponseWriter();

            writer.startElement(tag, htmlTag);
            HtmlRendererUtils.writeIdIfNecessary(writer, htmlTag, context);

            // TODO : Use HtmlRendererUtils.renderHTMLAttributes(writer, component, HTML.COMMON_PASSTROUGH_ATTRIBUTES);
            String[] supportedAttributes = {HTML.STYLE_CLASS_ATTR, HTML.STYLE_ATTR};
            HtmlRendererUtils.renderHTMLAttributes(writer, htmlTag, supportedAttributes);
            
            // write additional attributes supplied by f:param tags
            Map params = HtmlComponentUtils.getParameterMap(htmlTag);
            for(Iterator iter = params.entrySet().iterator(); iter.hasNext();)
            {
                Entry param = (Entry) iter.next();
                writer.writeAttribute(param.getKey().toString(), param.getValue().toString(), null);
            }
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException
    {
        RendererUtils.renderChildren(context, component);
    }
    
    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException
    {
        if ((context == null) || (component == null))
        {
            throw new NullPointerException();
        }
        HtmlTag htmlTag = (HtmlTag) component;

        if (htmlTag.isRendered())
        {
            String tag = htmlTag.getValue().toString();
            if( tag.trim().length() == 0 )
                return;

            ResponseWriter writer = context.getResponseWriter();
            // force separate end tag
            writer.writeText("", null);            
            writer.endElement( tag );
        }
    }
}

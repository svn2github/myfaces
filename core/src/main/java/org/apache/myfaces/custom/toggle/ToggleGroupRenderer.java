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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlGroupRendererBase;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * 
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Panel"
 *   type = "org.apache.myfaces.ToggleGroup"
 * 
 */
public class ToggleGroupRenderer extends HtmlGroupRendererBase {

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ToggleGroup toggleGroup = (ToggleGroup) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement( org.apache.myfaces.shared_tomahawk.renderkit.html.HTML.SPAN_ELEM, component );
        writer.writeAttribute(HTML.ID_ATTR, component.getClientId(context), null);

        HtmlRendererUtils.renderHTMLAttributes( writer, component, HTML.COMMON_PASSTROUGH_ATTRIBUTES );

        RendererUtils.renderChildren( context, component );

        writer.endElement( HTML.SPAN_ELEM );
    }
}

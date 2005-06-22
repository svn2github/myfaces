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
package org.apache.myfaces.custom.htmlTag;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
/**
 * @author bdudney (latest modification by $Author$)
 * @version $Revision$ $Date: 2005-05-11 11:47:12 -0400 (Wed, 11 May 2005) $
 */
public class HtmlTagRenderer extends HtmlRenderer {
  public static final String RENDERER_TYPE = "org.apache.myfaces.HtmlTagRenderer";

  public void encodeBegin(FacesContext context, UIComponent component)
      throws IOException {
    if ((context == null) || (component == null)) {
      throw new NullPointerException();
    }
    HtmlTag htmlTag = (HtmlTag) component;
    ResponseWriter writer = context.getResponseWriter();

    writer.startElement(htmlTag.getValue().toString(), htmlTag);
    HtmlRendererUtils.writeIdIfNecessary(writer, component, context);

    String styleClass = htmlTag.getStyleClass();
    String style = htmlTag.getStyle();

    if(null != styleClass) {
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
    }
    if(null != style) {
        writer.writeAttribute(HTML.STYLE_ATTR, style, null);
    }
  }

  public void encodeEnd(FacesContext context, UIComponent component)
      throws IOException {
    if ((context == null) || (component == null)) {
      throw new NullPointerException();
    }
    HtmlTag htmlTag = (HtmlTag) component;
    ResponseWriter writer = context.getResponseWriter();
    writer.endElement(htmlTag.getValue().toString());
  }
}
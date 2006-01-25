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
package org.apache.myfaces.custom.stylesheet;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
*/

public class StylesheetRenderer extends HtmlRenderer {

	 public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        Stylesheet stylesheet = (Stylesheet) component;
        ResponseWriter writer = context.getResponseWriter();
        
        if(stylesheet.isInline())
        {
          //include as inline css
          writer.startElement("style", component);
          writer.writeAttribute("type", "text/css", null);
          if(stylesheet.getMedia() != null)
          {
            writer.writeAttribute("media", stylesheet.getMedia(), null);
          }          
          //writer.writeText("<!--\n", null);
          Object text = RendererUtils.loadResourceFile(context, stylesheet.getPath());
          if (text != null)
          {
              writer.writeText(text, null);
          }
          //writer.writeText("\n-->", null);
          writer.endElement("style");  
        }
        else
        {
          //refere as link-element
          writer.startElement("link", component);
          writer.writeAttribute("rel", "stylesheet", null);
          writer.writeAttribute("type", "text/css", null);
          if(stylesheet.getMedia() != null)
          {
            writer.writeAttribute("media", stylesheet.getMedia(), null);
          }
          writer.writeURIAttribute
              ("href",
               context.getExternalContext().getRequestContextPath()+stylesheet.getPath(),
             "path");
          writer.endElement("link");          
        }
        

    }
}
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.HtmlRenderer;

/**
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
*/

public class StylesheetRenderer extends HtmlRenderer {

	 private static final Log LOG = LogFactory.getLog(StylesheetRenderer.class);
      
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
          writer.writeText(loadFile(context.getExternalContext().getRequestContextPath(), stylesheet.getPath()), null);
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

  private Object loadFile(String ctxPath, String file)
  {
    
    String href = ctxPath+file;
    LOG.debug("loadFile: " + href);
    
    File cssFile = new File(href);
    String content;
    


    if (cssFile.canRead())
    {
      FileInputStream in;
      try {
        in = new FileInputStream(cssFile);
        byte[] fileBuffer = new byte[(int) cssFile.length()];
        in.read(fileBuffer);
        in.close();
        content = new String(fileBuffer);
      }
      catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        content=null;
      }
      catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        content=null;
      }

    }
    else
    {
      LOG.error("File not readable: " + href);
      content=null;
    }
    
    return content;
  }
}
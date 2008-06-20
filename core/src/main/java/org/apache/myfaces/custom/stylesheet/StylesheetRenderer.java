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
package org.apache.myfaces.custom.stylesheet;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.stylesheet.TextResourceFilter.ResourceInfo;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;

/**
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Output"
 *   type = "org.apache.myfaces.Stylesheet"
 * 
 * @author mwessendorf (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StylesheetRenderer extends HtmlRenderer
{
	public void encodeEnd(FacesContext context, UIComponent component)
		throws IOException
	{

		if ((context == null) || (component == null))
		{
			throw new NullPointerException();
		}
		Stylesheet stylesheet = (Stylesheet) component;
		ResponseWriter writer = context.getResponseWriter();
		
		//A path starting with / or not is the same for this component,
		//because ctx.getExternalContext().getResourceAsStream(file);
		//or ServletContext.getResourceAsStream() specifies that
		//ALL resources must start with '/'
		String path = stylesheet.getPath(); 
        if (path.startsWith("/"))
        {
            path = path.substring(1);
        }	

		if (stylesheet.isInline())
		{
			//include as inline css
			writer.startElement("style", component);
			writer.writeAttribute("type", "text/css", null);
			if (stylesheet.getMedia() != null)
			{
				writer.writeAttribute("media", stylesheet.getMedia(), null);
			}
			//writer.writeText("<!--\n", null);

			Object text;
			if (stylesheet.isFiltered())
			{
			    ResourceInfo info = TextResourceFilter.getInstance(context).getOrCreateFilteredResource(context, path); 
				text = info.getText();
			}
			else
			{
				text = RendererUtils.loadResourceFile(context,'/'+ path);
			}
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
			if (stylesheet.getMedia() != null)
			{
				writer.writeAttribute("media", stylesheet.getMedia(), null);
			}

			String stylesheetPath;
			if (stylesheet.isFiltered())
			{
				TextResourceFilter.getInstance(context).getOrCreateFilteredResource(context, path);
				stylesheetPath = AddResourceFactory.getInstance(context).getResourceUri(context, TextResourceFilterProvider.class, path, true);
			}
			else
			{
				stylesheetPath = context.getApplication().getViewHandler().getResourceURL(context, path);
			}

			writer.writeURIAttribute("href", stylesheetPath, "path");
			writer.endElement("link");
		}
	}
}

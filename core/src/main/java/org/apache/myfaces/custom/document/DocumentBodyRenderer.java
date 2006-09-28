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
package org.apache.myfaces.custom.document;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.renderkit.html.util.ExtensionsPhaseListener;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * Document to enclose the whole document. If not otherwise possible you can use
 * state="start|end" to demarkate the document boundaries
 *
 * @author Mario Ivankovits (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class DocumentBodyRenderer extends AbstractDocumentRenderer
{
	public static final String RENDERER_TYPE = "org.apache.myfaces.DocumentBody";
	private String BODY_ELEM = "body";
	private String[] ATTRS = new String[] {"onload", "onunload", "onresize", "onkeypress"};

	protected String getHtmlTag()
	{
		return BODY_ELEM;
	}

	protected Class getDocumentClass()
	{
		return DocumentBody.class;
	}

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    	throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(BODY_ELEM, uiComponent);
        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, ATTRS);
        writer.endElement(BODY_ELEM);
    }

	protected void writeBeforeEnd(FacesContext facesContext) throws IOException
	{
		super.writeBeforeEnd(facesContext);
       	ExtensionsPhaseListener.writeCodeBeforeBodyEnd(facesContext);

		// fake string, so the ExtensionsPhaseListener will not create the javascript again
		facesContext.getExternalContext().getRequestMap().put(ExtensionsPhaseListener.ORG_APACHE_MYFACES_MY_FACES_JAVASCRIPT, "");
	}
}
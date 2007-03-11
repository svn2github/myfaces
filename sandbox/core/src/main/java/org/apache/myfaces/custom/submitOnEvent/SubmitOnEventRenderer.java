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
package org.apache.myfaces.custom.submitOnEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;

/**
 * Attach an event handler to an input element or use a global event handler to
 * submit a form by "clicking" on a link or button
 *
 * @version $Id$
 */
public class SubmitOnEventRenderer extends HtmlRenderer
{
	private final static Set ON_CHANGE_FAMILY = new TreeSet(Arrays.asList(new String[]
	{
		UISelectBoolean.COMPONENT_FAMILY,
		UISelectMany.COMPONENT_FAMILY,
		UISelectOne.COMPONENT_FAMILY
	}));

	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        SubmitOnEvent submitOnEvent = (SubmitOnEvent) uiComponent;

        UIComponent forComponent = null;

		UIComponent triggerComponent = null;
        UIComponent parent = uiComponent.getParent();
        if (parent != null)
        {
            if (UIInput.COMPONENT_FAMILY.equals(parent.getFamily()) || parent instanceof UIInput)
            {
                triggerComponent = parent;
            }
            else if (UICommand.COMPONENT_FAMILY.equals(parent.getFamily()) || parent instanceof UICommand)
            {
                forComponent = parent;
            }
        }

		if (triggerComponent != null && !triggerComponent.isRendered())
		{
			// the component we are attaced to is not being rendered, so we can quit now ...
			return;
		}

		if (forComponent == null)
        {
            String forComponentId = submitOnEvent.getFor();
            if (forComponentId == null)
            {
                throw new IllegalArgumentException("SubmitOnEvent: attribute 'for' missing");
            }

            forComponent = uiComponent.findComponent(forComponentId);
			if (forComponent == null)
            {
                throw new IllegalArgumentException("SubmitOnEvent: can't find 'for'-component '" + forComponentId + "'");
            }
        }

		AddResourceFactory.getInstance(facesContext).addJavaScriptAtPosition(
				facesContext, AddResource.HEADER_BEGIN, SubmitOnEventRenderer.class,
				"submitOnEvent.js");

        StringBuffer js = new StringBuffer(80);
        js.append("setTimeout(\"");
        js.append("orgApacheMyfacesSubmitOnEventRegister('");
        if (submitOnEvent.getEvent() != null)
        {
            js.append(submitOnEvent.getEvent().toLowerCase());
        }
        else
        {
			if (triggerComponent != null
				&& triggerComponent.getFamily() != null
				&& ON_CHANGE_FAMILY.contains(triggerComponent.getFamily()))
			{
				js.append("change");
			}
			else
			{
				js.append("keypress");
			}
		}
        js.append("','");
        if (submitOnEvent.getCallback() != null)
        {
            js.append(submitOnEvent.getCallback());
        }
        js.append("','");
        if (triggerComponent != null)
        {
            js.append(triggerComponent.getClientId(facesContext));
        }
        js.append("','");
        js.append(forComponent.getClientId(facesContext));
        // js.append("');");
        js.append("');\", 50)");

        // AddResourceFactory.getInstance(facesContext).addInlineScriptAtPosition(facesContext, AddResource.BODY_END, js.toString());

        ResponseWriter out = facesContext.getResponseWriter();
        out.startElement(HTML.SCRIPT_ELEM, uiComponent);
        out.writeAttribute(HTML.SCRIPT_LANGUAGE_ATTR, HTML.SCRIPT_LANGUAGE_JAVASCRIPT, null);
        out.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        out.writeText(js.toString(), null);
        out.endElement(HTML.SCRIPT_ELEM);
    }
}

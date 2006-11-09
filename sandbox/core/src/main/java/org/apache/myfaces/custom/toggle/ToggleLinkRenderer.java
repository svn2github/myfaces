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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.ext.HtmlLinkRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

/**
 * Renderer for component HtmlAjaxChildComboBox
 * 
 * @author Sharath Reddy
 */
public class ToggleLinkRenderer extends HtmlLinkRenderer {
    public static final int DEFAULT_MAX_SUGGESTED_ITEMS = 200;

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.checkParamValidity(context, component, ToggleLink.class);
        
        if(((ToggleLink) component).isDisabled())
        	return;
        	
        super.encodeEnd(context, component);

        // render the hidden input field
        ResponseWriter writer = context.getResponseWriter();
        ToggleLink toggleLink = (ToggleLink) component;
        String hiddenFieldId = toggleLink.getClientId(context) + "_hidden";

        writer.startElement(HTML.INPUT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HTML.ID_ATTR, hiddenFieldId, null);
        writer.writeAttribute(HTML.NAME_ATTR, hiddenFieldId, null);

        String value = new Boolean(toggleLink.getEditMode()).toString();
        writer.writeAttribute(HTML.VALUE_ATTR, value, null);

        writer.endElement(HTML.INPUT_ELEM);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.checkParamValidity(context, component, ToggleLink.class);
        
        ToggleLink toggleLink = (ToggleLink) component;
        if(toggleLink.isDisabled())
        	return;

        this.writeJavascriptToToggleVisibility(context, toggleLink);

        String functionName = this.getJavascriptFunctionName(context, toggleLink);
        toggleLink.setOnclick(functionName + "(true);");

        super.encodeBegin(context, component);
    }
 
    // Generate the javascript function to hide the Link component
    // and display the components specified in the 'for' attribute
    private void writeJavascriptToToggleVisibility(FacesContext context, ToggleLink toggleLink) throws IOException {

        ResponseWriter out = context.getResponseWriter();

        out.startElement(HTML.SCRIPT_ELEM, null);
        out.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        String functionName = getJavascriptFunctionName(context, toggleLink);

        out.write(functionName + " = function() { ");
        out.write("var toggleLink = document.getElementById(");
        out.write("'" + toggleLink.getClientId(context) + "'");
        out.write(");");
        out.write("\n");
        out.write("toggleLink.style.display = 'none';\n");

        String[] componentsToToggle = toggleLink.getFor().split(",");

        for (int i = 0; i < componentsToToggle.length; i++) {
            String componentId = componentsToToggle[i].trim();

            UIComponent component = toggleLink.findComponent(componentId);

            if (component == null) {
                Log log = LogFactory.getLog(ToggleLinkRenderer.class);
                log.error("Unable to find component with id " + componentId);
                continue;
            }

            out.write("var component" + i + " = document.getElementById(");
            out.write("'" + component.getClientId(context) + "'");
            out.write(");\n");
            out.write("component" + i + ".style.display = 'inline';\n");
        }

        // toggle the value of the hidden field
        out.write("var hiddenField = document.getElementById(");
        out.write("'" + toggleLink.getClientId(context) + "_hidden'");
        out.write(");\n");
        out.write("hiddenField.value = 'true';");

        out.write("};");
        out.endElement(HTML.SCRIPT_ELEM);

    }

    private String getJavascriptFunctionName(FacesContext context, ToggleLink toggleLink) {
        String modifiedId = toggleLink.getClientId(context).replaceAll("\\:", "_");
        return "toggle_" + modifiedId;
    }

}

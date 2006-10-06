/*
 * Copyright 2004-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
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
public class ToggleOutputLinkRenderer extends HtmlLinkRenderer {
    public static final int DEFAULT_MAX_SUGGESTED_ITEMS = 200;

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.checkParamValidity(context, component, ToggleOutputLink.class);
        super.encodeEnd(context, component);

        // render the hidden input field
        ResponseWriter writer = context.getResponseWriter();
        ToggleOutputLink toggleOutputLink = (ToggleOutputLink) component;
        String hiddenFieldId = toggleOutputLink.getClientId(context) + "_hidden";

        writer.startElement(HTML.INPUT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        writer.writeAttribute(HTML.ID_ATTR, hiddenFieldId, null);
        writer.writeAttribute(HTML.NAME_ATTR, hiddenFieldId, null);

        String value = new Boolean(toggleOutputLink.getEditMode()).toString();
        writer.writeAttribute(HTML.VALUE_ATTR, value, null);

        writer.endElement(HTML.INPUT_ELEM);
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        RendererUtils.checkParamValidity(context, component, ToggleOutputLink.class);

        ToggleOutputLink toggleOutputLink = (ToggleOutputLink) component;

        this.writeJavascriptToToggleVisibility(context, toggleOutputLink);

        String functionName = this.getJavascriptFunctionName(context, toggleOutputLink);
        toggleOutputLink.setOnclick(functionName + "(true);");

        super.encodeBegin(context, component);
    }

    // Generate the javascript function to hide the Link component
    // and display the components specified in the 'for' attribute
    private void writeJavascriptToToggleVisibility(FacesContext context, ToggleOutputLink toggleOutputLink) throws IOException {

        ResponseWriter out = context.getResponseWriter();

        out.startElement(HTML.SCRIPT_ELEM, null);
        out.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        String functionName = getJavascriptFunctionName(context, toggleOutputLink);

        out.write(functionName + " = function() { ");
        out.write("var toggleOutputLink = document.getElementById(");
        out.write("'" + toggleOutputLink.getClientId(context) + "'");
        out.write(");");
        out.write("\n");
        out.write("toggleOutputLink.style.display = 'none';\n");

        String[] componentsToToggle = toggleOutputLink.getFor().split(",");

        for (int i = 0; i < componentsToToggle.length; i++) {
            String componentId = componentsToToggle[i].trim();

            UIComponent component = toggleOutputLink.findComponent(componentId);

            if (component == null) {
                Log log = LogFactory.getLog(ToggleOutputLinkRenderer.class);
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
        out.write("'" + toggleOutputLink.getClientId(context) + "_hidden'");
        out.write(");\n");
        out.write("hiddenField.value = 'true';");

        out.write("};");
        out.endElement(HTML.SCRIPT_ELEM);

    }

    private String getJavascriptFunctionName(FacesContext context, ToggleOutputLink toggleOutputLink) {
        String modifiedId = toggleOutputLink.getClientId(context).replaceAll("\\:", "_");
        return "toggle_" + modifiedId;
    }

}

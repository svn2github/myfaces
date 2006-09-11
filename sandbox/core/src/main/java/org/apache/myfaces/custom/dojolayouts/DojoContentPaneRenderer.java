/**
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

package org.apache.myfaces.custom.dojolayouts;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.collections.map.HashedMap;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

/**
 * Simple renderer to the dojo content pane renders a simple div and the
 * javascripted initializer we do not use the dojo taglib mechanism here due to
 * the fact that the dojo tag system is incompatible with adf and other projects
 * 
 * @author werpu
 * 
 */
public class DojoContentPaneRenderer extends HtmlRenderer {

    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);

    }

    protected void encodeJavascriptBegin(FacesContext context, UIComponent component) throws IOException {
        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        try {
            if (javascriptLocation != null) {
                DojoUtils.addMainInclude(context, component, javascriptLocation, new DojoConfig());
            } else {
                DojoUtils.addMainInclude(context, component, null, new DojoConfig());
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        DojoUtils.addRequire(context, component, "dojo.widget.ContentPane");
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        Boolean rendered = (Boolean) component.getAttributes().get("rendered");

        if ((rendered != null) && (!rendered.booleanValue()))
            return;
        encodeJavascriptBegin(context, component);

        super.encodeBegin(context, component);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.DIV_ELEM, component);
        HtmlRendererUtils.writeIdIfNecessary(writer, component, context);

        DojoContentPane pane = (DojoContentPane) component;

        String styleClass = pane.getStyleClass();
        String style = pane.getStyle();
        if (null != styleClass) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }
        if (null != style) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }

    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // RendererUtils.renderChildren(context, component);
        // HtmlRendererUtils.writePrettyLineSeparator(context);
        super.encodeChildren(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
        encodeJavascriptEnd(context, component);
    }

    protected void encodeJavascriptEnd(FacesContext context, UIComponent component) throws IOException {
        DojoContentPane pane = (DojoContentPane) component;
        Map attributes = new HashedMap();
        attributes.put("sizeShare", pane.getSizeShare());
        String panelComponentVar = DojoUtils.calculateWidgetVarName(component.getClientId(context));
        attributes.put("id", panelComponentVar);

        // optional incoming dojo attrs
        if (pane.getAdjustPaths() != null)
            attributes.put("adjustPaths", pane.getAdjustPaths());
        if (pane.getExecuteScripts() != null)
            attributes.put("executeScripts", pane.getExecuteScripts());
        if (pane.getExtractContent() != null)
            attributes.put("extractContent", pane.getExtractContent());
        if (pane.getHandler() != null)
            attributes.put("handler", pane.getHandler());
        if (pane.getHref() != null)
            attributes.put("href", pane.getHref());
        if (pane.getLayoutAlign() != null)
            attributes.put("layoutAlign", pane.getLayoutAlign());
        if (pane.getParseContent() != null)
            attributes.put("parseContent", pane.getParseContent());
        if (pane.getPreload() != null)
            attributes.put("preload", pane.getPreload());
        if (pane.getRefreshOnShow() != null)
            attributes.put("refreshOnShow", pane.getRefreshOnShow());

        DojoUtils.renderWidgetInitializationCode(context, component, "ContentPane", attributes);
    }

    public boolean getRendersChildren() {
        return true;
    }

}

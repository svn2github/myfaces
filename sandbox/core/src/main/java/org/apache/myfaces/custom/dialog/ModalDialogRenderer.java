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

package org.apache.myfaces.custom.dialog;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

public class ModalDialogRenderer extends HtmlRenderer {
    public static final String RENDERER_TYPE = "org.apache.myfaces.ModalDialog";

    public static final String DIV_ID_PREFIX = "_div";

    //@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        DojoUtils.addMainInclude(context, component, javascriptLocation,
                                 DojoUtils.getDjConfigInstance(context));
        DojoUtils.addRequire(context, component, "dojo.widget.Dialog");

        writeModalDialogBegin((ModalDialog) component, context.getResponseWriter());
    }

    //@Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        StringBuffer buf = new StringBuffer();

        buf.append("</div>");

        context.getResponseWriter().write(buf.toString());
    }

    private void appendHiderIds(StringBuffer buf, ModalDialog dlg) {
        String[] hiders = null;
        if (dlg.getHiderIds() != null) {
            hiders = dlg.getHiderIds().split(",");
        }

        for (int i = 0; i < hiders.length; i++) {
            String varName = "btn" + i;
            buf.append("var ").append(varName).append(" = document.getElementById(\"")
            .append(hiders[i].trim()).append("\");")
            .append(dlg.getDialogVar()).append(".setCloseControl(").append(varName).append(");");
        }
    }

    private void appendDialogAttributes(StringBuffer buf, ModalDialog dlg) {
        if(dlg.getDialogAttr() == null)
            return;

        StringTokenizer it = new StringTokenizer(dlg.getDialogAttr(), " ");
        while(it.hasMoreElements())
        {
            String[] pair = it.nextToken().split("=");
            String attribute = pair[0];
            String value = pair[1].replaceAll("'", "");
            try {
                Double number = new Double(value);
            }
            catch(NumberFormatException e) {
                value = new StringBuffer("\"").append(value).append("\"").toString();
            }
            buf.append(attribute).append(":").append(value).append(", ");
        }
        buf.setLength(buf.length() - 2);
    }

    private void writeModalDialogBegin(ModalDialog dlg, ResponseWriter writer) throws IOException {
        StringBuffer buf = new StringBuffer();
        buf.append("<script type=\"text/javascript\">")
        .append("var ").append(dlg.getDialogVar()).append(";")
        .append("function "+dlg.getDialogVar()+"_loader(e) {").append(dlg.getDialogVar())
        .append(" = dojo.widget.createWidget(\"dialog\", {id:")
        .append("\"").append(dlg.getDialogId()).append("\", ");

        appendDialogAttributes(buf, dlg);

        String dlgId = dlg.getId() != null ?
                       dlg.getId() :
                       new StringBuffer(dlg.getDialogId()).append(DIV_ID_PREFIX).toString();
        buf.append("}, dojo.byId(\"")
        .append(dlgId).append("\"));");

        appendHiderIds(buf, dlg);

        buf.append("}")
        .append("setTimeout('"+dlg.getDialogVar()+"_loader();',500);")
        .append("</script>");

        buf.append("<div id=\"").append(dlgId).append("\"");
        if(dlg.getStyle() != null)
            buf.append(" style=\"").append(dlg.getStyle()).append("\"");
        if(dlg.getStyleClass() != null)
            buf.append(" class=\"").append(dlg.getStyleClass()).append("\"");
        buf.append(">");

        writer.write(buf.toString());
    }
    
    public boolean getRendersChildren()
    {
        return true;
    }
    /*
     * (non-Javadoc)
     *
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {

        RendererUtils.renderChildren(facesContext, uiComponent);
        HtmlRendererUtils.writePrettyLineSeparator(facesContext);
    }
    
}

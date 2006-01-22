/*
 * Copyright 2004-2006 The Apache Software Foundation.
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

package org.apache.myfaces.custom.inputAjax;

import org.apache.myfaces.renderkit.html.HtmlTextRendererBase;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.ajax.util.AjaxRendererUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * User: treeder
 * Date: Oct 28, 2005
 * Time: 7:54:08 PM
 */
public class HtmlInputTextAjaxRenderer extends HtmlTextRendererBase implements AjaxRenderer
{
    private static final Log log = LogFactory.getLog(HtmlInputTextAjaxRenderer.class);
    private static final String JAVASCRIPT_ENCODED = "org.apache.myfaces.custom.inputAjax.HtmlInputTextAjax.JAVASCRIPT_ENCODED";


    /**
     * Encodes any stand-alone javascript functions that are needed.
     * Uses either the extension filter, or a
     * user-supplied location for the javascript files.
     *
     * @param context   FacesContext
     * @param component UIComponent
     * @throws java.io.IOException
     */
    private void encodeJavascript(FacesContext context, UIComponent component) throws IOException
    {
        HtmlInputTextAjax comp = (HtmlInputTextAjax) component;

        AddResource addResource = AddResource.getInstance(context);

        AjaxRendererUtils.addPrototypeScript(context, component, addResource);

        ResponseWriter out = context.getResponseWriter();
        AjaxRendererUtils.writeAjaxScript(context, out, comp);

        context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
    }




    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        log.debug("encodeEnd in HtmlInputTextAjaxRenderer");
        RendererUtils.checkParamValidity(context, component, HtmlInputTextAjax.class);

        if (HtmlRendererUtils.isDisplayValueOnly(component) || isDisabled(context, component))
        {
            super.encodeEnd(context, component);
            return;
        }

        String clientId = component.getClientId(context);
        String submitFunctionStart = component.getId() + "_MyFaces_inputAjax_ajaxSubmit1('" + clientId + "');";
        HtmlInputTextAjax comp = (HtmlInputTextAjax) component;
        if(!comp.getShowOkButton().booleanValue()){
            // then submit on change
            // allow for user defined onchange as well
            String onChange = comp.getOnchange();
            if(onChange == null){
                onChange = "";
            }
            onChange = submitFunctionStart + " " + onChange;
            comp.setOnchange(onChange);
        }
        this.encodeJavascript(context, component);
        super.encodeEnd(context, component);

        ResponseWriter writer = context.getResponseWriter();
        if(comp.getShowOkButton() != null && comp.getShowOkButton().booleanValue()){
            // then show an ok button
            writer.startElement(HTML.INPUT_ELEM, comp);
            writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_BUTTON, null);
            String okText = comp.getOkText() != null ? comp.getOkText() : "Ok";
            writer.writeAttribute(HTML.VALUE_ATTR, okText, null);
            writer.writeAttribute(HTML.ONCLICK_ATTR, submitFunctionStart, null);
            writer.endElement(HTML.INPUT_TYPE_BUTTON);
            if(comp.getShowCancelButton() != null && comp.getShowCancelButton().booleanValue()){
                // put a space here
                writer.write("&nbsp;");
                writer.startElement(HTML.ANCHOR_ELEM, comp);
                writer.writeAttribute(HTML.HREF_ATTR, "javascript:" + AjaxRendererUtils.JS_MYFACES_NAMESPACE + "clearById('" + clientId + "');", null);
                String cancelText = comp.getCancelText() != null ? comp.getCancelText() : "cancel";
                writer.writeText(cancelText, null);
                writer.endElement(HTML.ANCHOR_ELEM);
            }
        }

        // output a span for error messages
        /*writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, "msgFor_" + clientId, null);
        writer.writeText(" ", null);
        writer.endElement(HTML.SPAN_ELEM);*/


    }

    public void encodeAjax(FacesContext context, UIComponent component) throws IOException
    {
        log.debug("encodeAjax in HtmlInputTextAjaxRenderer");
        AjaxRendererUtils.encodeAjax(context, component);
    }
}

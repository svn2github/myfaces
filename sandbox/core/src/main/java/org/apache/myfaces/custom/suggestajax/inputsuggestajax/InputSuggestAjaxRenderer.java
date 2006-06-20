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

package org.apache.myfaces.custom.suggestajax.inputsuggestajax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.suggestajax.SuggestAjaxRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Gerald Müllan
 * @author Martin Marinschek
 * @version $Revision: 177984 $ $Date: 2005-05-23 19:39:37 +0200 (Mon, 23 May 2005) $
 */
public class InputSuggestAjaxRenderer extends SuggestAjaxRenderer implements AjaxRenderer
{
    private static final Log log = LogFactory.getLog(InputSuggestAjaxRenderer.class);

   /**
     * Encodes any stand-alone javascript functions that are needed.
     * Uses either the extension filter, or a
     * user-supplied location for the javascript files.
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException
     */
    private void encodeJavascript(FacesContext context, UIComponent component)
                                                                        throws IOException
    {
        String javascriptLocation = (String)component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        String styleLocation = (String)component.getAttributes().get(JSFAttr.STYLE_LOCATION);

        DojoUtils.addMainInclude(context, component, javascriptLocation, new DojoConfig());
        DojoUtils.addRequire(context, component, "extensions.FacesIO");
        DojoUtils.addRequire(context, component, "dojo.widget.ComboBox");
        DojoUtils.addRequire(context, component, "extensions.ComboBox");
        DojoUtils.addRequire(context, component, "dojo.widget.Wizard");
        DojoUtils.addRequire(context, component, "dojo.event.*");

        AddResource addResource = AddResourceFactory.getInstance(context);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        if (inputSuggestAjax.getPopupStyleClass() == null)
        {
            if( styleLocation != null)
            {
                addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, styleLocation + "/input_suggest.css");
            }
            else
            {
                String theme = ((InputSuggestAjax)component).getLayout();
                if(theme == null)
                    theme = "default";
                addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, InputSuggestAjaxRenderer.class, theme + "/input_suggest.css");
            }
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, InputSuggestAjax.class);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        encodeJavascript(context,component);

        inputSuggestAjax.getAttributes().put("autocomplete","off");

    /*     String oldStyleClass = inputSuggestAjax.getStyleClass();
    inputSuggestAjax.setStyleClass(
            (oldStyleClass!=null && oldStyleClass.length()>=0 ? oldStyleClass : "")+" myFacesInputSuggestAjax");

    inputSuggestAjax.setStyleClass(oldStyleClass);*/

        String clientId = component.getClientId(context);
        String actionURL = getActionUrl(context);

        String charset = (inputSuggestAjax.getCharset() != null ? inputSuggestAjax.getCharset() : "");

        String ajaxUrl = context.getExternalContext().encodeActionURL(actionURL+"?affectedAjaxComponent=" + clientId +
                "&charset=" + charset + "&" + clientId + "=%{searchString}");

        ResponseWriter out = context.getResponseWriter();

        String value = RendererUtils.getStringValue(context, component);

        out.startElement(HTML.INPUT_ELEM, component);
        out.writeAttribute(HTML.ID_ATTR, clientId, null);
        out.writeAttribute(HTML.NAME_ATTR, clientId, null);
        out.writeAttribute(HTML.SIZE_ATTR, "100px", null);
        out.writeAttribute("dojoType", "combobox", null);
        out.writeAttribute("dataUrl", ajaxUrl, null);
        out.writeAttribute("mode", "remote", null);
        if (value != null)
        {
            out.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        if (isDisabled(context, component))
        {
            out.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        out.endElement(HTML.INPUT_ELEM);

        out.startElement(HTML.SCRIPT_ELEM, null);
        out.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        out.write("dojo.event.connect(window, \"onload\", function(evt) {\n"
                    + "var comboWidget = dojo.widget.byId(\""+ clientId +"\");\n"
                    + "comboWidget.textInputNode.value = \""+ value +"\";\n"
                    + "comboWidget.comboBoxValue.value = \""+ value +"\";\n");
        out.write("});\n");
        out.endElement(HTML.SCRIPT_ELEM);
    }

    public void encodeAjax(FacesContext context, UIComponent uiComponent)
                                                                    throws IOException
    {
        Collection suggesteds = getSuggestedItems(context, uiComponent);

        StringBuffer buf = new StringBuffer();

        buf.append("[");

        int suggestedCount=0;

        //writing the suggested list
        for (Iterator suggestedItem = suggesteds.iterator() ; suggestedItem.hasNext() ; suggestedCount++)
        {
            if( suggestedCount > DEFAULT_MAX_SUGGESTED_ITEMS)
                break;

            Object item = suggestedItem.next();

            buf.append("[\"").append(item.toString()).append("\",\"")
                .append(item.toString().substring(0, 1).toUpperCase()).append("\"],");
        }

        buf.append("];");

        context.getResponseWriter().write(buf.toString());
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

}

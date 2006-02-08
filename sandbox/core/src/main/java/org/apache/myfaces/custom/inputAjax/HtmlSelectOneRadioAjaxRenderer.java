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

import org.apache.myfaces.renderkit.html.ext.HtmlRadioRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.ajax.util.AjaxRendererUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * User: treeder
 * Date: Nov 10, 2005
 * Time: 4:48:15 PM
 */
public class HtmlSelectOneRadioAjaxRenderer extends HtmlRadioRenderer implements AjaxRenderer
{
    private static final Log log = LogFactory.getLog(HtmlSelectOneRadioAjaxRenderer.class);
    private static final String JAVASCRIPT_ENCODED = "org.apache.myfaces.custom.inputAjax.HtmlSelectOneRadioAjax.JAVASCRIPT_ENCODED";


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

        HtmlSelectOneRadioAjax selectManyCheckbox = (HtmlSelectOneRadioAjax) component;

        AddResource addResource = AddResourceFactory.getInstance(context);

        AjaxRendererUtils.addPrototypeScript(context, component, addResource);

        ResponseWriter out = context.getResponseWriter();

        String extraParams =("&checked=\" + el.checked + \"");
        AjaxRendererUtils.writeAjaxScript(context, out, selectManyCheckbox, extraParams);

        context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
    }


    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, HtmlSelectOneRadioAjax.class);

        if (HtmlRendererUtils.isDisplayValueOnly(component) || isDisabled(context, component))
        {
            super.encodeEnd(context, component);
            return;
        }

        String clientId = component.getClientId(context);

        HtmlSelectOneRadioAjax selectOneRadio = (HtmlSelectOneRadioAjax) component;

        // allow for user defined onclick's as well
        String onClick = selectOneRadio.getOnclick();
        if(onClick == null){
            onClick = "";
        }
        onClick = component.getId() + "_MyFaces_inputAjax_ajaxSubmit2(this, '" + clientId + "'); " + onClick;
        selectOneRadio.setOnclick(onClick);

        this.encodeJavascript(context, component);

        super.encodeEnd(context, component);

    }

    public void encodeAjax(FacesContext context, UIComponent component) throws IOException
    {
        log.debug("encodeAjax in HtmlSelectOneRadioAjaxRenderer");
        // check for request type (portlet support)
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Map extraReturnAttributes = new HashMap();
        extraReturnAttributes.put("checked", request.getParameter("checked"));
        AjaxRendererUtils.encodeAjax(context, component, extraReturnAttributes);

    }



}

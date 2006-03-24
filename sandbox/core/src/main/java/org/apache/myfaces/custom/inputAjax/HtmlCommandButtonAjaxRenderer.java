package org.apache.myfaces.custom.inputAjax;

import org.apache.myfaces.renderkit.html.ext.HtmlButtonRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.custom.ajax.util.AjaxRendererUtils;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * User: Travis Reeder
 * Date: Mar 22, 2006
 * Time: 4:38:13 PM
 */
public class HtmlCommandButtonAjaxRenderer extends HtmlButtonRenderer implements AjaxRenderer
{
    private static final Log log = LogFactory.getLog(HtmlCommandButtonAjaxRenderer.class);
    private static final String JAVASCRIPT_ENCODED = "org.apache.myfaces.custom.inputAjax.HtmlCommandButtonAjax.JAVASCRIPT_ENCODED";


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
        HtmlCommandButtonAjax comp = (HtmlCommandButtonAjax) component;

        AddResource addResource = AddResourceFactory.getInstance(context);

        AjaxRendererUtils.addPrototypeScript(context, component, addResource);

        ResponseWriter out = context.getResponseWriter();
        AjaxRendererUtils.writeAjaxScript(context, out, comp);

        context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
    }




    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        log.debug("encodeEnd in HtmlCommandButtonAjaxRenderer");
        RendererUtils.checkParamValidity(context, component, HtmlCommandButtonAjax.class);

        if (HtmlRendererUtils.isDisplayValueOnly(component) || isDisabled(context, component))
        {
            super.encodeEnd(context, component);
            return;
        }

        String clientId = component.getClientId(context);
        String submitFunctionStart = AjaxRendererUtils.JS_MYFACES_NAMESPACE + "ajaxSubmit3('" + clientId + "');";
        HtmlCommandButtonAjax comp = (HtmlCommandButtonAjax) component;
            // then submit on click
            comp.setOnclick(submitFunctionStart);

        this.encodeJavascript(context, component);
        super.encodeEnd(context, component);

    }

    public void encodeAjax(FacesContext context, UIComponent component) throws IOException
    {
        log.debug("encodeAjax in HtmlCommandButtonAjaxRenderer");
        AjaxRendererUtils.encodeAjax(context, component);
    }

}

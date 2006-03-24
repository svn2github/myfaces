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

package org.apache.myfaces.custom.ajax.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.custom.ajax.AjaxCallbacks;
import org.apache.myfaces.custom.prototype.PrototypeResourceLoader;
import org.apache.myfaces.custom.util.ComponentUtils;
import org.apache.myfaces.custom.inputAjax.HtmlInputTextAjax;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlMessageRendererBase;
import org.apache.myfaces.component.html.ext.HtmlMessages;

/**
 * @author Travis Reeder (latest modification by $Author: mmarinschek $)
 * @version $Revision: 290397 $ $Date: 2005-09-20 10:35:09 +0200 (Di, 20 Sep 2005) $
 */
public final class AjaxRendererUtils
{
    /**
     * util class.
     */
    private AjaxRendererUtils()
    {
        //util clazz
    }

    private static final Log log = LogFactory.getLog(AjaxRendererUtils.class);
    public static final String JAVASCRIPT_ENCODED = "org.apache.myfaces.custom.inputAjax.JAVASCRIPT_ENCODED";
    public static final String JS_MYFACES_NAMESPACE = "_MyFaces_inputAjax_";

    public static void addPrototypeScript(FacesContext context, UIComponent component, AddResource addResource)
    {
        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        if (javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/prototype.js");
        }
        else
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, PrototypeResourceLoader.class, "prototype.js");
        }
    }

    public static void writeAjaxScript(FacesContext context, ResponseWriter out, AjaxCallbacks component)
            throws IOException
    {
        writeAjaxScript(context, out, component, null);
    }

    /**
     * Not really liking having the extraParams thing, seems to inflexible for altering other things
     *
     * @param context
     * @param out
     * @param component
     * @param extraParams
     * @throws IOException
     */
    public static void writeAjaxScript(FacesContext context, ResponseWriter out, AjaxCallbacks component, String extraParams) throws IOException
    {
        UIComponent uiComponent = (UIComponent) component;
        String clientId = uiComponent.getClientId(context);
        String viewId = context.getViewRoot().getViewId();
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        String ajaxURL = viewHandler.getActionURL(context, viewId);

        HtmlInputTextAjax htmlInputTextAjax = null;

        String ajaxMessagesId = null;
        String ajaxMessageId = null;

        if (uiComponent instanceof HtmlInputTextAjax)
        {
            htmlInputTextAjax = (HtmlInputTextAjax) uiComponent;

            //finding the corresponding message component to display an ajaxMessage
            UIComponent ajaxMessage = context.getViewRoot()
                    .findComponent(htmlInputTextAjax.getClientId(context) + "_msgFor");

            if (ajaxMessage != null)
            {
                ajaxMessageId = ajaxMessage.getClientId(context);
            }

            //finding the corresponding messages component to display an ajaxMessage
            UIComponent ajaxMessages = (UIComponent) ComponentUtils
                    .findFirstMessagesComponent(context, context.getViewRoot());

            if (ajaxMessages != null && ((HtmlMessages) ajaxMessages).getForceSpan())
            {
                ajaxMessagesId = ajaxMessages.getClientId(context);
            }
        }

        String jsNameSpace = //uiComponent.getId() +
                JS_MYFACES_NAMESPACE;
        String AJAX_RESPONSE_MAP = JS_MYFACES_NAMESPACE + "ajaxResponseMap";

        // todo: only namespace the things that are specific to the component and only output those a second time, use comment below to limit
        // // check to see if javascript has already been written

        if (!context.getExternalContext().getRequestMap().containsKey(JAVASCRIPT_ENCODED))
        {
            out.startElement(HTML.SCRIPT_ELEM, null);
            out.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);

            // for component specific mappings

            out.writeText("var " + AJAX_RESPONSE_MAP + " = new Object();\n", null);

            // output global functions
            // utility functions todo: these should be part of a standard js include
            // clear a field
            StringBuffer buff = new StringBuffer();
            buff.append("function ").append(JS_MYFACES_NAMESPACE).append("clearById(elname){\n")
                    .append("    var el = document.getElementById(elname);\n")
                    .append("    el.value = \"\";\n")
                    .append("}\n");
            out.writeText(buff.toString(), null);

            buff = new StringBuffer();
            buff.append("\n")
                    .append("function ").append(jsNameSpace).append("notifyElement(originalRequest, successful)\n")
                    .append("{\n")
                    .append("    //alert(\"originalRequest: \" + originalRequest + \" - \" + successful + \"\\ntext: \" + originalRequest.responseText);\n")
                    .append("    var errorArray = originalRequest.responseXML.getElementsByTagName(\"response\")[0].getElementsByTagName(\"error\");\n")
                    .append("    if(errorArray && errorArray.length > 0){\n")
                    .append("      for(ierr = 0; ierr < errorArray.length; i++){\n")
                    .append("        var myObError = errorArray[ierr];\n")
                    .append("        var errorClientId = myObError.getAttribute(\"elname\");\n")
                    .append("        var errorSeverity = myObError.getAttribute(\"severity\");\n")
                    .append("        var errorSummary = myObError.getAttribute(\"summary\");\n")
                    .append("        var errorDetail = null;\n")
                    .append("        var errorDetailElements = myObError.getElementsByTagName(\"detail\");\n")
                    .append("        if(errorDetailElements) errorDetail = errorDetailElements[0].text;\n")
                    .append("        var style = myObError.getAttribute(\"style\");\n")
                    .append("        var styleClass = myObError.getAttribute(\"styleClass\");\n")
                    .append("        ")
                    .append(jsNameSpace).append("displayError(errorClientId, errorSeverity, errorSummary, errorDetail, styleClass, style);\n")
                    .append("      }\n"); // end error loop

            //also after displaying the error message, onSuccessFunction() should be called
            // TR - Why would success be called on error?
           /* if (component.getOnSuccess() != null)
                buff.append(AJAX_RESPONSE_MAP).append("['elname']""onSuccessFunction(elname, elvalue, originalRequest);\n");*/

            buff.append("    }\n")
                    .append("    var myObElementArray = originalRequest.responseXML.getElementsByTagName(\"response\")[0].getElementsByTagName(\"elementUpdated\");\n")
                    .append("    if(myObElementArray && myObElementArray.length > 0){\n")
                    .append("      for(iob = 0; iob < myObElementArray.length; i++){\n")
                    .append("       var myObElement = myObElementArray[iob];\n")
                    .append("       var elname = myObElement.getAttribute(\"elname\");\n")
                    .append("       var elvalue = myObElement.getAttribute(\"elvalue\");\n")
                    .append("       if (successful)\n")
                    .append("       {\n");
            if (component.getOnSuccess() != null)
            {
                buff.append("        ").append(AJAX_RESPONSE_MAP).append("['elname']['onSuccessFunction'](elname, elvalue, originalRequest);\n");
            }
            buff.append(jsNameSpace).append("clearErrorMessageAndFieldStyles(elname, myObElementArray);\n");
            buff.append("        ").append(jsNameSpace).append("clearError(elname);\n");
            buff.append("    }\n");
            if (component.getOnFailure() != null)
            {
                buff.append("        else\n")
                        .append("        {\n")
                        .append("        ").append(AJAX_RESPONSE_MAP).append("['elname']['onFailureFunction'](elname, elvalue, originalRequest);}\n");
            }
            buff.append("     }\n")
                    .append("    }\n")//// end returned successful object loop
                    .append("}\n");

            // displayError function shows any errors sent back
            buff.append("function ").append(jsNameSpace).append("displayError(elname, severity, summary, detail, styleClass, style){\n")
                    .append("        var summaryAndDetail = summary;\n")
                    .append("        if(detail) summaryAndDetail += \": \" + detail;\n");
            if (ajaxMessagesId != null)
            {
                buff.append("        var ajaxMessagesSpan = document.getElementById( \"" + ajaxMessagesId + "\");\n")
                        .append("            if(ajaxMessagesSpan){\n")
                        .append("                 ajaxMessagesSpan.innerHTML += summaryAndDetail + '<br/>';\n")
                        .append("            }");
            }
            buff.append("        var msgSpan = document.getElementById(elname+\"_msgFor\");\n")
                    .append("            if(msgSpan) msgSpan.innerHTML = summaryAndDetail;\n")
                    .append("            if(styleClass) msgSpan.className = styleClass;\n");
            //.append("        if(style) msgSpan.style = style;") // guess you can't swap out the entire style like this

            if (htmlInputTextAjax != null)
            {
                //styling of the ajax fields
                buff.append("var errorStyleElem = document.getElementById(elname);\n")
                        .append("if(errorStyleElem){\n");
                if (htmlInputTextAjax.getErrorStyleClass() != null)
                    buff.append("errorStyleElem.className =\"").append(htmlInputTextAjax.getErrorStyleClass()).append("\";\n");
                if (htmlInputTextAjax.getErrorStyle() != null)
                    buff.append("errorStyleElem.style.cssText =\"").append(htmlInputTextAjax.getErrorStyle()).append("\";\n");
                buff.append("}\n");
            }

            buff.append("}\n");
            // clearError function to remove error display if there was one previously
            buff.append("function ").append(jsNameSpace).append("clearError(elname){\n")
                    .append("    var msgSpan = document.getElementById(elname+\"_msgFor\");\n")
                    .append("    if(msgSpan){\n")
                    .append("        msgSpan.innerHTML = \"\";\n")
                    .append("    }\n")
                    .append("}\n");
            //clearError function; same as above, but also for the htmlInputTextAjax field if using the messages comp
            buff.append("function ").append(jsNameSpace).append("clearErrorMessageAndFieldStyles(elname, possibleUpdateTag){\n")
                    .append("try\n")
                    .append("    {\n")
                    .append("        if(possibleUpdateTag[0].tagName == \"elementUpdated\")\n")
                    .append("        {\n");
            if (ajaxMessagesId != null)
            {
                buff.append("            var errorMessageSpan = document.getElementById(\"" + ajaxMessagesId + "\");\n")
                        .append("            errorMessageSpan.innerHTML = \"\";\n");
            }
            //set ajaxinputField to default style or styleclass
            buff.append("            var errorField = document.getElementById(elname);\n");
            if (htmlInputTextAjax != null)
            {
                if (htmlInputTextAjax.getStyleClass() != null)
                    buff.append("errorField.className = \"" + htmlInputTextAjax.getStyleClass() + "\";\n");
                else if (htmlInputTextAjax.getStyle() != null)
                {
                    buff.append("errorField.style.cssText = \"" + htmlInputTextAjax.getStyle() + "\";\n");
                }
                else
                {
                    buff.append("errorField.style.cssText = \"\";\n")
                            .append("errorField.className = \"\";\n");
                }


            }
            buff.append("        }\n")
                    .append("    }catch(e)\n")
                    .append("    {\n")
                    .append("        this.dispatchException(e);\n")
                    .append("    }\n")
                    .append("    }\n");

            buff.append("function ").append(jsNameSpace)
                    .append("notifyElementFailure(originalRequest){\n")
                    .append("    ").append(jsNameSpace)
                    .append("notifyElement(originalRequest, false);\n" + "}\n");

            buff.append("function ").append(jsNameSpace)
                    .append("notifyElementSuccess(originalRequest){\n")
                    .append("    ").append(jsNameSpace)
                    .append("notifyElement(originalRequest, true);\n");
            buff.append("}\n")
                    .append("function ").append(jsNameSpace)
                    .append("complete(originalRequest){\n")
                            // todo: allow for an onComplete attribute
                    .append("}\n");
            // a function that takes an element id
            buff.append("function ").append(jsNameSpace).append("ajaxSubmit1(elname){\n")
                    .append("    var el = document.getElementById(elname);\n")
                    .append("    var elvalue = el.value;\n")
                    .append("    ")
                    .append(jsNameSpace)
                    .append("ajaxSubmit(elname, elvalue, el);\n")
                    .append("}\n");
            // a function that can take the actual element for things like HtmlSelectMany
            buff.append("function ").append(jsNameSpace).append("ajaxSubmit2(el, elname){\n")
                    .append("    var checked = el.checked;\n")
                    .append("    var elvalue = el.value;\n")
                    .append("    var extraParams = '&checked=' + checked;\n")
                    .append("    ").append(jsNameSpace).append("ajaxSubmit(elname, elvalue, el, extraParams);\n")
                    .append("}\n");

/*todo: For forms, the affectedAjaxComponent can be the commandButton, then in AjaxDecodePhaseListener, it can check if it's
        a command button, and if so, find the form (naming container), then update the form and then finally
        call the action method on the button*/
            // function for submitting an entire form
            buff.append("function ").append(jsNameSpace).append("ajaxSubmit3(elname){\n")
                    .append("    var el = document.getElementById(elname);\n")
                    .append("    var formEl = el.form;\n")
                    .append("    var elvalue = 'submit';")
                    .append("    ").append(jsNameSpace).append("ajaxSubmit(elname, elvalue, formEl);\n")
                    .append("}\n");

            // and now the actual function that will send the request
            buff.append("function ").append(jsNameSpace).append("ajaxSubmit(elname, elvalue, el, extraParams){\n");
            if (component.getOnStart() != null)
                buff.append("    ").append(AJAX_RESPONSE_MAP).append("[elname]").append("['onStartFunction'](elname, elvalue);\n");
            buff.append("    var pars = \"affectedAjaxComponent=\" + elname + \"&elname=\" + elname + \"&elvalue=\" + elvalue + \"");
            buff.append("&\" + elname + \"=\" + elvalue + \"");
            if (context.getApplication().getStateManager().isSavingStateInClient(context))
            {
                buff.append("&jsf_tree_64=\"+encodeURIComponent(document.getElementById(\"jsf_tree_64\").value)+\"&jsf_state_64=\"+encodeURIComponent(document.getElementById(\"jsf_state_64\").value)+\"&jsf_viewid=\"+encodeURIComponent(document.getElementById(\"jsf_viewid\").value)+\"");
            }
            buff.append("\";\n"); // end pars
            buff.append("    if(extraParams) pars += extraParams;\n");
            buff.append("    pars += '&' + Form.serialize(el);\n");
            //buff.append(" alert(pars);"); // can be used for debugging
            buff.append("    var " + "_ajaxRequest = new Ajax.Request(\n")
                    .append("    ").append(AJAX_RESPONSE_MAP).append("[elname]['ajaxUrl'],\n")
                    .append("    {method: 'post'" + ", parameters: pars");
            buff.append(", onComplete: ").append(jsNameSpace).append("complete");
            buff.append(", onSuccess: ").append(jsNameSpace).append("notifyElementSuccess");
            buff.append(", onFailure: ").append(jsNameSpace).append("notifyElementFailure");
            buff.append(
                    "} \n" +
                            "            );\n" +
                            "}\n"
            );

            out.writeText(buff.toString(), null);


            out.endElement(HTML.SCRIPT_ELEM);

            context.getExternalContext().getRequestMap().put(JAVASCRIPT_ENCODED, Boolean.TRUE);
        }

        // component specific mappings, one per component
        out.startElement(HTML.SCRIPT_ELEM, null);
        out.writeAttribute(HTML.TYPE_ATTR, "text/javascript", null);
        out.writeText(AJAX_RESPONSE_MAP + "['" + clientId + "'] = new Object();\n", null);
        out.writeText(AJAX_RESPONSE_MAP + "['" + clientId + "']['ajaxUrl'] = '" + ajaxURL + "';\n", null);
        if (component.getOnSuccess() != null)
            out.writeText(AJAX_RESPONSE_MAP + "['" + clientId + "']['onSuccessFunction'] = " + component.getOnSuccess() + ";\n", null);
        if (component.getOnFailure() != null)
            out.writeText(AJAX_RESPONSE_MAP + "['" + clientId + "']['onFailureFunction'] = " + component.getOnFailure() + ";\n", null);
        if (component.getOnStart() != null)
            out.writeText(AJAX_RESPONSE_MAP + "['" + clientId + "']['onStartFunction'] = " + component.getOnStart() + ";\n", null);
        out.endElement(HTML.SCRIPT_ELEM);
        /*
                new Ajax.Request(form.action, {
        method: form.method,
        parameters: Form.serialize(form),
        onSuccess: updateFunction
        });
        */



    }


    public static void encodeAjax(FacesContext context, UIComponent component)
            throws IOException
    {

        encodeAjax(context, component, null);
    }

    /**
     * Outputs elementUpdate elements with the client id and value.
     * Also outputs error elements.
     *
     * @param context
     * @param component
     * @param extraReturnAttributes
     * @throws IOException
     */
    public static void encodeAjax(FacesContext context, UIComponent component, Map extraReturnAttributes) throws IOException
    {
        String clientId = component.getClientId(context);
        Object responseOb = context.getExternalContext().getResponse();
        if (responseOb instanceof HttpServletResponse)
        {
            HttpServletResponse response = (HttpServletResponse) responseOb;
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            StringBuffer buff = new StringBuffer();

            // send error messages
            boolean hasErrorMessages = false;
            Iterator iter = context.getMessages(clientId);
            while (iter.hasNext())
            {
                FacesMessage msg = (FacesMessage) iter.next();
                String style = "";
                String styleClass = "";

                String msgForId = clientId + "_msgFor";
                //System.out.println("Looking for component: " + msgForId);
                UIComponent msgComponent = context.getViewRoot().findComponent(msgForId);
                String msgId = null;

                if (msgComponent != null)
                {
                    //System.out.println("Component found");
                    // then send to update single component
                    // get styleclass
                    String[] styleAndClass = HtmlMessageRendererBase.getStyleAndStyleClass(msgComponent, msg.getSeverity());
                    style = styleAndClass[0];
                    styleClass = styleAndClass[1];
                    msgId = msgComponent.getClientId(context);
                    //System.out.println("style: " + style);
                }
                else
                {
                    // send to update global messages, maybe this could happen on the client side though ?
                }
                buff.append("<error elname=\"").append(clientId)
                        .append("\" severity=\"").append(msg.getSeverity().toString());
                if (styleClass != null) buff.append("\" styleClass=\"").append(styleClass);
                if (style != null) buff.append("\" style=\"").append(style);
                buff.append("\" summary=\"").append(msg.getSummary())
                        .append("\" ");
                        if(msgId != null) buff.append(" msgId=\"").append(msgId).append("\"");
                        buff.append(">\n");
                String detail = msg.getDetail();
                if (detail != null)
                {
                    buff.append("<detail>");
                    buff.append(msg.getDetail());
                    buff.append("</detail>\n");
                }
                buff.append("</error>\n");
                hasErrorMessages = true;
            }
            if (!hasErrorMessages)
            {
                // send elementUpdated messages
                buff.append("<elementUpdated elname=\"").append(clientId).append("\"");
                if(component instanceof UIInput){
                    UIInput uiInput = (UIInput) component;
                    // todo: might have to make sure this value can be serialized like this
                    // todo: and should probably be in text block, rather than an attribute
                        buff.append(" elvalue=\"").append(uiInput.getValue()).append("\"");
                }
                if (extraReturnAttributes != null)
                {
                    Iterator iter2 = extraReturnAttributes.keySet().iterator();
                    while (iter2.hasNext())
                    {
                        String key = (String) iter2.next();
                        buff.append(" ").append(key).append("=\"").append(extraReturnAttributes.get(key).toString()).append("\"");
                    }
                }
                buff.append(" />");
                buff.append("\n");
            }

            String output = buff.toString();
            log.debug(output);
            PrintWriter out = response.getWriter();
            out.print(output);

        }
    }
}

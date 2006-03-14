/**
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.statechangednotifier;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.renderkit.html.HtmlHiddenRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

/**
 * @author Bruno Aranda (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class StateChangedNotifierRenderer extends HtmlHiddenRenderer
{

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, StateChangedNotifier.class);

        StateChangedNotifier notifier = (StateChangedNotifier) uiComponent;

        if (notifier.getDisabled() != null)
        {
            if (notifier.getDisabled().booleanValue())
                return;
        }
        String javascriptLocation = (String) notifier.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        DojoUtils.addMainInclude(facesContext, uiComponent, javascriptLocation, new DojoConfig());
        DojoUtils.addRequire(facesContext, uiComponent, "dojo.widget.Dialog");
        DojoUtils.addRequire(facesContext, uiComponent, "dojo.event.*");
        
        //DojoUtils.addRequire(facesContext, "dojo.xml.Parse");

        writeDialog(facesContext, uiComponent);

        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        addResource.addJavaScriptHere(facesContext, StateChangedNotifierRenderer.class, "stateChangedNotifier.js");

        String styleLocation = (String) uiComponent.getAttributes().get(JSFAttr.STYLE_LOCATION);

        //we need a style def for the dialog system
        if (StringUtils.isNotBlank(styleLocation))
        {
            addResource.addStyleSheet(facesContext, AddResource.HEADER_BEGIN, styleLocation + "/default.css");
        }
        else
        {
            addResource.addStyleSheet(facesContext, AddResource.HEADER_BEGIN, StateChangedNotifierRenderer.class,
                    "css/default.css");
        }

        encodeJavascript(facesContext, notifier);

        super.encodeEnd(facesContext, uiComponent);
    }

    /**
     * dialog definition
     * this one is needed for the dojoized dialog
     * 
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    private void writeDialog(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        /*
         * <div id="form1__idJsp1Notifier_Dialog" style="position:absolute; visibility: hidden;">
         <div id="form1__idJsp1Notifier_Dialog_Content">
         values have changed do you really want to
         <br>
         submit the values
         </div>
         <input type="button" id="form1__idJsp1Notifier_Dialog_Yes" value="yes" />
         <input type="button" id="form1__idJsp1Notifier_Dialog_No" value="no" />
         * </div>
         */

        String notifierClientId = uiComponent.getClientId(facesContext);
        String replacedClientId = notifierClientId.replaceAll(":", "_");
        String dialogVar = replacedClientId + "Notifier_Dialog";

        String yesText = (String) uiComponent.getAttributes().get("yesText");
        String noText = (String) uiComponent.getAttributes().get("noText");

        yesText = (yesText == null) ? "Yes" : yesText;
        noText = (noText == null) ? "No" : noText;

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, dialogVar, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "position:absolute; visibility: hidden;", null);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, dialogVar + "_Content", null);
        writer.endElement(HTML.DIV_ELEM);

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
        writer.writeAttribute(HTML.ID_ATTR, dialogVar + "_Yes", null);
        writer.writeAttribute(HTML.VALUE_ATTR, yesText, null);
        writer.endElement(HTML.INPUT_ELEM);
        
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
        writer.writeAttribute(HTML.ID_ATTR, dialogVar + "_No", null);
        writer.writeAttribute(HTML.VALUE_ATTR, noText, null);
        writer.endElement(HTML.INPUT_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeJavascript(FacesContext facesContext, StateChangedNotifier notifier)
    {
        String notifierClientId = notifier.getClientId(facesContext);

        String replacedClientId = notifierClientId.replaceAll(":", "_");
        String initFunctionName = "init_" + replacedClientId;

        UIForm form = getParentForm(notifier);
        String formId = form.getClientId(facesContext);

        String notifierVar = replacedClientId + "Notifier";

        StringBuffer sb = new StringBuffer();
        sb.append("dojo.addOnLoad(window, '" + initFunctionName + "');\n");
        sb.append("function " + initFunctionName + "() {\n");
        sb.append(notifierVar + " = new org_apache_myfaces_StateChangedNotifier('" + notifierVar + "','" + formId
                + "','" + notifierClientId + "','" + notifier.getConfirmationMessage() + "',");

        String excludedCommandIds = notifier.getExcludedIds();
        if (excludedCommandIds != null)
        {
            sb.append("'" + excludedCommandIds + "');\n");
        }
        else
        {
            sb.append("'');\n");
        }

        sb.append(replacedClientId + "Notifier.prepareNotifier();\n");

        sb.append("}\n");

        AddResource addResource = AddResourceFactory.getInstance(facesContext);
        addResource.addInlineScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, sb.toString());
    }

    /**
     * Get the parent UIForm. If no parent is a UIForm then returns null.
     *
     * @param component
     * @return UIForm
     */
    private UIForm getParentForm(UIComponent component)
    {
        // See if we are in a form
        UIComponent parent = component.getParent();
        while (parent != null && !(parent instanceof UIForm))
        {
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }
}

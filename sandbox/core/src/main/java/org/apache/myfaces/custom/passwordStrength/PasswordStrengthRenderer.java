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
package org.apache.myfaces.custom.passwordStrength;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.util.MessageUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;

/**
 * @JSFRenderer renderKitId = "HTML_BASIC"
 * family = "org.apache.myfaces.PasswordStrength"
 * type = "org.apache.myfaces.PasswordStrength"
 */
public class PasswordStrengthRenderer extends Renderer {

    /**
     * Returns a valid javascript var name depending on the given
     * client id of the component
     *
     * @param context
     * @param component
     * @return
     */
    private String getJavascriptVar(FacesContext context, UIComponent component) {
        return JAVASCRIPT_VAR_PREFIX + component.getClientId(context).replaceAll("\\:", "_");
    }

    ;

    private void addResources(FacesContext context, UIComponent component,
                              ResponseWriter writer) throws IOException {
        PasswordStrengthComponent passwordStrength = (PasswordStrengthComponent) component;
        AddResource addResource = AddResourceFactory.getInstance(context);

        // Load the css style ...
        String styleLocation = (String) component.getAttributes().get(
                JSFAttr.STYLE_LOCATION);
        if (StringUtils.isNotBlank(styleLocation)) {
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN,
                    styleLocation + "/passwordStrength.css");
        } else {
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN,
                    PasswordStrengthRenderer.class, "css/passwordStrength.css");
        }

        // Load the JS file ...
        String javascriptLocation = (String) component.getAttributes().get(
                JSFAttr.JAVASCRIPT_LOCATION);
        if (javascriptLocation != null) {
            addResource.addJavaScriptAtPosition(context,
                    AddResource.HEADER_BEGIN, javascriptLocation
                    + "/genericProgressbarProvider.js");
            addResource.addJavaScriptAtPosition(context,
                    AddResource.HEADER_BEGIN, javascriptLocation
                    + "/passwordStrength.js");
        } else {
            addResource.addJavaScriptAtPosition(context,
                    AddResource.HEADER_BEGIN, PasswordStrengthRenderer.class,
                    "genericProgressbarProvider.js");
            addResource.addJavaScriptAtPosition(context,
                    AddResource.HEADER_BEGIN, PasswordStrengthRenderer.class,
                    "passwordStrength.js");
        }


        //Add Initialization stuff ...
        String messageId = getMessageID(context, passwordStrength);
        writer.write("<script type=\"text/javascript\">");
        writer.write("var " + getJavascriptVar(context, component) + " = new " + JAVASCRIPT_CLASS + "();");
        //fixme onload handler
        String addOnStartUP = "window.addEventListener('load',function() {"
                + getJavascriptVar(context, component)
                + ".startUpPasswordStrength('"
                + messageId
                + "'); }, false);";
        writer.write(addOnStartUP);
        writer.write("</script>");
    }

    private String getMessageID(FacesContext context,
                                PasswordStrengthComponent passwordStrength) {
        String clientID = passwordStrength.getClientId(context);
        String messageId = "";
        if (TextIndicatorType.TEXT
                .equalsIgnoreCase(getStrengthIndicatorTypeValue(passwordStrength))) {
            messageId = getIndicatorMessageId(clientID);
        } else {
            messageId = getProgressBarContainerID(clientID);
        }
        return messageId;
    }



    private void renderStartDiv(UIComponent component, ResponseWriter writer)
            throws IOException {
        writer.startElement(HTML.DIV_ELEM, component);
        writer.startElement(HTML.TABLE_ELEM, component);
        writer.startElement(HTML.TR_ELEM, component);
        writer.startElement(HTML.TD_ELEM, component);
    }

    private void renderEndDiv(UIComponent component, ResponseWriter writer)
            throws IOException {
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private String getDefaultTextDesc() {
        return MessageUtils.getMessage(BUNDLE_BASE_NAME,
                MessageUtils.getCurrentLocale(),
                "org.apache.myfaces.custom.passwordStrength.DESC", null)
                .getDetail();
    }

    private String getDefaultShowDetails() {
        return "true";
    }

    private String getDefaultUseCustomSecurity() {
        return "false";
    }

    private String getDefaultCustomSecurityRule() {
        return "A1";
    }

    private String getDefaultPenaltyRatio() {
        return "50";
    }

    private String getDefaultPrefix() {
        return MessageUtils.getMessage(BUNDLE_BASE_NAME,
                MessageUtils.getCurrentLocale(),
                "org.apache.myfaces.custom.passwordStrength.PREFIX", null)
                .getDetail();
    }

    private String getLeftCharactersString() {
        return "'" + MessageUtils.getMessage(BUNDLE_BASE_NAME,
                MessageUtils.getCurrentLocale(),
                "org.apache.myfaces.custom.passwordStrength.LEFT_CHARS", null)
                .getDetail() + "'";
    }

    private String getDefaultStrengthIndicatorType() {
        return TextIndicatorType.TEXT;
    }

    private void createTextSpan(PasswordStrengthComponent passwordStrength,
                                FacesContext context, String clientID) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String preferredLength = passwordStrength.getPreferredPasswordLength();
        String prefixText = (passwordStrength.getPrefixText() == null) ? "'" + getDefaultPrefix() + "'"
                : "'" + passwordStrength.getPrefixText() + "'";
        String textStrengthDescriptions = (passwordStrength
                .getTextStrengthDescriptions() == null) ? "'"
                + getDefaultTextDesc() + "'" : "'"
                + passwordStrength.getTextStrengthDescriptions() + "'";
        String textID = "'" + clientID + "'";
        String showDetails = (passwordStrength.getShowDetails() == null) ? "'"
                + getDefaultShowDetails() + "'" : "'"
                + passwordStrength.getShowDetails().toLowerCase() + "'";
        String useCustomSecurity = (passwordStrength.getUseCustomSecurity() == null) ? "'"
                + getDefaultUseCustomSecurity() + "'"
                : "'" + passwordStrength.getUseCustomSecurity().toLowerCase()
                + "'";
        String customSecurityExpression = (passwordStrength
                .getCustomSecurityExpression() == null) ? "'"
                + getDefaultCustomSecurityRule() + "'" : "'"
                + passwordStrength.getCustomSecurityExpression() + "'";
        String penaltyRatio = (passwordStrength
                .getPenaltyRatio() == null) ? "'"
                + getDefaultPenaltyRatio() + "'" : "'"
                + passwordStrength.getPenaltyRatio() + "'";
        writer.startElement(HTML.SPAN_ELEM, passwordStrength);
        writer.startElement(HTML.INPUT_ELEM, passwordStrength);
        writer.writeAttribute(HTML.TYPE_ATTR, "password", HTML.TYPE_ATTR);
        writer.writeAttribute(HTML.ID_ATTR, clientID, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientID, HTML.NAME_ATTR);
        String value = "";
        Object objValue = ((UIInput) passwordStrength).getSubmittedValue();
        if (objValue != null) {
            value = passwordStrength.getValue().toString();
        }
        writer.writeAttribute("value", value, "value");
  
        String onKeyUpString = createOnKeyUpString(context,
                passwordStrength, textID, preferredLength, prefixText,
                textStrengthDescriptions, true, showDetails, useCustomSecurity,
                customSecurityExpression, penaltyRatio);
        String onKeyUpAttr = passwordStrength.getOnkeyup();
        passwordStrength.setOnkeyup(constructJSFunction(onKeyUpString, onKeyUpAttr));

        String onBlurString = getOnBlurString(context, passwordStrength);
        String onBlurAttr = passwordStrength.getOnblur();
        passwordStrength.setOnblur(constructJSFunction(onBlurString, onBlurAttr));
        
        HtmlRendererUtils.renderHTMLAttributes(writer, passwordStrength, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(context, passwordStrength))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }
        
        writer.endElement(HTML.INPUT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }
    
    /**
     * Concatenates two javascript functions.  One of which is usually supplied as an attribute
     * of this component's tag.  The other one is the script created by this component.
     */
    private String constructJSFunction(String function, String attrFunction)
    {
        if(attrFunction != null && attrFunction.length() > 0) 
        {
            if(!attrFunction.endsWith(";"))
            {
                attrFunction += ";";
            }
            return (function+attrFunction);
        }
        else
        {
            return function;
        }
    }

    /**
     * creates a custom progress bar
     */
    private void createProgressbar(UIComponent component,
                                   FacesContext context, ResponseWriter writer) throws IOException {
        String clientID = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, getProgressBarContainerID(clientID),
                HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, "org_apache_myfaces_passwordStrength_progress",
                HTML.CLASS_ATTR);

        /*inner progress bar*/
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, getProgressBarID(clientID),
                HTML.ID_ATTR);
        /*subcomponent per css definition we can use a simple class*/
        writer.writeAttribute(HTML.CLASS_ATTR, "progressStart",
                HTML.CLASS_ATTR);
        /*inner progress bar end*/
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    private void createTextIndicatorMessage(UIComponent component,
                                            FacesContext context, ResponseWriter writer) throws IOException {
        String clientID = component.getClientId(context);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, getIndicatorMessageId(clientID),
                HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, "org_apache_myfaces_passwordStrength_progress_indicatorMessage",
                HTML.CLASS_ATTR);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void createIndicatorSpan(UIComponent component,
                                     FacesContext context, ResponseWriter writer) throws IOException {
        PasswordStrengthComponent passwordStrength = (PasswordStrengthComponent) component;
        String clientID = passwordStrength.getClientId(context);
        String strengthIndicatorType = getStrengthIndicatorTypeValue(passwordStrength);
        writer.endElement(HTML.TD_ELEM);
        writer.startElement(HTML.TD_ELEM, component);
        if (TextIndicatorType.TEXT.equalsIgnoreCase(strengthIndicatorType)) { //It is a text ...
            createTextIndicatorMessage(component, context, writer);
        } else { //It is a progressbar ...     
            //createProgressBarSpan(component, context, writer);
            createProgressbar(component, context, writer);
        }
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEM);
        writer.startElement(HTML.TR_ELEM, component);
        writer.startElement(HTML.TD_ELEM, component);
        writer.startElement("div", component);
        writer.writeAttribute("id", getleftCharsMessageId(clientID), "id");
        writer.endElement("div");
    }

    private String getStrengthIndicatorType(
            PasswordStrengthComponent passwordStrength) {
        return (passwordStrength.getStrengthIndicatorType() == null) ? "'"
                + getDefaultStrengthIndicatorType() + "'" : "'"
                + passwordStrength.getStrengthIndicatorType() + "'";
    }

    private String getStrengthIndicatorTypeValue(
            PasswordStrengthComponent passwordStrength) {
        return (passwordStrength.getStrengthIndicatorType() == null) ? getDefaultStrengthIndicatorType()
                : passwordStrength.getStrengthIndicatorType();
    }


    private void createHTMLComponents(FacesContext facesContext,
                                      UIComponent component, ResponseWriter writer, String clientID)
            throws IOException {
        PasswordStrengthComponent passwordStrength = (PasswordStrengthComponent) component;
        renderStartDiv(component, writer);
        createTextSpan(passwordStrength, facesContext, clientID);
        createIndicatorSpan(component, facesContext, writer);
        renderEndDiv(component, writer);
    }

    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        RendererUtils.checkParamValidity(context, component,
                PasswordStrengthComponent.class);
        if (HtmlRendererUtils.isDisplayValueOnly(component)
                || isDisabled(context, component)) {
            super.encodeEnd(context, component);
            return;
        }
        String clientID = component.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        addResources(context, component, writer);
        createHTMLComponents(context, component, writer, clientID);
    }

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
    }

    public void decode(FacesContext context, UIComponent component) {
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String clientID = component.getClientId(context);
        String newValue = (String) requestMap.get(clientID);
        ((UIInput) component).setSubmittedValue(newValue);
    }

    protected boolean isDisabled(FacesContext facesContext,
                                 UIComponent component) {
        if (component instanceof HtmlInputText) {
            return ((HtmlInputText) component).isDisabled();
        } else {
            return org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils
                    .getBooleanAttribute(component, HTML.DISABLED_ATTR, false);
        }
    }

    private String createOnKeyUpString(FacesContext context,
                                       UIComponent component, String textID, String preferredLength,
                                       String prefix, String textStrengthDescriptions,
                                       boolean showMessageIndicator, String showDetails,
                                       String useCustomSecurity, String customSecurityExpression,
                                       String penaltyRatio) {
        PasswordStrengthComponent passwordStrength = (PasswordStrengthComponent) component;
        String clientID = component.getClientId(context);
        String showMessageIndicatorString = "";
        String strengthIndicatorType = getStrengthIndicatorType(passwordStrength);
        String progressBarId = "'" + getProgressBarID(clientID) + "'";
        String indicatorMessageID = "'" + getIndicatorMessageId(clientID) + "'";
        String leftCharsMessageID = "'" + getleftCharsMessageId(clientID) + "'";
        if (showMessageIndicator == true) {
            showMessageIndicatorString = getJavascriptVar(context, component) + ".show('"
                    + getMessageID(context, passwordStrength) + "');";
        }
        return updateStatusValue(context, component, textID, preferredLength, prefix,
                textStrengthDescriptions, indicatorMessageID,
                leftCharsMessageID, showMessageIndicatorString,
                strengthIndicatorType, progressBarId, showDetails,
                getLeftCharactersString(), useCustomSecurity,
                customSecurityExpression, penaltyRatio);
    }

    private String updateStatusValue(FacesContext context,
                                     UIComponent component,
                                     String textID, String preferredLength,
                                     String prefix, String textStrengthDescriptions,
                                     String indicatorMessageID, String leftCharsMessageID,
                                     String showMessageIndicatorString,
                                     String strengthIndicatorType, String progressBarId,
                                     String showDetails, String leftCharactersString,
                                     String useCustomSecurity, String customSecurityExpression,
                                     String penaltyRatio) {
        return getJavascriptVar(context, component) + ".updateStatusValue("
                + textID + "," + preferredLength + ", "
                + prefix + ", " + textStrengthDescriptions + ", "
                + indicatorMessageID + ", " + leftCharsMessageID + ", "
                + strengthIndicatorType + ", " + progressBarId + ", "
                + showDetails + ", " + leftCharactersString + ", "
                + useCustomSecurity + ", " + customSecurityExpression + ", "
                + penaltyRatio
                + ");"
                + showMessageIndicatorString;
    }

    private String getIndicatorMessageId(String clientID) {
        return clientID + "indicatorMessage";
    }

    private String getleftCharsMessageId(String clientID) {
        return clientID + "leftCharsMessage";
    }

    private String getProgressBarID(String clientID) {
        return clientID + PROGRESSBAR_SUFFIX;
    }

    private String getProgressBarContainerID(String clientID) {
        return getProgressBarID(clientID) + PROGRESSBAR_CONTAINER_SUFFIX;
    }

    private String getOnBlurString(FacesContext context, UIComponent component) {
        PasswordStrengthComponent passwordStrength = (PasswordStrengthComponent) component;
        String clientID = passwordStrength.getClientId(context);
        return getJavascriptVar(context, component) + ".hide('" + getMessageID(context, passwordStrength) + "');" + getJavascriptVar(context, component) + ".hide('"
                + getleftCharsMessageId(clientID) + "');";
    }

    final String BUNDLE_BASE_NAME = "org.apache.myfaces.custom.passwordStrength.resource.PasswordStrength";
    final String DEFAULT_PROGRESSBAR_WIDTH = "150";
    final String DEFAULT_PROGRESSBAR_HEIGHT = "20";
    final String PROGRESSBAR_SUFFIX = "_PROGRESSBAR";
    final String PROGRESSBAR_CONTAINER_SUFFIX = "_CONTAINER";
    final String DEFAULT_PROGRESSBAR_VALUE = "20";
    /**
     * Package and class declaration
     * for the underlying namespaced
     * javasript class
     */
    static final String JAVASCRIPT_CLASS = "org.apache.myfaces.passwordStrength";
    /**
     * pseudo namespacing of the javascript var to avoid potential conflicts
     */
    static final String JAVASCRIPT_VAR_PREFIX = "myfaces_";
}

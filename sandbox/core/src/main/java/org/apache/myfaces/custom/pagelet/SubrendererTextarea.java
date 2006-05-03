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
package org.apache.myfaces.custom.pagelet;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

public class SubrendererTextarea extends BasePageletRenderer {
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    	encodeEndTextarea(facesContext, uiComponent);
    }
    
    /**
     * encode end for the basic text area functionality
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    protected void encodeEndTextarea(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, Pagelet.class);

        if (!uiComponent.isRendered())
            return;

        Pagelet spchk = (Pagelet) uiComponent;
        encodeJavascript(facesContext, uiComponent);

        ResponseWriter writer = facesContext.getResponseWriter();

        String uniqueId = calcUniqueId(uiComponent, facesContext);

        writeTextArea(facesContext, spchk, writer, uniqueId, false);

        //if (spchk.getControlMode().equals(spchk.COMTROL_MODE_PAGELET_RAWTEXT)) {
        //    writeRawTextDialog(facesContext, spchk, writer);
        // }
        // end control panel
    }

    
        
    /**
     * text area encoding
     *
     * @param context
     * @param component
     */
    public void encodeJavascript(FacesContext context, UIComponent component) throws IOException {
        // AddResource takes care to add only one reference to the same script

        Pagelet spchk = (Pagelet) component;

        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);

        String styleLocation = (String) component.getAttributes().get(JSFAttr.STYLE_LOCATION);

        AddResource addResource = AddResourceFactory.getInstance(context);

        /* javascriptLocation = "/scripts/"; */
        if (javascriptLocation != null) {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "cpaint2.inc.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "spell_checker.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "utils.js");

        } else {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, Pagelet.class, "cpaint2.inc.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, Pagelet.class, "spell_checker.js");
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, Pagelet.class, "utils.js");
        }

        /*if (spchk.getControlMode().equals(Pagelet.COMTROL_MODE_PAGELET_RAWTEXT)) {
            encodeJavascriptPagelet(context, component); //in raw text we need the pagelet dojo includes as well

            //since we recycle some of the mechanisms
        }
        */
        if (styleLocation == null)
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, Pagelet.class, "spell_checker.css");
        else
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, styleLocation + "spell_checker.css");

        //TODO<< Add the dialog resource definition css include

    }
    
    
    
    protected void writeTextArea(FacesContext facesContext, Pagelet spchk, ResponseWriter writer, String uniqueId,
            boolean asDialog) throws IOException {
        String divId      = uniqueId + DIV_ID_SUFFIX;
        String varName    = uniqueId + TEXTBOX_ID_SUFFIX;
        String textAreaId = uniqueId + TEXT_AREA_SUFFIX;
        String editDivId  = uniqueId + EDIT_DIV_SUFFIX;
        String formName   = uniqueId + FORM_SUFFIX;

        String methodBinding = spchk.getSpellchecker().getExpressionString();


        if (spchk.isDisplayValueOnly()) {
            writer.startElement("div", null);
            HtmlRendererUtils.writeIdIfNecessary(writer, spchk, facesContext);

            String style = "width:" + spchk.getWidth() + "px;" + "heigth:" + spchk.getHeight() + "px;" + spchk.getDisplayValueOnlyStyle();

            writer.writeAttribute("style", style, null);

            if (spchk.getDisplayValueOnlyStyleClass() != null)
                writer.writeAttribute("class", spchk.getDisplayValueOnlyStyleClass(), null);
                //TODO add the dialog handling here

            String strValue = RendererUtils.getStringValue(facesContext, spchk);
            writer.writeText(strValue, JSFAttr.VALUE_ATTR);
            writer.endElement("div");

            return;
        }

        // control panel

        // positioning table
        startWriterPositioningTable(writer);
        startContentColum(writer);

        {
            startControlPanel(writer, divId);

            // textarea
            writeTextarea(writer, textAreaId, spchk, formName);

            // edit div
            writeEditDiv(writer, spchk, editDivId);
            endControlPanel(writer);
        }

        endContentColum(writer);

        writeActionLinks2(facesContext, spchk, writer, uniqueId, asDialog);
    }
    
    protected void writeActionLinks2(FacesContext facesContext, Pagelet spchk, ResponseWriter writer, String uniqueId,
            boolean asDialog) throws IOException {
        String divId      = uniqueId + DIV_ID_SUFFIX;
        String varName    = uniqueId + TEXTBOX_ID_SUFFIX;
        String textAreaId = uniqueId + TEXT_AREA_SUFFIX;
        String formName   = uniqueId + FORM_SUFFIX;


        String actionSpanId = getActionSpanId(uniqueId);
        String resizeSpanId = getResizeId(uniqueId);
        String statusSpanId = getStatusSpanId(uniqueId);

        String  zoomSpanId    = getZoomId(uniqueId);
        boolean writeLinks    = (spchk.getLinkSpellchecker() != null) && (spchk.getLinkResume() != null) && !spchk.isDisabled() && !spchk.isReadonly();
        boolean writeResizer  = (spchk.getLinkResize() != null) && (spchk.getLinkDownsize() != null) && !spchk.isDisabled();
        boolean writeZoom     = (spchk.getLinkZoom() != null) && !spchk.isDisabled() && !asDialog;
        String  methodBinding = spchk.getSpellchecker().getExpressionString();

        startPositionColumn(writer);

        {

            // resizer
            writer.startElement("div", null);

            if (!spchk.getControlMode().equals(Pagelet.COMTROL_MODE_PAGELET_RAWTEXT)) {

                if (!asDialog)
                    writeActionLinks(writeLinks, writer, varName, facesContext, spchk, resizeSpanId);

                writeBR(writer);

                writeResizerLinks(writer, facesContext, spchk, textAreaId, resizeSpanId, zoomSpanId, writeResizer, writeZoom);

                writeBR(writer);

                if (!asDialog)
                    writeStatusSpan(writer, statusSpanId);
            } else {

                if (!asDialog)
                    writeResizerLinks(writer, facesContext, spchk, textAreaId, resizeSpanId, zoomSpanId, writeResizer, writeZoom);

            }

            writer.endElement("div");
        }

        endPositionColumn(writer);
        endWriterPositioningTable(writer);


        // script
        if (!asDialog) {
            writeScript(writer, spchk, facesContext, varName, spchk, divId, formName, actionSpanId, statusSpanId, textAreaId, methodBinding,
                writeLinks);
        }
    }
    

    protected void writeActionLinks(boolean writeLinks, ResponseWriter writer, String varName, FacesContext facesContext,
            Pagelet spchk, String resizerId) throws IOException {

        if (writeLinks) {
            String uniqueId      = calcUniqueId(spchk, facesContext);
            String linkSpellchId = uniqueId + "spellchk"; // TODO unique
            String linkResumeId  = uniqueId + "resume"; // TODO unique
            writer.startElement("div", null);
            writer.writeAttribute("id", linkSpellchId, null);
            writer.writeAttribute("onclick", "displayInline('" + linkResumeId + "');setCurrentObject(" + varName + ");" + varName + ".spellCheck();displayNone('" + linkSpellchId + "');displayNone('" + resizerId + "');", null);
            RendererUtils.renderChild(facesContext, spchk.getLinkSpellchecker());
            writer.endElement("div");
            writer.startElement("div", null);
            writer.writeAttribute("id", linkResumeId, null);
            writer.writeAttribute("style", "display:none", null);
            writer.writeAttribute("onclick", "displayInline('" + linkSpellchId + "');setCurrentObject(" + varName + ");" + varName + ".resumeEditing();displayNone('" + linkResumeId + "');displayInline('" + resizerId + "');", null);
            RendererUtils.renderChild(facesContext, spchk.getLinkResume());
            writer.endElement("div");
        }
    }
    
    protected void writeScript(ResponseWriter writer, UIComponent uiComponent, FacesContext facesContext, String varName,
            Pagelet spchk, String divId, String formName, String actionSpanId, String statusSpanId, String textAreaId,
            String methodBinding, boolean writeLinks) throws IOException {
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        /*
         * @param varName - the name of the variable that the object is assigned
         * to (must be unique and the same as the variable) @param width - the
         * width of the spell checker textarea @param height - the height of the
         * spell checker textarea @param spellUrl - the url of the
         * spell_checker.php code @param divId - the id of the div that the
         * spell checker is contained in (must be unique) @param name - the name
         * of the textarea form element @param actionSpanId - the id of the
         * action span @param statusSpanId - the id of the status span @param id -
         * the id of the spell checker textarea (must be unique) @param
         * spellcheckermb Methodbinding // MODIFIED LIBRARY FOR THIS FEATURE
         */

        String       strValue = RendererUtils.getStringValue(facesContext, uiComponent);
        StringBuffer script   = new StringBuffer();

        script.append("var ").append(varName).append(" = new ajaxSpell('").append(varName).append("',").append(asString(spchk.getWidth())).append(",").append(asString(spchk.getHeight())).append(",'").append(getSpellcheckServletPath(facesContext)).append("', '").append(divId).append("', '").append(formName).append("', '").append(actionSpanId).append("', '").append(statusSpanId).append("', '").append(textAreaId).append("', '").append(methodBinding).append("',").append(asString(spchk.getTextSpellcheck(), (!writeLinks) ? "Check Spelling &amp; Preview" : null)).append(",").append(asString(spchk.getTextResume(), (!writeLinks) ? "Resume Editing" : null)).append(",").append(asString(spchk.getTextWorking(), "Working ...")).append(",").append(asString(spchk.getTextNoMisspellings(), "No Misspellings Found")).append(",").append(asString(spchk.getTextChecking(), "Checking ...")).append(");\n");
        script.append(varName).append(".objToCheck.value=unescape('").append(StringEscapeUtils.escapeJavaScript(strValue)).append("');\n"); // TODO

        // encode
        // value
        writer.writeText(script.toString(), null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    protected void writeTextarea(ResponseWriter writer, String textAreaId, Pagelet spchk, String formName) throws IOException {
        writer.startElement(HTML.TEXTAREA_ELEM, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "resizable", null);
        writer.writeAttribute(HTML.ID_ATTR, textAreaId, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "width:" + spchk.getWidth() + "px;height:" + spchk.getHeight() + "px", null);
        writer.writeAttribute("name", textAreaId, null); // TODO:possible

        // bug, have to
        // recheck that with
        // tom dual name
        writer.writeAttribute("name", formName, null);

        if (spchk.isDisabled())
            writer.writeAttribute(HTML.DISABLED_ATTR, spchk.isDisabled() + "", null);

        if (spchk.isReadonly())
            writer.writeAttribute(HTML.READONLY_ATTR, spchk.isReadonly() + "", null);

        writer.endElement(HTML.TEXTAREA_ELEM);
    }

}

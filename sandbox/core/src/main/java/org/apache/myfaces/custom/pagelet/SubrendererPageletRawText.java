package org.apache.myfaces.custom.pagelet;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;


/**
 * A subrenderer rendering the raw text component
 * @author werpu
 *
 */
public class SubrendererPageletRawText extends SubrendererTextarea {

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
        writeRawTextDialog(facesContext, spchk, writer);
        // end control panel
    }


    protected void writeTextRawTextDialogTA(FacesContext facesContext, Pagelet spchk, ResponseWriter writer, String uniqueId,
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
        startControlPanel(writer, divId);

        // textarea
        writeTextarea(writer, textAreaId, spchk, formName);

        // edit div
        writeEditDiv(writer, spchk, editDivId);
        endControlPanel(writer);
    }


    private void writeActionButtons(boolean writeLinks, ResponseWriter writer, String varName, FacesContext facesContext,
            Pagelet spchk, String resizerId) throws IOException {

        if (writeLinks) {
            AddResource addResource = AddResourceFactory.getInstance(facesContext);

            String uniqueId      = calcUniqueId(spchk, facesContext);
            String linkSpellchId = uniqueId + "spellchk"; // TODO unique
            String linkResumeId  = uniqueId + "resume"; // TODO unique
            writer.startElement("div", null);
            writer.writeAttribute("id", linkSpellchId, null);
            writer.writeAttribute(HTML.STYLE_ATTR, "float:right;", null);
            writer.writeAttribute("onclick", "displayInline('" + linkResumeId + "');setCurrentObject(" + varName + ");" + varName + ".spellCheck();displayNone('" + linkSpellchId + "');displayNone('" + resizerId + "');", null);
            writer.startElement("img", spchk);
            writer.writeAttribute("alt", "[Spellcheck]", null);
            writer.writeAttribute("src", addResource.getResourceUri(facesContext, PageletRenderer.class, "img/SPELLCHECK_0.gif"), null);
            writer.endElement("img");
            writer.endElement("div");
            writer.startElement("div", null);
            writer.writeAttribute("id", linkResumeId, null);
            writer.writeAttribute("style", "display:none;float: right;", null);
            writer.writeAttribute("onclick", "displayInline('" + linkSpellchId + "');setCurrentObject(" + varName + ");" + varName + ".resumeEditing();displayNone('" + linkResumeId + "');displayInline('" + resizerId + "');", null);
            writer.startElement("img", spchk);
            writer.writeAttribute("alt", "[Resume]", null);
            writer.writeAttribute("src", addResource.getResourceUri(facesContext, PageletRenderer.class, "img/RESUME_0.gif"), null);
            writer.endElement("img");
            writer.endElement("div");
        }
    }


    private void writeActionLinksRawText(FacesContext facesContext, Pagelet spchk, ResponseWriter writer, String uniqueId,
            boolean asDialog) throws IOException {
        String divId      = uniqueId + DIV_ID_SUFFIX;
        String varName    = uniqueId + TEXTBOX_ID_SUFFIX;
        String textAreaId = uniqueId + TEXT_AREA_SUFFIX;
        String formName   = uniqueId + FORM_SUFFIX;


        String actionSpanId = getActionSpanId(uniqueId);
        String resizeSpanId = getResizeId(uniqueId);
        String statusSpanId = getStatusSpanId(uniqueId);

        boolean writeLinks    = (spchk.getLinkSpellchecker() != null) && (spchk.getLinkResume() != null) && !spchk.isDisabled() && !spchk.isReadonly();
        String  methodBinding = spchk.getSpellchecker().getExpressionString();

        writeActionButtons(writeLinks, writer, varName, facesContext, spchk, resizeSpanId);
        writeEmptyFloatRight(spchk, writer);
        writeStatusSpanRawText(writer, statusSpanId);
        writeScript(writer, spchk, facesContext, varName, spchk, divId, formName, actionSpanId, statusSpanId, textAreaId, methodBinding,
            writeLinks);

    }

    private void writeRawTextDialog(FacesContext facesContext, Pagelet spchk, ResponseWriter writer) throws IOException {

        if (spchk.isDisabled())
            return; //no dialog at raw text

        String uniqueId = calcUniqueId(spchk, facesContext);
        /*we are in rawtext mode we need a new dialog system as well*/


        String dialogId         = uniqueId + "Dialog";
        String okId             = dialogId + "Ok";
        String cancelId         = dialogId + "Cancel";
        String dialogContentId  = uniqueId + "DlgContent";
        String sourceTextAreaId = uniqueId + TEXT_AREA_SUFFIX;
        String textAreaId       = dialogContentId + TEXT_AREA_SUFFIX;
        String correctionDiv    = dialogContentId + EDIT_DIV_SUFFIX;
        String statusSpanId     = getStatusSpanId(uniqueId);
        String linkSpellchId    = uniqueId + "spellchk"; // TODO unique
        String linkResumeId     = uniqueId + "resume"; // TODO unique


        String varName = dialogContentId + TEXTBOX_ID_SUFFIX;

        writer.startElement(HTML.SCRIPT_ELEM, spchk);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        writer.write("var " + dialogId + " = null;\n");

        /*function idJsp0_ajax_spellchecker1_div_zoomOn() {
                        var textArea = dojo.byId('idJsp0_ajax_spellchecker1_ta');

                    var viewport_size   = dojo.html.getViewportSize();

                    textArea.style.width        = (parseInt(viewport_size[0] ) - 120)+"px";
                    textArea.style.height       = (parseInt(viewport_size[1] ) - 120)+"px";

                        dlg.show();
                        dojo.byId('idJsp0_ajax_spellchecker1_ta').value = dojo.byId('idJsp0_ajax_spellchecker0_ta').value;
                }*/
        writer.write("function " + dialogId + "_zoomOn() { \n");
        /*      if(idJsp0_ajax_spellchecker8Dialog == null)
                idJsp0_ajax_spellchecker8Dialog = dojo.widget.createWidget("Dialog",{},dojo.byId("idJsp0_ajax_spellchecker8Dialog"));
                */


        writer.write("	var textArea = dojo.byId('" + textAreaId + "');\n");
        writer.write("	var viewport_size 	= dojo.html.getViewportSize();\n");
        writer.write(" textArea.style.width 	= (parseInt(viewport_size[0] ) - 120)+'px';\n");
        writer.write("	textArea.style.height	= (parseInt(viewport_size[1] ) - 120)+'px';\n");

        writer.write(dialogId + ".show();\n");
        writer.write("//double call due to weird ie timing issues\n");
        writer.write(dialogId + ".show();\n");
        writer.write(" if(dojo.byId('" + textAreaId + "') != null && dojo.byId('" + textAreaId + "').style.visibility != 'hidden') \n");

        if (!spchk.isReadonly()) {
            writer.write(" try { \n");
            writer.write(varName + ".resetSpellChecker();\n");
            writer.write("displayNone('" + linkResumeId + "');\n");
            writer.write("displayInline('" + linkSpellchId + "');\n");
            writer.write(" } catch (e) {};\n");


        }

        writer.write(" 	dojo.byId('" + textAreaId + "').value = dojo.byId('" + sourceTextAreaId + "').value;\n");
        writer.write(" try { \n");
        writer.write(" 		dojo.byId('" + textAreaId + "').focus();");
        writer.write(" } catch (e) {};\n");
        //writer.write("        dojo.byId('" + correctionDiv + "').innerHTML = dojo.byId('" + sourceTextAreaId + "').value;\n");
        //writer.write(" } catch (e) {};\n");


        writer.write("}\n");

        /*function idJsp0_ajax_spellchecker1_div_zoomOff() {
                        dojo.byId('idJsp0_ajax_spellchecker0_ta').value = dojo.byId('idJsp0_ajax_spellchecker1_ta').value;
                        dlg.hide();
                }*/
        writer.write("function " + dialogId + "_zoomOff() { \n");
        writer.write("	dojo.byId('" + sourceTextAreaId + "').value = dojo.byId('" + textAreaId + "').value;");
        writer.write(dialogId + ".hide();\n");
        writer.write("	dojo.byId('" + sourceTextAreaId + "').focus();");

        writer.write("}\n");
        //TODO we have to move this into the core code
        //but for now we leave it this way, but
        //it has to be moved into the pure javascript core code
        //to reduce the code bloat

        /*
                function init() {
                                dlg = dojo.widget.byId("idJsp0_ajax_spellchecker1_div");
                                var btn  = dojo.byId("cancelx");
                                var btn2 = dojo.byId("okx");
                                //dojo.byId('idJsp0_ajax_spellchecker1_ta').setAttribute("dojoType", "resizabletextarea");
                                dojo.event.connect(btn, "onclick", dlg, "hide");
                                dojo.event.connect(btn2, "onclick","idJsp0_ajax_spellchecker1_div_zoomOff");
                                dojo.event.connect(dojo.byId('zoombt'), "onclick", 'idJsp0_ajax_spellchecker1_div_zoomOn');
                        }
                        setTimeout("init()",0);
         */
        String zoomSpanId = getZoomId(uniqueId);
        writer.write(" function " + dialogId + "_init() {\n");

        //writer.write(" " + dialogId + " = dojo.widget.byId('" + dialogId + "');\n");
        writer.write(" dojo.event.connect(dojo.byId('" + zoomSpanId + "'), 'onclick', '" + dialogId + "_zoomOn');\n");
        writer.write(" dojo.event.connect(dojo.byId('" + okId + "'), 'onclick', '" + dialogId + "_zoomOff');\n");

        writer.write("   " + dialogId + " = dojo.widget.createWidget('Dialog',{},dojo.byId('" + dialogId + "'));\n");
        writer.write(" dojo.event.connect(dojo.byId('" + cancelId + "'), 'onclick'," + dialogId + ", 'hide');\n");

        //TODO do the event connectors to all this, ok and cancel are still missing
        writer.write("}\n");
        writer.write(" setTimeout('" + dialogId + "_init()',1000);\n");
        writer.endElement(HTML.SCRIPT_ELEM);

        writer.startElement(HTML.DIV_ELEM, spchk);
        writer.writeAttribute(HTML.ID_ATTR, dialogId, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "visibility: hidden;", null);
        writeTextRawTextDialogTA(facesContext, spchk, writer, dialogContentId, true);


        writer.startElement(HTML.DIV_ELEM, spchk);

        {

            if (spchk.getLinkPopupLabel() != null) {
                RendererUtils.renderChild(facesContext, spchk.getLinkPopupLabel());
            }

            AddResource addResource = AddResourceFactory.getInstance(facesContext);

            writeEmptyFloatRight(spchk, writer);
            writeOkImage(facesContext, spchk, writer, okId, "float:right;", addResource);
            writeEmptyFloatRight(spchk, writer);
            writeCancelImg(facesContext, spchk, writer, cancelId, "float:right;", addResource);
            writeEmptyFloatRight(spchk, writer);
            writeActionLinksRawText(facesContext, spchk, writer, dialogContentId, true);


        }

        writer.endElement(HTML.DIV_ELEM);

        writer.endElement(HTML.DIV_ELEM);

    }

    private void writeStatusSpanRawText(ResponseWriter writer, String statusSpanId) throws IOException {
        writer.startElement("div", null);

        writer.writeAttribute("className", "status", null);
        writer.writeAttribute("style", "float:right;", null);
        writer.writeAttribute("id", statusSpanId, null);
        writer.endElement("div");
    }

}

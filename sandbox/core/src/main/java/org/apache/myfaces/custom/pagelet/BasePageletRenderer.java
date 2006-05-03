package org.apache.myfaces.custom.pagelet;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTextareaRendererBase;


/**
 * Base pagelet class with common suffixes and prefixes
 * and a set of accessor helpers easing
 * the structures
 *
 * @author werpu
 *
 */
public class BasePageletRenderer extends HtmlTextareaRendererBase {

    protected static final String TEXTBOX_ID_SUFFIX = "_editor";
    protected static final String DIV_ID_SUFFIX     = "_div";
    protected static final String FORM_SUFFIX       = "_form";
    protected static final String TEXT_AREA_SUFFIX  = "_ta";
    protected static final String EDIT_DIV_SUFFIX   = TEXTBOX_ID_SUFFIX + "_edit_div";
    protected static final String SERVLET_PATH      = "spellchecker";

    protected static String asString(int value) {
        return (value == 0) ? "null" : ("'" + Integer.toString(value) + "'");
    }

    protected static String asString(String value, String defaultValue) {

        if (value == null) {
            return (defaultValue == null) ? "null" : ("'" + defaultValue + "'");
        }

        return value;
    }

    protected String calcUniqueId(UIComponent uiComponent, FacesContext facesContext) {
        String uniqueId = uiComponent.getClientId(facesContext).replaceAll(":", "_");

        if (uniqueId.startsWith("_") && (uniqueId.length() > 1))
            uniqueId = uniqueId.substring(1, uniqueId.length());

        return uniqueId;
    }

    protected void endContentColum(ResponseWriter writer) throws IOException {
        writer.endElement(HTML.TD_ELEM);
    }

    protected void endControlPanel(ResponseWriter writer) throws IOException {
        writer.endElement("div");
    }

    protected void endPositionColumn(ResponseWriter writer) throws IOException {
        writer.endElement(HTML.TD_ELEM);
        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        writer.endElement(HTML.TD_ELEM);
    }

    protected void endWriterPositioningTable(ResponseWriter writer) throws IOException {
        writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }

    protected String getActionSpanId(String uniqueId) {
        String actionSpanId = uniqueId + "action";

        return actionSpanId;
    }

    protected String getPageletBackupControlId(String uniqueId) {
        String backupControlId = uniqueId + "_ta"; // TODO check if this one is

        // valid

        return backupControlId;
    }

    protected String getPageletCancelId(String dialogId) {
        return dialogId + "_cancel";
    }

    protected String getPageletClearId(String dialogId) {
        return dialogId + "_clear";
    }

    protected String getPageletDialogId(String uniqueId) {
        String dialogId = uniqueId + "DialogContent";

        return dialogId;
    }

    protected String getPageletEditorId(String dialogId) {
        return dialogId + "_editor";
    }

    protected String getPageletEventhandlerId(String uniqueId) {
        return uniqueId + "EvtHandler";
    }

    protected String getPageletSpellerId(String dialogId) {
        return dialogId + "_speller";
    }

    protected String getPageletSubmitterId(String dialogId) {
        return dialogId + "_submitter";
    }

    protected String getResizeId(String uniqueId) {
        String resizeSpanId = uniqueId + "resize";

        return resizeSpanId;
    }

    protected String getSpellcheckServletPath(FacesContext facesContext) {

        //TODO map this for the phase listener approach into a jsf call
        return AjaxSpellcheckerServlet.getServletPath((ServletRequest) facesContext.getExternalContext().getRequest(), SERVLET_PATH);
    }

    protected String getStatusSpanId(String uniqueId) {
        String statusSpanId = uniqueId + "status";

        return statusSpanId;
    }

    protected String getZoomId(String uniqueId) {
        String zoomSpanId = uniqueId + "zoom";

        return zoomSpanId;
    }

    protected void startContentColum(ResponseWriter writer) throws IOException {
        writer.startElement(HTML.TD_ELEM, null);
    }

    protected void startControlPanel(ResponseWriter writer, String divId) throws IOException {
        writer.startElement("div", null);
        writer.writeAttribute("id", divId, null);
    }

    protected void startPositionColumn(ResponseWriter writer) throws IOException {
        writer.startElement(HTML.TD_ELEM, null);
        writer.writeAttribute("VALIGN", "top", null);
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.startElement(HTML.TR_ELEM, null);
        writer.startElement(HTML.TD_ELEM, null);
    }

    protected void startWriterPositioningTable(ResponseWriter writer) throws IOException {
        writer.startElement(HTML.TABLE_ELEM, null);
        writer.startElement(HTML.TR_ELEM, null);
    }

    protected void writeBR(ResponseWriter writer) throws IOException {
        writer.startElement(HTML.BR_ELEM, null);
        writer.endElement(HTML.BR_ELEM);
    }

    protected void writeCancelImg(FacesContext facesContext, UIComponent uiComponent, ResponseWriter writer, String btcancelId,
            String buttonStyle, AddResource addResource) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", btcancelId, null);
        writer.writeAttribute("style", buttonStyle, null);
        writer.startElement("img", uiComponent);
        writer.writeAttribute("alt", "[Cancel]", null);
        writer.writeAttribute("src", addResource.getResourceUri(facesContext, PageletRenderer.class, "img/CANCEL_0.gif"), null);
        writer.endElement("img");
        writer.endElement("div");
    }

    protected void writeClearImage(FacesContext facesContext, UIComponent uiComponent, ResponseWriter writer, String btclearId,
            String buttonStyle, AddResource addResource) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", btclearId, null);
        writer.writeAttribute("style", buttonStyle, null);
        writer.startElement("img", uiComponent);
        writer.writeAttribute("alt", "[Clear]", null);
        writer.writeAttribute("src", addResource.getResourceUri(facesContext, PageletRenderer.class, "img/CLEAR_0.gif"), null);
        writer.endElement("img");
        writer.endElement("div");
    }

    protected void writeEditDiv(ResponseWriter writer, UIComponent uiComponent, String editDivId) throws IOException {
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, editDivId, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "text-align:left;", null);
        writer.endElement(HTML.DIV_ELEM);
    }

    protected void writeEmptyFloatRight(Pagelet spchk, ResponseWriter writer) throws IOException {
        writer.startElement("div", spchk);
        writer.writeAttribute("style", "float:right;", null);
        writer.write("&nbsp;");
        writer.endElement("div");
    }

    protected void writeOkImage(FacesContext facesContext, UIComponent uiComponent, ResponseWriter writer, String btsubmitterId,
            String buttonStyle, AddResource addResource) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", btsubmitterId, null);
        writer.writeAttribute("style", buttonStyle, null);
        writer.startElement("img", uiComponent);
        writer.writeAttribute("alt", "[OK]", null);
        writer.writeAttribute("src", addResource.getResourceUri(facesContext, PageletRenderer.class, "img/OK_0.gif"), null);
        writer.endElement("img");
        writer.endElement("div");
    }

    protected void writeResizerLinks(ResponseWriter writer, FacesContext context, Pagelet spellchk, String textAreaId,
            String resizerId, String zoomId, boolean writeResizer, boolean writeZoom) throws IOException {

        writer.startElement("span", null);
        writer.writeAttribute("id", resizerId, null);

        // if none of the zoom width and heights are set we go for sane values
        // according
        // due to different widths and heights of normal user screens (which can
        // vary
        // due to the shift towards widescreen, so we go for a 1:1.5 ratio to be
        // in a safe
        // area)
        int zoomWidth = Math.max(spellchk.getWidth(), spellchk.getZoomWidth());

        if (spellchk.getZoomWidth() == 0)
            zoomWidth = spellchk.getWidth() + 150;

        int zoomHeight = Math.max(spellchk.getHeight(), spellchk.getZoomHeight());

        if (spellchk.getZoomHeight() == 0)
            zoomHeight = spellchk.getHeight() + 100;

        if (writeZoom) {

            if (!spellchk.getControlMode().equals(Pagelet.COMTROL_MODE_PAGELET_RAWTEXT)) {
                writer.startElement("div", null);
                writer.writeAttribute("onclick", "myfaces_toggleZoomTa('" + textAreaId + "', " + zoomWidth + ", " + zoomHeight + ", 10);", null);
                RendererUtils.renderChild(context, spellchk.getLinkZoom());
                writer.endElement("div");
                writeBR(writer);
            } else {
                writer.startElement("div", spellchk);
                writer.writeAttribute("id", zoomId, null);
                RendererUtils.renderChild(context, spellchk.getLinkZoom());
                writer.endElement("div");
                writeBR(writer);

            }
        }

        if (writeResizer) {
            writer.startElement("div", null);
            writer.writeAttribute("onclick", "resizeTa('" + textAreaId + "', 20, " + zoomHeight + ");", null);
            RendererUtils.renderChild(context, spellchk.getLinkResize());
            writer.endElement("div");

            writeBR(writer);

            writer.startElement("div", null);
            writer.writeAttribute("onclick", "downsizeTa('" + textAreaId + "', 20, " + spellchk.getHeight() + ");", null);
            RendererUtils.renderChild(context, spellchk.getLinkDownsize());
            writer.endElement("div");
        }

        writer.endElement("span");
    }

    protected void writeSpellcheckImage(FacesContext facesContext, UIComponent uiComponent, ResponseWriter writer, String btspellerId,
            String buttonStyle, AddResource addResource) throws IOException {
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", btspellerId, null);
        writer.writeAttribute("style", buttonStyle, null);
        writer.startElement("img", uiComponent);
        writer.writeAttribute("alt", "[Spellcheck]", null);
        writer.writeAttribute("src", addResource.getResourceUri(facesContext, PageletRenderer.class, "img/SPELLCHECK_0.gif"), null);
        writer.endElement("img");
        writer.endElement("div");
    }

    protected void writeStatusSpan(ResponseWriter writer, String statusSpanId) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("className", "status", null);
        writer.writeAttribute("id", statusSpanId, null);
        writer.endElement("span");
    }

}

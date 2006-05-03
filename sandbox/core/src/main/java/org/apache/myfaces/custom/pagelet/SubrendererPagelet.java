package org.apache.myfaces.custom.pagelet;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;


/**
 * Refactored subrenderer for the pagelet mode
 * of the pagelet component
 * this class renders only the rich edit paglet mode part
 * of the component
 *
 * @author werpu
 *
 */
public class SubrendererPagelet extends BasePageletRenderer {

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        encodeEndPagelet(facesContext, uiComponent);

    }

    /**
     * general pagelet encoding, can be shared among all pagelet types
     *
     * @param facesContext
     * @param uiComponent
     */
    public void encodeJavascript(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Pagelet control = (Pagelet) uiComponent;

        String javascriptLocation = (String) control.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);

        AddResource addResource = AddResourceFactory.getInstance(facesContext);

        if (javascriptLocation != null)
            addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, javascriptLocation + "utils.js");
        else
            addResource.addJavaScriptAtPosition(facesContext, AddResource.HEADER_BEGIN, Pagelet.class, "utils.js");

        DojoUtils.addMainInclude(facesContext, uiComponent, javascriptLocation, new DojoConfig());

        DojoUtils.addRequire(facesContext, uiComponent, "dojo.event.*");
        DojoUtils.addRequire(facesContext, uiComponent, "dojo.html");
        DojoUtils.addRequire(facesContext, uiComponent, "dojo.widget.Editor");

        DojoUtils.addRequire(facesContext, uiComponent, "dojo.widget.Dialog");
        DojoUtils.addRequire(facesContext, uiComponent, "dojo.widget.Tooltip");

        // todo until we have a a new version of myfaces
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        
        writer.write("dojo.hostenv.setModulePrefix('org.apache.myfaces.pagelet', '../pagelet.PageletRenderer/');\n");
        writer.endElement(HTML.SCRIPT_ELEM);

        if (!control.isReadonly() && !control.isDisabled())
            DojoUtils.addRequire(facesContext, uiComponent, "org.apache.myfaces.pagelet.MyFacesHiddenRichText");

        DojoUtils.addRequire(facesContext, uiComponent, "dojo.fx.html");
        DojoUtils.addRequire(facesContext, uiComponent, "dojo.event.*");

        if (!DojoUtils.isInlineScriptSet(facesContext, "progressor.js"))
            addResource.addJavaScriptHere(facesContext, PageletRenderer.class, "progressor.js");

        if (!DojoUtils.isInlineScriptSet(facesContext, "pagelet.js"))
            addResource.addJavaScriptHere(facesContext, PageletRenderer.class, "pagelet.js");

        String styleLocation = (String) uiComponent.getAttributes().get(JSFAttr.STYLE_LOCATION);

        // we some styles for the notifier subsystem
        if (StringUtils.isNotBlank(styleLocation)) {
            addResource.addStyleSheet(facesContext, AddResource.HEADER_BEGIN, styleLocation + "/pagelet.css");
        } else {
            addResource.addStyleSheet(facesContext, AddResource.HEADER_BEGIN, PageletRenderer.class, "css/pagelet.css");
        }

        DojoUtils.addRequire(facesContext, uiComponent, "org.apache.myfaces.pagelet.*");

    }

    public void encodeJavascriptEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Pagelet spchk = (Pagelet) uiComponent;

        /*
         *
         * var dlg, dlg2; var evtHandler, evtHandler2;
         *
         * evtHandler = new
         * myfaces_Pagelet("evtHandler","DialogContent",'sensitiveField',
         * 'DialogContent_editor' );
         * dojo.event.connect(dojo.byId('sensitiveField'), "onclick",
         * evtHandler, "onShowdlg");
         * dojo.event.connect(dojo.byId('DialogContent_submitter'), "onclick",
         * evtHandler, "onDlgClose");
         * dojo.event.connect(dojo.byId('DialogContent_speller'),"onclick",
         * evtHandler, "onSpellCheck");
         * dojo.event.connect(dojo.byId('DialogContent_clear'),"onclick",
         * evtHandler, "onCleanup");
         *
         */
        String         uniqueId         = calcUniqueId(uiComponent, facesContext);
        ResponseWriter writer           = facesContext.getResponseWriter();
        String         dialogId         = getPageletDialogId(uniqueId);
        String         eventHandlerId   = getPageletEventhandlerId(uniqueId);
        String         internalEditorId = getPageletEditorId(dialogId);
        String         btsubmitterId    = getPageletSubmitterId(dialogId);
        String         btspellerId      = getPageletSpellerId(dialogId);
        String         btcancelId       = getPageletCancelId(dialogId);
        String         btclearId        = getPageletClearId(dialogId);
        String         methodBinding    = ((Pagelet) uiComponent).getSpellchecker().getExpressionString();

        writer.startElement("script", uiComponent);
        writer.writeAttribute("type", "text/javascript", null);

        // for inline editing
        writer.write("	var " + uniqueId + " = dojo.byId('" + uniqueId + "');\n");

        if (!spchk.isReadonly() && !spchk.isDisabled()) {
            writer.write("dojo.lang.setTimeout( function () { \n");
            writer.write(" var editor = dojo.widget.createWidget('MyFacesHiddenRichText', { widgetId:'" + uniqueId + "RichText' }, " + uniqueId + ");}, 0); \n");
        }

        // normal pagelet instantiation part
        writer.write("var " + uniqueId + "var;\n");
        writer.write("var " + eventHandlerId + ";\n");
        writer.write(uniqueId + "EvtHandler = new myfaces_Pagelet(\"" + uniqueId + "EvtHandler\",\"" + dialogId + "\",'" + uniqueId + "', '" + internalEditorId + "','" + getSpellcheckServletPath(facesContext) + "', '" + methodBinding + "'," + spchk.isReadonly() + ", '" + spchk.getControlMode() + "' );\n");

        if (!spchk.isDisabled()) {

            if (spchk.isReadonly())
                writer.write(" dojo.event.connect(dojo.byId('" + btsubmitterId + "'), \"onclick\", " + eventHandlerId + ", \"onDlgCancel\");\n");

            if (!spchk.isReadonly()) {
                writer.write(" dojo.event.connect(dojo.byId('" + btsubmitterId + "'), \"onclick\", " + eventHandlerId + ", \"onDlgClose\");\n");
                writer.write(" dojo.event.connect(dojo.byId('" + btcancelId + "'), \"onclick\", " + eventHandlerId + ", \"onDlgCancel\");\n");
                writer.write(" dojo.event.connect(dojo.byId('" + btspellerId + "'),\"onclick\", " + eventHandlerId + ", \"onSpellCheck\");\n");
                writer.write(" dojo.event.connect(dojo.byId('" + btclearId + "'),\"onclick\", " + eventHandlerId + ", \"onCleanup\");\n");
            }
        }

        writer.endElement("script");
    }

    /**
     * Read only pagelet encoding
     *
     * @param facesContext
     * @param uiComponent
     * @throws IOException
     */
    private void encodeEndPagelet(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, Pagelet.class);

        if (!uiComponent.isRendered())
            return;

        String uniqueId = calcUniqueId(uiComponent, facesContext);

        // we have to shift the value binding in the decode from the back into
        // the value
        String         dialogId        = getPageletDialogId(uniqueId);
        String         backupControlId = getPageletBackupControlId(uniqueId);
        String         eventHandlerId  = getPageletEventhandlerId(uniqueId);
        ResponseWriter writer          = facesContext.getResponseWriter();

        renderPageletPageSingleton(facesContext, uiComponent, writer);

        Pagelet spchk = (Pagelet) uiComponent;
        encodeJavascript(facesContext, uiComponent);

        /*
         * the main holder component <input type="hidden"
         * id="sensitiveField2_back" /> <div id="sensitiveField2"
         * style="background-color: lightGrey; width: 200px; height: 200px;
         * overflow: auto;"> hello snafu world </div>
         */
        String style      = (String) uiComponent.getAttributes().get("style");
        String styleClass = (String) uiComponent.getAttributes().get("styleClass");

        Integer width  = (Integer) uiComponent.getAttributes().get("width");
        Integer height = (Integer) uiComponent.getAttributes().get("height");

        width  = (width == null) ? new Integer(200) : width;
        height = (height == null) ? new Integer(200) : height;

        if (style == null)
            style = "width: " + width.toString() + "px; height: " + height.toString() + "px; overflow: auto;";

        writePageletDialog(facesContext, uiComponent, dialogId, writer);

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", backupControlId, null);
        writer.writeAttribute("name", backupControlId, null);

        String strValue = org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils.getStringValue(facesContext, uiComponent);
        writer.writeAttribute("value", strValue, org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr.VALUE_ATTR);

        writer.endElement("input");

        startWriterPositioningTable(writer);
        startContentColum(writer);

        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, uniqueId, null);
        writer.writeAttribute("style", style, null);

        if (styleClass != null)
            writer.writeAttribute("class", styleClass, null);
        else if (!spchk.isDisabled() && !spchk.isReadonly())
            writer.writeAttribute("class", "pagelet", null);
        else if (spchk.isDisabled())
            writer.writeAttribute("class", "pagelet_inactive", null);
        else
            writer.writeAttribute("class", "pagelet_readonly", null);

        writer.endElement(HTML.DIV_ELEM);
        endContentColum(writer);
        startPositionColumn(writer);

        if ((spchk.getLinkZoom() != null) && !spchk.isDisabled()) {

            // resizer
            writer.startElement(HTML.DIV_ELEM, null);
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, uniqueId + "zoomer", null);
            RendererUtils.renderChild(facesContext, spchk.getLinkZoom());
            writer.endElement(HTML.DIV_ELEM);
            writeBR(writer);
            writeResizerLinks(writer, facesContext, spchk, uniqueId, uniqueId + "resizer", null, spchk.getLinkResize() != null, false);
            writeBR(writer);
            writer.endElement(HTML.DIV_ELEM);
        }

        endPositionColumn(writer);
        endWriterPositioningTable(writer);

        writer.startElement("script", uiComponent);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("var " + uniqueId + "_filler = dojo.byId('" + uniqueId + "'); \n");
        writer.write(uniqueId + "_filler.innerHTML = unescape('" + StringEscapeUtils.escapeJavaScript(strValue) + "');");
        writer.endElement("script");

        encodeJavascriptEnd(facesContext, uiComponent);

        if (!spchk.isDisabled()) {

            // now do the special link encoding
            writer.startElement("script", uiComponent);
            writer.writeAttribute("type", "text/javascript", null);

            // direct click
            if (spchk.getLinkZoom() == null)
                writer.write("dojo.event.connect(dojo.byId(\"" + uniqueId + "\"), \"onclick\", " + eventHandlerId + ", \"onShowdlg\");\n");

            // zoom button
            else
                writer.write("dojo.event.connect(dojo.byId(\"" + uniqueId + "zoomer\"), \"onclick\", " + eventHandlerId + ", \"onShowdlg\");\n");

            writer.endElement("script");
        }
    }

    /**
     * renders the pagelet page singleton part this part is only rendered once
     * and used by all pagelet in a shared manner it for now is mainly only the
     * initializer code for the progress display and the floating suggestion
     * list
     *
     * @param facesContext
     * @param uiComponent
     * @param writer
     * @throws IOException
     */
    private void renderPageletPageSingleton(FacesContext facesContext, UIComponent uiComponent, ResponseWriter writer) throws IOException {

        // first render generalPageletStructure
        if (((HttpServletRequest) facesContext.getExternalContext().getRequest()).getAttribute("PAGELET_CORE_SINGLETON") == null) {

            // initialize the core singletoned code for this component
            ((HttpServletRequest) facesContext.getExternalContext().getRequest()).setAttribute("PAGELET_CORE_SINGLETON", new Boolean(true));

            // write the tooltip for the spellcheck results first
            writer.startElement("div", uiComponent);
            writer.writeAttribute("id", "dojoDialog_spellcheckResults", null);
            writer.writeAttribute("style", "position:fixed;width:600px;left:20;top:20; visibility:hidden;", null);
            writer.write("results");
            writer.endElement("div");

            // in progress page part
            writer.startElement("div", uiComponent);
            writer.writeAttribute("id", "dojoDialog_inprogress", null);
            writer.writeAttribute("style",
                "position:absolute; width: 100px;left:0;top:0;background-color: red;visibility: hidden; z-index: 999;", null);
            writer.write("Please wait");
            writer.endElement("div");
        }
    }

    /**
     * writes the pagelet dialog subsystem this is the rendering code utilized
     * as the main dialog for the html editor of the pagelet
     *
     * @param uiComponent
     * @param dialogId
     * @param writer
     * @throws IOException
     */
    private void writePageletDialog(FacesContext facesContext, UIComponent uiComponent, String dialogId, ResponseWriter writer)
        throws IOException {

        /*
         * <div class="dojoDialogSpeller" id="DialogContent2"
         * style="position:absolute;width:600px;left:20;top:20;background-color:
         * lightGrey; visibility: hidden; "> <TEXTAREA
         * id="DialogContent2_editor"></TEXTAREA> <button
         * id="DialogContent2_submitter"> [ok] </button> <button
         * id="DialogContent2_speller"> [spell] </button> <button
         * id="DialogContent2_clear"> [clear] </button> </div>
         */
        Pagelet spchk = (Pagelet) uiComponent;

        String internalEditorId = getPageletEditorId(dialogId);
        String btsubmitterId    = getPageletSubmitterId(dialogId);
        String btspellerId      = getPageletSpellerId(dialogId);
        String btcancelId       = getPageletCancelId(dialogId);
        String btclearId        = getPageletClearId(dialogId);

        String style = "visibility: hidden; background-color: #fff;text-align: left;";

        String buttonStyle    = "cursor: pointer;float: right; font-weight: bold; position: relative; right: auto;";
        String buttonBarStyle = "background-color: rgb(204,204,255); width: 100%; position: relative; bottom: 0px;";
        // String buttonBarStyle = "";

        writer.startElement("div", uiComponent);
        writer.writeAttribute("class", "dojoDialogSpeller", null);
        writer.writeAttribute("id", dialogId, null);
        writer.writeAttribute("style", style, null);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", internalEditorId, null);
        writer.endElement("div");

        AddResource addResource = AddResourceFactory.getInstance(facesContext);

        writer.startElement("div", uiComponent);
        writer.writeAttribute("style", buttonBarStyle, null);

        writer.startElement("table", uiComponent);
        writer.writeAttribute("width", "50%", null);
        writer.writeAttribute("style", "float:left;", null);
        writer.startElement("tr", uiComponent);

        if (spchk.getLinkPopupLabel() != null) {
            writer.startElement("td", uiComponent);
            writer.writeAttribute("align", "left", null);

            RendererUtils.renderChild(facesContext, spchk.getLinkPopupLabel());
            writer.endElement("td");
        }

        writer.endElement("tr");
        writer.endElement("table");

        writer.startElement("table", uiComponent);
        writer.writeAttribute("width", "40%", null);
        writer.writeAttribute("style", "float:right;", null);
        writer.startElement("tr", uiComponent);

        writer.startElement("td", uiComponent);
        writer.writeAttribute("align", "right", null);

        if (!spchk.isReadonly()) {

            writeClearImage(facesContext, uiComponent, writer, btclearId, buttonStyle, addResource);
            writeCancelImg(facesContext, uiComponent, writer, btcancelId, buttonStyle, addResource);
            writeSpellcheckImage(facesContext, uiComponent, writer, btspellerId, buttonStyle, addResource);

        }

        writeOkImage(facesContext, uiComponent, writer, btsubmitterId, buttonStyle, addResource);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        writer.endElement("div");
        writer.endElement("div");
    }

}

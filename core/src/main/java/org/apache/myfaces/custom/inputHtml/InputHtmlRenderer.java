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
package org.apache.myfaces.custom.inputHtml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.FacesException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.custom.tabbedpane.HtmlPanelTab;
import org.apache.myfaces.custom.tabbedpane.HtmlPanelTabbedPane;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.HTMLEncoder;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.JavascriptUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.FormInfo;

/**
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Input"
 *   type = "org.apache.myfaces.InputHtml"
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputHtmlRenderer extends HtmlRenderer {
    // TODO : Enable multiple editors on one page
    // TODO : Fix i18n (check the kupustart_form.js)
    // TODO : Finish Disabled mode.
    // TODO : Automatic Fallback for non kupu capable browsers (Safari, smartphones, non javascript, ...).
    // TODO : Make Image & Link Library work.
    // TODO : Allow disabeling of tag filtering
    // TODO : Fix height while zoomed

    private static final Log log = LogFactory.getLog(HtmlRendererUtils.class);
    
    private static String ROWSPAN_ATTR = "rowspan";
    
    private static String I18N_TRANSLATE_ATTR = "i18n:translate";
    private static String I18N_ATTRIBUTES_ATTR = "i18n:attributes";

    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent) {
        if( !UserRoleUtils.isEnabledOnUserRole(uiComponent) )
            return false;

        return ((InputHtml)uiComponent).isDisabled();
    }

    private static boolean useFallback(InputHtml editor){
        // TODO : Handle fallback="auto"
        return editor.getFallback().equals("true");
    }

    public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException {
        RendererUtils.checkParamValidity(context, uiComponent, InputHtml.class);
        InputHtml editor = (InputHtml) uiComponent;
        if( HtmlRendererUtils.isDisplayValueOnly(editor) )
            encodeDisplayValueOnly(context, editor);
        else if( useFallback(editor) )
            encodeEndFallBackMode(context, editor);
        else{
            // HACK
            // As only one inputHtml is supported by page in this version,
            // we need to make sure we don't encode hidden inputHtml
            // TODO : Fix this by supporting multiple inputHtml per page.

            if( ! isVisible(editor) ){
                encodeHidden(context, editor);
            }else if( ! hasThisPageAlreadyRenderedAnInputHtml( context ) ){
                encodeEndNormalMode(context, editor);
                setThisPageAlreadyRenderedAnInputHtml( context );
            }else{
                log.warn("Only one inputHtml can be displayed at the same time. The component will be rendered isung a textarea." +
                         "\nConpoment : "+RendererUtils.getPathToComponent( editor ));
                encodeEndFallBackMode(context, editor);
            }
        }
    }

    static private boolean hasThisPageAlreadyRenderedAnInputHtml(FacesContext context){
            return context.getExternalContext().getRequestMap().containsKey( InputHtmlRenderer.class.getName() );
    }

    static private void setThisPageAlreadyRenderedAnInputHtml(FacesContext context){
        context.getExternalContext().getRequestMap().put(InputHtmlRenderer.class.getName(), Boolean.TRUE);
    }

    /**
     * Try to figure out if this component is visible to avoid rendering the code if not necessary.
     */
    private boolean isVisible(InputHtml editor){
            for(UIComponent parent = editor.getParent(); parent != null ; parent = parent.getParent()){
                if( parent instanceof HtmlPanelTab ){
                    HtmlPanelTab panelTab = (HtmlPanelTab) parent;
                    HtmlPanelTabbedPane panelTabbedPane = null;
                    for(UIComponent panelAncestor=panelTab.getParent(); panelAncestor!=null ; panelAncestor=panelAncestor.getParent()){
                        if( panelAncestor instanceof HtmlPanelTabbedPane ){
                            panelTabbedPane = (HtmlPanelTabbedPane)panelAncestor;
                            break;
                        }
                    }

                    if( panelTabbedPane != null ){
                    if( panelTabbedPane.isClientSide() ){
                        parent = panelTabbedPane;
                        continue;
                    }

                    // Not client side tabbed pane.
                    // We need to check if the current panel tab is bisible;
                    int selectedIndex = panelTabbedPane.getSelectedIndex();
                    List children = panelTabbedPane.getChildren();
                    int tabIdx = 0;
                    for (int i = 0, len = children.size(); i < len && tabIdx <= selectedIndex ; i++){
                        UIComponent child = htmlTabbedPaneRenderer_getUIComponent((UIComponent)children.get(i));
                        if (child instanceof HtmlPanelTab){
                                if( child == panelTab ){
                                    if( ! child.isRendered() || tabIdx != selectedIndex){
                                    return false;
                                }else{
                                        // It's visible. Check at upper level.
                                        parent = panelTabbedPane;
                                    continue;
                                }
                                }
                            tabIdx++;
                        }
                        }
                    }else{
                        log.debug("pannelTabbedPane == null for component "+RendererUtils.getPathToComponent(panelTab));
                    }
                }
            }

            return true;
    }

    /**
     * This is a copy of HtmlTabbedPaneRenderer.getUIComponent
     */
    private UIComponent htmlTabbedPaneRenderer_getUIComponent(UIComponent uiComponent)
    {
        /* todo: forms other than UIForm, e.g. Trinidad's UIForm */
        if (uiComponent instanceof UIForm || uiComponent instanceof UINamingContainer)
        {
            List children = uiComponent.getChildren();
            for (int i = 0, len = children.size(); i < len; i++)
            {
                uiComponent = htmlTabbedPaneRenderer_getUIComponent((UIComponent)children.get(i));
            }
        }
        return uiComponent;
    }

    private void encodeHidden(FacesContext context, InputHtml editor) throws IOException {
        String clientId = editor.getClientId(context);
        // Use a hidden textarea
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.TEXTAREA_ELEM, editor);

        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none", null);

        String text = RendererUtils.getStringValue(context, editor);
        writer.writeText(text, JSFAttr.VALUE_ATTR);

        writer.endElement(HTML.TEXTAREA_ELEM);
    }

    private void encodeDisplayValueOnly(FacesContext context, InputHtml editor) throws IOException {
        // Use only a textarea
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.SPAN_ELEM, editor);
        HtmlRendererUtils.writeIdIfNecessary(writer, editor, context);

        HtmlRendererUtils.renderDisplayValueOnlyAttributes(editor, writer);

        String text = RendererUtils.getStringValue(context, editor);
        writer.write( editor.getHtmlBody( text ) );

        writer.endElement(HTML.SPAN_ELEM);
    }

    private void encodeEndFallBackMode(FacesContext context, InputHtml editor) throws IOException {
        String clientId = editor.getClientId(context);
        // Use only a textarea
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.TEXTAREA_ELEM, editor);

        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        HtmlRendererUtils.writeIdIfNecessary(writer, editor, context);

        if( editor.getStyle()!=null )
            writer.writeAttribute(HTML.STYLE_ATTR, editor.getStyle(), null);
        if( editor.getStyleClass()!=null )
            writer.writeAttribute(HTML.CLASS_ATTR, editor.getStyleClass(), null);

        if (isDisabled(context, editor))
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);

        String text = RendererUtils.getStringValue(context, editor);
        writer.write( htmlToPlainText(text, editor) );

        writer.endElement(HTML.TEXTAREA_ELEM);
    }

    private static String htmlToPlainText(String html, InputHtml editor){
        return editor.getHtmlBody( html )
                .replaceAll("<br.*>","\n")
                .replaceAll("<.+?>", "");
    }

    private void encodeEndNormalMode(FacesContext context, InputHtml editor) throws IOException {
        String clientId = editor.getClientId(context);
        FormInfo parentFormInfo = RendererUtils.findNestingForm(editor, context);
        if(parentFormInfo == null)
            throw new FacesException("InputHtml must be embedded in a form.");
        String formId = parentFormInfo.getFormName();

        AddResource addResource = AddResourceFactory.getInstance(context);
        addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupustyles.css");
        addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupudrawerstyles.css");
        addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "myFaces.css");

        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "sarissa.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "sarissa_ieemu_xpath.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupuhelpers.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupueditor.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupubasetools.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupuloggers.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupunoi18n.js");
        //addResource.addJavaScriptAtPosition(context, InputHtmlRenderer.class, "i18n/i18n.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupucleanupexpressions.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupucontentfilters.js");

        if (editor.isShowAnyToolBox())
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kuputoolcollapser.js");
        }
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupucontextmenu.js");

        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupuinit.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupustart.js");

        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupusourceedit.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupuspellchecker.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupudrawers.js");
        
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "kupuundo.js");
        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "diff_match_patch.js");

        addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputHtmlRenderer.class, "myFacesUtils.js");

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM,editor);
        writer.writeAttribute(HTML.STYLE_ATTR, "display: none;",null);

            writer.startElement("xml", editor);
            writer.writeAttribute("id", "kupuconfig", null);
            writer.writeAttribute(HTML.CLASS_ATTR, "kupuconfig", null);

                writer.startElement("kupuconfig", editor);

                    writeTag(writer, "dst", "fulldoc.html", editor);
                    writeTag(writer, "use_css", "1", editor);
                    writeTag(writer, "reload_after_save", "0", editor);
                    writeTag(writer, "strict_output", "1", editor);
                    writeTag(writer, "content_type", "application/xhtml+xml", editor);
                    writeTag(writer, "compatible_singletons", "1", editor);

                    writer.startElement("table_classes", editor);
                        writeTag(writer, "class", "plain", editor);
                        writeTag(writer, "class", "listing", editor);
                        writeTag(writer, "class", "grid", editor);
                        writeTag(writer, "class", "data", editor);
                    writer.endElement("table_classes");

                    writer.startElement("cleanup_expressions",editor);
                        writer.startElement("set",editor);
                            writer.startElement("name",editor);
                                writer.write("Convert single quotes to curly ones");
                            writer.endElement("name");
                            writer.startElement("expression",editor);
                                writer.startElement("reg",editor);
                                    writer.write("(\\W)'");
                                writer.endElement("reg");
                                writer.startElement("replacement",editor);
                                    writer.write("\\1&#x8216;");
                                writer.endElement("replacement");
                            writer.endElement("expression");
                            writer.startElement("expression",editor);
                                writer.startElement("reg",editor);
                                    writer.write("'");
                                writer.endElement("reg");
                                writer.startElement("replacement",editor);
                                    writer.write("&#x8217;");
                                writer.endElement("replacement");
                            writer.endElement("expression");
                        writer.endElement("set");
                        writer.startElement("set",editor);
                        writer.startElement("name",editor);
                            writer.write("Reduce whitespace");
                        writer.endElement("name");
                        writer.startElement("expression",editor);
                            writer.startElement("reg",editor);
                                writer.write("[\\n\\r\\t]");
                            writer.endElement("reg");
                            writer.startElement("replacement",editor);
                                writer.write("\\x20");
                            writer.endElement("replacement");
                        writer.endElement("expression");
                        writer.startElement("expression",editor);
                            writer.startElement("reg",editor);
                                writer.write("[ ]{2}");
                            writer.endElement("reg");
                            writer.startElement("replacement",editor);
                                writer.write("\\x20");
                            writer.endElement("replacement");
                        writer.endElement("expression");
                      writer.endElement("set");
                    writer.endElement("cleanup_expressions");

                    writeTag(writer, "image_xsl_uri", addResource.getResourceUri(context, InputHtmlRenderer.class, "kupudrawers/drawer.xsl"), editor);
                    writeTag(writer, "link_xsl_uri", addResource.getResourceUri(context, InputHtmlRenderer.class, "kupudrawers/drawer.xsl"), editor);

                    // TODO : Make this work (reference available images, ...).
                    writeTag(writer, "image_libraries_uri", addResource.getResourceUri(context, InputHtmlRenderer.class, "kupudrawers/imagelibrary.xml"), editor);
                    writeTag(writer, "link_libraries_uri", addResource.getResourceUri(context, InputHtmlRenderer.class, "kupudrawers/linklibrary.xml"), editor);
                    writeTag(writer, "search_images_uri", "", editor);
                    writeTag(writer, "search_links_uri", "", editor);

                writer.endElement("kupuconfig");
            writer.endElement("xml");
        writer.endElement(HTML.DIV_ELEM);


        writer.startElement(HTML.DIV_ELEM,editor);
        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-fulleditor", null);
        writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
        writer.writeAttribute("i18n:domain","kupu",null);
        
            //
            // Toolbar
            //
            writer.startElement(HTML.DIV_ELEM,editor);
            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb",null);
            writer.writeAttribute(HTML.ID_ATTR, "toolbar",null);

                writer.startElement(HTML.SPAN_ELEM,editor);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-tb-buttons",null);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttons",null);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup",null);
                    writer.writeAttribute(HTML.STYLE_ATTR, "float: right", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-logo",null);
                    if( editor.isAddKupuLogo() ){
                        writer.startElement(HTML.BUTTON_ELEM,editor);
                        writer.writeAttribute(HTML.TYPE_ATTR, "button",null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-logo",null);
                        writer.writeAttribute(HTML.TITLE_ATTR, "Kupu 1.4.16",null);
                        writer.writeAttribute(I18N_ATTRIBUTES_ATTR,"title title_logo;",null);
                        writer.writeAttribute(HTML.ACCESSKEY_ATTR, "k",null);
                        writer.writeAttribute(HTML.ONCLICK_ATTR, "window.open('http://kupu.oscom.org');",null);
                        writer.write("&#160;");
                        writer.endElement(HTML.BUTTON_ELEM);
                    }
                    writer.endElement(HTML.SPAN_ELEM);
                    
                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR,"kupu-tb-buttongroup",null);
                    writer.writeAttribute(HTML.STYLE_ATTR,"float: right",null);
                    writer.writeAttribute(HTML.ID_ATTR,"kupu-zoom",null);
                        writer.startElement(HTML.BUTTON_ELEM,null);
                        writer.writeAttribute(HTML.TYPE_ATTR, "button",null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-zoom",null);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-zoom-button",null);
                        writer.writeAttribute(I18N_ATTRIBUTES_ATTR,"title title_zoom;",null);
                        writer.writeAttribute(HTML.TITLE_ATTR,"Zoom: Access key (Alt or Ctrl) + X",null);
                        writer.writeAttribute(HTML.ACCESSKEY_ATTR, "x",null);
                        writer.write("&#160;");
                        writer.endElement(HTML.BUTTON_ELEM);
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SELECT_ELEM,editor);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-tb-styles",null);
                        writer.startElement(HTML.OPTION_ELEM,editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "P",null);
                        writer.writeAttribute(I18N_TRANSLATE_ATTR,"Normal",null);
                        writer.write("Normal paragraph");
                        writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM,editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "H1",null);
                            writer.startElement(HTML.SPAN_ELEM,editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"Heading 1",null);
                            writer.write("Heading 1");
                            writer.endElement(HTML.SPAN_ELEM);
                        writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM, editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "H2",null);
                            writer.startElement(HTML.SPAN_ELEM,editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"Heading 2",null);
                            writer.write("Heading 2");
                            writer.endElement(HTML.SPAN_ELEM);
                        writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM,editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "H3",null);
                            writer.startElement(HTML.SPAN_ELEM,editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"Heading 3",null);
                            writer.write("Heading 3");
                            writer.endElement(HTML.SPAN_ELEM);

                            writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM,editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "H4",null);
                            writer.startElement(HTML.SPAN_ELEM,editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"Heading 4",null);
                            writer.write("Heading 4");
                            writer.endElement(HTML.SPAN_ELEM);

                        writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM,editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "H5",null);
                            writer.startElement(HTML.SPAN_ELEM,editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"Heading 5",null);
                            writer.write("Heading 5");
                            writer.endElement(HTML.SPAN_ELEM);

                        writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM,editor);
                        writer.writeAttribute(HTML.VALUE_ATTR, "H6",null);

                            writer.startElement(HTML.SPAN_ELEM,editor);

                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"Heading 6",null);
                            writer.write("Heading 6");
                            writer.endElement(HTML.SPAN_ELEM);

                        writer.endElement(HTML.OPTION_ELEM);

                        writer.startElement(HTML.OPTION_ELEM,editor);
                        
                        writer.writeAttribute(HTML.VALUE_ATTR, "PRE",null);
                        writer.writeAttribute(I18N_TRANSLATE_ATTR,"Formatted",null);
                            writer.write("Formatted");
                        writer.endElement(HTML.OPTION_ELEM);

                    writer.endElement(HTML.SELECT_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                        writeButton(writer, "kupu-save", "save: alt-s", "s", editor, null);
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-basicmarkup", null);
                        writeButton(writer, "kupu-bold", "Bold: Access key (Alt or Ctrl) + B", "b", editor,"title title_bold;");
                        writeButton(writer, "kupu-italic", "Italic: Access key (Alt or Ctrl) + I", "i", editor,"title title_italic;");
                        writeButton(writer, "kupu-underline", "Underline: Access key (Alt or Ctrl) + U", "u", editor,"title title_underline;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-subsuper", null);
                        writeButton(writer, "kupu-subscript", "Subscript: Access key (Alt or Ctrl) + -", "-", editor,"title title_subscript;");
                        writeButton(writer, "kupu-superscript", "Superscript: Access key (Alt or Ctrl) + +", "+", editor,"title title_superscript;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-color", null);
                        writeButton(writer, "kupu-forecolor", "text color: alt-f", "f", editor,"title title_text_color;");
                        writeButton(writer, "kupu-hilitecolor", "background color: alt-h", "h", editor,"title title_back_color;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-justify", null);
                        writeButton(writer, "kupu-justifyleft", "Left justify: Access key (Alt or Ctrl) + L", "l", editor,"title title_ljust;");
                        writeButton(writer, "kupu-justifycenter", "Center justify: Access key (Alt or Ctrl) + C", "c", editor,"title title_center;");
                        writeButton(writer, "kupu-justifyright", "Right justify: Access key (Alt or Ctrl) + R", "r", editor,"title title_rjust;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-list", null);
                        writeButton(writer, "kupu-insertorderedlist", "Numbered list: Access key (Alt or Ctrl) + #", "#", "kupu-list-ol-addbutton", editor,"title title_numlist;");
                        writeButton(writer, "kupu-insertunorderedlist", "Unordered list: Access key (Alt or Ctrl) + *", "*", "kupu-list-ul-addbutton", editor,"title title_unorderedlist;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-definitionlist", null);
                        writeButton(writer, "kupu-insertdefinitionlist", "Definition list: Access key (Alt or Ctrl) + =", "=", "kupu-list-dl-addbutton", editor,"title title_deflist;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-indent", null);
                        writeButton(writer, "kupu-outdent", "Decrease quote level: Access key (Alt or Ctrl) + <", "<", editor,"title title_decrease_quote;");
                        writeButton(writer, "kupu-indent", "Increase quote level: Access key (Alt or Ctrl) + >", ">", editor,"title title_increase_quote;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    if( ! editor.isAllowExternalLinks() ){
                        writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                    }
                        writeButton(writer, "kupu-image", "Insert image", null, "kupu-imagelibdrawer-button", false, editor,"title title_insert_image;"); // TODO : Enable
                        writeButton(writer, "kupu-inthyperlink", "Insert internal link", null, "kupu-linklibdrawer-button", false, editor,"title title_insert_internal_link;"); // TODO : Enable
                        writeButton(writer, "kupu-exthyperlink", "Insert external link", null, "kupu-linkdrawer-button", editor,"title title_insert_external_link;");
                        writeButton(writer, "kupu-anchors", "Insert anchors", null, "kupu-anchors", editor,"title title_insert_anchors;");
                        writeButton(writer, "kupu-table", "Insert table", null, "kupu-tabledrawer-button", editor,"title title_insert_table;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-remove", null);
                        writeButton(writer, "kupu-removeimage invisible", "Remove image", null, "kupu-removeimage-button", editor,"title title_removeimage;");
                        writeButton(writer, "kupu-removelink invisible", "Remove link", null, "kupu-removelink-button", editor,"title title_removelink;");
                    writer.endElement(HTML.SPAN_ELEM);

                       writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-undo", null);
                        writeButton(writer, "kupu-undo", "Undo: Access key (Alt or Ctrl) + Z", "z", editor,"title title_undo;");
                        writeButton(writer, "kupu-redo", "Redo: Access key (Alt or Ctrl) + Y", "y", editor,"title title_redo;");
                    writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup kupu-spellchecker-span", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-spellchecker", null);
                    //if( ! editor.isAllowEditSource() ){
                        writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                    //}
                        writeButton(writer, "kupu-spellchecker", "Check spelling", null, editor,"title title_check_spelling;");
                     writer.endElement(HTML.SPAN_ELEM);

                    writer.startElement(HTML.SPAN_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup kupu-source-span", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-source", null);
                    if( ! editor.isAllowEditSource() ){
                        writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                    }
                        writeButton(writer, "kupu-source", "Switch between visual editor and HTML view", "h", editor,"title toggle_source_view;");
                     writer.endElement(HTML.SPAN_ELEM);

                writer.endElement(HTML.SPAN_ELEM); // kupu-tb-buttons

                 writer.startElement(HTML.SELECT_ELEM,editor);
                 writer.writeAttribute(HTML.ID_ATTR, "kupu-ulstyles", null);
                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-ulstyles", null);
                     writeOption(writer, "disc", "list-disc", "&#x25CF;", editor);        // TODO : Check as last parameter has been removed.
                     writeOption(writer, "square", "list-square", "&#x25A0;", editor);
                     writeOption(writer, "circle", "list-circle", "&#x25CB;", editor);
                     writeOption(writer, "none", "list-nobullet", "no bullet", editor);
                 writer.endElement(HTML.SELECT_ELEM);

                 writer.startElement(HTML.SELECT_ELEM,editor);
                 writer.writeAttribute(HTML.ID_ATTR, "kupu-olstyles", null);
                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-olstyles", null);
                     writeOption(writer, "decimal", "list-decimal", "1", editor);
                     writeOption(writer, "upper-roman", "list-upperroman", "I", editor);
                     writeOption(writer, "lower-roman", "list-lowerroman", "i", editor);
                     writeOption(writer, "upper-alpha", "list-upperalpha", "A", editor);
                     writeOption(writer, "lower-alpha", "list-loweralpha", "a", editor);
                 writer.endElement(HTML.SELECT_ELEM);

                 writer.startElement(HTML.DIV_ELEM,editor);
                 writer.writeAttribute(HTML.STYLE_ATTR, "display:block;", null);
                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-librarydrawer-parent", null);

                     writer.startElement(HTML.IFRAME_ELEM,editor);
                     writer.writeAttribute(HTML.STYLE_ATTR,"display:none",null);
                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-anchorframe",null);
                     writer.writeAttribute("src","javascript:''",null);
                     writer.writeAttribute("scrolling","auto",null);
                     writer.writeAttribute("frameborder","1",null);
                     writer.endElement(HTML.IFRAME_ELEM);

                 writer.endElement(HTML.DIV_ELEM);

                // External Link drawer
                 writer.startElement(HTML.DIV_ELEM,editor);
                 writer.writeAttribute(HTML.ID_ATTR, "kupu-linkdrawer", null);
                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-drawer kupu-linkdrawer", null);
                     writer.startElement(HTML.H1_ELEM,editor);
                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-drawer-title",null);
                     
                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"title_external_link", null);
                         writer.write("External Link");
                     writer.endElement(HTML.H1_ELEM);
                     writer.startElement(HTML.DIV_ELEM,editor);
                     writer.writeAttribute(HTML.ID_ATTR, "kupu-linkdrawer-addlink", null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panels kupu-linkdrawer-addlink", null);
                    
                        writer.startElement(HTML.UL_ELEM,editor);
                        writer.writeAttribute(HTML.CLASS_ATTR,"kupu-tabs",null);
    
                            writer.startElement(HTML.LI_ELEM,editor);
                            writer.writeAttribute(HTML.CLASS_ATTR,"kupu-linkdrawer-addlink selected",null);
                                writer.startElement(HTML.ANCHOR_ELEM,editor);
                                writer.writeAttribute("href","#",null);
                                
                                writer.writeAttribute(I18N_TRANSLATE_ATTR,"tab_link",null);
                                writer.writeAttribute("shape","rect",null);
                                writer.write("Link to url");
                                writer.endElement(HTML.ANCHOR_ELEM);
                            writer.endElement(HTML.LI_ELEM);
                            
                            writer.startElement(HTML.LI_ELEM,editor);
                            writer.writeAttribute(HTML.CLASS_ATTR,"kupu-linkdrawer-embed",null);

                                writer.startElement(HTML.ANCHOR_ELEM,editor);
                                writer.writeAttribute("href","#",null);
                                
                                writer.writeAttribute(I18N_TRANSLATE_ATTR,"tab_embed",null);
                                writer.writeAttribute("shape","rect",null);
                                writer.write("Embed external object");
                                writer.endElement(HTML.ANCHOR_ELEM);
                            writer.endElement(HTML.LI_ELEM);
    
                        writer.endElement(HTML.UL_ELEM); 
                    
                         writer.startElement(HTML.TABLE_ELEM,editor);
                         writer.writeAttribute(HTML.CLASS_ATTR,"kupu-tabbed",null);
                           writer.startElement(HTML.TBODY_ELEM,editor);
                           writer.writeAttribute(HTML.CLASS_ATTR,"kupu-addlink",null);
                                                                          
                             writer.startElement(HTML.TR_ELEM,editor);
                                 writer.startElement(HTML.TD_ELEM,editor);
                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                    writer.startElement(HTML.DIV_ELEM,editor);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                        writer.startElement(HTML.SPAN_ELEM,editor);
                                             
                                             writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_link_text",null);
                                             writer.write("Link the highlighted text to this URL:");
                                         writer.endElement(HTML.SPAN_ELEM);
                                    writer.endElement(HTML.DIV_ELEM);
                                    
                                    writer.startElement(HTML.INPUT_ELEM,editor);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-st kupu-linkdrawer-input", null);
                                    writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                                    writer.writeAttribute(HTML.ONKEYPRESS_ATTR, "return HandleDrawerEnter(event, 'linkdrawer-preview');", null);
                                    writer.endElement(HTML.INPUT_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                                writer.startElement(HTML.TD_ELEM,null);
                                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-preview-button", null);
                                writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                    writer.startElement(HTML.BUTTON_ELEM,editor);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                    writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                    writer.writeAttribute(HTML.ID_ATTR,"linkdrawer-preview",null);
                                    writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.preview()", null);
                                    
                                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_preview",null);
                                    writer.write("Preview");
                                    writer.endElement(HTML.BUTTON_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                             writer.endElement(HTML.TR_ELEM);
                            writer.startElement(HTML.TR_ELEM,editor);
                            writer.writeAttribute(HTML.CLASS_ATTR,"kupu-linkdrawer-anchors",null);
                            writer.writeAttribute(HTML.STYLE_ATTR,"display:none",null);

                                writer.startElement(HTML.TD_ELEM,editor);
                                writer.writeAttribute(HTML.COLSPAN_ATTR, "2", null);
                                writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                
                                    writer.startElement(HTML.LABEL_ATTR,editor);
                                    
                                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_anchor",null);
                                    writer.writeAttribute("for","anchorselect",null);
                                    writer.write("Anchor");
                                    writer.endElement(HTML.LABEL_ATTR);
                                    writer.startElement(HTML.SELECT_ELEM,editor);
                                    writer.writeAttribute(HTML.ID_ATTR,"anchorselect",null);
                                        writer.startElement(HTML.OPTION_ELEM,editor);
                                        
                                        writer.writeAttribute(I18N_TRANSLATE_ATTR,"opt_top_of_page",null);
                                        writer.writeAttribute(HTML.VALUE_ATTR,"",null);
                                        writer.write("top of page (default)");
                                        writer.endElement(HTML.OPTION_ELEM);
                                    writer.endElement(HTML.SELECT_ELEM);
                            
                                writer.endElement(HTML.TD_ELEM);
                        
                            writer.endElement(HTML.TR_ELEM);

                             writer.startElement(HTML.TR_ELEM,editor);
                                writer.startElement(HTML.TD_ELEM,editor);
                                writer.writeAttribute(HTML.COLSPAN_ATTR,"2",null);
                                writer.writeAttribute(HTML.ALIGN_ATTR, "center", null);
                                writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                    writer.startElement(HTML.DIV_ELEM,editor);
                                    writer.writeAttribute(HTML.CLASS_ATTR,"watermark",null);
                                        writer.startElement(HTML.SPAN_ELEM,editor);
                                        
                                        writer.writeAttribute(I18N_TRANSLATE_ATTR,"watermark_preview",null);
                                        writer.write("Preview");
                                        writer.endElement(HTML.SPAN_ELEM);
                                    writer.endElement(HTML.DIV_ELEM);
                                    writer.startElement(HTML.IFRAME_ELEM,editor);
                                    writer.writeAttribute(HTML.FRAMEBORDER_ATTR, "1", null);
                                    writer.writeAttribute(HTML.SCROLLING_ATTR, "auto", null);
                                    writer.writeAttribute(HTML.WIDTH_ATTR, "440", null);
                                    writer.writeAttribute(HTML.HEIGHT_ATTR, "198", null);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-linkdrawer-preview", null);
                                    // addResource.getResourceUri(context, InputHtmlRenderer.class, "kupublank.html")
                                    writer.writeAttribute(HTML.SRC_ATTR, "javascript:''", null);
                                    writer.endElement(HTML.IFRAME_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                             writer.endElement(HTML.TR_ELEM);
                           writer.endElement(HTML.TBODY_ELEM);
                           
                           writer.startElement(HTML.TBODY_ELEM,editor);
                           writer.writeAttribute(HTML.CLASS_ATTR,"kupu-embed",null);
                               writer.startElement(HTML.TR_ELEM,editor);
                                   writer.startElement(HTML.TD_ELEM,editor);
                                   writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                   writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                       writer.startElement(HTML.DIV_ELEM,editor);
                                       writer.writeAttribute(HTML.CLASS_ATTR,"kupu-toolbox-label",null);
                                           writer.startElement(HTML.SPAN_ELEM,editor);
                                           
                                           writer.writeAttribute(I18N_TRANSLATE_ATTR,"help_paste_here",null);
                                           writer.write("Paste &lt;object&gt; or &lt;embed&gt; tag here:");
                                           writer.endElement(HTML.SPAN_ELEM);
                                       writer.endElement(HTML.DIV_ELEM);
                                       writer.startElement(HTML.TEXTAREA_ELEM,editor);
                                       writer.writeAttribute(HTML.CLASS_ATTR,"kupu-toolbox-st kupu-embed-input",null);
                                       
                                       writer.writeAttribute(I18N_TRANSLATE_ATTR,"placeholder_html_code",null);
                                       writer.write("[insert arbitrary HTML code here]");
                                       writer.endElement(HTML.TEXTAREA_ELEM);
                                   writer.endElement(HTML.TD_ELEM);
                               writer.endElement(HTML.TR_ELEM);
                               writer.startElement(HTML.TR_ELEM,editor);
                                   writer.startElement(HTML.TD_ELEM,editor);
                                   
                                   writer.writeAttribute(I18N_TRANSLATE_ATTR,"custom_html_help",null);
                                   writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                   writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                   writer.write("You may use this box to insert custom snippets of");
                                   writer.write("HTML code. There is no guarantee that any code");
                                   writer.write("inserted in this way will be preserved when you save");
                                   writer.write("your edits. To select an existing object from IE click on");
                                   writer.write("the border, from Firefox double click it.");
                                   writer.endElement(HTML.TD_ELEM);
                               writer.endElement(HTML.TR_ELEM);
                           writer.endElement(HTML.TBODY_ELEM);
                         writer.endElement(HTML.TABLE_ELEM);

                         writer.startElement(HTML.DIV_ELEM,editor);
                         writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialogbuttons", null);
                             writer.startElement(HTML.BUTTON_ELEM,editor);

                             writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                             writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                             writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.save()", null);
                             
                             writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_ok",null);
                                 writer.write("Ok");
                             writer.endElement(HTML.BUTTON_ELEM);
                             writer.startElement(HTML.BUTTON_ELEM,editor);

                             writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                             writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                             writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.closeDrawer()", null);
                             
                             writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_cancel",null);
                                 writer.write("Cancel");
                             writer.endElement(HTML.BUTTON_ELEM);
                         writer.endElement(HTML.DIV_ELEM);
                     writer.endElement(HTML.DIV_ELEM);
                 writer.endElement(HTML.DIV_ELEM);

                // Table drawer
                 writer.startElement(HTML.DIV_ELEM,editor);
                 writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer",null);
                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-drawer kupu-tabledrawer",null);
                     writer.startElement(HTML.H1_ELEM,editor);
                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-drawer-title",null);
                     
                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_title",null);
                         writer.write("Table");
                    writer.endElement(HTML.H1_ELEM);
                    writer.startElement(HTML.DIV_ELEM,editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panels", null);
                         writer.startElement(HTML.TABLE_ELEM,editor);
                        writer.writeAttribute(HTML.WIDTH_ATTR, "300",null);
                            writer.startElement(HTML.TR_ELEM,editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panelsrow",null);
                                writer.startElement(HTML.TD_ELEM,editor);
                                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panel",null);
                                writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                writer.writeAttribute(ROWSPAN_ATTR,"1",null);

                                    writer.startElement(HTML.DIV_ELEM,editor);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-addtable", null);
                                        writer.startElement(HTML.TABLE_ELEM,editor);
                                             writer.startElement(HTML.TR_ELEM,editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_class_label",null);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.write("Table Class");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM,editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.SELECT_ELEM,editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-addclasschooser", null);
                                                         writeOption(writer, "plain", "Plain", "Plain", editor);
                                                         writeOption(writer, "listing","Listing" , "Listing", editor);
                                                         writeOption(writer, "grid", "Grid", "Grid", editor);
                                                         writeOption(writer, "data", "Data", "Data", editor);
                                                     writer.endElement(HTML.SELECT_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM, editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_rows_label",null);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);                                                 
                                                     writer.write("Rows");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM, editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.INPUT_ELEM, editor);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-newrows", null);
                                                     writer.writeAttribute(HTML.VALUE_ATTR, "3", null); // MyFaces special
                                                     writer.writeAttribute(HTML.ONKEYPRESS_ATTR, "return HandleDrawerEnter(event);", null);
                                                     writer.endElement(HTML.INPUT_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM, editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_columns_label",null);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);                                                 
                                                     writer.write("Columns");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM, editor);
                                                     writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                     writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.INPUT_ELEM, editor);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-newcols", null);
                                                     writer.writeAttribute(HTML.VALUE_ATTR, "3", null); // MyFaces special
                                                     writer.writeAttribute(HTML.ONKEYPRESS_ATTR, "return HandleDrawerEnter(event);", null);
                                                     writer.endElement(HTML.INPUT_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM, editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM, editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.INPUT_ELEM,editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-tabledrawer-makeheader",null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR,"checkbox",null);
                                                     writer.writeAttribute("checked","checked",null);
                                                     writer.writeAttribute(HTML.ONKEYPRESS_ATTR,"return HandleDrawerEnter(event);",null);
                                                     writer.writeAttribute(HTML.ID_ATTR,"tabledrawer_makeheader",null);
                                                     writer.endElement(HTML.INPUT_ELEM);
                                                     writer.startElement(HTML.LABEL_ATTR,editor);
                                                     
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_headings_label",null);
                                                     writer.writeAttribute("for","tabledrawer_makeheader",null);
                                                         writer.write("Create Headings");
                                                     writer.endElement(HTML.LABEL_ATTR);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM, editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM, editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-dialog-button",null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_add_table_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.createTable()", null);
                                                         writer.write("Add Table");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_fix_tables_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.fixAllTables()", null);
                                                        writer.write("Fix All Tables");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                         writer.endElement(HTML.TABLE_ELEM);
                                     writer.endElement(HTML.DIV_ELEM);

                                     writer.startElement(HTML.DIV_ELEM,editor);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-edittable", null);
                                        writer.startElement(HTML.TABLE_ELEM,editor);
                                             writer.startElement(HTML.TR_ELEM,editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_class_label",null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.write("Table Class");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM,editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.SELECT_ELEM,editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-editclasschooser", null);
                                                     writer.writeAttribute(HTML.ONCHANGE_ATTR, "drawertool.current_drawer.setTableClass(this.options[this.selectedIndex].value)", null);
                                                         writeOption(writer, "plain", "Plain", "Plain", editor);
                                                         writeOption(writer, "listing", "Listing", "Listing", editor);
                                                         writeOption(writer, "grid", "Grid", "Grid", editor);
                                                         writeOption(writer, "data", "Data", "Data", editor);
                                                     writer.endElement(HTML.SELECT_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM,editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_alignment_label",null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.write("Current column alignment");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM,editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.SELECT_ELEM,editor);
                                                     writer.writeAttribute(HTML.ID_ATTR,"kupu-tabledrawer-alignchooser",null);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tabledrawer-alignchooser", null);
                                                     writer.writeAttribute(HTML.ONCHANGE_ATTR, "drawertool.current_drawer.tool.setColumnAlign(this.options[this.selectedIndex].value)", null);
                                                     writeOption(writer, "left", "tabledrawer_left_option", "Left", editor);
                                                     writeOption(writer, "center", "tabledrawer_center_option", "Center", editor);
                                                     writeOption(writer, "right", "tabledrawer_right_option", "Right", editor);
                                                     writer.endElement(HTML.SELECT_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM,editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_column_label",null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.write("Column");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM,editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_add_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.addTableColumn()", null);
                                                         writer.write("Add");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_remove_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.delTableColumn()", null);
                                                         writer.write("Remove");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM,editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_row_label",null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.write("Row");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM,editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_add_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.addTableRow()", null);
                                                         writer.write("Add");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_remove_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.delTableRow()", null);
                                                         writer.write("Remove");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                             writer.startElement(HTML.TR_ELEM,editor);
                                                 writer.startElement(HTML.TH_ELEM,editor);
                                                 writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_table_label",null);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.write("Fix Table");
                                                 writer.endElement(HTML.TH_ELEM);
                                                 writer.startElement(HTML.TD_ELEM,editor);
                                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                                     writer.startElement(HTML.BUTTON_ELEM, editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_fix_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.fixTable()", null);
                                                         writer.write("Fix");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                     writer.startElement(HTML.BUTTON_ELEM,editor);
                                                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-dialog-button",null);
                                                     writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
                                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_remove_button",null);
                                                     writer.writeAttribute(HTML.ONCLICK_ATTR,"drawertool.current_drawer.delTable()",null);
                                                     writer.write("Remove");
                                                     writer.endElement(HTML.BUTTON_ELEM);
                                                 writer.endElement(HTML.TD_ELEM);
                                             writer.endElement(HTML.TR_ELEM);

                                         writer.endElement(HTML.TABLE_ELEM);
                                     writer.endElement(HTML.DIV_ELEM); // kupu-tabledrawer-edittable
                                writer.endElement(HTML.TD_ELEM);
                            writer.endElement(HTML.TR_ELEM);
                        writer.endElement(HTML.TABLE_ELEM);

                        writer.startElement(HTML.DIV_ELEM,editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialogbuttons", null);
                            writer.startElement(HTML.BUTTON_ELEM,editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialog-button", null);
                            writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"tabledrawer_close_button",null);
                            writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.closeDrawer(this)", null);
                                writer.write("Close");
                            writer.endElement(HTML.BUTTON_ELEM);
                        writer.endElement(HTML.DIV_ELEM);
                    writer.endElement(HTML.DIV_ELEM);
                 writer.endElement(HTML.DIV_ELEM); // kupu-tabledrawer
                 
                 writer.startElement(HTML.DIV_ELEM,editor); //kupu-anchordrawer
                 writer.writeAttribute(HTML.ID_ATTR,"kupu-anchordrawer",null);
                 writer.writeAttribute(HTML.CLASS_ATTR,"kupu-drawer kupu-anchordrawer",null);

                     writer.startElement(HTML.H1_ELEM,editor);
                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-drawer-title",null);
                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"anchordrawer_title",null);
                     writer.write("Anchors");
                     writer.endElement(HTML.H1_ELEM);

                     writer.startElement(HTML.DIV_ELEM,editor);
                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-panels kupu-ins-bm",null);
                     
                         writer.startElement(HTML.UL_ELEM,editor);
                         writer.writeAttribute(HTML.CLASS_ATTR,"kupu-tabs",null);
                         
                             writer.startElement(HTML.LI_ELEM,editor);
                             writer.writeAttribute(HTML.CLASS_ATTR,"kupu-ins-bm selected",null);
                                 writer.startElement(HTML.ANCHOR_ELEM,editor);
                                 writer.writeAttribute("href","#",null);
                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tab_link_anchor",null);
                                 writer.writeAttribute("shape","rect",null);
                                 writer.write("Link to anchor");
                                 writer.endElement(HTML.ANCHOR_ELEM);
                             writer.endElement(HTML.LI_ELEM);
                         
                             writer.startElement(HTML.LI_ELEM,editor);
                             writer.writeAttribute(HTML.CLASS_ATTR,"kupu-anchor",null);
                                 writer.startElement(HTML.ANCHOR_ELEM,editor);
                                 writer.writeAttribute("href","#",null);
                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tab_manage_anchors",null);
                                 writer.writeAttribute("shape","rect",null);
                                 writer.write("Manage Anchors");
                                 writer.endElement(HTML.ANCHOR_ELEM);
                             writer.endElement(HTML.LI_ELEM);
                         
                             writer.startElement(HTML.LI_ELEM,editor);
                             writer.writeAttribute(HTML.CLASS_ATTR,"kupu-toc",null);
                                 writer.startElement(HTML.ANCHOR_ELEM,editor);
                                 writer.writeAttribute("href","#",null);
                                 writer.writeAttribute(I18N_TRANSLATE_ATTR,"tab_toc",null);
                                 writer.writeAttribute("shape","rect",null);
                                 writer.write("Table of contents");
                                 writer.endElement(HTML.ANCHOR_ELEM);
                             writer.endElement(HTML.LI_ELEM);
                         
                         writer.endElement(HTML.UL_ELEM);
                     
                         writer.startElement(HTML.TABLE_ELEM,editor);
                         writer.writeAttribute(HTML.CLASS_ATTR,"kupu-tabbed",null);
                         writer.writeAttribute("cellspacing","0",null);
                         
                             writer.startElement(HTML.TR_ELEM,editor);
                             
                                 writer.startElement(HTML.TD_ELEM,editor);
                                 writer.writeAttribute(HTML.CLASS_ATTR,"kupu-bm-select",null);
                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                 
                                     writer.startElement(HTML.SELECT_ELEM,editor);
                                     writer.writeAttribute(HTML.ID_ATTR,"kupu-bm-sel1",null);
                                     writer.writeAttribute("size","10",null);
                                     
                                     writer.endElement(HTML.SELECT_ELEM);
                                 
                                 writer.endElement(HTML.TD_ELEM);
                             
                                 writer.startElement(HTML.TD_ELEM,editor);
                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                 
                                 writer.startElement(HTML.DIV_ELEM,editor);
                                 writer.writeAttribute(HTML.CLASS_ATTR,"kupu-bm-heading",null);
                                 
                                     writer.startElement(HTML.DIV_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-bm-tablestyle invis-ins-bm invis-anchor",null);
                                     
                                         writer.startElement(HTML.LABEL_ATTR,editor);
                                         writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_table_style",null);
                                         writer.writeAttribute("for","kupu-bm-outcls",null);
                                         writer.write("Table style");
                                         
                                         writer.endElement(HTML.LABEL_ATTR);
                                         
                                         writer.startElement(HTML.SELECT_ELEM,editor);
                                         writer.writeAttribute(HTML.ID_ATTR,"kupu-bm-outcls",null);
                                         writer.writeAttribute("size","1",null);
                                         
                                         writer.startElement(HTML.OPTION_ELEM,editor);
                                         writer.writeAttribute(HTML.VALUE_ATTR,"",null);
                                         writer.writeAttribute(I18N_TRANSLATE_ATTR,"Normal",null);
                                         writer.write("Normal paragraph");
                                         writer.endElement(HTML.OPTION_ELEM);
                                         
                                         writer.endElement(HTML.SELECT_ELEM);
                                         
                                     writer.endElement(HTML.DIV_ELEM);
                                 
                                     writer.startElement(HTML.DIV_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-bm-numbering invis-ins-bm invis-anchor",null);
                                     
                                         writer.startElement(HTML.LABEL_ATTR,editor);
                                         writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_numbering",null);
                                         writer.writeAttribute("for","kupu-bm-number",null);
                                         writer.write("Numbering");
                                         
                                         writer.endElement(HTML.LABEL_ATTR);
                                     
                                         writer.startElement(HTML.INPUT_ELEM,editor);
                                         writer.writeAttribute(HTML.TYPE_ATTR,"checkbox",null);
                                         writer.writeAttribute(HTML.CLASS_ATTR,"hide-ins-bm hide-anchor",null);
                                         writer.writeAttribute("checked","checked",null);
                                         writer.writeAttribute(HTML.ID_ATTR,"kupu-bm-number",null);
                                         writer.endElement(HTML.INPUT_ELEM);
                                     
                                     writer.endElement(HTML.DIV_ELEM);
                                 
                                     writer.startElement(HTML.DIV_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"invis-ins-bm",null);
                                     
                                         writer.startElement(HTML.LABEL_ATTR,editor);
                                         writer.writeAttribute("for","kupu-bm-checkall",null);
                                         
                                         writer.startElement("em",editor);
                                         writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_toggle_all",null);
                                         writer.write("toggle all");
                                         writer.endElement("em");
                                         
                                         writer.endElement(HTML.LABEL_ATTR);
                                         
                                         writer.startElement(HTML.INPUT_ELEM,editor);
                                         writer.writeAttribute(HTML.TYPE_ATTR,"checkbox",null);
                                         writer.writeAttribute(HTML.CLASS_ATTR,"hide-ins-bm",null);
                                         writer.writeAttribute("checked","checked",null);
                                         writer.writeAttribute(HTML.ID_ATTR,"kupu-bm-checkall",null);
                                         writer.endElement(HTML.INPUT_ELEM);
                                     
                                     writer.endElement(HTML.DIV_ELEM);
                                     
                                 writer.endElement(HTML.DIV_ELEM);
                                 
                                 writer.startElement(HTML.DIV_ELEM,editor);
                                 writer.writeAttribute(HTML.CLASS_ATTR,"kupu-bm-paras",null);
                                 writer.write("&#160;");
                                 writer.endElement(HTML.DIV_ELEM);
                                 
                                 writer.endElement(HTML.TD_ELEM);
                                 
                             writer.endElement(HTML.TR_ELEM);
                             
                             writer.startElement(HTML.TR_ELEM,editor);
                             
                                 writer.startElement(HTML.TD_ELEM,editor);
                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                 
                                     writer.startElement(HTML.SELECT_ELEM,editor);
                                     writer.writeAttribute(HTML.ID_ATTR,"kupu-bm-sel2",null);
                                     writer.writeAttribute("size","1",null);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"hide-ins-bm hide-anchor",null);
                                     
                                         writer.startElement(HTML.OPTION_ELEM,editor);
                                         writer.writeAttribute(HTML.VALUE_ATTR,"",null);
                                         writer.writeAttribute(I18N_TRANSLATE_ATTR,"opt_2nd_style",null);
                                         writer.write("(optional 2nd style)");
                                         writer.endElement(HTML.OPTION_ELEM);
                                     
                                     writer.endElement(HTML.SELECT_ELEM);
                                 
                                 writer.endElement(HTML.TD_ELEM);
                             
                                 writer.startElement(HTML.TD_ELEM,editor);
                                 writer.writeAttribute(HTML.COLSPAN_ATTR,"1",null);
                                 writer.writeAttribute(ROWSPAN_ATTR,"1",null);
                                 
                                     writer.startElement(HTML.DIV_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"kupu-bm-help",null);
                                     
                                     writer.startElement(HTML.SPAN_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"hide-anchor hide-toc discreet",null);
                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"create_anchor_link_help",null);
                                     writer.write("Select a paragraph style then choose a paragraph and Ok to insert a link to that location.");
                                     
                                     writer.endElement(HTML.SPAN_ELEM);
                                     
                                     writer.startElement(HTML.SPAN_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"hide-ins-bm hide-anchor discreet",null);
                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"create_toc_help",null);
                                     writer.write("Select one or two paragraph styles, then Ok to insert a table of contents.");
                                     
                                     writer.endElement(HTML.SPAN_ELEM);
                                     
                                     writer.startElement(HTML.SPAN_ELEM,editor);
                                     writer.writeAttribute(HTML.CLASS_ATTR,"hide-ins-bm hide-toc discreet",null);
                                     writer.writeAttribute(I18N_TRANSLATE_ATTR,"create_anchor_help",null);
                                     writer.write("Anchors are created for checked paragraphs and");
                                     writer.write("removed for unchecked paragraphs when you press Ok.");
                                     writer.write("Anchors in use on this page may not be deleted.");
                                     
                                     writer.endElement(HTML.SPAN_ELEM);
                                     
                                     writer.endElement(HTML.DIV_ELEM);
                                 
                                 writer.endElement(HTML.TD_ELEM);
                             
                             writer.endElement(HTML.TR_ELEM);
                             
                         writer.endElement(HTML.TABLE_ELEM);
                     
                         writer.startElement(HTML.DIV_ELEM,editor);
                         writer.writeAttribute(HTML.CLASS_ATTR,"kupu-dialogbuttons",null);
                         
                             writer.startElement(HTML.BUTTON_ELEM,editor);
                             writer.writeAttribute(HTML.CLASS_ATTR,"kupu-dialog-button",null);
                             writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
                             writer.writeAttribute(HTML.ONCLICK_ATTR,"drawertool.current_drawer.save()",null);
                             writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_ok",null);
                             writer.write("Ok");
                             writer.endElement(HTML.BUTTON_ELEM);
                         
                             writer.startElement(HTML.BUTTON_ELEM,editor);
                             writer.writeAttribute(HTML.CLASS_ATTR,"kupu-dialog-button",null);
                             writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
                             writer.writeAttribute(HTML.ONCLICK_ATTR,"drawertool.closeDrawer()",null);
                             writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_cancel",null);
                             writer.write("Cancel");
                             writer.endElement(HTML.BUTTON_ELEM);
                         
                         writer.endElement(HTML.DIV_ELEM);
                     writer.endElement(HTML.DIV_ELEM);
                 writer.endElement(HTML.DIV_ELEM);
            writer.endElement(HTML.DIV_ELEM); // toolbar

            //
            // Tool Boxes
            //
            writer.startElement(HTML.DIV_ELEM, editor);
            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolboxes", null);
            writer.writeAttribute(HTML.ID_ATTR, "kupu-toolboxes", null);
            if( ! editor.isShowAnyToolBox() ){
                writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            }

                // Properties tool box
                writer.startElement(HTML.DIV_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-properties", null);
                if( ! editor.isShowPropertiesToolBox() ){
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                }
                    writer.startElement(HTML.H1_ELEM, editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-heading", null);
                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"title_properties",null);
                        writer.write("Properties");
                    writer.endElement(HTML.H1_ELEM);

                    writer.startElement(HTML.DIV_ELEM, editor);
                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                        writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_title",null);
                            writer.write("Title:");
                        writer.endElement(HTML.DIV_ELEM);
                        writer.startElement(HTML.INPUT_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-properties-title", null);
                        writer.writeAttribute(HTML.TYPE_ATTR,"text",null);
                        writer.endElement(HTML.INPUT_ELEM);
                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                        writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_description",null);
                            writer.write("Description:");
                        writer.endElement(HTML.DIV_ELEM);
                        writer.startElement(HTML.TEXTAREA_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                            writer.writeAttribute(HTML.ID_ATTR, "kupu-properties-description", null);
                        writer.endElement(HTML.TEXTAREA_ELEM);
                    writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.DIV_ELEM);

                // Links tool box
                writer.startElement(HTML.DIV_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-links", null);
                if( ! editor.isShowLinksToolBox() ){
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                }
                    writer.startElement(HTML.H1_ELEM, editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-heading", null);
                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_links",null);
                        writer.write("Links");
                    writer.endElement(HTML.H1_ELEM);

                    writer.startElement(HTML.DIV_ELEM, editor);
                    writer.writeAttribute(HTML.ID_ATTR,"kupu-toolbox-addlink",null);

                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.startElement(HTML.SPAN_ELEM, editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_link_text",null);
                                writer.write("Link the highlighted text to this URL:");
                            writer.endElement(HTML.SPAN_ELEM);
                        writer.endElement(HTML.DIV_ELEM);

                        writer.startElement(HTML.INPUT_ELEM, editor);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-link-input", null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                        writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                        writer.endElement(HTML.INPUT_ELEM);

                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
                            writer.startElement(HTML.BUTTON_ELEM, editor);
                            writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                            writer.writeAttribute(HTML.ID_ATTR, "kupu-link-button", null);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-action", null);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_makelink",null);
                                writer.write("Make Link");
                            writer.endElement(HTML.BUTTON_ELEM);
                        writer.endElement(HTML.DIV_ELEM);

                    writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.DIV_ELEM);

                // Images tool box
                writer.startElement(HTML.DIV_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-images", null);
                if( ! editor.isShowImagesToolBox() ){
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                }
                    writer.startElement(HTML.H1_ELEM, editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-heading", null);
                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"title_images",null);
                        writer.write("Images");
                    writer.endElement(HTML.H1_ELEM);

                    writer.startElement(HTML.DIV_ELEM, editor);
                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.startElement(HTML.SPAN_ELEM, editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_image_class",null);
                                writer.write("Image class:");
                            writer.endElement(HTML.SPAN_ELEM);
                        writer.endElement(HTML.DIV_ELEM);

                        writer.startElement(HTML.SELECT_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-image-float-select", null);
                            writeOption(writer, "image-inline", "opt_inline", "Inline", editor);
                            writeOption(writer, "image-left", "opt_left", "Left", editor);
                            writeOption(writer, "image-right", "opt_right", "Right", editor);
                        writer.endElement(HTML.SELECT_ELEM);

                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.startElement(HTML.SPAN_ELEM, editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_insert_image_url",null);
                                writer.write("Insert image at the following URL:");
                            writer.endElement(HTML.SPAN_ELEM);
                        writer.endElement(HTML.DIV_ELEM);
                        writer.startElement(HTML.INPUT_ELEM, editor);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-image-input", null);
                        writer.writeAttribute(HTML.VALUE_ATTR, "kupuimages/kupu_icon.gif", null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                        writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                        writer.endElement(HTML.INPUT_ELEM);

                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
                            writer.startElement(HTML.BUTTON_ELEM, editor);
                            writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                            writer.writeAttribute(HTML.ID_ATTR, "kupu-image-addbutton", null);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-action", null);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"button_insert_image",null);
                                writer.write("Insert Image");
                            writer.endElement(HTML.BUTTON_ELEM);
                        writer.endElement(HTML.DIV_ELEM);

                    writer.endElement(HTML.DIV_ELEM);

                writer.endElement(HTML.DIV_ELEM);

                // Tables tool box
                writer.startElement(HTML.DIV_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-tables", null);
                if( ! editor.isShowTablesToolBox() ){
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                }
                    writer.startElement(HTML.H1_ELEM, editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-heading", null);
                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"title_tables",null);
                        writer.write("Tables");
                    writer.endElement(HTML.H1_ELEM);

                    writer.startElement(HTML.DIV_ELEM, editor);

                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.startElement(HTML.SPAN_ELEM, editor);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_table_class",null);
                                writer.write("Table Class:");
                            writer.endElement(HTML.SPAN_ELEM);
                            writer.startElement(HTML.SELECT_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                            writer.writeAttribute(HTML.ID_ATTR, "kupu-table-classchooser", null);
                                writer.write(' ');
                            writer.endElement(HTML.SELECT_ELEM);
                        writer.endElement(HTML.DIV_ELEM);

                        // Add table
                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-addtable", null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-addtable", null);
                            writer.startElement(HTML.DIV_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_rows",null);
                                writer.write("Rows:");
                            writer.endElement(HTML.DIV_ELEM);
                            writer.startElement(HTML.INPUT_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                            writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                            writer.writeAttribute(HTML.ID_ATTR, "kupu-table-newrows", null);
                            writer.endElement(HTML.INPUT_ELEM);

                            writer.startElement(HTML.DIV_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_columns",null);
                                writer.write("Columns:");
                            writer.endElement(HTML.DIV_ELEM);
                            
                            writer.startElement(HTML.INPUT_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                            writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                            writer.writeAttribute(HTML.ID_ATTR, "kupu-table-newcols", null);
                            writer.endElement(HTML.INPUT_ELEM);

                            writer.startElement(HTML.DIV_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                writer.startElement(HTML.SPAN_ELEM,editor);
                                writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_headings",null);
                                    writer.write("Headings:");
                                writer.endElement(HTML.SPAN_ELEM);
                                writer.startElement(HTML.INPUT_ELEM, editor);
                                writer.writeAttribute(HTML.NAME_ATTR, "kupu-table-makeheader", null);
                                writer.writeAttribute(HTML.ID_ATTR, "kupu-table-makeheader", null);
                                writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
                                writer.endElement(HTML.INPUT_ELEM);
                                writer.startElement(HTML.LABEL_ELEM, editor);
                                writer.writeAttribute(HTML.FOR_ATTR, "kupu-table-makeheader", null);
                                writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_create",null);
                                    writer.write("Create");
                                writer.endElement(HTML.LABEL_ELEM);
                            writer.endElement(HTML.DIV_ELEM);

                            writer.startElement(HTML.DIV_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
                                writeButton(writer, "kupu-table-fixall-button", "Fix Table", editor,"button_fix_table");
                                writeButton(writer, "kupu-table-addtable-button", "Add Table", editor,"button_add_table");
                            writer.endElement(HTML.DIV_ELEM);
                        writer.endElement(HTML.DIV_ELEM); // Add table

                        // Edit table
                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-edittable", null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-edittable", null);

                            writer.startElement(HTML.DIV_ELEM, editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                writer.startElement(HTML.SPAN_ELEM,editor);
                                writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_col_align",null);
                                    writer.write("Col Align:");
                                writer.endElement(HTML.SPAN_ELEM);

                                writer.startElement(HTML.SELECT_ELEM, editor);
                                writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
                                writer.writeAttribute(HTML.ID_ATTR, "kupu-table-alignchooser", null);
                                    writeOption(writer, "left","tabledrawer_left_option", "Left", editor);
                                    writeOption(writer, "center","tabledrawer_center_option", "Center", editor);
                                    writeOption(writer, "right","tabledrawer_right_option", "Right", editor);
                                writer.endElement(HTML.SELECT_ELEM);
                            writer.endElement(HTML.DIV_ELEM);

                            writer.startElement(HTML.DIV_ELEM,editor);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
                                writer.startElement(HTML.BR_ELEM,editor);
                                writer.writeAttribute("clear","none",null);
                                writer.endElement(HTML.BR_ELEM);

                                writeButton(writer, "kupu-table-addcolumn-button", "Add Column", editor,"button_add_column");
                                writeButton(writer, "kupu-table-delcolumn-button", "Remove Column", editor,"button_remove_column");

                                writer.startElement(HTML.BR_ELEM,editor);
                                writer.writeAttribute("clear","none",null);
                                writer.endElement(HTML.BR_ELEM);

                                writeButton(writer, "kupu-table-addrow-button", "Add Row", editor,"button_add_row");
                                writeButton(writer, "kupu-table-delrow-button", "Remove Row", editor,"button_remove_row");
                                writeButton(writer, "kupu-table-fix-button", "Fix Table", editor,"button_fix_table");
                                writeButton(writer, "kupu-table-del-button", "Remove Table", editor,"button_remove_table");
                            writer.endElement(HTML.DIV_ELEM);

                        writer.endElement(HTML.DIV_ELEM); // Edit table

                    writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.DIV_ELEM);

                // Cleanup expressions tool box
                writer.startElement(HTML.DIV_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-cleanupexpressions", null);
                if( ! editor.isShowCleanupExpressionsToolBox() ){
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                }
                    writer.startElement(HTML.H1_ELEM, editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-heading", null);
                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"title_cleanup_expressions",null);
                        writer.write("Cleanup expressions");
                    writer.endElement(HTML.H1_ELEM);

                    writer.startElement(HTML.DIV_ELEM, editor);
                        writer.startElement(HTML.DIV_ELEM, editor);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                            writer.startElement(HTML.SPAN_ELEM,editor);
                            
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"label_select_cleanup",null);
                                writer.write("Select a cleanup action:");
                            writer.endElement(HTML.SPAN_ELEM);
                        writer.endElement(HTML.DIV_ELEM);
                        writer.startElement(HTML.SELECT_ELEM,editor);
                        writer.writeAttribute(HTML.ID_ATTR, "kupucleanupexpressionselect", null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-st", null);
                        writer.endElement(HTML.SELECT_ELEM);
                        writer.startElement(HTML.DIV_ELEM,editor);
                        writer.writeAttribute(HTML.STYLE_ATTR,"text-align: center",null);
                            writer.startElement(HTML.BUTTON_ELEM,editor);
                            writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
                            writer.writeAttribute(HTML.ID_ATTR,"kupucleanupexpressionbutton",null);
                            writer.writeAttribute(HTML.CLASS_ATTR,"kupu-toolbox-action",null);
                            writer.writeAttribute(I18N_TRANSLATE_ATTR,"perform_action",null);
                                writer.write("Perform action");
                            writer.endElement(HTML.BUTTON_ELEM);
                        writer.endElement(HTML.DIV_ELEM);
                    writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.DIV_ELEM);

                // Debug tool box
                writer.startElement(HTML.DIV_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-debug", null);
                if( ! editor.isShowDebugToolBox() ){
                    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                }
                    writer.startElement(HTML.H1_ELEM, editor);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-heading", null);
                    writer.writeAttribute(I18N_TRANSLATE_ATTR,"title_debug_log",null);
                        writer.write("Debug Log");
                    writer.endElement(HTML.H1_ELEM);
                    writer.startElement(HTML.DIV_ELEM, editor);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-debuglog", null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                    writer.endElement(HTML.DIV_ELEM);
                writer.endElement(HTML.DIV_ELEM);

            writer.endElement(HTML.DIV_ELEM); // kupu-toolboxes

            // Color Chooser
            writer.startElement(HTML.TABLE_ELEM, editor);
            writer.writeAttribute(HTML.ID_ATTR, "kupu-colorchooser", null);
            writer.writeAttribute(HTML.CLASS_ATTR,"kupu-colorchooser",null);
            writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
            writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
            writer.writeAttribute(HTML.STYLE_ATTR, "position: fixed; border-style: solid; border-color: black; border-width: 1px;", null);
            writer.endElement(HTML.TABLE_ELEM);

            // Edit space
            writer.startElement(HTML.DIV_ELEM, editor);
            if( !editor.isShowAnyToolBox() ){
                writer.writeAttribute(HTML.STYLE_ATTR, "margin-right: 0.3em", null);
            }
            if( editor.getStyle()!=null ){
                // Convert the style into an style declaration so that it doesn't preempt the Zoom works as it's relying on changing the class
                addResource.addInlineStyleAtPosition(
                        context, AddResource.HEADER_BEGIN,
                        "#kupu-editor{height: inherit;}\n"+
                        "div.kupu-fulleditor{"+editor.getStyle()+"}");
            }
            writer.writeAttribute(HTML.CLASS_ATTR,
                                  "kupu-editorframe"+(editor.getStyleClass()==null ? "" : " "+editor.getStyleClass()), null);
            // Warning, this is special to MyFaces, to allow to have the component in a hidden tab.
                writer.startElement(HTML.IFRAME_ELEM, editor);
                writer.writeAttribute(HTML.ID_ATTR, getIFrameID(editor, context), null);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-editor-iframe", null);
                writer.writeAttribute(HTML.FRAMEBORDER_ATTR, "0", null);
                writer.writeAttribute(HTML.SCROLLING_ATTR, "auto", null);
                writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, "myFacesKupuReactivateDesignMode(this)", null);
                writer.writeAttribute(HTML.ONFOCUS_ATTR, "myFacesKupuReactivateDesignMode(this)", null);

                writer.endElement(HTML.IFRAME_ELEM);

                writer.startElement(HTML.TEXTAREA_ELEM, editor);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-editor-textarea", null);
                writer.writeAttribute(HTML.ID_ATTR, "kupu-editor-textarea", null);
                    writer.write(' ');
                writer.endElement(HTML.TEXTAREA_ELEM);
            writer.endElement(HTML.DIV_ELEM);

        writer.endElement(HTML.DIV_ELEM); // kupu-fulleditor

        String text = editor.getValueAsHtmlDocument( context );
        String encodedText = text == null ? "" : JavascriptUtils.encodeString( text );

        String resourceBaseURL = addResource.getResourceUri(context, InputHtmlRenderer.class, null);

        writer.startElement(HTML.SCRIPT_ELEM, editor);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            writer.write("myFacesKupuSet(" +
                         "\""+encodedText+"\"," +
                         "\""+clientId+"\"," +
                         "\""+formId+"\"," +
                         "\""+resourceBaseURL+"\"" +
                         ");");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    static private String getIFrameID(InputHtml editor, FacesContext context){
        return editor.getClientId(context)+"_iframe";
    }

    static private void writeTag(ResponseWriter writer, String tagName, String tagBody, UIComponent component) throws IOException{
        writer.startElement(tagName, component);
        writer.writeText(tagBody, null);
        writer.endElement(tagName);
    }

    static private void writeButton(ResponseWriter writer, String classAttr, String title, String accessKey, UIComponent component, String i18nattributes) throws IOException{
        writeButton(writer, classAttr, title, accessKey, classAttr+"-button", component, i18nattributes);
    }

    static private void writeButton(ResponseWriter writer, String classAttr, String title, String accessKey, String id, UIComponent component, String i18nattributes) throws IOException{
        writeButton(writer, classAttr, title, accessKey, id, true, component,i18nattributes);
    }

    static private void writeButton(ResponseWriter writer, String classAttr, String title, String accessKey, String id, boolean display, UIComponent component, String i18nattributes) throws IOException{
        writer.startElement(HTML.BUTTON_ELEM, component);
        
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.CLASS_ATTR, classAttr, null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.TITLE_ATTR, title, null);
        writer.writeAttribute(I18N_ATTRIBUTES_ATTR, i18nattributes==null?"title":i18nattributes, null);
        if( ! display )
            writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
        if( accessKey != null ){
            writer.writeAttribute(HTML.ACCESSKEY_ATTR, accessKey, null);
        }
        writer.write("&#160;");
        writer.endElement(HTML.BUTTON_ELEM);
    }

    static private void writeButton(ResponseWriter writer, String id, String text, UIComponent component, String i18nattributes) throws IOException{
        writer.startElement(HTML.BUTTON_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(I18N_ATTRIBUTES_ATTR, i18nattributes, null);
            writer.write(text);
        writer.endElement(HTML.BUTTON_ELEM);
    }

    static private void writeOption(ResponseWriter writer, String value, String translate, String body, UIComponent component) throws IOException{
        writer.startElement(HTML.OPTION_ELEM,component);
        
        writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        writer.writeAttribute(I18N_TRANSLATE_ATTR, translate, null);
            writer.write(body);
        writer.endElement(HTML.OPTION_ELEM);
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        RendererUtils.checkParamValidity(facesContext, uiComponent, InputHtml.class);
        InputHtml editor = (InputHtml) uiComponent;

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);

        if (paramMap.containsKey(clientId)) {
            //request parameter found, set submittedValue
            String submitedText = (String)paramMap.get(clientId);
            String htmlText = useFallback(editor) ? HTMLEncoder.encode(submitedText, true, true) : submitedText;

            editor.setSubmittedValue( htmlText );
        } else {
            log.warn(HtmlRendererUtils.NON_SUBMITTED_VALUE_WARNING + " Component : "+
                     RendererUtils.getPathToComponent( editor ));
        }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, InputHtml.class);
        InputHtml editor = (InputHtml) uiComponent;
        String submittedDocument = editor.getValueFromDocument((String)submittedValue);
        return RendererUtils.getConvertedUIOutputValue(facesContext, editor, submittedDocument);
    }
}

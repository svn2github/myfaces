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
package org.apache.myfaces.custom.inputHtml;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.component.html.util.AddResource;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.util.HTMLEncoder;
import org.apache.myfaces.renderkit.html.util.JavascriptUtils;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputHtmlRenderer extends HtmlRenderer {
    // TODO : Finish Disabled mode.
    // TODO : Automatic Fallback for non kupu capable browsers (Safari, smartphones, non javascript, ...).
    // TODO : Make Image & Link Library work.
	// TODO : Allow disabeling of tag filtering

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
		else
			encodeEndNormalMode(context, editor);
    }

	private void encodeDisplayValueOnly(FacesContext context, InputHtml editor) throws IOException {
		// Use only a textarea
		ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.SPAN_ELEM, editor);
        HtmlRendererUtils.writeIdIfNecessary(writer, editor, context);

        HtmlRendererUtils.renderDisplayValueOnlyAttributes(editor, writer);

        String text = RendererUtils.getStringValue(context, editor);
        writer.write( InputHtml.getHtmlBody( text ) );

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
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, editor.getStyleClass(), null);

        if (isDisabled(context, editor))
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);

        String text = RendererUtils.getStringValue(context, editor);
        writer.write( htmlToPlainText( text ) );

        writer.endElement(HTML.TEXTAREA_ELEM);
	}

	private static String htmlToPlainText(String html){
		return InputHtml.getHtmlBody( html )
				.replaceAll("<br.*>","\n")
				.replaceAll("<.+?>", "");
	}

	private void encodeEndNormalMode(FacesContext context, InputHtml editor) throws IOException {
		String clientId = editor.getClientId(context);
        String formId;
        {
            UIComponent tmpComponent = editor.getParent();
            while(!(tmpComponent instanceof UIForm) ){
                tmpComponent = tmpComponent.getParent();
            }
            formId = tmpComponent.getClientId(context);
        }


        AddResource.addStyleSheet(InputHtmlRenderer.class, "kupustyles.css", context);
        AddResource.addStyleSheet(InputHtmlRenderer.class, "kupudrawerstyles.css", context);

        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "sarissa.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupuhelpers.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupueditor.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupubasetools.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupuloggers.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupucontentfilters.js", context);
		AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupucleanupexpressions.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupucontextmenu.js", context);
		AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupuinit.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupustart_form.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupusourceedit.js", context);
        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "kupudrawers.js", context);

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement(HTML.DIV_ELEM,null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display: none;",null);

        	writer.startElement("xml", null);
        	writer.writeAttribute("id", "kupuconfig", null);

        		writer.startElement("kupuconfig", null);

	        		writeTag(writer, "dst", "fulldoc.html");
	        		writeTag(writer, "use_css", "0");
	        		writeTag(writer, "reload_after_save", "0");
	        		writeTag(writer, "strict_output", "1");
	        		writeTag(writer, "content_type", "application/xhtml+xml");
	        		writeTag(writer, "compatible_singletons", "1");

					writer.startElement("table_classes", null);
	        			writeTag(writer, "class", "plain");
	        			writeTag(writer, "class", "listing");
	        			writeTag(writer, "class", "grid");
	        			writeTag(writer, "class", "data");
					writer.endElement("table_classes");

					writer.startElement("cleanup_expressions",null);
						writer.startElement("set",null);
							writer.startElement("name",null);
								writer.write("Convert single quotes to curly ones");
							writer.endElement("name");
							writer.startElement("expression",null);
								writer.startElement("reg",null);
									writer.write("(\\W)'");
								writer.endElement("reg");
								writer.startElement("replacement",null);
									writer.write("\\1&#x2018;");
								writer.endElement("replacement");
							writer.endElement("expression");
							writer.startElement("expression",null);
								writer.startElement("reg",null);
									writer.write("'");
								writer.endElement("reg");
								writer.startElement("replacement",null);
									writer.write("&#x2019;");
								writer.endElement("replacement");
							writer.endElement("expression");
		                writer.endElement("set");
						writer.startElement("set",null);
						writer.startElement("name",null);
							writer.write("Reduce whitespace");
						writer.endElement("name");
						writer.startElement("expression",null);
							writer.startElement("reg",null);
								writer.write("[ ]{2}");
							writer.endElement("reg");
							writer.startElement("replacement",null);
								writer.write("\\x20");
							writer.endElement("replacement");
						writer.endElement("expression");
					  writer.endElement("set");
		            writer.endElement("cleanup_expressions");

					writeTag(writer, "image_xsl_uri", AddResource.getResourceMappedPath(InputHtmlRenderer.class, "kupudrawers/drawer.xsl", context));
					writeTag(writer, "link_xsl_uri", AddResource.getResourceMappedPath(InputHtmlRenderer.class, "kupudrawers/drawer.xsl", context));

					// TODO : Make this work (reference available images, ...).
					writeTag(writer, "image_libraries_uri", AddResource.getResourceMappedPath(InputHtmlRenderer.class, "kupudrawers/imagelibrary.xml", context));
					writeTag(writer, "link_libraries_uri", AddResource.getResourceMappedPath(InputHtmlRenderer.class, "kupudrawers/linklibrary.xml", context));
	        		writeTag(writer, "search_images_uri", "");
	        		writeTag(writer, "search_links_uri", "");

        		writer.endElement("kupuconfig");
			writer.endElement("xml");
   		writer.endElement(HTML.DIV_ELEM);


		writer.startElement(HTML.DIV_ELEM,null);
        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-fulleditor", null);

        	//
        	// Toolbar
        	//
        	writer.startElement(HTML.DIV_ELEM,null);
			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb",null);
			writer.writeAttribute(HTML.ID_ATTR, "toolbar",null);

				writer.startElement(HTML.SPAN_ELEM,null);
				writer.writeAttribute(HTML.ID_ATTR, "kupu-tb-buttons",null);

    				writer.startElement(HTML.SPAN_ELEM,null);
    				writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup",null);
        			writer.writeAttribute(HTML.ID_ATTR, "kupu-logo",null);
        			writer.writeAttribute(HTML.STYLE_ATTR, "float: right", null);
                        writer.startElement(HTML.BUTTON_ELEM,null);
                        writer.writeAttribute(HTML.TYPE_ATTR, "button",null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-zoom",null);
                        writer.writeAttribute(HTML.ID_ATTR, "kupu-zoom-button",null);
                        writer.writeAttribute(HTML.TITLE_ATTR, "zoom: alt-x",null);
                        writer.writeAttribute(HTML.ACCESSKEY_ATTR, "x",null);
                            writer.write("&#xA0;");
                        writer.endElement(HTML.BUTTON_ELEM);
                        if( editor.isAddKupuLogo() ){
            				writer.startElement(HTML.BUTTON_ELEM,null);
                            writer.writeAttribute(HTML.TYPE_ATTR, "button",null);
            				writer.writeAttribute(HTML.CLASS_ATTR, "kupu-logo",null);
            				writer.writeAttribute(HTML.TITLE_ATTR, "Kupu 1.2",null);
            				writer.writeAttribute(HTML.ACCESSKEY_ATTR, "k",null);
            				writer.writeAttribute(HTML.ONCLICK_ATTR, "window.open('http://kupu.oscom.org');",null);
            					writer.write("&#xA0;");
                      		writer.endElement(HTML.BUTTON_ELEM);
                        }
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SELECT_ELEM,null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-tb-styles",null);
                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "P",null);
                		writer.writeAttribute("i18n:translate", "paragraph-normal", null);
                			writer.write("Normal");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "H1",null);
                			writer.startElement(HTML.SPAN_ELEM,null);
                			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                			writer.writeAttribute("i18n:translate", "heading", null);
                				writer.write("Heading");
                			writer.endElement(HTML.SPAN_ELEM);
                			writer.write(" 1");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "H2",null);
                			writer.startElement(HTML.SPAN_ELEM,null);
                			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                			writer.writeAttribute("i18n:translate", "heading", null);
                				writer.write("Heading");
                			writer.endElement(HTML.SPAN_ELEM);
                			writer.write(" 2");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "H3",null);
                			writer.startElement(HTML.SPAN_ELEM,null);
                			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                			writer.writeAttribute("i18n:translate", "heading", null);
                				writer.write("Heading");
                			writer.endElement(HTML.SPAN_ELEM);
                			writer.write(" 3");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "H4",null);
                			writer.startElement(HTML.SPAN_ELEM,null);
                			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                			writer.writeAttribute("i18n:translate", "heading", null);
                				writer.write("Heading");
                			writer.endElement(HTML.SPAN_ELEM);
                			writer.write(" 4");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "H5",null);
                			writer.startElement(HTML.SPAN_ELEM,null);
                			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                			writer.writeAttribute("i18n:translate", "heading", null);
                				writer.write("Heading");
                			writer.endElement(HTML.SPAN_ELEM);
                			writer.write(" 5");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "H6",null);
                			writer.startElement(HTML.SPAN_ELEM,null);
                			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                			writer.writeAttribute("i18n:translate", "heading", null);
                				writer.write("Heading");
                			writer.endElement(HTML.SPAN_ELEM);
                			writer.write(" 6");
                		writer.endElement(HTML.OPTION_ELEM);

                		writer.startElement(HTML.OPTION_ELEM,null);
                		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                		writer.writeAttribute(HTML.VALUE_ATTR, "PRE",null);
                		writer.writeAttribute("i18n:translate", "paragraph-formatted", null);
                			writer.write("Formatted");
                		writer.endElement(HTML.OPTION_ELEM);

                	writer.endElement(HTML.SELECT_ELEM);

					writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
					writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                		writeButton(writer, "kupu-save", "Save", "s");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-basicmarkup", null);
                		writeButton(writer, "kupu-bold", "bold: alt-b", "b");
                		writeButton(writer, "kupu-italic", "italic: alt-i", "i");
                		writeButton(writer, "kupu-underline", "underline: alt-u", "u");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-subsuper", null);
                		writeButton(writer, "kupu-subscript", "subscript: alt--", "-");
                		writeButton(writer, "kupu-superscript", "superscript: alt-+", "+");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                		writeButton(writer, "kupu-forecolor", "text color: alt-f", "f");
                		writeButton(writer, "kupu-hilitecolor", "background color: alt-h", "h");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-justify", null);
                		writeButton(writer, "kupu-justifyleft", "left justify: alt-l", "l");
                		writeButton(writer, "kupu-justifycenter", "center justify: alt-c", "c");
                		writeButton(writer, "kupu-justifyright", "right justify: alt-r", "r");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-list", null);
                		writeButton(writer, "kupu-insertorderedlist", "numbered list: alt-#", "#", "kupu-list-ol-addbutton");
                		writeButton(writer, "kupu-insertunorderedlist", "unordered list: alt-*", "*", "kupu-list-ul-addbutton");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-definitionlist", null);
                		writeButton(writer, "kupu-insertdefinitionlist", "definition list: alt-=", "=", "kupu-list-dl-addbutton");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-indent", null);
                		writeButton(writer, "kupu-outdent", "outdent: alt-<", "<");
                		writeButton(writer, "kupu-indent", "indent: alt->", ">");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    if( ! editor.isAllowExternalLinks() ){
                        writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
                    }
                		writeButton(writer, "kupu-image", "image", null, "kupu-imagelibdrawer-button", false); // TODO : Enable
                		writeButton(writer, "kupu-inthyperlink", "link", null, "kupu-linklibdrawer-button", false); // TODO : Enable
                		writeButton(writer, "kupu-exthyperlink", "external link", null, "kupu-linkdrawer-button");
                		writeButton(writer, "kupu-table", "table", null, "kupu-tabledrawer-button");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-remove", null);
                		writeButton(writer, "kupu-removeimage invisible", "Remove image", null, "kupu-removeimage-button");
                		writeButton(writer, "kupu-removelink invisible", "Remove link", null, "kupu-removelink-button");
                	writer.endElement(HTML.SPAN_ELEM);

                   	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                	writer.writeAttribute(HTML.ID_ATTR, "kupu-bg-undo", null);
                		writeButton(writer, "kupu-undo", "undo: alt-z", "z");
                		writeButton(writer, "kupu-redo", "redo: alt-y", "y");
                	writer.endElement(HTML.SPAN_ELEM);

                	writer.startElement(HTML.SPAN_ELEM,null);
                	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-tb-buttongroup", null);
                    writer.writeAttribute(HTML.ID_ATTR, "kupu-source", null);
                	if( ! editor.isAllowEditSource() ){
                	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
        			}
                		writeButton(writer, "kupu-source", "edit HTML code", null);
                 	writer.endElement(HTML.SPAN_ELEM);

                writer.endElement(HTML.SPAN_ELEM); // kupu-tb-buttons

             	writer.startElement(HTML.SELECT_ELEM,null);
             	writer.writeAttribute(HTML.ID_ATTR, "kupu-ulstyles", null);
             		writeOption(writer, "disc", "list-disc", "&#x25CF;");
             		writeOption(writer, "square", "list-square", "&#x25A0;");
             		writeOption(writer, "circle", "list-circle", "&#x25CB;");
             		writeOption(writer, "none", "list-nobullet", "no bullet");
             	writer.endElement(HTML.SELECT_ELEM);

             	writer.startElement(HTML.SELECT_ELEM,null);
             	writer.writeAttribute(HTML.ID_ATTR, "kupu-olstyles", null);
             		writeOption(writer, "decimal", "list-decimal", "1");
             		writeOption(writer, "upper-roman", "list-upperroman", "I");
             		writeOption(writer, "lower-roman", "list-lowerroman", "i");
             		writeOption(writer, "upper-alpha", "list-upperalpha", "A");
             		writeOption(writer, "lower-alpha", "list-loweralpha", "a");
             	writer.endElement(HTML.SELECT_ELEM);

             	writer.startElement(HTML.DIV_ELEM,null);
             	writer.writeAttribute(HTML.STYLE_ATTR, "display:block;", null);
                 	writer.startElement(HTML.DIV_ELEM,null);
                 	writer.writeAttribute(HTML.ID_ATTR, "kupu-librarydrawer", null);
                 	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-drawer", null);
                 	writer.endElement(HTML.DIV_ELEM);
             	writer.endElement(HTML.DIV_ELEM);

                // External Link drawer
             	writer.startElement(HTML.DIV_ELEM,null);
             	writer.writeAttribute(HTML.ID_ATTR, "kupu-linkdrawer", null);
             	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-drawer", null);
                 	writer.startElement(HTML.H1_ELEM,null);
                 	writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                 	writer.writeAttribute("i18n:translate", "", null);
                 		writer.write("External Link");
                 	writer.endElement(HTML.H1_ELEM);
                 	writer.startElement(HTML.DIV_ELEM,null);
                 	writer.writeAttribute(HTML.ID_ATTR, "kupu-linkdrawer-addlink", null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panels", null);
                     	writer.startElement(HTML.TABLE_ELEM,null);
                     		writer.startElement(HTML.TR_ELEM,null);
                         		writer.startElement(HTML.TD_ELEM,null);
                                    writer.startElement(HTML.DIV_ELEM,null);
                                    writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-label", null);
                                        writer.startElement(HTML.SPAN_ELEM,null);
                                     		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
                                     		writer.writeAttribute("i18n:translate", "items-matching-keyword", null);
                                     			writer.write("Link the highlighted text to this URL");
                                 		writer.endElement(HTML.SPAN_ELEM);
                                 		writer.write(":");
                                    writer.endElement(HTML.DIV_ELEM);
                                    writer.startElement(HTML.INPUT_ELEM,null);
                                    writer.writeAttribute(HTML.ID_ATTR, "kupu-linkdrawer-input", null);
                                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-st", null);
                                    writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                                    writer.endElement(HTML.INPUT_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                                writer.startElement(HTML.TD_ELEM,null);
                                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-preview-button", null);
                                    writer.startElement(HTML.BUTTON_ELEM,null);
                                    writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                    writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.preview()", null);
                                        writer.write("Preview");
                                    writer.endElement(HTML.BUTTON_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                     		writer.endElement(HTML.TR_ELEM);
                            writer.startElement(HTML.TR_ELEM,null);
                                writer.startElement(HTML.TD_ELEM,null);
                                writer.writeAttribute(HTML.COLSPAN_ATTR, "2", null);
                                writer.writeAttribute(HTML.ALIGN_ATTR, "center", null);
                                    writer.startElement(HTML.IFRAME_ELEM,null);
                                    writer.writeAttribute(HTML.FRAMEBORDER_ATTR, "1", null);
                                    writer.writeAttribute(HTML.SCROLLING_ATTR, "auto", null);
                                    writer.writeAttribute(HTML.WIDTH_ATTR, "440", null);
                                    writer.writeAttribute(HTML.HEIGHT_ATTR, "198", null);
                                    writer.writeAttribute(HTML.ID_ATTR, "kupu-linkdrawer-preview", null);
                                    writer.writeAttribute(HTML.SRC_ATTR, AddResource.getResourceMappedPath(InputHtmlRenderer.class, "kupublank.html", context), null);
                                    writer.endElement(HTML.IFRAME_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                            writer.endElement(HTML.TR_ELEM);
                     	writer.endElement(HTML.TABLE_ELEM);
                     	writer.startElement(HTML.DIV_ELEM,null);
                     	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialogbuttons", null);
                     		writer.startElement(HTML.BUTTON_ELEM,null);
                     		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                     		writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.save()", null);
                     			writer.write("Ok");
                     		writer.endElement(HTML.BUTTON_ELEM);
                     		writer.startElement(HTML.BUTTON_ELEM,null);
                     		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                     		writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.closeDrawer()", null);
                     			writer.write("Cancel");
                     		writer.endElement(HTML.BUTTON_ELEM);
                     	writer.endElement(HTML.DIV_ELEM);
                 	writer.endElement(HTML.DIV_ELEM);
             	writer.endElement(HTML.DIV_ELEM);

                // Table drawer
             	writer.startElement(HTML.DIV_ELEM,null);
             	writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer",null);
             	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-drawer",null);
             	    writer.startElement(HTML.H1_ELEM,null);
             	        writer.write("Table");
                    writer.endElement(HTML.H1_ELEM);
                    writer.startElement(HTML.DIV_ELEM,null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panels", null);
                 		writer.startElement(HTML.TABLE_ELEM,null);
                        writer.writeAttribute(HTML.WIDTH_ATTR, "99%",null);
                            writer.startElement(HTML.TR_ELEM,null);
                            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panelsrow",null);
                                writer.startElement(HTML.TD_ELEM,null);
                                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-panel",null);
                                    writer.startElement(HTML.TABLE_ELEM,null);
                             		writer.writeAttribute(HTML.WIDTH_ATTR, "100%",null);
                             		    writer.startElement(HTML.TBODY_ELEM,null);

                             				writer.startElement(HTML.TR_ELEM,null);
                             					writer.startElement(HTML.TD_ELEM,null);
                             					writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                             						writer.write("Table Class ");
                             					writer.endElement(HTML.TD_ELEM);
                             					writer.startElement(HTML.TD_ELEM,null);
                             					writer.writeAttribute(HTML.WIDTH_ATTR, "50%", null);
                             						writer.startElement(HTML.SELECT_ELEM,null);
                             						writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-classchooser", null);
                             						writer.writeAttribute(HTML.ONCHANGE_ATTR, "drawertool.current_drawer.tool.setTableClass(this.options[this.selectedIndex].value)", null);
                             							writeOption(writer, "plain", "Plain");
                             							writeOption(writer, "listing", "Listing");
                             							writeOption(writer, "grid", "Grid");
                             							writeOption(writer, "data", "Data");
                             						writer.endElement(HTML.SELECT_ELEM);
                             					writer.endElement(HTML.TD_ELEM);
                             				writer.endElement(HTML.TR_ELEM);

                             				writer.startElement(HTML.TR_ELEM, null);

                             					writer.startElement(HTML.TD_ELEM, null);
                             					writer.writeAttribute(HTML.COLSPAN_ATTR, "2", null);
                                                writer.writeAttribute(HTML.CLASS_ATTR, "", null); // ?

                                 					writer.startElement(HTML.DIV_ELEM, null);
                                 					writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-addtable", null);
                                 						writer.startElement(HTML.TABLE_ELEM, null);
                                 						writer.writeAttribute(HTML.WIDTH_ATTR, "100%", null);

                                 							writer.startElement(HTML.TR_ELEM, null);
                                 								writer.startElement(HTML.TD_ELEM, null);
                                 								writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                 								writer.writeAttribute(HTML.WIDTH_ATTR, "50%", null);
                                 									writer.write("Rows");
                                 								writer.endElement(HTML.TD_ELEM);
                                 								writer.startElement(HTML.TD_ELEM, null);
                                 									writer.startElement(HTML.INPUT_ELEM, null);
                                 									writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                                 									writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-newrows", null);
                                                                    writer.writeAttribute(HTML.VALUE_ATTR, "3", null);
                                 									writer.endElement(HTML.INPUT_ELEM);
                                 								writer.endElement(HTML.TD_ELEM);
                                 							writer.endElement(HTML.TR_ELEM);

                                 							writer.startElement(HTML.TR_ELEM, null);
                                 								writer.startElement(HTML.TD_ELEM, null);
                                 								writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                 									writer.write("Columns");
                                 								writer.endElement(HTML.TD_ELEM);
                                 								writer.startElement(HTML.TD_ELEM, null);
                                 									writer.startElement(HTML.INPUT_ELEM, null);
                                 									writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
                                 									writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-newcols", null);
                                                                    writer.writeAttribute(HTML.VALUE_ATTR, "3", null);
                                 									writer.endElement(HTML.INPUT_ELEM);
                                 								writer.endElement(HTML.TD_ELEM);
                             								writer.endElement(HTML.TR_ELEM);

                                 							writer.startElement(HTML.TR_ELEM, null);
                                 								writer.startElement(HTML.TD_ELEM, null);
                                 								writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                 									writer.write("Headings");
                                 								writer.endElement(HTML.TD_ELEM);
                                 								writer.startElement(HTML.TD_ELEM, null);
                                 								writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
                                 									writer.startElement(HTML.INPUT_ELEM, null);
                                 									writer.writeAttribute(HTML.NAME_ATTR, "kupu-tabledrawer-makeheader", null);
                                 									writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-makeheader", null);
                                 									writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
                                 									writer.endElement(HTML.INPUT_ELEM);
                                 									writer.startElement(HTML.LABEL_ELEM, null);
                                 									writer.writeAttribute(HTML.FOR_ATTR, "kupu-tabledrawer-makeheader", null);
                                 										writer.write("Create");
                                 									writer.endElement(HTML.LABEL_ELEM);
                                 								writer.endElement(HTML.TD_ELEM);
                             								writer.endElement(HTML.TR_ELEM);

                             								writer.startElement(HTML.TR_ELEM, null);
                             									writer.startElement(HTML.TD_ELEM, null);
                             									writer.writeAttribute(HTML.COLSPAN_ATTR, "2", null);
                             									writer.writeAttribute(HTML.STYLE_ATTR, "text-align: center", null);
                             										writer.startElement(HTML.BUTTON_ELEM, null);
                             										writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                             										writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.createTable()", null);
                             											writer.write("Add Table");
                             										writer.endElement(HTML.BUTTON_ELEM);
                             									writer.endElement(HTML.TD_ELEM);
                             								writer.endElement(HTML.TR_ELEM);

                                                            writer.startElement(HTML.TR_ELEM, null);
                                                                writer.startElement(HTML.TD_ELEM, null);
                                                                writer.writeAttribute(HTML.COLSPAN_ATTR, "2", null);
                                                                writer.writeAttribute(HTML.STYLE_ATTR, "text-align: center", null);
                                                                    writer.startElement(HTML.BUTTON_ELEM, null);
                                                                    writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                                    writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.tool.fixAllTables()", null);
                                                                        writer.write("Fix All Tables");
                                                                    writer.endElement(HTML.BUTTON_ELEM);
                                                                writer.endElement(HTML.TD_ELEM);
                                                            writer.endElement(HTML.TR_ELEM);

                                 						writer.endElement(HTML.TABLE_ELEM);
                                 					writer.endElement(HTML.DIV_ELEM); // kupu-tabledrawer-addtable

                                 					writer.startElement(HTML.DIV_ELEM, null);
                                 					writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-edittable", null);
                                 						writer.startElement(HTML.TABLE_ELEM, null);
                                 						writer.writeAttribute(HTML.WIDTH_ATTR, "100%", null);

                                     						writer.startElement(HTML.TR_ELEM,null);
                	                         					writer.startElement(HTML.TD_ELEM,null);
                	                         					writer.writeAttribute(HTML.WIDTH_ATTR, "50%", null);
                	                         						writer.write("Current column alignment");
                	                         					writer.endElement(HTML.TD_ELEM);
                	                         					writer.startElement(HTML.TD_ELEM,null);
                	                         						writer.startElement(HTML.SELECT_ELEM,null);
                	                         						writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-alignchooser", null);
                		                         					writer.writeAttribute(HTML.ONCHANGE_ATTR, "drawertool.current_drawer.tool.setColumnAlign(this.options[this.selectedIndex].value)", null);
                	                         							writeOption(writer, "left", "Left");
                	                         							writeOption(writer, "center", "Center");
                	                         							writeOption(writer, "right", "Right");
                	                         						writer.endElement(HTML.SELECT_ELEM);
                	                         					writer.endElement(HTML.TD_ELEM);
                                         					writer.endElement(HTML.TR_ELEM);

                                         					writer.startElement(HTML.TR_ELEM,null);
                	                         					writer.startElement(HTML.TD_ELEM,null);
                	                         						writer.write("Column");
                	                         					writer.endElement(HTML.TD_ELEM);
                	                         					writer.startElement(HTML.TD_ELEM,null);
                	                         						writer.startElement(HTML.BUTTON_ELEM, null);
                	                         						writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                	                         						writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-addcolumn-button", null);
                	                         						writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.tool.addTableColumn()", null);
                	                         							writer.write("Add");
                	                         						writer.endElement(HTML.BUTTON_ELEM);
                	                         						writer.startElement(HTML.BUTTON_ELEM, null);
                	                         						writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                	                         						writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-delcolumn-button", null);
                	                         						writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.tool.delTableColumn()", null);
                	                         							writer.write("Remove");
                	                         						writer.endElement(HTML.BUTTON_ELEM);
                	                         					writer.endElement(HTML.TD_ELEM);
                                         					writer.endElement(HTML.TR_ELEM);

                                         					writer.startElement(HTML.TR_ELEM,null);
                	                         					writer.startElement(HTML.TD_ELEM,null);
                	                         						writer.write("Row");
                	                         					writer.endElement(HTML.TD_ELEM);
                	                         					writer.startElement(HTML.TD_ELEM,null);
                	                         						writer.startElement(HTML.BUTTON_ELEM, null);
                	                         						writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                	                         						writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-addrow-button", null);
                	                         						writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.addTableRow()", null);
                	                         							writer.write("Add");
                	                         						writer.endElement(HTML.BUTTON_ELEM);
                	                         						writer.startElement(HTML.BUTTON_ELEM, null);
                	                         						writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                	                         						writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-delrow-button", null);
                	                         						writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.delTableRow()", null);
                	                         							writer.write("Remove");
                	                         						writer.endElement(HTML.BUTTON_ELEM);
                	                         					writer.endElement(HTML.TD_ELEM);
                                         					writer.endElement(HTML.TR_ELEM);

                                                            writer.startElement(HTML.TR_ELEM,null);
                                                                writer.startElement(HTML.TD_ELEM,null);
                                                                    writer.write("Fix Table");
                                                                writer.endElement(HTML.TD_ELEM);
                                                                writer.startElement(HTML.TD_ELEM,null);
                                                                    writer.startElement(HTML.BUTTON_ELEM, null);
                                                                    writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                                                                    // BUG writer.writeAttribute(HTML.ID_ATTR, "kupu-tabledrawer-addrow-button", null);
                                                                    writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.current_drawer.tool.fixTable()", null);
                                                                        writer.write("Fix");
                                                                    writer.endElement(HTML.BUTTON_ELEM);
                                                                writer.endElement(HTML.TD_ELEM);
                                                            writer.endElement(HTML.TR_ELEM);

                                 						writer.endElement(HTML.TABLE_ELEM);
                                 					writer.endElement(HTML.DIV_ELEM); // kupu-tabledrawer-edittable

                             					writer.endElement(HTML.TD_ELEM);
                             				writer.endElement(HTML.TR_ELEM);

                             			writer.endElement(HTML.TBODY_ELEM);
                                    writer.endElement(HTML.TABLE_ELEM);
                                writer.endElement(HTML.TD_ELEM);
                            writer.endElement(HTML.TR_ELEM);
                        writer.endElement(HTML.TABLE_ELEM);
                        writer.startElement(HTML.DIV_ELEM,null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-dialogbuttons", null);
                            writer.startElement(HTML.BUTTON_ELEM,null);
                            writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
                            writer.writeAttribute(HTML.ONCLICK_ATTR, "drawertool.closeDrawer()", null);
                                writer.write("Close");
                            writer.endElement(HTML.BUTTON_ELEM);
                        writer.endElement(HTML.DIV_ELEM);
                    writer.endElement(HTML.DIV_ELEM);
             	writer.endElement(HTML.DIV_ELEM); // kupu-tabledrawer

            writer.endElement(HTML.DIV_ELEM); // toolbar

            //
            // Tool Boxes
            //
            writer.startElement(HTML.DIV_ELEM, null);
            writer.writeAttribute("xmlns", "", null);
            writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolboxes", null);
        	if( ! editor.isShowAnyToolBox() ){
        	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
        	}

            	// Properties tool box
            	writer.startElement(HTML.DIV_ELEM, null);
            	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-properties", null);
            	if( ! editor.isShowPropertiesToolBox() ){
            	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	}
            		writer.startElement(HTML.H1_ELEM, null);
            			writer.write("Properties");
            		writer.endElement(HTML.H1_ELEM);
            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
            			writer.write("Title:");
            		writer.endElement(HTML.DIV_ELEM);
            		writer.startElement(HTML.INPUT_ELEM, null);
                    writer.writeAttribute(HTML.CLASS_ATTR, "wide", null); // TODO : Check class name. Should be something like kupu-wide in next version.
            		writer.writeAttribute(HTML.ID_ATTR, "kupu-properties-title", null);
            		writer.endElement(HTML.INPUT_ELEM);
            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
            			writer.write("Description:");
            		writer.endElement(HTML.DIV_ELEM);
            		writer.startElement(HTML.TEXTAREA_ELEM, null);
            			writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
            			writer.writeAttribute(HTML.ID_ATTR, "kupu-properties-description", null);
            		writer.endElement(HTML.TEXTAREA_ELEM);
            	writer.endElement(HTML.DIV_ELEM);

            	// Links tool box
            	writer.startElement(HTML.DIV_ELEM, null);
            	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-links", null);
            	if( ! editor.isShowLinksToolBox() ){
            	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	}
            		writer.startElement(HTML.H1_ELEM, null);
            		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
            		writer.writeAttribute("i18n:translate", "links", null);
            			writer.write("Links");
            		writer.endElement(HTML.H1_ELEM);

            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-addlink", null);

	            		writer.startElement(HTML.DIV_ELEM, null);
	            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
	            			writer.startElement(HTML.SPAN_ELEM, null);
	            			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
	            			writer.writeAttribute("i18n:translate", "items-matching-keyword", null);
	            				writer.write("Link the highlighted text to this URL:");
	            			writer.endElement(HTML.SPAN_ELEM);
	            		writer.endElement(HTML.DIV_ELEM);

	            		writer.startElement(HTML.INPUT_ELEM, null);
	            		writer.writeAttribute(HTML.ID_ATTR, "kupu-link-input", null);
	            		writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
	            		writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
	            		writer.endElement(HTML.INPUT_ELEM);

	            		writer.startElement(HTML.DIV_ELEM, null);
	            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
	            			writer.startElement(HTML.BUTTON_ELEM, null);
	            			writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
	            			writer.writeAttribute(HTML.ID_ATTR, "kupu-link-button", null);
	            			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-action", null);
	            				writer.write("Make Link");
	            			writer.endElement(HTML.BUTTON_ELEM);
	            		writer.endElement(HTML.DIV_ELEM);

            		writer.endElement(HTML.DIV_ELEM);
            	writer.endElement(HTML.DIV_ELEM);

            	// Images tool box
            	writer.startElement(HTML.DIV_ELEM, null);
            	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-images", null);
            	if( ! editor.isShowImagesToolBox() ){
            	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	}
            		writer.startElement(HTML.H1_ELEM, null);
            		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
            		writer.writeAttribute("i18n:translate", "images", null);
            			writer.write("Images");
            		writer.endElement(HTML.H1_ELEM);

            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
            			writer.startElement(HTML.SPAN_ELEM, null);
            			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
            			writer.writeAttribute("i18n:translate", "", null);
            				writer.write("Insert image at the following URL:");
            			writer.endElement(HTML.SPAN_ELEM);
            		writer.endElement(HTML.DIV_ELEM);

            		writer.startElement(HTML.INPUT_ELEM, null);
            		writer.writeAttribute(HTML.ID_ATTR, "kupu-image-input", null);
            		writer.writeAttribute(HTML.VALUE_ATTR, "kupuimages/kupu_icon.gif", null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
            		writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
            		writer.endElement(HTML.INPUT_ELEM);

        			writer.startElement(HTML.SPAN_ELEM, null);
        			writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
        			writer.writeAttribute("i18n:translate", "", null);
        				writer.write("Image float:");
           			writer.endElement(HTML.SPAN_ELEM);

            		writer.startElement(HTML.SELECT_ELEM, null);
            		writer.writeAttribute(HTML.ID_ATTR, "kupu-image-float-select", null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
            			writeOption(writer, "none", "No Float");
            			writeOption(writer, "left", "Left");
            			writeOption(writer, "right", "Right");
            		writer.endElement(HTML.SELECT_ELEM);

            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "text-align: center", null);
            			writer.startElement(HTML.BUTTON_ELEM, null);
            			writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
            			writer.writeAttribute(HTML.ID_ATTR, "kupu-image-addbutton", null);
            			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-action", null);
            				writer.write("Insert Image");
            			writer.endElement(HTML.BUTTON_ELEM);
            		writer.endElement(HTML.DIV_ELEM);

            	writer.endElement(HTML.DIV_ELEM);

            	// Tables tool box
            	writer.startElement(HTML.DIV_ELEM, null);
            	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-tables", null);
            	if( ! editor.isShowTablesToolBox() ){
            	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	}
            		writer.startElement(HTML.H1_ELEM, null);
            		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
            		writer.writeAttribute("i18n:translate", "table-inspector", null);
            			writer.write("Tables");
            		writer.endElement(HTML.H1_ELEM);

            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
            			writer.write("Table Class:");
            			writer.startElement(HTML.SELECT_ELEM, null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
            			writer.writeAttribute(HTML.ID_ATTR, "kupu-table-classchooser", null);
            			writer.endElement(HTML.SELECT_ELEM);
            		writer.endElement(HTML.DIV_ELEM);

            		// Add table
            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-addtable", null);
            			writer.startElement(HTML.DIV_ELEM, null);
            			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
            				writer.write("Rows:");
            			writer.endElement(HTML.DIV_ELEM);
            			writer.startElement(HTML.INPUT_ELEM, null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
            			writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
            			writer.writeAttribute(HTML.ID_ATTR, "kupu-table-newrows", null);
            			writer.endElement(HTML.INPUT_ELEM);

	        			writer.startElement(HTML.DIV_ELEM, null);
	        			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
	        				writer.write("Columns:");
	        			writer.endElement(HTML.DIV_ELEM);
	        			writer.startElement(HTML.INPUT_ELEM, null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
	        			writer.writeAttribute(HTML.TYPE_ATTR, "text", null);
	        			writer.writeAttribute(HTML.ID_ATTR, "kupu-table-newcols", null);
	        			writer.endElement(HTML.INPUT_ELEM);

	        			writer.startElement(HTML.DIV_ELEM, null);
	        			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
	    					writer.write("Headings:");
	    					writer.startElement(HTML.INPUT_ELEM, null);
	    					writer.writeAttribute(HTML.NAME_ATTR, "kupu-table-makeheader", null);
	    					writer.writeAttribute(HTML.ID_ATTR, "kupu-table-makeheader", null);
	            			writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
	            			writer.endElement(HTML.INPUT_ELEM);
	            			writer.startElement(HTML.LABEL_ELEM, null);
	            			writer.writeAttribute(HTML.FOR_ATTR, "kupu-table-makeheader", null);
	            				writer.write("Create");
	            			writer.endElement(HTML.LABEL_ELEM);
	        			writer.endElement(HTML.DIV_ELEM);

	        			writer.startElement(HTML.DIV_ELEM, null);
	        			writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
	        				writeButton(writer, "kupu-table-fixall-button", "Fix Table");
	        				writeButton(writer, "kupu-table-addtable-button", "Add Table");
	        			writer.endElement(HTML.DIV_ELEM);
	        		writer.endElement(HTML.DIV_ELEM); // Add table

	        		// Edit table
	        		writer.startElement(HTML.DIV_ELEM, null);
        			writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-edittable", null);

        				writer.startElement(HTML.DIV_ELEM, null);
        				writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
        					writer.write("Col Align:");
        					writer.startElement(HTML.SELECT_ELEM, null);
                            writer.writeAttribute(HTML.CLASS_ATTR, "wide", null);
    	            		writer.writeAttribute(HTML.ID_ATTR, "kupu-table-alignchooser", null);
    	            			writeOption(writer, "left", "Left");
    	            			writeOption(writer, "center", "Center");
    	            			writeOption(writer, "right", "Right");
    	            		writer.endElement(HTML.SELECT_ELEM);
        				writer.endElement(HTML.DIV_ELEM);

                        writer.startElement(HTML.DIV_ELEM,null);
                        writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-buttons", null);
            				writer.write("<br/>");
            				writeButton(writer, "kupu-table-addcolumn-button", "Add Column");
            				writeButton(writer, "kupu-table-delcolumn-button", "Remove Column");
            				writer.write("<br/>");
            				writeButton(writer, "kupu-table-addrow-button", "Add Row");
            				writeButton(writer, "kupu-table-delrow-button", "Remove Row");
                            writeButton(writer, "kupu-table-fix-button", "Fix");
                        writer.endElement(HTML.DIV_ELEM);

        			writer.endElement(HTML.DIV_ELEM); // Edit table

            	writer.endElement(HTML.DIV_ELEM);

            	// Cleanup expressions tool box
            	writer.startElement(HTML.DIV_ELEM, null);
            	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-cleanupexpressions", null);
            	if( ! editor.isShowCleanupExpressionsToolBox() ){
            	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	}
            		writer.startElement(HTML.H1_ELEM, null);
            		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
            		writer.writeAttribute("i18n:translate", "", null);
            			writer.write("Cleanup expressions");
            		writer.endElement(HTML.H1_ELEM);
            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
						writer.startElement(HTML.SPAN_ELEM,null);
						writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
	            		writer.writeAttribute("i18n:translate", "", null);
							writer.write("Select a cleanup action:");
						writer.endElement(HTML.SPAN_ELEM);
            		writer.endElement(HTML.DIV_ELEM);
					writer.startElement(HTML.SELECT_ELEM,null);
					writer.writeAttribute(HTML.ID_ATTR, "kupucleanupexpressionselect", null);
					writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-st", null);
					writer.endElement(HTML.SELECT_ELEM);
					writer.startElement(HTML.DIV_ELEM,null);
					writer.writeAttribute(HTML.STYLE_ATTR,"text-align: center",null);
						writer.startElement(HTML.BUTTON_ELEM,null);
						writer.writeAttribute(HTML.TYPE_ATTR,"button",null);
						writer.writeAttribute(HTML.ID_ATTR,"kupucleanupexpressionbutton",null);
						writer.writeAttribute(HTML.CLASS_ATTR,"kupu-toolbox-action",null);
							writer.write("Perform action");
						writer.endElement(HTML.BUTTON_ELEM);
					writer.endElement(HTML.DIV_ELEM);
            	writer.endElement(HTML.DIV_ELEM);

            	// Debug tool box
            	writer.startElement(HTML.DIV_ELEM, null);
            	writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-debug", null);
            	if( ! editor.isShowDebugToolBox() ){
            	    writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	}
            		writer.startElement(HTML.H1_ELEM, null);
            		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
            		writer.writeAttribute("i18n:translate", "debug-log", null);
            			writer.write("Debug Log");
            		writer.endElement(HTML.H1_ELEM);
            		writer.startElement(HTML.DIV_ELEM, null);
            		writer.writeAttribute(HTML.ID_ATTR, "kupu-toolbox-debuglog", null);
            		writer.writeAttribute(HTML.CLASS_ATTR, "kupu-toolbox-label", null);
            		writer.endElement(HTML.DIV_ELEM);
            	writer.endElement(HTML.DIV_ELEM);

            writer.endElement(HTML.DIV_ELEM); // kupu-toolboxes

            // Color Chooser
            writer.startElement(HTML.TABLE_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, "kupu-colorchooser", null);
            writer.writeAttribute(HTML.CELLPADDING_ATTR, "0", null);
            writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);
            writer.writeAttribute(HTML.STYLE_ATTR, "position: fixed; border-style: solid; border-color: black; border-width: 1px;", null);
            writer.endElement(HTML.TABLE_ELEM);

            // Edit space
            writer.startElement(HTML.DIV_ELEM, null);
            if( !editor.isShowAnyToolBox() ){
                writer.writeAttribute(HTML.STYLE_ATTR, "margin-right: 0.3em", null);
            }
            if( editor.getStyle()!=null ){
                // Convert the style into an style declaration so that it doesn't preempt the Zoom works as it's relying on changing the class
                AddResource.addInlineStyleToHeader(
						"#kupu-editor{height: inherit;} "+
						"div.kupu-fulleditor{"+editor.getStyle()+"}",
						context);
            }
            writer.writeAttribute(HTML.CLASS_ATTR,
                    "kupu-editorframe"+(editor.getStyleClass()==null ? "" : " "+editor.getStyleClass()), null);
            	writer.startElement(HTML.IFRAME_ELEM, null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-editor", null);
            	writer.writeAttribute(HTML.FRAMEBORDER_ATTR, "0", null);
            	writer.writeAttribute(HTML.SCROLLING_ATTR, "auto", null);
            	writer.writeAttribute(HTML.SRC_ATTR, "about:blank", null); // Text loaded afterward by javascript

            	writer.endElement(HTML.IFRAME_ELEM);
            	writer.startElement(HTML.TEXTAREA_ELEM, null);
                writer.writeAttribute(HTML.CLASS_ATTR, "kupu-editor-textarea", null);
            	writer.writeAttribute(HTML.ID_ATTR, "kupu-editor-textarea", null);
            	//writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
            	writer.endElement(HTML.TEXTAREA_ELEM);
            writer.endElement(HTML.DIV_ELEM);

        writer.endElement(HTML.DIV_ELEM); // kupu-fulleditor

        String text = editor.getValueAsHtmlDocument( context );
        String encodedText = text == null ? "" : JavascriptUtils.encodeString( text );

        AddResource.addJavaScriptToHeader(InputHtmlRenderer.class, "myFacesUtils.js", context);

        String resourceBaseURL = AddResource.getResourceMappedPath(InputHtmlRenderer.class, "", context);

        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        	writer.write("myFacesKupuSet(" +
        			"\""+encodedText+"\"," +
        			"\""+clientId+"\"," +
        			"\""+formId+"\"," +
        			"\""+resourceBaseURL+"\"" +
        			");");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    static private void writeTag(ResponseWriter writer, String tagName, String tagBody) throws IOException{
		writer.startElement(tagName, null);
		writer.writeText(tagBody, null);
		writer.endElement(tagName);
    }

    static private void writeButton(ResponseWriter writer, String classAttr, String title, String accessKey) throws IOException{
        writeButton(writer, classAttr, title, accessKey, classAttr+"-button");
    }

    static private void writeButton(ResponseWriter writer, String classAttr, String title, String accessKey, String id) throws IOException{
        writeButton(writer, classAttr, title, accessKey, id, true);
    }

    static private void writeButton(ResponseWriter writer, String classAttr, String title, String accessKey, String id, boolean display) throws IOException{
		writer.startElement(HTML.BUTTON_ELEM,null);
		writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.CLASS_ATTR, classAttr, null);
		writer.writeAttribute(HTML.ID_ATTR, id, null);
		writer.writeAttribute(HTML.TITLE_ATTR, title, null);
		writer.writeAttribute("i18n:attributes", "title", null);
        if( ! display )
            writer.writeAttribute(HTML.STYLE_ATTR, "display: none", null);
		if( accessKey != null ){
		    writer.writeAttribute(HTML.ACCESSKEY_ATTR, accessKey, null);
		}
			writer.write("&#xA0;");
		writer.endElement(HTML.BUTTON_ELEM);
    }

    static private void writeButton(ResponseWriter writer, String id, String text) throws IOException{
        writer.startElement(HTML.BUTTON_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        	writer.write(text);
        writer.endElement(HTML.BUTTON_ELEM);
	}

    static private void writeOption(ResponseWriter writer, String value, String body) throws IOException{
        writer.startElement(HTML.OPTION_ELEM,null);
        writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        	writer.write(body);
        writer.endElement(HTML.OPTION_ELEM);
    }

    static private void writeOption(ResponseWriter writer, String value, String translate, String body) throws IOException{
        writer.startElement(HTML.OPTION_ELEM,null);
        writer.writeAttribute("xmlns:i18n", "http://xml.zope.org/namespaces/i18n", null);
        writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        writer.writeAttribute("i18n:translate", translate, null);
        	writer.write(body);
        writer.endElement(HTML.OPTION_ELEM);
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        RendererUtils.checkParamValidity(facesContext, uiComponent, InputHtml.class);
		InputHtml editor = (InputHtml) uiComponent;

		Map paramMap = facesContext.getExternalContext()
	            .getRequestParameterMap();
	    String clientId = uiComponent.getClientId(facesContext);
	    if (paramMap.containsKey(clientId)) {
	        //request parameter found, set submittedValue
			String submitedText = (String)paramMap.get(clientId);
			String htmlText = ! useFallback(editor) ?
							submitedText :
							HTMLEncoder.encode(submitedText, true, true);
	        ((EditableValueHolder) uiComponent).setSubmittedValue( htmlText );
	    } else {
	        //request parameter not found, nothing to decode - set submitted value to empty
	        //if the component has not been disabled
	        if(!HtmlRendererUtils.isDisabledOrReadOnly(editor)) {
	            ((EditableValueHolder) uiComponent).setSubmittedValue( RendererUtils.EMPTY_STRING );
	        }
	    }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException {
        RendererUtils.checkParamValidity(facesContext, uiComponent, InputHtml.class);
		InputHtml editor = (InputHtml) uiComponent;
        String submittedDocument = (String) RendererUtils.getConvertedUIOutputValue(facesContext, editor, submittedValue);
        return editor.getValueFromDocument( submittedDocument );
    }
}
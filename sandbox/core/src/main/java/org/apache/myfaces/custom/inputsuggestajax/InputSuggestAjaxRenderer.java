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

package org.apache.myfaces.custom.inputsuggestajax;

import org.apache.myfaces.custom.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.ext.HtmlTextRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.renderkit.html.util.UnicodeEncoder;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.commons.lang.StringUtils;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gerald Müllan
 * @author Martin Marinschek
 * @version $Revision: 177984 $ $Date: 2005-05-23 19:39:37 +0200 (Mon, 23 May 2005) $
 */
public class InputSuggestAjaxRenderer extends HtmlTextRenderer implements AjaxRenderer
{
    private static final int DEFAULT_MAX_SUGGESTED_ITEMS = 200;

   /**
     * Encodes any stand-alone javascript functions that are needed.
     * Uses either the extension filter, or a
     * user-supplied location for the javascript files.
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException
     */
    private void encodeJavascript(FacesContext context, UIComponent component)
                                                                        throws IOException
    {
        String javascriptLocation = (String)component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        String styleLocation = (String)component.getAttributes().get(JSFAttr.STYLE_LOCATION);

        DojoUtils.addMainInclude(context, javascriptLocation, new DojoConfig());
        DojoUtils.addRequire(context, "dojo.widget.*");
        DojoUtils.addRequire(context, "dojo.widget.ComboBox");
        DojoUtils.addRequire(context, "dojo.widget.html.ComboBox");
        DojoUtils.addRequire(context, "dojo.event.topic");

        AddResource addResource = AddResourceFactory.getInstance(context);

        if( StringUtils.isNotBlank( styleLocation ) )
        {
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, styleLocation + "/ajax_suggest.css");
        }
        else
        {
            String theme = ((InputSuggestAjax)component).getLayout();
            if(theme == null)
                theme = "default";
            addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, InputSuggestAjaxRenderer.class, theme + "/ajax_suggest.css");
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, InputSuggestAjax.class);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        encodeJavascript(context,component);

        inputSuggestAjax.getAttributes().put("autocomplete","off");
        
        String oldStyleClass = inputSuggestAjax.getStyleClass();
        inputSuggestAjax.setStyleClass(
        		(oldStyleClass!=null && oldStyleClass.length()>=0 ? oldStyleClass : "")+" myFacesInputSuggestAjax");

        inputSuggestAjax.setStyleClass(oldStyleClass);

        String clientId = component.getClientId(context);
        String actionURL = getActionUrl(context);

        String urlWithValue = context.getExternalContext().encodeActionURL(actionURL+"?affectedAjaxComponent=" + clientId
                              + "&" + clientId+"=");

        ResponseWriter out = context.getResponseWriter();

        if (getChildren(inputSuggestAjax) != null
                && !getChildren(inputSuggestAjax).isEmpty())
        {
            //suggestTable stuff
            out.startElement(HTML.TABLE_ELEM, null);
            out.writeAttribute(HTML.STYLE_ATTR,"border-collapse:collapse;",null);
            out.writeAttribute(HTML.CELLPADDING_ATTR,"0",null);
            out.startElement(HTML.TR_ELEM, null);
            out.startElement(HTML.TD_ELEM, null);

            super.encodeEnd(context, inputSuggestAjax);

            out.endElement(HTML.TR_ELEM);
            out.endElement(HTML.TD_ELEM);

            out.startElement(HTML.TR_ELEM, null);
            out.startElement(HTML.TD_ELEM, null);

            out.startElement(HTML.DIV_ELEM, null);
            if(inputSuggestAjax.getLayout().equals("default"))
            {
                out.writeAttribute(HTML.CLASS_ATTR, "ajaxTablePopup", null);
            }
            if(inputSuggestAjax.getPopupStyleClass()!= null)
            {
                out.writeAttribute(HTML.CLASS_ATTR, inputSuggestAjax.getPopupStyleClass(), null);
            }
            if (inputSuggestAjax.getPopupId() != null)
            {
                out.writeAttribute(HTML.ID_ATTR,inputSuggestAjax.getPopupId(), null);
            }
            else
            {
                out.writeAttribute(HTML.ID_ATTR,
                        component.getClientId(context)+"_auto_complete", null);
            }
            if (inputSuggestAjax.getPopupStyle() != null)
            {
                out.writeAttribute(HTML.STYLE_ATTR, inputSuggestAjax.getPopupStyle(), null);
            }
            else
            {
                out.writeAttribute(HTML.STYLE_ATTR,"display:inline;", null);
            }
            out.endElement(HTML.DIV_ELEM);

            out.endElement(HTML.TR_ELEM);
            out.endElement(HTML.TD_ELEM);
            out.endElement(HTML.TABLE_ELEM);

            out.startElement(HTML.SCRIPT_ELEM, null);
            out.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
            out.write(getAJAXHandlingCode(urlWithValue, clientId).toString());
            out.endElement(HTML.SCRIPT_ELEM);
        }
        else
        {
            //simple suggest stuff
            out.write("<input dojoType=\"combobox\" "
                + "value=\"\" width=\"50px;\" "
                + "dataUrl=\""+ urlWithValue + "%{searchString}\"\n"
                + "mode=\"remote\">");
        }

        //todo:client-side state saving as well
      /*  if (context.getApplication().getStateManager().isSavingStateInClient(context))
        {
            out.writeText("', {\n" +
                          "      method:       'post',\n" +
                          "      asynchronous: true,\n" +
                          "      parameters: '',\n"+
                          "      callback: function(element,entry) {return entry+'&jsf_tree_64='+encodeURIComponent(document.getElementById('jsf_tree_64').value)+'&jsf_state_64='+encodeURIComponent(document.getElementById('jsf_state_64').value)+'&jsf_viewid='+encodeURIComponent(document.getElementById('jsf_viewid').value)}" +
                          "    })",null);            
        }
        else
        {                        
            out.writeText("', {\n" +
                          "      method:       'post',\n" +
                          "      asynchronous: true,\n" +
                          "      parameters: '',\n" +
                          "      callback: function(element,entry) {return entry}" +
                          "    })", null);
        }*/

    }

    private StringBuffer getAJAXHandlingCode(String urlWithValue, String clientId)
    {
        StringBuffer buf = new StringBuffer();

        //doing ajax request and handling the response
        buf.append("var handlerNode = document.getElementById(\"" + clientId + "\");\n"
                 + "dojo.event.connect(handlerNode, \"onkeyup\", function(evt)\n"
                    + "{\n"
                    + "    dojo.io.bind({\n"
                    + "    url: \""+ urlWithValue +"\"+handlerNode.value,\n"
                    + "    handle: function(type, data, evt){\n"
                    + DojoUtils.createDebugStatement("after response")
                    + "        if(type == \"load\"){\n"
                    + "            var popUp = document.getElementById(\"" + clientId+"_auto_complete"+"\");\n"
                    + "            popUp.innerHTML = data;\n"
                    + "        }else if(type == \"error\"){\n"
                    + "            // here, \"data\" is our error object\n"
                    + "            // respond to the error here\n"
                    + "        }else{\n"
                    + "            // other types of events might get passed, handle them here\n"
                    + "        }\n"
                    + "    },\n"
                    + "    mimetype: \"text/plain\"\n"
                    + "});\n"
                + "});\n");

        //if setting the focus outside the input field, popup should not be displayed
        buf.append("dojo.event.connect(document, \"onclick\", function(evt)\n"
                    + "{\n"
                    + "     var popUp = document.getElementById(\"" + clientId+"_auto_complete"+"\");\n"
                    + "     popUp.innerHTML = \"\";\n"
                    + "});\n");

        //puting the values from the choosen row into the fields
        buf.append("function putValueToField(trElem)\n"
                + "{\n"
                + "   for(i=0;i<trElem.childNodes.length;i++)\n"
                + "   {\n"
                + "      var idToPutValue = trElem.childNodes[i].id.substr(11); \n"
                + "      var elemToPutValue = document.getElementById(idToPutValue);\n"
                + "      elemToPutValue.value = trElem.childNodes[i].innerHTML;\n "
                + "   }\n"
                + "}\n");

        return buf;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

    public void encodeAjax(FacesContext context, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(context, uiComponent, InputSuggestAjax.class);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) uiComponent;

        MethodBinding mb = inputSuggestAjax.getSuggestedItemsMethod();
        Collection suggesteds = null;
        int maxSuggestedCount = inputSuggestAjax.getMaxSuggestedItems()!=null
        							? inputSuggestAjax.getMaxSuggestedItems().intValue()
        							: DEFAULT_MAX_SUGGESTED_ITEMS;
        if (inputSuggestAjax.getMaxSuggestedItems()!=null) {
        	try{
	        	suggesteds = (Collection) mb.invoke(context,new Object[]{
	                    AjaxPhaseListener.getValueForComponent(context, uiComponent),
	                    new Integer(maxSuggestedCount)});
        	}catch(MethodNotFoundException dummy){
        		suggesteds = (List) mb.invoke(context,new Object[]{
	                    AjaxPhaseListener.getValueForComponent(context, uiComponent)});
        	}
        } else {
        	try{
	        	suggesteds = (List) mb.invoke(context,new Object[]{
	                    AjaxPhaseListener.getValueForComponent(context, uiComponent)});
        	}catch(MethodNotFoundException dummy){
        		suggesteds = (Collection) mb.invoke(context,new Object[]{
                        AjaxPhaseListener.getValueForComponent(context, uiComponent),
                        new Integer( DEFAULT_MAX_SUGGESTED_ITEMS )});
        	}
        }
        
        StringBuffer buf = new StringBuffer();

        if (getChildren(inputSuggestAjax)!=null
                && !getChildren(inputSuggestAjax).isEmpty())
        {
            buf.append("<table>");

            buf.append(renderTableHeader(inputSuggestAjax.getChildren()));
            buf.append(renderTableBody(inputSuggestAjax.getChildren(), suggesteds,
                                       context, inputSuggestAjax));
            //todo:render footer as well

            buf.append("</table>");
        }
        else
        {
            buf.append("[");

            int suggestedCount=0;

            for (Iterator suggestedItem = suggesteds.iterator() ; suggestedItem.hasNext() ; suggestedCount++)
            {
                if( suggestedCount > maxSuggestedCount )
                    break;

                Object item = suggestedItem.next();

                buf.append("[\"").append(UnicodeEncoder.encode(item.toString())).append("\",\"")
                    .append(UnicodeEncoder.encode(item.toString()).substring(0, 1).toUpperCase()).append("\"],");
            }

            buf.append("];");
        }

        context.getResponseWriter().write(buf.toString());
    }

    private StringBuffer renderTableBody(List columns,
                                         Collection suggesteds,
                                         FacesContext context,
                                         InputSuggestAjax inputSuggestAjax)
    {
        StringBuffer bodyContent = new StringBuffer();
        bodyContent.append("<tbody>");

        for (Iterator suggestedEntry = suggesteds.iterator(); suggestedEntry.hasNext();)
        {
            Object addressEntryObject = suggestedEntry.next();

           bodyContent.append("<tr onmouseover=");
            if(inputSuggestAjax.getColumnHoverClass()!=null)
                bodyContent.append("\"this.className='")
                        .append(inputSuggestAjax.getColumnHoverClass()).append("'\" ");
            else
                bodyContent.append("\"this.className='tableSuggestHover'\" ");

            if(inputSuggestAjax.getColumnOutClass()!=null)
                bodyContent.append("onmouseout=\"this.className='")
                        .append(inputSuggestAjax.getColumnOutClass()).append("'\" ");
            else
                bodyContent.append("onmouseout=\"this.className='tableSuggestOut'\"");

            bodyContent.append("onclick=\"putValueToField(this)\">");

            context.getExternalContext().getRequestMap()
                    .put(inputSuggestAjax.getVar(),addressEntryObject);

            for (Iterator iterColumns = columns.iterator(); iterColumns.hasNext();)
            {
                UIComponent column = (UIComponent)iterColumns.next();

                for (Iterator iterComps = column.getChildren().iterator(); iterComps.hasNext();)
                {
                    UIComponent comp =  (UIComponent) iterComps.next();

                    if (comp instanceof HtmlOutputText)
                    {
                        HtmlOutputText htmlOutputText = (HtmlOutputText) comp;

                           bodyContent.append("<td id=\"putValueTo_")
                                   .append(RendererUtils.getClientId(context,inputSuggestAjax,htmlOutputText.getFor())+"\">")
                                   .append(htmlOutputText.getValue())
                                   .append("</td>");
                        break;
                    }
                }
            }
            context.getExternalContext().getRequestMap().remove(inputSuggestAjax.getVar());
            bodyContent.append("</tr>");
        }

        bodyContent.append("</tbody>");

        return bodyContent;
    }


    private StringBuffer renderTableHeader(List columns)
    {
        StringBuffer headerContent = new StringBuffer();

        headerContent.append("<thead><tr>");

        for (Iterator iterColumns = columns.iterator(); iterColumns.hasNext();)
        {
            UIComponent column = (UIComponent)iterColumns.next();

            if (column instanceof UIColumn)
            {
                UIComponent tableHeader = (UIComponent) column.getFacet("header");

                if (tableHeader!=null &&
                        tableHeader instanceof HtmlOutputText)
                {
                    HtmlOutputText htmlOutputText = (HtmlOutputText) tableHeader;
                    headerContent.append("<th>").append(htmlOutputText.getValue()).append("</th>");
                }
            }
        }

        headerContent.append("</tr></thead>");
        return headerContent;
    }
}

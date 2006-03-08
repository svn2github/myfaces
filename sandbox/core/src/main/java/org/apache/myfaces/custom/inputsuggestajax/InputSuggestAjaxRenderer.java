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

import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;

import org.apache.myfaces.renderkit.html.ext.HtmlTextRenderer;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.UnicodeEncoder;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
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
import java.util.ArrayList;

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
        DojoUtils.addRequire(context, "dojo.widget.Wizard");
        DojoUtils.addRequire(context, "dojo.event.*");
        DojoUtils.addRequire(context, "dojo.string");
        DojoUtils.addRequire(context, "dojo.fx.html");
        DojoUtils.addRequire(context, "dojo.lang");
        DojoUtils.addRequire(context, "dojo.html");
        DojoUtils.addRequire(context, "dojo.style");
        DojoUtils.addRequire(context, "dojo.collections.ArrayList");

        AddResource addResource = AddResourceFactory.getInstance(context);

        if(javascriptLocation != null)
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, javascriptLocation + "/tableSuggest.js");
        }
        else
        {
            addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, InputSuggestAjaxRenderer.class, "tableSuggest.js");
        }

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        if (inputSuggestAjax.getPopupStyleClass() == null)
        {
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
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, InputSuggestAjax.class);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        encodeJavascript(context,component);

       inputSuggestAjax.getAttributes().put("autocomplete","off");

    /*     String oldStyleClass = inputSuggestAjax.getStyleClass();
    inputSuggestAjax.setStyleClass(
            (oldStyleClass!=null && oldStyleClass.length()>=0 ? oldStyleClass : "")+" myFacesInputSuggestAjax");

    inputSuggestAjax.setStyleClass(oldStyleClass);*/

        String clientId = component.getClientId(context);
        String actionURL = getActionUrl(context);

        String urlWithValue = context.getExternalContext().encodeActionURL(actionURL+"?affectedAjaxComponent=" + clientId);

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
            //out.write("<input dojoType=\"subcombobox\" value=\"\" style=\"width:150px;\">");

            out.endElement(HTML.TR_ELEM);
            out.endElement(HTML.TD_ELEM);

            out.startElement(HTML.TR_ELEM, null);
            out.startElement(HTML.TD_ELEM, null);

            out.startElement(HTML.DIV_ELEM, null);
            if(inputSuggestAjax.getPopupStyleClass()!= null)
            {
                out.writeAttribute(HTML.CLASS_ATTR, inputSuggestAjax.getPopupStyleClass(), null);
            }
            else if(inputSuggestAjax.getLayout().equals("default"))
            {
                out.writeAttribute(HTML.CLASS_ATTR, "ajaxTablePopup", null);
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
            out.write(getAJAXHandlingCode(urlWithValue, clientId, inputSuggestAjax).toString());
            out.endElement(HTML.SCRIPT_ELEM);

          /* out.write("<script type=\"text/javascript\">\n                    "
                    + "\tdojo.widget.SubComboBox = function(){\n"
                    + "\t\tdojo.widget.html.ComboBox.call(this);\n"
                    + "\t\tthis.widgetType = \"SubComboBox\";\n"
                    + "\t}\n"
                    + "\n"
                    + "\tdojo.inherits(dojo.widget.SubComboBox, dojo.widget.html.ComboBox);\n"
                    + "\n"
                    + "\tdojo.lang.extend(dojo.widget.SubComboBox, {\n"
                    + "\t\tfillInTemplate: function(args, frag){\n"
                    + "\t\t\tthis.dataProvider = {\n"
                    + "\t\t\t\tstartSearch: function(searchStr, type, ignoreLimit){\n"
                    +DojoUtils.createDebugStatement("starting request")
                     + "       dojo.io.bind ({\n"
                    + "             url: \""+ urlWithValue +"\"+searchStr,\n"
                    + "             handle: function(type, data, evt)"
                    + "             {\n"
                    +                   DojoUtils.createDebugStatement("after response")
                    + "                 if(type == \"load\")\n"
                    + "                 {\n"
                    +                       DojoUtils.createDebugStatement("response successful")
                    +" var popUp = document.getElementById(\"" + clientId+"_auto_complete"+"\");\n"
                    + "                     popUp.innerHTML = data;\n"
                    + "                 }\n"
                    + "                 else if(type == \"error\")\n"
                    + "                 {\n"
                    +                       DojoUtils.createDebugStatement("error during response")
                    + "                     // here, \"data\" is our error object\n"
                    + "                 }\n"
                    + "             },\n"
                    + "             mimetype: \"text/plain\"\n"
                    + "       });\n"
                    + "\t\t\t\t}\n"
                    + "\t\t\t};\n"
                    + "\t\t}\n"
                    + "\t});\n"
                    + "dojo.widget.tags.addParseTreeHandler(\"dojo:subcombobox\");\n"
                    + "</script>");*/
        }
        else
        {
            //simple suggest stuff
            out.write("<input dojoType=\"combobox\" "
                + "value=\"\" style=\"width:150px;\" "
                + "dataUrl=\""+ urlWithValue + "&" + clientId+"=%{searchString}\"\n"
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

    private StringBuffer getAJAXHandlingCode(String urlWithValue, String clientId, InputSuggestAjax inputSuggestAjax)
    {
        StringBuffer buf = new StringBuffer();
        String tableSuggestVar = "tableSuggest"+clientId.substring(1).replace(':','_');

        //doing ajax request and handling the response
        buf.append(   "var "+tableSuggestVar+" = new org_apache_myfaces_TableSuggest();\n"
                    + "dojo.event.connect(dojo.byId(\"" + clientId + "\"), \"onkeyup\", function(evt)\n"
                    + "{\n"
                    + "   if( (evt.keyCode == 13) || (evt.keyCode == 9) ){\n"  //enter,tab
                    + "      document.onclick();\n"
                    + "      return;\n"
                    + "   }\n"
                    + "   var handlerNode = dojo.byId(\"" + clientId + "\");\n"
                    + "   var popUp = dojo.byId(\"" + clientId+"_auto_complete"+"\");\n"
                    + "   var inputValue = handlerNode.value;\n"
                    + "   var url = \""+ urlWithValue +"&" + clientId+"=\"+inputValue;\n"
                    +     DojoUtils.createDebugStatement("onkeyup event occured, length is","inputValue.length")
                    +     DojoUtils.createDebugStatement("value is","inputValue")
                    + "   if(inputValue != \"\" ");
                    if(inputSuggestAjax.getStartRequest()!=null)
                        buf.append("&& inputValue.length >= "+inputSuggestAjax.getStartRequest()+")\n");
                    else buf.append(")");
                    if(inputSuggestAjax.getDelay()!=null)
                        buf.append("dojo.lang.setTimeout("+tableSuggestVar+".handleRequestResponse, "+inputSuggestAjax.getDelay()+", [url, popUp, "+ tableSuggestVar +",evt.keyCode]);\n");
                    else
                        buf.append(""+tableSuggestVar+".handleRequestResponse(url, popUp, "+ tableSuggestVar +",evt.keyCode);\n");
       buf.append("   else document.onclick();\n"
                    + "});\n");

        //if setting the focus outside the input field, popup should not be displayed
        buf.append("dojo.event.connect(document, \"onclick\", function(evt)\n"
                    + "{\n"
                    + "     "+tableSuggestVar+".resetSettings(\"" + clientId+"_auto_complete\");\n"
                    + "});\n");

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
            if (inputSuggestAjax.getMaxSuggestedItems() != null)
            {
                List oneSuggestedTable = new ArrayList();
                List wholeList = new ArrayList();
                wholeList.addAll(suggesteds);

                int j = 0;

                while (j <= wholeList.size()-1)
                {
                    for (int i = 0; i < inputSuggestAjax.getMaxSuggestedItems().intValue(); i++)
                    {
                        Object entry = wholeList.get(j);
                        oneSuggestedTable.add(entry);
                        j++;

                        if(j == wholeList.size())
                            break;
                    }

                    writeOneSuggestList(buf, inputSuggestAjax, oneSuggestedTable, context);
                    oneSuggestedTable.clear();
                }
            }
            else
            {
                writeSuggestList(buf, inputSuggestAjax, suggesteds, context);
            }
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

    private void writeSuggestList(StringBuffer buf,
                                  InputSuggestAjax inputSuggestAjax,
                                  Collection suggesteds,
                                  FacesContext context)
    {
        writeOneSuggestList(buf, inputSuggestAjax, suggesteds, context);
    }

    private void writeOneSuggestList(StringBuffer buf,
                                     InputSuggestAjax inputSuggestAjax,
                                     Collection suggesteds,
                                     FacesContext context)
    {
        buf.append("<table");
        if(inputSuggestAjax.getTableStyleClass()!=null)
            buf.append(" class=\""+inputSuggestAjax.getTableStyleClass()+"\">");
        else
            buf.append(">");

        buf.append(renderTableHeader(inputSuggestAjax.getChildren()));
        buf.append(renderTableBody(inputSuggestAjax, suggesteds,context));

        buf.append("</table>");

        if (inputSuggestAjax.getMaxSuggestedItems() != null)
            buf.append(renderNextPageField(inputSuggestAjax,context));
    }

    private StringBuffer renderTableBody(InputSuggestAjax inputSuggestAjax,
                                         Collection suggesteds,
                                         FacesContext context)
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

            if(inputSuggestAjax.getColumnOutClass()!=null)
                bodyContent.append("onmouseout=\"this.className='")
                        .append(inputSuggestAjax.getColumnOutClass()).append("'\" ");

            bodyContent.append("onclick=\"tableSuggest"+inputSuggestAjax.getClientId(context).substring(1).replace(':','_')+".putValueToField(this)\">");

            context.getExternalContext().getRequestMap()
                    .put(inputSuggestAjax.getVar(),addressEntryObject);

            for (Iterator iterColumns = inputSuggestAjax.getChildren().iterator(); iterColumns.hasNext();)
            {
                UIComponent column = (UIComponent)iterColumns.next();

                for (Iterator iterComps = column.getChildren().iterator(); iterComps.hasNext();)
                {
                    Object comp =  iterComps.next();

                    if (comp instanceof HtmlOutputText)
                    {
                        HtmlOutputText htmlOutputText = (HtmlOutputText) comp;

                        if (htmlOutputText.getLabel()!=null)
                        {
                            bodyContent.append("<td> <span ");
                            if(htmlOutputText.getFor()!=null)
                                bodyContent.append("id=\"putValueTo_"+RendererUtils.getClientId(context, inputSuggestAjax, htmlOutputText.getFor()) + "\"");
                            bodyContent.append(">"+ htmlOutputText.getLabel() + "</span>");

                            if (htmlOutputText.getValue()!=null)
                            {
                                bodyContent.append("<span id=\"putValueTo_")
                                        .append(RendererUtils.getClientId(context, inputSuggestAjax, htmlOutputText.getForValue()) + "\"")
                                        .append(" style=\"display:none;\">" + htmlOutputText.getValue() + "</span>").append("</td>");
                            }
                            else
                            {
                                bodyContent.append("</td>");
                                break;
                            }
                        }
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

    private StringBuffer renderNextPageField(InputSuggestAjax inputSuggestAjax,
                                             FacesContext context)
    {
        StringBuffer content = new StringBuffer();

        content.append("<div onclick=\"tableSuggest"+inputSuggestAjax.getClientId(context).substring(1).replace(':','_')+".nextPage(this)\"");
        if(inputSuggestAjax.getNextPageFieldClass()!=null)
            content.append(" class=\""+ inputSuggestAjax.getNextPageFieldClass() +"\">");
        else
            content.append(">");

        content.append(". . . </div>");

        return content;
    }
}

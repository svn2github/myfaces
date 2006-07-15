/*
 * Copyright 2004-2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.suggestajax.tablesuggestajax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.UIComponentPerspective;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.suggestajax.SuggestAjaxRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.component.ExecuteOnCallback;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gerald Muellan
 *         Date: 25.03.2006
 *         Time: 17:05:38
 */
public class TableSuggestAjaxRenderer extends SuggestAjaxRenderer implements AjaxRenderer
{
    private static final Log log = LogFactory.getLog(TableSuggestAjaxRenderer.class);

    public static final int DEFAULT_START_REQUEST = 0;
    public static final int DEFAULT_BETWEEN_KEY_UP = 1000;
    
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

        DojoUtils.addMainInclude(context, component, javascriptLocation, new DojoConfig());
        DojoUtils.addRequire(context, component, "extensions.FacesIO");
        DojoUtils.addRequire(context, component, "dojo.event.*");
        DojoUtils.addRequire(context, component, "dojo.string");
        DojoUtils.addRequire(context, component, "dojo.fx.html");
        DojoUtils.addRequire(context, component, "dojo.lang");
        DojoUtils.addRequire(context, component, "dojo.html");
        DojoUtils.addRequire(context, component, "dojo.style");
        DojoUtils.addRequire(context, component, "dojo.collections.ArrayList");

        AddResource addResource = AddResourceFactory.getInstance(context);

        if(javascriptLocation != null)
        {
            addResource.addJavaScriptHere(context,  javascriptLocation + "/tableSuggest.js");
        }
        else
        {
            addResource.addJavaScriptHere(context,  TableSuggestAjaxRenderer.class, "tableSuggest.js");
        }

        TableSuggestAjax tableSuggestAjax = (TableSuggestAjax) component;

        if (tableSuggestAjax.getPopupStyleClass() == null)
        {
            if( styleLocation != null )
            {
                addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, styleLocation + "/table_suggest.css");
            }
            else
            {
                String theme = ((TableSuggestAjax)component).getLayout();
                if(theme == null)
                    theme = "default";
                addResource.addStyleSheet(context, AddResource.HEADER_BEGIN, TableSuggestAjaxRenderer.class, theme + "/table_suggest.css");
            }
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, TableSuggestAjax.class);

        TableSuggestAjax tableSuggestAjax = (TableSuggestAjax) component;

        encodeJavascript(context,component);

       tableSuggestAjax.getAttributes().put("autocomplete","off");

        String clientId = component.getClientId(context);
        String actionURL = getActionUrl(context);

        String ajaxUrl = context.getExternalContext().encodeActionURL(actionURL+"?affectedAjaxComponent=" + clientId);

        ResponseWriter out = context.getResponseWriter();

        if (getChildren(tableSuggestAjax) != null
                && !getChildren(tableSuggestAjax).isEmpty())
        {
            //suggestTable stuff
            out.startElement(HTML.TABLE_ELEM, null);
            out.writeAttribute(HTML.STYLE_ATTR,"border-collapse:collapse;",null);
            out.writeAttribute(HTML.CELLPADDING_ATTR,"0",null);
            out.startElement(HTML.TR_ELEM, null);
            out.startElement(HTML.TD_ELEM, null);

            super.encodeEnd(context, tableSuggestAjax);

            out.endElement(HTML.TR_ELEM);
            out.endElement(HTML.TD_ELEM);

            out.startElement(HTML.TR_ELEM, null);
            out.startElement(HTML.TD_ELEM, null);

            out.startElement(HTML.DIV_ELEM, null);
            if(tableSuggestAjax.getPopupStyleClass()!= null)
            {
                out.writeAttribute(HTML.CLASS_ATTR, tableSuggestAjax.getPopupStyleClass(), null);
            }
            else if(tableSuggestAjax.getLayout().equals("default"))
            {
                out.writeAttribute(HTML.CLASS_ATTR, "ajaxTablePopup", null);
            }
            if (tableSuggestAjax.getPopupId() != null)
            {
                out.writeAttribute(HTML.ID_ATTR,tableSuggestAjax.getPopupId(), null);
            }
            else
            {
                out.writeAttribute(HTML.ID_ATTR,
                        component.getClientId(context)+"_auto_complete", null);
            }
            if (tableSuggestAjax.getPopupStyle() != null)
            {
                out.writeAttribute(HTML.STYLE_ATTR, tableSuggestAjax.getPopupStyle(), null);
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

            out.write(getEventHandlingCode(ajaxUrl, clientId, tableSuggestAjax).toString());

            out.endElement(HTML.SCRIPT_ELEM);
        }
    }

    private StringBuffer getEventHandlingCode(String ajaxUrl, String clientId, TableSuggestAjax tableSuggestAjax)
    {
        int betweenKeyUp = 0;
        int startRequest = 0;
        String charset = null;

        if (tableSuggestAjax.getBetweenKeyUp()!=null)
            betweenKeyUp = tableSuggestAjax.getBetweenKeyUp().intValue();
        else
            betweenKeyUp = DEFAULT_BETWEEN_KEY_UP;

        if(tableSuggestAjax.getStartRequest()!=null)
            startRequest = tableSuggestAjax.getStartRequest().intValue();
        else
            startRequest = DEFAULT_START_REQUEST;

        if (tableSuggestAjax.getCharset() != null)
            charset = tableSuggestAjax.getCharset();
        else
            charset = "";
        
        StringBuffer buf = new StringBuffer();
        String tableSuggestVar = "tableSuggest"+clientId.replace(':','_');

        //doing ajax request and handling the response
        buf.append( "var " + tableSuggestVar + " = new org_apache_myfaces_TableSuggest(\""+ ajaxUrl + "\", " 
                   + betweenKeyUp + ", " + startRequest + ", \"" + charset + "\", " + tableSuggestAjax.getAcceptValueToField().toString() + ");\n" 
                   + "dojo.event.connect(dojo.byId(\"" + clientId + "\"), \"onkeyup\", function(evt) { " 
                   + tableSuggestVar + ".decideRequest(evt); });\n"  
                   + "dojo.event.connect(dojo.byId(\"" + clientId + "\"), \"onblur\", function(evt) { " 
                   + tableSuggestVar + ".onBlur(evt); });\n" 
                   + "dojo.event.connect(dojo.byId(\"" + clientId + "\"), \"onfocus\", function(evt) { " 
                   + tableSuggestVar + ".onFocus(evt); });\n");

        //if setting the focus outside the input field, popup should not be displayed
        buf.append("dojo.event.connect(document, \"onclick\", function(evt) { " + tableSuggestVar + ".resetSettings(); });\n");
        return buf;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

    public void encodeAjax(FacesContext context, UIComponent uiComponent)
                                                                    throws IOException
    {
        TableSuggestAjax tableSuggestAjax;

        String clientId = (String)context.getExternalContext()
                                    .getRequestParameterMap().get("affectedAjaxComponent");

        UIViewRoot root = context.getViewRoot();

        UIComponent ajaxComp = root.findComponent(clientId);

        //checking if ajaxComp is inside a dataTable; here needed for getting the correct ids
        if (ajaxComp instanceof UIComponentPerspective)
        {
            UIComponentPerspective componentPerspective = (UIComponentPerspective) ajaxComp;

            tableSuggestAjax = (TableSuggestAjax) componentPerspective.executeOn(context, new ExecuteOnCallback()
            {
                public Object execute(FacesContext facesContext, UIComponent uiComponent)
                {
                    try
                    {
                        renderResponse ( (TableSuggestAjax) uiComponent, facesContext);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    return uiComponent;
                }
            });
        }
        else
        {
            renderResponse ( (TableSuggestAjax) uiComponent, context);
        }
    }

    private void encodeTable(TableSuggestAjax tableSuggestAjax, FacesContext context)
                                                                                throws IOException

    {
        //now, writing the response
        StringBuffer buf = new StringBuffer();

        Collection suggesteds = getSuggestedItems(context, tableSuggestAjax);

        if (getChildren(tableSuggestAjax)!=null
                && !getChildren(tableSuggestAjax).isEmpty())
        {
            if (tableSuggestAjax.getMaxSuggestedItems() != null)
            {
                List oneSuggestedTable = new ArrayList();
                List wholeList = new ArrayList();
                wholeList.addAll(suggesteds);

                int j = 0;

                while (j <= wholeList.size()-1)
                {
                    for (int i = 0; i < tableSuggestAjax.getMaxSuggestedItems().intValue(); i++)
                    {
                        Object entry = wholeList.get(j);
                        oneSuggestedTable.add(entry);
                        j++;

                        if(j == wholeList.size())
                            break;
                    }

                    writeOneSuggestList(buf, oneSuggestedTable, context, tableSuggestAjax);
                    oneSuggestedTable.clear();
                }
            }
            else
            {
                writeSuggestList(buf, suggesteds, context, tableSuggestAjax);
            }
        }

        context.getResponseWriter().write(buf.toString());
    }

    private void writeOneSuggestList(StringBuffer buf,Collection suggesteds,
                                     FacesContext context, TableSuggestAjax tableSuggestAjax)
    {
        writeSuggestList(buf, suggesteds, context, tableSuggestAjax);
    }

    private void writeSuggestList(StringBuffer buf,Collection suggesteds,
                                  FacesContext context, TableSuggestAjax tableSuggestAjax)
    {
        buf.append("<table");
        if(tableSuggestAjax.getTableStyleClass()!=null)
            buf.append(" class=\""+tableSuggestAjax.getTableStyleClass()+"\">");
        else
            buf.append(">");

        buf.append(renderTableHeader(tableSuggestAjax.getChildren()));
        buf.append(renderTableBody(tableSuggestAjax, suggesteds, context));

        buf.append("</table>");

        buf.append(renderNextPageField(tableSuggestAjax, context));
    }

    
    private void encodeAjaxTableTemplate(FacesContext context, 
            TableSuggestAjax tableSuggestAjax) throws IOException
    {
        StringBuffer buf = new StringBuffer();
        buf.append("<table");
        if (tableSuggestAjax.getTableStyleClass() != null)
        {
            buf.append(" class=\"" + tableSuggestAjax.getTableStyleClass() + "\" ");
        }
        
        buf.append(" id=\"" + tableSuggestAjax.getClientId(context) + "_table" + "\" ");
        buf.append(">");
        
        buf.append(renderTableHeader(tableSuggestAjax.getChildren()));
        buf.append("<tbody></tbody>");
        buf.append("</table>");
        
        context.getResponseWriter().write(buf.toString());
                
    }
    
    
    
    //renders the response data in an XML format
    private void renderResponse(TableSuggestAjax tableSuggestAjax,
            FacesContext context) throws IOException
    {
        
        renderColumnHeaders(tableSuggestAjax, context);
        
        StringBuffer response = new StringBuffer();

        Collection suggestedItems = 
            getSuggestedItems(context, tableSuggestAjax);

        if (getChildren(tableSuggestAjax) == null
                || getChildren(tableSuggestAjax).isEmpty())
        {
            return;
        }
            
        for (Iterator it = suggestedItems.iterator(); it.hasNext();) 
        {
            
            Object addressEntryObject = it.next();

            context.getExternalContext().getRequestMap().put(
                    tableSuggestAjax.getVar(), addressEntryObject);
            
            response.append("<item>");

            Iterator columns = tableSuggestAjax.getChildren().iterator();
            while (columns.hasNext()) 
            {
                UIComponent column = (UIComponent) columns.next();

                Iterator columnChildren = column.getChildren().iterator(); 
                while (columnChildren.hasNext())
                {
                    Object component = columnChildren.next();
                    
                    if (!(component instanceof HtmlOutputText)) 
                    {
                        continue;
                    }
                    
                    HtmlOutputText htmlOutputText = 
                        (HtmlOutputText) component;

                    response.append("<column>");
                    
                    //foreign-key field is a simple text field
                    if (htmlOutputText.getFor() != null)
                    {
                        response.append("<forText>");
                        
                        response.append(
                                RendererUtils.getClientId(context,
                                        tableSuggestAjax,
                                        htmlOutputText.getFor()));
                        
                        response.append("</forText>");
                        
                        response.append("<label>");
                        response.append(
                                htmlOutputText.getLabel());
                        response.append("</label>");
                        
                    }
                    //foreign-key field is a combo-box field 
                    else if (htmlOutputText.getForValue() != null)
                    {
                        response.append("<forValue>");
                        
                        response.append(
                                RendererUtils.getClientId(context,
                                        tableSuggestAjax,
                                        htmlOutputText.getForValue()));
                        
                        response.append("</forValue>");
                        
                        response.append("<label>");
                        
                        response.append(
                                htmlOutputText.getLabel());
                        response.append("</label>");
                        
                        response.append("<value>");
                        
                        response.append(
                                htmlOutputText.getValue());
                        response.append("</value>");
                    }
                    response.append("</column>");
                }
            }
            response.append("</item>");
        }
        
        context.getExternalContext().getRequestMap().remove(tableSuggestAjax.getVar());
        System.out.println(response.toString());
        context.getResponseWriter().write(response.toString());
        
    }

    
    
    
    
    private StringBuffer renderTableBody(TableSuggestAjax tableSuggestAjax,
                                         Collection suggesteds,
                                         FacesContext context)
    {
        StringBuffer bodyContent = new StringBuffer();

        String clientId = tableSuggestAjax.getClientId(context);
        String tableSuggestVar = clientId.replace(':','_');

        bodyContent.append("<tbody>");

        int i = 0;

        for (Iterator suggestedEntry = suggesteds.iterator(); suggestedEntry.hasNext();)
        {
           Object addressEntryObject = suggestedEntry.next();

           bodyContent.append("<tr id=\"row"+ (i+1) + clientId +"\" onmouseover=");
            if(tableSuggestAjax.getColumnHoverClass()!=null)
                bodyContent.append("\"this.className='")
                        .append(tableSuggestAjax.getColumnHoverClass()).append("'\" ");

            if(tableSuggestAjax.getColumnOutClass()!=null)
                bodyContent.append("onmouseout=\"this.className='")
                        .append(tableSuggestAjax.getColumnOutClass()).append("'\" ");

            bodyContent.append("onclick=\"tableSuggest"+ tableSuggestVar +".putValueToField(this,'"+ clientId +"')\">");
            i++;

            context.getExternalContext().getRequestMap()
                            .put(tableSuggestAjax.getVar(),addressEntryObject);

            for (Iterator iterColumns = tableSuggestAjax.getChildren().iterator(); iterColumns.hasNext();)
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
                                bodyContent.append("id=\"putValueTo"+RendererUtils.getClientId(context, tableSuggestAjax, htmlOutputText.getFor()) + "\"");
                                
                            bodyContent.append(">" + htmlOutputText.getLabel() + "</span>");
                            if (htmlOutputText.getValue()!=null)
                            {
                                bodyContent.append("<span id=\"putValueTo")
                                        .append(RendererUtils.getClientId(context, tableSuggestAjax, htmlOutputText.getForValue()) + "\"")
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
            context.getExternalContext().getRequestMap().remove(tableSuggestAjax.getVar());
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

    //renders column names in the XML response
    private void renderColumnHeaders(TableSuggestAjax tableSuggestAjax,
            FacesContext context) throws IOException
    {
        StringBuffer columnHeaders = new StringBuffer();

        Iterator it = tableSuggestAjax.getChildren().iterator();
        
        while (it.hasNext())
        {
            UIComponent column = (UIComponent) it.next();

            if (column instanceof UIColumn)
            {
                UIComponent tableHeader = (UIComponent) column.getFacet("header");

                if (tableHeader != null &&
                        tableHeader instanceof HtmlOutputText)
                {
                    columnHeaders.append("<columnHeader>");
                    HtmlOutputText htmlOutputText = (HtmlOutputText) tableHeader;
                    columnHeaders.append(htmlOutputText.getValue());
                    columnHeaders.append("</columnHeader>");
                }
            }
        }
        System.out.println(columnHeaders.toString());
        context.getResponseWriter().write(columnHeaders.toString());
    }
    
    
    
    
    
    
    
    private StringBuffer renderNextPageField(TableSuggestAjax tableSuggestAjax,FacesContext context)
    {
        StringBuffer content = new StringBuffer();

        String tableSuggestVar = tableSuggestAjax.getClientId(context).replace(':','_');

        content.append("<div onclick=\"tableSuggest"+ tableSuggestVar +".nextPage(this)\"");
        if(tableSuggestAjax.getNextPageFieldClass()!=null)
            content.append(" class=\""+ tableSuggestAjax.getNextPageFieldClass() +"\" ");

            String popUpStyle = tableSuggestAjax.getPopupStyle();
            if(popUpStyle!=null && popUpStyle.indexOf("overflow:auto") > -1)
                content.append("style=\"display:none;\"");

        content.append(">. . . </div>");

        return content;
    }

}



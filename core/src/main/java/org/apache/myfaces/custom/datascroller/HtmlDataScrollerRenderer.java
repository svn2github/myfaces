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
package org.apache.myfaces.custom.datascroller;

import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Renderer for the HtmlDataScroller component.
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataScrollerRenderer extends HtmlRenderer
{
    public static final String RENDERER_TYPE = "org.apache.myfaces.DataScroller";

    protected static final String PAGE_NAVIGATION = "idx".intern();

    public boolean getRendersChildren()
    {
        return true;
    }

    /**
     * Determine which datascroller navigation option the user chose (if any),
     * and if they did then queue the appropriate ScrollerActionEvent for
     * later execution.
     */
    public void decode(FacesContext context, UIComponent component)
    {
        RendererUtils.checkParamValidity(context, component, HtmlDataScroller.class);

        Map parameter = context.getExternalContext().getRequestParameterMap();
        String param = (String) parameter.get(calculateHiddenFieldId(context, (HtmlDataScroller)component));
        if (param != null && param.length() > 0)
        {
            if (param.startsWith(PAGE_NAVIGATION))
            {
                // the user chose a specific page# to jump to
                component.queueEvent(new ScrollerActionEvent(component, Integer.parseInt(param
                                .substring(PAGE_NAVIGATION.length(), param.length()))));
            }
            else
            {
                // the user chose first/last/prev/next/fastrewind/fastforward
                component.queueEvent(new ScrollerActionEvent(component, param));
            }
        }
    }

    /**
     * Expose much of the internal state of this component so that UIComponents
     * nested within this component can very flexibly display the component state
     * to the user.
     */
    protected void setVariables(FacesContext facescontext, HtmlDataScroller scroller)
                    throws IOException
    {
        Map requestMap = facescontext.getExternalContext().getRequestMap();

        String pageCountVar = scroller.getPageCountVar();
        if (pageCountVar != null)
        {
            int pageCount = scroller.getPageCount();
            requestMap.put(pageCountVar, new Integer(pageCount));
        }
        String pageIndexVar = scroller.getPageIndexVar();
        if (pageIndexVar != null)
        {
            int pageIndex = (scroller.getRowCount() > 0)? scroller.getPageIndex() : 0;
            requestMap.put(pageIndexVar, new Integer(pageIndex));
        }
        String rowsCountVar = scroller.getRowsCountVar();
        if (rowsCountVar != null)
        {
            int rowsCount = scroller.getRowCount();
            requestMap.put(rowsCountVar, new Integer(rowsCount));
        }
        String displayedRowsCountVar = scroller.getDisplayedRowsCountVar();
        if (displayedRowsCountVar != null)
        {
            int displayedRowsCount = scroller.getRows();
            int max = scroller.getRowCount() - scroller.getFirstRow();
            if (displayedRowsCount > max)
                displayedRowsCount = max;
            requestMap.put(displayedRowsCountVar, new Integer(displayedRowsCount));
        }
        String firstRowIndexVar = scroller.getFirstRowIndexVar();
        if (firstRowIndexVar != null)
        {
            int firstRowIndex = (scroller.getRowCount() > 0)? scroller.getFirstRow() + 1 : 0;
            requestMap.put(firstRowIndexVar, new Integer(firstRowIndex));
        }
        String lastRowIndexVar = scroller.getLastRowIndexVar();
        if (lastRowIndexVar != null)
        {
            int lastRowIndex = scroller.getFirstRow() + scroller.getRows();
            int count = scroller.getRowCount();
            if (lastRowIndex > count)
                lastRowIndex = count;
            requestMap.put(lastRowIndexVar, new Integer(lastRowIndex));
        }
    }

    public void removeVariables(FacesContext facescontext, HtmlDataScroller scroller)
                    throws IOException
    {
        Map requestMap = facescontext.getExternalContext().getRequestMap();

        String pageCountVar = scroller.getPageCountVar();
        if (pageCountVar != null)
        {
            requestMap.remove(pageCountVar);
        }
        String pageIndexVar = scroller.getPageIndexVar();
        if (pageIndexVar != null)
        {
            requestMap.remove(pageIndexVar);
        }
        String rowsCountVar = scroller.getRowsCountVar();
        if (rowsCountVar != null)
        {
            requestMap.remove(rowsCountVar);
        }
        String displayedRowsCountVar = scroller.getDisplayedRowsCountVar();
        if (displayedRowsCountVar != null)
        {
            requestMap.remove(displayedRowsCountVar);
        }
        String firstRowIndexVar = scroller.getFirstRowIndexVar();
        if (firstRowIndexVar != null)
        {
            requestMap.remove(firstRowIndexVar);
        }
        String lastRowIndexVar = scroller.getLastRowIndexVar();
        if (lastRowIndexVar != null)
        {
            requestMap.remove(lastRowIndexVar);
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        super.encodeBegin(facesContext, uiComponent);

        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataScroller.class);

        HtmlDataScroller scroller = (HtmlDataScroller) uiComponent;

        setVariables(facesContext, scroller);
    }

    public void encodeChildren(FacesContext facescontext, UIComponent uicomponent)
                    throws IOException
    {
        RendererUtils.checkParamValidity(facescontext, uicomponent, HtmlDataScroller.class);

        RendererUtils.renderChildren(facescontext, uicomponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataScroller.class);

        HtmlDataScroller scroller = (HtmlDataScroller) uiComponent;

        if (scroller.getUIData() == null)
        {
            return;
        }

        renderSubmitFunction(facesContext, scroller);
        renderHiddenField(facesContext, scroller);
        renderInvisibleSubmitButton(facesContext, scroller);

        renderScroller(facesContext, scroller);
        removeVariables(facesContext, scroller);
    }

    protected void renderSubmitFunction(FacesContext facesContext, HtmlDataScroller scroller)
        throws IOException {
        HtmlRendererUtils.ScriptContext scriptContext = new HtmlRendererUtils.ScriptContext();
        scriptContext.append("function ");
        scriptContext.append(calculateSubmitFunctionName(facesContext, scroller));
        scriptContext.append("(paramValue) {").prettyLine();
        scriptContext.increaseIndent();

        scriptContext.append("var hidden = document.getElementById('");
        scriptContext.append(calculateHiddenFieldId(facesContext, scroller));
        scriptContext.append("');");
        scriptContext.prettyLine();
        scriptContext.append("hidden.value = paramValue;");
        scriptContext.prettyLine();

        scriptContext.append("var submitBtn = document.getElementById('");
        scriptContext.append(calculateInvisibleButtonId(facesContext, scroller));
        scriptContext.append("');");
        scriptContext.prettyLine();
        scriptContext.append("submitBtn.click()");
        scriptContext.prettyLine();

        scriptContext.decreaseIndent();
        scriptContext.append("}");

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.SCRIPT_ELEM, scroller);
        writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);
        writer.writeText(scriptContext.toString(), null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    protected String calculateSubmitFunctionName(FacesContext facesContext, HtmlDataScroller scroller) {
        String clientId = scroller.getClientId(facesContext);
        return clientId.replaceAll(":", "_")+"_submit";
    }

    protected void renderHiddenField(FacesContext facesContext, HtmlDataScroller scroller)
        throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, scroller);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
        String id = calculateHiddenFieldId(facesContext, scroller);
        writer.writeAttribute(HTML.ID_ATTR, id, null);
        writer.writeAttribute(HTML.NAME_ATTR, id, null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    protected String calculateHiddenFieldId(FacesContext facesContext, HtmlDataScroller scroller) {
        return scroller.getClientId(facesContext)+"_hidden";
    }

    protected void renderInvisibleSubmitButton(FacesContext facesContext, HtmlDataScroller scroller)
        throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, scroller);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_SUBMIT, null);
        writer.writeAttribute(HTML.ID_ATTR, calculateInvisibleButtonId(facesContext, scroller), null);
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
        writer.endElement(HTML.INPUT_ELEM);
    }

    protected String calculateInvisibleButtonId(FacesContext facesContext, HtmlDataScroller scroller) {
        return scroller.getId()+"_submitButton";
    }

    protected void renderScroller(FacesContext facesContext, HtmlDataScroller scroller)
                    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        if (!scroller.isRenderFacetsIfSinglePage() && scroller.getPageCount() <= 1)
            return;

        if (scroller.getFirst() == null && scroller.getFastRewind() == null
            && scroller.getPrevious() == null && !scroller.isPaginator()
            && scroller.getNext() == null && scroller.getFastForward() == null
            && scroller.getLast() == null)
            return;

        writeScrollerStart(writer, scroller);
        String styleClass = scroller.getStyleClass();
        if (styleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, JSFAttr.STYLE_CLASS_ATTR);
        }
        String style = scroller.getStyle();
        if (style != null)
        {
            writer.writeAttribute(HTML.STYLE_ATTR, style, JSFAttr.STYLE_ATTR);
        }
        writeScrollerRowStart(writer, scroller);

        UIComponent facetComp = scroller.getFirst();
        if (facetComp != null)
        {
            writeScrollerElementStart(writer, scroller);
            writeStyleClass("firstStyleClass", scroller.getFirstStyleClass(), writer);
            renderFacet(facesContext, scroller, facetComp, HtmlDataScroller.FACET_FIRST);
            writeScrollerElementEnd(writer, scroller);
        }
        facetComp = scroller.getFastRewind();
        if (facetComp != null)
        {
            writeScrollerElementStart(writer, scroller);
            writeStyleClass("fastrStyleClass", scroller.getFastrStyleClass(), writer);
            renderFacet(facesContext, scroller, facetComp, HtmlDataScroller.FACET_FAST_REWIND);
            writeScrollerElementEnd(writer, scroller);
        }
        facetComp = scroller.getPrevious();
        if (facetComp != null)
        {
            writeScrollerElementStart(writer, scroller);
            writeStyleClass("previous", scroller.getPreviousStyleClass(), writer);
            renderFacet(facesContext, scroller, facetComp, HtmlDataScroller.FACET_PREVIOUS);
            writeScrollerElementEnd(writer, scroller);
        }
        if (scroller.isPaginator())
        {
            writeScrollerElementStart(writer, scroller);
            renderPaginator(facesContext, scroller);
            writeScrollerElementEnd(writer, scroller);
        }
        facetComp = scroller.getNext();
        if (facetComp != null)
        {
            writeScrollerElementStart(writer, scroller);
            writeStyleClass("next", scroller.getNextStyleClass(), writer);
            renderFacet(facesContext, scroller, facetComp, HtmlDataScroller.FACET_NEXT);
            writeScrollerElementEnd(writer, scroller);
        }
        facetComp = scroller.getFastForward();
        if (facetComp != null)
        {
            writeScrollerElementStart(writer, scroller);
            writeStyleClass("fastf", scroller.getFastfStyleClass(), writer);
            renderFacet(facesContext, scroller, facetComp, HtmlDataScroller.FACET_FAST_FORWARD);
            writeScrollerElementEnd(writer, scroller);
        }
        facetComp = scroller.getLast();
        if (facetComp != null)
        {
            writeScrollerElementStart(writer, scroller);
            writeStyleClass("last", scroller.getLastStyleClass(), writer);
            renderFacet(facesContext, scroller, facetComp, HtmlDataScroller.FACET_LAST);
            writeScrollerElementEnd(writer, scroller);
        }

        writeScrollerRowEnd(writer, scroller);
        writeScrollerEnd(writer, scroller);
    }

    private void writeStyleClass(String jsfAttrName, String styleClass, ResponseWriter writer) throws IOException {
        if (styleClass != null)
        {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, jsfAttrName);
        }
    }

    private boolean isListLayout(HtmlDataScroller scroller) {
        return scroller.isListLayout();
    }

    protected void writeScrollerEnd(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.endElement(isListLayout(scroller)?HTML.UL_ELEM:HTML.TABLE_ELEM);
    }

    protected void writeScrollerRowEnd(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        if(!isListLayout(scroller)) writer.endElement(HTML.TR_ELEM);
    }

    protected void writeScrollerElementEnd(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.endElement(isListLayout(scroller)?HTML.LI_ELEM:HTML.TD_ELEM);
    }

    protected void writeScrollerElementStart(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.startElement(isListLayout(scroller)?HTML.LI_ELEM:HTML.TD_ELEM, scroller);
    }

    protected void writeScrollerRowStart(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        if(!isListLayout(scroller)) writer.startElement(HTML.TR_ELEM, scroller);
    }

    protected void writeScrollerStart(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.startElement(isListLayout(scroller)?HTML.UL_ELEM:HTML.TABLE_ELEM, scroller);
    }


    protected void renderFacet(FacesContext facesContext, HtmlDataScroller scroller,
                               UIComponent facetComp, String facetName) throws IOException
    {
        renderLinkStart(facesContext, scroller, facetName);

        facetComp.encodeBegin(facesContext);
        if (facetComp.getRendersChildren())
            facetComp.encodeChildren(facesContext);
        facetComp.encodeEnd(facesContext);

        renderLinkEnd(facesContext);
    }

    protected void renderLinkStart(FacesContext facesContext, HtmlDataScroller scroller,
                               String paramValue) throws IOException
    {
        String onclick = scroller.getOnclick();
        String ondblclick = scroller.getOndblclick();

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.ANCHOR_ELEM, scroller);
        writer.writeAttribute(HTML.HREF_ATTR, "#", null);

        StringBuffer onclickJavascript = new StringBuffer();
        if (onclick != null) {
            onclickJavascript.append(onclick).append(";");
        }
        onclickJavascript.append(calculateSubmitFunctionName(facesContext, scroller));
        onclickJavascript.append("('"+paramValue+"')");

        writer.writeAttribute(HTML.ONCLICK_ATTR, onclickJavascript.toString(), null);

        if (ondblclick != null) {
            writer.writeAttribute(HTML.ONDBLCLICK_ATTR, ondblclick, null);
        }
    }

    protected void renderLinkEnd(FacesContext facesContext) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.ANCHOR_ELEM);
    }

    

    /**
     * The "paginator" is a sequence of page numbers which the user can click
     * on to leap directly to a specific page of data.
     */
    protected void renderPaginator(FacesContext facesContext, HtmlDataScroller scroller)
                    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        int maxPages = scroller.getPaginatorMaxPages();
        if (maxPages <= 1)
        {
            maxPages = 2;
        }
        int pageCount = scroller.getPageCount();
        if (pageCount <= 1)
        {
            return;
        }
        int pageIndex = scroller.getPageIndex();
        int delta = maxPages / 2;

        int pages;
        int start;
        if (pageCount > maxPages && pageIndex > delta)
        {
            pages = maxPages;
            start = pageIndex - pages / 2 - 1;
            if (start + pages > pageCount)
            {
                start = pageCount - pages;
            }
        }
        else
        {
            pages = pageCount < maxPages ? pageCount : maxPages;
            start = 0;
        }

        if(!scroller.isSingleElementLayout())
        {
            writePaginatorStart(writer, scroller);

            String styleClass = scroller.getPaginatorTableClass();
            if (styleClass != null)
            {
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, "paginatorTableClass");
            }
            String style = scroller.getPaginatorTableStyle();
            if (style != null)
            {
                writer.writeAttribute(HTML.STYLE_ATTR, style, "paginatorTableStyle");
            }

            writePaginatorRowStart(writer, scroller);
        }

        String onclick = scroller.getOnclick();
   	    String ondblclick = scroller.getOndblclick();
        
        for (int i = start, size = start + pages; i < size; i++)
        {
            int idx = i + 1;
            writePaginatorElementStart(writer, scroller);
            String cStyleClass;
            String cStyle;
            if (idx == pageIndex)
            {
                cStyleClass = scroller.getPaginatorActiveColumnClass();
                cStyle = scroller.getPaginatorActiveColumnStyle();
            }
            else
            {
                cStyleClass = scroller.getPaginatorColumnClass();
                cStyle = scroller.getPaginatorColumnStyle();
            }
            if (cStyleClass != null)
            {
                writer.writeAttribute(HTML.CLASS_ATTR, cStyleClass, idx==pageIndex?"paginatorActiveColumnClass":"paginatorColumnClass");
            }
            if (cStyle != null)
            {
                writer.writeAttribute(HTML.STYLE_ATTR, cStyle, idx==pageIndex?"paginatorActiveColumnStyle":"paginatorColumnStyle");
            }

            if(idx == pageIndex && !scroller.isPaginatorRenderLinkForActive())
            {
                writer.write(Integer.toString(idx));
            }
            else
            {
                renderLinkStart(facesContext, scroller, PAGE_NAVIGATION+Integer.toString(idx));
                writer.write(Integer.toString(idx));
                renderLinkEnd(facesContext);
            }

            writePaginatorElementEnd(writer, scroller);
        }

        if(!scroller.isSingleElementLayout())
        {
            writePaginatorRowEnd(writer, scroller);
            writePaginatorEnd(writer, scroller);
        }
    }

    protected void writePaginatorEnd(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.endElement(isListLayout(scroller)?HTML.UL_ELEM:HTML.TABLE_ELEM);
    }

    protected void writePaginatorRowEnd(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        if(!isListLayout(scroller)) writer.endElement(HTML.TR_ELEM);
    }

    protected void writePaginatorElementEnd(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.endElement(isListLayout(scroller)?HTML.LI_ELEM:HTML.TD_ELEM);
    }

    protected void writePaginatorElementStart(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.startElement(isListLayout(scroller)?HTML.LI_ELEM:HTML.TD_ELEM, scroller);
    }

    protected void writePaginatorRowStart(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        if(!isListLayout(scroller)) writer.startElement(HTML.TR_ELEM, scroller);
    }

    protected void writePaginatorStart(ResponseWriter writer, HtmlDataScroller scroller) throws IOException {
        writer.startElement(isListLayout(scroller)?HTML.UL_ELEM:HTML.TABLE_ELEM, scroller);
    }

    protected String getParameterValue(FacesContext facesContext, HtmlDataScroller scroller,
                                      String text, int pageIndex) {
        String id = HtmlDataScrollerRenderer.PAGE_NAVIGATION + Integer.toString(pageIndex);
        return id;
    }

    protected HtmlCommandLink getLink(FacesContext facesContext, HtmlDataScroller scroller,
                                      String text, int pageIndex)
    {
        String id = HtmlDataScrollerRenderer.PAGE_NAVIGATION + Integer.toString(pageIndex);
        Application application = facesContext.getApplication();

        HtmlCommandLink link = (HtmlCommandLink) application
                        .createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link.setId(scroller.getId() + id);
        link.setTransient(true);
        UIParameter parameter = (UIParameter) application
                        .createComponent(UIParameter.COMPONENT_TYPE);
        parameter.setId(scroller.getId() + id + "_param");
        parameter.setTransient(true);
        parameter.setName(scroller.getClientId(facesContext));
        parameter.setValue(id);
        List children = link.getChildren();
        children.add(parameter);
        if (text != null)
        {
            HtmlOutputText uiText = (HtmlOutputText) application
                            .createComponent(HtmlOutputText.COMPONENT_TYPE);
            uiText.setTransient(true);
            uiText.setValue(text);
            children.add(uiText);
        }
        scroller.getChildren().add(link);
        return link;
    }

    protected HtmlCommandLink getLink(FacesContext facesContext, HtmlDataScroller scroller,
                                      String facetName)
    {
        Application application = facesContext.getApplication();

        HtmlCommandLink link = (HtmlCommandLink) application
                        .createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link.setId(scroller.getId() + facetName);
        link.setTransient(true);
        UIParameter parameter = (UIParameter) application
                        .createComponent(UIParameter.COMPONENT_TYPE);
        parameter.setId(scroller.getId() + facetName + "_param");
        parameter.setTransient(true);
        parameter.setName(scroller.getClientId(facesContext));
        parameter.setValue(facetName);
        System.out.println("\t\tWith param: id: "+parameter.getId()+" name:" +parameter.getName()+" / "+parameter.getValue());
        List children = link.getChildren();
        children.add(parameter);
        scroller.getChildren().add(link);
        return link;
    }
}

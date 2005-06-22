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
package org.apache.myfaces.custom.datascroller;

import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log: HtmlDataScrollerRenderer.java,v $
 * Revision 1.19  2005/01/04 01:42:23  svieujot
 * Bugfix for last page.
 *
 * Revision 1.18  2005/01/04 00:28:07  svieujot
 * dataScroller, add rowsCountVar, displayedRowsCountVar, firstRowIndexVar and lastRowIndexVar attributes.
 *
 * Revision 1.17  2004/12/18 16:31:21  tomsp
 * fixed issue MYFACES-1
 *
 * Revision 1.16  2004/10/13 11:50:57  matze
 * renamed packages to org.apache
 *
 * Revision 1.15  2004/09/02 08:57:17  manolito
 * missing setTransient
 *
 * Revision 1.14  2004/08/25 16:02:12  manolito
 * Prevent division by zero in getPageIndex
 *
 */
public class HtmlDataScrollerRenderer
    extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlDataScrollerRenderer.class);

    protected static final String FACET_FIRST         = "first".intern();
    protected static final String FACET_PREVIOUS      = "previous".intern();
    protected static final String FACET_NEXT          = "next".intern();
    protected static final String FACET_LAST          = "last".intern();
    protected static final String FACET_FAST_FORWARD  = "fastf".intern();
    protected static final String FACET_FAST_REWIND   = "fastr".intern();
    protected static final String PAGE_NAVIGATION     = "idx".intern();

    public static final String RENDERER_TYPE = "org.apache.myfaces.DataScroller";

    public boolean getRendersChildren()
    {
        return true;
    }

    public void decode(FacesContext context, UIComponent component)
    {
        RendererUtils.checkParamValidity(context, component, HtmlDataScroller.class);

        HtmlDataScroller scroller = (HtmlDataScroller)component;

        UIData uiData = findUIData(scroller);
        if (uiData == null)
        {
            return;
        }

        Map parameter = context.getExternalContext().getRequestParameterMap();
        String param = (String)parameter.get(component.getClientId(context));
        if (param != null)
        {
            if (param.equals(FACET_FIRST))
            {
                uiData.setFirst(0);
            }
            else if (param.equals(FACET_PREVIOUS))
            {
                int previous = uiData.getFirst() - uiData.getRows();
                if (previous >= 0)
                    uiData.setFirst(previous);
            }
            else if (param.equals(FACET_NEXT))
            {
                int next = uiData.getFirst() + uiData.getRows();
                if (next < uiData.getRowCount())
                    uiData.setFirst(next);
            }
            else if (param.equals(FACET_FAST_FORWARD))
            {
                int fastStep = scroller.getFastStep();
                if (fastStep <= 0)
                    fastStep = 1;
                int next = uiData.getFirst() + uiData.getRows() * fastStep;
                int rowcount = uiData.getRowCount();
                if (next > rowcount)
                     next = (rowcount - 1) - ((rowcount - 1) % uiData.getRows());
                uiData.setFirst(next);
            }
            else if (param.equals(FACET_FAST_REWIND))
            {
                int fastStep = scroller.getFastStep();
                if (fastStep <= 0)
                    fastStep = 1;
                int previous = uiData.getFirst() - uiData.getRows() * fastStep;
                if (previous < 0)
                    previous = 0;
                uiData.setFirst(previous);
            }
            else if (param.equals(FACET_LAST))
            {
                int rowcount = uiData.getRowCount();
                int rows = uiData.getRows();
                int delta = rowcount % rows;
                int first = delta > 0 && delta < rows ? rowcount - delta : rowcount - rows;
                if (first >= 0)
                {
                    uiData.setFirst(first);
                }
                else
                {
                    uiData.setFirst(0);
                }
            }
            else if (param.startsWith(PAGE_NAVIGATION))
            {
                int index = Integer.parseInt(param.substring(PAGE_NAVIGATION.length(), param.length()));
                int pageCount = getPageCount(uiData);
                if (index > pageCount)
                {
                    index = pageCount;
                }
                else if (index <= 0)
                {
                    index = 1;
                }
                uiData.setFirst(uiData.getRows() * (index - 1));
            }
        }
    }

	protected void setVariables(FacesContext facescontext, HtmlDataScroller scroller) throws IOException
    {
        UIData uiData = findUIData(scroller);
        if (uiData == null)
        {
            return;
        }
		
		Map requestMap = facescontext.getExternalContext().getRequestMap();
		
        String pageCountVar = scroller.getPageCountVar();
        if (pageCountVar != null)
        {
            int pageCount = getPageCount(uiData);
            requestMap.put(pageCountVar, new Integer(pageCount));
        }
        String pageIndexVar = scroller.getPageIndexVar();
        if (pageIndexVar != null)
        {
            int pageIndex = getPageIndex(uiData);
            requestMap.put(pageIndexVar, new Integer(pageIndex));
        }
        String rowsCountVar = scroller.getRowsCountVar();
        if (rowsCountVar != null)
        {
            int rowsCount = uiData.getRowCount();
            requestMap.put(rowsCountVar, new Integer(rowsCount));
        }
        String displayedRowsCountVar = scroller.getDisplayedRowsCountVar();
        if (displayedRowsCountVar != null)
        {
            int displayedRowsCount = uiData.getRows();
            int max = uiData.getRowCount()-uiData.getFirst();
            if( displayedRowsCount > max )
                displayedRowsCount = max;
            requestMap.put(displayedRowsCountVar, new Integer(displayedRowsCount));
        }
        String firstRowIndexVar = scroller.getFirstRowIndexVar();
        if (firstRowIndexVar != null)
        {
            int firstRowIndex = uiData.getFirst()+1;
            requestMap.put(firstRowIndexVar, new Integer(firstRowIndex));
        }
        String lastRowIndexVar = scroller.getLastRowIndexVar();
        if (lastRowIndexVar != null)
        {
            int lastRowIndex = uiData.getFirst()+uiData.getRows();
            int count = uiData.getRowCount();
            if( lastRowIndex > count )
                lastRowIndex = count;
            requestMap.put(lastRowIndexVar, new Integer(lastRowIndex));
        }
    }
	
    public void removeVariables(FacesContext facescontext, HtmlDataScroller scroller) throws IOException
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

	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		super.encodeBegin(facesContext, uiComponent);
		
		RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataScroller.class);
		
		HtmlDataScroller scroller = (HtmlDataScroller)uiComponent;
		
		setVariables(facesContext, scroller);
	}

	public void encodeChildren(FacesContext facescontext, UIComponent uicomponent) throws IOException
    {
        RendererUtils.checkParamValidity(facescontext, uicomponent, HtmlDataScroller.class);

        RendererUtils.renderChildren(facescontext, uicomponent);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException{
		RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlDataScroller.class);
		
		HtmlDataScroller scroller = (HtmlDataScroller)uiComponent;

        UIData uiData = findUIData(scroller);
        if (uiData == null)
        {
            return;
        }

		renderPaginator(uiData, facesContext, scroller);
		removeVariables(facesContext, scroller);
    }
	
	public void renderPaginator(UIData uiData, FacesContext facesContext, HtmlDataScroller scroller) throws IOException
    {
		ResponseWriter writer = facesContext.getResponseWriter();

		if (!scroller.isRenderFacetsIfSinglePage() && getPageCount(uiData)<=1)
			return;

        writer.startElement("table", scroller);
        String styleClass = scroller.getStyleClass();
        if (styleClass != null)
        {
            writer.writeAttribute("class", styleClass, null);
        }
        String style = scroller.getStyle();
        if (style != null)
        {
            writer.writeAttribute("style", style, null);
        }
        writer.startElement("tr", scroller);

        UIComponent facetComp = scroller.getFirst();
        if (facetComp != null)
        {
            writer.startElement("td", scroller);
            renderFacet(facesContext, scroller, facetComp, FACET_FIRST);
            writer.endElement("td");
        }
        facetComp = scroller.getFastRewind();
        if (facetComp != null)
        {
            writer.startElement("td", scroller);
            renderFacet(facesContext, scroller, facetComp, FACET_FAST_REWIND);
            writer.endElement("td");
        }
        facetComp = scroller.getPrevious();
        if (facetComp != null)
        {
            writer.startElement("td", scroller);
            renderFacet(facesContext, scroller, facetComp, FACET_PREVIOUS);
            writer.endElement("td");
        }
        if (scroller.isPaginator())
        {
            writer.startElement("td", scroller);
            renderPaginator(facesContext, scroller, uiData);
            writer.endElement("td");
        }
        facetComp = scroller.getNext();
        if (facetComp != null)
        {
            writer.startElement("td", scroller);
            renderFacet(facesContext, scroller, facetComp, FACET_NEXT);
            writer.endElement("td");
        }
        facetComp = scroller.getFastForward();
        if (facetComp != null)
        {
            writer.startElement("td", scroller);
            renderFacet(facesContext, scroller, facetComp, FACET_FAST_FORWARD);
            writer.endElement("td");
        }
        facetComp = scroller.getLast();
        if (facetComp != null)
        {
            writer.startElement("td", scroller);
            renderFacet(facesContext, scroller, facetComp, FACET_LAST);
            writer.endElement("td");
        }

        writer.endElement("tr");
        writer.endElement("table");
    }

    private void renderFacet(FacesContext facesContext,
                             HtmlDataScroller scroller,
                             UIComponent facetComp,
                             String facetName)
        throws IOException
    {
        UIComponent link = getLink(facesContext, scroller, facetComp, facetName);
        link.encodeBegin(facesContext);
        facetComp.encodeBegin(facesContext);
        if (facetComp.getRendersChildren())
            facetComp.encodeChildren(facesContext);
        facetComp.encodeEnd(facesContext);
        link.encodeEnd(facesContext);
    }
    
    protected void renderPaginator(FacesContext facesContext,
                                   HtmlDataScroller scroller,
                                   UIData uiData)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        int maxPages = scroller.getPaginatorMaxPages();
        if (maxPages <= 1)
        {
            maxPages = 2;
        }
        int pageCount = getPageCount(uiData);
        if (pageCount <= 1)
        {
            return;
        }
        int pageIndex = getPageIndex(uiData);
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

        writer.startElement("table", scroller);

        String styleClass = scroller.getPaginatorTableClass();
        if (styleClass != null)
        {
            writer.writeAttribute("class", styleClass, null);
        }
        String style = scroller.getPaginatorTableStyle();
        if (style != null)
        {
            writer.writeAttribute("style", style, null);
        }

        writer.startElement("tr", scroller);

        for (int i = start, size = start + pages; i < size; i++)
        {
            int idx = i + 1;
            writer.startElement("td", scroller);
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
                writer.writeAttribute("class", cStyleClass, null);
            }
            if (cStyle != null)
            {
                writer.writeAttribute("style", cStyle, null);
            }

            HtmlCommandLink link = getLink(facesContext, scroller, Integer.toString(idx), idx);
            link.encodeBegin(facesContext);
            link.encodeChildren(facesContext);
            link.encodeEnd(facesContext);

            writer.endElement("td");
        }

        writer.endElement("tr");
        writer.endElement("table");
    }

    protected HtmlCommandLink getLink(FacesContext facesContext,
                                      HtmlDataScroller scroller,
                                      String text,
                                      int pageIndex)
    {
        String id = PAGE_NAVIGATION + Integer.toString(pageIndex);
        Application application = facesContext.getApplication();

        HtmlCommandLink link =
            (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link.setId(scroller.getId() + id);
        link.setTransient(true);
        UIParameter parameter
            = (UIParameter)application.createComponent(UIParameter.COMPONENT_TYPE);
        parameter.setId(scroller.getId() + id + "_param");
        parameter.setTransient(true);
        parameter.setName(scroller.getClientId(facesContext));
        parameter.setValue(id);
        List children = link.getChildren();
        children.add(parameter);
        if (text != null)
        {
            HtmlOutputText uiText =
                (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
            uiText.setTransient(true);
            uiText.setValue(text);
            children.add(uiText);
        }
        scroller.getChildren().add(link);
        return link;
    }

    protected HtmlCommandLink getLink(FacesContext facesContext,
                                      HtmlDataScroller scroller,
                                      UIComponent facetComp,
                                      String facetName)
    {
        Application application = facesContext.getApplication();

        HtmlCommandLink link
                = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
        link.setId(scroller.getId() + facetName);
        link.setTransient(true);
        UIParameter parameter
                = (UIParameter)application.createComponent(UIParameter.COMPONENT_TYPE);
        parameter.setId(scroller.getId() + facetName + "_param");
        parameter.setTransient(true);
        parameter.setName(scroller.getClientId(facesContext));
        parameter.setValue(facetName);
        List children = link.getChildren();
        children.add(parameter);
        if (facetComp != null)
            children.add(facetComp);
        // dirty, cause facet-comp is now child from link & scroller!
        scroller.getChildren().add(link);
        return link;
    }

    protected int getPageIndex(UIData uiData)
    {
        int rows = uiData.getRows();
        int pageIndex;
        if (rows > 0)
        {
            pageIndex = uiData.getFirst() / rows + 1;
        }
        else
        {
            log.warn("DataTable " + uiData.getClientId(FacesContext.getCurrentInstance()) + " has invalid rows attribute.");
            pageIndex = 0;
        }
        if (uiData.getFirst() % rows > 0)
        {
            pageIndex++;
        }
        return pageIndex;
    }

    protected int getPageCount(UIData uiData)
    {
        int rows = uiData.getRows();
        int pageCount;
        if (rows > 0)
        {
            pageCount = rows <= 0 ? 1 : uiData.getRowCount() / rows;
            if (uiData.getRowCount() % rows > 0)
            {
                pageCount++;
            }
        }
        else
        {
            rows = 1;
            pageCount = 1;
        }
        return pageCount;
    }

    protected UIData findUIData(HtmlDataScroller scroller)
    {
        String forStr = scroller.getFor();
        UIComponent forComp;
        if (forStr == null)
        {
            // DataScroller may be a child of uiData
            forComp = scroller.getParent();
        }
        else
        {
            forComp = scroller.findComponent(scroller.getFor());
            if (forComp == null)
            {
                log.warn("could not find UIData referenced by attribute dataScroller@for = '" + scroller.getFor() + "'");
            }
        }
        if (!(forComp instanceof UIData))
        {
            throw new IllegalArgumentException("uiComponent referenced by attribute tableScroller@for must be of type " + UIData.class.getName());
        }
        return (UIData)forComp;
    }

}

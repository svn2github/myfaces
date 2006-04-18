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

package org.apache.myfaces.renderkit.html.ext;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.column.HtmlColumn;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;
import org.apache.myfaces.custom.crosstable.UIColumns;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase;
import org.apache.myfaces.renderkit.html.util.TableContext;
import org.apache.myfaces.renderkit.html.util.RowInfo;
import org.apache.myfaces.renderkit.html.util.ColumnInfo;

/**
 * Renderer for the Tomahawk extended HtmlDataTable component.
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTableRenderer extends HtmlTableRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTableRenderer.class);

    /** DetailStamp facet name. */
    public static final String DETAIL_STAMP_FACET_NAME = "detailStamp";


    protected void afterRow(FacesContext facesContext, UIData uiData) throws IOException {
        super.afterRow(facesContext, uiData);

        renderDetailRow(facesContext, uiData);
    }

    /**
     *
     * @param facesContext
     * @param uiData
     * @throws IOException
     */
    private void renderDetailRow(FacesContext facesContext, UIData uiData) throws IOException {
        UIComponent detailStampFacet = uiData.getFacet(DETAIL_STAMP_FACET_NAME);

        if(uiData instanceof HtmlDataTable ){
            HtmlDataTable htmlDataTable = (HtmlDataTable)uiData;

            if(htmlDataTable.isCurrentDetailExpanded()){
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.startElement(HTML.TR_ELEM,uiData);
                writer.startElement(HTML.TD_ELEM,uiData);
                writer.writeAttribute(HTML.COLSPAN_ATTR,new Integer(uiData.getChildren().size()) ,null);

                if(detailStampFacet!=null){
                    RendererUtils.renderChild(facesContext, detailStampFacet);
                }

                writer.endElement(HTML.TD_ELEM);
                writer.endElement(HTML.TR_ELEM);
            }
        }
    }

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        if (uiComponent instanceof HtmlDataTable)
        {
            HtmlDataTable htmlDataTable = (HtmlDataTable) uiComponent;
            if (htmlDataTable.isRenderedIfEmpty() || htmlDataTable.getRowCount() > 0)
            {
                super.encodeBegin(facesContext, uiComponent);
            }
        }
        else
        {
            super.encodeBegin(facesContext, uiComponent);
        }
    }

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
    {
        if (component instanceof HtmlDataTable)
        {
            HtmlDataTable htmlDataTable = (HtmlDataTable) component;
            if (htmlDataTable.isRenderedIfEmpty() || htmlDataTable.getRowCount() > 0)
            {
                super.encodeChildren(facesContext, component);
            }
        }
        else
        {
            super.encodeChildren(facesContext, component);
        }
    }



    private boolean isGroupedTable(UIData uiData)
    {
        if(uiData instanceof HtmlDataTable)
        {
            List children = getChildren(uiData);
            for (int j = 0, size = getChildCount(uiData); j < size; j++)
            {
                UIComponent child = (UIComponent) children.get(j);
                if(child instanceof HtmlSimpleColumn)
                {
                    HtmlSimpleColumn column = (HtmlSimpleColumn) child;
                    if(column.isGroupBy())return true;
                }
            }
        }

        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    protected void beforeBody(FacesContext facesContext, UIData uiData) throws IOException
    {

        if(isGroupedTable(uiData))
        {
            createColumnInfos((HtmlDataTable) uiData, facesContext);
        }
        super.beforeBody(facesContext,uiData);
    }

    private void createColumnInfos(HtmlDataTable htmlDataTable, FacesContext facesContext)
                throws IOException
        {
            int first = htmlDataTable.getFirst();
            int rows = htmlDataTable.getRows();
            int last;
            int currentRowSpan=-1;
            int currentRowInfoIndex=-1;

            TableContext tableContext=htmlDataTable.getTableContext();
            RowInfo rowInfo=null;
            ColumnInfo columnInfo=null;
            HtmlSimpleColumn currentColumn=null;
            Hashtable groupHashTable = new Hashtable();
            String currentColumnContent = null;

            if (rows <= 0)
            {
               last = htmlDataTable.getRowCount();
            }
            else
            {
               last = first + rows;
            }


            //Loop over the Children Columns to find the Columns with groupBy Attribute true
            List children = getChildren(htmlDataTable);
            int nChildren = getChildCount(htmlDataTable);

            for (int j = 0, size = nChildren; j < size; j++)
            {
                UIComponent child = (UIComponent) children.get(j);
                if(child instanceof HtmlSimpleColumn)
                {
                    currentColumn = (HtmlSimpleColumn) child;
                    if(currentColumn.isGroupBy())
                    {
                        groupHashTable.put(new Integer(j),"");
                    }
                }
            }

            boolean groupEndReached = false;

            for (int rowIndex = first; last==-1 || rowIndex < last; rowIndex++)
            {
               htmlDataTable.setRowIndex(rowIndex);
               rowInfo = new RowInfo();
               //scrolled past the last row
               if (!htmlDataTable.isRowAvailable())
                   break;

               Enumeration groupIndexList = groupHashTable.keys();
               while(groupIndexList.hasMoreElements())
               {
                   currentColumnContent = "";
                   Integer currentIndex=(Integer) groupIndexList.nextElement();
                   currentColumn = (HtmlSimpleColumn) children.get(currentIndex.intValue());
                   List currentColumnChildren = currentColumn.getChildren();
                   for (int j = 0, size = currentColumnChildren.size(); j < size; j++)
                    {
                        UIComponent currentColumnChild = (UIComponent) currentColumnChildren.get(j);
                        if(currentColumnChild.isRendered())
                        {
                            currentColumnContent += currentColumnChild.getValueBinding("value").getValue(facesContext);
                        }
                    }
                    if(currentColumnContent.compareTo((String)groupHashTable.get(currentIndex))!=0 && currentRowInfoIndex > -1)
                    {
                        groupEndReached = true;
                        groupHashTable.put(currentIndex,currentColumnContent);
                    }
                   else if(currentRowInfoIndex == -1)
                    {
                        groupHashTable.put(currentIndex,currentColumnContent);   
                    }
               }
               currentRowSpan++;


               for (int j = 0, size = nChildren; j < size; j++)
                    {
                        columnInfo = new ColumnInfo();
                        if(groupHashTable.containsKey(new Integer(j)))  // Column is groupBy
                        {
                            if(currentRowSpan > 0)
                            {
                                if(groupEndReached)
                                {
                                    ((ColumnInfo)
                                    ((RowInfo)
                                        tableContext.getRowInfos().get(currentRowInfoIndex-currentRowSpan+1)).
                                        getColumnInfos().get(j)).
                                        setRowSpan(currentRowSpan);
                                        columnInfo.setStyle(htmlDataTable.getRowGroupStyle());
                                        columnInfo.setStyleClass(htmlDataTable.getRowGroupStyleClass());
                                }
                                else
                                {
                                    columnInfo.setRendered(false);
                                }
                            }
                            else
                            {
                                columnInfo.setStyle(htmlDataTable.getRowGroupStyle());
                                columnInfo.setStyleClass(htmlDataTable.getRowGroupStyleClass());
                            }

                        }
                        else    // Column  is not group by
                        {
                            if(groupEndReached)
                            {
                                ((ColumnInfo)
                                    ((RowInfo)
                                        tableContext.getRowInfos().get(currentRowInfoIndex)).
                                        getColumnInfos().get(j)).
                                        setStyle(htmlDataTable.getRowGroupStyle());
                                ((ColumnInfo)
                                    ((RowInfo)
                                        tableContext.getRowInfos().get(currentRowInfoIndex)).
                                        getColumnInfos().get(j)).
                                        setStyleClass(htmlDataTable.getRowGroupStyleClass());
                            }
                        }
                        rowInfo.getColumnInfos().add(columnInfo);
                    }
                if(groupEndReached)
                {
                    currentRowSpan=0;
                    groupEndReached = false;
                }
                tableContext.getRowInfos().add(rowInfo);
                currentRowInfoIndex++;
            }
            for (int j = 0, size = nChildren; j < size; j++)
                    {
                        if(groupHashTable.containsKey(new Integer(j)))  // Column is groupBy
                        {
                                    ((ColumnInfo)
                                    ((RowInfo)
                                        tableContext.getRowInfos().get(currentRowInfoIndex-currentRowSpan)).
                                        getColumnInfos().get(j)).
                                        setRowSpan(currentRowSpan+1);
                        }
                        else    // Column  is not group by
                        {
                                ((ColumnInfo)
                                    ((RowInfo)
                                        tableContext.getRowInfos().get(currentRowInfoIndex)).
                                        getColumnInfos().get(j)).
                                        setStyle(htmlDataTable.getRowGroupStyle());
                                ((ColumnInfo)
                                    ((RowInfo)
                                        tableContext.getRowInfos().get(currentRowInfoIndex)).
                                        getColumnInfos().get(j)).
                                        setStyleClass(htmlDataTable.getRowGroupStyleClass());
                        }
                    }

            htmlDataTable.setRowIndex(-1);
        }


    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        if (uiComponent instanceof HtmlDataTable)
        {
            HtmlDataTable htmlDataTable = (HtmlDataTable) uiComponent;
            if (htmlDataTable.isRenderedIfEmpty() || htmlDataTable.getRowCount() > 0)
            {
                super.encodeEnd(facesContext, uiComponent);
            }
        }
        else
        {
            super.encodeEnd(facesContext, uiComponent);
        }
    }

    protected void renderRowStart(FacesContext facesContext,
                                  ResponseWriter writer, UIData uiData, Iterator rowStyleClassIterator)
                    throws IOException
    {
        super.renderRowStart(facesContext, writer, uiData, rowStyleClassIterator);

        // get event handlers from component
        HtmlDataTable table = (HtmlDataTable) uiData;

        renderRowAttribute(writer, HTML.ONCLICK_ATTR, table.getRowOnClick());
        renderRowAttribute(writer, HTML.ONDBLCLICK_ATTR, table.getRowOnDblClick());
        renderRowAttribute(writer, HTML.ONKEYDOWN_ATTR, table.getRowOnKeyDown());
        renderRowAttribute(writer, HTML.ONKEYPRESS_ATTR, table.getRowOnKeyPress());
        renderRowAttribute(writer, HTML.ONKEYUP_ATTR, table.getRowOnKeyUp());
        renderRowAttribute(writer, HTML.ONMOUSEDOWN_ATTR, table.getRowOnMouseDown());
        renderRowAttribute(writer, HTML.ONMOUSEMOVE_ATTR, table.getRowOnMouseMove());
        renderRowAttribute(writer, HTML.ONMOUSEOUT_ATTR, table.getRowOnMouseOut());
        renderRowAttribute(writer, HTML.ONMOUSEOVER_ATTR, table.getRowOnMouseOver());
        renderRowAttribute(writer, HTML.ONMOUSEUP_ATTR, table.getRowOnMouseUp());
    }

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#renderRowStyle(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, java.util.Iterator)
     */
    protected void renderRowStyle(FacesContext facesContext, ResponseWriter writer, UIData uiData, Iterator rowStyleIterator) throws IOException
    {
      String rowStyleClass;
      String rowStyle;
      if (uiData instanceof HtmlDataTable)
      {
        HtmlDataTable datatable = (HtmlDataTable) uiData;
        rowStyleClass = datatable.getRowStyleClass();
        rowStyle = datatable.getRowStyle();
      }
      else
      {
        rowStyleClass = (String) uiData.getAttributes().get(JSFAttr.ROW_STYLECLASS_ATTR);
        rowStyle = (String) uiData.getAttributes().get(JSFAttr.ROW_STYLE_ATTR);
      }
      if(rowStyleClass == null)
      {
        super.renderRowStyle(facesContext, writer, uiData, rowStyleIterator);
      }
      else
      {
        if(rowStyleIterator.hasNext())
        {
          // skip next row style
          rowStyleIterator.next();
        }
        writer.writeAttribute(HTML.CLASS_ATTR, rowStyleClass, null);
      }
      if(rowStyle != null)
      {
        writer.writeAttribute(HTML.STYLE_ATTR, rowStyle, null);
      }
    }

    protected void renderRowAttribute(ResponseWriter writer,
                                      String htmlAttribute, Object value) throws IOException
    {
        if (value != null)
        {
            writer.writeAttribute(htmlAttribute, value, null);
        }
    }

    /**
     * Render the specified column object using the current row data.
     * <p>
     * When the component is a UIColumn object, the inherited method is
     * invoked to render a single table cell.
     * <p>
     * In addition to the inherited functionality, support is implemented
     * here for UIColumns children. When a UIColumns child is encountered:
     * <pre>
     * For each dynamic column in that UIColumns child:
     *   * Select the column (which sets variable named by the var attribute
     *     to refer to the current column object) 
     *   * Call this.renderColumnBody passing the UIColumns object.
     * </pre>
     * The renderColumnBody method eventually:
     * <ul>
     * <li>emits TD
     * <li>calls encodeBegin on the UIColumns (which does nothing)
     * <li>calls rendering methods on all children of the UIColumns
     * <li>calls encodeEnd on the UIColumns (which does nothing)
     * <li> emits /TD
     * </ul>
     * If the children of the UIColumns access the variable named by the var
     * attribute on the UIColumns object, then they end up rendering content
     * that is extracted from the current column object.  
     * 
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#encodeColumnChild(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, javax.faces.component.UIComponent, java.util.Iterator)
     */
    protected void encodeColumnChild(FacesContext facesContext,
                                     ResponseWriter writer, UIData uiData,
                                     UIComponent component, Iterator columnStyleIterator)
                    throws IOException
    {
        super.encodeColumnChild(facesContext, writer, uiData, component,
                columnStyleIterator);
        if (component instanceof UIColumns)
        {
            UIColumns columns = (UIColumns) component;
            for (int k = 0, colSize = columns.getRowCount(); k < colSize; k++)
            {
                columns.setRowIndex(k);
                renderColumnBody(facesContext, writer, uiData, component,
                        columnStyleIterator);
            }
            columns.setRowIndex(-1);
        }
    }

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#renderColumnBody(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, javax.faces.component.UIComponent, java.util.Iterator)
     */
    protected void renderColumnBody(FacesContext facesContext,
                                    ResponseWriter writer, UIData uiData,
                                    UIComponent component, Iterator columnStyleIterator)
                    throws IOException
    {
        if (isGroupedTable(uiData)){

            HtmlDataTable htmlDataTable = (HtmlDataTable) uiData;
            List tableChildren = htmlDataTable.getChildren();

            int first = htmlDataTable.getFirst();
            int rowInfoIndex= htmlDataTable.getRowIndex()-first;
            int columnInfoIndex = tableChildren.indexOf(component);

            RowInfo rowInfo = (RowInfo) htmlDataTable.getTableContext().getRowInfos().get(rowInfoIndex);
            ColumnInfo columnInfo= (ColumnInfo) rowInfo.getColumnInfos().get(columnInfoIndex);

            if(!columnInfo.isRendered()) return;

            writer.startElement(HTML.TD_ELEM, uiData);
            String styleClass = ((HtmlColumn) component).getStyleClass();
            if(columnInfo.getStyleClass()!= null)
            {
                styleClass = columnInfo.getStyleClass();
            }

            if(columnStyleIterator.hasNext())
            {
              if (styleClass == null)
              {
                  styleClass = (String) columnStyleIterator.next();
              }
              else
              {
                // skip the column style class
                columnStyleIterator.next();
              }
            }
            if (styleClass != null)
            {
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            }

            if (columnInfo.getStyle() != null)
            {
                writer.writeAttribute(HTML.STYLE_ATTR, columnInfo.getStyle(), null);
            }

            if (columnInfo.getRowSpan() > 1)
            {
                writer.writeAttribute("rowspan", new Integer(columnInfo.getRowSpan()).toString(), null);
                if (columnInfo.getStyle() == null)
                {
                    writer.writeAttribute(HTML.STYLE_ATTR, "vertical-align:top", null);
                }
            }

            renderHtmlColumnAttributes(writer, component, null);

            RendererUtils.renderChild(facesContext, component);
            writer.endElement(HTML.TD_ELEM);
        }
        else if (component instanceof HtmlColumn)
        {
            writer.startElement(HTML.TD_ELEM, uiData);
            String styleClass = ((HtmlColumn) component).getStyleClass();
            if(columnStyleIterator.hasNext())
            {
              if (styleClass == null)
              {
                  styleClass = (String) columnStyleIterator.next();
              }
              else
              {
                // skip the column style class
                columnStyleIterator.next();
              }
            }
            if (styleClass != null)
            {
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            }
            renderHtmlColumnAttributes(writer, component, null);

            RendererUtils.renderChild(facesContext, component);
            writer.endElement(HTML.TD_ELEM);
        }
        else
        {
            super.renderColumnBody(facesContext, writer, uiData, component,
                    columnStyleIterator);
        }
    }

    /**
     * Render the header or footer of the specified column object.
     * <p>
     * When the component is a UIColumn object, the inherited method is
     * invoked to render a single header cell.
     * <p>
     * In addition to the inherited functionality, support is implemented
     * here for UIColumns children. When a UIColumns child is encountered:
     * <pre>
     * For each dynamic column in that UIColumns child:
     *   * Select the column (which sets variable named by the var attribute
     *     to refer to the current column object) 
     *   * Call this.renderColumnHeaderCell or this.renderColumnFooterCell
     *     passing the header or footer facet of the UIColumns object.
     * </pre>
     * If the facet of the UIColumns accesses the variable named by the var
     * attribute on the UIColumns object, then it ends up rendering content
     * that is extracted from the current column object.
     *   
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#renderColumnChildHeaderOrFooterRow(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, java.lang.String, boolean)
     */
    protected void renderColumnChildHeaderOrFooterRow(
                    FacesContext facesContext, ResponseWriter writer,
                    UIComponent uiComponent, String styleClass, boolean header)
                    throws IOException
    {
        super.renderColumnChildHeaderOrFooterRow(facesContext, writer,
                        uiComponent, styleClass, header);
        if (uiComponent instanceof UIColumns)
        {
            UIColumns columns = (UIColumns) uiComponent;
            for (int i = 0, size = columns.getRowCount(); i < size; i++)
            {
                columns.setRowIndex(i);
                if (header)
                {
                    renderColumnHeaderCell(facesContext, writer, columns,
                                    columns.getHeader(), styleClass, 0);
                }
                else
                {
                    renderColumnFooterCell(facesContext, writer, columns,
                                    columns.getFooter(), styleClass, 0);
                }
            }
            columns.setRowIndex(-1);
        }
    }

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#renderColumnHeaderCell(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, javax.faces.component.UIComponent, java.lang.String, int)
     */
    protected void renderColumnHeaderCell(FacesContext facesContext,
                                          ResponseWriter writer, UIComponent uiComponent,
                                          UIComponent facet, String headerStyleClass, int colspan)
                    throws IOException
    {
        if (uiComponent instanceof HtmlColumn)
        {
            writer.startElement(HTML.TH_ELEM, uiComponent);
            if (colspan > 1)
            {
                writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan),
                                null);
            }
            String styleClass = ((HtmlColumn) uiComponent)
                            .getHeaderstyleClass();
            if (styleClass == null)
            {
                styleClass = headerStyleClass;
            }
            if (styleClass != null)
            {
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            }
            renderHtmlColumnAttributes(writer, uiComponent, "header");
            if (facet != null)
            {
                RendererUtils.renderChild(facesContext, facet);
            }
            writer.endElement(HTML.TH_ELEM);
        }
        else
        {
            super.renderColumnHeaderCell(facesContext, writer, uiComponent,
                            facet, headerStyleClass, colspan);
        }
    }

    /**
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#renderColumnFooterCell(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, javax.faces.component.UIComponent, java.lang.String, int)
     */
    protected void renderColumnFooterCell(FacesContext facesContext,
                                          ResponseWriter writer, UIComponent uiComponent,
                                          UIComponent facet, String footerStyleClass, int colspan)
                    throws IOException
    {
        if (uiComponent instanceof HtmlColumn)
        {
            writer.startElement(HTML.TD_ELEM, uiComponent);
            if (colspan > 1)
            {
                writer.writeAttribute(HTML.COLSPAN_ATTR, new Integer(colspan),
                                null);
            }
            String styleClass = ((HtmlColumn) uiComponent)
                            .getFooterstyleClass();
            if (styleClass == null)
            {
                styleClass = footerStyleClass;
            }
            if (styleClass != null)
            {
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            }
            renderHtmlColumnAttributes(writer, uiComponent, "footer");
            if (facet != null)
            {
                RendererUtils.renderChild(facesContext, facet);
            }
            writer.endElement(HTML.TD_ELEM);
        }
        else
        {
            super.renderColumnFooterCell(facesContext, writer, uiComponent,
                            facet, footerStyleClass, colspan);
        }
    }

    protected void renderHtmlColumnAttributes(ResponseWriter writer,
                                              UIComponent uiComponent, String prefix) throws IOException
    {
        String[] attrs = HTML.COMMON_PASSTROUGH_ATTRIBUTES_WITHOUT_STYLE;
        for (int i = 0, size = attrs.length; i < size; i++)
        {
            String attributeName = attrs[i];
            String compAttrName = prefix != null ? prefix + attributeName : attributeName;
            HtmlRendererUtils.renderHTMLAttribute(writer, uiComponent,
                            compAttrName, attributeName);
        }
        String compAttrName = prefix != null ? prefix + HTML.STYLE_ATTR : HTML.STYLE_ATTR;
        HtmlRendererUtils.renderHTMLAttribute(writer, uiComponent,
                        compAttrName, HTML.STYLE_ATTR);

        HtmlRendererUtils.renderHTMLAttribute(writer, uiComponent,
                HTML.WIDTH_ATTR, HTML.WIDTH_ATTR);
    }

    /**
     * Return the number of columns spanned by the specified component.
     * <p>
     * For normal components, use the inherited implementation.
     * For UIColumns children, return the number of dynamic columns rendered
     * by that child.
     * 
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase
     *   #determineChildColSpan(javax.faces.component.UIComponent)
     */
    protected int determineChildColSpan(UIComponent uiComponent)
    {
        int result = super.determineChildColSpan(uiComponent);
        if (uiComponent instanceof UIColumns)
        {
            result += ((UIColumns) uiComponent).getRowCount();
        }
        return result;
    }

    /**
     * Return true if the specified component has a facet that needs to be
     * rendered in a THEAD or TFOOT section.
     * 
     * @see org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase#hasFacet(boolean, javax.faces.component.UIComponent)
     */
    protected boolean hasFacet(boolean header, UIComponent uiComponent)
    {
        boolean result = super.hasFacet(header, uiComponent);
        if (!result && uiComponent instanceof UIColumns)
        {
            // Why is this necessary? It seems to me that the inherited
            // implementation will work fine with a UIColumns component...
            UIColumns columns = (UIColumns) uiComponent;
            result = header ? columns.getHeader() != null : columns.getFooter() != null;
        }
        return result;
    }

    /**
     * Renders the column footer.
     * Rendering will be supressed if all of the facets have rendered="false"   
     */
    protected void renderColumnFooterRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, String footerStyleClass) throws IOException
    {
        if (determineRenderFacet(component, false))
        {
            super.renderColumnFooterRow(facesContext, writer, component, footerStyleClass);
        }
    }

    /**
     * Renders the column header.
     * Rendering will be supressed if all of the facets have rendered="false"   
     */
    protected void renderColumnHeaderRow(FacesContext facesContext, ResponseWriter writer, UIComponent component, String headerStyleClass) throws IOException
    {
        if (determineRenderFacet(component, true))
        {
            super.renderColumnHeaderRow(facesContext, writer, component, headerStyleClass);
        }
    }

    /**
     * determine if the header or footer should be rendered.
     */
    protected boolean determineRenderFacet(UIComponent component, boolean header)
    {
        for (Iterator it = getChildren(component).iterator(); it.hasNext();)
        {
            UIComponent uiComponent = (UIComponent) it.next();
            if(uiComponent.isRendered() && determineChildColSpan(uiComponent) > 0)
            {
                UIComponent facet = header ? (UIComponent) uiComponent.getFacets().get(HEADER_FACET_NAME)
                        : (UIComponent) uiComponent.getFacets().get(FOOTER_FACET_NAME);

                if (facet != null && facet.isRendered())
                {
                       return true;
                }
            }
        }

        return false;
    }
}

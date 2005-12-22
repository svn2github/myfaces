package org.apache.myfaces.renderkit.html.ext;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.column.HtmlColumn;
import org.apache.myfaces.custom.crosstable.UIColumns;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;
import org.apache.myfaces.renderkit.html.HtmlTableRendererBase;

/**
 * Renderer for the Tomahawk extended HtmlDataTable component.
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlTableRenderer extends HtmlTableRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTableRenderer.class);
  
    /**
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#encodeChildren(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
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

    /**
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#renderRowStyle(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, java.util.Iterator)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#encodeColumnChild(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, javax.faces.component.UIComponent, java.util.Iterator)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#renderColumnBody(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, javax.faces.component.UIComponent, java.util.Iterator)
     */
    protected void renderColumnBody(FacesContext facesContext,
                    ResponseWriter writer, UIData uiData,
                    UIComponent component, Iterator columnStyleIterator)
                    throws IOException
    {
        if (component instanceof HtmlColumn)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#renderColumnChildHeaderOrFooterRow(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, java.lang.String, boolean)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#renderColumnHeaderCell(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, javax.faces.component.UIComponent, java.lang.String, int)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#renderColumnFooterCell(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, javax.faces.component.UIComponent, java.lang.String, int)
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase
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
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#hasFacet(boolean, javax.faces.component.UIComponent)
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
}

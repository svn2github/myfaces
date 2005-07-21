package org.apache.myfaces.renderkit.html.ext;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */

import org.apache.myfaces.renderkit.html.HtmlTableRendererBase;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.crosstable.UIColumns;
import org.apache.myfaces.renderkit.html.HTML;

public class HtmlTableRenderer
        extends HtmlTableRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlTableRenderer.class);

    protected void renderRowStart(
            FacesContext facesContext,
            ResponseWriter writer,
            UIData uiData,
            String rowStyleClass) throws IOException
    {
        super.renderRowStart(facesContext, writer, uiData, rowStyleClass);

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

    protected void renderRowAttribute(ResponseWriter writer, String htmlAttribute, Object value) throws IOException
    {
      if(value != null)
      {
        writer.writeAttribute(htmlAttribute, value, null);
      }
    }

    /**
     * handles uicolumns component
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#encodeColumnChild(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIData, javax.faces.component.UIComponent, java.lang.String)
     */
    protected void encodeColumnChild(FacesContext facesContext, ResponseWriter writer,
                    UIData uiData, UIComponent component, String columnStyle) throws IOException
    {
        super.encodeColumnChild(facesContext, writer, uiData, component, columnStyle);
        if (component instanceof UIColumns)
        {
            UIColumns columns = (UIColumns) component;
            for (int k = 0, colSize = columns.getRowCount(); k < colSize; k++)
            {
                columns.setRowIndex(k);
                renderColumnBody(facesContext, writer, uiData, component, columnStyle);
            }
            columns.setRowIndex(-1);
        }
    }

    /**
     * handles uicolumns component
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#renderColumnChildHeaderOrFooterRow(javax.faces.context.FacesContext, javax.faces.context.ResponseWriter, javax.faces.component.UIComponent, java.lang.String, boolean)
     */
    protected void renderColumnChildHeaderOrFooterRow(FacesContext facesContext,
                    ResponseWriter writer, UIComponent uiComponent, String styleClass,
                    boolean header) throws IOException
    {
        super.renderColumnChildHeaderOrFooterRow(facesContext, writer, uiComponent, styleClass,
                        header);
        if (uiComponent instanceof UIColumns)
        {
            UIColumns columns = (UIColumns) uiComponent;
            for (int i = 0, size = columns.getRowCount(); i < size; i++)
            {
                columns.setRowIndex(i);
                if (header)
                {
                    renderColumnHeaderCell(facesContext, writer, columns, columns.getHeader(),
                                    styleClass, 0);
                }
                else
                {
                    renderColumnFooterCell(facesContext, writer, columns, columns.getFooter(),
                                    styleClass, 0);
                }
            }
            columns.setRowIndex(-1);
        }
    }

    /**
     * handles uicolumns component
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#determineChildColSpan(javax.faces.component.UIComponent)
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
     * handles uicolumns component
     * @see org.apache.myfaces.renderkit.html.HtmlTableRendererBase#hasFacet(boolean, javax.faces.component.UIComponent)
     */
    protected boolean hasFacet(boolean header, UIComponent uiComponent)
    {
        boolean result = super.hasFacet(header, uiComponent);
        if (!result && uiComponent instanceof UIColumns)
        {
            UIColumns columns = (UIColumns) uiComponent;
            result = header ? columns.getHeader() != null : columns.getFooter() != null;
        }
        return result;
    }
}

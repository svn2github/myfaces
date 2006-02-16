package org.apache.myfaces.custom.selectOneRow;

import org.apache.myfaces.renderkit.html.HtmlRenderer;
import org.apache.myfaces.renderkit.html.HTML;
import org.apache.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 15.02.2006
 * Time: 15:01:48
 * To change this template use File | Settings | File Templates.
 */
public class SelectOneRowRenderer extends HtmlRenderer
{
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.SelectOneRow";

    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException
    {
        if ((component instanceof SelectOneRow) && component.isRendered())
        {
            SelectOneRow row = (SelectOneRow) component;
            String clientId = row.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, row);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_RADIO, null);
        writer.writeAttribute(HTML.NAME_ATTR, row.getGroupName(), null);

        if (false) { // todo: disabled Attribute
            writer.writeAttribute(HTML.DISABLED_ATTR, HTML.DISABLED_ATTR, null);
        }

        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        if (isRowSelected(row))
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

            writer.writeAttribute(HTML.VALUE_ATTR, clientId, null);

        HtmlRendererUtils.renderHTMLAttributes(writer, row, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);

        writer.endElement(HTML.INPUT_ELEM);
        }
    }

 private boolean isRowSelected(UIComponent component) {
  UIInput input = (UIInput) component;
  Object value = input.getValue();

  int currentRowIndex = getCurrentRowIndex(component);

  return (value != null)
    && (currentRowIndex == ((Long) value).intValue());

 }

 private int getCurrentRowIndex(UIComponent component) {
  UIData uidata = findUIData(component);
  if (uidata == null)
   return -1;
  else
   return uidata.getRowIndex();
 }

 protected UIData findUIData(UIComponent uicomponent) {
  if (uicomponent == null)
   return null;
  if (uicomponent instanceof UIData)
   return (UIData) uicomponent;
  else
   return findUIData(uicomponent.getParent());
 }

 public void decode(FacesContext context, UIComponent uiComponent) {
  if(! (uiComponent instanceof SelectOneRow) )
  {
      return;
  }

  if (!uiComponent.isRendered()) {
   return;
  }
  SelectOneRow row = (SelectOneRow) uiComponent;

  Map requestMap = context.getExternalContext().getRequestParameterMap();
  String postedValue;

  if (requestMap.containsKey(row.getGroupName())) {
   postedValue = (String) requestMap.get(row.getGroupName());
   String clientId = row.getClientId(context);
   if (clientId.equals(postedValue)) {

    String[] postedValueArray = postedValue.split(":");
    String rowIndex = postedValueArray[postedValueArray.length - 2];

    Long newValue = Long.valueOf(rowIndex);
    //the value to go in conversion&validation
    row.setSubmittedValue(newValue);
    row.setValid(true);
   }
  }
 }
}

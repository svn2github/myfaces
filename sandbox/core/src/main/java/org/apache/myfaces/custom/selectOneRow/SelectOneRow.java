package org.apache.myfaces.custom.selectOneRow;

import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.util.Map;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 01.02.2006
 * Time: 15:55:59
 * To change this template use File | Settings | File Templates.
 */
public class SelectOneRow extends UIInput
{
    private String groupName;

 public static final String COMPONENT_TYPE = "org.apache.myfaces.SelectOneRow";

 public static final String COMPONENT_FAMILY = "org.apache.myfaces.SelectOneRow";

 public SelectOneRow() {
  setRendererType(null);

 }

 public String getFamily() {
  return COMPONENT_FAMILY;
 }

 public String getGroupName() {
  return groupName;
 }

 public void setGroupName(String groupName) {
  this.groupName = groupName;
 }

 public void restoreState(FacesContext context, Object state) {

  Object[] values = (Object[]) state;
  super.restoreState(context, values[0]);
  groupName = (String) values[1];

 }

 public Object saveState(FacesContext context) {
  Object[] values = new Object[2];
  values[0] = super.saveState(context);
  values[1] = groupName;
  return values;
 }

 public void encodeBegin(FacesContext context) throws IOException {

  if (!isRendered()) {
   return;
  }

  ResponseWriter writer = context.getResponseWriter();

  writer.write("<input class=\"selectOneRadio\" type=\"radio\" name=\"");
  writer.write(getGroupName());
  writer.write("\"");

  writer.write(" id=\"");
  String clientId = getClientId(context);
  writer.write(clientId);
  writer.write("\"");

  writer.write(" value=\"");
  writer.write(clientId);
  writer.write("\"");

  if (isRowSelected(this)) {
   writer.write(" checked");
  }

  writer.write(" \\>");

 }

 private boolean isRowSelected(UIComponent component) {
  UIInput input = (UIInput) component;
  Object value = input.getValue();

  int currentRowIndex = getCurrentRowIndex();

  return (value != null)
    && (currentRowIndex == ((Integer) value).intValue());

 }

 private int getCurrentRowIndex() {
  UIData uidata = findUIData(this);
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

 public void decode(FacesContext context) {

  if (!isRendered()) {
   return;
  }

  Map requestMap = context.getExternalContext().getRequestParameterMap();
  String postedValue;

  if (requestMap.containsKey(getGroupName())) {
   postedValue = (String) requestMap.get(getGroupName());
   String clientId = getClientId(context);
   if (clientId.equals(postedValue)) {

    String[] postedValueArray = postedValue.split(":");
    String rowIndex = postedValueArray[postedValueArray.length - 2];

    Integer newValue = Integer.valueOf(rowIndex);
    //the value to go in conversion&validation
    setSubmittedValue(newValue);
    setValid(true);
   }
  }
 }
}

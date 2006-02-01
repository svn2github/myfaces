package org.apache.myfaces.custom.selectOneRow;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.application.Application;

/**
 * Created by IntelliJ IDEA.
 * User: Ernst
 * Date: 01.02.2006
 * Time: 15:49:56
 * To change this template use File | Settings | File Templates.
 */
public class SelectOneRowTag extends UIComponentTag
{
    private String groupName;


 public String getValue() {
  return value;
 }

 public void setValue(String value) {
  this.value = value;
 }

 private String value;

 public String getComponentType() {

  return SelectOneRow.COMPONENT_TYPE;
 }

 public String getRendererType() {
  return null;
 }

 public String getGroupName() {
  return groupName;
 }

 public void setGroupName(String groupName) {
  this.groupName = groupName;
 }

 public void release() {
  super.release();
  groupName = null;
 }

 protected void setProperties(UIComponent component) {

  super.setProperties(component);
  UIInput singleInputRowSelect = (UIInput) component;
  singleInputRowSelect.getAttributes().put("groupName", groupName);

  if (getValue() != null) {

   if (isValueReference(getValue())) {
    FacesContext context = FacesContext.getCurrentInstance();
    Application app = context.getApplication();
    ValueBinding binding = app.createValueBinding(getValue());
    singleInputRowSelect.setValueBinding("value",binding);

   }

  }

 }
}

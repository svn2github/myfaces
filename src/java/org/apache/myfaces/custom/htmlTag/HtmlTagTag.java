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
package org.apache.myfaces.custom.htmlTag;

import javax.faces.component.UIComponent;
import org.apache.myfaces.taglib.html.HtmlOutputTextTagBase;
/**
 * @author bdudney (latest modification by $Author$)
 * @version $Revision$ $Date: 2005-05-11 11:47:12 -0400 (Wed, 11 May 2005) $
 */
public class HtmlTagTag extends HtmlOutputTextTagBase {
  private String _style = null;
  private String _styleClass = null;

  public String getComponentType() {
    return HtmlTag.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return HtmlTagRenderer.RENDERER_TYPE;
  }

  public void release() {
    super.release();
    this._style = null;
    this._styleClass = null;
  }

  /**
   * overrides setProperties() form UIComponentTag.
   */
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, "style", _style);
    setStringProperty(component, "styleClass", _styleClass);
  }

  public void setStyle(String style) {
    this._style = style;
  }

  public void setStyleClass(String styleClass) {
    this._styleClass = styleClass;
  }
}
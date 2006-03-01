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
package org.apache.myfaces.custom.buffer;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlComponentTagBase;
/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class BufferTag extends HtmlComponentTagBase {
  private String _into = null;

  public void release() {
      super.release();
      _into = null;
  }

  public String getComponentType() {
    return Buffer.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return BufferRenderer.RENDERER_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, "into", _into);
  }

  public void setInto(String into) {
    this._into = into;
  }
}
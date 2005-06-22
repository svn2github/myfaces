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
package org.apache.myfaces.custom.crosstable;

import javax.faces.component.UIData;

/**
 * @author Mathias Broekelmann (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIColumns extends UIData
{
  public static final String COMPONENT_TYPE = "org.apache.myfaces.Columns";
  public static final String COMPONENT_FAMILY = UIData.COMPONENT_FAMILY;

  /**
   *
   */
  public UIColumns()
  {
    super();
  }

  /**
   * @see javax.faces.component.UIComponentBase#getRendererType()
   */
  public String getRendererType()
  {
    return null;
  }

  /**
   * @see javax.faces.component.UIComponent#getFamily()
   */
  public String getFamily()
  {
    return COMPONENT_FAMILY;
  }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.custom.renderOne;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

/**
 * Tag that allows rendering the first child either by index or the first
 * rendered one.
 * <p>
 * A component that only renders the first child either by index or the first
 * visible one..
 * </p>
 * @JSFComponent
 *   name = "t:renderOne"
 *   class = "org.apache.myfaces.custom.renderOne.UIRenderOne"
 *   tagClass = "org.apache.myfaces.custom.renderOne.UIRenderOneTag"
 *   
 * @author Andrew Robinson (latest modification by $Author: skitching $)
 * @version $Revision: 676950 $ $Date: 2008-07-15 19:09:46 +0300 (Tue, 15 Jul 2008) $
 */
public abstract class AbstractUIRenderOne extends UIComponentBase
{
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    public static final String COMPONENT_TYPE = "org.apache.myfaces.UILimitRendered";
    private static final String RENDERONE_TYPE_FIRST = "first";
    private static final String RENDERONE_TYPE_INDEX = "index";
    private static final Integer FIRST_ITEM_INDEX = new Integer("0");
            
    /**
     * The filter type: first|index. first: the first list value should evaluate to a Number 
     * or a value that can be parsed into an integer. index: A collection, 
     * array or comma-separated list of numbers. (Default: "first")
     * 
     * @JSFProperty
     * @return the type
     */
    public abstract String getType();

    /**
     * @param type the type to set
     */
    public abstract void setType(String type);

    /**
     *  The value valid for the type. If this evaluates to null, the first child will 
     *  be rendered. 
     *  If the type is "first", this value will be ignored, as "first" always
     *  renders the first rendered item. 
     *  If the type is "index", the value must be a number.      
     *   
     * @JSFProperty
     * @return the value
     */
    public abstract Integer getValue();

    /**
     * @param value the value to set
     */
    public abstract void setValue(Integer value);
    
    /**
     * @see javax.faces.component.UIComponentBase#getRendersChildren()
     */
    public boolean getRendersChildren()
    {
        return true;
    }
    
    /**
     * @see javax.faces.component.UIComponentBase#encodeChildren(javax.faces.context.FacesContext)
     */
    public void encodeChildren(FacesContext context) throws IOException
    {
        if (!isRendered()) 
        {
            return;
        }

        RendererUtils.renderChild(context, (UIComponent)selectOneChild());
    }
    
    protected Object selectOneChild()
    {
      
      Integer value = getValue();
      String type = getType();
      
      // default is render by count.
      if (type == null) 
      {
          type = RENDERONE_TYPE_FIRST;
      }
      
      // render by index case.
      if (RENDERONE_TYPE_INDEX.equals(type)) 
      {
          
          // if value by index is not specified then the first element will be 
          // rendered only.
          if (value == null) 
          {
              value = FIRST_ITEM_INDEX;
          }
          
          // select the child by index.
          return selectFirstChildByIndex(value);
      } 
     
      // render by count case.
      if (RENDERONE_TYPE_FIRST.equals(type)) 
      {
          
          // select the first child that has rendered="true".
          return selectFirstChildOnly();
      } 
      else 
      {
          throw new IllegalArgumentException("type");
      }
    }
    
    protected Object selectFirstChildOnly()
    {
        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent child = (UIComponent)iter.next();
            if (child.isRendered())
            {
                return child;
            }
        }
        
        return null;
    }
        
    protected Object selectFirstChildByIndex(Integer value)
    {      
        return getChildren().get(value.intValue());
    }
}

/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.fisheye;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.div.DivTag;

/**
 * JSP Tag for the FishEyeList component
 * 
 * @author Jurgen Lust (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlFishEyeNavigationMenuTag extends DivTag
{
    private String _attachEdge;
    private String _conservativeTrigger;
    private String _effectUnits;
    private String _itemHeight;
    private String _itemMaxHeight;
    private String _itemMaxWidth;
    private String _itemPadding;
    private String _itemWidth;
    private String _labelEdge;
    private String _orientation;

    public String getAttachEdge()
    {
        return _attachEdge;
    }

    public String getComponentType()
    {
        return HtmlFishEyeNavigationMenu.COMPONENT_TYPE;
    }

    public String getConservativeTrigger()
    {
        return _conservativeTrigger;
    }

    public String getEffectUnits()
    {
        return _effectUnits;
    }

    public String getItemHeight()
    {
        return _itemHeight;
    }

    public String getItemMaxHeight()
    {
        return _itemMaxHeight;
    }

    public String getItemMaxWidth()
    {
        return _itemMaxWidth;
    }

    public String getItemPadding()
    {
        return _itemPadding;
    }

    public String getItemWidth()
    {
        return _itemWidth;
    }

    public String getLabelEdge()
    {
        return _labelEdge;
    }

    public String getOrientation()
    {
        return _orientation;
    }

    public String getRendererType()
    {
        return HtmlFishEyeNavigationMenuRenderer.RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
        _attachEdge = null;
        _effectUnits = null;
        _itemHeight = null;
        _itemMaxHeight = null;
        _itemMaxWidth = null;
        _itemPadding = null;
        _itemWidth = null;
        _labelEdge = null;
        _orientation = null;
    }

    public void setAttachEdge(String attachEdge)
    {
        this._attachEdge = attachEdge;
    }

    public void setConservativeTrigger(String conservativeTrigger)
    {
        this._conservativeTrigger = conservativeTrigger;
    }

    public void setEffectUnits(String effectUnits)
    {
        this._effectUnits = effectUnits;
    }

    public void setItemHeight(String itemHeight)
    {
        this._itemHeight = itemHeight;
    }

    public void setItemMaxHeight(String itemMaxHeight)
    {
        this._itemMaxHeight = itemMaxHeight;
    }

    public void setItemMaxWidth(String itemMaxWidth)
    {
        this._itemMaxWidth = itemMaxWidth;
    }

    public void setItemPadding(String itemPadding)
    {
        this._itemPadding = itemPadding;
    }

    public void setItemWidth(String itemWidth)
    {
        this._itemWidth = itemWidth;
    }

    public void setLabelEdge(String labelEdge)
    {
        this._labelEdge = labelEdge;
    }

    public void setOrientation(String orientation)
    {
        this._orientation = orientation;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        HtmlFishEyeNavigationMenu fisheye = (HtmlFishEyeNavigationMenu) component;
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        if (_attachEdge != null)
        {
            if (isValueReference(_attachEdge))
            {
                fisheye.setValueBinding("attachEdge", app
                        .createValueBinding(_attachEdge));
            }
            else
            {
                fisheye.setAttachEdge(_attachEdge);
            }
        }
        if (_effectUnits != null)
        {
            if (isValueReference(_effectUnits))
            {
                fisheye.setValueBinding("effectUnits", app
                        .createValueBinding(_effectUnits));
            }
            else
            {
                fisheye.setEffectUnits(new Integer(_effectUnits));
            }
        }
        if (_itemHeight != null)
        {
            if (isValueReference(_itemHeight))
            {
                fisheye.setValueBinding("itemHeight", app
                        .createValueBinding(_itemHeight));
            }
            else
            {
                fisheye.setItemHeight(new Integer(_itemHeight));
            }
        }
        if (_itemMaxHeight != null)
        {
            if (isValueReference(_itemMaxHeight))
            {
                fisheye.setValueBinding("itemMaxHeight", app
                        .createValueBinding(_itemMaxHeight));
            }
            else
            {
                fisheye.setItemMaxHeight(new Integer(_itemMaxHeight));
            }
        }
        if (_itemMaxWidth != null)
        {
            if (isValueReference(_itemMaxWidth))
            {
                fisheye.setValueBinding("itemMaxWidth", app
                        .createValueBinding(_itemMaxWidth));
            }
            else
            {
                fisheye.setItemMaxWidth(new Integer(_itemMaxWidth));
            }
        }
        if (_itemPadding != null)
        {
            if (isValueReference(_itemPadding))
            {
                fisheye.setValueBinding("itemPadding", app
                        .createValueBinding(_itemPadding));
            }
            else
            {
                fisheye.setItemPadding(new Integer(_itemPadding));
            }
        }
        if (_itemWidth != null)
        {
            if (isValueReference(_itemWidth))
            {
                fisheye.setValueBinding("itemWidth", app
                        .createValueBinding(_itemWidth));
            }
            else
            {
                fisheye.setItemWidth(new Integer(_itemWidth));
            }
        }
        if (_labelEdge != null)
        {
            if (isValueReference(_labelEdge))
            {
                fisheye.setValueBinding("labelEdge", app
                        .createValueBinding(_labelEdge));
            }
            else
            {
                fisheye.setLabelEdge(_labelEdge);
            }
        }
        if (_orientation != null)
        {
            if (isValueReference(_orientation))
            {
                fisheye.setValueBinding("orientation", app
                        .createValueBinding(_orientation));
            }
            else
            {
                fisheye.setOrientation(_orientation);
            }
        }
        if (_conservativeTrigger != null)
        {
            if (isValueReference(_conservativeTrigger))
            {
                fisheye.setValueBinding("conservativeTrigger", app
                        .createValueBinding(_conservativeTrigger));
            }
            else
            {
                fisheye.setConservativeTrigger(Boolean
                        .valueOf(_conservativeTrigger));
            }
        }
    }

}

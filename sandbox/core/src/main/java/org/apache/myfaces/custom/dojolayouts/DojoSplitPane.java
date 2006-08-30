/**
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

package org.apache.myfaces.custom.dojolayouts;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class DojoSplitPane extends DojoContentPane {
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.DojoSplitPane";
    public static final String COMPONENT_FAMILY = "javax.faces.Output";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.DojoSplitPaneRenderer";

    private String _splitOrientation = null;
    private Integer _sizerWidth = null;
    private Integer _activeSizing = null;

    public DojoSplitPane()
    {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSplitOrientationation(String orientation)
    {
        _splitOrientation = orientation;
    }

    public String getSplitOrientationation()
    {
        if (_splitOrientation != null) return _splitOrientation;
        ValueBinding vb = getValueBinding("orientation");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setSizerWidth(Integer sizerWidth)
    {
        _sizerWidth = sizerWidth;
    }

    public Integer getSizerWidth()
    {
        if (_sizerWidth != null) return _sizerWidth;
        ValueBinding vb = getValueBinding("sizerWidth");
        return vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
    }

    public void setActiveSizing(Integer activeSizing)
    {
        _activeSizing = activeSizing;
    }

    public Integer getActiveSizing()
    {
        if (_activeSizing != null) return _activeSizing;
        ValueBinding vb = getValueBinding("activeSizing");
        return vb != null ? (Integer)vb.getValue(getFacesContext()) : null;
    }



    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _splitOrientation;
        values[2] = _sizerWidth;
        values[3] = _activeSizing;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _splitOrientation = (String)values[1];
        _sizerWidth = (Integer)values[2];
        _activeSizing = (Integer)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}

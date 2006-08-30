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

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.div.Div;

public class DojoContentPane extends UIOutput {

    public static final String DEFAULT_COMPONENT_TYPE   = "org.apache.myfaces.DojoContentPane";

    public static final String DEFAULT_COMPONENT_FAMILY = "javax.faces.Output";

    public static final String DEFAULT_RENDERER_TYPE    = "org.apache.myfaces.DojoContentPaneRenderer";

    private Integer            _sizeShare               = null;

    private String             _style                   = null;

    private String             _styleClass              = null;

    public DojoContentPane() {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily() {
        return DEFAULT_COMPONENT_FAMILY;
    }

    public void setSizeShare(Integer sizeShare) {
        _sizeShare = sizeShare;
    }

    public void setStyle(String style) {
        _style = style;
    }

    public void setSTyleClass(String styleClass) {
        _styleClass = styleClass;
    }

    public Integer getSizeShare() {
        if (_sizeShare != null)
            return _sizeShare;
        ValueBinding vb = getValueBinding("sizeShare");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public String getStyle() {
        if (_style != null)
            return _style;
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getStyleClass() {
        if (_style != null)
            return _styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _sizeShare;
        values[2] = _style;
        values[3] = _styleClass;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _sizeShare = (Integer) values[1];
        _style = (String) values[2];
        _styleClass = (String) values[3];
    }
}

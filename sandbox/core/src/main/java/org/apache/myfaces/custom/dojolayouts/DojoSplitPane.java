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
    // ------------------ GENERATED CODE BEGIN (do not modify!)
    // --------------------

    public static final String COMPONENT_TYPE        = "org.apache.myfaces.DojoSplitPane";

    public static final String COMPONENT_FAMILY      = "javax.faces.Output";

    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.DojoSplitPaneRenderer";

    private String             _splitOrientation     = null;

    private Integer            _sizerWidth           = null;

    private Integer            _activeSizing         = null;

    private Boolean            _persist              = null;

    private Integer            _startPoint           = null;

    private Integer            _lastPoint            = null;


    public DojoSplitPane() {
        super();
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setSplitOrientationation(String orientation) {
        _splitOrientation = orientation;
    }

    public String getSplitOrientationation() {
        if (_splitOrientation != null)
            return _splitOrientation;
        ValueBinding vb = getValueBinding("orientation");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setSizerWidth(Integer sizerWidth) {
        _sizerWidth = sizerWidth;
    }

    public Integer getSizerWidth() {
        if (_sizerWidth != null)
            return _sizerWidth;
        ValueBinding vb = getValueBinding("sizerWidth");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setActiveSizing(Integer activeSizing) {
        _activeSizing = activeSizing;
    }

    public Integer getActiveSizing() {
        if (_activeSizing != null)
            return _activeSizing;
        ValueBinding vb = getValueBinding("activeSizing");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setPersist(Boolean persist) {
        _persist = persist;
    }

    public Boolean getPersist() {
        if (_persist != null)
            return _persist;
        ValueBinding vb = getValueBinding("persist");
        return vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
    }

    public void setLastPoint(Integer lastPoint) {
        _lastPoint = lastPoint;
    }

    public Integer getLastPoint() {
        if (_lastPoint != null)
            return _lastPoint;
        ValueBinding vb = getValueBinding("lastPoint");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }

    public void setStartPoint(Integer startPoint) {
        _startPoint = startPoint;
    }

    public Integer getStartPoint() {
        if (_startPoint != null)
            return _startPoint;
        ValueBinding vb = getValueBinding("startPoint");
        return vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
    }
    
    
    public Object saveState(FacesContext context) {
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = _splitOrientation;
        values[2] = _sizerWidth;
        values[3] = _activeSizing;
        values[4] = _persist;

        // //savestate startPoint begin
        values[5] = _startPoint;
        // //savestate startPoint end

        // //savestate lastPoint begin
        values[6] = _lastPoint;
        // //savestate lastPoint end

        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _splitOrientation = (String) values[1];
        _sizerWidth = (Integer) values[2];
        _activeSizing = (Integer) values[3];
        _persist = (Boolean) values[4];

        // //restorestate startPoint begin
        _startPoint = (Integer) values[5];
        // //restorestate startPoint end

        // //restorestate lastPoint begin
        _lastPoint = (Integer) values[6];
        // //restorestate lastPoint end

    }
    // ------------------ GENERATED CODE END
    // ---------------------------------------
}

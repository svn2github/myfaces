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

import javax.faces.component.UIComponent;

public class DojoSplitPaneTag extends DojoContentPaneTag {

    private static final String TAG_PARAM_ORIENTATION  = "orientation";

    private static final String TAG_PARAM_SIZERWIDTH   = "sizerwidth";

    private static final String TAG_PARAM_ACTIVESIZING = "activesizing";

    private String              _orientation           = null;

    private String              _sizerWidth            = null;

    private String              _activeSizing          = null;

    public void setActiveSizing(String activeSizing) {
        this._activeSizing = activeSizing;
    }

    public void setOrientation(String orientation) {
        this._orientation = orientation;
    }

    public void setSizerWidth(String sizerWidth) {
        this._sizerWidth = sizerWidth;
    }

    public String getComponentType() {
        return DojoSplitPane.COMPONENT_TYPE;
    }
    public String getRendererType() {
        return DojoSplitPane.DEFAULT_RENDERER_TYPE;
    }


    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        super.setIntegerProperty(component, TAG_PARAM_ACTIVESIZING, _activeSizing);
        super.setIntegerProperty(component, TAG_PARAM_ORIENTATION, _orientation);
        super.setIntegerProperty(component, TAG_PARAM_SIZERWIDTH, _sizerWidth);
    }

    public void release() {
        super.release();
        _activeSizing = null;
        _orientation = null;
        _sizerWidth = null;
    }
}

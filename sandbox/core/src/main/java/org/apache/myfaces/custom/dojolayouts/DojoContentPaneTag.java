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

import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlOutputTextTagBase;

public class DojoContentPaneTag extends HtmlOutputTextTagBase {

    private static final String TAG_PARAM_STYLE_CLASS = "styleClass";

    private static final String TAG_PARAM_STYLE       = "style";


    public static final String  TAG_PARAM_SIZESHARE   = "sizeShare";

    private String              _sizeShare            = null;

    private String              _style                = null;

    private String              _styleClass           = null;


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        super.setIntegerProperty(component, TAG_PARAM_SIZESHARE, _sizeShare);
        super.setStringProperty(component, TAG_PARAM_STYLE, _style);
        super.setStringProperty(component, TAG_PARAM_STYLE_CLASS, _styleClass);


    }

    public void release() {
        super.release();
        _style = null;
        _styleClass = null;
        _sizeShare = null;


    }

    public void setSizeShare(String sizeShare) {
        this._sizeShare = sizeShare;
    }

    public String getRendererType() {
        return DojoContentPane.DEFAULT_RENDERER_TYPE;
    }

    public String getComponentType() {
        return DojoContentPane.DEFAULT_COMPONENT_TYPE;
    }

    public void setStyle(String style) {
        this._style = style;
    }

    public void setStyleClass(String styleClass) {
        this._styleClass = styleClass;
    }


}

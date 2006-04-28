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
package org.apache.myfaces.custom.collapsiblemenu;

import org.apache.myfaces.taglib.html.ext.HtmlPanelGroupTag;

import javax.faces.component.UIComponent;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsibleMenuTag extends HtmlPanelGroupTag
{
    public String getComponentType()
    {
        return CollapsibleMenu.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return CollapsibleMenu.DEFAULT_RENDERER_TYPE;
    }

    private String _left;
    private String _top;
    private String _height;
    private String _width;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "left", _left);
        setStringProperty(component, "top", _top);
        setStringProperty(component, "height", _height);
        setStringProperty(component, "width", _width);
    }

    public void release()
    {
        super.release();
        _left = null;
        _top = null;
        _height = null;
        _width = null;
    }

    public String getHeight() {
        return _height;
    }
    public void setHeight(String height) {
        _height = height;
    }
    public String getLeft() {
        return _left;
    }
    public void setLeft(String left) {
        _left = left;
    }
    public String getTop() {
        return _top;
    }
    public void setTop(String top) {
        _top = top;
    }
    public String getWidth() {
        return _width;
    }
    public void setWidth(String width) {
        _width = width;
    }
}

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

import org.apache.myfaces.taglib.html.ext.HtmlCommandLinkTag;
import javax.faces.component.UIComponent;

/**
 * Adapted from the original component developed by Kevin Le (http://pragmaticobjects.com)
 * @author Sharath Reddy
 * @version $Revision$ $Date$
 */
public class CollapsibleIconTag extends HtmlCommandLinkTag
{
    private String _url;
    private String _title;
    private String _value;
    
    public void release() {
        super.release();

        _url = null;
        _title = null;
        _value= null;
    }

    public String getComponentType()
    {
        return CollapsibleIcon.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return CollapsibleIcon.DEFAULT_RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        
        setStringProperty(component, "title", _title);
        setStringProperty(component, "url", _url);
        setStringProperty(component, "value", _value);
    }
       
    public String getTitle() {
        return _title;
    }
    public void setTitle(String title) {
        this._title = title;
    }
    
    public String getUrl() {
        return _url;
    }
    
    public void setUrl(String url) {
        _url = url;
    }
    
    public String getValue() {
        return _value;
    }
    
    public void setValue(String value) {
        _value = value;
    }
}
